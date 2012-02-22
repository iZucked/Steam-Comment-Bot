/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.its.tests.scenarios;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;

import com.mmxlabs.lngscheduler.emf.extras.IncompleteScenarioException;

public class TestOptimisationResult1 extends AbstractOptimisationResultTester {

	@Test
	public void testFitnessRepeatability() throws IOException, IncompleteScenarioException, InterruptedException {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/TestOptimisationResult1.scenario");

		runScenario(url);
	}

}
