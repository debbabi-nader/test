package io.demo.test.dtos;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;


public class SignInCredentialsDto implements Serializable {

	private static final long serialVersionUID = 5345872347858453861L;

	@NotBlank
	private String email;
	
	@NotBlank
	private String password;
	
	public SignInCredentialsDto() {
		super();
	}

	public SignInCredentialsDto(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
