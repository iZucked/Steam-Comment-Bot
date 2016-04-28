/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.scenarios;

import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.lingo.its.tests.LiNGOTestDataProvider;
import com.mmxlabs.lingo.its.tests.category.OptimisationTest;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;

@RunWith(value = ShiroRunner.class)
public class TestOptimisationResult2 extends AbstractOptimisationResultTester {

	@Test
	@Category(OptimisationTest.class)
	public void testOptimisationResult() throws Exception {

		// Load the scenario to test
		final URL url = getClass().getResource("/scenarios/des-cargo.lingo");
		Assert.assertNotNull(url);

		runScenarioWithGCO(new LiNGOTestDataProvider(url));
	}

}
