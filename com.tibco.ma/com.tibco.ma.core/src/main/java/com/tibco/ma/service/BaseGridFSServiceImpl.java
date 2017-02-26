package com.tibco.ma.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.tibco.ma.dao.BaseGridFSDao;
import com.tibco.ma.model.MongoDBConstants;

public abstract class BaseGridFSServiceImpl implements BaseGridFSService {

	public abstract BaseGridFSDao getDao();

	@Override
	public GridFSFile store(InputStream content, DBObject object) {
		return getDao().saveFile(content, object);
	}

	@Override
	public List<GridFSDBFile> querySFDBFile(Query query) {
		return getDao().querySFDBFile(query);
	}

	@Override
	public GridFSDBFile querySFDBFileOne(Query query) {
		return getDao().querySFDBFileOne(query);
	}

	@Override
	public void deleteSFDBFile(Query query) {
		getDao().deleteSFDBFile(query);

	}

	@Override
	public GridFSFile store(InputStream content, String filename) {
		return getDao().saveFile(content, filename);
	}

	@Override
	public GridFSFile store(InputStream content, String filename,
			DBObject object) {
		return getDao().saveFile(content, filename, object);
	}

	@Override
	public GridFSFile store(InputStream content, String filename,
			String contentType) {
		return getDao().saveFile(content, filename, contentType);
	}

	@Override
	public GridFSFile store(InputStream content, String filename,
			String contentType, DBObject object) {
		return getDao().saveFile(content, filename, contentType, object);
	}

	@Override
	public Map<String, Object> saveGetFileUrlAndMd5(InputStream inputStream,
			String fileName, Map<String, Object> map) {
		Map<String, Object> mapResult = new HashMap<String, Object>();
		GridFSFile gridFSFile = getDao().saveFile(inputStream, fileName);
		if (gridFSFile != null) {
			if (map != null) {
				for (String key : map.keySet()) {
					gridFSFile.put(key, map.get(key));
				}
			}
			gridFSFile.save();
			mapResult.put("fileUrl", gridFSFile.getId().toString());
			mapResult.put("md5", gridFSFile.getMD5());
		}
		return mapResult;
	}

	@Override
	public void deleteByOid(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId(id)));
		getDao().deleteSFDBFile(query);
	}
}
