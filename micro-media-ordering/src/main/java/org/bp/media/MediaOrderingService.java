package org.bp.media;

import static org.apache.camel.model.rest.RestParamType.body;

import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.HashMap;

import javax.xml.namespace.QName;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.language.bean.Bean;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.apache.camel.util.json.JsonObject;
import org.bp.media.model.OrderMediaRequest;
import org.bp.media.model.OrderSummary;
import org.bp.media.model.PaymentRequest;
import org.bp.media.model.PaymentResponse;
import org.bp.media.model.TVOrder;
import org.bp.media.model.TVType;
import org.bp.media.model.Internet_Fault_Exception;
import org.bp.media.clients.OrderInternetService;
import org.bp.media.clients.OrderInternetServiceService;
import org.bp.media.clients.OrderTvService;
import org.bp.media.clients.OrderTvServiceService;
import org.bp.media.exceptions.InternetServiceException;
import org.bp.media.exceptions.TVOrderException;
import org.bp.media.model.Amount;
import org.bp.media.model.ExceptionResponse;
import org.bp.media.model.TV_Fault_Exception;
import org.bp.media.model.Household;
import org.bp.media.model.InternetBroadband;
import org.bp.media.model.InternetOrder;
import org.bp.media.model.OrderInfo;
import org.bp.media.model.Utils;
import org.bp.media.state.ProcessingEvent;
import org.bp.media.state.ProcessingState;
import org.bp.media.state.StateService;
import org.bp.media.model.PaymentCard;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import io.swagger.util.Json;

@Component
public class MediaOrderingService extends RouteBuilder {

	@org.springframework.context.annotation.Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@org.springframework.beans.factory.annotation.Autowired
	RestTemplate restTemplate;

	HashMap<String, OrderSummary> orders = new HashMap<>();
	HashMap<String, Integer> tvOrdersMappings = new HashMap<>();
	HashMap<String, Integer> internetOrdersMappings = new HashMap<>();
	HashMap<String, Integer> paymentTransactionsMappings = new HashMap<>();
	HashMap<String, OrderMediaRequest> orderRequests = new HashMap<>();

	@org.springframework.beans.factory.annotation.Autowired
	OrderIdentifierService orderIdentifierService;

	@org.springframework.beans.factory.annotation.Autowired
	OrderGetterService orderGetterService;

	@org.springframework.beans.factory.annotation.Autowired
	PaymentService paymentService;

	@org.springframework.beans.factory.annotation.Autowired
	StateService tvStateService;

	@org.springframework.beans.factory.annotation.Autowired
	StateService internetStateService;

	@org.springframework.beans.factory.annotation.Value("${media.tv.server}")
	private String mediaTVServer;

	@org.springframework.beans.factory.annotation.Value("${media.internet.server}")
	private String mediaInternetServer;

	@org.springframework.beans.factory.annotation.Value("${media.payment.server}")
	private String mediaPaymentServer;

	@Override
	public void configure() throws Exception {
		orderTVExceptionHandlers();
		orderInternetExceptionHandlers();
		gateway();
		tv();
		internet();
		payment();
	}

