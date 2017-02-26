package com.tibco.ma.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
@XmlRootElement
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
@Document(collection = "app_setting")
public class AppSetting implements Serializable {

	/**
	 * {"id":"${id}", "app":${appId}, "type":"${type}", "value": ${Object}}
	 *
	 */
	private static final long serialVersionUID = -8714373912026513170L;
	@Id
	private String id;

	private App app;

	private AppSettingType type;

	public AppSettingType getAppSettingType() {
		return type;
	}

	public void setAppSettingType(AppSettingType appSettingType) {
		this.type = appSettingType;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	private Map<String, Object> map = new HashMap<String, Object>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public AppSetting(String id, Map<String, Object> map) {
		super();
		this.id = id;
		this.map = map;
	}
	/**
	 * 
	 * @param id
	 */
	public AppSetting(String id) {
		super();
		this.id = id;
	}

	public AppSetting() {
		super();
	}

}
