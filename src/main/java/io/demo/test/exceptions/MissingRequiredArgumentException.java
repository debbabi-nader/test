package io.demo.test.exceptions;


public class MissingRequiredArgumentException extends BadRequestException {

	private static final long serialVersionUID = -533189490346899059L;

	public MissingRequiredArgumentException() {
		super();
	}

	public MissingRequiredArgumentException(Throwable cause) {
		super(cause);
	}
	
}
