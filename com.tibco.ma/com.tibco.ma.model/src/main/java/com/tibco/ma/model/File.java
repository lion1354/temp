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
@Document(collection = "files")
public class File {
	@Id
	private String id;
	private String name;
	private String description;
	private String contentType;
	private Specification specification;
	private FileGroup fileGroup;
	private String filePath;

	public File() {
		super();
		// TODO Auto-generated constructor stub
	}

	private String md5;

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public File(String id, String name, String description, String contentType, Specification specification,
			FileGroup fileGroup, String filePath, String md5, Long size) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.contentType = contentType;
		this.specification = specification;
		this.fileGroup = fileGroup;
		this.filePath = filePath;
		this.md5 = md5;
		this.size = size;
	}

	private Long size;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public File(String id, String name, String description, String contentType, Specification specification,
			FileGroup fileGroup, String filePath, Long size) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.contentType = contentType;
		this.specification = specification;
		this.fileGroup = fileGroup;
		this.filePath = filePath;
		this.size = size;
	}

	public FileGroup getFileGroup() {
		return fileGroup;
	}

	public void setFileGroup(FileGroup fileGroup) {
		this.fileGroup = fileGroup;
	}

	public String getName() {
		return name;
	}

	public File(String id) {
		super();
		this.id = id;
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

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Specification getSpecification() {
		return specification;
	}

	public void setSpecification(Specification specification) {
		this.specification = specification;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

}
