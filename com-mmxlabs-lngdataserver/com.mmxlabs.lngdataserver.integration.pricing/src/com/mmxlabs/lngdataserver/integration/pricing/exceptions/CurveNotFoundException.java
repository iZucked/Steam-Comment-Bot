/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing.exceptions;

public class CurveNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public CurveNotFoundException() {
		super();
	}
	
	public CurveNotFoundException(String message) {
		super(message);
	}
}
