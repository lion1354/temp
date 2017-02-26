package com.popular.exception;

import com.popular.responseutil.ResponseErrorCode;

public class PopularException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7309570349994692998L;

	private ResponseErrorCode errorCode;

	public PopularException() {
		super();
	}

	public PopularException(String msg) {
		super(msg);
	}

	public PopularException(Throwable t) {
		super(t);
	}

	public PopularException(ResponseErrorCode code, String msg) {
		super(msg);
		this.errorCode = code;
	}

	public ResponseErrorCode getErrorCode() {
		return errorCode;
	}
}
