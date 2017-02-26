package com.tibco.ma.common.pns;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javapns.Push;
import javapns.devices.Device;
import javapns.devices.exceptions.InvalidDeviceTokenFormatException;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.NewsstandNotificationPayload;
import javapns.notification.Payload;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.tibco.ma.common.StringUtil;

public class PNHelper {

	private static final int NUMBER_THREADS = 3;
	private static final Logger log = LoggerFactory.getLogger(PNHelper.class);

	@SuppressWarnings("rawtypes")
	public static Boolean isProduction(InputStream certificate, String password)
			throws Exception {
		try {
			KeyStore p12 = KeyStore.getInstance("pkcs12");
			if (password == null) {
				password = "";
			}
			p12.load(certificate, password.toCharArray());
			Enumeration e = p12.aliases();
			while (e.hasMoreElements()) {
				String alias = (String) e.nextElement();
				X509Certificate c = (X509Certificate) p12.getCertificate(alias);
				String subject = c.getSubjectDN().toString().toLowerCase();
				if (!subject.contains("cn")
						&& !subject.contains("ios push services")) {
					throw new Exception(
							"Invalid ios push services certificate, please check!");
				}
				String subjectArray[] = subject.split(",");
				for (String s : subjectArray) {
					String[] str = s.trim().split("=");
					if (str[0].equals("cn")) {
						if (str[1].contains("development")) {
							return false;
						} else {
							return true;
						}
					} else {
						continue;
					}
				}
			}
			return false;
		} catch (Exception e) {
			log.error("{}", e);
			throw new Exception(
					"Verify certificates happened error, the error message: "
							+ e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> sendGCM(String msg, String url,
			String imageUrl, Map<String, Object> dataInfo, String msg_type,
			String serverKey, List<String> regList) throws Exception {
		try {
			log.info("GCM send start...");
			Sender sender = new Sender(serverKey);
			JSONObject jsonObject = new JSONObject();
			if (dataInfo != null) {
				jsonObject.putAll(dataInfo);
			}
			Message message = new Message.Builder().addData("message", msg)
					.addData("url", url).addData("imageUrl", imageUrl)
					.addData("type", msg_type)
					.addData("datainfo", jsonObject.toJSONString()).build();
			MulticastResult result = sender.sendNoRetry(message, regList);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("success", result.getSuccess());
			resultMap.put("failed", result.getFailure());
			log.info("GCM send message to devices, success = "
					+ result.getSuccess() + ", failed = " + result.getFailure());
			List<Map<String, Object>> errorResults = new ArrayList<Map<String, Object>>();
			List<Result> listResult = result.getResults();
			for (int i = 0; i < listResult.size(); i++) {
				Result res = listResult.get(i);
				if (StringUtil.notEmpty(res.getErrorCodeName())) {
					Map<String, Object> error = new HashMap<String, Object>();
					error.put("errorMsg", res.getErrorCodeName());
					error.put("token", regList.get(i));
					errorResults.add(error);
					log.info("Failed device token = " + regList.get(i));
					log.info("errorMsg = " + res.getErrorCodeName());
				}
			}
			resultMap.put("result", errorResults);
			log.info("GCM send finished!");
			return resultMap;
		} catch (Exception e) {
			log.error("{}", e);
			throw new Exception("GCM Send failed, the error message is "
					+ e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	public static Map<String, List<PushedNotification>> sendAPNS(
			List<String> deviceToken, InputStream certificate,
			String certificatePassword, Boolean isProduction, String msg,
			Map<String, Object> dataInfo, Boolean silent) throws Exception {
		try {
			log.info("APNS send start...");
			Map<String, List<PushedNotification>> map = new HashMap<String, List<PushedNotification>>();
			List<PushedNotification> failedNotifications, successfulNotifications = null;
			Payload payLoad;
			if (silent) {
				payLoad = NewsstandNotificationPayload.contentAvailable();
			} else {
				String sound = "sound.aiff";
				//int badge = 1;
				payLoad = new PushNotificationPayload();
				((PushNotificationPayload) payLoad).addAlert(msg);
				//((PushNotificationPayload) payLoad).addBadge(badge);
				((PushNotificationPayload) payLoad).addSound(sound);
			}

			if (dataInfo != null) {
				for (String key : dataInfo.keySet()) {
					Object value = dataInfo.get(key);
					if (value instanceof String) {
						payLoad.addCustomDictionary(key, value.toString());
					} else if (value instanceof Integer) {
						payLoad.addCustomDictionary(key, (int) value);
					} else if (value instanceof List) {
						payLoad.addCustomDictionary(key, (List) value);
					} else {
						payLoad.addCustomDictionary(key, value.toString());
					}
				}
			}

			List<PushedNotification> notifications = new ArrayList<PushedNotification>();
			if (deviceToken != null & deviceToken.size() > 0) {
				List<Device> device = new ArrayList<Device>();
				for (String token : deviceToken) {
					try {
						device.add(new BasicDevice(token));
					} catch (InvalidDeviceTokenFormatException invalidDeviceTokenFormatException) {
						log.error(invalidDeviceTokenFormatException
								.getMessage());
					}
				}
				notifications = Push.payload(payLoad, certificate,
						certificatePassword, isProduction, NUMBER_THREADS,
						device);
			}

			failedNotifications = PushedNotification
					.findFailedNotifications(notifications);
			successfulNotifications = PushedNotification
					.findSuccessfulNotifications(notifications);

			map.put("success", successfulNotifications);
			map.put("failed", failedNotifications);

			int failed = failedNotifications.size();
			int successful = successfulNotifications.size();

			String resuly = "The APNS send failed = " + failed + ", success = "
					+ successful;
			log.info(resuly);
			for (PushedNotification pushedNotification : failedNotifications) {
				if (pushedNotification.getDevice() != null) {
					log.info("Failed device:"
							+ pushedNotification.getDevice().getToken());
				}
				if (pushedNotification.getException() != null) {
					log.info("Failed message:"
							+ pushedNotification.getException().getMessage());
				}
			}
			log.info("APNS send finished!");
			return map;
		} catch (Exception e) {
			log.error("{}", e);
			throw new Exception("APNS Send failed, the error message is "
					+ e.getMessage());
		}
	}
}
