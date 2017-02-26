package com.tibco.ma.dao;

import java.io.InputStream;
import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015-10-13
 */
@Repository
public abstract class BaseGridFSDaoImpl implements BaseGridFSDao {

	public abstract GridFsTemplate getFSTemplate();
	
	@Override
	public GridFSFile saveFile(InputStream content, DBObject object) {
		return getFSTemplate().store(content, object);
	}

	@Override
	public GridFSFile saveFile(InputStream content, String filename) {
		return getFSTemplate().store(content, filename);
	}

	@Override
	public GridFSFile saveFile(InputStream content, String filename,
			DBObject object) {
		return getFSTemplate().store(content, filename, object);
	}

	@Override
	public GridFSFile saveFile(InputStream content, String filename,
			String contentType) {
		return getFSTemplate().store(content, filename, contentType);
	}

	@Override
	public GridFSFile saveFile(InputStream content, String filename,
			String contentType, DBObject object) {
		return getFSTemplate().store(content, filename, contentType, object);
	}

	@Override
	public List<GridFSDBFile> querySFDBFile(Query query) {
		return getFSTemplate().find(query);
	}

	@Override
	public void deleteSFDBFile(Query query) {
		getFSTemplate().delete(query);
	}

	@Override
	public GridFSDBFile querySFDBFileOne(Query query) {
		return getFSTemplate().findOne(query);
	}

}
