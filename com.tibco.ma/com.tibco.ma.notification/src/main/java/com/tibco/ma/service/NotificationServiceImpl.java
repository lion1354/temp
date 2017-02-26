package com.tibco.ma.service;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.tibco.ma.common.DateQuery;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.dao.NotificationDao;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.model.PNTask;

@Service
public class NotificationServiceImpl extends BaseServiceImpl<PNTask> implements
		NotificationService {

	private static Logger log = LoggerFactory
			.getLogger(NotificationServiceImpl.class);

	@Resource
	private NotificationDao dao;

	@Override
	public BaseDao<PNTask> getDao() {
		return dao;
	}

	@Override
	public void save(PNTask pnTask) throws Exception {
		if (StringUtil.isEmpty(pnTask.getId())) {
			String send_time = DateQuery.formatPSTDatetime(System
					.currentTimeMillis());
			pnTask.setSend_time(send_time);
			//pnTask.setMsg_type(1);
			dao.save(pnTask);
		} else {
			Query query = new Query();
			query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(
					new ObjectId(pnTask.getId())));
			Update update = Update
					.update("content", pnTask.getContent())
					.set("preview", pnTask.getPreview())
					.set("app", pnTask.getApp())
					.set("msg_type", pnTask.getMsg_type())
					.set("timed", pnTask.getTimed())
					.set("url", pnTask.getUrl())
					.set("deviceOS", pnTask.getDeviceOS())
					//.set("in_stadium", pnTask.getIn_stadium())
					.set("send_time", pnTask.getSend_time())
					//.set("resend", pnTask.isResend())
					.set("status", pnTask.getStatus());
					//.set("scope", pnTask.getScope())
					//.set("latest_status_date_time",pnTask.getLatest_status_date_time());
			dao.update(query, update, PNTask.class);
		}
	}
	
	
	/*
	 private static PNTask jsonToObj(JSONObject json) {
		PNTask pnTask = new PNTask();

		if (json.containsKey("id")) {
			pnTask.setId(json.get("id").toString());
		}
		if (json.containsKey("appID")) {
			String appid = json.get("appID").toString();
			App app = new App(appid);
			pnTask.setApp(app);
		}
		if (json.containsKey("preview")) {
			pnTask.setPreview(json.get("preview").toString());
		}
		if (json.containsKey("content")) {
			pnTask.setContent(json.get("content").toString());
		}
		if (json.containsKey("msg_type")) {
			pnTask.setMsg_type(Integer
					.parseInt(json.get("msg_type").toString()));
		}
		if (json.containsKey("timed")) {
			pnTask.setTimed(json.get("timed").toString());
		}
		if (json.containsKey("url")) {
			pnTask.setUrl(json.get("url").toString());
		}
		if (json.containsKey("deviceOS")) {
			pnTask.setDeviceOS(json.get("deviceOS").toString());
		}
		if (json.containsKey("in_stadium")) {
			pnTask.setIn_stadium(json.get("in_stadium").toString());
		}
		if (json.containsKey("send_time")) {
			pnTask.setSend_time(json.get("send_time").toString());
		}
		if (json.containsKey("resend")) {
			if ("true".equals(json.get("resend").toString())) {
				pnTask.setResend(true);
			} else {
				pnTask.setResend(false);
			}
		}
		if (json.containsKey("status")) {
			pnTask.setStatus(Integer.parseInt(json.get("status").toString()));
		}
		if (json.containsKey("scope")) {
			pnTask.setScope(Integer.parseInt(json.get("scope").toString()));
		}
		if (json.containsKey("latest_status_date_time")) {
			pnTask.setLatest_status_date_time(json.get(
					"latest_status_date_time").toString());
		}
		return pnTask;
	}
	 */
}
