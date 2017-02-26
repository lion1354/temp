package com.tibco.ma.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bson.Document;

public class TestJson {

	public static void test1() {
		Document filter = new Document();
		String json = "{\"$or\":[{\"wins\":{\"$gte\":1000,\"$lte\":3000}},{\"hight\":{\"$lt\":5}},{\"hometown\":{\"$select\":{\"query\":{\"className\":\"Team\",\"where\":{\"playAge\":{\"$select\":{\"query\":{\"className\":\"ageInfo\",\"where\":{\"age\":{\"$gt\":6}}},\"key\":\"age\"}}}},\"key\":\"city\"}}}]}";
		Document doc = Document.parse(json);
		System.out.println(doc.toJson());
		Set<String> keySet1 = doc.keySet();
		for (String key : keySet1) {
			Object ob = doc.get(key);
			if (ob instanceof Document) {

			} else if (ob instanceof List) {
				List<Document> list = (ArrayList<Document>) ob;
				for (Document doc1 : list) {
					System.out.println(doc1.toJson());
				}
				filter.append(key, list);
				System.out.println(filter.toJson());
			}
		}
	}

	public static Document test2(String json) {
		Document filter = new Document();
		Document doc = Document.parse(json);
		// System.out.println("==> " + doc.toJson());
		Set<String> keySet1 = doc.keySet();
		for (String key : keySet1) {
			Object ob = doc.get(key);
			if (ob instanceof Document) {
				filter.append(key, test2(((Document) ob).toJson()));
			} else if (ob instanceof List) {
				List<Document> list = (List) ob;
				List<Document> templist = new ArrayList<Document>();
				for (Document temp : list) {
					templist.add(test2(temp.toJson()));
				}
				filter.append(key, templist);
			} else if (ob instanceof Number) {
				filter.append(key, ob);
			} else if (ob instanceof String) {
				filter.append(key, ob);
			} else {
				filter.append(key, ob);
			}

		}
		return filter;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Document test3(String json) {
		Document filter = new Document();
		Document doc = Document.parse(json);
		// System.out.println("==> " + doc.toJson());
		Set<String> keySet1 = doc.keySet();
		for (String key : keySet1) {
			System.out.println(key);
			if (key.equals("$select")) {
				Object ob = doc.get(key);
				System.out.println(((Document) ob).toJson());
			} else {
				Object ob = doc.get(key);
				if (ob instanceof Document) {
					filter.append(key, test3(((Document) ob).toJson()));
				} else if (ob instanceof List) {
					List<Document> list = (List) ob;
					List<Document> templist = new ArrayList<Document>();
					for (Document temp : list) {
						templist.add(test3(temp.toJson()));
					}
					filter.append(key, templist);
				} else if (ob instanceof Number) {
					filter.append(key, ob);
				} else if (ob instanceof String) {
					filter.append(key, ob);
				} else {
					filter.append(key, ob);
				}
			}
		}
		return filter;
	}

	public static void main(String[] args) {
		Document filter = new Document();
		// String json =
		// "{\"hometown\":{\"$select\":{\"query\":{\"className\":\"Team\",\"where\":{\"winPct\":{\"$gt\":0.5}}},\"key\":\"city\"}}}";
//		 String json =
//		 "{\"$or\":[{\"wins\":{\"$gte\":1000,\"$lte\":3000}},{\"hight\":{\"$lt\":5}},{\"hometown\":{\"$select\":{\"query\":{\"className\":\"Team\",\"where\":{\"playAge\":{\"$select\":{\"query\":{\"className\":\"ageInfo\",\"where\":{\"age\":{\"$gt\":6}}},\"key\":\"age\"}}}},\"key\":\"city\"}}}]}";

		// String json =
		// "{\"$or\":[{\"wins\":{\"$gt\":150}},{\"wins\":{\"$lt\":5}},{\"hometown\":{\"$select\":{\"query\":{\"className\":\"Team\",\"where\":{\"playAge\":{\"$select\":{\"query\":{\"className\":\"ageInfo\",\"where\":{\"age\":{\"$gt\":6}}},\"key\":\"age\"}}}},\"key\":\"city\"}}}]}";
		// System.out.println("--> " + Document.parse(json).toJson());
		// filter = test2(json);
		// System.out.println("))> " + filter.toJson());

		String json = "{\"hometown\":{\"$select\":{\"query\":{\"className\":\"Team\",\"where\":{\"playerAge\":{\"$select\":{\"query\":{\"className\":\"ageInfo\",\"where\":{\"age\":{\"$gt\":30}}},\"key\":\"age\"}}}},\"key\":\"city\"}}}";
//		filter = test3(json);
//		System.out.println(filter.toJson());
		Document doc = Document.parse(json);
		Document result = new Document();
		// System.out.println("--> " + doc.toJson());
		Set<String> keySet = doc.keySet();
		for (String key : keySet) {
			Object ob = doc.get(key);
			if (ob instanceof Document) {
				Document doc1 = (Document) ob;
				Set<String> keySet1 = doc1.keySet();
				for (String key1 : keySet1) {
					Object ob1 = doc1.get(key1);
					if (key1.equals("$select")) {
						Document doc2 = (Document) ob1;
						System.out.println(doc2.toJson());
						Document queryDoc2 = ((Document) doc2.get("query"));
						Document wherequeryDoc2 = ((Document) queryDoc2
								.get("where"));
						Set<String> wherequeryDocKeySet1 = wherequeryDoc2
								.keySet();
						for (String wherequeryDocKey1 : wherequeryDocKeySet1) {
						}
					}
				}
			}
		}

	}
}
