package com.tibco.ma.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "admin_role")
public class AdminRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8020264163074034612L;

	@Id
	private String id;
	private String name;
	private String description;
	private List<AdminResources> resources;
	private Boolean checked;
	
	public AdminRole() {
		super();
	}

	public AdminRole(String id) {
		super();
		this.id = id;
	}

	public AdminRole(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public List<AdminResources> getResources() {
		return resources;
	}

	public void setResources(List<AdminResources> resources) {
		this.resources = resources;
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

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

}
