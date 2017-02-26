package com.tibco.ma.engine;

import com.tibco.ma.engine.core.Execution;
import com.tibco.ma.model.Connector;
import com.tibco.ma.model.Parameters;

/**
 * 
 * @author Aidan
 *
 */
public interface Action {

	/**
	 * 
	 * @param execution
	 */
	public void execute(Execution execution, Connector connector, Parameters param);
}
