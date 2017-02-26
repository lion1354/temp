package com.tibco.ma.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.tibco.ma.common.DocumentUtil;
import com.tibco.ma.common.mongodb.DatabaseUtil;
import com.tibco.ma.model.MongoDBConstants;

@Repository
public class BaseDaoImpl<T> implements BaseDao<T> {

	private static final Logger log = LoggerFactory
			.getLogger(BaseDaoImpl.class);

	@Autowired
	protected MongoTemplate mongoTemplate;

	@Override
	public void insertOne(String collectionName, Document document)
			throws Exception {
		try {
			MongoDatabase db = DatabaseUtil.getDatabase();
			if (document.containsKey(MongoDBConstants.DOCUMENT_ID)) {
				document.remove(MongoDBConstants.DOCUMENT_ID);
			}
			db.getCollection(collectionName).insertOne(document);
		} catch (Exception e) {
			log.error("insertOne error: {}", e);
			throw e;
		}
	}

	@Override
	public void insertMany(String collectionName, List<Document> documents)
			throws Exception {
		try {
			MongoDatabase db = DatabaseUtil.getDatabase();
			for (Document doc : documents) {
				if (doc.containsKey(MongoDBConstants.DOCUMENT_ID)) {
					doc.remove(MongoDBConstants.DOCUMENT_ID);
				}
			}
			db.getCollection(collectionName).insertMany(documents);
		} catch (Exception e) {
			log.error("insertMany error: {}", e);
			throw e;
		}
	}

	@Override
	public DeleteResult deleteOne(String collectionName, Document filter)
			throws Exception {
		try {
			MongoDatabase db = DatabaseUtil.getDatabase();
			return db.getCollection(collectionName).deleteOne(filter);
		} catch (Exception e) {
			log.error("deleteOne error: {}", e);
			throw e;
		}
	}

	@Override
	public DeleteResult deleteMany(String collectionName, Document filter)
			throws Exception {
		try {
			MongoDatabase db = DatabaseUtil.getDatabase();
			if (filter == null) {
				filter = new Document();
			}
			return db.getCollection(collectionName).deleteMany(filter);
		} catch (Exception e) {
			log.error("deleteMany error: {}", e);
			throw e;
		}
	}

	@Override
	public void dropCollection(String collectionName) throws Exception {
		try {
			MongoDatabase db = DatabaseUtil.getDatabase();
			db.getCollection(collectionName).drop();
		} catch (Exception e) {
			log.error("dropCollection error: {}", e);
			throw e;
		}
	}

	@Override
	public UpdateResult updateOne(String collectionName, Document filter,
			Document update) throws Exception {
		try {
			MongoDatabase db = DatabaseUtil.getDatabase();
			if (update.containsKey(MongoDBConstants.DOCUMENT_ID)) {
				update.remove(MongoDBConstants.DOCUMENT_ID);
			}
			return db.getCollection(collectionName).updateOne(filter,
					new Document("$set", update));
		} catch (Exception e) {
			log.error("updateOne error: {}", e);
			throw e;
		}
	}

	@Override
	public UpdateResult updateMany(String collectionName, Document filter,
			Document update) throws Exception {
		try {
			MongoDatabase db = DatabaseUtil.getDatabase();
			if (update.containsKey(MongoDBConstants.DOCUMENT_ID)) {
				update.remove(MongoDBConstants.DOCUMENT_ID);
			}
			if (filter == null) {
				filter = new Document();
			}
			return db.getCollection(collectionName).updateMany(filter,
					new Document("$set", update));
		} catch (Exception e) {
			log.error("updateMany error: {}", e);
			throw e;
		}
	}

	@Override
	public UpdateResult removeField(String collectionName, Document filter,
			String fieldName) throws Exception {
		try {
			MongoDatabase db = DatabaseUtil.getDatabase();
			if (filter == null) {
				filter = new Document();
			}
			Document update = new Document("$unset", new Document(fieldName, 1));
			return db.getCollection(collectionName).updateMany(filter, update);
		} catch (Exception e) {
			log.error("removeField error: {}", e);
			throw e;
		}
	}

	@Override
	public UpdateResult addArrayField(String collectionName, Document filter,
			Document update) throws Exception {
		try {
			MongoDatabase db = DatabaseUtil.getDatabase();
			if (filter == null) {
				filter = new Document();
			}
			Document value = new Document("$push", update);
			return db.getCollection(collectionName).updateOne(filter, value);
		} catch (Exception e) {
			log.error("addArrayField error: {}", e);
			throw e;
		}
	}

	@Override
	public UpdateResult removeArrayField(String collectionName,
			Document filter, Document update) throws Exception {
		try {
			MongoDatabase db = DatabaseUtil.getDatabase();
			if (filter == null) {
				filter = new Document();
			}
			Document value = new Document("$pull", update);
			return db.getCollection(collectionName).updateOne(filter, value);
		} catch (Exception e) {
			log.error("removeArrayField error: {}", e);
			throw e;
		}
	}

