package com.tibco.ma.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "admin_resources")
public class AdminResources implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1070490867761178222L;

	@Id
	private String id;
	private String name;
	private List<String> url;
	private String description;
	private String group_id;
	private String group_name;

	public AdminResources() {
		super();
	}

	public AdminResources(String id) {
		super();
		this.id = id;
	}

	public AdminResources(String name, List<String> url, String description) {
		super();
		this.name = name;
		this.url = url;
		this.description = description;

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

	public List<String> getUrl() {
		return url;
	}

	public void setUrl(List<String> url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
}
