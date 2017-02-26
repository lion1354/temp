package com.tibco.ma.common;

public class MaException extends Exception {
	private ResponseErrorCode errorCode;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MaException() {
		super();
	}

	public MaException(String msg) {
		super(msg);
	}

	public MaException(Throwable t) {
		super(t);
	}

	public MaException(ResponseErrorCode code, String msg) {
		super(msg);
		this.errorCode = code;
	}

	public ResponseErrorCode getErrorCode() {
		return errorCode;
	}

}
