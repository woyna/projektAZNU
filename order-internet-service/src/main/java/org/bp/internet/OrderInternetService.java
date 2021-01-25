package org.bp.internet;

import org.bp.types.OrderInfo;
import org.bp.types.Fault;
import org.bp.types.InternetOrder;
import org.bp.types.InternetService;
import org.bp.types.InternetBroadband;

@javax.jws.WebService
@org.springframework.stereotype.Service
public class OrderInternetService {
	
	public OrderInfo orderInternet(InternetOrder internetOrder) throws Fault{
		if (internetOrder!=null) {
			InternetService internetService = internetOrder.getInternetService();
			if (internetService!=null && internetService.getProvider() !=null && internetService.getBroadband()!=null && internetService.getBroadband() == InternetBroadband.FIBRE_OPTIC) {
				Fault fault = new Fault();
				fault.setCode(5);
				fault.setText("Fiber-optic broadband is currently not supported");
				throw fault;
			}
		}
		OrderInfo internetOrderInfo = new OrderInfo();
		internetOrderInfo.setId(1);
		internetOrderInfo.setCost(new java.math.BigDecimal(545));
		return internetOrderInfo;	
	}
	
	public OrderInfo cancelOrder(int orderId) throws Fault {
		return null;
	}

}
