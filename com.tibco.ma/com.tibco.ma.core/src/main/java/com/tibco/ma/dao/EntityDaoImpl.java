package com.tibco.ma.dao;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mongodb.client.MongoDatabase;
import com.tibco.ma.common.mongodb.DatabaseUtil;

@SuppressWarnings("rawtypes")
@Repository
public class EntityDaoImpl extends BaseDaoImpl implements EntityDao {
	private static final Logger log = LoggerFactory
			.getLogger(EntityDaoImpl.class);

	@Override
	public void insertOneWithId(String collectionName, Document document)
			throws Exception {
		try {
			MongoDatabase db = DatabaseUtil.getDatabase();
			db.getCollection(collectionName).insertOne(document);
		} catch (Exception e) {
			log.error("insertOne error: {}", e);
			throw e;
		}
	}
}
