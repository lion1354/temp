package com.tibco.ma.model.core;

import java.util.LinkedHashMap;
import java.util.Map;

import com.tibco.ma.model.MongoDBConstants;

public class MaRelation implements MaDataType {

	private String relEntityId;
	private String relEntityName;
	private String relValuesCollection;

	public MaRelation(String relEntityId, String relEntityName,
			String relValuesCollection) {
		this.relEntityId = relEntityId;
		this.relEntityName = relEntityName;
		this.relValuesCollection = relValuesCollection;
	}

	public String getRelEntityId() {
		return relEntityId;
	}

	public String getRelEntityName() {
		return relEntityName;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put(MongoDBConstants.COLUMN_RELENTITYID, relEntityId);
		map.put(MongoDBConstants.COLUMN_RELENTITYNAME, relEntityName);
		map.put(MongoDBConstants.COLUMN_RELATION_COLLECTION, relValuesCollection);
		map.put(KEY_TYPE, EntityColType.Relation.name());
		return map;
	}
}
