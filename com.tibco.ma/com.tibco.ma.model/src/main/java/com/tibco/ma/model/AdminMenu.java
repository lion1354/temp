package com.tibco.ma.model;

import java.io.Serializable;
import java.util.TreeSet;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tibco.ma.common.StringUtil;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
@Document(collection = "admin_menu")
public class AdminMenu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5435230846546782657L;
	private String name;
	@Id
	private String id;

	private String describe;

	private int menuLevel;

	private String url;

	private int order;

	private String code;

	private String p_code;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(int menuLevel) {
		this.menuLevel = menuLevel;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getP_code() {
		return p_code;
	}

	public void setP_code(String p_code) {
		this.p_code = p_code;
	}
	
	@Override
	public String toString() {
		return "AdminMenu [name=" + name + ", id=" + id + ", describe="
				+ describe + ", menuLevel=" + menuLevel + ", url=" + url
				 + ", order=" + order + ", code="
				+ code + ", p_code=" + p_code + "]";
	}
	/**
	 * 
	 * @param name
	 * @param id
	 * @param describe
	 * @param level
	 * @param url
	 * @param order
	 * @param code
	 * @param p_code
	 */
	public AdminMenu(String name, String id, String describe, int level,
			String url, int order, String code, String p_code) {
		super();
		this.name = name;
		this.id = id;
		this.describe = describe;
		this.menuLevel = level;
		this.url = url;
		this.order = order;
		this.code = code;
		this.p_code = p_code;
	}

	public AdminMenu() {
		super();
	}

	public AdminMenu(String id) {
		super();
		this.id = id;
	}

}
