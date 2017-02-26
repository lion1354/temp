package com.tibco.ma.service;

import javax.servlet.http.HttpServletRequest;

import com.tibco.ma.model.FileGroup;
import com.tibco.ma.model.Image;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
public interface FileGroupService extends BaseService<FileGroup> {
	public void deleteByGroupId(String groupId, boolean deleteCol)
			throws Exception;
	/**
	 * 
	 * @param request
	 * @param baseGridFSService
	 * @return
	 * @throws Exception
	 */
	public FileGroup saveFileGroup(HttpServletRequest request,
			BaseGridFSService baseGridFSService) throws Exception;
	/**
	 * 
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public FileGroup queryByGroupId(String groupId) throws Exception;
	/**
	 * 
	 * @param groupId
	 * @param spec_name
	 * @return
	 * @throws Exception
	 */
	public Image getImage(String groupId, String spec_name) throws Exception;
	/**
	 * 
	 * @param groupId
	 * @throws Exception
	 */
	public void deleteCategoryByGroupId(String groupId) throws Exception;
	/**
	 * 
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public FileGroup getFileGroupAndFile(String groupId) throws Exception;

}
