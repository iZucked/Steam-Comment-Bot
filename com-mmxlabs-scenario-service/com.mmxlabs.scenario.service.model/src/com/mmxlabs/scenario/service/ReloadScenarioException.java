/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service;

/**
 * Exception thrown when attempting to reload a previous failed to load scenario.
 * 
 * @author Simon Goodall
 * 
 */
public class ReloadScenarioException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ReloadScenarioException(Exception e) {
		super(e);
	}
}
