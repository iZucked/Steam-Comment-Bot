/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

public class UninitialisedSeriesException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public UninitialisedSeriesException(final String message) {
		super(message);
	}
}
