package com.mmxlabs.lngdataserver.integration.distances.exceptions;

public class RouteOptionNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public RouteOptionNotFoundException(){
		super();
	}
	
	public RouteOptionNotFoundException(String message){
		super(message);
	}
}
