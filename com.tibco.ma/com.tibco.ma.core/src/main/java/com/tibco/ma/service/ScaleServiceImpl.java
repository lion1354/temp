package com.tibco.ma.service;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.tibco.ma.common.StringUtil;
import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.dao.ScaleDao;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.model.ScaleStaticsValue;

@Service
public class ScaleServiceImpl extends BaseServiceImpl<ScaleStaticsValue> implements ScaleService {
	
	@Resource
	private ScaleDao dao;
	
	@Override
	public BaseDao<ScaleStaticsValue> getDao() {
		return dao;
	}
	
	@Override
	public ScaleStaticsValue saveValue(ScaleStaticsValue value){
		if(StringUtil.isEmpty(value.getId())){
			value.setId(null);
			dao.save(value);
		}else{
			Query query = new Query(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId(value.getId())));
			Update update = Update.update("orderCnt", value.getOrderCnt());
			update.set("name", value.getName());
			update.set("status", value.getStatus());
			dao.update(query, update, ScaleStaticsValue.class);
		}
		return value;
	}
	
	public void updateStatus(String id){
		Query query = new Query(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId(id)));
		Update update = Update.update("status", 0);
		dao.update(query, update, ScaleStaticsValue.class);
	}

}
