package com.tibco.ma.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.tibco.ma.service.AdminResourceService;

public class MaInvocationSecurityMetadataSource implements
		FilterInvocationSecurityMetadataSource {

	private static final Logger log = LoggerFactory
			.getLogger(MaInvocationSecurityMetadataSource.class);

	@Resource
	private AdminResourceService resourceService;

	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		String requestUrl = ((FilterInvocation) object).getRequestUrl();

		List<String> roleNames = new ArrayList<>();
		try {
			roleNames = resourceService.queryAdminRoleByResulece(requestUrl);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}

		log.info("roles can access " + requestUrl + " : " + roleNames);
		return SecurityConfig.createList(getAccesses(roleNames));
	}

	private String[] getAccesses(List<String> roleNames) {
		String names[] = roleNames.toArray(new String[roleNames.size()]);
		for (int i = 0; i < names.length; i++) {
			names[i] = "ROLE_" + names[i];
		}
		return names;
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

}
