/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;

import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

/**
 * Generic tests linked to emailed cases
 * 
 */
public abstract class AdvancedOptimisationTester extends AbstractAdvancedOptimisationTester {

	// No other valid mode at present. All is not supported in this test framework,
	private static final SimilarityMode[] SIMILARITY_MODES = { SimilarityMode.OFF }; // , SimilarityMode.ALL };

	public List<DynamicNode> makeTests(@NonNull final String scenarioURL, @Nullable final LocalDate periodStart, @Nullable final YearMonth periodEnd, boolean withGCO) {
		final List<DynamicNode> tests = new LinkedList<>();

		for (final boolean withLimited : BOOLS) {

			if (withLimited && !RUN_LIMITED_ITERATION_CASES) {
				continue;
			} else if (!withLimited && !RUN_FULL_ITERATION_CASES) {
				continue;
			}

			final String limitedLabel = withLimited ? "Limited_" : "";
			final String gcoLabel = withGCO ? "GCO_" : "";
			for (final boolean withActionSets : BOOLS) {
				// Check period for ITS.
				if (withActionSets && (periodStart == null || periodEnd == null)) {
					continue;
				}
				final String actionLabel = withActionSets ? "_ActionSets" : "";
				for (final SimilarityMode similarityMode : SIMILARITY_MODES) {
					if (similarityMode == SimilarityMode.ALL) {
						continue;
					}
					final String label = String.format("%s%s_Similarity%s%s", limitedLabel, gcoLabel, similarityMode, actionLabel);
					tests.add(DynamicTest.dynamicTest(label, () -> {
						Assumptions.assumeTrue(TestingModes.OptimisationTestMode != TestMode.Skip);
						// // No tests defined yet...
						// Assumptions.assumeFalse(similarityMode == SimilarityMode.ALL);
						init(scenarioURL, periodStart, periodEnd, withGCO);
						runAdvancedOptimisationTestCase(withLimited, similarityMode, withActionSets, withGCO);
					}));
				}
			}
		}
		if (periodStart != null || periodEnd != null) {

			tests.add(DynamicTest.dynamicTest("validatePeriodTransform", () -> {
				Assumptions.assumeTrue(TestingModes.OptimisationTestMode != TestMode.Skip);
				Assumptions.assumeTrue(periodStart != null || periodEnd != null);
				// init(scenarioURL, periodStart, periodEnd, gco);

				final URL url = getClass().getResource(scenarioURL);
				Assertions.assertNotNull(url);
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
			}));
		}

		return tests;
	}
	//
	// /**
	// * Test the transformed scenario is pretty close to the original - specifically scheduling should be consistent.
	// *
	// * @throws Exception
	// */
	// @Test
	// @Tag(TestCategories.MICRO_TEST)
	// public void validatePeriodTransform(@NonNull final String _unused_method_prefix, @NonNull final String scenarioURL, @Nullable final LocalDate periodStart, @Nullable final YearMonth periodEnd,
	// boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// Assumptions.assumeTrue(periodStart != null || periodEnd != null);
	// final URL url = getClass().getResource(scenarioURL);
	// Assertions.assertNotNull(url);
	// ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(url, (modelReference, scenarioDataProvider) -> {
	// final OptimisationPlan optimiserSettings = LNGScenarioRunnerUtils.createExtendedSettings(ScenarioUtils.createDefaultOptimisationPlan());
	// if (periodStart != null) {
	// optimiserSettings.getUserSettings().setPeriodStartDate(periodStart);
	// }
	// if (periodEnd != null) {
	// optimiserSettings.getUserSettings().setPeriodEnd(periodEnd);
	// }
	// LNGScenarioRunnerCreator.withEvaluationRunner(scenarioDataProvider, optimiserSettings, runner -> PeriodVerifierUtil.runTest(runner, true));
	// });
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Full_NoSimilarity(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart, @Nullable YearMonth periodEnd,
	// boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(false, SimilarityMode.OFF, false, false);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Full_LowSimilarity(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart, @Nullable YearMonth periodEnd,
	// boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(false, SimilarityMode.LOW, false, false);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Full_MediumSimilarity(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart, @Nullable YearMonth periodEnd,
	// boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(false, SimilarityMode.MEDIUM, false, false);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Full_HighSimilarity(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart, @Nullable YearMonth periodEnd,
	// boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(false, SimilarityMode.HIGH, false, false);
	// }
	//
	// @Disabled("Not yet permitted")
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Full_NoSimilarity_ActionSet(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart, @Nullable YearMonth
	// periodEnd,
	// boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(false, SimilarityMode.OFF, true, false);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Full_LowSimilarity_ActionSet(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart, @Nullable YearMonth
	// periodEnd,
	// boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(false, SimilarityMode.LOW, true, false);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Full_MediumSimilarity_ActionSet(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart,
	// @Nullable YearMonth periodEnd, boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(false, SimilarityMode.MEDIUM, true, false);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Full_HighSimilarity_ActionSet(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart, @Nullable YearMonth
	// periodEnd,
	// boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(false, SimilarityMode.HIGH, true, false);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Limited_NoSimilarity(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart, @Nullable YearMonth periodEnd,
	// boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(true, SimilarityMode.OFF, false, false);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Limited_LowSimilarity(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart, @Nullable YearMonth periodEnd,
	// boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(true, SimilarityMode.LOW, false, false);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Limited_MediumSimilarity(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart, @Nullable YearMonth periodEnd,
	// boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(true, SimilarityMode.MEDIUM, false, false);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Limited_HighSimilarity(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart, @Nullable YearMonth periodEnd,
	// boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(true, SimilarityMode.HIGH, false, false);
	// }
	//
	// @Disabled("Not yet permitted")
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Limited_NoSimilarity_ActionSet(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart,
	// @Nullable YearMonth periodEnd, boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(true, SimilarityMode.OFF, true, false);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Limited_LowSimilarity_ActionSet(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart,
	// @Nullable YearMonth periodEnd, boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(true, SimilarityMode.LOW, true, false);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Limited_MediumSimilarity_ActionSet(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart,
	// @Nullable YearMonth periodEnd, boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(true, SimilarityMode.MEDIUM, true, false);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Limited_HighSimilarity_ActionSet(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart,
	// @Nullable YearMonth periodEnd, boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(true, SimilarityMode.HIGH, true, false);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Full_NoSimilarity_GCO(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart, @Nullable YearMonth periodEnd,
	// boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(false, SimilarityMode.OFF, false, true);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Full_LowSimilarity_GCO(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart, @Nullable YearMonth periodEnd,
	// boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(false, SimilarityMode.LOW, false, true);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Full_MediumSimilarity_GCO(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart, @Nullable YearMonth periodEnd,
	// boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(false, SimilarityMode.MEDIUM, false, true);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Full_HighSimilarity_GCO(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart, @Nullable YearMonth periodEnd,
	// boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(false, SimilarityMode.HIGH, false, true);
	// }
	//
	// @Disabled("Not yet permitted")
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Full_NoSimilarity_ActionSet_GCO(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart,
	// @Nullable YearMonth periodEnd, boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(false, SimilarityMode.OFF, true, true);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Full_LowSimilarity_ActionSet_GCO(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart,
	// @Nullable YearMonth periodEnd, boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(false, SimilarityMode.LOW, true, true);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Full_MediumSimilarity_ActionSet_GCO(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart,
	// @Nullable YearMonth periodEnd, boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(false, SimilarityMode.MEDIUM, true, true);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Full_HighSimilarity_ActionSet_GCO(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart,
	// @Nullable YearMonth periodEnd, boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(false, SimilarityMode.HIGH, true, true);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Limited_NoSimilarity_GCO(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart, @Nullable YearMonth periodEnd,
	// boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(true, SimilarityMode.OFF, false, true);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Limited_LowSimilarity_GCO(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart, @Nullable YearMonth periodEnd,
	// boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	//
	// runAdvancedOptimisationTestCase(true, SimilarityMode.LOW, false, true);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Limited_MediumSimilarity_GCO(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart, @Nullable YearMonth
	// periodEnd,
	// boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	// runAdvancedOptimisationTestCase(true, SimilarityMode.MEDIUM, false, true);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Limited_HighSimilarity_GCO(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart, @Nullable YearMonth periodEnd,
	// boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	// runAdvancedOptimisationTestCase(true, SimilarityMode.HIGH, false, true);
	// }
	//
	// @Disabled("Not yet permitted")
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Limited_NoSimilarity_ActionSet_GCO(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart,
	// @Nullable YearMonth periodEnd, boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	// runAdvancedOptimisationTestCase(true, SimilarityMode.OFF, true, true);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Limited_LowSimilarity_ActionSet_GCO(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart,
	// @Nullable YearMonth periodEnd, boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	// runAdvancedOptimisationTestCase(true, SimilarityMode.LOW, true, true);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Limited_MediumSimilarity_ActionSet_GCO(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart,
	// @Nullable YearMonth periodEnd, boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	// runAdvancedOptimisationTestCase(true, SimilarityMode.MEDIUM, true, true);
	// }
	//
	// @Test
	// @Tag(TestCategories.OPTIMISATION_TEST)
	// public void advancedOptimisation_Limited_HighSimilarity_ActionSet_GCO(@Nullable String _unused_method_prefix_, @NonNull String scenarioURL, @Nullable LocalDate periodStart,
	// @Nullable YearMonth periodEnd, boolean gco) throws Exception {
	// init(scenarioURL, periodStart, periodEnd, gco);
	// runAdvancedOptimisationTestCase(true, SimilarityMode.HIGH, true, true);
	// }
}
