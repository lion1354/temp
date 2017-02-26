package com.tibco.ma.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "admin_group")
public class AdminGroup implements Serializable {

	private static final long serialVersionUID = 7001953873754503674L;

	@Id
	private String id;
	private String name;
	private String module_id;
	private String module_name;
	private String adm_group_id;
	private String adm_group_name;
	private String description;
	private Date date;
	private List<String> childrens;
	private List<AdminResources> resources;
	private Boolean checked;

	public AdminGroup() {
		super();
	}

	public AdminGroup(String id) {
		super();
		this.id = id;
	}

	public AdminGroup(String name, String module_id, String adm_group_id,
			String description, List<AdminResources> resources) {
		super();
		this.name = name;
		this.module_id = module_id;
		this.adm_group_id = adm_group_id;
		this.description = description;
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

	public String getModule_id() {
		return module_id;
	}

	public void setModule_id(String module_id) {
		this.module_id = module_id;
	}

	public String getAdm_group_id() {
		return adm_group_id;
	}

	public void setAdm_group_id(String adm_group_id) {
		this.adm_group_id = adm_group_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<String> childrens) {
		this.childrens = childrens;
	}

	public List<AdminResources> getResources() {
		return resources;
	}

	public void setResources(List<AdminResources> resources) {
		this.resources = resources;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public String getModule_name() {
		return module_name;
	}

	public void setModule_name(String module_name) {
		this.module_name = module_name;
	}

	public String getAdm_group_name() {
		return adm_group_name;
	}

	public void setAdm_group_name(String adm_group_name) {
		this.adm_group_name = adm_group_name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
