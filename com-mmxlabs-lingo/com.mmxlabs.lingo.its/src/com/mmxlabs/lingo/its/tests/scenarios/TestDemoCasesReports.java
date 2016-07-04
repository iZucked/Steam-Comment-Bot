/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.scenarios;

import java.util.Arrays;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.mmxlabs.lingo.its.tests.AbstractReportTester_LiNGO;

@RunWith(value = Parameterized.class)
public class TestDemoCasesReports extends AbstractReportTester_LiNGO {

	public TestDemoCasesReports(String name, String scenario) throws Exception {
		super(name, scenario);
	}

	@Parameters(name = "{0}")
	public static Iterable<Object[]> generateTests() {
		return Arrays.asList(new Object[][] { { "testBonnyProblems_Bonny", "/scenarios/demo-cases/Bonny problems/0 Bonny.lingo" },
				{ "testBonnyProblems_LateAndLost_DES_Backfill", "/scenarios/demo-cases/Bonny problems/1 late and lost-DES backfill -F- Bonny.lingo" },
				{ "testBonnyProblems_O_LateAndLost_DES_Backfill", "/scenarios/demo-cases/Bonny problems/2 -O- late and lost-DES backfill -F- Bonny.lingo" },
				{ "testDryDockIssues_base", "/scenarios/demo-cases/Dry dock issues/0 base.lingo" },
				{ "testDryDockIssues_dryDock55_lateness_removed", "/scenarios/demo-cases/Dry dock issues/3 -O- dry dock 55 -F- lateness removed -F- base.lingo" },
				{ "testDryDockIssues_dryDock70", "/scenarios/demo-cases/Dry dock issues/4 dry dock 70 -O- -F- -F- base.lingo" },
				{ "testDryDockIssues_charterInGenerated", "/scenarios/demo-cases/Dry dock issues/5 charter-in generated - rewire for shorter one -O- -F- -F- base.lingo" },
				{ "testDryDockIssues_dryDock70", "/scenarios/demo-cases/Dry dock issues/4 dry dock 70 -O- -F- -F- base.lingo" },
				{ "testFleetCostOptimisation_fleet_demo", "/scenarios/demo-cases/Fleet cost optimisation/0 fleet demo.lingo" },
				{ "testFleetCostOptimisation_O_fleet_demo", "/scenarios/demo-cases/Fleet cost optimisation/1 -O- fleet demo.lingo" }, { "des-cargo", "/scenarios/des-cargo.lingo" },
				{ "sample-data", "/scenarios/sample-data.lingo" },

		});
	}

}
