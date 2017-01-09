/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.common.base.Joiner;
import com.google.inject.Injector;
import com.mmxlabs.common.time.Months;
import com.mmxlabs.lingo.its.tests.category.OptimisationTest;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.AbstractRunnerHook;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.models.lng.transformer.util.SequencesSerialiser;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * Generic tests linked to emailed cases
 * 
 */
@RunWith(value = Parameterized.class)
public abstract class AdvancedOptimisationTester extends AbstractOptimisationResultTester {

	// This should only be committed as false to avoid crazy test run times.
	private static final boolean RUN_FULL_ITERATION_CASES = false;
	// This should only be committed as true.
	private static final boolean RUN_LIMITED_ITERATION_CASES = true;

	private @NonNull final String scenarioURL;
	private @Nullable final YearMonth periodStart;
	private @Nullable final YearMonth periodEnd;
	private final boolean runGCO;

	public AdvancedOptimisationTester(@Nullable final String _unused_method_prefix_, @NonNull final String scenarioURL, @Nullable final YearMonth periodStart, @Nullable final YearMonth periodEnd) {
		this(_unused_method_prefix_, scenarioURL, periodStart, periodEnd, false);

	}

	public AdvancedOptimisationTester(@Nullable final String _unused_method_prefix_, @NonNull final String scenarioURL, @Nullable final YearMonth periodStart, @Nullable final YearMonth periodEnd,
			boolean runGCO) {
		this.scenarioURL = scenarioURL;
		this.periodStart = periodStart;
		this.periodEnd = periodEnd;
		this.runGCO = runGCO;
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

	@SuppressWarnings("unused")
	private void runAdvancedOptimisationTestCase(final boolean limitedIterations, @NonNull final SimilarityMode mode, final boolean withActionSets, final boolean withGeneratedCharterOuts)
			throws Exception {

		if (withGeneratedCharterOuts) {
			Assume.assumeTrue(runGCO);
		}

		if (withActionSets) {
			// Preconditions check - ensure period, otherwise ignore test case
			Assume.assumeTrue(periodStart != null);
			Assume.assumeTrue(periodEnd != null);

			// Should match OptimisationHelper (Repeated null checks for null analysis code)
			if (periodStart != null && periodEnd != null) {
				Assume.assumeTrue(Months.between(periodStart, periodEnd) <= 6);
			}
			if (periodStart != null && periodEnd != null) {
				Assume.assumeTrue(Months.between(periodStart, periodEnd) < 3 || mode != SimilarityMode.LOW);
			}
		}

		// Only run full iterations if the flag is set
		if (limitedIterations)

		{
			Assume.assumeTrue(RUN_LIMITED_ITERATION_CASES);
		} else {
			Assume.assumeTrue(RUN_FULL_ITERATION_CASES);
		}
		// Load the scenario to test
		final URL url = getClass().getResource(scenarioURL);
		Assert.assertNotNull(url);

		final List<String> components = new LinkedList<>();
		if (!limitedIterations) {
			components.add("full-iters");
		}
		components.add(String.format("similarity-%s", mode.toString()));
		if (withActionSets) {
			components.add("actionset");
		}
		if (withGeneratedCharterOuts) {
			components.add("gco");
		}
		// if (withShippingOnly) {
		// components.add("shipping");
		// }

		LiNGOTestDataProvider provider = new LiNGOTestDataProvider(url) {
			@Override
			public File getFitnessDataAsFile() throws java.io.IOException, java.net.URISyntaxException {
				URL fileURL = FileLocator.toFileURL(url);
				final URL propertiesFile = new URL(fileURL.toString().replaceAll(" ", "%20") + String.format(".%s.properties", Joiner.on(".").join(components)));
				return new File(propertiesFile.toURI());
			};

			@Override
			public URL getFitnessDataAsURL() throws java.io.IOException {
				final URL propertiesFile = new URL(url.toString() + String.format(".%s.properties", Joiner.on(".").join(components)));
				return propertiesFile;
			};
		};
		provider.execute(originalScenario -> {

			final UserSettings userSettings = ScenarioUtils.createDefaultUserSettings();

			if (periodStart != null) {
				userSettings.setPeriodStart(periodStart);
			}
			if (periodEnd != null) {
				userSettings.setPeriodEnd(periodEnd);
			}

			userSettings.setBuildActionSets(withActionSets);
			userSettings.setGenerateCharterOuts(withGeneratedCharterOuts);
			userSettings.setShippingOnly(false);
			userSettings.setSimilarityMode(mode);

			final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, originalScenario);
			Assert.assertNotNull(optimisationPlan);

			if (limitedIterations) {
				// Limit for quick optimisation
				// LSO Limit
				ScenarioUtils.setLSOStageIterations(optimisationPlan, 15_000);
				// Hill climb limit
				ScenarioUtils.setHillClimbStageIterations(optimisationPlan, 1_000);

			}
			UserSettings planUserSettings = optimisationPlan.getUserSettings();
			Assert.assertEquals(withActionSets, planUserSettings.isBuildActionSets());
			Assert.assertEquals(withGeneratedCharterOuts, planUserSettings.isGenerateCharterOuts());
			Assert.assertFalse(planUserSettings.isShippingOnly());
			Assert.assertEquals(periodStart, planUserSettings.getPeriodStart());
			Assert.assertEquals(periodEnd, planUserSettings.getPeriodEnd());

			// scenarioRunner.initAndEval();

			final IRunnerHook runnerHook;
			if (false) {
				runnerHook = new AbstractRunnerHook() {

					@Override
					public void doReportSequences(String phase, final ISequences rawSequences, LNGDataTransformer dataTransformer) {
						switch (phase) {

						case IRunnerHook.STAGE_LSO:
						case IRunnerHook.STAGE_HILL:
						case IRunnerHook.STAGE_INITIAL:
							verify(phase, rawSequences, dataTransformer.getInjector());
							break;
						case IRunnerHook.STAGE_ACTION_SETS:
							break;
						}
					}

					@Override
					public ISequences doGetPrestoredSequences(String stage, LNGDataTransformer dataTransformer) {
						switch (stage) {
						case IRunnerHook.STAGE_LSO:
						case IRunnerHook.STAGE_HILL:
							// return load(phase);
						case IRunnerHook.STAGE_INITIAL:
						case IRunnerHook.STAGE_ACTION_SETS:
							break;

						}
						return null;
					}

					private void save(final ISequences rawSequences, final String type, Injector injector) {
						try {
							final String suffix = Joiner.on(".").join(components) + "." + type + ".sequences";
							final URL expectedReportOutput = new URL(FileLocator.toFileURL(url).toString().replaceAll(" ", "%20") + suffix);
							final File file2 = new File(expectedReportOutput.toURI());
							try (FileOutputStream fos = new FileOutputStream(file2)) {
								Assert.assertNotNull(injector);
								SequencesSerialiser.save(injector.getInstance(IOptimisationData.class), rawSequences, fos);
							}
						} catch (final Exception e) {
							Assert.fail(e.getMessage());
						}
					}

					private ISequences load(final String type, Injector injector) {
						try {
							final String suffix = Joiner.on(".").join(components) + "." + type + ".sequences";
							final URL expectedReportOutput = new URL(FileLocator.toFileURL(url).toString().replaceAll(" ", "%20") + suffix);
							final File file2 = new File(expectedReportOutput.toURI());
							try (FileInputStream fos = new FileInputStream(file2)) {
								Assert.assertNotNull(injector);
								return SequencesSerialiser.load(injector.getInstance(IOptimisationData.class), fos);
							}
						} catch (final Exception e) {
							// return
							// Assert.fail(e.getMessage());
						}
						return null;
					}

					private void verify(final String type, final ISequences actual, Injector injector) {
						ISequences expected = load(type, injector);
						if (actual != null && expected != null) {
							Assert.assertEquals(expected, actual);
						}
					}
				};
			} else {
				runnerHook = null;
			}
			LNGScenarioRunnerCreator.withOptimisationRunner(originalScenario, optimisationPlan, scenarioRunner -> {

				// After initial evaluation, save, reload and compare models.
				Assert.assertTrue("Validate reloaded model is identical", TesterUtil.validateReloadedState(originalScenario));
				if (runnerHook != null) {
					scenarioRunner.setRunnerHook(runnerHook);
				}
				optimiseBasicScenario(scenarioRunner, provider);

			});
		});
	}

}
