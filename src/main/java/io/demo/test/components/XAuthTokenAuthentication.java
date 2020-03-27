package io.demo.test.components;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class XAuthTokenAuthentication implements Authentication {

	private static final long serialVersionUID = 348535158555772773L;

	private final String xAuthToken;

	public XAuthTokenAuthentication(String xAuthToken) {
		super();
		this.xAuthToken = xAuthToken;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public Object getCredentials() {
		return this.xAuthToken;
	}

	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}

	@Override
	public boolean isAuthenticated() {
		return false;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

	}

}
