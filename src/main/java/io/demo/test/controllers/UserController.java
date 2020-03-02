package io.demo.test.controllers;

import java.util.Collection;

import javax.json.JsonPatch;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
	public Collection<UserEntity> getUsers() {
		return this.userService.findAll(Sort.by(Sort.Direction.ASC, "firstName"));
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public UserEntity createUser(@Valid @RequestBody UserEntity userEntity) {
		return this.userService.create(userEntity);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json-patch+json")
	public UserEntity partialUpdateUser(@RequestBody JsonPatch jsonPatch, @PathVariable(value = "id", required = true) String id) {
		return this.userService.partialUpdate(jsonPatch, id);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable(value = "id", required = true) String id) {
		this.userService.delete(id);
	}
	
}
