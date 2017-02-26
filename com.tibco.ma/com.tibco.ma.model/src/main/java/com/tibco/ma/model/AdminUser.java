package com.tibco.ma.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "admin_user")
public class AdminUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8642461627262923693L;
	@Id
	private String id;
	private String username;
	private String password;
	private String email;
	private String type;
	private List<AdminRole> roles;

	private String state;
	private String activeCode;
	private Long registerTime;
	private Integer loginTimes;
	private String lastLoginTime;

	private Long siteForgotPWDTime;
	private String siteForgotPWDCode;

	private String telephone;
	private String address;
	private String zip;
	private String company;

	private List<String> appIds;

	public List<String> getAppIds() {
		return appIds;
	}

	public void setAppIds(List<String> appIds) {
		this.appIds = appIds;
	}

	public AdminUser() {
		super();
	}

	public AdminUser(String id) {
		super();
		this.id = id;
	}

	public List<AdminRole> getRoles() {
		return roles;
	}

	public void setRoles(List<AdminRole> roles) {
		this.roles = roles;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getActiveCode() {
		return activeCode;
	}

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	public Long getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Long registerTime) {
		this.registerTime = registerTime;
	}

	public Integer getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Long getSiteForgotPWDTime() {
		return siteForgotPWDTime;
	}

	public void setSiteForgotPWDTime(Long siteForgotPWDTime) {
		this.siteForgotPWDTime = siteForgotPWDTime;
	}

	public String getSiteForgotPWDCode() {
		return siteForgotPWDCode;
	}

	public void setSiteForgotPWDCode(String siteForgotPWDCode) {
		this.siteForgotPWDCode = siteForgotPWDCode;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

}
