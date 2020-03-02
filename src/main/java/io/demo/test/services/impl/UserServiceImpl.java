package io.demo.test.services.impl;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.demo.test.entities.UserEntity;
import io.demo.test.exceptions.AccountNotFoundException;
import io.demo.test.exceptions.InvalidEmailAddressException;
import io.demo.test.repositories.UserRepository;
import io.demo.test.services.UserService;


@Service
public class UserServiceImpl extends GenericServiceImpl<UserEntity> implements UserService {

	private final UserRepository userRepository;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}
	
	@Override
	public UserEntity getUserByEmail(String email) {
		
		UserEntity userEntity = this.userRepository.getUserByEmail(email);
		if (userEntity == null)
			throw new AccountNotFoundException(email);
		return userEntity;

	}

	@Transactional
	@Override
	public UserEntity create(UserEntity userEntity) {
		
		if (!Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").matcher(userEntity.getEmail()).matches())
			throw new InvalidEmailAddressException();
		
		return super.create(userEntity);

	}
	
}
