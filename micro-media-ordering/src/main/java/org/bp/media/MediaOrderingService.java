package org.bp.media;

import static org.apache.camel.model.rest.RestParamType.body;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.bp.media.model.OrderMediaRequest;
import org.bp.media.model.TVType;
import org.bp.media.exceptions.InternetServiceException;
import org.bp.media.exceptions.TVOrderException;
import org.bp.media.model.ExceptionResponse;
import org.bp.media.model.InternetBroadband;
import org.bp.media.model.OrderInfo;
import org.bp.media.model.Utils;
import org.bp.media.state.ProcessingEvent;
import org.bp.media.state.ProcessingState;
import org.bp.media.state.StateService;
import org.springframework.stereotype.Component;

@Component
public class MediaOrderingService extends RouteBuilder {

	@org.springframework.beans.factory.annotation.Autowired
	OrderIdentifierService orderIdentifierService;

	@org.springframework.beans.factory.annotation.Autowired
	PaymentService paymentService;

	@org.springframework.beans.factory.annotation.Autowired
	StateService tvStateService;

	@org.springframework.beans.factory.annotation.Autowired
	StateService internetStateService;

	@org.springframework.beans.factory.annotation.Value("${media.kafka.server}")
	private String mediaKafkaServer;

	@org.springframework.beans.factory.annotation.Value("${media.service.type}")
	private String mediaServiceType;

	@Override
	public void configure() throws Exception {
		if (mediaServiceType.equals("all") || mediaServiceType.equals("tv"))
			orderTVExceptionHandlers();
		if (mediaServiceType.equals("all") || mediaServiceType.equals("internet"))
			orderInternetExceptionHandlers();
		if (mediaServiceType.equals("all") || mediaServiceType.equals("gateway"))
			gateway();
		if (mediaServiceType.equals("all") || mediaServiceType.equals("tv"))
			tv();
		if (mediaServiceType.equals("all") || mediaServiceType.equals("internet"))
			internet();
		if (mediaServiceType.equals("all") || mediaServiceType.equals("payment"))
			payment();
	}

	private void gateway() {
		restConfiguration().component("servlet").bindingMode(RestBindingMode.json)
				.dataFormatProperty("prettyPrint", "true").enableCORS(true).contextPath("/api")
				// turn on swagger api-doc
				.apiContextPath("/api-doc").apiProperty("api.title", "Micro Media Ordering API")
				.apiProperty("api.version", "1.0.0");

		rest("/media").description("Micro Media Ordering REST service").consumes("application/json")
				.produces("application/json").post("/ordering").description("Order media services")
				.type(OrderMediaRequest.class).outType(OrderInfo.class).param().name("body").type(body)
				.description("Media services to order").endParam().responseMessage().code(200)
				.message("Media services successfully ordered").endResponseMessage().to("direct:orderMedia");

		from("direct:orderMedia").routeId("orderMedia").log("orderMedia fired").process((exchange) -> {
			exchange.getMessage().setHeader("orderMediaId", orderIdentifierService.getOrderIdentifier());
		}).to("direct:MediaOrderRequest").to("direct:orderRequester");

		from("direct:orderRequester").routeId("orderRequester").log("orderRequester fired").process((exchange) -> {
			exchange.getMessage().setBody(
					Utils.prepareOrderInfo(exchange.getMessage().getHeader("orderMediaId", String.class), null));
		});

		from("direct:MediaOrderRequest").routeId("MediaOrderRequest").log("brokerTopic fired").marshal().json()
				.to("kafka:MediaReqTopic?brokers=" + mediaKafkaServer + "&groupId=" + mediaServiceType);

	}

