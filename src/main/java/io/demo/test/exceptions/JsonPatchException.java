package io.demo.test.exceptions;


public class JsonPatchException extends BadRequestException {

	private static final long serialVersionUID = -4272201182065893303L;

	public JsonPatchException() {
		super();
	}

	public JsonPatchException(Throwable cause) {
		super(cause);
	}
	
}
