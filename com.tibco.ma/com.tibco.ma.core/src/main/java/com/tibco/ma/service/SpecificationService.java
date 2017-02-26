package com.tibco.ma.service;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.data.mongodb.core.query.Query;

import com.tibco.ma.model.Specification;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
public interface SpecificationService extends BaseService<Specification> {
	/**
	 * 
	 * @param jsonObject
	 * {
	 * 		categoryId:${category}
	 * 		comment:${comment}
	 * 		
	 * 		{
	 * 		specList:[
	 * 			id:${id}
	 * 			name:${name}
	 * 			width:${width}
	 * 			height:${height}	
	 * 
	 * 			],...
	 * 		}
	 * }
	 */
	public void add(JSONObject jsonObject);
	/**
	 * 
	 * @param categoryId
	 */
	public void deleteByCategory(String categoryId);
}
