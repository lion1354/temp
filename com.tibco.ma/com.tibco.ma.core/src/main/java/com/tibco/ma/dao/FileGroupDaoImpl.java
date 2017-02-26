package com.tibco.ma.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.tibco.ma.model.FileGroup;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
@Repository
public class FileGroupDaoImpl extends BaseDaoImpl<FileGroup> implements
		FileGroupDao {
	private static final Logger log = LoggerFactory.getLogger(FileGroup.class);

}
