package com.tibco.ma.service;

import java.io.Serializable;

import com.tibco.ma.model.Category;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
public interface CategoryService extends BaseService<Category> {
	/**
	 * 
	 * @param id
	 */
	public void deleteCategoryAndSpecificationByCategoryId(Serializable id);

}
