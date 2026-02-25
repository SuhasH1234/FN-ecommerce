package com.zosh.response;

import com.zosh.domain.USER_ROLE;

public class AuthResponse {

	private String jwt;

	private boolean status;

	private String message;

	private USER_ROLE role;

	public AuthResponse() {
	}

	public AuthResponse(String jwt, boolean status, String message, USER_ROLE role) {
		this.jwt = jwt;
		this.status = status;
		this.message = message;
		this.role = role;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public USER_ROLE getRole() {
		return role;
	}

	public void setRole(USER_ROLE role) {
		this.role = role;
	}
}
