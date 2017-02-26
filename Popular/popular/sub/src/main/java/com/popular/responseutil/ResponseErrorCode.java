package com.popular.responseutil;

public enum ResponseErrorCode {
	INVALID_PASSWORD(1000),
	INTERNAL_ERROR(1001),
	INVALID_TOKEN(1002),
	EXISTING_FACEBOOK_EMAIL(1003),
	EXISTING_TWITTER_EMAIL(1004),
	EXISTING_EMAIL(1005),
	INVALID_EMAIL(1006),
	REGISTER_ERROR(1007),
	LOGIN_ERROR(1008),
	NOT_FOUND_ACCESS_TOKEN(1009),
	NOT_FOUND_APP_KEY(1010),
	FACE_BOOK_LOGIN_ERROR(1011),
	RESET_PWD_ERROR(1012),
	UPDATE_CLASS_ERROR(1013),
	ADD_CLASS_ERROR(1014),
	DELETE_CLASS_ERROR(1015),
	GET_CLASS_ERROR(1016),
	EVAL_CLOUDCODE_ERROR(1017),
	FIND_PNTASK_ERROR(1018),
	UPLOAD_FILE_ERROR(1019), 
	APP_CONFIG_VALIDATE_ERROR(1020),
	APP_CONFIG_ADD_ERROR(1021),
	APP_CONFIG_UPDATE_ERROR(1022),
	APP_CONFIG_DELETE_ERROR(1023),
	APP_CONFIG_GET_ERROR(1024),
	APP_CONFIG_UNIQUECHECK_ERROR(1025),
	ADD_USER_PAGE_ANALYTICS_ERROR(1026),
	ADD_USER_ACTION_ANALYTICS_ERROR(1027),
	SENDING_MAIL_ERROR(1028);
	
	private int value = 0;
	
	private ResponseErrorCode(int value) {
        this.value = value;
    }
	
	public static ResponseErrorCode valueOf(int value) {
        switch (value) {
        case 1000:
            return INVALID_PASSWORD;
        case 1001:
            return INTERNAL_ERROR;
        case 1002:
            return INVALID_TOKEN;
        case 1003:
            return EXISTING_FACEBOOK_EMAIL;
        case 1004:
            return EXISTING_TWITTER_EMAIL;
        case 1005:
            return EXISTING_EMAIL;
        case 1006:
            return INVALID_EMAIL;
        case 1007:
            return REGISTER_ERROR;
        case 1008:
            return LOGIN_ERROR;
        case 1009:
            return NOT_FOUND_ACCESS_TOKEN;
        case 1010:
            return NOT_FOUND_APP_KEY;
        case 1011:
            return FACE_BOOK_LOGIN_ERROR;
        case 1012:
            return RESET_PWD_ERROR;
        case 1013:
            return UPDATE_CLASS_ERROR;
        case 1014:
            return ADD_CLASS_ERROR;
        case 1015:
            return DELETE_CLASS_ERROR;
        case 1016:
            return GET_CLASS_ERROR;
        case 1017:
            return EVAL_CLOUDCODE_ERROR;
        case 1018:
            return FIND_PNTASK_ERROR;
		case 1019:
			return UPLOAD_FILE_ERROR;
		case 1020:
			return APP_CONFIG_VALIDATE_ERROR;
		case 1021:
			return 	APP_CONFIG_ADD_ERROR;
		case 1022:
			return APP_CONFIG_UPDATE_ERROR;
		case 1023:
			return APP_CONFIG_DELETE_ERROR;
		case 1024:
			return APP_CONFIG_GET_ERROR;
		case 1025:
			return APP_CONFIG_UNIQUECHECK_ERROR;
		case 1026:
			return ADD_USER_PAGE_ANALYTICS_ERROR;
		case 1027:
			return ADD_USER_ACTION_ANALYTICS_ERROR;
		case 1028:
			return SENDING_MAIL_ERROR;
        default:
            return null;
        }
    }

    public int value() {
        return this.value;
    }
}
