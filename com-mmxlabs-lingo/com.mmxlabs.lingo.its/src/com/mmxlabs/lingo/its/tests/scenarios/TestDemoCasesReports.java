/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.scenarios;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestFactory;

import com.google.common.collect.Lists;
import com.mmxlabs.lingo.its.tests.AbstractReportTester_LiNGO;
import com.mmxlabs.lingo.its.tests.category.TestCategories;

public class TestDemoCasesReports extends AbstractReportTester_LiNGO {

	@TestFactory
	@Tag(TestCategories.REPORT_TEST)
	public List<DynamicNode> datedScenarioTests() {

		return Lists.newArrayList( //
				DynamicContainer.dynamicContainer("testBonnyProblems_Bonny", makeTests("/scenarios/demo-cases/Bonny problems/0 Bonny.lingo")), //
				DynamicContainer.dynamicContainer("testBonnyProblems_LateAndLost_DES_Backfill", makeTests("/scenarios/demo-cases/Bonny problems/1 late and lost-DES backfill -F- Bonny.lingo")), //
				DynamicContainer.dynamicContainer("testBonnyProblems_O_LateAndLost_DES_Backfill", makeTests("/scenarios/demo-cases/Bonny problems/2 -O- late and lost-DES backfill -F- Bonny.lingo")), //
				DynamicContainer.dynamicContainer("testDryDockIssues_base", makeTests("/scenarios/demo-cases/Dry dock issues/0 base.lingo")), //
				DynamicContainer.dynamicContainer("testDryDockIssues_dryDock55_lateness_removed",
						makeTests("/scenarios/demo-cases/Dry dock issues/3 -O- dry dock 55 -F- lateness removed -F- base.lingo")), //
				DynamicContainer.dynamicContainer("testDryDockIssues_dryDock70", makeTests("/scenarios/demo-cases/Dry dock issues/4 dry dock 70 -O- -F- -F- base.lingo")), //
				DynamicContainer.dynamicContainer("testDryDockIssues_charterInGenerated",
						makeTests("/scenarios/demo-cases/Dry dock issues/5 charter-in generated - rewire for shorter one -O- -F- -F- base.lingo")), //
				DynamicContainer.dynamicContainer("testFleetCostOptimisation_fleet_demo", makeTests("/scenarios/demo-cases/Fleet cost optimisation/0 fleet demo.lingo")), //
				DynamicContainer.dynamicContainer("testFleetCostOptimisation_O_fleet_demo", makeTests("/scenarios/demo-cases/Fleet cost optimisation/1 -O- fleet demo.lingo")), //
				DynamicContainer.dynamicContainer("des-cargo", makeTests("/scenarios/des-cargo.lingo")), //
				DynamicContainer.dynamicContainer("sample-data", makeTests("/scenarios/sample-data.lingo")) //

		);
	}

}
