package com.popular.rest;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.popular.common.AppConfig;
import com.popular.common.ClientVersionUtil;
import com.popular.common.CodecUtil;
import com.popular.common.util.StringUtils;
import com.popular.exception.DBException;
import com.popular.model.LoginInformation;
import com.popular.responseutil.ResponseStatus;
import com.popular.responseutil.ResponseUtils;
import com.popular.service.LoginInformationService;

@EnableWebMvc
@ControllerAdvice(annotations = RestController.class)
public class RestTokenValidateAdvice {
	private static Logger log = LoggerFactory.getLogger(SampleService.class);

	@Autowired
	private AppConfig appConfig;

	@Autowired
	private LoginInformationService loginInformationService;

	@ModelAttribute
	public void validate(Model model, WebRequest request,
			@RequestHeader(value = "client-agent", required = true) String client_agent,
			@RequestHeader(value = "request_time", required = true) String request_time,
			@RequestHeader(value = "client_id", required = true) String client_id,
			@RequestHeader(value = "type", required = false) String type,
			@RequestHeader(value = "security_token", required = true) String security_token) {
		try {
			if (appConfig.getSkipAPISecurity().equals("false")) {
				return;
			}
			String path = request.getContextPath();

			String client_secret = appConfig.getClientSecret();

			// log.info(
			// "path: {}, client-agent: {}, request_time: {}, client_id: {},
			// security_token: {}",
			// path, client_agent, request_time, client_id, security_token);

			if (StringUtils.isEmpty(client_id)) {

				log.warn("Permission denied on " + path + ", the 'client_id' is required in the header!!");
				throw new InvalidTokenException("Permission denied, the 'client_id' is required in the header!!");

			}

			ServletWebRequest servletWebRequest = (ServletWebRequest) request;
			String requestUri = servletWebRequest.getRequest().getRequestURI();

			if (!requestUri.contains("/login")
					&& !requestUri.contains("/register")
					&& !requestUri.contains("/sms")
					&& !requestUri.contains("/forgotPassword")) {
				try {
					LoginInformation info = loginInformationService.getLoginfoByDevice(client_id);
					if (info == null)
						throw new InvalidTokenException("this device had not login app!!");

				} catch (DBException e) {
					log.error("error :", e);
					throw new InvalidTokenException("this device had not login app!!");
				}
			}

			if (StringUtils.isEmpty(client_agent)) {
				log.warn("Permission denied on " + path + ", the '" + ClientVersionUtil.CLIENT_AGENT_KEY
						+ "' is required in the header!!");
				throw new InvalidTokenException("Permission denied, the '" + ClientVersionUtil.CLIENT_AGENT_KEY
						+ "' is required in the header!!");
			}

			if (StringUtils.isEmpty(request_time)) {
				log.warn("Permission denied on " + path + ", the 'request_time' is required in the header!!");
				throw new InvalidTokenException("Permission denied, the 'request_time' is required in the header!!");
			}

			if (StringUtils.isEmpty(security_token)) {
				log.warn("Permission denied on " + path + ", the 'security_token' is required in the header!!");
				throw new InvalidTokenException("Permission denied, the 'security_token' is required in the header!!");
			}

			String token = CodecUtil.genMD5Token(client_secret, client_id, request_time, client_agent);
			if (!token.equals(security_token)) {
				StringBuffer sb = new StringBuffer();
				sb.append("client_secret: ").append(client_secret).append("client_id: ").append(client_id)
						.append("request_time: ").append(request_time).append("client_agent: ").append(client_agent)
						.append("security_token: ").append(security_token);
				log.warn("Permission denied on " + path + ", invalid token!![" + sb + "]");
				throw new InvalidTokenException("Permission denied on " + path + ", invalid token!![" + sb + "]");
			} else {
				log.debug("pass security filter on " + path + "!");
			}
		} catch (NoSuchAlgorithmException e) {
			log.error("Permission denied, server internal error!", e);
			throw new InvalidTokenException("Permission denied, server internal error!");
		} catch (UnsupportedEncodingException e) {
			log.error("Permission denied, server internal error!!", e);
			throw new InvalidTokenException("PermissionPermission denied, server internal error!!");
		}

	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<?> handleException(InvalidTokenException e) {

		return ResponseUtils.newResponse().status(ResponseStatus.ERROR).httpStatus(HttpStatus.FORBIDDEN)
				.errorMsg(e.getMessage()).build();
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
