/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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

public class IOTesterLiNGO {

	public static Iterable<Object[]> generateTests() {
		return Arrays.asList(new Object[][] { { "testBonnyProblems_Bonny", "/scenarios/demo-cases/Bonny problems/0 Bonny.lingo" },
				{ "testBonnyProblems_LateAndLost_DES_Backfill", "/scenarios/demo-cases/Bonny problems/1 late and lost-DES backfill -F- Bonny.lingo" },
				{ "testBonnyProblems_O_LateAndLost_DES_Backfill", "/scenarios/demo-cases/Bonny problems/2 -O- late and lost-DES backfill -F- Bonny.lingo" },
				{ "testDryDockIssues_base", "/scenarios/demo-cases/Dry dock issues/0 base.lingo" },
				{ "testDryDockIssues_dryDock55_lateness_removed", "/scenarios/demo-cases/Dry dock issues/3 -O- dry dock 55 -F- lateness removed -F- base.lingo" },
				{ "testDryDockIssues_dryDock70", "/scenarios/demo-cases/Dry dock issues/4 dry dock 70 -O- -F- -F- base.lingo" },
				{ "testDryDockIssues_charterInGenerated", "/scenarios/demo-cases/Dry dock issues/5 charter-in generated - rewire for shorter one -O- -F- -F- base.lingo" },
				{ "testFleetCostOptimisation_fleet_demo", "/scenarios/demo-cases/Fleet cost optimisation/0 fleet demo.lingo" },
				{ "testFleetCostOptimisation_O_fleet_demo", "/scenarios/demo-cases/Fleet cost optimisation/1 -O- fleet demo.lingo" }, { "des-cargo", "/scenarios/des-cargo.lingo" },
				{ "sample-data", "/scenarios/sample-data.lingo" },

		});
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("generateTests")
	public void testLiNGOInOut(String name, String scenarioPath) throws Exception {

		final URL url = getClass().getResource(scenarioPath);

		final ITestDataProvider testCaseProvider = new LiNGOTestDataProvider(url);
		testCaseProvider.execute(testCase -> {

			EList<Fitness> originalFitnesses = IOTestUtil.scenarioModeltoFitnessList(testCase);
			long[] originalArray = IOTestUtil.fitnessListToArrayValues(originalFitnesses);
			File restoredFile = IOTestUtil.exportTestCase(testCase);

			try {

				URL restoredURL = IOTestUtil.directoryFileToURL(restoredFile);
				final ITestDataProvider restoredCaseProvider = new CSVTestDataProvider(restoredURL);
				restoredCaseProvider.execute(restoredCase -> {
					EList<Fitness> restoredFitnesses = IOTestUtil.scenarioModeltoFitnessList(restoredCase);
					long[] restoredArray = IOTestUtil.fitnessListToArrayValues(restoredFitnesses);
					Assertions.assertArrayEquals(originalArray, restoredArray);
				});
			} finally {
				// Delete temporary directory
				IOTestUtil.tempDirectoryTeardown(restoredFile);
			}
		});
	}
}