	private void tv() {
		from("kafka:MediaReqTopic?brokers=" + mediaKafkaServer + "&groupId=" + mediaServiceType).routeId("orderTV")
				.log("fired orderTV").unmarshal().json(JsonLibrary.Jackson, OrderMediaRequest.class)
				.process((exchange) -> {
					String mediaOrderId = exchange.getMessage().getHeader("orderMediaId", String.class);
					ProcessingState previousState = tvStateService.sendEvent(mediaOrderId, ProcessingEvent.START);
					if (previousState != ProcessingState.CANCELLED) {
						OrderInfo oi = new OrderInfo();
						oi.setId(orderIdentifierService.getOrderIdentifier());
						OrderMediaRequest omr = exchange.getMessage().getBody(OrderMediaRequest.class);
//TODO: cost handling
//					if (omr != null && omr.getTvService() != null && omr.getHotel().getCountry() != null) {
//						String country = omr.getHotel().getCountry();
//						if (country.equals("USA")) {
//							bi.setCost(new BigDecimal(999));
//						} else {
//							bi.setCost(new BigDecimal(888));
//						}
//					}
						if (omr.getTvService().getType().equals(TVType.SATELLITE)) {
							throw new TVOrderException("Not available TV service: " + omr.getTvService().getType());
						} else {
							oi.setCost(new BigDecimal(1200));
						}
						exchange.getMessage().setBody(oi);
						previousState = tvStateService.sendEvent(mediaOrderId, ProcessingEvent.FINISH);
					}
					exchange.getMessage().setHeader("previousState", previousState);
				}).marshal().json().to("stream:out").choice()
				.when(header("previousState").isEqualTo(ProcessingState.CANCELLED))
				.to("direct:orderTVCompensationAction").otherwise().setHeader("serviceType", constant("tv"))
				.to("kafka:OrderInfoTopic?brokers=" + mediaKafkaServer + "&groupId=" + mediaServiceType).endChoice();

		from("kafka:MediaOrderFailTopic?brokers=" + mediaKafkaServer + "&groupId=" + mediaServiceType)
				.routeId("orderTVCompensation").log("fired orderTVCompensation").unmarshal()
				.json(JsonLibrary.Jackson, ExceptionResponse.class).choice()
				.when(header("serviceType").isNotEqualTo("tv")).process((exchange) -> {
					String mediaOrderId = exchange.getMessage().getHeader("orderMediaId", String.class);
					ProcessingState previousState = tvStateService.sendEvent(mediaOrderId, ProcessingEvent.CANCEL);
					exchange.getMessage().setHeader("previousState", previousState);
				}).choice().when(header("previousState").isEqualTo(ProcessingState.FINISHED))
				.to("direct:orderTVCompensationAction").endChoice().endChoice();

		from("direct:orderTVCompensationAction").routeId("orderTVCompensationAction").log("fired orderTVCompensation")
				.to("stream:out");

	}

	private void internet() {
		from("kafka:MediaReqTopic?brokers=" + mediaKafkaServer + "&groupId=" + mediaServiceType)
				.routeId("orderInternet").log("fired orderInternet").unmarshal()
				.json(JsonLibrary.Jackson, OrderMediaRequest.class).process((exchange) -> {
					String mediaOrderId = exchange.getMessage().getHeader("orderMediaId", String.class);
					ProcessingState previousState = internetStateService.sendEvent(mediaOrderId, ProcessingEvent.START);
					if (previousState != ProcessingState.CANCELLED) {

						OrderInfo oi = new OrderInfo();
						oi.setId(orderIdentifierService.getOrderIdentifier());
						OrderMediaRequest omr = exchange.getMessage().getBody(OrderMediaRequest.class);
//					if (omr != null && omr.getFlight() != null && omr.getFlight().getFrom() != null
//							&& omr.getFlight().getFrom().getAirport() != null) {
//						String from = omr.getFlight().getFrom().getAirport();
//						if (from.equals("JFK")) {
//							oi.setCost(new BigDecimal(700));
//						} else {
//							oi.setCost(new BigDecimal(600));
//						}
//					}
						if (omr.getInternetService().getBroadband().equals(InternetBroadband.FIBRE_OPTIC)) {
							throw new InternetServiceException(
									"Not available broadband: " + omr.getInternetService().getBroadband());
						} else {
							oi.setCost(new BigDecimal(900));
						}
						exchange.getMessage().setBody(oi);
						previousState = internetStateService.sendEvent(mediaOrderId, ProcessingEvent.FINISH);
					}
					exchange.getMessage().setHeader("previousState", previousState);
				}).marshal().json().to("stream:out").choice()
				.when(header("previousState").isEqualTo(ProcessingState.CANCELLED))
				.to("direct:orderInternetCompensationAction").otherwise().setHeader("serviceType", constant("internet"))
				.to("kafka:OrderInfoTopic?brokers=" + mediaKafkaServer + "&groupId=" + mediaServiceType).endChoice();

		from("kafka:MediaOrderFailTopic?brokers=" + mediaKafkaServer + "&groupId=" + mediaServiceType)
				.routeId("orderInternetCompensation").log("fired orderInternetCompensation").unmarshal()
				.json(JsonLibrary.Jackson, ExceptionResponse.class).choice()
				.when(header("serviceType").isNotEqualTo("internet")).process((exchange) -> {
					String mediaOrderId = exchange.getMessage().getHeader("orderMediaId", String.class);
					ProcessingState previousState = internetStateService.sendEvent(mediaOrderId,
							ProcessingEvent.CANCEL);
					exchange.getMessage().setHeader("previousState", previousState);
				}).choice().when(header("previousState").isEqualTo(ProcessingState.FINISHED))
				.to("direct:orderInternetCompensationAction").endChoice().endChoice();

		from("direct:orderInternetCompensationAction").routeId("orderInternetCompensationAction")
				.log("fired orderInternetCompensationAction").to("stream:out");
	}

