package io.demo.test.configurations;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	private final DaoAuthenticationProvider daoAuthenticationProvider;
	
	@Autowired
	public SecurityConfiguration(DaoAuthenticationProvider daoAuthenticationProvider) {
		super();
		this.daoAuthenticationProvider = daoAuthenticationProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(this.daoAuthenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf(csrf -> csrf.disable())
			.exceptionHandling()
			.authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
			.and()
			.formLogin()
			.successHandler((req, rsp, auth) -> rsp.setStatus(HttpServletResponse.SC_OK))
			.failureHandler(new SimpleUrlAuthenticationFailureHandler())
			.usernameParameter("email")
			.loginProcessingUrl("/auth/sign-in")
			.and()
			.authorizeRequests()
			.antMatchers("/auth/**/*").permitAll()
			.anyRequest().authenticated()
			.and()
			.requestCache(requestCache -> requestCache.disable());
			
	}
	
}
