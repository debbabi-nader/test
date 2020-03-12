package io.demo.test.dtos;

import java.io.Serializable;


public class TokenDto implements Serializable {

	private static final long serialVersionUID = -2986197958941299905L;
	
	private String accessToken;

	public TokenDto() {
		super();
	}

	public TokenDto(String accessToken) {
		super();
		this.accessToken = accessToken;
	}
	
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}
