/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;

import com.google.common.base.Joiner;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.mmxlabs.common.time.Months;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.editor.util.UserSettingsHelper;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.ui.AbstractRunnerHook;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.models.lng.transformer.util.SequencesSerialiser;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.cache.CacheMode;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

/**
 * Generic tests linked to emailed cases
 * 
 */
public abstract class AbstractAdvancedOptimisationTester extends AbstractOptimisationResultTester {

	// This should only be committed as false to avoid crazy test run times.
	protected static final boolean RUN_FULL_ITERATION_CASES = false;
	// This should only be committed as true.
	protected static final boolean RUN_LIMITED_ITERATION_CASES = true;

	protected @Nullable String scenarioURL;
	protected @Nullable LocalDate periodStart;
	protected @Nullable YearMonth periodEnd;
	protected boolean runGCO;

	public void init(@NonNull final String scenarioURL, @Nullable final LocalDate periodStart, @Nullable final YearMonth periodEnd) {
		init(scenarioURL, periodStart, periodEnd, false);
	}

	public void init(@NonNull final String scenarioURL, @Nullable final LocalDate periodStart, @Nullable final YearMonth periodEnd, final boolean runGCO) {
		this.scenarioURL = scenarioURL;
		this.periodStart = periodStart;
		this.periodEnd = periodEnd;
		this.runGCO = runGCO;
	}

	@SuppressWarnings("unused")
	protected void runAdvancedOptimisationTestCase(final boolean limitedIterations, @NonNull final SimilarityMode mode, final boolean withActionSets, final boolean withGeneratedCharterOuts)
			throws Exception {
		runAdvancedOptimisationTestCase(limitedIterations, mode, withActionSets, withGeneratedCharterOuts, LNGScenarioRunnerCreator.createITSService());
	}

