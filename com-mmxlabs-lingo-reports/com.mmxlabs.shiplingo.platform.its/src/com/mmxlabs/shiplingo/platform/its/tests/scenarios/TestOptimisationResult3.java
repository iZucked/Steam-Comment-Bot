/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.its.tests.scenarios;

import java.io.IOException;
import java.net.URL;

import org.eclipse.emf.edapt.migration.MigrationException;
import org.junit.Test;

import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;

public class TestOptimisationResult3 extends AbstractOptimisationResultTester {

	@Test
	public void testFitnessRepeatability() throws IOException, IncompleteScenarioException, InterruptedException, MigrationException {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/TestOptimisationResult3.scenario");

		runScenario(url);

	}
}
