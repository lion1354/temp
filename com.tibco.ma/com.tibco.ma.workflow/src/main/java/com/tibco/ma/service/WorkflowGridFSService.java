package com.tibco.ma.service;

import java.io.InputStream;
import java.util.Map;
/**
 * 
 * @author Aidan
 *
 */
public interface WorkflowGridFSService extends BaseGridFSService {
	/**
	 * 
	 * @param inputStream
	 * @param fileName
	 * @param map
	 * @return
	 */
	public Map<String, Object> save(InputStream inputStream, String fileName,
			Map<String, Object> map);
	/**
	 * 
	 * @param inputStream
	 * @param fileName
	 * @return
	 */
	public String save(InputStream inputStream, String fileName);
	/**
	 * 
	 * @param id
	 */
	public void deleteById(String id);
}
