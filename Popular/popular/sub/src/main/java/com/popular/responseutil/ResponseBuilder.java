package com.popular.responseutil;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder<T> {
	private HttpStatus httpStatus;
	private List<T> values;
	private T value;
	private ResponseErrorCode error;
	private ResponseStatus status;
	private String errorMsg;
	private String alertMsg;
	private Integer errorCode;
	
	public ResponseBuilder<T> alertMsg(String alertMsg) {
		this.alertMsg = alertMsg;
		return this;
	}

	public ResponseBuilder<T> errorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
		return this;
	}

	public ResponseBuilder<T> httpStatus(HttpStatus status) {
		this.httpStatus = status;
		return this;
	}

	public ResponseBuilder<T> error(ResponseErrorCode errorCode) {
		this.error = errorCode;
		return this;
	}
	
	public ResponseBuilder<T> errorCode(Integer errorCode) {
		this.errorCode = errorCode;
		return this;
	}

	public ResponseBuilder<T> status(ResponseStatus status) {
		this.status = status;
		return this;
	}

	public ResponseBuilder<T> values(List<T> values) {
		this.values = values;
		return this;
	}

	public ResponseBuilder<T> value(T value) {
		this.value = value;
		return this;
	}

	public ResponseEntity<ObjectsResponse<T>> build() {
		ObjectsResponse<T> objects = new ObjectsResponse<T>();

		objects.setError(error);
		objects.setValue(value);
		objects.setValues(values);
		objects.setErrorMsg(errorMsg);
		objects.setStatus(status);
		objects.setAlertMsg(alertMsg);
		objects.setErrorCode(errorCode);

		ResponseEntity<ObjectsResponse<T>> ret = new ResponseEntity<ObjectsResponse<T>>(objects,
				httpStatus);

		return ret;
	}
}
