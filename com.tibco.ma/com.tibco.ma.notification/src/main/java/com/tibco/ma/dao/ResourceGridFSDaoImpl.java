package com.tibco.ma.dao;

import javax.annotation.Resource;

import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ResourceGridFSDaoImpl extends BaseGridFSDaoImpl implements ResourceGridFSDao {
	
	@Resource
	private GridFsTemplate notificationGridTemplate;
	
	@Override
	public GridFsTemplate getFSTemplate() {
		return notificationGridTemplate;
	}

}
