/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.migration.scenario;

public class ScenarioMigrationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ScenarioMigrationException() {
		super();
	}

	public ScenarioMigrationException(final String message) {
		super(message);
	}

	public ScenarioMigrationException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ScenarioMigrationException(final Throwable cause) {
		super(cause);
	}

}
