package io.demo.test.services.unittests;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.demo.test.entities.UserEntity;
import io.demo.test.exceptions.AccountNotFoundException;
import io.demo.test.repositories.UserRepository;
import io.demo.test.services.impl.UserServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;


public class UserServiceImplUnitTest {

	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private UserServiceImpl userServiceImpl;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void getUserByEmail_givenAnEmail_whenUserExists_thenReturnsTheUser() {
		
		// given
		String email = "john.doe@email.com";
		UserEntity user = new UserEntity(UUID.randomUUID().toString(), "John", "Doe", email, LocalDateTime.now(), LocalDateTime.now());
		given(this.userRepository.getUserByEmail(email)).willReturn(user);
		
		// when
		UserEntity foundUser = this.userServiceImpl.getUserByEmail(email);
		
		// then
		assertThat(foundUser).isNotNull().isEqualToComparingFieldByField(user);

	}
	
	@Test
	public void getUserByEmail_givenAnEmail_whenUserDoesNotExist_thenThrowsAnAccountNotFoundException() {
		
		// given
		String email = "john.doe@email.com";
		given(this.userRepository.getUserByEmail(email)).willReturn(null);
		
		// when + then
		assertThatExceptionOfType(AccountNotFoundException.class).isThrownBy(() -> this.userServiceImpl.getUserByEmail(email));

	}

}
