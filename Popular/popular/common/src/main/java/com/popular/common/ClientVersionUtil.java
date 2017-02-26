package com.popular.common;

import java.util.List;

import org.springframework.http.HttpHeaders;

import com.popular.common.util.StringUtils;


public class ClientVersionUtil {

	public static final String CLIENT_AGENT_CLIENT_TYPE_IOS = "IOS";
	public static final String CLIENT_AGENT_CLIENT_TYPE_ANDROID = "AND";

	public static final String CLIENT_AGENT_DEVICE_TYPE_IPHONE4 = "iphone4";
	public static final String CLIENT_AGENT_DEVICE_TYPE_IPHONE5 = "iphone5";
	public static final String CLIENT_AGENT_DEVICE_TYPE_HDPI = "hdpi";
	public static final String CLIENT_AGENT_DEVICE_TYPE_MDPI = "mdpi";
	public static final String CLIENT_AGENT_DEVICE_TYPE_XHDPI = "xhdpi";
	public static final String CLIENT_AGENT_DEVICE_TYPE_XXHDPI = "xxhdpi";
	public static final String CLIENT_AGENT_DEVICE_TYPE_ALL = "all";

	public final static String CLIENT_AGENT_KEY = "client-agent";
	public final static String USER_AGENT_KEY = "user-agent";
	public final static char VERSION_SPLITER = '.';

	public static String retrieveClientAgent(HttpHeaders headers) {
		List<String> values = headers.get(CLIENT_AGENT_KEY);
		if (values == null || values.size() == 0) {
			return "";
		} else {
			return values.get(0);
		}

	}

	/**
	 * Retrieve the client type("t") from client agent string "m.n.r.b.t.d".
	 * 
	 * @param clientAgent
	 * @return generally return IOS or AND
	 */
	public static String getClientType(String clientAgent) {
		String ret = "";
		if (StringUtils.notEmpty(clientAgent)) {
			String values[] = clientAgent.split("\\.");
			if (values.length > 4) {
				ret = values[4];
			}
		}

		return ret;
	}

	/**
	 * Retrieve the build number("m") from client agent string "m.n.r.b.t.d".
	 * 
	 * @param clientAgent
	 * @return the build number.
	 */
	public static int getMajorNumber(String clientAgent) {
		int ret = -1;
		if (StringUtils.notEmpty(clientAgent)) {
			String values[] = clientAgent.split("\\.");
			if (values.length > 0) {
				ret = Integer.valueOf(values[0]);
			}

		}

		return ret;
	}

	/**
	 * Retrieve the build number("n") from client agent string "m.n.r.b.t.d".
	 * 
	 * @param clientAgent
	 * @return the build number.
	 */
	public static int getMinorNumber(String clientAgent) {
		int ret = -1;
		if (StringUtils.notEmpty(clientAgent)) {
			String values[] = clientAgent.split("\\.");
			if (values.length > 1) {
				ret = Integer.valueOf(values[1]);
			}

		}

		return ret;
	}

	/**
	 * Retrieve the build number("r") from client agent string "m.n.r.b.t.d".
	 * 
	 * @param clientAgent
	 * @return the build number.
	 */
	public static int getReleaseNumber(String clientAgent) {
		int ret = -1;
		if (StringUtils.notEmpty(clientAgent)) {
			String values[] = clientAgent.split("\\.");
			if (values.length > 2) {
				ret = Integer.valueOf(values[2]);
			}

		}

		return ret;
	}

	/**
	 * Retrieve the build number("b") from client agent string "m.n.r.b.t.d".
	 * 
	 * @param clientAgent
	 * @return the build number.
	 */
	public static int getBuildNumber(String clientAgent) {
		int ret = -1;
		if (StringUtils.notEmpty(clientAgent)) {
			String values[] = clientAgent.split("\\.");
			if (values.length > 3) {
				ret = Integer.valueOf(values[3]);
			}

		}

		return ret;
	}

	/**
	 * Retrieve the device type("d") from client agent string "m.n.r.b.t.d".
	 * 
	 * @param clientAgent
	 * @return generally return ldpi/mdpi/hdpi/ios4/ios5.
	 */
	public static String getDeviceType(String clientAgent) {
		String ret = "";
		if (StringUtils.notEmpty(clientAgent)) {
			String values[] = clientAgent.split("\\.");
			if (values.length == 6) {
				ret = values[5];
			}

		}

		return ret;
	}

	/**
	 * If this client type is ios.
	 * 
	 * @param clientAgent
	 * @return true: ios, false: otherwise
	 */
	public static boolean isIOS(String clientAgent) {
		return StringUtils.notEmptyAndEqual(getClientType(clientAgent),
				CLIENT_AGENT_CLIENT_TYPE_IOS);
	}

	/**
	 * If this client type is android.
	 * 
	 * @param clientAgent
	 * @return true: android, false: otherwise
	 */
	public static boolean isAndroid(String clientAgent) {
		return StringUtils.notEmptyAndEqual(getClientType(clientAgent),
				CLIENT_AGENT_CLIENT_TYPE_ANDROID);
	}

	/**
	 * If the device type is xhdpi.
	 * 
	 * @param clientAgent
	 * @return true: xhdpi, false: otherwise
	 */
	public static boolean isXHdpi(String clientAgent) {
		return StringUtils.notEmptyAndEqual(getDeviceType(clientAgent),
				CLIENT_AGENT_DEVICE_TYPE_XHDPI);
	}

	/**
	 * If the device type is mdpi.
	 * 
	 * @param clientAgent
	 * @return true: mdpi, false: otherwise
	 */
	public static boolean isMdpi(String clientAgent) {
		return StringUtils.notEmptyAndEqual(getDeviceType(clientAgent),
				CLIENT_AGENT_DEVICE_TYPE_MDPI);
	}

	/**
	 * If the device type is hdpi.
	 * 
	 * @param clientAgent
	 * @return true: hdpi, false: otherwise
	 */
	public static boolean isHdpi(String clientAgent) {
		return StringUtils.notEmptyAndEqual(getDeviceType(clientAgent),
				CLIENT_AGENT_DEVICE_TYPE_HDPI);
	}

	/**
	 * If the device type is iphone4.
	 * 
	 * @param clientAgent
	 * @return true: iphone4, false: otherwise
	 */
	public static boolean isIphone4(String clientAgent) {
		return StringUtils.notEmptyAndEqual(getDeviceType(clientAgent),
				CLIENT_AGENT_DEVICE_TYPE_IPHONE4);
	}

	/**
	 * If the device type is iphone5.
	 * 
	 * @param clientAgent
	 * @return true: iphone5, false: otherwise
	 */
	public static boolean isIphone5(String clientAgent) {
		return StringUtils.notEmptyAndEqual(getDeviceType(clientAgent),
				CLIENT_AGENT_DEVICE_TYPE_IPHONE5);
	}
	
	/**
	 * If the device type is all.
	 * 
	 * @param clientAgent
	 * @return true: all, false: otherwise
	 */
	public static boolean isAll(String clientAgent) {
		return StringUtils.notEmptyAndEqual(getDeviceType(clientAgent),
				CLIENT_AGENT_DEVICE_TYPE_ALL);
	}

}
