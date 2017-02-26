package com.tibco.ma.model;

import java.io.Serializable;

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
 * @date 2015-10-13
 */
@XmlRootElement
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
@Document(collection = "category")
public class Category implements Serializable {

	private static final long serialVersionUID = 2034349499957706649L;
	@Id
	private String id;

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", comment=" + comment + "]";
	}

	private String name;

	public Category(String id, String name, String comment) {
		super();
		this.id = id;
		this.name = name;
		this.comment = comment;
	}

	public Category() {
		super();
	}

	public Category(String id) {
		super();
		this.id = id;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	private String comment;
}
