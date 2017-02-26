package com.tibco.ma.common.log;

import java.lang.reflect.Method;

import net.sf.json.JSONObject;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import ch.qos.logback.classic.Level;

import com.tibco.ma.model.SystemLogger;

@Aspect
@Component
public class LoggerAdvice {
	private static org.slf4j.Logger logback = org.slf4j.LoggerFactory
			.getLogger(LoggerAdvice.class);
	private final int SUCCESS_STATUS = 1;
	private final int FAILURE_STATUS = 0;

	/*
	 * private LogService logService;
	 * 
	 * public LogService getLogService() { return logService; }
	 * 
	 * @Autowired public void setLogService(LogService logService) {
	 * this.logService = logService; }
	 */

	@Around("execution(* *(..)) && @annotation(Log)")
	public Object doRecordLog(ProceedingJoinPoint pjp) throws Throwable {
		Object object = null;
		SystemLogger logger = new SystemLogger();

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (auth != null) {
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			logger.setUsername(userDetails.getUsername());// username
		}

		Object target = pjp.getTarget();
		String methodName = pjp.getSignature().getName();
		Class<?>[] parameterTypes = ((MethodSignature) pjp.getSignature())
				.getMethod().getParameterTypes();
		Method method = target.getClass().getMethod(methodName, parameterTypes);
		if (null != method) {
			if (method.isAnnotationPresent(Log.class)) {
				Log log = method.getAnnotation(Log.class);
				Object[] args = pjp.getArgs();

				logger.setOperate(log.operate());// operate
				logger.setModelName(log.modelName());// modelName
				logger.setRemark(log.remark());// remark
				logger.setMethodName(method.getName());// methodName

				StringBuffer bf = new StringBuffer();
				for (int i = 0; i < args.length; i++) {
					if (null != args[i]) {
						bf.append(args[i] + ";");
					} else {
						bf.append("null;");
					}
				}
				logger.setParams(bf.toString());// params

				long startTime = 0L;
				long endTime = 0L;
				try {
					startTime = System.currentTimeMillis();
					object = pjp.proceed();
					logger.setLevel(Level.INFO.levelStr);
					logger.setStatus(SUCCESS_STATUS);// status success
				} catch (Exception e) {
					logger.setLevel(Level.ERROR.levelStr);
					logger.setStatus(FAILURE_STATUS);// status failure
					logback.error("failure: ", e);
					throw e;
				} finally {
					endTime = System.currentTimeMillis();
					logger.setStartTime(startTime);// start time
					logger.setEndTime(endTime);// end time
					logger.setTime(endTime - startTime);// execute time
				}
			} else {
				object = pjp.proceed();
			}
		} else {
			object = pjp.proceed();
		}
		JSONObject jsonObject = JSONObject.fromObject(logger);
		String json = jsonObject.toString();
		logback.info("{}", json);
		return object;
	}
}
