package com.tibco.ma.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CoreGridFSDaoImpl extends BaseGridFSDaoImpl implements CoreGridFSDao {

	@Autowired
	private GridFsTemplate coreGridTemplate;

	@Override
	public GridFsTemplate getFSTemplate() {
		return coreGridTemplate;
	}

}
