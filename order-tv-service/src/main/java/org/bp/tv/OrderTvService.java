package org.bp.tv;

import org.bp.types.OrderInfo;
import org.bp.types.Fault;
import org.bp.types.TVOrder;
import org.bp.types.TVService;
import org.bp.types.TVType;

@javax.jws.WebService
@org.springframework.stereotype.Service
public class OrderTvService {
	
	public OrderInfo orderTV(TVOrder tvOrder) throws Fault{
		if (tvOrder!=null) {
			TVService tvService = tvOrder.getTVService();
			if (tvService!=null && tvService.getProvider() !=null && tvService.getType()!=null && tvService.getType() == TVType.SATELLITE) {
				Fault fault = new Fault();
				fault.setCode(5);
				fault.setText("Satellite television is currently not supported");
				throw fault;
			}
		}
		OrderInfo tvOrderInfo = new OrderInfo();
		tvOrderInfo.setId(1);
		tvOrderInfo.setCost(new java.math.BigDecimal(345));
		return tvOrderInfo;	
	}
	
	public OrderInfo cancelOrder(int orderId) throws Fault {
		return null;
	}

}
