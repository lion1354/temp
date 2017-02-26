package com.tibco.ma.service;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tibco.ma.common.DateQuery;
import com.tibco.ma.common.ImageUtils;
import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.dao.NotificationResDao;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.model.NotificationRes;

@Service
public class NotificationResServiceImpl extends
		BaseServiceImpl<NotificationRes> implements NotificationResService {

	@Resource
	private NotificationResDao dao;
	@Resource
	private ResourceGridFSService resourceGridFSService;

	@Override
	public BaseDao<NotificationRes> getDao() {
		return dao;
	}

	@Override
	public void saveResource(MultipartFile file, NotificationRes res)
			throws Exception {
		if(file!=null){
			String myFileName = file.getOriginalFilename();
			String type = res.getType();
			String fileName = ImageUtils.createResName(type, myFileName);
			String imgUrl = resourceGridFSService.save(
					file.getInputStream(), fileName, null);
			res.setUrl(imgUrl);
			res.setSize(file.getSize()+"");
			res.setName(fileName);
			
			String contentType = file.getContentType();
			String[] suffixs = contentType.split("/");
			res.setFiletype(suffixs[1]);
			String time = DateQuery.formatPSTDatetime(System
					.currentTimeMillis());
			res.setTime(time);
			dao.save(res);
		}	
	}

	@Override
	public void deleteResourceByID(String id) throws Exception {
		NotificationRes notificationRes = dao.findById(new ObjectId(id), NotificationRes.class);
		if(notificationRes!=null){
			Query queryFS = new Query();
			queryFS.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
					new ObjectId(notificationRes.getUrl())));
			resourceGridFSService.deleteSFDBFile(queryFS);
		}
		Query query = new Query(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId(id)));
		dao.remove(query, NotificationRes.class);
	}

	
}
