package com.tibco.ma.controller.rest;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.tibco.ma.common.Constants;
import com.tibco.ma.common.ResponseStatus;
import com.tibco.ma.common.ResponseUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.model.App;
import com.tibco.ma.service.AppService;

@ControllerAdvice(annotations = RestController.class)
public class RestTokenValidateAdvice {
	private static Logger log = LoggerFactory.getLogger(SampleService.class);

	@Resource
	private AppService appService;

	@ModelAttribute
	public void validate(
			Model model,
			WebRequest request,
			@RequestHeader(value = "X-MA-Application-Id", required = false) String app_key,
			@RequestHeader(value = "X-MA-REST-API-Key", required = false) String rest_api_key,
			@RequestHeader(value = "client-agent", required = false) String client_agent,
			@RequestHeader(value = "request_time", required = false) String request_time,
			@RequestHeader(value = "client_id", required = false) String client_id
//			@RequestHeader(value = "security_token", required = false) String security_token
			) {
//		try {
			if (Constants.SKIP_API_SECURITY) {
				return;
			}
			String path = request.getContextPath();

			if (StringUtil.isEmpty(app_key)) {
				log.warn("Permission denied on " + path
						+ ", the 'app_id' is required in the header!!");
				throw new InvalidTokenException(
						"Permission denied, the 'app_id' is required in the header!!");
			}

			if (StringUtil.isEmpty(rest_api_key)) {
				log.warn("Permission denied on " + path
						+ ", the 'rest_api_key' is required in the header!!");
				throw new InvalidTokenException(
						"Permission denied, the 'rest_api_key' is required in the header!!");
			}

			// log.info(
			// "path: {}, client-agent: {}, request_time: {}, client_id: {}, security_token: {}",
			// path, client_agent, request_time, client_id, security_token);

//			if (StringUtil.isEmpty(client_id)) {
//
//				log.warn("Permission denied on " + path
//						+ ", the 'client_id' is required in the header!!");
//				throw new InvalidTokenException(
//						"Permission denied, the 'client_id' is required in the header!!");
//			}
//
//			if (StringUtil.isEmpty(client_agent)) {
//				log.warn("Permission denied on " + path + ", the '"
//						+ ClientVersionUtil.CLIENT_AGENT_KEY
//						+ "' is required in the header!!");
//				throw new InvalidTokenException("Permission denied, the '"
//						+ ClientVersionUtil.CLIENT_AGENT_KEY
//						+ "' is required in the header!!");
//			}
//
//			if (StringUtil.isEmpty(request_time)) {
//				log.warn("Permission denied on " + path
//						+ ", the 'request_time' is required in the header!!");
//				throw new InvalidTokenException(
//						"Permission denied, the 'request_time' is required in the header!!");
//			}

//			if (StringUtil.isEmpty(security_token)) {
//				log.warn("Permission denied on " + path
//						+ ", the 'security_token' is required in the header!!");
//				throw new InvalidTokenException(
//						"Permission denied, the 'security_token' is required in the header!!");
//			}

//			String client_secret = null;
			App app = null;
			try {
				app = appService.findById(new ObjectId(app_key), App.class);
				
//				client_secret = app.getSecretKey();
			} catch (Exception e) {
				log.error("get access key error", e);
			}

			if (app == null) {
				log.warn("Permission denied on " + path
						+ ", the 'app_id' is invalid!!");
				throw new InvalidTokenException(
						"Permission denied, the 'app_id' is invalid!!");
			}
			if (app != null
					&& !StringUtil.notEmptyAndEqual(app.getRestApiKey(),
							rest_api_key)) {
				log.warn("Permission denied on " + path
						+ ", the 'rest_api_key' is invalid!!");
				throw new InvalidTokenException(
						"Permission denied, the 'rest_api_key' is invalid!!");
			}

			log.debug("pass security filter on " + path + "!");
			
//			String token = CodecUtil.genMD5Token(app_key, rest_api_key,
//					client_secret, client_id, request_time, client_agent);
//			if (!token.equals(security_token)) {
//				StringBuffer sb = new StringBuffer();
//				sb.append("client_secret: ").append(client_secret)
//						.append("client_id: ").append(client_id)
//						.append("request_time: ").append(request_time)
//						.append("client_agent: ").append(client_agent)
//						.append("security_token: ").append(security_token);
//				log.warn("Permission denied on " + path + ", invalid token!!["
//						+ sb + "]");
//				throw new InvalidTokenException("Permission denied on " + path
//						+ ", invalid token!![" + sb + "]");
//			} else {
//				log.debug("pass security filter on " + path + "!");
//			}
//		} catch (NoSuchAlgorithmException e) {
//			log.error("Permission denied, server internal error!", e);
//			throw new InvalidTokenException(
//					"Permission denied, server internal error!");
//		} catch (UnsupportedEncodingException e) {
//			log.error("Permission denied, server internal error!!", e);
//			throw new InvalidTokenException(
//					"PermissionPermission denied, server internal error!!");
//		}

	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<?> handleException(InvalidTokenException e) {

		return ResponseUtils.newResponse().status(ResponseStatus.ERROR)
				.httpStatus(HttpStatus.FORBIDDEN).errorMsg(e.getMessage())
				.build();
	}

	class InvalidTokenException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public InvalidTokenException() {
			super();
		}

		public InvalidTokenException(String message) {
			super(message);
		}
	}
}
