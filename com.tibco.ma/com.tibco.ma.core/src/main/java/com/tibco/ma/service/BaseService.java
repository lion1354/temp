package com.tibco.ma.service;

import java.io.Serializable;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.tibco.ma.common.query.Pager;

public interface BaseService<T> {
	public void insertOne(String collectionName, Document document)
			throws Exception;

	public void insertMany(String collectionName, List<Document> documents)
			throws Exception;

	public DeleteResult deleteById(String collectionName, Serializable id)
			throws Exception;

	public DeleteResult deleteOne(String collectionName, Document filter)
			throws Exception;

	public DeleteResult deleteMany(String collectionName, Document filter)
			throws Exception;

	public void dropCollection(String collectionName) throws Exception;

	public void deleteManyAndDropCollection(String collectionName,
			Document filter) throws Exception;

	public UpdateResult updateById(String collectionName, Serializable id,
			Document update) throws Exception;

	public UpdateResult updateOne(String collectionName, Document filter,
			Document update) throws Exception;

	public UpdateResult updateMany(String collectionName, Document filter,
			Document update) throws Exception;

	public UpdateResult addArrayField(String collectionName, Document filter,
			Document update) throws Exception;

	public UpdateResult removeField(String collectionName, Document filter,
			String fieldName) throws Exception;

	public UpdateResult removeArrayField(String collectionName,
			Document filter, Document update) throws Exception;

	public UpdateResult replaceOne(String collectionName, Document filter,
			Document replacement) throws Exception;

	public List<Document> query(String collectionName, Document filter,
			Document sort) throws Exception;
	
	public List<Document> query(String collectionName, Document filter,
			Document sort, int page, int pageSize) throws Exception;
	
	public long count(String collectionName, Document filter) throws Exception;

	public Document getOneById(String collectionName, Serializable id)
			throws Exception;

	public Document getOne(String collectionName, Document filter)
			throws Exception;

	public Pager<String> page(String collectionName, Document filter,
			Document sort, int page, int pageSize) throws Exception;

	public boolean isIndexExist(String collectionName, String indexName)
			throws Exception;

	public String createIndex(String collectionName, Document keys,
			IndexOptions indexOptions) throws Exception;

	public void dropIndex(String collectionName, String indexName)
			throws Exception;

	public List<T> find(Query query, Class<T> entityClass) throws Exception;

	public T findOne(Query query, Class<T> entityClass) throws Exception;

	public T findById(ObjectId id, Class<T> entityClass) throws Exception;

	public void save(T t) throws Exception;

	public void update(Query query, Update update, Class<T> entityClass)
			throws Exception;

	public List<T> find(Query query, int skip, int limit, Class<T> entityClass)
			throws Exception;

	public long count(Query query, Class<T> entityClass);

	public void delete(Query query, Class<T> entityClass) throws Exception;
}
