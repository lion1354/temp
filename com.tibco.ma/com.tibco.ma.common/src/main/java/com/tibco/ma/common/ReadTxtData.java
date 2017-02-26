package com.tibco.ma.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.bson.Document;

/**
 * 
 * @author Daniel
 *
 */
public class ReadTxtData {
	// @Test
	public List readTXT(String path, String appId) throws FileNotFoundException {
		File txtfile = new File(path);
		Scanner scan = new Scanner(txtfile);
		List<String[]> list = new ArrayList<String[]>();
		List data = new ArrayList<String>();
		while (scan.hasNext()) {
			String row = scan.nextLine();
			String[] temp = row.split("\t");
			list.add(temp);
		}
		for (int i = 0; i < list.size(); i++) {
			String[] str = (String[]) list.get(i);
			List<String> value = Arrays.asList(str);
			if (i > 0) {
				Document document = new Document();
				Map<String, Object> map = new HashMap<>();
				List<String> key = Arrays.asList(list.get(0));
				for (int j = 0; j < key.size(); j++) {
					if (key.get(j).equals("Begin Date")
							|| key.get(j).equals("End Date")) {
						Date date;
						try {
							date = DateQuery.parsePSTDatetime(value.get(j),
									"MM/dd/yyyy");
							map.put(key.get(j), date);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					} else {
						map.put(key.get(j), value.get(j));
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