	private void gateway() {
		restConfiguration().component("servlet").bindingMode(RestBindingMode.json)
				.dataFormatProperty("prettyPrint", "true").enableCORS(true).contextPath("/api")
				.apiContextPath("/api-doc").apiProperty("api.title", "Micro Media Ordering API")
				.apiProperty("api.version", "1.0.0");

		rest("/media").description("Micro Media Ordering REST service").consumes("application/json")
				.produces("application/json").post("/order").description("Order media services")
				.type(OrderMediaRequest.class).outType(OrderSummary.class).param().name("body").type(body)
				.description("Media services to order").endParam().responseMessage().code(200)
				.message("Media services successfully ordered").endResponseMessage().to("direct:orderMedia");

		rest("/media").produces("application/json").get("/order/{orderId}").description("Get media order info")
				.outType(OrderSummary.class).param().name("orderId").type(RestParamType.path)
				.description("Media order ID to get").endParam().responseMessage().code(200)
				.message("Media services successfully get").endResponseMessage().to("direct:getOrders");

		from("direct:orderMedia").routeId("orderMedia").log("orderMedia fired").process((exchange) -> {
			exchange.getMessage().setHeader("orderMediaId", orderIdentifierService.getOrderIdentifier());
		}).to("direct:MediaOrderRequest");

		from("direct:getOrders").routeId("getOrders").log("getOrders fired").to("direct:MediaGetRequest");

		from("direct:MediaOrderRequest").routeId("MediaOrderRequest").log("brokerTopic fired").marshal().json()
				.multicast()
				.stopOnException()
				.to("direct:orderTV", "direct:orderInternet", "direct:registerPayment");

		from("direct:MediaGetRequest").routeId("MediaGetRequest").log("brokerTopic fired").marshal().json().multicast()
				.parallelProcessing().to("direct:getTVOrder", "direct:getInternetOrder", "direct:getPaymentInfo");

		from("direct:returnOrderSummary").routeId("returnOrderSummary").process((exchange) -> {
			String mediaOrderId = exchange.getMessage().getHeader("orderMediaId", String.class);
			OrderSummary orderSummary = orderGetterService.getOrderSummary(mediaOrderId);
			exchange.getMessage().setBody(orderSummary);
		}).marshal().json().log("Order created").to("stream:out").unmarshal().json(JsonLibrary.Jackson,
				OrderSummary.class);

		from("direct:GetOrdersTopic").routeId("gatherMediaOrders").log("fired gatherMediaOrders").unmarshal()
				.json(JsonLibrary.Jackson, OrderInfo.class).process((exchange) -> {
					String mediaOrderId = exchange.getMessage().getHeader("orderId", String.class);
					boolean isReady = orderGetterService.addOrderInfo(mediaOrderId,
							exchange.getMessage().getBody(OrderInfo.class),
							exchange.getMessage().getHeader("serviceType", String.class));
					exchange.getMessage().setHeader("isReady", isReady);
				}).choice().when(header("isReady").isEqualTo(true)).to("direct:gatherOrders").endChoice();

		from("direct:GetPaymentTopic").routeId("gatherOrdersPayment").log("fired gatherOrdersPayment").unmarshal()
				.json(JsonLibrary.Jackson, PaymentResponse.class).process((exchange) -> {
					String mediaOrderId = exchange.getMessage().getHeader("orderId", String.class);
					boolean isReady = orderGetterService.addPaymentInfo(mediaOrderId,
							exchange.getMessage().getBody(PaymentResponse.class));
					exchange.getMessage().setHeader("isReady", isReady);
				}).choice().when(header("isReady").isEqualTo(true)).to("direct:gatherOrders").endChoice();

		from("direct:gatherOrders").routeId("gatherOrders").process((exchange) -> {
			String mediaOrderId = exchange.getMessage().getHeader("orderId", String.class);
			OrderSummary orderSummary = new OrderSummary();
			orderSummary.setId(mediaOrderId);
			orderSummary.setTvOrderInfo(orderGetterService.getOrderSummary(mediaOrderId).getTvOrderInfo());
			orderSummary.setInternetOrderInfo(orderGetterService.getOrderSummary(mediaOrderId).getInternetOrderInfo());
			orderSummary.setPaymentInfo(orderGetterService.getOrderSummary(mediaOrderId).getPaymentInfo());
			exchange.getMessage().setBody(orderSummary);
		}).marshal().json().log("Order returned").to("stream:out").unmarshal().json(JsonLibrary.Jackson,
				OrderSummary.class);
	}

