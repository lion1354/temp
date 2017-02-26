package com.tibco.ma.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import net.sf.json.JSONObject;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author aidan
 * 
 *         2015/6/23
 * 
 */
@XmlRootElement
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
@Document(collection = "schedule")
public class Schedule implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8684243808328974090L;
	@Id
	private String id;
	private String name;
	private String scaleName;
	private JSONObject rule;
	private String[] functionIds;
	private Integer status; //0:disable, 1:enable
	private App app;

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

	public String getScaleName() {
		return scaleName;
	}

	public void setScaleName(String scaleName) {
		this.scaleName = scaleName;
	}

	public JSONObject getRule() {
		return rule;
	}

	public void setRule(JSONObject rule) {
		this.rule = rule;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public String[] getFunctionIds() {
		return functionIds;
	}

	public void setFunctionIds(String[] functionIds) {
		this.functionIds = functionIds;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
