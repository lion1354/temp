package com.tibco.ma.engine.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tibco.ma.engine.core.Execution;
import com.tibco.ma.model.Connector;
import com.tibco.ma.model.Parameters;

public class FileModel extends BaseModel {
	private static Logger log = LoggerFactory.getLogger(FileModel.class);

	@Override
	protected void exec(Execution execution, Connector connector,
			Parameters param) {
		log.info("this is file model");
		execution.getOutPut().add("file model");
	}

}
