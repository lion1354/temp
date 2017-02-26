package com.tibco.ma.common.json;

import java.util.HashMap;
import java.util.Map;

public abstract class JSONParser extends HashMap<String, Object> implements
		Map<String, Object> {

	/**
	 * Fetch xml from the address, and parse to JSON.
	 * 
	 * @param address
	 * @return
	 * @throws Exception
	 */
	public abstract JSON fetchXMLToJSON(String address) throws Exception;

	/**
	 * Fetch xml from the address using specific encoding, and parse to JSON.
	 * 
	 * @param address
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public abstract JSON fetchXMLToJSON(String address, String encoding)
			throws Exception;

	/**
	 * fetch JSON.
	 * 
	 * @param address
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public abstract JSON fetchJSON(String address, String encoding)
			throws Exception;

	/**
	 * fetch JSON.
	 * 
	 * @param address
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public abstract JSON fetchJSON(String address) throws Exception;

	public abstract JSON fetchJSONofGzip(String address) throws Exception;

	public abstract JSON fetchJSONofGzip(String address, String encoding)
			throws Exception;

	/**
	 * Parse json string to JSON.
	 * 
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public abstract JSON buildJSON(String json) throws Exception;

	public abstract JSON xmlToJson(String xml) throws Exception;

	public static JSONParser newInstance() {
		return new JacksonJSONParser();
	}
}
