package com.tibco.ma.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.gridfs.GridFSFile;
import com.tibco.ma.dao.BaseGridFSDao;
import com.tibco.ma.model.MongoDBConstants;

/**
 * 
 * @author qiankun
 * @Time 2015-05-26
 * @see Core module save
 * 
 */
@Service
public class CoreGridFSServiceImpl extends BaseGridFSServiceImpl implements
		CoreGridFSService {
	/** CoreGrindFSDaoImpl **/
	@Autowired
	private BaseGridFSDao coreGridFSDaoImpl;

	/**
	 * @param InputStream
	 * @param fileName
	 * @param Map
	 *            <String,Object> map
	 * 
	 * @return Map: fileUrl and md5
	 * 
	 */
	@Override
	public Map<String, Object> save(InputStream inputStream, String fileName,
			Map<String, Object> map) {
		Map<String, Object> mapResult = new HashMap<String, Object>();
		GridFSFile gridFSFile = coreGridFSDaoImpl.saveFile(inputStream,
				fileName);
		if (gridFSFile != null) {
			if (map != null) {
				for (String key : map.keySet()) {
					gridFSFile.put(key, map.get(key));
				}
			}
			gridFSFile.save();
			mapResult.put("fileUrl", gridFSFile.getId().toString());
			mapResult.put("md5", gridFSFile.getMD5());
		}
		return mapResult;

	}

	@Override
	public BaseGridFSDao getDao() {
		return coreGridFSDaoImpl;
	}

	@Override
	public String save(InputStream inputStream, String fileName) {
		GridFSFile gridFSFile = coreGridFSDaoImpl.saveFile(inputStream,
				fileName);
		if (gridFSFile != null) {
			gridFSFile.save();
			return gridFSFile.getId().toString();
		}
		return null;
	}

	/**
	 * 
	 * 
	 * @author qiankun
	 * @param id
	 *            :gridFS id
	 * 
	 * @return void
	 * 
	 * 
	 */
	@Override
	public void deleteById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId(id)));
		coreGridFSDaoImpl.deleteSFDBFile(query);
	}

}
