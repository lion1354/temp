package com.tibco.ma.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.tibco.ma.model.File;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
public interface FileService extends BaseService<File> {
	/**
	 * 
	 * @param groupId
	 * @return
	 */
	public boolean ValidateGroupId(String groupId);
	/**
	 * 
	 * @param groupId
	 * @return
	 */
	public List<File> queryByGroupId(String groupId);
	/**
	 * 
	 * @param groupId
	 */
	public void deleteByGroupId(String groupId);
	/**
	 * 
	 * @param specificationId
	 */
	public void deleteBySpecification(String specificationId);

	/**
	 * 
	 * @param groupId
	 * @param contentPath
	 * @return
	 * @throws Exception
	 */
	public List<File> showByGroupId(String groupId, String contentPath)
			throws Exception;
	/**
	 * 
	 * @param ids
	 * @param contentType
	 * @return
	 * @throws Exception
	 */
	public List<File> QueryByIds(String ids, String contentType)
			throws Exception;
	/**
	 * 
	 * @param fileId
	 * @param coreGridFSService
	 */
	public void deleteFileById(String fileId,
			BaseGridFSService coreGridFSService);
	/**
	 * 
	 * @param deleteIds
	 * @param type
	 * @throws Exception
	 */
	public void deleteOldFile(String deleteIds, String type) throws Exception;
	/**
	 * 
	 * @param request
	 * @param baseGridFSService
	 * @return
	 * @throws Exception
	 */
	public List<File> uploadFile(HttpServletRequest request,
			BaseGridFSService baseGridFSService) throws Exception;
	/**
	 * 
	 * @param request
	 * @param groupId
	 * @param baseGridFSService
	 * @return
	 * @throws Exception
	 */
	public List<File> saveFiles(HttpServletRequest request, String groupId,
			BaseGridFSService baseGridFSService) throws Exception;
}
