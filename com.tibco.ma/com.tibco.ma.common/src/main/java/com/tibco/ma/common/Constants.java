package com.tibco.ma.common;

public class Constants {
	/**
	 * 15 seconds
	 */
	public static final int JSON_CONNECTION_READ_TIMEOUT = 15000;

	public static final String IMAGE_SHOW_GRIDFS_PREFIX = "/ma/1/showImage?fileUrl=";

	public static final String IMAGE_SPECIFICATION_IMAGE_URL_REGEX = "^.+_oldUrl$";

	public static final String IMAGE_SPECIFICATION_IMAGE_URL_SPLIT = "_oldUrl";

	public static final String REST_NOTIFICATION_FINDRES = "/restapi/1/custNotification/findRes?id=";

	public static final String IMAGE_REST_CORE_SHOWRESOURCE = "/ma/1/shoRestResource?model=core&fileUrl=";

	public static final String IMAGE_NOTIFICATION_SHOWRES = "/ma/1/shoRestResource?model=notification&fileUrl=";

	public static final String IMAGE_CONNECTOR_SHOWRES = "/ma/1/shoRestResource?model=workflow&fileUrl=";

	public static final String EMAIL_SERVER_TEXT = "Thank you for your registration!<br>"
			+ "Please click below to activate your account now!<br>";

	public static final String CONTEXTPATH = "mobilePlatform";

	public static final String FBURLME = "https://graph.facebook.com/me";

	public static final boolean SKIP_API_SECURITY = false;

	public static final String STATICHTML = "statichtml";

	/**
	 * app config setting key
	 */
	public static final String APP_CONFIG_TIMEZONE = "TimeZone";

	// public static final String APP_CONFIG_LIMIT_SIZE_KEY = "LimitSize";
	//
	// public static final String APP_CONFIG_LIMIT_SIZE = "100";
}
