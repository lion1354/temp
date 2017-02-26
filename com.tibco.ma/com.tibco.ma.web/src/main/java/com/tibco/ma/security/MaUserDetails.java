package com.tibco.ma.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.tibco.ma.model.AdminRole;
import com.tibco.ma.model.AdminUser;

public class MaUserDetails implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AdminUser user;

	public MaUserDetails(AdminUser user) {
		this.user = user;
	}

	// retrieve user's authority
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> gaList = new ArrayList<GrantedAuthority>();

		List<AdminRole> roles = user.getRoles();
		for (AdminRole r : roles) {
			MAGrantedArthority ga = new MAGrantedArthority(r.getId());

			gaList.add(ga);
		}
		return gaList;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;// !user.getState().equals("0");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MaUserDetails other = (MaUserDetails) obj;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

}
