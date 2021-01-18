/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.scenarios;

import java.net.URL;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.lingo.its.tests.LiNGOTestDataProvider;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;

@ExtendWith(ShiroRunner.class)
public class TestOptimisationResult extends AbstractOptimisationResultTester {

	@Test
	@Tag(TestCategories.OPTIMISATION_TEST)
	public void testOptimisationResult1() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/sample-data.lingo");

		runScenarioWithGCO(new LiNGOTestDataProvider(url));
	}
	
	@Test
	@Tag(TestCategories.OPTIMISATION_TEST)
	public void testOptimisationResult2() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/des-cargo.lingo");
		Assertions.assertNotNull(url);

		runScenarioWithGCO(new LiNGOTestDataProvider(url));
	}
}