	private void tv() {
		from("direct:orderTV").routeId("orderTV").log("fired orderTV").unmarshal()
				.json(JsonLibrary.Jackson, OrderMediaRequest.class).process((exchange) -> {
					String mediaOrderId = exchange.getMessage().getHeader("orderMediaId", String.class);
					ProcessingState previousState = tvStateService.sendEvent(mediaOrderId, ProcessingEvent.START);
					if (previousState != ProcessingState.CANCELLED) {
						OrderMediaRequest omr = exchange.getMessage().getBody(OrderMediaRequest.class);
						URL wsdlURL = new URL("http://" + mediaTVServer + "/soap-api/service/tv?wsdl");

						QName SERVICE_NAME = new QName("http://tv.bp.org/", "OrderTvServiceService");
						OrderTvServiceService ss = new OrderTvServiceService(wsdlURL, SERVICE_NAME);
						OrderTvService port = ss.getOrderTvServicePort();

						TVOrder tvOrder = new TVOrder();
						tvOrder.setHousehold(omr.getHousehold());
						tvOrder.setTVService(omr.getTvService());
						tvOrder.setInstallmentDate(omr.getTvInstallmentDate());
						OrderInfo orderInfo = new OrderInfo();
						try {
							orderInfo = port.orderTV(tvOrder);
							System.out.println("orderTV.result=" + orderInfo);

						} catch (TV_Fault_Exception e) {
							System.out.println("Expected exception: Fault has occurred.");
							System.out.println(e.toString());
						}
						tvOrdersMappings.put(mediaOrderId, Integer.valueOf(orderInfo.getId()));
						orderGetterService.addOrderInfo(mediaOrderId, orderInfo, "tv");
						exchange.getMessage().setBody(orderInfo);
						previousState = tvStateService.sendEvent(mediaOrderId, ProcessingEvent.FINISH);
					}
					exchange.getMessage().setHeader("previousState", previousState);
				}).marshal().json().to("stream:out").choice()
				.when(header("previousState").isEqualTo(ProcessingState.CANCELLED))
				.to("direct:orderTVCompensationAction").otherwise().setHeader("serviceType", constant("tv"))
				.to("direct:OrderInfoTopic").endChoice();

		from("direct:TVOrderFail").routeId("orderTVCompensation").log("fired orderTVCompensation").unmarshal()
				.json(JsonLibrary.Jackson, ExceptionResponse.class).choice()
				.when(header("serviceType").isNotEqualTo("tv")).process((exchange) -> {
					String mediaOrderId = exchange.getMessage().getHeader("orderMediaId", String.class);
					ProcessingState previousState = tvStateService.sendEvent(mediaOrderId, ProcessingEvent.CANCEL);
					exchange.getMessage().setHeader("previousState", previousState);
				}).choice().when(header("previousState").isEqualTo(ProcessingState.FINISHED))
				.to("direct:orderTVCompensationAction").endChoice().endChoice();

		from("direct:orderTVCompensationAction").routeId("orderTVCompensationAction").log("fired orderTVCompensation")
				.to("stream:out");

		from("direct:getTVOrder").routeId("getOrderTV").log("fired getOrderTV").process((exchange) -> {
			String mediaOrderId = exchange.getMessage().getHeader("orderId", String.class);
			URL wsdlURL = new URL("http://" + mediaTVServer + "/soap-api/service/tv?wsdl");

			QName SERVICE_NAME = new QName("http://tv.bp.org/", "OrderTvServiceService");
			OrderTvServiceService ss = new OrderTvServiceService(wsdlURL, SERVICE_NAME);
			OrderTvService port = ss.getOrderTvServicePort();

			OrderInfo orderInfo = new OrderInfo();
			orderInfo.setId(String.valueOf(tvOrdersMappings.get(mediaOrderId)));
			try {
				orderInfo = port.getOrderInfo(orderInfo);
				System.out.println("getTVOrder.result=" + orderInfo);

			} catch (TV_Fault_Exception e) {
				System.out.println("Expected exception: Fault has occurred.");
				System.out.println(e.toString());
			}
			exchange.getMessage().setBody(orderInfo);
		}).marshal().json().to("stream:out").setHeader("serviceType", constant("tv")).to("direct:GetOrdersTopic");
	}

