package com.tibco.ma.test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDBManagerTest {
	private static final Logger log = LoggerFactory
			.getLogger(MongoDBManagerTest.class);

	private volatile static MongoDBManagerTest mongoDBManager;

	private static MongoDatabase db;
	private static MongoClient mongoClient;
	private MongoClientOptions mongoClientOptions;
	private MongoCredential mongoCredential;
	private ServerAddress serverAddress;
	private static Properties prop;

	private MongoDBManagerTest() {
//		if (getClass().getClassLoader()
//				.getResourceAsStream("config.properties") != null) {
//			try {
//				prop = new Properties();
//				prop.load(getClass().getClassLoader().getResourceAsStream(
//						"config.properties"));
//			} catch (IOException e) {
//				log.error("load config.properties error: ", e);
//			}
//		}
	}

	private static MongoDBManagerTest getInstance() {
		if (mongoDBManager == null) {
			synchronized (MongoDBManagerTest.class) {
				if (mongoDBManager == null) {
					mongoDBManager = new MongoDBManagerTest();
				}
			}
		}
		return mongoDBManager;
	}

	private MongoClient getMongoClient(String dbName) {
		if (mongoClient == null) {
			ServerAddress address = getServerAddress();
			List<MongoCredential> credentialsList = Arrays
					.asList(getMongoCredential(dbName));
			MongoClientOptions options = getMongoClientOptions();
			mongoClient = new MongoClient(address, credentialsList, options);
			// Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		}
		return mongoClient;
	}

	private ServerAddress getServerAddress() {
		if (serverAddress == null) {
			String host = "127.0.0.1";// String.valueOf(prop.get("host"));
			int port = 27017;// new Integer(prop.get("port").toString());
			serverAddress = new ServerAddress(host, port);
		}
		return serverAddress;
	}

	private MongoCredential getMongoCredential(String dbName) {
		if (mongoCredential == null) {
			String username = "ma";// String.valueOf(prop.get("username"));
			String password = "ma";// String.valueOf(prop.get("password"));
			mongoCredential = MongoCredential.createCredential(username,
					dbName, password.toCharArray());
		}
		return mongoCredential;
	}

	private MongoClientOptions getMongoClientOptions() {
		if (mongoClientOptions == null) {
			int poolSize = 10;// new Integer(prop.get("poolSize").toString());
			int blockSize = 10;// new Integer(prop.get("blockSize").toString());
			MongoClientOptions.Builder builder = MongoClientOptions.builder();
			builder.connectionsPerHost(poolSize);
			builder.threadsAllowedToBlockForConnectionMultiplier(blockSize);
			mongoClientOptions = builder.build();
		}
		return mongoClientOptions;
	}

	public static MongoDatabase getDatabase() {
		if (db == null) {
			String dbName = "ma";// String.valueOf(prop.get("dbname"));
			MongoDBManagerTest mongoDBManager = getInstance();
			mongoClient = mongoDBManager.getMongoClient(dbName);
			db = mongoClient.getDatabase(dbName);
		}
		return db;
	}

	public static synchronized MongoCollection<Document> getCollection(
			String collectionName) {
		return getDatabase().getCollection(collectionName);
	}

	public static synchronized <TDocument> MongoCollection<TDocument> getCollection(
			final String collectionName, final Class<TDocument> documentClass) {
		return getDatabase().getCollection(collectionName, documentClass);
	}

	public static synchronized void close() {
		if (mongoClient != null) {
			if (db != null) {
				db = null;
			}
			mongoClient.close();
			mongoClient = null;
		}
	}
}