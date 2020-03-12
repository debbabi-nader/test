package io.demo.test.exceptions;


public class WrongPasswordException extends UnauthorizedException {

	private static final long serialVersionUID = -1897863447589813222L;

	public WrongPasswordException() {
		super();
	}

	public WrongPasswordException(Throwable cause) {
		super(cause);
	}
	
}
