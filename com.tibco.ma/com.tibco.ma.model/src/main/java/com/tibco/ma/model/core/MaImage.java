package com.tibco.ma.model.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

public class MaImage implements MaDataType {
	
	public static final String GROUPID = "groupId";
	public static final String COMMENT = "comment";
	public static final String URLS = "urls";

	private String groupId;
	private String comment;
	private List<String> urls = new ArrayList<String>();

	public MaImage(String groupId, String comment, List<String> urls) {
		this.groupId = groupId;
		this.comment = comment;
		if (urls != null && urls.size() > 0) {
			this.urls = urls;
		}
	}

	@SuppressWarnings("unchecked")
	public MaImage(Document document) {
		this.groupId = document.getString("groupId");
		this.comment = document.getString("comment");
		Object urlsObj = document.get("urls");
		if (urlsObj != null && urlsObj instanceof List) {
			this.urls = (List<String>) urlsObj;
		}
	}

	public String getGroupId() {
		return groupId;
	}

	public String getComment() {
		return comment;
	}

	public List<String> getUrls() {
		return urls;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put(GROUPID, groupId);
		map.put(COMMENT, comment);
		map.put(URLS, urls);
		map.put(KEY_TYPE, EntityColType.Image.name());
		return map;
	}
}