	private void payment() {
		from("kafka:OrderInfoTopic?brokers=" + mediaKafkaServer + "&groupId=" + mediaServiceType)
				.routeId("paymentOrderInfo").log("fired paymentOrderInfo").unmarshal()
				.json(JsonLibrary.Jackson, OrderInfo.class).process((exchange) -> {
					String mediaOrderId = exchange.getMessage().getHeader("orderMediaId", String.class);
					boolean isReady = paymentService.addOrderInfo(mediaOrderId,
							exchange.getMessage().getBody(OrderInfo.class),
							exchange.getMessage().getHeader("serviceType", String.class));
					exchange.getMessage().setHeader("isReady", isReady);
				}).choice().when(header("isReady").isEqualTo(true)).to("direct:finalizePayment").endChoice();

		from("kafka:MediaReqTopic?brokers=" + mediaKafkaServer + "&groupId=" + mediaServiceType)
				.routeId("paymentOrderReq").log("fired paymentOrderReq").unmarshal()
				.json(JsonLibrary.Jackson, OrderMediaRequest.class).process((exchange) -> {
					String mediaOrderId = exchange.getMessage().getHeader("orderMediaId", String.class);
					boolean isReady = paymentService.addOrderMediaRequest(mediaOrderId,
							exchange.getMessage().getBody(OrderMediaRequest.class));
					exchange.getMessage().setHeader("isReady", isReady);
				}).choice().when(header("isReady").isEqualTo(true)).to("direct:finalizePayment").endChoice();

		from("direct:finalizePayment").routeId("finalizePayment").log("fired finalizePayment").process((exchange) -> {
			String mediaOrderId = exchange.getMessage().getHeader("orderMediaId", String.class);
			PaymentService.PaymentData paymentData = paymentService.getPaymentData(mediaOrderId);
			BigDecimal tvCost = paymentData.tvOrderInfo.getCost();
			BigDecimal internetCost = paymentData.internetOrderInfo.getCost();
			BigDecimal totalCost = tvCost.add(internetCost);
			OrderInfo mediaOrderInfo = new OrderInfo();
			mediaOrderInfo.setId(mediaOrderId);
			mediaOrderInfo.setCost(totalCost);
			exchange.getMessage().setBody(mediaOrderInfo);
		}).to("direct:notification");

		from("direct:notification").routeId("notification").log("fired notification").to("stream:out");
	}

	private void orderTVExceptionHandlers() {
		onException(TVOrderException.class).process((exchange) -> {
			ExceptionResponse er = new ExceptionResponse();
			er.setTimestamp(OffsetDateTime.now());
			Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
			er.setMessage(cause.getMessage());
			exchange.getMessage().setBody(er);
		}).marshal().json().to("stream:out").setHeader("serviceType", constant("tv"))
				.to("kafka:MediaOrderFailTopic?brokers=" + mediaKafkaServer + "&groupId=" + mediaServiceType)
				.handled(true);
	}

	private void orderInternetExceptionHandlers() {
		onException(InternetServiceException.class).process((exchange) -> {
			ExceptionResponse er = new ExceptionResponse();
			er.setTimestamp(OffsetDateTime.now());
			Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
			er.setMessage(cause.getMessage());
			exchange.getMessage().setBody(er);
		}).marshal().json().to("stream:out").setHeader("serviceType", constant("internet"))
				.to("kafka:MediaOrderFailTopic?brokers=" + mediaKafkaServer + "&groupId=" + mediaServiceType)
				.handled(true);
	}

}
