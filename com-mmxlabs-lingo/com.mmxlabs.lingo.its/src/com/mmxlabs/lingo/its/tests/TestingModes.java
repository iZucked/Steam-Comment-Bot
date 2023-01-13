/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

public final class TestingModes {

	private TestingModes() {

	}

	// Never commit as true
	public static final TestMode ReportTestMode = TestMode.Run;

	/**
	 * Toggle between storing fitness names and values in a properties file and testing the current fitnesses against the stored values. Should be run as part of a plugin test.
	 */
	public static final TestMode OptimisationTestMode = TestMode.Run;

	public static final TestMode UATCasestMode = TestMode.Run;

}
