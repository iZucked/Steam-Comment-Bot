/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.distances.exceptions;

public class VersionNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public VersionNotFoundException(){
		super();
	}
	
	public VersionNotFoundException(String message){
		super(message);
	}
}
