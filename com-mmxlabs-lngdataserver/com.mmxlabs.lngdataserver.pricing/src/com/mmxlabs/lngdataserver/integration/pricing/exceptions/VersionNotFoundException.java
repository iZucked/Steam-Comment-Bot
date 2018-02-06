package com.mmxlabs.lngdataserver.integration.pricing.exceptions;

public class VersionNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public VersionNotFoundException(){
		super();
	}
	
	public VersionNotFoundException(String message){
		super(message);
	}
}
