package com.tibco.ma.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import net.sf.json.JSONObject;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@XmlRootElement
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
@Document(collection = "parameters")
public class Parameters implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 499851233161432252L;
	@Id
	private String id;
	private String connectorId;
	private JSONObject values;
	private String createAt;
	private String updateAt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getConnectorId() {
		return connectorId;
	}

	public void setConnectorId(String connectorId) {
		this.connectorId = connectorId;
	}

	public JSONObject getValues() {
		return values;
	}

	public void setValues(JSONObject values) {
		this.values = values;
	}

	public String getCreateAt() {
		return createAt;
	}

	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}

	public String getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(String updateAt) {
		this.updateAt = updateAt;
	}

}
