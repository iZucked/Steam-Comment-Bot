/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobrunners.optimisation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.OptionalLong;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.json.simple.JSONArray;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.common.util.exceptions.UserFeedbackException;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.OptimisationResult;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl;
import com.mmxlabs.models.lng.parameters.util.UserSettingsMixin;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.SequencesContainer;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.config.OptimiserConfigurationOptions;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.AbstractRunnerHook;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.models.lng.transformer.ui.common.SolutionSetExporterUnit;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessGenericJSON.ScenarioMeta;
import com.mmxlabs.models.lng.transformer.ui.headless.LSOLoggingExporter;
import com.mmxlabs.models.lng.transformer.ui.headless.common.CustomTypeResolverBuilder;
import com.mmxlabs.models.lng.transformer.ui.headless.common.HeadlessRunnerUtils;
import com.mmxlabs.models.lng.transformer.ui.headless.common.ScenarioMetaUtils;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.EvaluationSettingsOverrideModule;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserJSON;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserJSONTransformer;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.LNGOptimisationOverrideModule;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.LoggingModule;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.AbstractJobRunner;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.lso.logging.LSOLogger;
import com.mmxlabs.optimiser.lso.logging.LSOLogger.LoggingParameters;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class OptimisationJobRunner extends AbstractJobRunner {

	public static final String JOB_TYPE = "optimisation";

	private OptimisationSettings optimiserSettings;
	private OptimiserConfigurationOptions benchmarkSettings;

	private HeadlessOptimiserJSON loggingData;

	@Override
	public void withParams(final String json) throws IOException {
		final ObjectMapper mapper = createObjectMapper();
		try {
			optimiserSettings = mapper.readValue(json, OptimisationSettings.class);
		} catch (final Exception e) {
			try {
				final OptimisationSettings settings = new OptimisationSettings();
				settings.setUserSettings(mapper.readValue(json, UserSettings.class));
				this.optimiserSettings = settings;
			} catch (final Exception e2) {
				benchmarkSettings = OptimiserConfigurationOptions.readFromRawJSON(json, Collections.emptyMap());
			}
		}
	}

	@Override
	public @Nullable AbstractSolutionSet run(final int threadsAvailable, final IProgressMonitor monitor) {
		if (optimiserSettings == null && benchmarkSettings == null) {
			throw new IllegalStateException("Optimiser parameters have not been set");
		}
		final IScenarioDataProvider pSDP = sdp;
		if (pSDP == null) {
			throw new IllegalStateException("Scenario has not been set");
		}

		if (enableLogging) {
			final HeadlessOptimiserJSONTransformer transformer = new HeadlessOptimiserJSONTransformer();
			final HeadlessOptimiserJSON json = transformer.createJSONResultObject();
			if (meta != null) {
				json.setMeta(meta);
			}
			json.getParams().setCores(threadsAvailable);

			loggingData = json;
		}
		if (benchmarkSettings != null) {
			return doJobRun(pSDP, benchmarkSettings, threadsAvailable, SubMonitor.convert(monitor));
		} else {
			return doJobRun(pSDP, optimiserSettings, threadsAvailable, SubMonitor.convert(monitor));
		}
	}

	@Override
	public void saveLogs(final File file) throws IOException {
		if (enableLogging && loggingData != null) {
			try (final PrintWriter p = new PrintWriter(new FileOutputStream(file))) {
				p.write(saveLogsAsString());
			}
		}
	}

	@Override
	public void saveLogs(final OutputStream os) throws IOException {
		if (enableLogging && loggingData != null) {
			try (final PrintWriter p = new PrintWriter(os)) {
				p.write(saveLogsAsString());
			}
		}
	}

	@Override
	public String saveLogsAsString() throws IOException {
		if (enableLogging && loggingData != null) {

			HeadlessRunnerUtils.renameInvalidBsonFields(loggingData.getMetrics().getStages());

			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.registerModule(new Jdk8Module());
			mapper.addMixIn(UserSettingsImpl.class, UserSettingsMixin.class);
			mapper.addMixIn(UserSettings.class, UserSettingsMixin.class);

			final CustomTypeResolverBuilder resolver = new CustomTypeResolverBuilder();
			resolver.init(JsonTypeInfo.Id.CLASS, null);
			resolver.inclusion(JsonTypeInfo.As.PROPERTY);
			resolver.typeProperty("@class");
			mapper.setDefaultTyping(resolver);

			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			mapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);

			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(loggingData);
		}
		throw new IllegalStateException("Logging not configured");
	}

	private AbstractRunnerHook createRunnerHook(final JSONArray jsonStagesStorage, final Map<String, LSOLogger> phaseToLoggerMap) {
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

	private @Nullable AbstractSolutionSet doJobRun(final @NonNull IScenarioDataProvider sdp, final @NonNull Object settings, final int numThreads, @NonNull final IProgressMonitor monitor) {

		int threadsToUse = numThreads;
		boolean doInjections = false;
		JsonNode injections = null;

		OptimisationPlan optimisationPlan = null;
		UserSettings userSettings = null;

		// Temp non-final resultName string
		String lResultName = null;
		if (settings instanceof final OptimiserConfigurationOptions optimisationSettings) {
			doInjections = OptimiserConfigurationOptions.requiresInjections(optimisationSettings);
			injections = optimisationSettings.injections;

			// incorporate the settings from the parameterModesRegistry
			optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(optimisationSettings.plan, true, false);
			userSettings = optimisationPlan.getUserSettings();

			if (optimisationSettings.getNumThreads() > 0) {
				threadsToUse = optimisationSettings.getNumThreads();
			}
			if (optimisationPlan.getResultName() != null) {
				lResultName = optimisationPlan.getResultName();
			}
		} else if (settings instanceof final OptimisationSettings optimisationSettings) {
			userSettings = optimisationSettings.getUserSettings();
			if (optimisationSettings.getResultName() != null) {
				lResultName = optimisationSettings.getResultName();
			}
		}
		final String resultName = lResultName;

		if (threadsToUse < 1) {
			threadsToUse = LNGScenarioChainBuilder.getNumberOfAvailableCores();
		}
		final HeadlessOptimiserJSON loggingOutput = loggingData;
		if (loggingOutput != null) {
			loggingOutput.getParams().setCores(threadsToUse);
		}

		final boolean exportLogs = loggingOutput != null;

		final JSONArray jsonStagesStorage = new JSONArray(); // array for output data at present

		if (loggingOutput != null) {
			loggingOutput.getMetrics().setStages(jsonStagesStorage);
		}

		// Create logging module
		final Map<String, LSOLogger> phaseToLoggerMap = new ConcurrentHashMap<>();

		final AbstractRunnerHook runnerHook = !exportLogs ? null : createRunnerHook(jsonStagesStorage, phaseToLoggerMap);

		// Create logging module
		final IOptimiserInjectorService localOverrides = createExtraModule(exportLogs, phaseToLoggerMap, runnerHook, doInjections, injections);

		final List<String> hints = new LinkedList<>();
		hints.add(LNGTransformerHelper.HINT_OPTIMISE_LSO);

		try {

			final LNGOptimisationBuilder runnerBuilder = LNGOptimisationBuilder.begin(sdp) //
					.withRunnerHook(runnerHook) //
					.withOptimiserInjectorService(localOverrides) //
					.withHints(hints.toArray(new String[hints.size()])) //
					.withThreadCount(threadsToUse) //
					.withOptimiseHint() //
					.withIncludeDefaultExportStage(false) // Disable default export so we do not create it twice
					.withOptimisationPlanCustomiser(plan -> {
						// Set a default name is there is none
						if (resultName == null || resultName.isBlank()) {
							plan.setResultName("Optimisation");
						}
						if (loggingOutput != null) {
							loggingOutput.setOptimisationPlan(plan);
						}
					}) //
			;

			if (optimisationPlan != null) {
				runnerBuilder.withOptimisationPlan(optimisationPlan);
				userSettings = optimisationPlan.getUserSettings();
			} else if (userSettings != null) {
				runnerBuilder.withUserSettings(userSettings);
			}

			assert userSettings != null;

			final LNGScenarioRunner runner = runnerBuilder //
					.buildDefaultRunner() //
					.getScenarioRunner();

			final long startTime = System.currentTimeMillis();
			
			// Don't run an evaluation if we are in clean state as this will unpair the starting point 
			if (!userSettings.isCleanSlateOptimisation()) {
				sdp.setLastEvaluationFailed(true);
				runner.evaluateInitialState();
			}

			final IMultiStateResult result = runner.runWithProgress(monitor);

			sdp.setLastEvaluationFailed(false);

			if (result == null) {
				throw new RuntimeException("Error optimising scenario - null result");
			}

			if (loggingOutput != null) {
				final long runTime = System.currentTimeMillis() - startTime;
				loggingOutput.getMetrics().setRuntime(runTime);
			}

			final boolean dualModeInsertions = userSettings.isDualMode();

			final OptionalLong portfolioBreakEvenTarget = OptionalLong.empty();
			final OptimisationResult options = AnalyticsFactory.eINSTANCE.createOptimisationResult();

			options.setUserSettings(EcoreUtil.copy(userSettings));

			final IChainLink link = SolutionSetExporterUnit.exportMultipleSolutions(null, 1, runner.getScenarioToOptimiserBridge(), () -> options, dualModeInsertions, portfolioBreakEvenTarget, false);

			final LNGDataTransformer dataTransformer = runner.getScenarioToOptimiserBridge().getDataTransformer();
			final SequencesContainer initialSequencesContainer = new SequencesContainer(dataTransformer.getInitialResult().getBestSolution());
			link.run(dataTransformer, initialSequencesContainer, result, new NullProgressMonitor());

			if (loggingOutput != null) {
				final ScenarioMeta scenarioMeta = ScenarioMetaUtils.writeOptimisationMetrics( //
						runner.getScenarioToOptimiserBridge().getOptimiserScenario(), //
						userSettings);

				loggingOutput.setScenarioMeta(scenarioMeta);
			}

			if (resultName == null || resultName.isBlank()) {
				options.setName("Optimisation");
			} else {
				options.setName(resultName);
			}

			return options;
		} catch (final OperationCanceledException e) {
			return null;
		} catch (final UserFeedbackException e) {
			throw e;
		} catch (final Exception e) {
			throw new RuntimeException("Error during optimisation", e);
		}
	}

	private @Nullable IOptimiserInjectorService createExtraModule(final boolean exportLogs, final Map<String, LSOLogger> phaseToLoggerMap, final AbstractRunnerHook runnerHook,
			final boolean doInjections, @Nullable final JsonNode injections) {
		if (!exportLogs && !doInjections) {
			return null;
		}

		return new IOptimiserInjectorService() {
			@Override
			public Module requestModule(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
				return null;
			}

			@Override
			@Nullable
			public List<@NonNull Module> requestModuleOverrides(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
				if (doInjections) {
					if (moduleType == ModuleType.Module_EvaluationParametersModule) {
						return Collections.<@NonNull Module>singletonList(new EvaluationSettingsOverrideModule(injections));
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

						modules.add(createLoggingModule(loggingParameters, phaseToLoggerMap, runnerHook));
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

	/**
	 * Creates a module for providing logging parameters to the {@link Injector}
	 * framework.
	 */

	private @NonNull Module createLoggingModule(final LoggingParameters loggingParameters, final Map<String, LSOLogger> phaseToLoggerMap, final AbstractRunnerHook runnerHook) {

		return new LoggingModule(phaseToLoggerMap, runnerHook, loggingParameters);
	}
}
