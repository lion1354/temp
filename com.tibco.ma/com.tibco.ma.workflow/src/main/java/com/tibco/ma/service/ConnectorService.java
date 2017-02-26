package com.tibco.ma.service;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.tibco.ma.model.Connector;

/**
 * 
 * @author Aidan
 *
 */
public interface ConnectorService extends BaseService<Connector> {

	public String updateImage(String id, String deleteUrl,
			CommonsMultipartFile logo) throws Exception;

}
