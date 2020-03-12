package io.demo.test.services.unittests;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.demo.test.entities.UserEntity;
import io.demo.test.exceptions.AccountNotFoundException;
import io.demo.test.exceptions.BadRequestException;
import io.demo.test.exceptions.ForeignKeyIntegrityViolationException;
import io.demo.test.exceptions.InvalidEmailAddressException;
import io.demo.test.exceptions.MissingRequiredArgumentException;
import io.demo.test.exceptions.ResourceNotFoundException;
import io.demo.test.exceptions.UniqueConstraintViolationException;
import io.demo.test.repositories.UserRepository;
import io.demo.test.services.impl.UserServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;


public class UserServiceImplUnitTest {
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private ObjectMapper objectMapper;
	
	@Mock
	private Validator validator;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@InjectMocks
	private UserServiceImpl userServiceImpl;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void findById_givenAnId_whenIdIsNull_thenThrowsAMissingRequiredArgumentException() {
		
		// given
		String id = null;

		// when + then
		assertThatExceptionOfType(MissingRequiredArgumentException.class).isThrownBy(() -> this.userServiceImpl.findById(id));

	}
	
	@Test
	public void findById_givenAnId_whenIdIsEmpty_thenThrowsAMissingRequiredArgumentException() {
		
		// given
		String id = "";

		// when + then
		assertThatExceptionOfType(MissingRequiredArgumentException.class).isThrownBy(() -> this.userServiceImpl.findById(id));

	}
	
