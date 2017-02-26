package com.tibco.ma.dao;

import org.bson.Document;

@SuppressWarnings("rawtypes")
public interface EntityDao extends BaseDao {
	public void insertOneWithId(String collectionName, Document document) throws Exception;
}
