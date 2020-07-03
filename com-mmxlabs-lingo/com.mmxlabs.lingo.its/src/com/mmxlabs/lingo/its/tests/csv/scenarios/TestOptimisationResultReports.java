/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.csv.scenarios;

import java.util.List;

import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestFactory;

import com.google.common.collect.Lists;
import com.mmxlabs.lingo.its.tests.AbstractReportTester_CSV;
import com.mmxlabs.lingo.its.tests.category.TestCategories;

public class TestOptimisationResultReports extends AbstractReportTester_CSV {

	@TestFactory
	@Tag(TestCategories.REPORT_TEST)
	public List<DynamicNode> datedScenarioTests() {

		return Lists.newArrayList( //
				DynamicContainer.dynamicContainer("sample-data", makeTests("/scenarios/sample-data/")), //
				DynamicContainer.dynamicContainer("des-cargo", makeTests("/scenarios/des-cargo/")) //
		);
	}

}
