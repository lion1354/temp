package com.popular.model;

import java.io.Serializable;

public class JsonDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6524655842751452913L;
	private boolean success;
	private String message;
	private Object data;

	public JsonDto(boolean success) {
		this(success, null, null);
	}

	public JsonDto(boolean success, Object object) {
		this(success, null, object);
	}

	public JsonDto(boolean success, String message) {
		this(success, message, null);
	}

	public JsonDto(boolean success, String message, Object data) {
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
