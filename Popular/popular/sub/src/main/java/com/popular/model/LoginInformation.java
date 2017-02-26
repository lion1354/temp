package com.popular.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class LoginInformation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7924468125954841821L;

	public LoginInformation() {
		super();
	}

	public LoginInformation(Integer userId, String deviceUniqueKey) {
		super();
		this.userId = userId;
		this.deviceUniqueKey = deviceUniqueKey;
	}

	public LoginInformation(Integer userId, String deviceUniqueKey,
			Date loginTime) {
		super();
		this.userId = userId;
		this.deviceUniqueKey = deviceUniqueKey;
		this.loginTime = loginTime;
	}

	private Integer id;
	private Integer userId;
	private String deviceUniqueKey;
	private Date loginTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getDeviceUniqueKey() {
		return deviceUniqueKey;
	}

	public void setDeviceUniqueKey(String deviceUniqueKey) {
		this.deviceUniqueKey = deviceUniqueKey;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

}
