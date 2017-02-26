package com.tibco.ma.service;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.tibco.ma.common.StringUtil;
import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.dao.ConnectorDao;
import com.tibco.ma.model.Connector;
/**
 * 
 * @author Aidan
 *
 */
@Service("connectorService")
public class ConnectorServiceImpl extends BaseServiceImpl<Connector> implements ConnectorService {

	@Resource
	private ConnectorDao dao;
	
	@Resource
	private WorkflowGridFSService gridfsService;

	@Override
	public BaseDao<Connector> getDao() {
		return dao;
	}

	public String updateImage(String id, String deleteUrl,
			CommonsMultipartFile logo) throws Exception {
		String iconUrl = null;
		// don't update icon
		if (StringUtil.notEmpty(id) && StringUtil.isEmpty(deleteUrl)
				&& logo == null) {
			Connector old = findById(new ObjectId(id), Connector.class);
			if (old != null) {
				iconUrl = old.getIconUrl();
			}
		}
		// delete icon
		if (StringUtil.notEmpty(deleteUrl)) {
			gridfsService.deleteById(deleteUrl);
			iconUrl = null;
		}
		// update icon
		if (logo != null) {
			iconUrl = gridfsService.save(logo.getInputStream(),
					logo.getOriginalFilename());
		}

		return iconUrl;
	}

}
