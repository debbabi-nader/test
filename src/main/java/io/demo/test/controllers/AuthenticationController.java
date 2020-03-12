package io.demo.test.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.demo.test.dtos.SignInCredentialsDto;
import io.demo.test.dtos.TokenDto;
import io.demo.test.services.AuthenticationService;


@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	@Autowired
	public AuthenticationController(AuthenticationService authenticationService) {
		super();
		this.authenticationService = authenticationService;
	}

	@RequestMapping(value = "/manual-sign-in", method = RequestMethod.POST)
	public TokenDto signIn(@Valid @RequestBody SignInCredentialsDto signInCredentialsDto) {
		return this.authenticationService.signIn(signInCredentialsDto);
	}
	
}
