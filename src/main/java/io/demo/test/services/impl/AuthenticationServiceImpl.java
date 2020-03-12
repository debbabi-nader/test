package io.demo.test.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;

import io.demo.test.dtos.SignInCredentialsDto;
import io.demo.test.dtos.TokenDto;
import io.demo.test.entities.UserEntity;
import io.demo.test.exceptions.WrongPasswordException;
import io.demo.test.services.AuthenticationService;
import io.demo.test.services.UserService;


@Service
public class AuthenticationServiceImpl<S extends Session> implements AuthenticationService {
	
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final SessionRepository<S> sessionRepository;
	
	@Autowired
	public AuthenticationServiceImpl(UserService userService, PasswordEncoder passwordEncoder, SessionRepository<S> sessionRepository) {
		super();
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.sessionRepository = sessionRepository;
	}

	@SuppressWarnings("unchecked")
	@Override
	public TokenDto signIn(SignInCredentialsDto signInCredentialsDto) {
		
		UserEntity user = this.userService.getUserByEmail(signInCredentialsDto.getEmail());
		
		if (!passwordEncoder.matches(signInCredentialsDto.getPassword(), user.getPassword()))
			throw new WrongPasswordException();

		Session session = this.sessionRepository.createSession();
		session.setAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, user.getEmail());
		this.sessionRepository.save((S) session);

		return new TokenDto(session.getId());
	
	}

}
