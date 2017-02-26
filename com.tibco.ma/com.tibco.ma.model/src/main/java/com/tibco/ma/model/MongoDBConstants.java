package com.tibco.ma.model;

public class MongoDBConstants {
	/* GLOBAL CONSTANTS */
	public static final String COLLECTION_NAME_SEPARATOR = "_";
	public static final String DOCUMENT_ID = "_id";
	public static final String ENTITY_COLLECTION_NAME = "_entity";
	public static final String VALUES_COLLECTION_NAME = "_values";
	public static final String RELATED_VALUES_COLLECTION_NAME = "_Join";
	public static final String GEO_INDEX_NAME = "_GeoIndex";
	
	public static final String SYSTEM_USER_COLLECTION_NAME = "_User";
	public static final String SYSTEM_DEVICE_COLLECTION_NAME = "_Device";
	
	public static final int PAGE_SIZE = 20;
	
	public static final int ORDERBY_ASC = 1;
	public static final int ORDERBY_DESC = -1;
	
	/*
	 * Entity
	 * 
	 * {
  	 *		"_id" : "${_id}",,
  	 *		"appId" : "${appId}",,
  	 *		"className" : "${className}",
  	 *		"cols" : [{
     * 				"colName" : "${colName}",
     * 				"colType" : "${colType}"
     *			}, {
     * 				"colName" : "${colName}",
     *				"colType" : "${colType}"
     *			}, ...],
  	 *		"comment" : "${comment}",
  	 *		"valuesCollection" : "${valuesCollection}"
	 * }
	 */
	/* entity Collection Constants */
	public static final String ENTITY_CLASSNAME = "className";
	public static final String ENTITY_COLS = "cols";
	public static final String ENTITY_COMMENT = "comment";
	public static final String ENTITY_APPID = "appId";
	public static final String ENTITY_VALUES_COLLECTION = "valuesCollection";

	/* column Collection Constants */
	public static final String COLUMN_COLNAME = "colName";
	public static final String COLUMN_COLTYPE = "colType";
	public static final String COLUMN_RELENTITYID = "relEntityId";
	public static final String COLUMN_RELENTITYNAME = "relEntityName";
	public static final String COLUMN_RELATION_COLLECTION = "relValuesCollection";

	/* values Collection Constants */
	public static final String VALUES_ENTITYID = "entityId";
	public static final String VALUES_CREATEAT = "createAt";
	public static final String VALUES_UPDATEAT = "updateAt";
	
	/* relation values Collection Constants */
	public static final String RELVALUES_OWNINGID = "owningId";
	public static final String RELVALUES_RELATEDID = "relatedId";
}
