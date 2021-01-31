package org.bp.media;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.bp.media.model.OrderMediaRequest;
import org.bp.media.model.OrderInfo;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
	
	private HashMap<String, PaymentData> payments;

	@PostConstruct
	void init() {
		payments = new HashMap<>();
	}

	public static class PaymentData {
		OrderMediaRequest orderMediaRequest;
		OrderInfo tvOrderInfo;
		OrderInfo internetOrderInfo;

		public boolean isReady() {
			return orderMediaRequest != null && tvOrderInfo != null && internetOrderInfo != null;
		}
	}

	public synchronized boolean addOrderMediaRequest(String mediaOrderId, OrderMediaRequest orderMediaRequest) {
		PaymentData paymentData = getPaymentData(mediaOrderId);
		paymentData.orderMediaRequest = orderMediaRequest;
		return paymentData.isReady();
	}

	public synchronized boolean addOrderInfo(String mediaOrderId, OrderInfo orderInfo, String serviceType) {
		PaymentData paymentData = getPaymentData(mediaOrderId);
		if (serviceType.equals("internet"))
			paymentData.internetOrderInfo = orderInfo;
		else
			paymentData.tvOrderInfo = orderInfo;
		return paymentData.isReady();
	}

	public synchronized PaymentData getPaymentData(String mediaOrderId) {
		PaymentData paymentData = payments.get(mediaOrderId);
		if (paymentData == null) {
			paymentData = new PaymentData();
			payments.put(mediaOrderId, paymentData);
		}
		return paymentData;
	}

}