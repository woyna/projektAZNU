package org.bp.media.model;

import java.math.BigDecimal;

public class Utils {
	
	static public OrderInfo prepareOrderInfo(String orderId, BigDecimal cost) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setId(orderId);
		orderInfo.setCost(cost);
		return orderInfo;
	}

}