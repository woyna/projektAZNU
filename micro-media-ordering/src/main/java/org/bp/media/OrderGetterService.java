package org.bp.media;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.bp.media.model.OrderInfo;
import org.bp.media.model.PaymentResponse;
import org.springframework.stereotype.Service;


@Service
public class OrderGetterService {
	
	private HashMap<String, OrderSummary> orders;

	@PostConstruct
	void init() {
		orders = new HashMap<>();
	}

	public static class OrderSummary {
		OrderInfo tvOrderInfo;
		OrderInfo internetOrderInfo;
		PaymentResponse paymentInfo;

		public boolean isReady() {
			return paymentInfo != null && tvOrderInfo != null && internetOrderInfo != null;
		}
	}

	public synchronized boolean addPaymentInfo(String mediaOrderId, PaymentResponse paymentInfo) {
		OrderSummary orderSummary = getOrderSummary(mediaOrderId);
		orderSummary.paymentInfo = paymentInfo;
		return orderSummary.isReady();
	}

	public synchronized boolean addOrderInfo(String mediaOrderId, OrderInfo orderInfo, String serviceType) {
		OrderSummary orderSumamry = getOrderSummary(mediaOrderId);
		if (serviceType.equals("internet"))
			orderSumamry.internetOrderInfo = orderInfo;
		else
			orderSumamry.tvOrderInfo = orderInfo;
		return orderSumamry.isReady();
	}

	public synchronized OrderSummary getOrderSummary(String mediaOrderId) {
		OrderSummary orderSummary = orders.get(mediaOrderId);
		if (orderSummary == null) {
			orderSummary = new OrderSummary();
			orders.put(mediaOrderId, orderSummary);
		}
		return orderSummary;
	}

}