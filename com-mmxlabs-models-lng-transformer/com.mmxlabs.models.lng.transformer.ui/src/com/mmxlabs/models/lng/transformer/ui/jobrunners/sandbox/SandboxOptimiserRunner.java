/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.json.simple.JSONArray;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Exposed;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.mmxlabs.common.util.TriFunction;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.OptimisationResult;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.ExtraDataProvider;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGInitialSequencesModule;
import com.mmxlabs.models.lng.transformer.ui.AbstractRunnerHook;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessGenericJSON.Meta;
import com.mmxlabs.models.lng.transformer.ui.headless.LSOLoggingExporter;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.EvaluationSettingsOverrideModule;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserJSON;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserJSONTransformer;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.LNGOptimisationOverrideModule;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.LoggingModule;
import com.mmxlabs.models.lng.transformer.util.ScheduleSpecificationTransformer;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.logging.LSOLogger;
import com.mmxlabs.optimiser.lso.logging.LSOLogger.LoggingParameters;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class SandboxOptimiserRunner {

	private final IScenarioDataProvider originalScenarioDataProvider;

	private final LNGScenarioRunner scenarioRunner;

	private final EditingDomain originalEditingDomain;

	private static final String[] hint_with_breakeven = { LNGTransformerHelper.HINT_OPTIMISE_LSO, //
			SchedulerConstants.HINT_DISABLE_CACHES, //
			LNGEvaluationModule.HINT_PORTFOLIO_BREAKEVEN };

	private static final String[] hint_without_breakeven = { LNGTransformerHelper.HINT_OPTIMISE_LSO, //
			// LNGTransformerHelper.HINT_KEEP_NOMINALS_IN_PROMPT
	};

	private final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge;

	private final LNGDataTransformer dataTransformer;

	private final UserSettings userSettings;

	private OptimisationPlan plan;

	public SandboxOptimiserRunner(@Nullable final ScenarioInstance scenarioInstance, final IScenarioDataProvider scenarioDataProvider, final EditingDomain editingDomain,
			final UserSettings userSettings, @Nullable final ExtraDataProvider extraDataProvider,
			@Nullable final TriFunction<ModelEntityMap, IOptimisationData, Injector, ISequences> initialSolutionProvider, @Nullable final Consumer<LNGOptimisationBuilder> builderCustomiser) {

		this.originalScenarioDataProvider = scenarioDataProvider;
		this.originalEditingDomain = editingDomain;
		this.userSettings = userSettings;

		final LNGScenarioModel lngScenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
		plan = OptimisationHelper.transformUserSettings(userSettings, lngScenarioModel);
		plan = LNGScenarioRunnerUtils.createExtendedSettings(plan);
		final IOptimiserInjectorService extraService = buildExtraModules(extraDataProvider, initialSolutionProvider);

		// TODO: Only disable caches if we do a break-even (caches *should* be ok otherwise?)
		final String[] hints = hint_without_breakeven;
		final LNGOptimisationBuilder builder = LNGOptimisationBuilder.begin(originalScenarioDataProvider, scenarioInstance) //
				.withOptimisationPlan(plan) //
				.withExtraDataProvider(extraDataProvider) //
				.withOptimiserInjectorService(extraService) //
				.withOptimiseHint() //
				.withHints(hints) //
				.withIncludeDefaultExportStage(false) //
		;

		if (builderCustomiser != null) {
			builderCustomiser.accept(builder);
		}

		final LNGOptimisationRunnerBuilder runner = builder.buildDefaultRunner();
		scenarioRunner = runner.getScenarioRunner();

		scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
		dataTransformer = scenarioToOptimiserBridge.getDataTransformer();
	}

	private IOptimiserInjectorService buildExtraModules(final ExtraDataProvider extraDataProvider,
			@Nullable final TriFunction<ModelEntityMap, IOptimisationData, Injector, ISequences> initialSolutionProvider) {
		return new IOptimiserInjectorService() {

			@Override
			public @Nullable Module requestModule(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
				return null;
			}

			@Override
			public @Nullable List<@NonNull Module> requestModuleOverrides(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {

				if (moduleType == ModuleType.Module_InitialSolution) {
					if (!hints.contains(LNGTransformerHelper.HINT_PERIOD_SCENARIO) && initialSolutionProvider != null) {
						return Collections.singletonList(new PrivateModule() {

							@Override
							protected void configure() {
								// Nothing to do here - see provides methods
							}

							@Provides
							@Singleton
							@Named("EXTERNAL_SOLUTION")
							private ISequences provideSequences(final Injector injector, final ModelEntityMap mem, final IOptimisationData data) {
								final ISequences sequences = initialSolutionProvider.apply(mem, data, injector);
								return sequences;
							}

							@Provides
							@Singleton
							@Named(LNGInitialSequencesModule.KEY_GENERATED_RAW_SEQUENCES)
							@Exposed
							private ISequences provideInitialSequences(@Named("EXTERNAL_SOLUTION") final ISequences sequences) {
								return sequences;
							}

							@Provides
							@Singleton
							@Named(LNGInitialSequencesModule.KEY_GENERATED_SOLUTION_PAIR)
							@Exposed
							private IMultiStateResult provideSolutionPair(@Named("EXTERNAL_SOLUTION") final ISequences sequences) {

								return new MultiStateResult(sequences, new HashMap<>());
							}

						});
					}
				}

				return null;
			}

		};
	}

	public void prepare() {
		scenarioRunner.evaluateInitialState();
	}

	public IMultiStateResult runOptimiser(final IProgressMonitor progressMonitor) {
		return scenarioRunner.runWithProgress(progressMonitor);
	}

	public LNGScenarioRunner getLNGScenarioRunner() {
		return scenarioRunner;
	}

	public static Function<IProgressMonitor, AbstractSolutionSet> createSandboxJobFunction(final int threadsAvailable, final IScenarioDataProvider sdp,
			final @Nullable ScenarioInstance scenarioInstance, final SandboxSettings sandboxSettings, final OptionAnalysisModel model, @Nullable final Meta meta,
			@Nullable final Consumer<Object> registerLogging) {

		final OptimisationResult sandboxResult = AnalyticsFactory.eINSTANCE.createOptimisationResult();
		sandboxResult.setUseScenarioBase(false);

		final boolean allowCaching = false; // model.isUseTargetPNL()
		final UserSettings userSettings = sandboxSettings.getUserSettings();
		return SandboxRunnerUtil.createSandboxFunction(sdp, userSettings, model, sandboxResult, (mapper, baseScheduleSpecification) -> {
			final AbstractRunnerHook runnerHook;
			final IOptimiserInjectorService loggingOverrides;
			final HeadlessOptimiserJSON loggingOutput;
			if (registerLogging != null) {
				final HeadlessOptimiserJSONTransformer transformer = new HeadlessOptimiserJSONTransformer();
				loggingOutput = transformer.createJSONResultObject();
				if (meta != null) {
					loggingOutput.setMeta(meta);
				}
				loggingOutput.getParams().setCores(threadsAvailable);
				loggingOutput.setType("sandbox:" + loggingOutput.getType());

				final JSONArray jsonStagesStorage = new JSONArray(); // array for output data at present

				loggingOutput.getMetrics().setStages(jsonStagesStorage);

				// Create logging module
				final Map<String, LSOLogger> phaseToLoggerMap = new ConcurrentHashMap<>();
				runnerHook = createRunnerHook(jsonStagesStorage, phaseToLoggerMap);

				final JsonNode injections = null;
				final boolean doInjections = false;

				loggingOverrides = createExtraModule(true, phaseToLoggerMap, runnerHook, doInjections, injections);

				registerLogging.accept(loggingOutput);
			} else {
				loggingOutput = null;
				runnerHook = null;
				loggingOverrides = null;
			}

			final ExtraDataProvider extraDataProvider = mapper.getExtraDataProvider();

			final SandboxOptimiserRunner runner = new SandboxOptimiserRunner(scenarioInstance, sdp, sdp.getEditingDomain(), userSettings, extraDataProvider, (mem, data, injector) -> {
				final ScheduleSpecificationTransformer transformer = injector.getInstance(ScheduleSpecificationTransformer.class);
				return transformer.createSequences(baseScheduleSpecification, mem, data, injector, true);
			}, builder -> {
				builder.withThreadCount(threadsAvailable);

				if (loggingOutput != null) {
					builder.withRunnerHook(runnerHook);
					builder.withOptimiserInjectorService(loggingOverrides);
					builder.withOptimisationPlanCustomiser(plan -> {
						loggingOutput.setOptimisationPlan(plan);
					});
				}
			});

			return new SandboxJob() {
				@Override
				public LNGScenarioToOptimiserBridge getScenarioRunner() {
					return runner.getLNGScenarioRunner().getScenarioToOptimiserBridge();
				}

				@Override
				public IMultiStateResult run(final IProgressMonitor monitor) {
					final long startTime = System.currentTimeMillis();
					try {
						return runner.runOptimiser(monitor);
					} finally {
						if (loggingOutput != null) {
							final long runTime = System.currentTimeMillis() - startTime;
							loggingOutput.getMetrics().setRuntime(runTime);
						}
					}
				}
			};
		}, null, allowCaching);
	}

	private static AbstractRunnerHook createRunnerHook(final JSONArray jsonStagesStorage, final Map<String, LSOLogger> phaseToLoggerMap) {
		return new AbstractRunnerHook() {

			@Override
			protected void doEndStageJob(@NonNull final String stage, final int jobID, @Nullable final Injector injector) {

				final String stageAndJobID = getStageAndJobID();
				final LSOLogger logger = phaseToLoggerMap.remove(stageAndJobID);
				if (logger != null) {
					final LSOLoggingExporter lsoLoggingExporter = new LSOLoggingExporter(jsonStagesStorage, stageAndJobID, logger);
					lsoLoggingExporter.exportData("best-fitness", "current-fitness");
				}
			}

		};
	}

	private static @Nullable IOptimiserInjectorService createExtraModule(final boolean exportLogs, final Map<String, LSOLogger> phaseToLoggerMap, final AbstractRunnerHook runnerHook,
			final boolean doInjections, @Nullable final JsonNode injections) {
		if (!exportLogs && !doInjections) {
			return null;
		}

		return new IOptimiserInjectorService() {
			@Override
			public @Nullable Module requestModule(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
				return null;
			}

			@Override

			public @Nullable List<@NonNull Module> requestModuleOverrides(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
				if (doInjections) {
					if (moduleType == ModuleType.Module_EvaluationParametersModule) {
						return Collections.<@NonNull Module> singletonList(new EvaluationSettingsOverrideModule(injections));
					}
				}
				// if (moduleType == ModuleType.Module_OptimisationParametersModule) {
				// return Collections.<@NonNull Module> singletonList(new
				// OptimisationSettingsOverrideModule());
				// }
				if (moduleType == ModuleType.Module_Optimisation) {
					final LinkedList<@NonNull Module> modules = new LinkedList<>();
					if (exportLogs) {

						// Default logging parameters
						final LoggingParameters loggingParameters = new LoggingParameters();
						loggingParameters.loggingInterval = 5000;
						loggingParameters.metricsToLog = new String[0];

						modules.add(new LoggingModule(phaseToLoggerMap, runnerHook, loggingParameters));
					}
					if (doInjections) {
						modules.add(new LNGOptimisationOverrideModule(injections));
					}

					return modules;
				}
				return null;
			}
		};
	}

}
