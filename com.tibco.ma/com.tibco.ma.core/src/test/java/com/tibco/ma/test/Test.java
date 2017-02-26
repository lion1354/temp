package com.tibco.ma.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;




/*
 * 
 * 	db.collection.update(
 *		{ '<field>': { '$exists': true } },  // Query
 *		{ '$unset': { '<field>': true  } },  // Update
 *		{ 'multi': true }                    // Options
 *	)
 *	
 *
 *	db.collection.update(
 *   	{ '<field>': { '$exists': true } },  // Query
 *   	{ '$unset': { '<field>': true  } },  // Update
 *   	false, true                          // Upsert, Multi
 *	)
 *
 *
 *	db.collection.update( 
 *   	{ id: { $exists: true } },  // criteria
 *  	{ $unset: { id: 1 } },      // modifier
 *   	false,                      // no need to upsert
 *   	true                        // multi-update
 *	)
 *
 *	db.domain.update({},{$unset: {affLink:1}},{multi: true});
 *
 */
public class Test {
	public static void main(String[] args) throws Exception {
//		ObjectId owningId = new ObjectId("5530d30d02f47ed23813b417");
//		ObjectId relatedId = new ObjectId("5530d41002f47ed23813b41e");

//		MongoDBManagerTest.getCollection("Test123").insertOne(
//				new Document("owningId", owningId).append("relatedId",
//						relatedId));
//		System.out
//				.println(new Document("$or", asList(new Document("owningId",
//				owningId), new Document("relatedId", owningId))).toJson());
		
//		DeleteResult result = MongoDBManagerTest.getCollection("Test123")
//				.deleteMany(
//						new Document("$or", asList(new Document("owningId",
//								relatedId),
//								new Document("relatedId", relatedId))));
//		
//		System.out.println(result.getDeletedCount());

//		FindIterable<Document> iterable = MongoDBManagerTest.getCollection(
//				"Test123").find(
//				new Document("owningId", owningId));
//		System.out.println(JSON.serialize(iterable));
//		List<Document> list = iterable.sort(new Document("createAt", -1)).into(new ArrayList<Document>());
//		List<ObjectId> objIds = new ArrayList<ObjectId>();
//		for (Document doc : list) {
//			objIds.add(doc.getObjectId("relatedId"));
//		}
//		System.out.println(new Document("relatedId", new Document("$in", objIds)).toJson());
//		
//		FindIterable<Document> iterable2 = MongoDBManagerTest.getCollection(
//				"Test123").find(
//				new Document("relatedId", new Document("$in", objIds)));
//		System.out.println(JSON.serialize(iterable2));
//		System.out.println(Document.parse("{}").toJson());
//		MongoDBManagerTest.getCollection("Test123").insertOne(new Document("test",Document.parse("{}")));
		
//		String json = "{\"id\":\"559b3c591059fa136c1d7c13\",\"cols\":[{\"colType\":\"Relation\",\"colName\":\"qqq\",\"target\":\"5530fac502f470b2b6c6a8ee\",\"relEntityId\":\"5530fac502f470b2b6c6a8ee\",\"relEntityName\":\"Foods\"}],\"className\":\"qqq\",\"comment\":\"qqq\"}";
//		Document doc = Document.parse(json);
//		Object ob = doc.get("cols");
//		List<Document> cols = (ArrayList<Document>) ob;
//		JSONArray obj = JSONArray.fromObject(doc.get("cols"));
//		
//		for(Document col :cols){
//			System.out.println(col.getString("relEntityId"));
//		}
		
//		ObjectId owningId = new ObjectId("552744a17ea940fc8fe12e16");
//		Document filter = new Document("$or", asList(new Document(
//				"owningId", owningId), new Document("relatedId", owningId)));
//		System.out.println(filter.toJson());
		
		String json = "{\"name\": {\"colValue\": \"Runtastic\",\"colType\": \"String\"},\"description\": \"Unlimited Cloud Storage\",\"image\": {\"colValue\": {\"groupId\": \"5577ebe7572c551fa83a3216\",\"comment\": \"blob1433922535659.jpeg\"},\"colType\": \"image\"}}";
		Document doc = Document.parse(json);
		Document doc1 = (Document)doc.get("image");
		Document doc2 = (Document)doc.get("name");
//		System.out.println(doc1.toJson());
		Document document = new Document("id", "dsd");
		document.append("image",
				((Document) doc1.get("colValue")).append("type", "image"));
		document.append("name", doc2.get("colValue"));
//		System.out.println(doc1.getString("colType"));
//		System.out.println(document.toJson());
//		document.put("image", "image");
//		System.out.println(document.toJson());
		
//		FindIterable<Document> iterable = MongoDBManagerTest.getCollection(
//				"values").find(new Document("ssvasv","ttt"));
//		System.out.println(iterable.first());
//		Document update = new Document("adsad",
//				new Document("relEntityId",
//						"asdsid").append(
//						"relEntityName",
//						"asdasdname").append(
//						"type",
//						EntityColType.Relation.name()));
//		System.out.println(update.toJson());
		
//		FindIterable<Document> iterable = MongoDBManagerTest.getCollection(
//				"ttt").find(new Document());
//		System.out.println("-->  "+JSON.serialize(iterable));
//		iterable.projection(new Document("updateAt", 1).append("_id", 0));
//		System.out.println("==>  "+JSON.serialize(iterable));
//		FindIterable<Document> iterable = MongoDBManagerTest.getCollection(
//				"ttt").find();
//		iterable.limit(0);
//		List list = iterable.into(new ArrayList<Document>());
//		System.out.println(list.size());

		Document docTest = new Document("createAt", 1439446117098L).append("entityId", new ObjectId("55828c5e61a61f0fc029cad7"));
		FindIterable<Document> iterable = MongoDBManagerTest.getCollection("values").find(docTest);
		List list = iterable.into(new ArrayList<Document>());
		System.out.println(list.size());
		
	}
	
	private static void formatDocument(Document document) {
		Set<String> keySet = document.keySet();
		for (String key : keySet) {
			if (document.get(key) instanceof ObjectId) {
				document.put(key, document.getObjectId(key).toString());
			} else if (document.get(key) instanceof Document) {
				formatDocument((Document) document.get(key));
			}
		}
	}
}
