package com.tibco.ma.common.mongodb;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.tibco.ma.common.SpringBeanUtil;

/**
 * The database utility help for database connection<br>
 * Not provide query execution, please do the query execution yourself as
 * following steps:<br>
 * (1)Get the MongoDatabase.<br>
 * (2)execute the query/insert/update/delete.<br>
 */
public class DatabaseUtil {
	private static final Logger log = LoggerFactory
			.getLogger(DatabaseUtil.class);
	private static MongoDatabase db;

	public static synchronized MongoDatabase getDatabase() throws Exception {
		if (db == null) {
			log.debug("Get new mongodb MongoDatabase Object!");
			Properties prop = new Properties();
			prop.load(DatabaseUtil.class.getClassLoader().getResourceAsStream(
					"config.properties"));
			String dbName = prop.getProperty("mongo.dbname");
			MongoClient client = (MongoClient) SpringBeanUtil.getBean("mongo");
			db = client.getDatabase(dbName);
		}
		return db;
	}

}