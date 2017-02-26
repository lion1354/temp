package com.tibco.ma.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@XmlRootElement
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
@Document(collection = "app")
public class App implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2623188804635866946L;

	@Id
	private String id;
	private String name;
	private AdminUser user;
	private String describe;
	private String restApiKey;
	private String masterKey;
	private String accessKey;
	private String secretKey;
	private boolean ops;
	private String iconUrl;
	private String isCollApp;
	private String ownUserName;
	private String ownUserEmail;
	private String collEmail;
	private List<String> collaborators;

	public List<String> getCollaborators() {
		return collaborators;
	}

	public void setCollaborators(List<String> collaborators) {
		this.collaborators = collaborators;
	}

	public String getOwnUserName() {
		return ownUserName;
	}

	public void setOwnUserName(String ownUserName) {
		this.ownUserName = ownUserName;
	}

	public String getOwnUserEmail() {
		return ownUserEmail;
	}

	public void setOwnUserEmail(String ownUserEmail) {
		this.ownUserEmail = ownUserEmail;
	}

	public String getCollEmail() {
		return collEmail;
	}

	public void setCollEmail(String collEmail) {
		this.collEmail = collEmail;
	}

	public String getIsCollApp() {
		return isCollApp;
	}

	public void setIsCollApp(String isCollApp) {
		this.isCollApp = isCollApp;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public App() {
		super();
	}

	public App(String id) {
		super();
		this.id = id;
	}

	public App(String id, String name, String describe) {
		super();
		this.id = id;
		this.name = name;
		this.describe = describe;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AdminUser getUser() {
		return user;
	}

	public void setUser(AdminUser user) {
		this.user = user;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getRestApiKey() {
		return restApiKey;
	}

	public void setRestApiKey(String restApiKey) {
		this.restApiKey = restApiKey;
	}

	public String getMasterKey() {
		return masterKey;
	}

	public void setMasterKey(String masterKey) {
		this.masterKey = masterKey;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public boolean isOps() {
		return ops;
	}

	public void setOps(boolean ops) {
		this.ops = ops;
	}

	@Override
	public String toString() {
		return "id=" + this.id + ";name=" + this.name + ";";
	}

}
