package com.tibco.ma.service;

import com.tibco.ma.model.Device;

public interface DeviceService extends BaseService<Device> {
	void save(Device device) throws Exception;
}
