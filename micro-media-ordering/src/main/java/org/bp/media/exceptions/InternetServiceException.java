package org.bp.media.exceptions;

public class InternetServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InternetServiceException() {
	}

	public InternetServiceException(String message) {
		super(message);
	}

	public InternetServiceException(Throwable cause) {
		super(cause);
	}

	public InternetServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public InternetServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}