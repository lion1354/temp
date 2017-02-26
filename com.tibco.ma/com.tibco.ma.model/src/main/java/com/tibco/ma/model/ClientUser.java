package com.tibco.ma.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author aidan 2015/4/23
 * 
 */
@XmlRootElement
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
@Document(collection = "client_user")
public class ClientUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2054671215611662951L;

	@Id
	private String id;
	private App app;
	private String username;
	private String password;
	private String email;
	private String state;
	private String code;
	private Long registerTime;

	private String firstname;
	private String lastname;
	private String token;
	private Integer accountType;// 1: general, 2: facebook, 3: twitter
	private Integer shopperId;
	private Integer loyaltyAccountExist;// 1: yes, 0: no
	private Integer loginCount;
	private Long createDateTime;
	private Long updateDateTime;
	private Long lastAccessed;
	private String imgURL;

	private Long lastResetPwdTime;
	private String passwordResetCode;

	public ClientUser() {
		super();
	}

	public ClientUser(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Long registerTime) {
		this.registerTime = registerTime;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public Integer getShopperId() {
		return shopperId;
	}

	public void setShopperId(Integer shopperId) {
		this.shopperId = shopperId;
	}

	public Integer getLoyaltyAccountExist() {
		return loyaltyAccountExist;
	}

	public void setLoyaltyAccountExist(Integer loyaltyAccountExist) {
		this.loyaltyAccountExist = loyaltyAccountExist;
	}

	public Integer getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}

	public Long getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Long createDateTime) {
		this.createDateTime = createDateTime;
	}

	public Long getUpdateDateTime() {
		return updateDateTime;
	}

	public void setUpdateDateTime(Long updateDateTime) {
		this.updateDateTime = updateDateTime;
	}

	public Long getLastAccessed() {
		return lastAccessed;
	}

	public void setLastAccessed(Long lastAccessed) {
		this.lastAccessed = lastAccessed;
	}

	public String getImgURL() {
		return imgURL;
	}

	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}

	public Long getLastResetPwdTime() {
		return lastResetPwdTime;
	}

	public void setLastResetPwdTime(Long lastResetPwdTime) {
		this.lastResetPwdTime = lastResetPwdTime;
	}

	public String getPasswordResetCode() {
		return passwordResetCode;
	}

	public void setPasswordResetCode(String passwordResetCode) {
		this.passwordResetCode = passwordResetCode;
	}

}
