package io.demo.test.exceptions;

public class UnauthorizedException extends RuntimeException {

	private static final long serialVersionUID = 7568970648889993459L;

	public UnauthorizedException() {
		super();
	}

	public UnauthorizedException(Throwable cause) {
		super(cause);
	}

}
