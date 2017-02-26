package com.popular.exception;

import com.popular.responseutil.ResponseErrorCode;

public class DBException extends Exception {
	private static final long serialVersionUID = 7649320558068288968L;

	private ResponseErrorCode errorCode;

	public DBException() {
		super();
	}

	public DBException(String msg) {
		super(msg);
	}

	public DBException(Throwable t) {
		super(t);
	}

	public DBException(ResponseErrorCode code, String msg) {
		super(msg);
		this.errorCode = code;
	}

	public ResponseErrorCode getErrorCode() {
		return errorCode;
	}

}
