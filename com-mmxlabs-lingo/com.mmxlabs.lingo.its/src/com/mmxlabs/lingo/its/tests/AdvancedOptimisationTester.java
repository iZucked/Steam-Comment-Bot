/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import com.mmxlabs.lingo.its.tests.category.OptimisationTest;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.AbstractRunnerHook;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
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

	public AdvancedOptimisationTester(@Nullable final String _unused_method_prefix_, @NonNull final String scenarioURL, @Nullable final YearMonth periodStart, @Nullable final YearMonth periodEnd) {
		this.scenarioURL = scenarioURL;
		this.periodStart = periodStart;
		this.periodEnd = periodEnd;

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

	private void runAdvancedOptimisationTestCase(final boolean limitedIterations, @NonNull final SimilarityMode mode, final boolean withActionSets, final boolean withGeneratedCharterOuts)
			throws Exception {

		if (withActionSets) {
			// Preconditions check - ensure period, otherwise ignore test case
			Assume.assumeTrue(periodStart != null);
			Assume.assumeTrue(periodEnd != null);
		}

		// Only run full iterations if the flag is set
		if (limitedIterations) {
			Assume.assumeTrue(RUN_LIMITED_ITERATION_CASES);
		} else {
			Assume.assumeTrue(RUN_FULL_ITERATION_CASES);
		}
		// Load the scenario to test
		final URL url = getClass().getResource(scenarioURL);
		Assert.assertNotNull(url);

		final LNGScenarioModel originalScenario = LNGScenarioRunnerCreator.getScenarioModelFromURL(url);

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

		final OptimiserSettings optimiserSettings = OptimisationHelper.transformUserSettings(userSettings, null, originalScenario);
		Assert.assertNotNull(optimiserSettings);

		if (limitedIterations) {
			// Limit for quick optimisation
			// LSO Limit
			optimiserSettings.getAnnealingSettings().setIterations(10_000);
			// Hill climb limit
			optimiserSettings.getSolutionImprovementSettings().setIterations(1_000);
		}

		Assert.assertEquals(withActionSets, optimiserSettings.isBuildActionSets());
		Assert.assertEquals(withGeneratedCharterOuts, optimiserSettings.isGenerateCharterOuts());
		Assert.assertFalse(optimiserSettings.isShippingOnly());
		Assert.assertEquals(periodStart, optimiserSettings.getRange().getOptimiseAfter());
		Assert.assertEquals(periodEnd, optimiserSettings.getRange().getOptimiseBefore());

		// scenarioRunner.initAndEval();

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

		// Optionally use pre-stored sequences state.
		final IRunnerHook runnerHook;
		if (false) {
			runnerHook = new AbstractRunnerHook() {

				@Override
				public void reportSequences(String phase, final ISequences rawSequences) {
					switch (phase) {

					case IRunnerHook.PHASE_LSO:
					case IRunnerHook.PHASE_HILL:
					case IRunnerHook.PHASE_INITIAL:
						verify(phase, rawSequences);
						break;
					case IRunnerHook.PHASE_ACTION_SETS:
						break;
					}
				}

				@Override
				public ISequences getPrestoredSequences(String phase) {
					switch (phase) {
					case IRunnerHook.PHASE_LSO:
					case IRunnerHook.PHASE_HILL:
						// return load(phase);
					case IRunnerHook.PHASE_INITIAL:
					case IRunnerHook.PHASE_ACTION_SETS:
						break;

					}
					return null;
				}

				private void save(final ISequences rawSequences, final String type) {
					try {
						final String suffix = Joiner.on(".").join(components) + "." + type + ".sequences";
						final URL expectedReportOutput = new URL(FileLocator.toFileURL(url).toString().replaceAll(" ", "%20") + suffix);
						final File file2 = new File(expectedReportOutput.toURI());
						try (FileOutputStream fos = new FileOutputStream(file2)) {
							// final Injector injector = scenarioRunner.getInjector();
							Assert.assertNotNull(injector);
							SequencesSerialiser.save(injector.getInstance(IOptimisationData.class), rawSequences, fos);
						}
					} catch (final Exception e) {
						Assert.fail(e.getMessage());
					}
				}

				private ISequences load(final String type) {
					try {
						final String suffix = Joiner.on(".").join(components) + "." + type + ".sequences";
						final URL expectedReportOutput = new URL(FileLocator.toFileURL(url).toString().replaceAll(" ", "%20") + suffix);
						final File file2 = new File(expectedReportOutput.toURI());
						try (FileInputStream fos = new FileInputStream(file2)) {
							// final Injector injector = scenarioRunner.getInjector();
							Assert.assertNotNull(injector);
							return SequencesSerialiser.load(injector.getInstance(IOptimisationData.class), fos);
						}
					} catch (final Exception e) {
						// return
						// Assert.fail(e.getMessage());
					}
					return null;
				}

				private void verify(final String type, final ISequences actual) {
					ISequences expected = load(type);
					if (actual != null && expected != null) {
						Assert.assertEquals(expected, actual);
					}
				}
			};
		} else {
			runnerHook = null;
		}

		final LNGScenarioRunner scenarioRunner = LNGScenarioRunnerCreator.createScenarioRunnerWithLSO(executorService, originalScenario, optimiserSettings);
		if (runnerHook != null) {
			scenarioRunner.setRunnerHook(runnerHook);
		}
	}

}
