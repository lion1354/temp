package com.tibco.ma.service;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mongodb.client.result.DeleteResult;
import com.tibco.ma.common.Constants;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.common.ValidateUtils;
import com.tibco.ma.common.query.Pager;
import com.tibco.ma.dao.AppSettingDao;
import com.tibco.ma.dao.BaseDao;
import com.tibco.ma.model.AppSetting;
import com.tibco.ma.model.AppSettingType;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.model.core.MaAppSettingFile;
import com.tibco.ma.model.core.MaDataType;
import com.tibco.ma.model.core.MaDate;
import com.tibco.ma.model.core.MaGeoPoint;

/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
@Service
public class AppSettingServiceImpl extends BaseServiceImpl<AppSetting>implements AppSettingService {

	private static Logger log = LoggerFactory.getLogger(AppSettingServiceImpl.class);
	@Resource
	private AppSettingDao dao;

	@Resource
	private FileService fileService;

	@Resource
	private CoreGridFSService coreGridFSService;

	@Override
	public BaseDao<AppSetting> getDao() {
		return dao;
	}

	@Override
	public String validateSaveValue(String value, String type) throws Exception {
		AppSettingType appSettingType = AppSettingType.valueOf(type);
		String message = null;
		if (appSettingType.equals(AppSettingType.Number)) {
			Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
			if (!pattern.matcher(value).matches()) {
				message = "It's not number. Please re-enter the number!";
			}
		} else if (appSettingType.equals(AppSettingType.Object)) {
			try {
				Document.parse(value);
			} catch (Exception e) {
				message = "It's not Object. Please re-enter the Object!";
			}
		}
		return message;
	}

	private void getSaveDocByType(Document document, String value, String type) throws Exception {
		AppSettingType appSettingType = AppSettingType.valueOf(type);
		switch (appSettingType) {
		// {"value":"${value}"}
		case String: {
			document.append("value", value);
			break;
		}
			// {"key":"value"}
		case Boolean: {
			document.append("value", Boolean.valueOf(value));
			break;
		}
			// {"key":"value"}
		case Number: {
			if (value.indexOf(".") != -1)
				document.append("value", Double.valueOf(value));
			else
				document.append("value", Long.valueOf(value));
			break;
		}
			// {"${colName}" : {"latitude" : "${latitude}", "longitude" :
			// "${longitude}"}}
		case GeoPoint: {
			MaGeoPoint maGeoPoint = new MaGeoPoint(Document.parse(value));
			Map<String, Object> map = maGeoPoint.toMap();
			if (map.containsKey(MaDataType.KEY_TYPE)) {
				map.remove(MaDataType.KEY_TYPE);
			}
			document.append("value", map);
			break;
		}
		case Date: {
			MaDate maDate = new MaDate(value);
			Map<String, Object> result = maDate.toMap();
			if (result.containsKey(MaDataType.KEY_TYPE)) {
				result.remove(MaDataType.KEY_TYPE);
			}
			document.append("value", result);
			break;
		}
		case Array: {
			try {
				Document valueDoc = Document.parse("{__value : " + value + "}");
				document.append("value", valueDoc.get("__value"));
			} catch (Exception e) {
				document.append("value", value);
			}
			break;
		}
		case File: {
			MaAppSettingFile appSettingFile = new MaAppSettingFile(Document.parse(value));
			document.append("value", appSettingFile.toMap());
			break;
		}
		case Object: {
			Document valueDoc = Document.parse(value);
			document.append("value", valueDoc);
			break;
		}
		default:
			break;
		}
	}

	private void deleteDocByType(AppSettingType type, Document document) {
		switch (type) {
		case File:
			String ids = ((Document) document.get("value")).getString("fileId");
			fileService.deleteFileById(ids, coreGridFSService);
			break;
		default:
			break;
		}
	}

