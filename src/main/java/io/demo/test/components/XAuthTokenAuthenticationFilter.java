package io.demo.test.components;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.ForwardAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

@Component
public class XAuthTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private static final Log LOGGER = LogFactory.getLog(XAuthTokenAuthenticationFilter.class);

	public XAuthTokenAuthenticationFilter() {
		super(new AntPathRequestMatcher("/api/v1/**"));
	}

	@Autowired
	@Override
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		String requestURL = request.getRequestURI().substring(request.getContextPath().length());

		LOGGER.info("XAuthTokenAuthenticationFilter attempting to authenticate request: " + requestURL);

		super.setAuthenticationSuccessHandler(new ForwardAuthenticationSuccessHandler(requestURL));

		String xAuthToken = request.getHeader("X-Auth-Token");
		if (xAuthToken == null)
			xAuthToken = "";

		XAuthTokenAuthentication xAuthTokenAuthentication = new XAuthTokenAuthentication(xAuthToken);

		return this.getAuthenticationManager().authenticate(xAuthTokenAuthentication);

	}

}
