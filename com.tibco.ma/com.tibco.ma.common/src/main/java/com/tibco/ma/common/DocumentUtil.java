package com.tibco.ma.common;

import java.util.Set;

import org.bson.Document;
import org.bson.types.ObjectId;

public class DocumentUtil {

	/**
	 * convert ObjectId to String
	 * 
	 * @param document
	 */
	public static void convertObjectIdToString(Document document) {
		if (document != null) {
			Set<String> keySet = document.keySet();
			for (String key : keySet) {
				if (document.get(key) instanceof ObjectId) {
					document.put(key, document.getObjectId(key).toString());
				} else if (document.get(key) instanceof Document) {
					convertObjectIdToString((Document) document.get(key));
				}
			}
		}
	}
}