	@Override
	public void save(String collectionName, String id, String appId, String value, String type, String key,
			String description) throws Exception {
		Document document = new Document("appId", new ObjectId(appId));
		if (StringUtil.isEmpty(id)) {
			//
			Document filter = new Document();
			filter.append("appId", new ObjectId(appId));
			filter.append("key", key);
			List<Document> results = queryAppSetting(collectionName, filter, null);
			if (results == null || results.size() < 1) {
				document.append("type", type).append("key", key).append("description", description);
				getSaveDocByType(document, value, type);
				insertOne(collectionName, document);
			} else {
				throw new Exception("key is exist");
			}
		} else {
			document.append("type", type).append("key", key).append("description", description);
			getSaveDocByType(document, value, type);
			updateById(collectionName, id, document);
		}

	}

	@Override
	public DeleteResult deleteValue(String collectionName, String id) throws Exception {
		Document valueDoc = getOneById(collectionName, id);
		String appId = valueDoc.get("appId").toString();
		AppSettingType type = AppSettingType.valueOf(valueDoc.get("type").toString());
		deleteDocByType(type, valueDoc);
		return deleteById(collectionName, id);
	}

	@Override
	public Pager<Document> valuesPage(String collectionName, Document filter, Document sort, int page, int pageSize,
			String appId, String projectURI) throws Exception {
		Pager<Document> pager = new Pager<Document>();
		pager.setNowpage(page);
		pager.setPagesize(pageSize);
		log.debug("query values ,filter :{},", filter.toJson());
		List<Document> docList = getDao().page(collectionName, filter, sort, page, pageSize);

		long count = getDao().count(collectionName, filter);
		// List<Document> list = new ArrayList<Document>();
		// for (Document doc : docList) {
		// for (Column col : coils) {
		// if (col.getColType().equals(EntityColType.Date)
		// && doc.containsKey(col.getColName())) {
		// String time = ((Document) doc.get(col.getColName()))
		// .getString("iso");
		// ((Document) doc.get(col.getColName())).put("iso",
		// DateQuery.getTimeZoneDatetime(time, timeZone));
		// }
		// }
		// doc.put(MongoDBConstants.DOCUMENT_ID,
		// doc.getObjectId(MongoDBConstants.DOCUMENT_ID).toString());
		// doc.put("appId", doc.getObjectId("appId").toString());
		// doc.put("createAt", DateQuery.getTimeZoneDatetime(
		// doc.getString("createAt"), timeZone));
		// doc.put("updateAt", DateQuery.getTimeZoneDatetime(
		// doc.getString("updateAt"), timeZone));
		// list.add(doc);
		// }

		pager.setCountrow(count);
		pager.setMaxPageIndex(10);
		long countpage = pager.getCountrow() % pager.getPagesize() == 0 ? pager.getCountrow() / pager.getPagesize()
				: pager.getCountrow() / pager.getPagesize() + 1;
		pager.setCountpage(countpage);
		pager.showPage();
		pager.setData(docList);
		return pager;
	}

	@Override
	public List<Document> queryAppSetting(String collectionName, Document filter, Document sort) throws Exception {
		// initAppConfig(appId, Constants.APP_CONFIG_TIMEZONE, TimeZone
		// .getDefault().getDisplayName(false, TimeZone.SHORT));
		//
		// Document appSettingDoc = queryByIdTypeAndAppId("app_setting", appId,
		// "String", Constants.APP_CONFIG_TIMEZONE);
		//
		// String timeZone = appSettingDoc.getString("value");

		List<Document> docList = getDao().query(collectionName, filter, sort);
		// List<Document> list = new ArrayList<Document>();
		// for (Document doc : docList) {
		// doc.put(MongoDBConstants.DOCUMENT_ID,
		// doc.getObjectId(MongoDBConstants.DOCUMENT_ID).toString());
		// doc.put("appId", doc.getObjectId("appId").toString());
		// list.add(doc);
		// }
		return docList;
	}

	@Override
	public Document queryByIdTypeAndAppId(String collectionName, String appId, String type, String key)
			throws Exception {
		Document filter = new Document();
		filter.append("appId", new ObjectId(appId));
		filter.append("key", key);
		filter.append("type", type);
		List<Document> lists = getDao().query(collectionName, filter, null);
		if (ValidateUtils.isValidate(lists)) {
			return lists.get(0);
		} else {
			return null;
		}

	}

