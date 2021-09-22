/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.mmxlabs.common.concurrent.DefaultJobExecutorFactory;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.common.util.CheckedConsumer;
import com.mmxlabs.common.util.CheckedFunction;
import com.mmxlabs.models.lng.migration.ModelsLNGVersionMaker;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.manager.SimpleScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.cache.CacheMode;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class LNGScenarioRunnerCreator {

	public static void withLiNGOFileEvaluationRunner(@NonNull final URL url, @NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, Exception> consumer) throws Exception {

		ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(url, (modelRecord, scenarioDataProvider) -> {
			final OptimisationPlan optimiserSettings = LNGScenarioRunnerUtils.createExtendedSettings(ScenarioUtils.createDefaultOptimisationPlan());
			withEvaluationRunner(scenarioDataProvider, optimiserSettings, consumer);
		});
	}

	public static void withLiNGOFileLegacyEvaluationRunner(@NonNull final URL url, @Nullable final Boolean withGCO, @NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, Exception> consumer)
			throws Exception {
		ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(url, (modelRecord, scenarioDataProvider) -> {
			withLegacyEvaluationRunner(scenarioDataProvider, withGCO, consumer);
		});
	}

	public static void withLiNGOFileLegacyOptimisationRunner(@NonNull final URL url, @Nullable final Boolean withGCO, @Nullable final Integer lsoIterations,
			@NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, Exception> consumer) throws Exception {

		ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(url, (modelRecord, scenarioDataProvider) -> {
			withLegacyOptimisationRunner(scenarioDataProvider, withGCO, lsoIterations, consumer);
		});
	}

	public static void withLiNGOFileOptimisationRunner(@NonNull final URL url, @NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, Exception> consumer) throws Exception {

		ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(url, (modelRecord, scenarioDataProvider) -> {
			withOptimisationRunner(scenarioDataProvider, consumer);
		});
	}

	public static <E extends Exception> void withExecutorService(final CheckedConsumer<@NonNull JobExecutorFactory, E> consumer) throws E {
		final JobExecutorFactory executorService = LNGScenarioChainBuilder.createExecutorService(1);
		consumer.accept(executorService);
	}

	public static <T, E extends Exception> T withExecutorService(final CheckedFunction<@NonNull JobExecutorFactory, T, E> func) throws E {
		final JobExecutorFactory executorService = new DefaultJobExecutorFactory(1);
		return func.apply(executorService);
	}

	public static void withEvaluationRunner(@NonNull final IScenarioDataProvider scenarioDataProvider, @NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, Exception> consumer)
			throws Exception {
		final OptimisationPlan optimiserSettings = LNGScenarioRunnerUtils.createExtendedSettings(ScenarioUtils.createDefaultOptimisationPlan());

		withEvaluationRunner(scenarioDataProvider, optimiserSettings, consumer);
	}

	public static void withEvaluationRunner(@NonNull final IScenarioDataProvider scenarioDataProvider, @NonNull final OptimisationPlan optimisationPlan,
			@NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, Exception> consumer) throws Exception {

		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				.withOptimiserInjectorService(createITSService()) //
				.withThreadCount(1)//
				.buildDefaultRunner();
		try {
			runner.evaluateInitialState();
			consumer.accept(runner.getScenarioRunner());
		} finally {
			runner.dispose();
		}
	}

	public static void withEvaluationRunnerWithGCO(@NonNull final IScenarioDataProvider scenarioDataProvider, @NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, Exception> consumer)
			throws Exception {
		withLegacyEvaluationRunner(scenarioDataProvider, true, consumer);

	}

	public static void withLegacyEvaluationRunner(@NonNull final IScenarioDataProvider scenarioDataProvider, @Nullable final Boolean withGCO,
			@NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, Exception> consumer) throws Exception {
		final OptimisationPlan optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(ScenarioUtils.createDefaultOptimisationPlan());

		if (withGCO != null) {
			optimisationPlan.getUserSettings().setGenerateCharterOuts(withGCO);
		}
		withEvaluationRunner(scenarioDataProvider, optimisationPlan, consumer);
	}

	public static <E extends Exception> void withOptimisationRunner(@NonNull final IScenarioDataProvider scenarioDataProvider, @NonNull final OptimisationPlan optimisationPlan,
			@NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, E> consumer) throws E {

		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				.withHints(LNGTransformerHelper.HINT_OPTIMISE_LSO) //
				.withOptimiserInjectorService(createITSService()) //
				.withThreadCount(1)//
				.buildDefaultRunner();
		try {
			runner.evaluateInitialState();
			consumer.accept(runner.getScenarioRunner());
		} finally {
			runner.dispose();
		}
	}

	public static <E extends Exception> void withOptimisationRunner(@NonNull final IScenarioDataProvider scenarioDataProvider, @NonNull final OptimisationPlan optimisationPlan,
			@NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, E> consumer, @Nullable final IOptimiserInjectorService optimiserInjectorService, final String... extraHints) throws E {

		final String[] hints = new String[1 + (extraHints == null ? 0 : extraHints.length)];
		hints[0] = LNGTransformerHelper.HINT_OPTIMISE_LSO;
		if (extraHints != null) {
			int idx = 1;
			for (final String h : extraHints) {
				hints[idx++] = h;
			}
		}

		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				.withHints(hints) //
				.withOptimiserInjectorService(optimiserInjectorService) //
				.withThreadCount(1)//
				.buildDefaultRunner();
		try {
			runner.evaluateInitialState();
			consumer.accept(runner.getScenarioRunner());
		} finally {
			runner.dispose();
		}
	}

	public static <E extends Exception> void withLegacyOptimisationRunner(@NonNull final IScenarioDataProvider scenarioDataProvider, @Nullable final Boolean withGCO,
			@Nullable final Integer lsoIterations, @NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, E> consumer) throws E {
		final OptimisationPlan optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(ScenarioUtils.createDefaultOptimisationPlan());

		if (withGCO != null) {
			optimisationPlan.getUserSettings().setGenerateCharterOuts(withGCO);
		}
		if (lsoIterations != null) {
			ScenarioUtils.setLSOStageIterations(optimisationPlan, lsoIterations);
		}
		withOptimisationRunner(scenarioDataProvider, optimisationPlan, consumer);
	}

	public static <E extends Exception> void withOptimisationRunner(@NonNull final IScenarioDataProvider scenarioDataProvider, @NonNull final CheckedConsumer<@NonNull LNGScenarioRunner, E> consumer)
			throws E {
		final OptimisationPlan optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(ScenarioUtils.createDefaultOptimisationPlan());

		withOptimisationRunner(scenarioDataProvider, optimisationPlan, consumer);
	}

	public static void saveScenarioModel(@NonNull final LNGScenarioModel scenario, @NonNull final File destinationFile) throws IOException {

		final LNGScenarioModel copy = EcoreUtil.copy(scenario);

		final IScenarioDataProvider tempDP = SimpleScenarioDataProvider.make(ModelsLNGVersionMaker.createDefaultManifest(), copy);

		ScenarioStorageUtil.storeToFile(ScenarioStorageUtil.createFrom(destinationFile.getName(), tempDP), destinationFile);
	}

	/**
	 * Use the {@link IParameterModesRegistry} to extend the existing settings object.
	 * 
	 * @param optimiserSettings
	 * @return
	 */

	/**
	 * Special optimiser injection service to disable special deployment settings during ITS runs
	 * 
	 * @return
	 */
	@NonNull
	public static IOptimiserInjectorService createITSService() {

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
							bind(CacheMode.class).annotatedWith(Names.named(SchedulerConstants.Key_VoyagePlanEvaluatorCache)).toInstance(CacheMode.On);
							bind(CacheMode.class).annotatedWith(Names.named(SchedulerConstants.Key_TimeWindowSchedulerCache)).toInstance(CacheMode.On);
						}
					});
				}
				if (moduleType == ModuleType.Module_EvaluationParametersModule) {
					return Collections.<@NonNull Module> singletonList(new AbstractModule() {

						@Override
						protected void configure() {

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
