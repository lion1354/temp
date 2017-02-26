package com.tibco.ma.engine.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import com.tibco.ma.common.SpringBeanUtil;
import com.tibco.ma.engine.WorkflowEngine;
import com.tibco.ma.engine.WorkflowException;
import com.tibco.ma.model.Connector;
import com.tibco.ma.model.Parameters;
import com.tibco.ma.model.Work;
import com.tibco.ma.service.ConnectorService;
import com.tibco.ma.service.ParametersService;

/**
 * 
 * @author Aidan
 *
 */
@Component("execution")
public class Execution implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4703439702415036381L;

	private WorkflowEngine engine;

	private ConnectorService connectorService;

	private ParametersService parametersService;

	private Work work;

	private Work parentWork;

	private String parentNodeName;

	private List<Connector> connectorList = new ArrayList<Connector>();

	private List<Parameters> parametersList = new ArrayList<Parameters>();

	private List<String> modelList = new ArrayList<String>();

	private List<Object> outPut = new ArrayList<Object>();

	private List<Object> inPut = new ArrayList<Object>();

	public Execution(){
		
	}

	/**
	 * 
	 * @param execution
	 * @param work
	 * @param parentNodeName
	 * @throws Exception
	 */
	Execution(Work work) throws Exception {
		if (work == null) {
			throw new WorkflowException("Error : work is null");
		}
		if(engine == null)
			engine = (WorkflowEngine) SpringBeanUtil.getBean("workflowEngine");
		if(connectorService == null)
			connectorService = (ConnectorService) SpringBeanUtil.getBean("connectorService");
		if(parametersService == null)
			parametersService = (ParametersService) SpringBeanUtil.getBean("parametersService");
		this.work = work;
		List<Map<String, Object>> connectors = work.getConnectors();
		for (Map<String, Object> conn : connectors) {
			Connector connector = connectorService
					.findById(new ObjectId(conn.get("connectorId") + ""),
							Connector.class);
			connectorList.add(connector);
			parametersList.add(parametersService.findById(
					new ObjectId(conn.get("parameterId") + ""),
					Parameters.class));
			modelList.add(getModelName(connector.getName()));
		}

	}

	/**
	 * 
	 * @param execution
	 * @param work
	 * @param parentNodeName
	 * @throws Exception
	 */
	Execution(Work work, String parentNodeName) throws Exception {
		if (work == null || parentNodeName == null) {
			throw new WorkflowException(
					"Error : work or parentNodeName is null");
		}
		if(engine == null)
			engine = (WorkflowEngine) SpringBeanUtil.getBean("workflowEngine");
		if(connectorService == null)
			connectorService = (ConnectorService) SpringBeanUtil.getBean("connectorService");
		if(parametersService == null)
			parametersService = (ParametersService) SpringBeanUtil.getBean("parametersService");
		this.work = work;
		this.parentNodeName = parentNodeName;
		List<Map<String, Object>> connectors = work.getConnectors();
		for (Map<String, Object> conn : connectors) {
			Connector connector = connectorService
					.findById(new ObjectId(conn.get("connectorId") + ""),
							Connector.class);
			connectorList.add(connector);
			parametersList.add(parametersService.findById(
					new ObjectId(conn.get("parameterId") + ""),
					Parameters.class));
			modelList.add(getModelName(connector.getName()));
		}
	}

	private String getModelName(String model) {
		return "com.tibco.ma.engine.model." + toUpperCaseFirstOne(model)
				+ "Model";
	}

	private String toUpperCaseFirstOne(String s) {
		StringBuilder sb = new StringBuilder();
		sb.append(Character.toUpperCase(s.charAt(0)));
		sb.append(s.substring(1).toLowerCase());
		return sb.toString();
	}

	/**
	 * 
	 * @param execution
	 * @param work
	 * @param parentNodeName
	 * @return
	 * @throws Exception
	 */
	public Execution createSubExecution(Work work, String parentNodeName)
			throws Exception {
		return new Execution(work, parentNodeName);
	}

	public WorkflowEngine getEngine() {
		return engine;
	}

	public void setEngine(WorkflowEngine engine) {
		this.engine = engine;
	}

	public Work getWork() {
		return work;
	}

	public void setWork(Work work) {
		this.work = work;
	}

	public Work getParentWork() {
		return parentWork;
	}

	public void setParentWork(Work parentWork) {
		this.parentWork = parentWork;
	}

	public String getParentNodeName() {
		return parentNodeName;
	}

	public void setParentNodeName(String parentNodeName) {
		this.parentNodeName = parentNodeName;
	}

	public List<Connector> getConnectorList() {
		return connectorList;
	}

	public void setConnectorList(List<Connector> connectorList) {
		this.connectorList = connectorList;
	}

	public List<Parameters> getParametersList() {
		return parametersList;
	}

	public void setParametersList(List<Parameters> parametersList) {
		this.parametersList = parametersList;
	}

	public List<String> getModelList() {
		return modelList;
	}

	public void setModelList(List<String> modelList) {
		this.modelList = modelList;
	}

	public List<Object> getOutPut() {
		return outPut;
	}

	public void setOutPut(List<Object> outPut) {
		this.outPut = outPut;
	}

	public List<Object> getInPut() {
		return inPut;
	}

	public void setInPut(List<Object> inPut) {
		this.inPut = inPut;
	}
}
