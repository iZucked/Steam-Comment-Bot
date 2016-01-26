/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.migration.scenario;

public class FinalClientMigratedVersionMismatchException extends ScenarioMigrationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String clientContext;
	private final int expectedVersion;
	private final int actualVersion;

	public FinalClientMigratedVersionMismatchException(final String clientContext, final int expectedVersion, final int actualVersion) {
		super();
		this.clientContext = clientContext;
		this.expectedVersion = expectedVersion;
		this.actualVersion = actualVersion;
	}

	public String getClientContext() {
		return clientContext;
	}

	public int getExpectedVersion() {
		return expectedVersion;
	}

	public int getActualVersion() {
		return actualVersion;
	}

}
