/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.scenarios;

import java.net.URL;

import org.junit.Test;

public class TestOptimisationResult1 extends AbstractOptimisationResultTester {

	@Test
	public void testOptimisationResult() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/TestOptimisationResult1.scenario");

		runScenario(url);
	}

}
