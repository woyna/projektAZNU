package org.bp.internet;

import org.bp.types.OrderInfo;

import java.util.HashMap;
import java.util.Random;

import org.bp.types.Fault;
import org.bp.types.InternetOrder;

@javax.jws.WebService
@org.springframework.stereotype.Service
public class OrderInternetService {
	
	HashMap<Integer, OrderInfo> orders = new HashMap<>();
	
	public OrderInfo orderInternet(InternetOrder internetOrder) throws Fault{
		ExceptionHandler.validateRequest(internetOrder);

		OrderInfo internetOrderInfo = new OrderInfo();
		internetOrderInfo.setId(getOrderId(orders));
		internetOrderInfo.setCost(internetOrder.getInternetService().calcCost());
		
		return internetOrderInfo;	
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
