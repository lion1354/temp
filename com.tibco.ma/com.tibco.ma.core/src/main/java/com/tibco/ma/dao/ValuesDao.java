package com.tibco.ma.dao;

import java.util.List;

import org.bson.Document;

@SuppressWarnings("rawtypes")
public interface ValuesDao extends BaseDao {
	public List<Document> query(String collectionName, Document filter,
			Document sort, Integer skip, Integer limit, Document projection)
			throws Exception;
}
