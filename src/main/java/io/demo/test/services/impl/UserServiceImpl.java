package io.demo.test.services.impl;

import java.util.regex.Pattern;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.demo.test.entities.UserEntity;
import io.demo.test.exceptions.AccountNotFoundException;
import io.demo.test.exceptions.InvalidEmailAddressException;
import io.demo.test.repositories.UserRepository;
import io.demo.test.services.UserService;


@Service
public class UserServiceImpl extends GenericServiceImpl<UserEntity> implements UserService {
	
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserServiceImpl(UserRepository repository, ObjectMapper objectMapper, Validator validator, PasswordEncoder passwordEncoder) {
		super(repository, objectMapper, validator);
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserEntity getUserByEmail(String email) {

		UserEntity userEntity = ((UserRepository) this.repository).getUserByEmail(email);
		if (userEntity == null)
			throw new AccountNotFoundException(email);
		return userEntity;

	}

	@Transactional
	@Override
	public UserEntity create(UserEntity userEntity) {
		
		if (!Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").matcher(userEntity.getEmail()).matches())
			throw new InvalidEmailAddressException();
		
		userEntity.setPassword(this.passwordEncoder.encode(userEntity.getPassword()));
		
		return super.create(userEntity);

	}
	
}
