package org.bp.mediaorderingui.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderSummary {

	@JsonProperty("id")
	private String id;

	@JsonProperty("tvOrderInfo")
	private OrderInfo tvOrderInfo;
	
	@JsonProperty("internetOrderInfo")
	private OrderInfo internetOrderInfo;
	
	@JsonProperty("paymentInfo")
	private PaymentResponse paymentInfo;
	
	public boolean isReady() {
		return tvOrderInfo != null && internetOrderInfo != null && paymentInfo != null;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public OrderInfo getTvOrderInfo() {
		return tvOrderInfo;
	}

	public void setTvOrderInfo(OrderInfo tvOrderInfo) {
		this.tvOrderInfo = tvOrderInfo;
	}

	public OrderInfo getInternetOrderInfo() {
		return internetOrderInfo;
	}

	public void setInternetOrderInfo(OrderInfo internetOrderInfo) {
		this.internetOrderInfo = internetOrderInfo;
	}

	public PaymentResponse getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(PaymentResponse paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	
}
