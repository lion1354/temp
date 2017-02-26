package com.tibco.ma.common;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.AdminMenu;
import com.tibco.ma.model.AdminResources;
import com.tibco.ma.model.AdminRole;
import com.tibco.ma.model.AdminUser;
import com.tibco.ma.model.App;
import com.tibco.ma.model.AppAuthenticationSetting;
import com.tibco.ma.model.AppSetting;
import com.tibco.ma.model.AppSettingType;
import com.tibco.ma.model.Category;
import com.tibco.ma.model.ClientUser;
import com.tibco.ma.model.CloudCode;
import com.tibco.ma.model.Credential;
import com.tibco.ma.model.Device;
import com.tibco.ma.model.File;
import com.tibco.ma.model.FileGroup;
import com.tibco.ma.model.Image;
import com.tibco.ma.model.NotificationRes;
import com.tibco.ma.model.PNTask;
import com.tibco.ma.model.Progress;
import com.tibco.ma.model.ScaleStaticsValue;
import com.tibco.ma.model.Schedule;
import com.tibco.ma.model.Specification;
import com.tibco.ma.model.SystemLogger;
import com.tibco.ma.model.core.EntityColType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({ AdminMenu.class, AdminResources.class, AdminRole.class,
		AdminUser.class, App.class, AppAuthenticationSetting.class,
		AppSetting.class, AppSettingType.class, Category.class,
		ClientUser.class, CloudCode.class, Credential.class, Device.class,
		EntityColType.class, File.class, FileGroup.class, Image.class,
		NotificationRes.class, PNTask.class, Progress.class,
		ScaleStaticsValue.class, Schedule.class, Specification.class,
		SystemLogger.class, Pager.class })
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
