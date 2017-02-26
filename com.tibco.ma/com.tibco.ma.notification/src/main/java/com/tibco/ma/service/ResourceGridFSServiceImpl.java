package com.tibco.ma.service;

import java.io.InputStream;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mongodb.gridfs.GridFSFile;
import com.tibco.ma.dao.BaseGridFSDao;
import com.tibco.ma.dao.ResourceGridFSDao;

@Service
public class ResourceGridFSServiceImpl extends BaseGridFSServiceImpl implements
		ResourceGridFSService {
	
	@Resource
	private ResourceGridFSDao resourceGridFSDao;
	
	@Override
	public String save(InputStream inputStream, String fileName,
			Map<String, Object> map) {
		GridFSFile gridFSFile = resourceGridFSDao.saveFile(inputStream,
				fileName);
		if (gridFSFile != null) {
			if (map != null) {
				for (String key : map.keySet()) {
					gridFSFile.put(key, map.get(key));
				}
			}
			gridFSFile.save();
			return gridFSFile.getId().toString();
		}
		return null;
	}

	@Override
	public BaseGridFSDao getDao() {
		return resourceGridFSDao;
	}

}
