package io.demo.test.components;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.demo.test.entities.UserEntity;

public class UserAuthentication implements Authentication {

	private static final long serialVersionUID = -7657268603858246005L;

	private final UserEntity userEntity;

	public UserAuthentication(UserEntity userEntity) {
		super();
		this.userEntity = userEntity;
	}

	@Override
	public String getName() {
		return this.userEntity.getFirstName() + " " + this.userEntity.getLastName();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return this.userEntity;
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

	}

}
