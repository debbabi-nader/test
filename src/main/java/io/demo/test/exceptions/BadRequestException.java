package io.demo.test.exceptions;


public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 3571401851459978912L;
	
	public BadRequestException() {
		super();
	}

	public BadRequestException(Throwable cause) {
		super(cause);
	}
	
}