	private void initAppConfig(String appId, String key, String value) throws Exception {
		// init time zone
		// ConfigProperty configProperty = configPropertyService
		// .queryByKeyAndAppId(Constants.APP_CONFIG_TIMEZONE, appId);
		// if (configProperty == null
		// || StringUtil.isEmpty(configProperty.getValue())) {
		// configProperty = new ConfigProperty();
		// configProperty.setKey(key);// Constants.APP_CONFIG_TIMEZONE
		// configProperty.setValue(value);//
		// TimeZone.getDefault().getDisplayName(false,TimeZone.SHORT)
		// configPropertyService.saveConfigProperty(configProperty, appId);
		// }

		Document doc = queryByIdTypeAndAppId("app_setting", appId, "String", Constants.APP_CONFIG_TIMEZONE);
		if (doc == null || StringUtil.isEmpty(doc.getString("value"))) {
			save("app_setting", null, appId, value, "String", key, "TIMEZONE");
		}
	}

	private void getSaveDocType(Document document, Object value, String type) {
		AppSettingType appSettingType = AppSettingType.valueOf(type);
		switch (appSettingType) {
		case String: {
			document.append("value", value);
			break;
		}
		case Number: {
			document.append("value", value);
			break;
		}
		case Boolean: {
			document.append("value", value);
			break;
		}
		case Date: {
			MaDate maDate = new MaDate(value);
			Map<String, Object> result = maDate.toMap();
			if (result.containsKey(MaDataType.KEY_TYPE)) {
				result.remove(MaDataType.KEY_TYPE);
			}
			document.append("value", result);
			break;
		}
		case File: {
			if (value instanceof Document) {
				MaAppSettingFile appSettingFile = new MaAppSettingFile(document);
				document.append("value", appSettingFile.toMap());
			}
			break;
		}
		case GeoPoint: {
			if (value instanceof Document) {
				MaGeoPoint maGeoPoint = new MaGeoPoint((Document) value);
				Map<String, Object> map = maGeoPoint.toMap();
				if (map.containsKey(MaDataType.KEY_TYPE)) {
					map.remove(MaDataType.KEY_TYPE);
				}
				document.append("value", map);
			}
			break;
		}
		case Array: {
			document.append("value", value);
			break;
		}
		case Object: {
			document.append("value", value);
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void save(String collection, Document values) throws Exception {
		Document document = new Document();
		document.append("appId", new ObjectId((String) values.get("appId")));
		String type = values.getString("type");
		document.append("type", type);
		String key = values.getString("key");
		document.append("key", key);
		String description = values.getString("description");
		document.append("description", description);
		Object value = values.get("value");
		getSaveDocType(document, value, type);
		insertOne(collection, document);
	}

	@Override
	public void update(String collection, String id, Document values) throws Exception {
		Document document = new Document();
		document.append("appId", new ObjectId((String) values.get("appId")));
		String type = values.getString("type");
		document.append("type", type);
		String key = values.getString("key");
		document.append("key", key);
		String description = values.getString("description");
		document.append("description", description);
		Object value = values.get("value");
		getSaveDocType(document, value, type);
		updateById(collection, id, document);
	}

	@Override
	public void deleteByKey(String collection, String key, String appId) throws Exception {
		Document document = new Document();
		if (StringUtil.notEmpty(appId)) {
			document.append("appId", new ObjectId(appId));
		}
		if (StringUtil.notEmpty(key)) {
			document.append("key", key);
		}
		if (!document.isEmpty()) {
			List<Document> documentsResult = dao.query(collection, document, null);
			if (documentsResult != null && documentsResult.size() > 0) {
				for (Document valueDoc : documentsResult) {
					String resultAppId = valueDoc.get("appId").toString();
					AppSettingType type = AppSettingType.valueOf(valueDoc.get("type").toString());
					deleteDocByType(type, valueDoc);
					String id = valueDoc.getString(MongoDBConstants.DOCUMENT_ID);
					deleteById(collection, id);
				}
			}
		}
	}
}
