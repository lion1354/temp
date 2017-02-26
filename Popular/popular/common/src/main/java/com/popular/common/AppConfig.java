package com.popular.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Aidan
 *
 *         2016/7/25
 */
@Component("appConfig")
public class AppConfig {
	@Value("${skipAPISecurity}")
	private String skipAPISecurity;
	@Value("${clientSecret}")
	private String clientSecret;
	@Value("${appkey_ios}")
	private String appkey_ios;
	@Value("${appkey_android}")
	private String appkey_android;
	@Value("${zone}")
	private String zone;

	@Value("${total_photo}")
	private String total_photo;
	@Value("${thumbnail_width}")
	private String thumbnail_width;
	@Value("${thumbnail_height}")
	private String thumbnail_height;
	@Value("${message_keep_day}")
	private String message_keep_day;
	@Value("${file_server}")
	private String fileServer;
	@Value("${file_directory}")
	private String fileDirectory;

	public String getFileDirectory() {
		return fileDirectory;
	}
	

	public void setFileDirectory(String fileDirectory) {
		this.fileDirectory = fileDirectory;
	}
	

	public String getFileServer() {
		return fileServer;
	}

	public void setFileServer(String fileServer) {
		this.fileServer = fileServer;
	}

	public String getSkipAPISecurity() {
		return skipAPISecurity;
	}

	public void setSkipAPISecurity(String skipAPISecurity) {
		this.skipAPISecurity = skipAPISecurity;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getAppkey_ios() {
		return appkey_ios;
	}

	public void setAppkey_ios(String appkey_ios) {
		this.appkey_ios = appkey_ios;
	}

	public String getAppkey_android() {
		return appkey_android;
	}

	public void setAppkey_android(String appkey_android) {
		this.appkey_android = appkey_android;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getTotal_photo() {
		return total_photo;
	}

	public void setTotal_photo(String total_photo) {
		this.total_photo = total_photo;
	}

	public String getThumbnail_width() {
		return thumbnail_width;
	}

	public void setThumbnail_width(String thumbnail_width) {
		this.thumbnail_width = thumbnail_width;
	}

	public String getThumbnail_height() {
		return thumbnail_height;
	}

	public void setThumbnail_height(String thumbnail_height) {
		this.thumbnail_height = thumbnail_height;
	}
	public String getMessage_keep_day() {
		return message_keep_day;
	}

	public void setMessage_keep_day(String message_keep_day) {
		this.message_keep_day = message_keep_day;
	}

}
