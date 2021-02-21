package org.bp.media.exceptions;

public class TVOrderException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TVOrderException() {
	}

	public TVOrderException(String message) {
		super(message);
	}

	public TVOrderException(Throwable cause) {
		super(cause);
	}

	public TVOrderException(String message, Throwable cause) {
		super(message, cause);
	}

	public TVOrderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}