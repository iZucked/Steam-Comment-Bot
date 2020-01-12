/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.distances.exceptions;

public class LocationNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LocationNotFoundException(){
		super();
	}
	
	public LocationNotFoundException(String message){
		super(message);
	}
}
