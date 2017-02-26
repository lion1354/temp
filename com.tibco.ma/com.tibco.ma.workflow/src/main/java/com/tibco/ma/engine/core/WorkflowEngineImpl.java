package com.tibco.ma.engine.core;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.tibco.ma.engine.WorkflowEngine;
import com.tibco.ma.engine.model.BaseModel;
import com.tibco.ma.model.Connector;
import com.tibco.ma.model.Parameters;
import com.tibco.ma.model.Work;
import com.tibco.ma.service.ConnectorService;
import com.tibco.ma.service.WorkService;

/**
 * 
 * @author Aidan
 *
 */
@Service("workflowEngine")
public class WorkflowEngineImpl implements WorkflowEngine {

	private static Logger log = LoggerFactory
			.getLogger(WorkflowEngineImpl.class);

	@Resource
	private WorkService workService;

	@Resource
	private ConnectorService connectorService;

	public Execution startWorkflowById(String workId) {
		Work work = null;
		try {
			work = workService.findById(new ObjectId(workId), Work.class);
			return startWorkflow(work);
		} catch (Exception e) {
			log.info("Error : " + e);
			return null;
		}
	}

	public Execution startWorkflowByName(String workName, String appId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(workName));
		query.addCriteria(Criteria.where("appId").is(appId));
		Work work = null;
		try {
			work = workService.findOne(query, Work.class);
			return startWorkflow(work);
		} catch (Exception e) {
			log.info("Error : " + e);
			return null;
		}
	}

	public Execution executeTask(Execution execution, Connector connector, Parameters param,
			String model) {
		getModel(model).execute(execution, connector, param);
		return execution;
	}

	private Execution startWorkflow(Work work) throws Exception {
		Execution execution = execution(work, null, null);
		if (execution.getConnectorList().size() != 0
				&& execution.getParametersList().size() != 0
				&& execution.getModelList().size() != 0) {
			int i = 0;
			while(i < execution.getConnectorList().size()){
				execution = executeTask(execution, execution.getConnectorList().get(i), execution
						.getParametersList().get(i), execution.getModelList()
						.get(i));
				i++;
			}
			return execution;
		} else {
			throw new Exception("Start workflow error!");
		}
	}

	private Execution execution(Work work, String parentId,
			String parentNodeName) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("create workflow order!");
		}
		Execution execution = new Execution(work);
		return execution;
	}
	
	public BaseModel getModel(String model) {
		Class<?> c = null;
		try {
			c = Class.forName(model);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		BaseModel base = null;
		try {
			base = (BaseModel)c.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return base;
	}
	
}
