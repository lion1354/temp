package com.tibco.ma.service;

import org.springframework.web.multipart.MultipartFile;

import com.tibco.ma.model.NotificationRes;

public interface NotificationResService extends BaseService<NotificationRes> {
	
	public void saveResource(MultipartFile file, NotificationRes res)
			throws Exception; 
	
	public void deleteResourceByID(String id) throws  Exception;
}
