package com.hsbc.demo.common;

import java.util.List;

public class ObjectsResponse<T>
{
	private JAXBGenericWrapper<T> values;
	private T value;
	private ResponseStatus status;
	private ResponseErrorCode error;
	private String errorMsg;
	private String alertMsg;
	private Integer errorCode;

	public List<T> getValues()
	{
		return values.getItems();
	}

	public void setValues(List<T> values)
	{
		this.values = new JAXBGenericWrapper<T>(values);
	}

	public T getValue()
	{
		return value;
	}

	public void setValue(T value)
	{
		this.value = value;
	}

	public ResponseStatus getStatus()
	{
		return status;
	}

	public void setStatus(ResponseStatus status)
	{
		this.status = status;
	}

	public ResponseErrorCode getError()
	{
		return error;
	}

	public void setError(ResponseErrorCode error)
	{
		this.error = error;
	}

	public String getErrorMsg()
	{
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg)
	{
		this.errorMsg = errorMsg;
	}

	public String getAlertMsg()
	{
		return alertMsg;
	}

	public void setAlertMsg(String alertMsg)
	{
		this.alertMsg = alertMsg;
	}

	public Integer getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(Integer errorCode)
	{
		this.errorCode = errorCode;
	}

	public void setValues(JAXBGenericWrapper<T> values)
	{
		this.values = values;
	}
}
