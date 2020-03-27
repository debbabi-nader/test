package io.demo.test.configurations;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.Session;

import io.demo.test.components.XAuthTokenAuthenticationFilter;
import io.demo.test.components.XAuthTokenAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration<S extends Session> extends WebSecurityConfigurerAdapter {

	// private final DaoAuthenticationProvider daoAuthenticationProvider;

	/*
	 * @Autowired public SecurityConfiguration(DaoAuthenticationProvider
	 * daoAuthenticationProvider) { super(); this.daoAuthenticationProvider =
	 * daoAuthenticationProvider; }
	 */

	private final XAuthTokenAuthenticationProvider<S> xAuthTokenAuthenticationProvider;

	private final XAuthTokenAuthenticationFilter xAuthTokenAuthenticationFilter;

	@Autowired
	public SecurityConfiguration(XAuthTokenAuthenticationProvider<S> xAuthTokenAuthenticationProvider,
			XAuthTokenAuthenticationFilter xAuthTokenAuthenticationFilter) {
		super();
		this.xAuthTokenAuthenticationProvider = xAuthTokenAuthenticationProvider;
		this.xAuthTokenAuthenticationFilter = xAuthTokenAuthenticationFilter;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(this.xAuthTokenAuthenticationProvider);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// http.csrf(csrf -> csrf.disable())
		// .exceptionHandling()
		// .authenticationEntryPoint((req, rsp, e) ->
		// rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
		// .and()
		// .formLogin()
		// .successHandler((req, rsp, auth) -> rsp.setStatus(HttpServletResponse.SC_OK))
		// .failureHandler(new SimpleUrlAuthenticationFailureHandler())
		// .usernameParameter("email")
		// .loginProcessingUrl("/auth/sign-in")
		// .and()
		// .authorizeRequests()
		// .antMatchers("/auth/**/*").permitAll()
		// .anyRequest().authenticated()
		// .and()
		// .requestCache(requestCache -> requestCache.disable());

		http.csrf().disable();

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeRequests().antMatchers("/auth/**/*").permitAll().antMatchers("/api/v1/**/*").authenticated();

		http.addFilterBefore(this.xAuthTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		http.exceptionHandling().authenticationEntryPoint(
				(req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"));

		http.requestCache(requestCache -> requestCache.disable());

	}

}
