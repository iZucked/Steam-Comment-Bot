/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

public final class TestingModes {

	private TestingModes() {

	}

	// Never commit as true
	public static final TestMode ReportTestMode = TestMode.Generate;

	/**
	 * Toggle between storing fitness names and values in a properties file and testing the current fitnesses against the stored values. Should be run as part of a plugin test.
	 */
	public static final TestMode OptimisationTestMode = TestMode.Run;

	// Cases with no generated output, Generate will be the same as skip
	public static final TestMode EvaluationCases = TestMode.Run;

	public static final TestMode UATCasestMode = TestMode.Run;

}
