package io.demo.test.exceptions;


public class ForeignKeyIntegrityViolationException extends BadRequestException {

	private static final long serialVersionUID = -3292893973530211675L;

	public ForeignKeyIntegrityViolationException() {
		super();
	}

	public ForeignKeyIntegrityViolationException(Throwable cause) {
		super(cause);
	}
		
}
