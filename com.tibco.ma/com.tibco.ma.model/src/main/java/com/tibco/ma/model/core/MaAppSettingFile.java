package com.tibco.ma.model.core;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bson.Document;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
public class MaAppSettingFile {
	private String fileId;
	private String fileName;
	private String filePath;

	public MaAppSettingFile(String fileId, String fileName, String filePath) {
		this.fileId = fileId;
		this.fileName = fileName;
		this.filePath = filePath;
	}

	public MaAppSettingFile(Document document) {
		this.fileId = document.getString("fileId");
		this.fileName = document.getString("fileName");
		this.filePath = document.getString("filePath");
	}

	public String getFileId() {
		return fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("fileId", fileId);
		map.put("fileName", fileName);
		map.put("filePath", filePath);
		return map;
	}
}
