package com.tibco.ma.common;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tibco.ma.common.DateQuery;

/**
 * @author Daniel
 */
public class Csv2Data {
	private static List<String[]> list = null;

	public List change2data(String path, String appId) {
		ReadCSV readCSV = new ReadCSV(path);
		int rows = readCSV.getColNum();
		list = new ArrayList<String[]>();
		for (int j = 0; j < rows; j++) {
			List<String[]> str = readCSV.getRowValues();
			list.addAll(str);
		}
		readCSV.close();
		// System.out.println(list.size());
		List data = new ArrayList();

		for (int j = 0; j < list.size(); j++) {
			String[] str = list.get(j);

			List<String> value = Arrays.asList(str);

			if (j > 4) {
				Document document = new Document();
				Map<String, Object> map = new HashMap<>();
				List<String> key = Arrays.asList(list.get(4));
				for (int k = 0; k < key.size(); k++) {
					if (key.get(k).equals("date")) {
						try {
							System.out.println("------" + value.get(k));
							Date date = DateQuery.parsePSTDatetime(
									value.get(k), "yyyyMMdd");
							map.put(key.get(k), date);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					} else {
						map.put(key.get(k), value.get(k));
					}
				}
				map.put("appId", appId);
				document.putAll(map);
				data.add(document);
			}
		}
		return data;
	}
}
