package org.bp.payment;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.bp.payment.model.PaymentException;
import org.bp.payment.model.PaymentRequest;
import org.bp.payment.model.PaymentResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@org.springframework.web.bind.annotation.RestController

@OpenAPIDefinition(info = @Info(title = "Payment service", version = "1", description = "Service for payment"))

public class PaymentService {

	HashMap<Integer, PaymentResponse> payments = new HashMap<>();

	@PostMapping("/payment")
	@Operation(summary = "payment operation", description = "operation for payment", responses = {
			@ApiResponse(responseCode = "200", description = "OK", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)) }) })
	public PaymentResponse payment(@org.springframework.web.bind.annotation.RequestBody PaymentRequest paymentRequest) {
		if (paymentRequest != null && paymentRequest.getAmount() != null
				&& paymentRequest.getAmount().getValue() != null
				&& paymentRequest.getAmount().getValue().compareTo(new BigDecimal(0)) < 0) {

			throw new PaymentException("Amount value can not be negative");
		}

		PaymentResponse paymentResponse = new PaymentResponse();
		paymentResponse.setTransactionDate(new Date());
		paymentResponse.setTransactionId(getPaymentId(payments));
		payments.put(paymentResponse.getTransactionId(), paymentResponse);
		return paymentResponse;
	}

	@GetMapping("/payment/{id}")
	@Operation(summary = "get payment", description = "get payment by id", responses = {
			@ApiResponse(responseCode = "200", description = "OK", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)) }) })
	public PaymentResponse getPayment(@PathVariable int id) {
		PaymentResponse response = payments.get(Integer.valueOf(id));

		if (response == null) {
			throw new PaymentException("Payment " + id + " not found");
		}
		
		return response;
	}

	private int getPaymentId(HashMap<Integer, PaymentResponse> payments) {
		Random rand = new Random();
		int id = 1;

		do {
			id = Math.abs(rand.nextInt());
		} while (payments.get(Integer.valueOf(id)) != null);

		return id;
	}

}
