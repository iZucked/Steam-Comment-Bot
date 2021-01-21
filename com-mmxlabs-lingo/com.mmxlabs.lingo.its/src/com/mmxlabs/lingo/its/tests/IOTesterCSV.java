/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

import org.eclipse.emf.common.util.EList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.mmxlabs.models.lng.schedule.Fitness;

public class IOTesterCSV {

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

	@ParameterizedTest(name = "{0}")
	@MethodSource("generateTests")
	public void testCSVInOut(final String name, final String scenarioPath) throws Exception {

		final URL url = getClass().getResource(scenarioPath);

		final ITestDataProvider testCaseProvider = new CSVTestDataProvider(url);
		testCaseProvider.execute(testCase -> {
			final EList<Fitness> originalFitnesses = IOTestUtil.scenarioModeltoFitnessList(testCase);
			final long[] originalArray = IOTestUtil.fitnessListToArrayValues(originalFitnesses);
			final File restoredFile = IOTestUtil.exportTestCase(testCase);

			try {

				final URL restoredURL = IOTestUtil.directoryFileToURL(restoredFile);

				final ITestDataProvider restoredCaseProvider = new CSVTestDataProvider(restoredURL);
				restoredCaseProvider.execute(restoredCase -> {
					final EList<Fitness> restoredFitnesses = IOTestUtil.scenarioModeltoFitnessList(restoredCase);
					final long[] restoredArray = IOTestUtil.fitnessListToArrayValues(restoredFitnesses);
					Assertions.assertArrayEquals(originalArray, restoredArray);
				});
			} finally {
				// Delete temporary directory
				IOTestUtil.tempDirectoryTeardown(restoredFile);
			}
		});
	}
}
