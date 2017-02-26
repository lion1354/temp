package com.tibco.ma.model.core;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bson.Document;

public class MaFile implements MaDataType {

	public static final String GROUPID = "groupId";
	public static final String COMMENT = "comment";
	public static final String URL = "url";

	private String groupId;
	private String comment;
	private String url;

	public MaFile(String groupId, String comment, String url) {
		this.groupId = groupId;
		this.comment = comment;
		this.url = url;
	}

	public MaFile(Document document) {
		this.groupId = document.getString("groupId");
		this.comment = document.getString("comment");
		this.url = document.getString("url");
	}

	public String getGroupId() {
		return groupId;
	}

	public String getComment() {
		return comment;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put(GROUPID, groupId);
		map.put(COMMENT, comment);
		map.put(URL, url);
		map.put(KEY_TYPE, EntityColType.File.name());
		return map;
	}
}
