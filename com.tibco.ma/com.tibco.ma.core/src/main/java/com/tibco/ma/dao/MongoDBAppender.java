package com.tibco.ma.dao;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

import com.mongodb.client.MongoDatabase;
import com.tibco.ma.common.mongodb.DatabaseUtil;

@Service
public class MongoDBAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

	private static final Logger log = LoggerFactory
			.getLogger(MongoDBAppender.class);

	@Override
	protected void append(ILoggingEvent eventObject) {
		String logger = (String) eventObject.getArgumentArray()[0];
		if (null != logger) {
			Document document = Document.parse(logger);
			try {
				MongoDatabase db = DatabaseUtil.getDatabase();
				db.getCollection("system_logger").insertOne(document);
			} catch (Exception e) {
				log.error("insert logger error: {}", e);
			}
		}
	}
}
