package com.tibco.ma.model.core;

import java.util.Map;

public interface MaDataType {

	public static final String KEY_TYPE = "__type";
	public static final String KEY_VALUE = "__value";

	public Map<String, Object> toMap();
}
