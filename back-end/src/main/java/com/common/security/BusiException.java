package com.common.security;

public class BusiException extends RuntimeException {
	private static final long serialVersionUID = -3876502758804606346L;

	public BusiException() {
	}

	public BusiException(String message) {
		super(message);
	}

	public BusiException(String message, Throwable th) {
	    super(message, th);
	}

	public BusiException(Throwable th) {
		super(th);
	}

	public String getMessage() {
		return super.getMessage();
	}
}