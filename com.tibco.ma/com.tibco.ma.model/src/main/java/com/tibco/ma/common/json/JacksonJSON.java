package com.tibco.ma.common.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.tibco.ma.common.StringUtil;

public class JacksonJSON implements JSON {
	private JsonNode json;

	public JacksonJSON(String json) throws IOException {
		ObjectMapper om = new ObjectMapper();
		this.json = om.readTree(StringUtil.isEmpty(json) ? "{}" : json);
	}
	
	public JacksonJSON(JsonNode jsonnode) {
		this.json = jsonnode;
	}

	public boolean has(String key) {
		checkKey(key);
		return json.has(key);
	}

	public String getString(String key) {
		checkKey(key);
		JsonNode value = json.get(key);
		return value.asText();
	}

	public String optString(String key) {
		checkKey(key);
		if (json.has(key)) {
			return json.get(key).asText();
		} else {
			return null;
		}
	}

	public String optString(String key, String defaultvalue) {
		checkKey(key);
		if (json.has(key)) {
			return json.get(key).asText();
		} else {
			return defaultvalue;
		}
	}

	public long getLong(String key) {
		checkKey(key);
		JsonNode value = json.get(key);
		return value.asLong();
	}

	public long optLong(String key) {
		checkKey(key);
		if (json.has(key)) {
			return json.get(key).asLong();
		} else {
			return 0;
		}
	}

	public int getInt(String key) {
		checkKey(key);
		JsonNode value = json.get(key);
		return value.asInt();
	}

	public int optInt(String key) {
		checkKey(key);
		if (json.has(key)) {
			return json.get(key).asInt();
		} else {
			return 0;
		}
	}

	public String[] getStringArray(String key) {
		checkKey(key);
		String ret[] = null;
		JsonNode value = json.get(key);

		if (value.isArray()) {
			ArrayNode an = (ArrayNode) value;
			ret = new String[an.size()];
			for (int i = 0; i < an.size(); i++) {
				if (an.get(i) != null) {
					ret[i] = an.get(i).asText();
				} else {
					ret[i] = null;
				}
			}
		} else {
			ret = new String[] { value.asText() };
		}
		return ret;
	}

	public String[] optStringArray(String key) {
		checkKey(key);
		if (json.has(key)) {
			return getStringArray(key);
		} else {
			return new String[] {};
		}

	}

	public List<JSON> optList(String key) {
		checkKey(key);
		if (json.has(key)) {
			List<JSON> ret = new ArrayList<JSON>();
			JsonNode node = json.get(key);
			if (node.isArray()) {
				for (Iterator<JsonNode> it = node.iterator(); it.hasNext();) {
					JsonNode n = it.next();
					JacksonJSON json = new JacksonJSON(n);
					ret.add(json);
				}
			}
			return ret;
		} else {
			return new ArrayList<JSON>();
		}
	}

	private void checkKey(String key) {
		if (StringUtil.isEmpty(key)) {
			throw new NullPointerException("key is empty!");
		}
	}

	@Override
	public List<JSON> asJSONArray() {
		List<JSON> ret = new ArrayList<JSON>();
		if (json.isArray()) {
			for (Iterator<JsonNode> it = json.iterator(); it.hasNext();) {
				JsonNode n = it.next();
				JacksonJSON json = new JacksonJSON(n);
				ret.add(json);
			}
		} else {
			ret.add(new JacksonJSON(json));
		}
		return ret;
	}

	@Override
	public JSON asJSONObject() {
		JSON ret = null;
		if (json.isArray()) {
			for (Iterator<JsonNode> it = json.iterator(); it.hasNext();) {
				JsonNode n = it.next();
				JacksonJSON json = new JacksonJSON(n);
				ret = json;
				break;
			}
		} else {
			ret = new JacksonJSON(json);
		}
		return ret;
	}

	@Override
	public boolean isArray() {
		return json.isArray();
	}

	@Override
	public boolean isEmpty() {
		boolean ret = false;
		if (json == null) {
			ret = true;
		} else {
			ret = !json.elements().hasNext();
		}

		return ret;
	}

	@Override
	public JSON getJSON(String key) {
		try {
			if (!json.has(key)) {
				String s = null;
				return new JacksonJSON(s);
			} else {
				return new JacksonJSON(json.get(key));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		try {
			String s = null;
			JacksonJSON j = new JacksonJSON(s);
			System.out.println(j.isEmpty());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean getBoolean(String key) {
		checkKey(key);
		JsonNode value = json.get(key);
		return value.asBoolean();
	}

	@Override
	public boolean optBoolean(String key) {
		checkKey(key);
		if (json.has(key)) {
			return json.get(key).asBoolean();
		} else {
			return false;
		}
	}@Override
	public String toString() {
		return json.toString();
	}

	@Override
	public List<String> getKeys() {
		List<String> keys = new ArrayList<String>();
		Iterator<String> it = json.fieldNames();
		for (Iterator<String> iterator = it; iterator.hasNext();) {
			String type = (String) iterator.next();
			keys.add(type);
		}
		return keys;
	}

	@Override
	public Object getObject(String key) {
		checkKey(key);
		return json.get(key);
	}

}
