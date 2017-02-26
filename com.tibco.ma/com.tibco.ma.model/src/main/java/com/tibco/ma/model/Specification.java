package com.tibco.ma.model;

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
@Document(collection = "specifications")
public class Specification {
	@Id
	private String id;
	private Category category;
	private String name;
	private float width;
	private float height;
	private String comment;

	public Specification() {
		super();
	}

	public Specification(String id) {
		super();
		this.id = id;
	}

	public Specification(String id, String name, float width, float height, String comment) {
		super();
		this.id = id;
		this.name = name;
		this.width = width;
		this.height = height;
		this.comment = comment;

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getwidth() {
		return width;
	}

	public void setwidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "Specification [id=" + id + ", category=" + category + ", name=" + name + ", width=" + width
				+ ", height=" + height + ", comment=" + comment + "]";
	}

}
