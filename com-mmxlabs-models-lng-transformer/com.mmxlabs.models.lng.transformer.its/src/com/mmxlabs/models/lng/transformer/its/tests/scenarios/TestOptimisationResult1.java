/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.scenarios;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;

import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;

public class TestOptimisationResult1 extends AbstractOptimisationResultTester {

	@Test
	public void testOptimisationResult() throws IOException, InterruptedException, IncompleteScenarioException {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/TestOptimisationResult1.scenario");

		runScenario(url);
	}

}
