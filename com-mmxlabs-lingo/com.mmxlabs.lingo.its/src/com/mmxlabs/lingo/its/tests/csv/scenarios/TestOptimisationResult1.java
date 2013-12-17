/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.csv.scenarios;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.lingo.its.utils.CSVImporter;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;

public class TestOptimisationResult1 extends AbstractOptimisationResultTester {

	@Test
	public void testOptimisationResult() throws IOException, InterruptedException, IncompleteScenarioException, URISyntaxException {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/sample-data/");

		LNGScenarioModel scenario = CSVImporter.importCSVScenario(url.toString());

		runScenario(scenario, new URL(url.toString() + "fitness"));
	}

}
