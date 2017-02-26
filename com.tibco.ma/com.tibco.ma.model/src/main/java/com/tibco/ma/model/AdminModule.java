package com.tibco.ma.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "admin_module")
public class AdminModule implements Serializable {

	private static final long serialVersionUID = -854478018327958074L;
	@Id
	private String id;
	private String name;
	private String description;

	public AdminModule() {
		super();
	}

	public AdminModule(String id) {
		super();
		this.id = id;
	}

	public AdminModule(String name, AdminModule parent, int sort, String description,
			String type) {
		super();
		this.name = name;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
