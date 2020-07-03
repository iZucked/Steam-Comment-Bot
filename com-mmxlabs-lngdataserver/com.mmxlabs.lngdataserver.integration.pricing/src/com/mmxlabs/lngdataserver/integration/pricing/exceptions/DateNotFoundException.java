/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing.exceptions;

public class DateNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public DateNotFoundException() {
		super();
	}
	
	public DateNotFoundException(String message) {
		super(message);
	}
}
