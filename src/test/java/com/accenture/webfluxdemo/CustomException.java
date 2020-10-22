package com.accenture.webfluxdemo;

public class CustomException extends Throwable {

	private String message;

	public CustomException(Throwable e) {
		this.message = e.getMessage();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