	private void internet() {
		from("direct:orderInternet").routeId("orderInternet").log("fired orderInternet").unmarshal()
				.json(JsonLibrary.Jackson, OrderMediaRequest.class).process((exchange) -> {
					String mediaOrderId = exchange.getMessage().getHeader("orderMediaId", String.class);
					ProcessingState previousState = internetStateService.sendEvent(mediaOrderId, ProcessingEvent.START);
					if (previousState != ProcessingState.CANCELLED) {
						OrderMediaRequest omr = exchange.getMessage().getBody(OrderMediaRequest.class);
						URL wsdlURL = new URL("http://" + mediaInternetServer + "/soap-api/service/internet?wsdl");

						QName SERVICE_NAME = new QName("http://internet.bp.org/", "OrderInternetServiceService");
						OrderInternetServiceService ss = new OrderInternetServiceService(wsdlURL, SERVICE_NAME);
						OrderInternetService port = ss.getOrderInternetServicePort();

						InternetOrder internetOrder = new InternetOrder();
						internetOrder.setHousehold(omr.getHousehold());
						internetOrder.setInternetService(omr.getInternetService());
						internetOrder.setInstallmentDate(omr.getInternetInstallmentDate());
						OrderInfo orderInfo = new OrderInfo();
						try {
							orderInfo = port.orderInternet(internetOrder);
							System.out.println("orderInternet.result=" + orderInfo);

						} catch (Internet_Fault_Exception e) {
							System.out.println("Expected exception: Fault has occurred.");
							System.out.println(e.toString());
						}

						internetOrdersMappings.put(mediaOrderId, Integer.valueOf(orderInfo.getId()));
						orderGetterService.addOrderInfo(mediaOrderId, orderInfo, "internet");
						exchange.getMessage().setBody(orderInfo);
						previousState = internetStateService.sendEvent(mediaOrderId, ProcessingEvent.FINISH);
					}
					exchange.getMessage().setHeader("previousState", previousState);
				}).marshal().json().to("stream:out").choice()
				.when(header("previousState").isEqualTo(ProcessingState.CANCELLED))
				.to("direct:orderInternetCompensationAction").otherwise().setHeader("serviceType", constant("internet"))
				.to("direct:OrderInfoTopic").endChoice();

		from("direct:InternetOrderFail").routeId("orderInternetCompensation").log("fired orderInternetCompensation")
				.unmarshal().json(JsonLibrary.Jackson, ExceptionResponse.class).choice()
				.when(header("serviceType").isNotEqualTo("internet")).process((exchange) -> {
					String mediaOrderId = exchange.getMessage().getHeader("orderMediaId", String.class);
					ProcessingState previousState = internetStateService.sendEvent(mediaOrderId,
							ProcessingEvent.CANCEL);
					exchange.getMessage().setHeader("previousState", previousState);
				}).choice().when(header("previousState").isEqualTo(ProcessingState.FINISHED))
				.to("direct:orderInternetCompensationAction").endChoice().endChoice();

		from("direct:orderInternetCompensationAction").routeId("orderInternetCompensationAction")
				.log("fired orderInternetCompensationAction").to("stream:out");

		from("direct:getInternetOrder").routeId("getOrderInternet").log("fired getOrderInternet")
				.process((exchange) -> {
					String mediaOrderId = exchange.getMessage().getHeader("orderId", String.class);
					URL wsdlURL = new URL("http://" + mediaInternetServer + "/soap-api/service/internet?wsdl");

					QName SERVICE_NAME = new QName("http://internet.bp.org/", "OrderInternetServiceService");
					OrderInternetServiceService ss = new OrderInternetServiceService(wsdlURL, SERVICE_NAME);
					OrderInternetService port = ss.getOrderInternetServicePort();

					OrderInfo orderInfo = new OrderInfo();
					orderInfo.setId(String.valueOf(internetOrdersMappings.get(mediaOrderId)));
					try {
						orderInfo = port.getOrderInfo(orderInfo);
						System.out.println("getInternetOrder.result=" + orderInfo);

					} catch (Internet_Fault_Exception e) {
						System.out.println("Expected exception: Fault has occurred.");
						System.out.println(e.toString());
					}
					exchange.getMessage().setBody(orderInfo);
				}).marshal().json().to("stream:out").setHeader("serviceType", constant("internet"))
				.to("direct:GetOrdersTopic");
	}

