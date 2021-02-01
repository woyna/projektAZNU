package org.bp.payment;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bp.payment.model.Amount;
import org.bp.payment.model.PaymentCard;
import org.bp.payment.model.PaymentException;
import org.bp.payment.model.PaymentRequest;

public class ExceptionHandler {

	public static void validateRequest(PaymentRequest paymentRequest) throws PaymentException {
		if (paymentRequest == null || paymentRequest.getAmount() == null || paymentRequest.getPaymentCard() == null) {
			throw new PaymentException("Invalid payment request");
		}
		validateAmount(paymentRequest.getAmount());
		validatePaymentCard(paymentRequest.getPaymentCard());
	}

	private static void validateAmount(Amount amount) throws PaymentException {
		if (amount.getValue() == null) {
			throw new PaymentException("Amount value cannot be null");
		}
		if (amount.getCurrency() == null) {
			throw new PaymentException("Amount currency cannot be null");
		}
		if (amount.getValue().compareTo(new BigDecimal(0)) < 0) {
			throw new PaymentException("Amount value cannot be negative");
		}
		validateCurrency(amount.getCurrency());
	}

	private static void validateCurrency(String currency) throws PaymentException {
		Pattern pattern = Pattern.compile("^[A-Z]{3}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(currency);

		if (!matcher.find()) {
			throw new PaymentException("Invalid currency format. Accepted format: ABC");
		}
	}

	private static void validatePaymentCard(PaymentCard paymentCard) throws PaymentException {
		if (paymentCard.getName() == null || paymentCard.getName().isEmpty()) {
			throw new PaymentException("Payment card name cannot be null or empty");
		}
		if (paymentCard.getNumber() == null || paymentCard.getNumber().isEmpty()) {
			throw new PaymentException("Payment card number cannot be null or empty");
		}
		if (paymentCard.getValidTo() == null || paymentCard.getValidTo().isEmpty()) {
			throw new PaymentException("Payment card Valid To cannot be null or empty");
		}
		validateNumber(paymentCard.getNumber());
		validateValidTo(paymentCard.getValidTo());
	}

	private static void validateNumber(String number) throws PaymentException {
		Pattern pattern = Pattern.compile("^(\\d{4}\\s?){4}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(number);

		if (!matcher.find()) {
			throw new PaymentException("Invalid payment card number format. It should consist of 16 digits");
		}
	}

	private static void validateValidTo(String validToStr) throws PaymentException {
		Pattern pattern = Pattern.compile("^\\d{2}\\/(\\d{4}|\\d{2})$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(validToStr);

		if (!matcher.find()) {
			throw new PaymentException("Invalid payment card Valid To format. Accepted format: 00/00 or 00/0000");
		}

		if (validToStr.length() < 7) {
			validToStr = validToStr.substring(0,3) + "20" + validToStr.substring(3); 
		}
		int validToYear = Integer.valueOf(validToStr.substring(3));
		int validToMonth = Integer.valueOf(validToStr.substring(0, 2));
		if (validToMonth < 1 || validToMonth > 12) {
			throw new PaymentException("Invalid payment card Valid To month");
		}
	
		YearMonth validTo = YearMonth.of(validToYear, validToMonth);
		
		if (validTo.isBefore(YearMonth.now())) {
			throw new PaymentException("Payment card is not valid anymore");
		}
	}
}
