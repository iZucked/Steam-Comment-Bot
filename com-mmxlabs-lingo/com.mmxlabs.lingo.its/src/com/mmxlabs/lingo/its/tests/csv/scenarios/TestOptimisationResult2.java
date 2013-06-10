/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.csv.scenarios;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

import com.mmxlabs.lingo.its.tests.scenarios.AbstractOptimisationResultTester;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;

public class TestOptimisationResult2 extends AbstractOptimisationResultTester {

	@Test
	public void testOptimisationResult() throws IOException, InterruptedException, IncompleteScenarioException, URISyntaxException {

		// Load the scenario to test
		final URL url = getClass().getResource("/csv-import/des-cargo/");

		LNGScenarioModel scenario = CSVImporter.importCSVScenario(url.toString());

		runScenario(scenario, new URL(url.toString() + "fitness"));
	}

}
