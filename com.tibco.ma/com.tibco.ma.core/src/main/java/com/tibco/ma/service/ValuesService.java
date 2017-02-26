package com.tibco.ma.service;

import java.util.List;

import org.bson.Document;

import com.tibco.ma.common.query.Pager;
import com.tibco.ma.model.core.EntityColType;
import com.tibco.ma.model.core.MaGeoPoint;

@SuppressWarnings("rawtypes")
public interface ValuesService extends BaseService {
	public String getValuesCollectionName(String appId, String className);

	public String getRelValuesCollectionName(String appId, String ownClassName,
			String colName);

	public String getGeoIndexName(String appId, String className, String colName);

	public void deleteValue(String appId, String id, String entityId,
			String ownEntityId, String ownColName) throws Exception;

	public void save(String appId, String id, String entityId, String colName,
			String value, String ownEntityId, String ownValuesId,
			String ownColName) throws Exception;

	public Document save(String appId, String entityId, String className,
			Document values) throws Exception;

	public void update(Document entity, String id, Document document)
			throws Exception;

	public Pager<Document> valuesPage(String appId, String entityId,
			Document filter, Document sort, int page, int pageSize)
			throws Exception;

	public List<Document> valuesQuery(String appId, String collectionName,
			Document filter, Document sort, Integer skip, Integer limit,
			Document projection, String[] includeArray) throws Exception;

	public EntityColType judgeType(Object obj);

	public void relValues(String appId, String className, String owningId,
			Object relIdObj, String colName, EntityColType relType)
			throws Exception;

	public List<Document> query(String appId, String className,
			Document filter, Document keys, Document sort);

	public void createGeoIndex(String appId, String entityId, String colName)
			throws Exception;

	public void dropGeoIndex(String appId, String className, String colName)
			throws Exception;

	/*
	 *  add by Aaron. 2015/8/21
	 */
	
	/**
	 * Query the coordinates of the points in the area of the circular, you need
	 * to specify the center coordinates and radius.
	 * 
	 * @param collection
	 * @param locField
	 * @param center
	 * @param radius
	 * @param limit
	 * @return List<Document>
	 * @throws Exception
	 */
	public List<Document> findWithCircle(String collectionName,
			String locField, MaGeoPoint center, double radius, Integer limit)
			throws Exception;

	/**
	 * Query the coordinates from the area of a rectangular, the method is only
	 * in 2d index support.
	 * 
	 * @param collection
	 * @param locField
	 * @param bottomLeft
	 * @param upperRight
	 * @param limit
	 * @return List<Document>
	 * @throws Exception
	 */
	public List<Document> findWithBox(String collection, String locField,
			MaGeoPoint bottomLeft, MaGeoPoint upperRight, Integer limit)
			throws Exception;

	/**
	 * Query in all coordinates of points within a closed polygon is specified,
	 * the given polygon must first connect to form a closed polygon coordinates
	 * Such as a triangle final LinkedList<MaGeoPoint> polygon = new
	 * LinkedList<>(); polygon.addLast(new MaGeoPoint(121.36, 31.18));
	 * polygon.addLast(new MaGeoPoint(121.35, 31.36)); polygon.addLast(new
	 * MaGeoPoint(121.39, 31.17)); polygon.addLast(new MaGeoPoint(121.36,
	 * 31.18));
	 * 
	 * @param collection
	 * @param locField
	 * @param polygon
	 * @param limit
	 * @return List<Document>
	 * @throws Exception
	 */
	public List<Document> findWithPolygon(String collection, String locField,
			List<MaGeoPoint> polygon, Integer limit) throws Exception;

	/**
	 * Specify a point, return to this point and the coordinates of the point is
	 * near from near to far, need $nearSphere 2dsphere index or 2d index
	 * Note:$nearSphere in sharding cluster is invalid, using geoNear
	 * 
	 * @param collection
	 * @param locField
	 * @param center
	 * @param minDistance
	 * @param maxDistance
	 * @param limit
	 * @return List<Document>
	 * @throws Exception
	 */
	public List<Document> findNearSphere(String collection, String locField,
			MaGeoPoint center, double minDistance, double maxDistance,
			Integer limit) throws Exception;

	/**
	 * Aggregation query, query a point near the point, and returns the distance
	 * each point to the center.
	 * 
	 * @param collection
	 * @param locField
	 * @param center
	 * @param maxDistance
	 * @param limit
	 * @return List<Document>
	 * @throws Exception
	 */
	public List<Document> findNear(String collection, String locField,
			MaGeoPoint center, double maxDistance, Integer limit)
			throws Exception;
	
	public List<Document> query(String collectionName,
			Document filter, Document keys, Document sort);

}
