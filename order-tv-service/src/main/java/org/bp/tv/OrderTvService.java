package org.bp.tv;

import org.bp.types.OrderInfo;

import java.util.HashMap;
import java.util.Random;

import org.bp.types.Fault;
import org.bp.types.TVOrder;

@javax.jws.WebService
@org.springframework.stereotype.Service
public class OrderTvService {
	
	HashMap<Integer, OrderInfo> orders = new HashMap<>();

	public OrderInfo orderTV(TVOrder tvOrder) throws Fault {
		ExceptionHandler.validateRequest(tvOrder);

		OrderInfo tvOrderInfo = new OrderInfo();
		tvOrderInfo.setId(getOrderId(orders));
		tvOrderInfo.setCost(tvOrder.getTVService().calcCost());
		orders.put(tvOrderInfo.getId(), tvOrderInfo);
		
		return tvOrderInfo;
	}

	public OrderInfo cancelOrder(int orderId) throws Fault {
		return null;
	}
	
	private int getOrderId(HashMap<Integer, OrderInfo> orders) {
		Random rand = new Random();
		int id = 1;

		do {
			id = Math.abs(rand.nextInt());
		} while (orders.get(Integer.valueOf(id)) != null);

		return id;
	}
}