	@Override
	public UpdateResult replaceOne(String collectionName, Document filter,
			Document replacement) throws Exception {
		try {
			if (replacement.containsKey(MongoDBConstants.DOCUMENT_ID)) {
				replacement.remove(MongoDBConstants.DOCUMENT_ID);
			}
			MongoDatabase db = DatabaseUtil.getDatabase();
			if (filter == null) {
				filter = new Document();
			}
			return db.getCollection(collectionName).replaceOne(filter,
					replacement);
		} catch (Exception e) {
			log.error("replaceOne error: {}", e);
			throw e;
		}
	}

	@Override
	public List<Document> query(String collectionName, Document filter,
			Document sort) throws Exception {
		try {
			FindIterable<Document> iterable = null;
			MongoDatabase db = DatabaseUtil.getDatabase();

			if (filter == null) {
				filter = new Document();
			}
			if (sort == null) {
				iterable = db.getCollection(collectionName).find(filter);
			} else {
				iterable = db.getCollection(collectionName).find(filter)
						.sort(sort);
			}

			List<Document> list = iterable.into(new ArrayList<Document>());
			if (list != null && list.size() > 0) {
				for (Document document : list) {
					DocumentUtil.convertObjectIdToString(document);
				}
			}
			return list;
		} catch (Exception e) {
			log.error("query error: {}", e);
			throw e;
		}
	}

	@Override
	public long count(String collectionName, Document filter) throws Exception {
		try {
			MongoDatabase db = DatabaseUtil.getDatabase();
			if (filter == null) {
				filter = new Document();
			}
			return db.getCollection(collectionName).count(filter);
		} catch (Exception e) {
			log.error("count error: {}", e);
			throw e;
		}
	}

	@Override
	public Document getOne(String collectionName, Document filter)
			throws Exception {
		try {
			MongoDatabase db = DatabaseUtil.getDatabase();
			FindIterable<Document> iterable = db.getCollection(collectionName)
					.find(filter);
			Document document = iterable.first();
			DocumentUtil.convertObjectIdToString(document);
			return document;
		} catch (Exception e) {
			log.error("replaceOne error: {}", e);
			throw e;
		}
	}

	@Override
	public List<Document> page(String collectionName, Document filter,
			Document sort, int page, int pageSize) throws Exception {
		try {
			FindIterable<Document> iterable = null;
			MongoDatabase db = DatabaseUtil.getDatabase();

			if (filter == null) {
				filter = new Document();
			}
			if (sort == null) {
				iterable = db.getCollection(collectionName).find(filter)
						.skip((page - 1) * pageSize).limit(pageSize);
			} else {
				iterable = db.getCollection(collectionName).find(filter)
						.sort(sort).skip((page - 1) * pageSize).limit(pageSize);
			}

			List<Document> list = iterable.into(new ArrayList<Document>());
			if (list != null && list.size() > 0) {
				for (Document document : list) {
					DocumentUtil.convertObjectIdToString(document);
				}
			}
			return list;
		} catch (Exception e) {
			log.error("page error: {}", e);
			throw e;
		}
	}

	@Override
	public String createIndex(String collectionName, Document keys,
			IndexOptions indexOptions) throws Exception {
		try {
			MongoDatabase db = DatabaseUtil.getDatabase();
			if (indexOptions == null) {
				return db.getCollection(collectionName).createIndex(keys);
			} else {
				return db.getCollection(collectionName).createIndex(keys,
						indexOptions);
			}
		} catch (Exception e) {
			log.error("createIndex error: {}", e);
			throw e;
		}
	}

	@Override
	public void dropIndex(String collectionName, String indexName)
			throws Exception {
		try {
			MongoDatabase db = DatabaseUtil.getDatabase();
			db.getCollection(collectionName).dropIndex(indexName);
		} catch (Exception e) {
			log.error("dropIndex error: {}", e);
			throw e;
		}
	}

	@Override
	public List<Document> listIndexes(String collectionName) throws Exception {
		try {
			MongoDatabase db = DatabaseUtil.getDatabase();
			ListIndexesIterable<Document> iterable = db.getCollection(
					collectionName).listIndexes();
			return iterable.into(new ArrayList<Document>());
		} catch (Exception e) {
			log.error("listIndexes error: {}", e);
			throw e;
		}
	}

	@Override
	public T findOne(Query query, Class<T> entityClass) {
		return mongoTemplate.findOne(query, entityClass);
	}

	@Override
	public T findById(ObjectId id, Class<T> entityClass) {
		return mongoTemplate.findById(id, entityClass);
	}

	@Override
	public void save(T t) {
		mongoTemplate.save(t);
	}

	@Override
	public void update(Query query, Update update, Class<T> entityClass) {
		mongoTemplate.updateFirst(query, update, entityClass);
	}

	@Override
	public List<T> find(Query query, Class<T> entityClass) {
		return mongoTemplate.find(query, entityClass);
	}

	@Override
	public void remove(Query query, Class<T> entityClass) {
		mongoTemplate.remove(query, entityClass);
	}

	@Override
	public long count(Query query, Class<T> entityClass) {
		return mongoTemplate.count(query, entityClass);
	}

	@Override
	public List<T> doAggregationWork(Aggregation agg, String collectionName,
			Class<T> entityClass) {
		AggregationResults<T> results = mongoTemplate.aggregate(agg,
				collectionName, entityClass);
		List<T> resultList = results.getMappedResults();
		return resultList;
	}
}
