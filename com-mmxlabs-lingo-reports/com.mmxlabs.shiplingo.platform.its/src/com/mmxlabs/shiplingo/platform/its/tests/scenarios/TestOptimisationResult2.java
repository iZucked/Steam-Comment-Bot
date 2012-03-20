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

public class TestOptimisationResult2 extends AbstractOptimisationResultTester {

	/**
	 * If run on two separate occasions the fitnesses generated need to be identical. This method tests this by being run twice. The first execution prints out a map that maps the name of the fitness
	 * to the value to the console. This is copied and pasted into the method. The second execution will test that map against a the fitnesses that have been generated again.
	 * 
	 * @throws IOException
	 * @throws IncompleteScenarioException
	 * @throws InterruptedException
	 * @throws MigrationException 
	 */
	@Test
	public void testOptimisationResult() throws IOException, InterruptedException, IncompleteScenarioException, MigrationException {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/TestOptimisationResult2.scenario");

		runScenario(url);
	}
}
