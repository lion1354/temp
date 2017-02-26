package com.tibco.ma.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
@XmlRootElement
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
@Document(collection = "file_group")
public class FileGroup {
	@Id
	private String id;
	private String groupName;
	private String description;
	private Category category;
	@Transient
	private List<File> lists;

	public List<File> getLists() {
		return lists;
	}

	public void setLists(List<File> lists) {
		this.lists = lists;
	}

	public FileGroup(String id, String groupName, String groupId,
			String description, Category category) {
		super();
		this.id = id;
		this.groupName = groupName;

		this.description = description;
		this.category = category;
	}

	public FileGroup(String id) {
		super();
		this.id = id;
	}

	public FileGroup() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}
