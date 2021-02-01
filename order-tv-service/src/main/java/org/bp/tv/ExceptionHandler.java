package org.bp.tv;

import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.XMLGregorianCalendar;

import org.bp.types.Fault;
import org.bp.types.Household;
import org.bp.types.TVOrder;
import org.bp.types.TVService;

public class ExceptionHandler {

	public static void validateRequest(TVOrder tvOrder) throws Fault {
				if (tvOrder == null || tvOrder.getHousehold() == null || tvOrder.getTVService() == null || tvOrder.getInstallmentDate() == null) {
			Fault fault = new Fault();
			fault.setCode(10);
			fault.setText("Invalid order request");
			throw fault;
		}
				
		validateHousehold(tvOrder.getHousehold());
		validateInstallmentDate(tvOrder.getInstallmentDate());
		validateTVService(tvOrder.getTVService());
	}
	
	private static void validateHousehold(Household household) throws Fault {
		if (household.getAddress() == null || household.getCity() == null || household.getZipCode() == null) {
			Fault fault = new Fault();
			fault.setCode(20);
			fault.setText("Invalid household (null)");
			throw fault;
		}
		if (household.getAddress().isEmpty() || household.getCity().isEmpty() || household.getZipCode().isEmpty()) {
			Fault fault = new Fault();
			fault.setCode(21);
			fault.setText("Invalid household (empty value)");
			throw fault;
		}
		
		validateZipCode(household.getZipCode());
	}
	
	private static void validateZipCode(String zipCode) throws Fault {
		Pattern pattern = Pattern.compile("^\\d{2}-?\\d{3}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(zipCode);

		if (!matcher.find()) {
			Fault fault = new Fault();
			fault.setCode(22);
			fault.setText("Invalid household zip code format. Accepted format: 00-000 or 00000");
			throw fault;
		}
	}
	
	private static void validateInstallmentDate(XMLGregorianCalendar installmentDate) throws Fault {
		if (!installmentDate.isValid()) {
			Fault fault = new Fault();
			fault.setCode(30);
			fault.setText("Invalid installment date");
			throw fault;
		}
		
		if (installmentDate.toGregorianCalendar().before(new GregorianCalendar())) {
			Fault fault = new Fault();
			fault.setCode(31);
			fault.setText("Installment date earlier than today");
			throw fault;
		}
	}
	
	private static void validateTVService(TVService tvService) throws Fault {
		if (tvService.getProvider() == null ||tvService.getType() == null) {
			Fault fault = new Fault();
			fault.setCode(40);
			fault.setText("Invalid TV Service (null)");
			throw fault;
		}
		
		if (tvService.getProvider().isEmpty()) {
			Fault fault = new Fault();
			fault.setCode(41);
			fault.setText("Invalid TV Service (empty value)");
			throw fault;
		}
	}
}
