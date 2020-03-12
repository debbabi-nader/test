package io.demo.test.services;

import io.demo.test.dtos.SignInCredentialsDto;
import io.demo.test.dtos.TokenDto;


public interface AuthenticationService {
	
	public TokenDto signIn(SignInCredentialsDto signInCredentialsDto);

}
