package com.tibco.ma.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.tibco.ma.common.DocumentUtil;
import com.tibco.ma.common.mongodb.DatabaseUtil;

@SuppressWarnings("rawtypes")
@Repository
public class ValuesDaoImpl extends BaseDaoImpl implements ValuesDao {
	private static final Logger log = LoggerFactory
			.getLogger(ValuesDaoImpl.class);

	@Override
	public List<Document> query(String collectionName, Document filter,
			Document sort, Integer skip, Integer limit, Document projection)
			throws Exception {
		try {
			FindIterable<Document> iterable = null;
			MongoDatabase db = DatabaseUtil.getDatabase();

			if (filter == null) {
				filter = new Document();
			}
			iterable = db.getCollection(collectionName).find(filter);

			if (sort != null) {
				iterable.sort(sort);
			}
			if (skip != null && limit != null) {
				iterable.skip(skip).limit(limit);
			}
			if (projection != null) {
				iterable.projection(projection);
			}

			List<Document> list = iterable.into(new ArrayList<Document>());
			if (list != null && list.size() > 0) {
				for (Document document : list) {
					DocumentUtil.convertObjectIdToString(document);
				}
			}
			return list;
		} catch (Exception e) {
			log.error("rest query values error: {}", e);
			throw e;
		}
	}
}