	@Test
	public void findById_givenAnId_whenUserIsNotFound_thenThrowsAResourceNotFoundException() {
		
		// given
		String id = UUID.randomUUID().toString();
		given(this.userRepository.findById(id)).willReturn(Optional.ofNullable(null));
		
		// when + then
		assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> this.userServiceImpl.findById(id));
		
	}
	
	@Test
	public void findById_givenAnId_whenUserIsFound_thenReturnsTheUser() {
		
		// given
		String id = UUID.randomUUID().toString();
		UserEntity user = new UserEntity(id, "John", "Doe", "john.doe@email.com", "", LocalDateTime.now(), LocalDateTime.now());
		given(this.userRepository.findById(id)).willReturn(Optional.of(user));

		// when
		UserEntity foundUser = this.userServiceImpl.findById(id);
		
		// then
		assertThat(foundUser).isNotNull().isEqualToComparingFieldByField(user);

	}
	
	@Test
	public void getUserByEmail_givenAnEmail_whenUserIsNotFound_thenThrowsAnAccountNotFoundException() {
		
		// given
		String email = "john.doe@email.com";
		given(this.userRepository.getUserByEmail(email)).willReturn(null);
		
		// when + then
		assertThatExceptionOfType(AccountNotFoundException.class).isThrownBy(() -> this.userServiceImpl.getUserByEmail(email));

	}
	
	@Test
	public void getUserByEmail_givenAnEmail_whenUserIsFound_thenReturnsTheUser() {
		
		// given
		String email = "john.doe@email.com";
		UserEntity user = new UserEntity(UUID.randomUUID().toString(), "John", "Doe", email, "", LocalDateTime.now(), LocalDateTime.now());
		given(this.userRepository.getUserByEmail(email)).willReturn(user);
		
		// when
		UserEntity foundUser = this.userServiceImpl.getUserByEmail(email);
		
		// then
		assertThat(foundUser).isNotNull().isEqualToComparingFieldByField(user);

	}
	
	@Test
	public void create_givenUserEntity_whenEmailIsNotValid_thenThrowsAnInvalidEmailAddressException() {
		
		// given
		String invalidEmail = "john.doe@email";
		UserEntity user = new UserEntity(UUID.randomUUID().toString(), "John", "Doe", "", invalidEmail, LocalDateTime.now(), LocalDateTime.now());
		
		// when + then
		assertThatExceptionOfType(InvalidEmailAddressException.class).isThrownBy(() -> this.userServiceImpl.create(user));

	}
	
	@Test
	public void create_givenUserEntity_whenDataIntegrityViolationExceptionIsCtachedWithSQLExceptionAsMostSpecificCauseHaving23505SQLState_thenThrowsUniqueConstraintViolationException() {
		
		// given
		UserEntity user = new UserEntity(UUID.randomUUID().toString(), "John", "Doe", "john.doe@email.com", "", LocalDateTime.now(), LocalDateTime.now());
		given(this.userRepository.saveAndFlush(any(UserEntity.class))).willThrow(new DataIntegrityViolationException("", new SQLException("", "23505")));
		
		// when + then
		assertThatExceptionOfType(UniqueConstraintViolationException.class).isThrownBy(() -> this.userServiceImpl.create(user));

	}
	
	@Test
	public void create_givenUserEntity_whenDataIntegrityViolationExceptionIsCtachedWithUnknownCause_thenThrowsBadRequestException() {
		
		// given
		UserEntity user = new UserEntity(UUID.randomUUID().toString(), "John", "Doe", "john.doe@email.com", "", LocalDateTime.now(), LocalDateTime.now());
		given(this.userRepository.saveAndFlush(any(UserEntity.class))).willThrow(DataIntegrityViolationException.class);
		
		// when + then
		assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> this.userServiceImpl.create(user));

	}
	
	@Test
	public void create_givenUserEntity_whenEverythingIsValid_thenReturnsUserEntityWithId() {
		
		// given
		UserEntity user = new UserEntity(null, "John", "Doe", "john.doe@email.com", "", null, null);
		given(this.userRepository.saveAndFlush(any(UserEntity.class))).willAnswer((i) -> {
			UserEntity persistedUser = i.getArgument(0);
			persistedUser.setCreatedAt(LocalDateTime.now());
			persistedUser.setUpdatedAt(LocalDateTime.now());
			return persistedUser;
		});

		// when
		UserEntity createdUser = this.userServiceImpl.create(user);
		
		// then
		assertThat(createdUser.getId()).isNotBlank();

	}
	
	/*
	@Test
	public void partialUpdate_givenAnIdAndAJsonPatch_whenThePatchedUserEntityIsNotValid_thenThrowsAConstraintViolationException() {
		
		// given
		String id = UUID.randomUUID().toString();
		UserEntity user = new UserEntity(id, "John", "Doe", "john.doe@email.com", LocalDateTime.now(), LocalDateTime.now());
		UserServiceImpl userServiceImplSpy = spy(this.userServiceImpl);
		doReturn(user).when(userServiceImplSpy).findById(anyString());

		given(this.validator.validate(any())).willThrow(ConstraintViolationException.class);
		
		// when + then
	    assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(() -> userServiceImplSpy.partialUpdate(Json.createPatch(null), id));

	}
	*/
	
	@Test
	public void delete_givenAnId_whenIdIsNull_thenThrowsAMissingRequiredArgumentException() {
		
		// given
		String id = null;

		// when + then
		assertThatExceptionOfType(MissingRequiredArgumentException.class).isThrownBy(() -> this.userServiceImpl.delete(id));

	}
	
	@Test
	public void delete_givenAnId_whenIdIsEmpty_thenThrowsAMissingRequiredArgumentException() {
		
		// given
		String id = "";

		// when + then
		assertThatExceptionOfType(MissingRequiredArgumentException.class).isThrownBy(() -> this.userServiceImpl.delete(id));

	}
	
	@Test
	public void delete_givenAnId_whenAnEmptyResultDataAccessExceptionIsCatched_thenThrowsAResourceNotFoundException() {
		
		// given
		String id = UUID.randomUUID().toString();
		doThrow(EmptyResultDataAccessException.class).when(this.userRepository).deleteById(anyString());

		// when + then
		assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> this.userServiceImpl.delete(id));

	}
	
	@Test
	public void delete_givenAnId_whenDataIntegrityViolationExceptionIsCtachedWithSQLExceptionAsMostSpecificCauseHaving23503SQLState_thenThrowsForeignKeyIntegrityViolationException() {
		
		// given
		String id = UUID.randomUUID().toString();
		doThrow(new DataIntegrityViolationException("", new SQLException("", "23503"))).when(this.userRepository).deleteById(anyString());
		
		// when + then
		assertThatExceptionOfType(ForeignKeyIntegrityViolationException.class).isThrownBy(() -> this.userServiceImpl.delete(id));

	}
	
	@Test
	public void delete_givenAnId_whenADataIntegrityViolationExceptionIsCatched_thenThrowsABadRequestException() {
		
		// given
		String id = UUID.randomUUID().toString();
		doThrow(DataIntegrityViolationException.class).when(this.userRepository).deleteById(anyString());

		// when + then
		assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> this.userServiceImpl.delete(id));

	}
	
	@Test
	public void delete_givenAnId_whenUserIsDeletedAndChangesAreFlushed_thenDoesNothing() {
		
		// given
		String id = UUID.randomUUID().toString();
		doNothing().when(this.userRepository).deleteById(anyString());
		doNothing().when(this.userRepository).flush();
		
		// when
		this.userServiceImpl.delete(id);
		
		// then
		verify(this.userRepository, atMostOnce()).deleteById(id);
		verify(this.userRepository, atMostOnce()).flush();

	}

}
