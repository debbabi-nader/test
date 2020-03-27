package io.demo.test.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.demo.test.entities.UserEntity;
import io.demo.test.exceptions.AccountNotFoundException;
import io.demo.test.services.CustomUserDetailsService;
import io.demo.test.services.UserService;

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserEntity userEntity;

		try {
			userEntity = this.userService.getUserByEmail(username);
		}
		catch (AccountNotFoundException ex) {
			throw new UsernameNotFoundException(username);
		}

		return new User(userEntity.getEmail(), userEntity.getPassword(), true, true, true, true,
				AuthorityUtils.createAuthorityList("ROLE_USER"));

	}

}
