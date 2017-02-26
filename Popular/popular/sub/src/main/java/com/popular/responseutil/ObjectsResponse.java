package com.popular.responseutil;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.popular.model.City;
import com.popular.model.ClientUser;
import com.popular.model.Country;
import com.popular.model.Message;
import com.popular.model.Province;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({ ClientUser.class, City.class, Message.class, Province.class,
		Country.class })
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class ObjectsResponse<T> {
	@XmlElement
	private JAXBGenericWrapper<T> values;
	@XmlElement
	private T value;
	@XmlElement
	private ResponseStatus status;
	@XmlElement
	private ResponseErrorCode error;
	@XmlElement
	private String errorMsg;
	@XmlElement
	private String alertMsg;
	@XmlElement
	private Integer errorCode;

	public List<T> getValues() {
		return values.getItems();
	}

	public void setValues(List<T> values) {
		this.values = new JAXBGenericWrapper<T>(values);
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public ResponseErrorCode getError() {
		return error;
	}

	public void setError(ResponseErrorCode error) {
		this.error = error;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getAlertMsg() {
		return alertMsg;
	}

	public void setAlertMsg(String alertMsg) {
		this.alertMsg = alertMsg;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public void setValues(JAXBGenericWrapper<T> values) {
		this.values = values;
	}
}
