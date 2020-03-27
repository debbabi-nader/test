package io.demo.test.controllers;

import java.security.Principal;
import java.util.Collection;

import javax.json.JsonPatch;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.demo.test.entities.UserEntity;
import io.demo.test.services.UserService;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {

	private static final Log LOGGER = LogFactory.getLog(UserController.class);

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	@RequestMapping(value = "", params = { "email" }, method = RequestMethod.GET)
	public UserEntity getUserByEmail(@RequestParam(value = "email", required = true) String email) {
		return this.userService.getUserByEmail(email);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	public Collection<UserEntity> getUsers() {
		return this.userService.findAll(Sort.by(Sort.Direction.ASC, "firstName"));
	}

	@RequestMapping(value = "/current", method = RequestMethod.GET)
	public Principal getCurrentUser(Principal user) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if (securityContext == null)
			return null;
		Authentication authentication = securityContext.getAuthentication();
		if (authentication == null)
			return null;
		LOGGER.debug("Name:          " + authentication.getName());
		LOGGER.debug("Authorities:   " + authentication.getAuthorities());
		LOGGER.debug("Principal:     " + authentication.getPrincipal().toString());
		LOGGER.debug("Authenticated: " + authentication.isAuthenticated());

		return user;
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public UserEntity createUser(@Valid @RequestBody UserEntity userEntity) {
		return this.userService.create(userEntity);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json-patch+json")
	public UserEntity partialUpdateUser(@RequestBody JsonPatch jsonPatch,
			@PathVariable(value = "id", required = true) String id) {
		return this.userService.partialUpdate(jsonPatch, id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable(value = "id", required = true) String id) {
		this.userService.delete(id);
	}

}
