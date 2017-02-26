package com.tibco.ma.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.model.MongoDBConstants;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

	public abstract BaseDao<T> getDao();

	@Override
	public void insertOne(String collectionName, Document document)
			throws Exception {
		getDao().insertOne(collectionName, document);
	}

	@Override
	public void insertMany(String collectionName, List<Document> documents)
			throws Exception {
		getDao().insertMany(collectionName, documents);
	}

	@Override
	public DeleteResult deleteById(String collectionName, Serializable id)
			throws Exception {
		return getDao().deleteOne(
				collectionName,
				new Document(MongoDBConstants.DOCUMENT_ID, new ObjectId(id
						.toString())));
	}

	@Override
	public DeleteResult deleteOne(String collectionName, Document filter)
			throws Exception {
		return getDao().deleteOne(collectionName, filter);
	}

	@Override
	public DeleteResult deleteMany(String collectionName, Document filter)
			throws Exception {
		return getDao().deleteMany(collectionName, filter);
	}

	@Override
	public void dropCollection(String collectionName) throws Exception {
		getDao().dropCollection(collectionName);
	}
	
	@Override
	public void deleteManyAndDropCollection(String collectionName, Document filter)
			throws Exception {
		deleteMany(collectionName, filter);
		long count = count(collectionName, null);
		if (count < 1) {
			dropCollection(collectionName);
		}
	}

	@Override
	public UpdateResult updateById(String collectionName, Serializable id,
			Document update) throws Exception {
		return getDao().updateOne(
				collectionName,
				new Document(MongoDBConstants.DOCUMENT_ID, new ObjectId(id
						.toString())), update);
	}

	@Override
	public UpdateResult updateOne(String collectionName, Document filter,
			Document update) throws Exception {
		return getDao().updateOne(collectionName, filter, update);
	}

	@Override
	public UpdateResult updateMany(String collectionName, Document filter,
			Document update) throws Exception {
		return getDao().updateMany(collectionName, filter, update);
	}

	@Override
	public UpdateResult removeField(String collectionName, Document filter,
			String fieldName) throws Exception {
		return getDao().removeField(collectionName, filter, fieldName);
	}
	
	@Override
	public UpdateResult addArrayField(String collectionName, Document filter,
			Document update) throws Exception {
		return getDao().addArrayField(collectionName, filter, update);
	}

	@Override
	public UpdateResult removeArrayField(String collectionName,
			Document filter, Document update) throws Exception {
		return getDao().removeArrayField(collectionName, filter, update);
	}

	@Override
	public UpdateResult replaceOne(String collectionName, Document filter,
			Document replacement) throws Exception {
		return getDao().replaceOne(collectionName, filter, replacement);
	}

	@Override
	public List<Document> query(String collectionName, Document filter,
			Document sort) throws Exception {
		return getDao().query(collectionName, filter, sort);
	}

	@Override
	public List<Document> query(String collectionName, Document filter,
			Document sort, int page, int pageSize) throws Exception {
		return getDao().page(collectionName, filter, sort, page, pageSize);
	}

	@Override
	public long count(String collectionName, Document filter) throws Exception {
		return getDao().count(collectionName, filter);
	}

	@Override
	public Document getOneById(String collectionName, Serializable id)
			throws Exception {
		Document filter = new Document(MongoDBConstants.DOCUMENT_ID,
				new ObjectId(id.toString()));
		return getDao().getOne(collectionName, filter);
	}

	@Override
	public Document getOne(String collectionName, Document filter)
			throws Exception {
		return getDao().getOne(collectionName, filter);
	}

	@Override
	public Pager<String> page(String collectionName, Document filter,
			Document sort, int page, int pageSize) throws Exception {
		Pager<String> pager = new Pager<String>();

		pager.setNowpage(page);
		pager.setPagesize(pageSize);

		List<Document> docList = getDao().page(collectionName, filter, sort,
				page, pageSize);
		long count = getDao().count(collectionName, filter);

		List<String> list = new ArrayList<String>();
		for (Document doc : docList) {
			list.add(doc.toJson());
		}

		pager.setCountrow(count);
		pager.setMaxPageIndex(10);
		long countpage = pager.getCountrow() % pager.getPagesize() == 0 ? pager
				.getCountrow() / pager.getPagesize() : pager.getCountrow()
				/ pager.getPagesize() + 1;
		pager.setCountpage(countpage);
		pager.showPage();
		pager.setData(list);
		return pager;
	}

	@Override
	public boolean isIndexExist(String collectionName, String indexName)
			throws Exception {
		boolean isExist = false;
		List<Document> indexes = getDao().listIndexes(collectionName);
		for (Document index : indexes) {
			if (index.getString("name").equals(indexName)) {
				isExist = true;
				break;
			}
		}
		return isExist;
	}

	public String createIndex(String collectionName, Document keys,
			IndexOptions indexOptions) throws Exception {
		return getDao().createIndex(collectionName, keys, indexOptions);
	}

	public void dropIndex(String collectionName, String indexName)
			throws Exception {
		getDao().dropIndex(collectionName, indexName);
	}

	@Override
	public List<T> find(Query query, Class<T> entityClass) throws Exception {
		return getDao().find(query, entityClass);
	}

	@Override
	public T findOne(Query query, Class<T> entityClass) throws Exception {
		return getDao().findOne(query, entityClass);
	}

	@Override
	public T findById(ObjectId id, Class<T> entityClass) throws Exception {
		return getDao().findById(id, entityClass);
	}

	@Override
	public void save(T t) throws Exception {
		getDao().save(t);
	}

	@Override
	public void update(Query query, Update update, Class<T> entityClass)
			throws Exception {
		getDao().update(query, update, entityClass);
	}

	@Override
	public List<T> find(Query query, int skip, int limit, Class<T> entityClass) {
		query.skip(skip);
		query.limit(limit);
		return getDao().find(query, entityClass);
	}

	@Override
	public long count(Query query, Class<T> entityClass) {
		return getDao().count(query, entityClass);
	}

	@Override
	public void delete(Query query, Class<T> entityClass) throws Exception {
		getDao().remove(query, entityClass);
	}

}
