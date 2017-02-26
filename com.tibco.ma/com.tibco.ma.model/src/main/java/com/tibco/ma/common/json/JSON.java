package com.tibco.ma.common.json;

import java.util.List;

public interface JSON {
	public boolean has(String key);

	public String getString(String key);

	public String optString(String key);

	public String optString(String key, String defaultValue);

	public long getLong(String key);

	public long optLong(String key);

	public int getInt(String key);

	public int optInt(String key);
	
	public boolean getBoolean(String key);

	public boolean optBoolean(String key);

	public String[] getStringArray(String key);

	public String[] optStringArray(String key);

	public List<JSON> optList(String key);

	public List<JSON> asJSONArray();

	public JSON asJSONObject();

	public JSON getJSON(String key);

	public boolean isArray();

	/**
	 * if the JSON object is null, or no keys, return true.
	 * 
	 * @return
	 */
	public boolean isEmpty();
	
	public List<String> getKeys();
	
	public Object getObject(String key);
	
}
