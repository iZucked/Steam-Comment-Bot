/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

public class ScenarioNotEvaluatedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ScenarioNotEvaluatedException(final String message) {
		super(message);
	}

	public ScenarioNotEvaluatedException() {
		super();
	}
}
