package com.mmxlabs.lngdataserver.distances.exceptions;

public class PortNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PortNotFoundException(){
		super();
	}
	
	public PortNotFoundException(String message){
		super(message);
	}
}
