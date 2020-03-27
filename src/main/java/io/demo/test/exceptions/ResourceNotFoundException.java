package io.demo.test.exceptions;

public class ResourceNotFoundException extends NotFoundException {

	private static final long serialVersionUID = -6707290497344410484L;

	private String id;

	public ResourceNotFoundException(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
