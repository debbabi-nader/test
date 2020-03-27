package io.demo.test.services;

import io.demo.test.entities.UserEntity;

public interface UserService extends GenericService<UserEntity> {

	public UserEntity getUserByEmail(String email);

}
