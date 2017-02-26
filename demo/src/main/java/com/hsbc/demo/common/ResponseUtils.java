package com.hsbc.demo.common;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtils<T> {
	public static <T> ResponseBuilder<T> newResponse() {

		return new ResponseBuilder<T>();
	}

	public static ResponseEntity<?> success() {
		return newResponse().status(ResponseStatus.OK)
				.httpStatus(HttpStatus.OK).build();

	}

	public static ResponseEntity<?> successWithStatus(HttpStatus status) {
		return newResponse().status(ResponseStatus.OK).httpStatus(status)
				.build();
	}

	public static <T> ResponseEntity<ObjectsResponse<T>> successWithValues(
			List<T> values) {

		return ResponseUtils.<T> newResponse().status(ResponseStatus.OK)
				.httpStatus(HttpStatus.OK).values(values).build();
	}

	public static <T> ResponseEntity<ObjectsResponse<T>> successWithValue(
			T value) {
		return ResponseUtils.<T> newResponse().status(ResponseStatus.OK)
				.httpStatus(HttpStatus.OK).value(value).build();

	}

	public static <T> ResponseEntity<ObjectsResponse<T>> fail(String errorMsg) {

		return ResponseUtils.<T> newResponse().status(ResponseStatus.ERROR)
				.httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
				.errorMsg(errorMsg).build();
	}

	public static <T> ResponseEntity<ObjectsResponse<T>> alert(String alertMsg) {

		return ResponseUtils.<T> newResponse().status(ResponseStatus.ALERT)
				.httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
				.alertMsg(alertMsg).build();
	}

	public static <T> ResponseEntity<ObjectsResponse<T>> fail(String errorMsg,
			HttpStatus status) {

		return ResponseUtils.<T> newResponse().status(ResponseStatus.ERROR)
				.httpStatus(status).errorMsg(errorMsg).build();
	}

	public static <T> ResponseEntity<ObjectsResponse<T>> fail(
			ResponseErrorCode errorCode, String errorMsg) {

		return ResponseUtils.<T> newResponse().error(errorCode)
				.status(ResponseStatus.ERROR).errorMsg(errorMsg)
				.httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	public static <T> ResponseEntity<ObjectsResponse<T>> fail(
			ResponseErrorCode errorCode, String errorMsg, HttpStatus status) {

		return ResponseUtils.<T> newResponse().error(errorCode)
				.status(ResponseStatus.ERROR).errorMsg(errorMsg)
				.httpStatus(status).build();
	}
	
	public static <T> ResponseEntity<ObjectsResponse<T>> fail(
			int errorCode, String errorMsg, HttpStatus status) {

		return ResponseUtils.<T> newResponse().errorCode(errorCode)
				.status(ResponseStatus.ERROR).errorMsg(errorMsg)
				.httpStatus(status).build();
	}
}
