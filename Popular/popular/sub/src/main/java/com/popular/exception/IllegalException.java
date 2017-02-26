package com.popular.exception;

public class IllegalException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9198623478424523819L;

	public IllegalException() {
		super();
	}

	public IllegalException(String message) {
		super(message);
	}

	public IllegalException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalException(Throwable cause) {
		super(cause);
	}
}
