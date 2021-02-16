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
	
	public OrderInfo getOrderInfo(OrderInfo orderInfo) throws Fault {
		OrderInfo orderInfoResponse = orders.get(orderInfo.getId());
		
		if (orderInfoResponse == null) {
			Fault fault = new Fault();
			fault.setCode(50);
			fault.setText("There is no order with id " + orderInfo.getId());
			throw fault;
		}
		
		return orderInfoResponse;
	}

	public OrderInfo cancelOrder(int orderId) throws Fault {
		OrderInfo orderInfoResponse;
		
		if (orders.containsKey(orderId)) {
			orderInfoResponse = orders.get(orderId);
			orders.remove(orderId);
		} else {
			Fault fault = new Fault();
			fault.setCode(60);
			fault.setText("There is no order with id " + orderId);
			throw fault;
		}
		
		return orderInfoResponse;
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
