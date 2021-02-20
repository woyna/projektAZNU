package org.bp.media;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.bp.media.model.OrderInfo;
import org.bp.media.model.OrderSummary;
import org.bp.media.model.PaymentResponse;
import org.springframework.stereotype.Service;


@Service
public class OrderGetterService {
	
	private HashMap<String, OrderSummary> orders;

	@PostConstruct
	void init() {
		orders = new HashMap<>();
	}

//	public static class OrderSummary {
//		OrderInfo tvOrderInfo;
//		OrderInfo internetOrderInfo;
//		PaymentResponse paymentInfo;
//
//		public boolean isReady() {
//			return tvOrderInfo != null && internetOrderInfo != null && paymentInfo != null;
//		}
//	}

	public synchronized boolean addPaymentInfo(String mediaOrderId, PaymentResponse paymentInfo) {
		OrderSummary orderSummary = getOrderSummary(mediaOrderId);
		orderSummary.setPaymentInfo(paymentInfo);
		return orderSummary.isReady();
	}

	public synchronized boolean addOrderInfo(String mediaOrderId, OrderInfo orderInfo, String serviceType) {
		OrderSummary orderSummary = getOrderSummary(mediaOrderId);
		if (serviceType.equals("internet"))
			orderSummary.setInternetOrderInfo(orderInfo);
		else
			orderSummary.setTvOrderInfo(orderInfo);
		return orderSummary.isReady();
	}

	public synchronized OrderSummary getOrderSummary(String mediaOrderId) {
		OrderSummary orderSummary = orders.get(mediaOrderId);
		if (orderSummary == null) {
			orderSummary = new OrderSummary();
			orderSummary.setId(mediaOrderId);
			orders.put(mediaOrderId, orderSummary);
		}
		return orderSummary;
	}

}