package com.tibco.ma.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@XmlRootElement
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
@Document(collection = "device")
public class Device implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9054743461650748290L;
	@Id
	private String id;
	private String appId;
	private String deviceId;
	private String deviceToken;
	private Boolean isActive;
	private String os;
	private String osVersion;
	private String device;
	private String clientAgent;
	private Date createDatetime;
	private Date updateDatetime;
	private Boolean timedAlertStatus;
	private Boolean arenaAlertStatus;
	private String userId;
	private Map extensions;

	public Device() {
		super();
	}

	public Device(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getClientAgent() {
		return clientAgent;
	}

	public void setClientAgent(String clientAgent) {
		this.clientAgent = clientAgent;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public Date getUpdateDatetime() {
		return updateDatetime;
	}

	public void setUpdateDatetime(Date updateDatetime) {
		this.updateDatetime = updateDatetime;
	}

	public Boolean getTimedAlertStatus() {
		return timedAlertStatus;
	}

	public void setTimedAlertStatus(Boolean timedAlertStatus) {
		this.timedAlertStatus = timedAlertStatus;
	}

	public Boolean getArenaAlertStatus() {
		return arenaAlertStatus;
	}

	public void setArenaAlertStatus(Boolean arenaAlertStatus) {
		this.arenaAlertStatus = arenaAlertStatus;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Map getExtensions() {
		return extensions;
	}

	public void setExtensions(Map extensions) {
		this.extensions = extensions;
	}

}
