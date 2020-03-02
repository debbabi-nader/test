package io.demo.test.exceptions;


public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = -7145997250493033823L;

	public NotFoundException() {
		super();
	}

	public NotFoundException(Throwable cause) {
		super(cause);
	}

}
