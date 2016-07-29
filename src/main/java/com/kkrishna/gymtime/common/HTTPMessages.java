package com.kkrishna.gymtime.common;

public enum HTTPMessages {
	INVALID_EMAIL_OR_PASSWORD("Invalid Email Or Password"), 
	EMAIL_ALREADY_EXISTS("Email Already Exists"),
	SUCCESS("Success");

	private String message;

	private HTTPMessages(String message) {

		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
