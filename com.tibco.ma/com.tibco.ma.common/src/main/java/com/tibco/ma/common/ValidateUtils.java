package com.tibco.ma.common;

import java.util.Collection;

public class ValidateUtils {
	/**
	 * if collection is null or empty return false
	 * 
	 * @param collection
	 * @return boolean
	 */
	public static boolean isValidate(Collection collection) {
		if (collection == null || collection.isEmpty()) {
			return false;
		}
		return true;
	}
}
