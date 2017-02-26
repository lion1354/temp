package com.tibco.ma.engine.model;

import com.tibco.ma.engine.Action;
import com.tibco.ma.engine.core.Execution;
import com.tibco.ma.model.Connector;
import com.tibco.ma.model.Parameters;

public abstract class BaseModel implements Action {
	
	protected abstract void exec(Execution execution, Connector connector, Parameters param);

	public void execute(Execution execution, Connector connector, Parameters param) {
		exec(execution, connector, param);
	}

}
