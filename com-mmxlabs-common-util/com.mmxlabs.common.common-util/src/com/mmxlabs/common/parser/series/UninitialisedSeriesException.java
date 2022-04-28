/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

public class UninitialisedSeriesException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public UninitialisedSeriesException(final String message) {
		super(message);
	}
}
