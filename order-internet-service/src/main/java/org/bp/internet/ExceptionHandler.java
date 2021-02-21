package org.bp.internet;

import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.XMLGregorianCalendar;

import org.bp.types.Fault;
import org.bp.types.Household;
import org.bp.types.InternetOrder;
import org.bp.types.InternetService;

public class ExceptionHandler {

	public static void validateRequest(InternetOrder internetOrder) throws Fault {
		if (internetOrder == null || internetOrder.getHousehold() == null || internetOrder.getInternetService() == null
				|| internetOrder.getInstallmentDate() == null) {
			Fault fault = new Fault();
			fault.setCode(10);
			fault.setText("Invalid order request");
			throw fault;
		}
		
		validateHousehold(internetOrder.getHousehold());
		validateInstallmentDate(internetOrder.getInstallmentDate());
		validateInternetService(internetOrder.getInternetService());
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

	private static void validateInternetService(InternetService internetService) throws Fault {
		if (internetService.getProvider() == null || internetService.getBroadband() == null) {
			Fault fault = new Fault();
			fault.setCode(40);
			fault.setText("Invalid Internet Service (null)");
			throw fault;
		}

		if (internetService.getProvider().isEmpty()) {
			Fault fault = new Fault();
			fault.setCode(41);
			fault.setText("Invalid TV Service (empty value)");
			throw fault;
		}
		
		if (internetService.getSpeed() < 0) {
			Fault fault = new Fault();
			fault.setCode(42);
			fault.setText("Speed cannot be negative");
			throw fault;
		}
	}
}
