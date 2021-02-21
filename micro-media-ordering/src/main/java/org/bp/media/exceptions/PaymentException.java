package org.bp.media.exceptions;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentException {
	@JsonProperty("timestamp")
	private Date timestamp;
	
	@JsonProperty("message")
	private String message;
	
	@JsonProperty("details")
	private String details;
	
	public PaymentException() {
	
	}

	public PaymentException(Date timestamp, String message, String details) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}

}