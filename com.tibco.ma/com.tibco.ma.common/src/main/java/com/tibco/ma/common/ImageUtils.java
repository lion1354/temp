package com.tibco.ma.common;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
public class ImageUtils {

	public static String getRootPath(HttpServletRequest request) {
		return request.getServletContext().getRealPath("/");
	}

	public static String getParentPath(HttpServletRequest request) {
		String rootPath = getRootPath(request);
		rootPath = rootPath.substring(0, rootPath.length() - 1);
		rootPath = rootPath.substring(0,
				rootPath.lastIndexOf(File.separatorChar) + 1);
		return rootPath;
	}

	public static String getWebAppRootPath(HttpServletRequest request) {
		String rootPath = getParentPath(request);
		rootPath = rootPath.substring(0, rootPath.length() - 1);
		rootPath = rootPath.substring(0,
				rootPath.lastIndexOf(File.separatorChar) + 1);
		return rootPath;
	}

	public static boolean checkImage(String contentType) {
		List<String> expectTypes = Arrays.asList("image/jpg", "image/png",
				"image/jpeg", "image/gif");
		if (null != contentType) {
			if (!expectTypes.contains(contentType.toLowerCase())) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}
	
	public static boolean checkImageByName(String name) {
		List<String> expectTypes = Arrays.asList(".jpg", ".png",
				".jpeg", ".gif");
		if (null != name) {
			for (String string : expectTypes) {
				if(name.toLowerCase().contains(string)){
					return true;
				}
			}
		} else {
			return false;
		}
		return false;
	}

	public static String trashImage(String fileName) {
		String suffix = fileName.substring(fileName.lastIndexOf("."),
				fileName.length());
		String prefix = fileName.substring(0, fileName.lastIndexOf("."));
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String todayDateTime = dateFormat.format(date).toString();
		return prefix
				+ "_"
				+ todayDateTime.replace(" ", "").replace("-", "")
						.replace(":", "") + suffix;
	}

	public static String createFileName(String path, String contentType,
			String OriginalFilename) {
		String[] suffixs = contentType.split("/");
		return path + File.separator + DateQuery.getCurrentTimeInMillis() + "."
				+ suffixs[1];
	}

	public static String createNewFileName(String name, String contentType) {
		String[] suffixs = contentType.split("/");
		return name + DateQuery.getCurrentTimeInMillis() + "." + suffixs[1];
	}

	public static String createResName(String type, String originalFilename) {
		String[] suffixs = originalFilename.split("\\.");
		if (suffixs.length > 1) {
			return type + "_" + DateQuery.getCurrentTimeInMillis() + "."
					+ suffixs[suffixs.length - 1];
		} else {
			return originalFilename;
		}
	}

	public static String createFileName(String name) {
		return DateQuery.getCurrentTimeInMillis() + name;
	}

}
