package com.tibco.ma.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;
/**
 * 
 * @author Aidan
 *
 */
@Repository
public class WorkflowGridFSDaoImpl extends BaseGridFSDaoImpl implements WorkflowGridFSDao {

	@Autowired
	private GridFsTemplate workflowGridTemplate;

	@Override
	public GridFsTemplate getFSTemplate() {
		return workflowGridTemplate;
	}

}
