package com.tibco.ma.service;

import java.util.List;

import org.json.simple.JSONObject;

import com.tibco.ma.model.AdminMenu;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
public interface AdminMenuService extends BaseService<AdminMenu> {
	/**
	 * 
	 * @param json
	 *	{
	 * 	   "id":"${id}"
	 * 	   "name":"${name}"
	 * 		"code":"${code}"
	 * 		"order":"${order}"
	 * 		"menuLevel":"${menuLevel}"
	 * 		"p_code":"${pCode}"
	 * 		"url":"${url}"
	 * }
	 * @throws Exception
	 */
	void save(JSONObject json) throws Exception;
	
	/**
	 * 
	 * @param p_id
	 * @return
	 */
	List<AdminMenu> getMenuByParent(String p_id);
	/**
	 * 
	 * @param menu
	 * @return
	 */
	public boolean exists(AdminMenu menu);
	/**
	 * 
	 * @param json
	 * {
	 * "id":${id}
	 * }
	 * @throws Exception
	 */
	public void delete(JSONObject json) throws Exception;
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public AdminMenu loadMenuById(String id) throws Exception;
	/**
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public int checkCode(String code) throws Exception;

}