	@SuppressWarnings("unused")
	protected void runAdvancedOptimisationTestCase(final boolean limitedIterations, @NonNull final SimilarityMode mode, final boolean withActionSets, final boolean withGeneratedCharterOuts,
			final IOptimiserInjectorService optimiserInjectorService, String... extraHints) throws Exception {
		Assumptions.assumeTrue(TestingModes.OptimisationTestMode != TestMode.Skip);

		if (withGeneratedCharterOuts) {
			Assumptions.assumeTrue(runGCO);
		}

		if (withActionSets) {
			// Preconditions check - ensure period, otherwise ignore test case
			Assumptions.assumeTrue(periodStart != null);
			Assumptions.assumeTrue(periodEnd != null);

			// Should match OptimisationHelper (Repeated null checks for null analysis code)
			if (periodStart != null && periodEnd != null) {
				Assumptions.assumeTrue(Months.between(periodStart, periodEnd) <= 6);
			}
			if (periodStart != null && periodEnd != null) {
				Assumptions.assumeTrue(Months.between(periodStart, periodEnd) < 3 || mode != SimilarityMode.LOW);
			}
		}

		// Only run full iterations if the flag is set
		if (limitedIterations) {
			Assumptions.assumeTrue(RUN_LIMITED_ITERATION_CASES);
		} else {
			Assumptions.assumeTrue(RUN_FULL_ITERATION_CASES);
		}
		// Load the scenario to test
		final URL url = getClass().getResource(scenarioURL);
		Assertions.assertNotNull(url);

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

		final LiNGOTestDataProvider provider = new LiNGOTestDataProvider(url) {
			@Override
			public File getFitnessDataAsFile() throws java.io.IOException, java.net.URISyntaxException {
				final URL fileURL = FileLocator.toFileURL(url);
				final URL propertiesFile = new URL(fileURL.toString().replaceAll(" ", "%20") + String.format(".%s.properties", Joiner.on(".").join(components)));
				return new File(propertiesFile.toURI());
			}

			@Override
			public URL getFitnessDataAsURL() throws java.io.IOException {
				return new URL(String.format("%s.%s.properties", url.toString(), Joiner.on(".").join(components)));
			}
		};
		provider.execute(originalScenarioDataProvider -> {

			final UserSettings userSettings = UserSettingsHelper.createDefaultUserSettings();

			if (periodStart != null) {
				userSettings.setPeriodStartDate(periodStart);
			}
			if (periodEnd != null) {
				userSettings.setPeriodEnd(periodEnd);
			}

			userSettings.setBuildActionSets(withActionSets);
			userSettings.setGenerateCharterOuts(withGeneratedCharterOuts);
			userSettings.setShippingOnly(false);
			userSettings.setSimilarityMode(mode);

			final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, originalScenarioDataProvider.getTypedScenario(LNGScenarioModel.class));
			Assertions.assertNotNull(optimisationPlan);

			if (limitedIterations) {
				// Limit for quick optimisation
				// LSO Limit
				ScenarioUtils.setLSOStageIterations(optimisationPlan, 15_000);
				// Hill climb limit
				ScenarioUtils.setHillClimbStageIterations(optimisationPlan, 1_000);

			}
			final UserSettings planUserSettings = optimisationPlan.getUserSettings();
			Assertions.assertEquals(withActionSets, planUserSettings.isBuildActionSets());
			Assertions.assertEquals(withGeneratedCharterOuts, planUserSettings.isGenerateCharterOuts());
			Assertions.assertFalse(planUserSettings.isShippingOnly());
			Assertions.assertEquals(periodStart, planUserSettings.getPeriodStartDate());
			Assertions.assertEquals(periodEnd, planUserSettings.getPeriodEnd());

			// scenarioRunner.initAndEval();

			final IRunnerHook runnerHook;
			if (false) {
				runnerHook = new AbstractRunnerHook() {

					@Override
					public void doReportSequences(final String phase, final ISequences rawSequences, final LNGDataTransformer dataTransformer) {
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
					public ISequences doGetPrestoredSequences(final String stage, final LNGDataTransformer dataTransformer) {
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

					private void save(final ISequences rawSequences, final String type, final Injector injector) {
						try {
							final String suffix = Joiner.on(".").join(components) + "." + type + ".sequences";
							final URL expectedReportOutput = new URL(FileLocator.toFileURL(url).toString().replaceAll(" ", "%20") + suffix);
							final File file2 = new File(expectedReportOutput.toURI());
							try (FileOutputStream fos = new FileOutputStream(file2)) {
								Assertions.assertNotNull(injector);
								SequencesSerialiser.save(injector.getInstance(IOptimisationData.class), rawSequences, fos);
							}
						} catch (final Exception e) {
							Assertions.fail(e.getMessage());
						}
					}

					private ISequences load(final String type, final Injector injector) {
						try {
							final String suffix = Joiner.on(".").join(components) + "." + type + ".sequences";
							final URL expectedReportOutput = new URL(FileLocator.toFileURL(url).toString().replaceAll(" ", "%20") + suffix);
							final File file2 = new File(expectedReportOutput.toURI());
							try (FileInputStream fos = new FileInputStream(file2)) {
								Assertions.assertNotNull(injector);
								return SequencesSerialiser.load(injector.getInstance(IOptimisationData.class), fos);
							}
						} catch (final Exception e) {
							// return
							// Assertions.fail(e.getMessage());
						}
						return null;
					}

					private void verify(final String type, final ISequences actual, final Injector injector) {
						final ISequences expected = load(type, injector);
						if (actual != null && expected != null) {
							Assertions.assertEquals(expected, actual);
						}
					}
				};
			} else {
				runnerHook = null;
			}
			LNGScenarioRunnerCreator.withOptimisationRunner(originalScenarioDataProvider, optimisationPlan, scenarioRunner -> {

				// After initial evaluation, save, reload and compare models.
				Assertions.assertTrue(TesterUtil.validateReloadedState(originalScenarioDataProvider), "Validate reloaded model is identical");
				if (runnerHook != null) {
					scenarioRunner.setRunnerHook(runnerHook);
				}
				optimiseBasicScenario(scenarioRunner, provider);

			}, optimiserInjectorService, extraHints);
		});
	}

	@NonNull
	public static IOptimiserInjectorService withCacheSettings(final CacheMode voyageEvaluatorCache, final CacheMode timeWindowCache, final CacheMode pnlTrimmerCache) {

		return new IOptimiserInjectorService() {

			@Override
			@Nullable
			public Module requestModule(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
				return null;
			}

			@Override
			@Nullable
			public List<@NonNull Module> requestModuleOverrides(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
				if (moduleType == ModuleType.Module_LNGTransformerModule) {
					return Collections.<@NonNull Module> singletonList(new AbstractModule() {

						@Override
						protected void configure() {
							// Default bindings for caches
							bind(CacheMode.class).annotatedWith(Names.named(SchedulerConstants.Key_VoyagePlanEvaluatorCache)).toInstance(voyageEvaluatorCache);
							bind(CacheMode.class).annotatedWith(Names.named(SchedulerConstants.Key_TimeWindowSchedulerCache)).toInstance(timeWindowCache);
							bind(CacheMode.class).annotatedWith(Names.named(SchedulerConstants.Key_PNLBasedWindowTrimmerCache)).toInstance(pnlTrimmerCache);
						}
					});
				}
				if (moduleType == ModuleType.Module_EvaluationParametersModule) {
					return Collections.<@NonNull Module> singletonList(new AbstractModule() {

						@Override
						protected void configure() {
							// Nothing needed here
						}

						@Provides
						@Named(LNGParameters_EvaluationSettingsModule.OPTIMISER_REEVALUATE)
						private boolean isOptimiserReevaluating() {
							return false;
						}
					});
				}
				return null;
			}
		};

	}
}
