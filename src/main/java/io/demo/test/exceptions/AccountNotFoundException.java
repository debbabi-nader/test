package io.demo.test.exceptions;


public class AccountNotFoundException extends NotFoundException {

	private static final long serialVersionUID = -25539262776109991L;
	
	private String email;

	public AccountNotFoundException(String email) {
		super();
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
