package com.tibco.ma.engine;

import com.tibco.ma.engine.core.Execution;
import com.tibco.ma.model.Connector;
import com.tibco.ma.model.Parameters;

/**
 * workflow engine
 * @author Aidan
 *
 */
public interface WorkflowEngine {

	/**
	 * start workflow by work id
	 * @param workId
	 * @return
	 */
	public Execution startWorkflowById(String workId);
	
	/**
	 * start workflow by work name
	 * @param workName
	 * @return
	 */
	public Execution startWorkflowByName(String workName, String appId);
	
	/**
	 * execute task
	 * @param connectorId
	 * @return
	 */
	public Execution executeTask(Execution execution, Connector connector, Parameters param, String model);
	
}
