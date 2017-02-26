package com.popular.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.popular.model.FileType;

public interface FileService {

	List<String> saveFile(HttpServletRequest request, String userId, FileType fileType)
			throws Exception;

	void deleteFile(String filePath);

	String getFilePathFromUrl(HttpServletRequest request, String url);
	
	String getFileUrlFromPath(String path);

}
