/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.lingo.its.tests.category.OptimisationTest;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

/**
 * Generic tests linked to emailed cases
 * 
 */
@RunWith(value = Parameterized.class)
public abstract class AdvancedOptimisationTester extends AbstractAdvancedOptimisationTester {

	public AdvancedOptimisationTester(@Nullable final String _unused_method_prefix_, @NonNull final String scenarioURL, @Nullable final LocalDate periodStart, @Nullable final YearMonth periodEnd) {
		super(_unused_method_prefix_, scenarioURL, periodStart, periodEnd, false);

	}

	public AdvancedOptimisationTester(@Nullable final String _unused_method_prefix_, @NonNull final String scenarioURL, @Nullable final LocalDate periodStart, @Nullable final YearMonth periodEnd,
			boolean runGCO) {
		super(_unused_method_prefix_, scenarioURL, periodStart, periodEnd, runGCO);
	}

	/**
	 * Test the transformed scenario is pretty close to the original - specifically scheduling should be consistent.
	 * 
	 * @throws Exception
	 */
	@Test
	@Category(MicroTest.class)
	public void validatePeriodTransform() throws Exception {
		Assume.assumeTrue(periodStart != null || periodEnd != null);
		final URL url = getClass().getResource(scenarioURL);
		Assert.assertNotNull(url);
		ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(url, (modelReference, scenarioDataProvider) -> {
			final OptimisationPlan optimiserSettings = LNGScenarioRunnerUtils.createExtendedSettings(ScenarioUtils.createDefaultOptimisationPlan());
			if (periodStart != null) {
				optimiserSettings.getUserSettings().setPeriodStartDate(periodStart);
			}
			if (periodEnd != null) {
				optimiserSettings.getUserSettings().setPeriodEnd(periodEnd);
			}
			LNGScenarioRunnerCreator.withEvaluationRunner(scenarioDataProvider, optimiserSettings, runner -> PeriodVerifierUtil.runTest(runner, true));
		});
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Full_NoSimilarity() throws Exception {
		runAdvancedOptimisationTestCase(false, SimilarityMode.OFF, false, false);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Full_LowSimilarity() throws Exception {
		runAdvancedOptimisationTestCase(false, SimilarityMode.LOW, false, false);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Full_MediumSimilarity() throws Exception {
		runAdvancedOptimisationTestCase(false, SimilarityMode.MEDIUM, false, false);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Full_HighSimilarity() throws Exception {
		runAdvancedOptimisationTestCase(false, SimilarityMode.HIGH, false, false);
	}

	@Ignore("Not yet permitted")
	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Full_NoSimilarity_ActionSet() throws Exception {
		runAdvancedOptimisationTestCase(false, SimilarityMode.OFF, true, false);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Full_LowSimilarity_ActionSet() throws Exception {
		runAdvancedOptimisationTestCase(false, SimilarityMode.LOW, true, false);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Full_MediumSimilarity_ActionSet() throws Exception {
		runAdvancedOptimisationTestCase(false, SimilarityMode.MEDIUM, true, false);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Full_HighSimilarity_ActionSet() throws Exception {
		runAdvancedOptimisationTestCase(false, SimilarityMode.HIGH, true, false);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_NoSimilarity() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.OFF, false, false);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_LowSimilarity() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.LOW, false, false);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_MediumSimilarity() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.MEDIUM, false, false);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_HighSimilarity() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.HIGH, false, false);
	}

	@Ignore("Not yet permitted")
	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_NoSimilarity_ActionSet() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.OFF, true, false);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_LowSimilarity_ActionSet() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.LOW, true, false);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_MediumSimilarity_ActionSet() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.MEDIUM, true, false);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_HighSimilarity_ActionSet() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.HIGH, true, false);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Full_NoSimilarity_GCO() throws Exception {
		runAdvancedOptimisationTestCase(false, SimilarityMode.OFF, false, true);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Full_LowSimilarity_GCO() throws Exception {
		runAdvancedOptimisationTestCase(false, SimilarityMode.LOW, false, true);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Full_MediumSimilarity_GCO() throws Exception {
		runAdvancedOptimisationTestCase(false, SimilarityMode.MEDIUM, false, true);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Full_HighSimilarity_GCO() throws Exception {
		runAdvancedOptimisationTestCase(false, SimilarityMode.HIGH, false, true);
	}

	@Ignore("Not yet permitted")
	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Full_NoSimilarity_ActionSet_GCO() throws Exception {
		runAdvancedOptimisationTestCase(false, SimilarityMode.OFF, true, true);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Full_LowSimilarity_ActionSet_GCO() throws Exception {
		runAdvancedOptimisationTestCase(false, SimilarityMode.LOW, true, true);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Full_MediumSimilarity_ActionSet_GCO() throws Exception {
		runAdvancedOptimisationTestCase(false, SimilarityMode.MEDIUM, true, true);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Full_HighSimilarity_ActionSet_GCO() throws Exception {
		runAdvancedOptimisationTestCase(false, SimilarityMode.HIGH, true, true);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_NoSimilarity_GCO() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.OFF, false, true);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_LowSimilarity_GCO() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.LOW, false, true);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_MediumSimilarity_GCO() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.MEDIUM, false, true);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_HighSimilarity_GCO() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.HIGH, false, true);
	}

	@Ignore("Not yet permitted")
	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_NoSimilarity_ActionSet_GCO() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.OFF, true, true);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_LowSimilarity_ActionSet_GCO() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.LOW, true, true);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_MediumSimilarity_ActionSet_GCO() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.MEDIUM, true, true);
	}

	@Test
	@Category(OptimisationTest.class)
	public void advancedOptimisation_Limited_HighSimilarity_ActionSet_GCO() throws Exception {
		runAdvancedOptimisationTestCase(true, SimilarityMode.HIGH, true, true);
	}
}
