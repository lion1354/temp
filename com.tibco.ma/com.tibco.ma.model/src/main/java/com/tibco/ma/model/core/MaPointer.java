package com.tibco.ma.model.core;

import java.util.LinkedHashMap;
import java.util.Map;

import com.tibco.ma.model.MongoDBConstants;

public class MaPointer implements MaDataType {

	public static final String RELATEDID = "relatedId";

	private String relEntityId;
	private String relEntityName;
	private String relatedId;

	public MaPointer(String relEntityId, String relEntityName,
			String relatedId) {
		this.relEntityId = relEntityId;
		this.relEntityName = relEntityName;
		this.relatedId = relatedId;
	}

	public String getRelEntityId() {
		return relEntityId;
	}

	public String getRelEntityName() {
		return relEntityName;
	}

	public Object getRelatedId() {
		return relatedId;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put(MongoDBConstants.COLUMN_RELENTITYID, relEntityId);
		map.put(MongoDBConstants.COLUMN_RELENTITYNAME, relEntityName);
		map.put(RELATEDID, relatedId);
		map.put(KEY_TYPE, EntityColType.Pointer.name());
		return map;
	}
}
