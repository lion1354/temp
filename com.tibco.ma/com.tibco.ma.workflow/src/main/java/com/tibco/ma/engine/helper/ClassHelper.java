package com.tibco.ma.engine.helper;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Aidan
 *
 */
public class ClassHelper {

	private static final Logger log = LoggerFactory.getLogger(ClassHelper.class);
	
	/**
	 * 
	 * @param count
	 * @return
	 */
	public static long castLong(Object count) {
		if(count == null) return -1L;
		if(count instanceof Long) {
			return (Long)count;
		} else if(count instanceof BigDecimal) {
			return ((BigDecimal)count).longValue();
		} else if(count instanceof Integer) {
			return ((Integer)count).longValue();
		} else if(count instanceof BigInteger) {
			return ((BigInteger)count).longValue();
		} else if(count instanceof Byte) {
			return ((Byte)count).longValue();
        } else if(count instanceof Short) {
            return ((Short)count).longValue();
		} else {
			return -1L;
		}
	}
	
	/**
	 * 
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class<?> loadClass(String className) throws ClassNotFoundException {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException ex) {
                try {
                    return ClassLoader.class.getClassLoader().loadClass(className);
                } catch (ClassNotFoundException exc) {
                    throw exc;
                }
            }
        }
    }
	
	/**
	 * 
	 * @param clazzStr
	 * @return
	 */
	public static Object newInstance(String clazzStr) {
        try {
        	log.debug("loading class:" + clazzStr);
            Class<?> clazz = loadClass(clazzStr);
            return instantiate(clazz);
        } catch (ClassNotFoundException e) {
            log.error("Class not found.", e);
        } catch (Exception ex) {
        	log.error("[class=" + clazzStr + "]\n" + ex.getMessage());
        }
        return null;
    }
	
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> T instantiate(Class<T> clazz) {
		if (clazz.isInterface()) {
			log.error("The class is interface, not a class");
			return null;
		}
		try {
			return clazz.newInstance();
		} catch (Exception ex) {
			log.error("Please check the class is a abstract class?", ex.getCause());
		}
		return null;
	}
}
