/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.csv.scenarios;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.lng.transformer.its.tests.scenarios.AbstractOptimisationResultTester;
import com.mmxlabs.models.mmxcore.MMXRootObject;

public class TestOptimisationResult1 extends AbstractOptimisationResultTester {

	@Test
	public void testOptimisationResult() throws IOException, InterruptedException, IncompleteScenarioException, URISyntaxException {

		// Load the scenario to test
		final URL url = getClass().getResource("/csv-import/sample-data/");

		MMXRootObject scenario = CSVImporter.importCSVScenario(url.toString());

		runScenario(scenario, new URL(url.toString() + "fitness"));
	}

}
