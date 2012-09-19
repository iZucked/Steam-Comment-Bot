/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.its.tests.csv.scenarios;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.emf.edapt.migration.MigrationException;
import org.junit.Test;

import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.shiplingo.platform.its.tests.scenarios.AbstractOptimisationResultTester;

public class TestOptimisationResult1 extends AbstractOptimisationResultTester {

	@Test
	public void testOptimisationResult() throws IOException, InterruptedException, IncompleteScenarioException, MigrationException, URISyntaxException {

		// Load the scenario to test
		final URL url = getClass().getResource("/csv-import/samples-data/");

		File f = new File(url.toURI());
		MMXRootObject scenario = CSVImporter.importCSVScenario(f.toString());
		
		runScenario(scenario, new URL(url.toString() + "fitness"));
	}

}
