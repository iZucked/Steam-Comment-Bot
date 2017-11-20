package com.mmxlabs.lngdataserver.pricing.exceptions;

public class DateNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public DateNotFoundException() {
		super();
	}
	
	public DateNotFoundException(String message) {
		super(message);
	}
}
