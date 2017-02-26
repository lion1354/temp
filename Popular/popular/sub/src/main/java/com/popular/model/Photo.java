package com.popular.model;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.popular.common.util.DateUtils;

import net.sf.json.JSONObject;

@JsonInclude(Include.NON_NULL)
public class Photo implements Serializable {
	private static final long serialVersionUID = 3564149152452377589L;

	public Photo() {
		super();
	}

	public Photo(Integer userId, String originalUrl, String description) {
		super();
		this.userId = userId;
		this.originalUrl = originalUrl;
		this.description = description;
	}

	public Photo(JSONObject json) {
		super();

		this.id = json.containsKey("id") ? json.getInt("id") : null;
		this.userId = json.containsKey("userId") ? json.getInt("userId") : null;
		this.thumbnailUrl = json.containsKey("thumbnailUrl")
				? json.getString("thumbnailUrl") : null;
		this.originalUrl = json.containsKey("originalUrl")
				? json.getString("originalUrl") : null;
		this.description = json.containsKey("description")
				? json.getString("description") : null;
		try {
			this.createTime = json.containsKey("createTime")
					? DateUtils.parseDateTime(json.getString("createTime"))
					: new Date();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private Integer id;
	private Integer userId;
	private String thumbnailUrl;
	private String originalUrl;
	private String description;
	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
