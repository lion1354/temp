package com.tibco.ma.service;

import java.io.InputStream;
import java.util.Map;

public interface ResourceGridFSService extends BaseGridFSService{
	public String save(InputStream inputStream, String fileName,
			Map<String, Object> map);
}
