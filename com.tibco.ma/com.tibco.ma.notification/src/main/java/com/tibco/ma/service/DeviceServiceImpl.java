package com.tibco.ma.service;

import java.util.Date;

import javax.annotation.Resource;

import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.tibco.ma.common.StringUtil;
import com.tibco.ma.dao.AppDao;
import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.dao.DeviceDao;
import com.tibco.ma.model.Device;

@Service
public class DeviceServiceImpl extends BaseServiceImpl<Device> implements
		DeviceService {

	@Resource
	private DeviceDao dao;

	@Resource
	private AppDao appDao;

	@Override
	public BaseDao<Device> getDao() {
		return dao;
	}

	@Override
	public void save(Device device) throws Exception {
		Query deviceQuery = new Query();
		deviceQuery.addCriteria(Criteria.where("appId").is(device.getAppId()));
		deviceQuery.addCriteria(Criteria.where("deviceId").is(
				device.getDeviceId()));
		Device exDevice = dao.findOne(deviceQuery, Device.class);
		if (exDevice != null) {
			device.setId(exDevice.getId());
		}

		if (StringUtil.isEmpty(device.getId())) {
			Date date = new Date();
			device.setCreateDatetime(date);
			device.setUpdateDatetime(date);
			device.setIsActive(true);
			dao.save(device);
		} else {
			Document document = new Document();
			if (StringUtil.notEmpty(device.getDeviceToken())) {
				document.append("deviceToken", device.getDeviceToken());
			}
			if (StringUtil.notEmpty(device.getOs())) {
				document.append("os", device.getOs());
			}
			if (StringUtil.notEmpty(device.getOsVersion())) {
				document.append("osVersion", device.getOsVersion());
			}
			if (StringUtil.notEmpty(device.getDevice())) {
				document.append("device", device.getDevice());
			}
			if (StringUtil.notEmpty(device.getClientAgent())) {
				document.append("clientAgent", device.getClientAgent());
			}
			if (StringUtil.notEmpty(device.getUserId())) {
				document.append("userId", device.getUserId());
			}
			if (device.getExtensions() != null) {
				document.append("extensions", device.getExtensions());
			}

			document.append("updateDatetime", new Date());
			updateById("device", device.getId(), document);
		}

	}

}
