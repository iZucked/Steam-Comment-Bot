/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing.exceptions;

public class InvalidCurveTypeException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public InvalidCurveTypeException() {
		super();
	}
	
	public InvalidCurveTypeException(String message) {
		super(message);
	}
}
