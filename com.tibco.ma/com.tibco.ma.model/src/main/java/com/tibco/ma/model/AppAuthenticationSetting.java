package com.tibco.ma.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import net.sf.json.JSONArray;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author aidan
 * 
 *         2015/7/3
 * 
 */

@XmlRootElement
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
@Document(collection = "app_authentication_setting")
public class AppAuthenticationSetting implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7236520491188742825L;
	@Id
	private String id;
	private String appId;
	private JSONArray facebookApplications;
	private Boolean isAllowFacebookAuthentication;
	private String twitterConsumerKeys;
	private Boolean isAllowTwitterAuthentication;

	public AppAuthenticationSetting() {
		super();
	}

	public AppAuthenticationSetting(String id) {
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

	public JSONArray getFacebookApplications() {
		return facebookApplications;
	}

	public void setFacebookApplications(JSONArray facebookApplications) {
		this.facebookApplications = facebookApplications;
	}

	public Boolean getIsAllowFacebookAuthentication() {
		return isAllowFacebookAuthentication;
	}

	public void setIsAllowFacebookAuthentication(Boolean isAllowFacebookAuthentication) {
		this.isAllowFacebookAuthentication = isAllowFacebookAuthentication;
	}

	public String getTwitterConsumerKeys() {
		return twitterConsumerKeys;
	}

	public void setTwitterConsumerKeys(String twitterConsumerKeys) {
		this.twitterConsumerKeys = twitterConsumerKeys;
	}

	public Boolean getIsAllowTwitterAuthentication() {
		return isAllowTwitterAuthentication;
	}

	public void setIsAllowTwitterAuthentication(Boolean isAllowTwitterAuthentication) {
		this.isAllowTwitterAuthentication = isAllowTwitterAuthentication;
	}

}
