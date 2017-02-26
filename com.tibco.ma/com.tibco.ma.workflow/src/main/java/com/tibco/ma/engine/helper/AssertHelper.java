package com.tibco.ma.engine.helper;

/**
 * 
 * @author Aidan
 *
 */
public abstract class AssertHelper {

	/**
	 * 
	 * @param expression
	 * @param message 
	 */
	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 * 
	 * @param expression
	 */
	public static void isTrue(boolean expression) {
		isTrue(expression, "[Assertion failed] - this expression must be true");
	}
	
	/**
	 * 
	 * @param object
	 * @param message
	 */
	public static void isNull(Object object, String message) {
		if (object != null) {
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 * 
	 * @param object
	 */
	public static void isNull(Object object) {
		isNull(object, "[Assertion failed] - the object argument must be null");
	}
	
	/**
	 * 
	 * @param object
	 * @param message
	 */
	public static void notNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 * 
	 * @param object
	 */
	public static void notNull(Object object) {
		notNull(object, "[Assertion failed] - this argument is required; it must not be null");
	}
	
	/**
	 * 
	 * @param str
	 * @param message
	 */
	public static void notEmpty(String str, String message) {
		if (str == null || str.length() == 0) {
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 * 
	 * @param str
	 */
	public static void notEmpty(String str) {
		notEmpty(str, "[Assertion failed] - this argument is required; it must not be null or empty");
	}
}
