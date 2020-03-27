package io.demo.test.exceptions;

public class InvalidEmailAddressException extends BadRequestException {

	private static final long serialVersionUID = -5710534110021717027L;

	public InvalidEmailAddressException() {
		super();
	}

	public InvalidEmailAddressException(Throwable cause) {
		super(cause);
	}

}