	private void payment() {
		from("direct:OrderInfoTopic").routeId("paymentOrderInfo").log("fired paymentOrderInfo").unmarshal()
				.json(JsonLibrary.Jackson, OrderInfo.class).process((exchange) -> {
					String mediaOrderId = exchange.getMessage().getHeader("orderMediaId", String.class);
					boolean isReady = paymentService.addOrderInfo(mediaOrderId,
							exchange.getMessage().getBody(OrderInfo.class),
							exchange.getMessage().getHeader("serviceType", String.class));
					exchange.getMessage().setHeader("isReady", isReady);
				}).choice().when(header("isReady").isEqualTo(true)).to("direct:finalizePayment").endChoice();

		from("direct:registerPayment").routeId("paymentOrderReq").log("fired paymentOrderReq").unmarshal()
				.json(JsonLibrary.Jackson, OrderMediaRequest.class).process((exchange) -> {
					String mediaOrderId = exchange.getMessage().getHeader("orderMediaId", String.class);
					boolean isReady = paymentService.addOrderMediaRequest(mediaOrderId,
							exchange.getMessage().getBody(OrderMediaRequest.class));
					exchange.getMessage().setHeader("isReady", isReady);
					OrderMediaRequest omr = exchange.getMessage().getBody(OrderMediaRequest.class);
					orderRequests.putIfAbsent(mediaOrderId, omr);
				}).choice().when(header("isReady").isEqualTo(true)).to("direct:finalizePayment").endChoice();

		from("direct:finalizePayment").routeId("finalizePayment").log("fired finalizePayment").process((exchange) -> {
			String mediaOrderId = exchange.getMessage().getHeader("orderMediaId", String.class);
			OrderMediaRequest omr = orderRequests.get(mediaOrderId);
			PaymentService.PaymentData paymentData = paymentService.getPaymentData(mediaOrderId);
			BigDecimal tvCost = paymentData.tvOrderInfo.getCost();
			BigDecimal internetCost = paymentData.internetOrderInfo.getCost();
			BigDecimal totalCost = tvCost.add(internetCost);

			PaymentRequest paymentRequest = new PaymentRequest();
			paymentRequest.setPaymentCard(omr.getPaymentCard());
			Amount amount = new Amount();
			paymentRequest.setAmount(amount);
			amount.setValue(totalCost);
			amount.setCurrency("PLN");
			PaymentResponse paymentResponse = new PaymentResponse();
			try {
				ResponseEntity<PaymentResponse> re = restTemplate.postForEntity(
						"http://" + mediaPaymentServer + "/payment", paymentRequest, PaymentResponse.class);
				paymentResponse = re.getBody();
			} catch (HttpClientErrorException e) { // catch 4xx response codes
				log.error(e.getResponseBodyAsString());
			}
			paymentTransactionsMappings.put(mediaOrderId, Integer.valueOf(paymentResponse.getTransactionId()));
			orderGetterService.addPaymentInfo(mediaOrderId, paymentResponse);
			exchange.getMessage().setBody(paymentResponse);
		}).marshal().json().to("stream:out").to("direct:returnOrderSummary");
		
		from("direct:getPaymentInfo").routeId("getPayment").log("fired getPayment").process((exchange) -> {
			String mediaOrderId = exchange.getMessage().getHeader("orderId", String.class);
			String transactionId = String.valueOf(paymentTransactionsMappings.get(mediaOrderId));

			PaymentResponse paymentResponse = new PaymentResponse();
			try {
				ResponseEntity<PaymentResponse> re = restTemplate.getForEntity(
						"http://" + mediaPaymentServer + "/payment/" + transactionId, PaymentResponse.class);
				paymentResponse = re.getBody();
			} catch (HttpClientErrorException e) { // catch 4xx response codes
				log.error(e.getResponseBodyAsString());
			}

			exchange.getMessage().setBody(paymentResponse);
		}).marshal().json().to("stream:out").to("direct:GetPaymentTopic");
	}

	private void orderTVExceptionHandlers() {
		onException(TVOrderException.class).process((exchange) -> {
			ExceptionResponse er = new ExceptionResponse();
			er.setTimestamp(OffsetDateTime.now());
			Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
			er.setMessage(cause.getMessage());
			exchange.getMessage().setBody(er);
		}).marshal().json().to("stream:out").setHeader("serviceType", constant("tv")).to("direct:TVOrderFail")
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
				.to("direct:InternetOrderFail").handled(true);
	}

}
