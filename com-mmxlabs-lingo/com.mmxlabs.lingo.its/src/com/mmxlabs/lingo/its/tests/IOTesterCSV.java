/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

import org.eclipse.emf.common.util.EList;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.mmxlabs.models.lng.schedule.Fitness;

@RunWith(Parameterized.class)
public class IOTesterCSV {

	private final String name;
	private final String scenarioPath;

	public IOTesterCSV(final String name, final String scenarioPath) throws Exception {

		this.name = name;
		this.scenarioPath = scenarioPath;
	}

	@Parameters(name = "{0}")
	public static Iterable<Object[]> generateTests() {
		return Arrays.asList(new Object[][] { { "testBonnyProblems_Bonny", "/scenarios/demo-cases/Bonny problems/0 Bonny.scenario - CSV/" },
				{ "testBonnyProblems_LateAndLost_DES_Backfill", "/scenarios/demo-cases/Bonny problems/1 late and lost-DES backfill -F- Bonny.scenario - CSV/" },
				{ "testBonnyProblems_O_LateAndLost_DES_Backfill", "/scenarios/demo-cases/Bonny problems/2 -O- late and lost-DES backfill -F- Bonny.scenario - CSV/" },
				{ "testDryDockIssues_base", "/scenarios/demo-cases/Dry dock issues/0 base.scenario - CSV/" },
				{ "testDryDockIssues_dryDock55_lateness_removed", "/scenarios/demo-cases/Dry dock issues/3 -O- dry dock 55 -F- lateness removed -F- base.scenario - CSV/" },
				{ "testDryDockIssues_dryDock70", "/scenarios/demo-cases/Dry dock issues/4 dry dock 70 -O- -F- -F- base.scenario - CSV/" },
				{ "testDryDockIssues_charterInGenerated", "/scenarios/demo-cases/Dry dock issues/5 charter-in generated - rewire for shorter one -O- -F- -F- base.scenario - CSV/" },
				{ "testFleetCostOptimisation_fleet_demo", "/scenarios/demo-cases/Fleet cost optimisation/0 fleet demo-scenario.scenario - CSV/" },
				{ "testFleetCostOptimisation_O_fleet_demo", "/scenarios/demo-cases/Fleet cost optimisation/1 -O- fleet demo-scenario.scenario - CSV/" },

		});
	}

	@Test
	public void testCSVInOut() throws Exception {

		final URL url = getClass().getResource(this.scenarioPath);

		final ITestDataProvider testCaseProvider = new CSVTestDataProvider(url);
		testCaseProvider.execute(testCase -> {
			final EList<Fitness> originalFitnesses = IOTestUtil.ScenarioModeltoFitnessList(testCase);
			final long[] originalArray = IOTestUtil.fitnessListToArrayValues(originalFitnesses);
			final File restoredFile = IOTestUtil.exportTestCase(testCase);

			try {

				final URL restoredURL = IOTestUtil.directoryFileToURL(restoredFile);

				final ITestDataProvider restoredCaseProvider = new CSVTestDataProvider(restoredURL);
				restoredCaseProvider.execute(restoredCase -> {
					final EList<Fitness> restoredFitnesses = IOTestUtil.ScenarioModeltoFitnessList(restoredCase);
					final long[] restoredArray = IOTestUtil.fitnessListToArrayValues(restoredFitnesses);
					Assert.assertArrayEquals(originalArray, restoredArray);
				});
			} finally {
				// Delete temporary directory
				IOTestUtil.tempDirectoryTeardown(restoredFile);
			}
		});
	}
}
