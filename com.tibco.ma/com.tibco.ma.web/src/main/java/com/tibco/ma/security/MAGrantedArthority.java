package com.tibco.ma.security;

import org.springframework.security.core.GrantedAuthority;

public class MAGrantedArthority implements GrantedAuthority {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8903413191303528839L;
	private String authroity;

	public MAGrantedArthority() {

	}

	public MAGrantedArthority(String authroity) {
		this.authroity = authroity;
	}

	@Override
	public String getAuthority() {

		return "ROLE_" + authroity;

	}

	@Override
	public String toString() {
		return getAuthority();
	}
}
