package com.tibco.ma.engine;

public class WorkflowException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7069606877306782672L;

	public WorkflowException() {
		super();
	}

	public WorkflowException(String msg, Throwable cause) {
		super(msg);
		super.initCause(cause);
	}

	public WorkflowException(String msg) {
		super(msg);
	}

	public WorkflowException(Throwable cause) {
		super();
		super.initCause(cause);
	}

	
}
