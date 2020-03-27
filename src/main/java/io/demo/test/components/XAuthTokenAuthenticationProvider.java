package io.demo.test.components;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;

import io.demo.test.entities.UserEntity;
import io.demo.test.exceptions.AccountNotFoundException;
import io.demo.test.services.UserService;

@Component
public class XAuthTokenAuthenticationProvider<S extends Session> implements AuthenticationProvider {

	private static final Log LOGGER = LogFactory.getLog(XAuthTokenAuthenticationProvider.class);

	private final SessionRepository<S> sessionRepository;

	private final UserService userService;

	@Autowired
	public XAuthTokenAuthenticationProvider(SessionRepository<S> sessionRepository, UserService userService) {
		super();
		this.sessionRepository = sessionRepository;
		this.userService = userService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		LOGGER.info("XAuthTokenAuthenticationProvider authenticate method is invoked...");

		if (!this.supports(authentication.getClass()))
			return null;

		String xAuthToken = (String) authentication.getCredentials();

		LOGGER.info("XAuthToken: " + xAuthToken);

		Session session = this.sessionRepository.findById(xAuthToken);

		if (session == null)
			throw new BadCredentialsException("No session was found for the provided session id");

		if (session.isExpired())
			throw new BadCredentialsException("Session expired");

		UserEntity user;

		try {
			String email = session.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME);
			LOGGER.info("Email: " + email);
			user = this.userService.getUserByEmail(email);
		}
		catch (AccountNotFoundException e) {
			throw new BadCredentialsException("Invalid session");
		}

		return new UserAuthentication(user);

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return XAuthTokenAuthentication.class.isAssignableFrom(authentication);
	}

}
