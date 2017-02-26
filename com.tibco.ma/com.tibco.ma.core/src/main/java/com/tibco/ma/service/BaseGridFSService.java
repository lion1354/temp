package com.tibco.ma.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
public interface BaseGridFSService {
	/**
	 * 
	 * @param content
	 * @param object
	 * @return
	 */
	public GridFSFile store(InputStream content, DBObject object);
	/**
	 * 
	 * @param query
	 * @return
	 */
	public List<GridFSDBFile> querySFDBFile(Query query);
	/**
	 * 
	 * @param query
	 * @return
	 */
	public GridFSDBFile querySFDBFileOne(Query query);
	/**
	 * 
	 * @param query
	 */
	public void deleteSFDBFile(Query query);
	/**
	 * 
	 * @param content
	 * @param filename
	 * @return
	 */
	public GridFSFile store(InputStream content, String filename);
	/**
	 * 
	 * @param content
	 * @param filename
	 * @param object
	 * @return
	 */
	public GridFSFile store(InputStream content, String filename,
			DBObject object);
	/**
	 * 
	 * @param content
	 * @param filename
	 * @param contentType
	 * @return
	 */
	public GridFSFile store(InputStream content, String filename,
			String contentType);
	/**
	 * 
	 * @param content
	 * @param filename
	 * @param contentType
	 * @param object
	 * @return
	 */
	public GridFSFile store(InputStream content, String filename,
			String contentType, DBObject object);
	/**
	 * 
	 * @param inputStream
	 * @param fileName
	 * @param map
	 * @return
	 */
	public Map<String, Object> saveGetFileUrlAndMd5(InputStream inputStream,
			String fileName, Map<String, Object> map);
	/**
	 * 
	 * @param id
	 */
	public void deleteByOid(String id);

}
