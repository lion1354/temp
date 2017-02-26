package com.tibco.ma.model.core;

import java.util.LinkedHashMap;
import java.util.Map;

import com.tibco.ma.common.DateQuery;

public class MaDate implements MaDataType {

	public static final String ISO = "iso";

	private Long iso;

	public MaDate(Object obj) {
		if (obj instanceof String) {
			iso = DateQuery.getDateTime((String) obj);
		} else if (obj instanceof Long) {
			iso = Long.valueOf((Long) obj);
		}
	}

	public Long getIso() {
		return iso;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put(ISO, iso);
		map.put(KEY_TYPE, EntityColType.Date.name());
		return map;
	}
}
