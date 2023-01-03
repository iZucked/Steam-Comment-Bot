/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.manager;

public class ModelInconsistentException extends RuntimeException {

	public ModelInconsistentException(final String message) {
		super(message);
	}
	
	@Override
	public String toString() {
		return getMessage();
	}
}
