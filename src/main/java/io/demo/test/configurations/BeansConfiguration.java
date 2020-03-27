package io.demo.test.configurations;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
// import org.springframework.session.web.http.HttpSessionIdResolver;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

// import io.demo.test.services.CustomUserDetailsService;


@Configuration
public class BeansConfiguration {
	
	/*
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	 */
	
	@Bean
    public ObjectMapper objectMapper() {
		
		return new ObjectMapper()
				.setDefaultPropertyInclusion(Include.NON_NULL)
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
				.findAndRegisterModules();
		
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	
	}
	
	/*
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
	
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		
		daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(this.customUserDetailsService);
		
		return daoAuthenticationProvider;
	
	}
	*/
	
	/*
	@Bean
	public HttpSessionIdResolver httpSessionIdResolver() {
	
		return HeaderHttpSessionIdResolver.xAuthToken(); 
	
	}
	*/
	
}
