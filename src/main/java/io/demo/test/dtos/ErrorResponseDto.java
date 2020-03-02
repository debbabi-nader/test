package io.demo.test.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;


public class ErrorResponseDto implements Serializable {

	private static final long serialVersionUID = 3793252567678047668L;
	
	private int status;
	private String error;
	private String message;
	private String details;
	private String path;
	private LocalDateTime timestamp;
	
	public ErrorResponseDto() {
		super();
	}
	
	public ErrorResponseDto(int status, String error, String message, String details, String path) {
		super();
		this.status = status;
		this.error = error;
		this.message = message;
		this.details = details;
		this.path = path;
		this.timestamp = LocalDateTime.now();
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getError() {
		return error;
	}
	
	public void setError(String error) {
		this.error = error;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

}
