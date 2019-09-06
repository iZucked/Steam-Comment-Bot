/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.optimiser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.concurrent.CleanableExecutorService;
import com.mmxlabs.common.concurrent.SimpleCleanableExecutorService;
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParallelOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.SimilaritySettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.actionplan.ActionSetLogger;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.AbstractRunnerHook;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.exporter.util.HeadlessJSONParser;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.exporter.util.HeadlessParameters;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.exporter.util.JMap;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.exporter.util.JSONParseResult;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.exporter.util.LNGHeadlessParameters;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.lso.logging.LSOLogger;
import com.mmxlabs.optimiser.lso.logging.LSOLoggingExporter;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class HeadlessOptimiserRunner {
	//
	public static class Options {

		public String scenarioFileName;
		public String jsonFile;

		public String outputScenarioFileName;
		public String outputLoggingFolder;
		// public String output;
		// public String outputPath;
		// public String outputName;

		public boolean exportResults = false;
		public boolean turnPerfOptsOn = true;
	}

	public void run(final File lingoFile, final Options options, IProgressMonitor monitor, final BiConsumer<ScenarioModelRecord, IScenarioDataProvider> completedHook, HeadlessOptimiserJSON jsonOutput) throws Exception {
		// Get the root object
		ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(lingoFile.toURI().toURL(), (modelRecord, scenarioDataProvider) -> {
			run(options, modelRecord, scenarioDataProvider, monitor, completedHook, jsonOutput);
		});
	}

	public boolean run(final Options options, final ScenarioModelRecord scenarioModelRecord, @NonNull final IScenarioDataProvider sdp, IProgressMonitor monitor,
			final BiConsumer<ScenarioModelRecord, IScenarioDataProvider> completedHook, HeadlessOptimiserJSON jsonOutput) {
		final SettingsOverride overrideSettings = new SettingsOverride(); // ?some settings for the optimiser?

		final String jsonFilePath = options.jsonFile;
		final Pair<JSONParseResult, LNGHeadlessParameters> jsonParse = setParametersFromJSON(overrideSettings, jsonFilePath);
		if (!jsonParse.getFirst().allRequirementsPassed()) {
			// json parse failed
			for (final String error : jsonParse.getFirst().getRequiredMissingText()) {
				System.err.println(error);
			}
			return false;
		}
		final LNGHeadlessParameters headlessParameters = jsonParse.getSecond();

		// // set output file
		// final String path = overrideSettings.getOutputPath();
		//
		// // set scenario file
		// if (options.scenarioFileName != null) {
		// overrideSettings.setScenario(options.scenarioFileName);
		// } else {
		// overrideSettings.setScenario(headlessParameters.getParameterValue("scenario-path", String.class) + "/" + headlessParameters.getParameterValue("scenario", String.class));
		// }

		// Set Idle time levels
		// overrideSettings.setIdleTimeLow(headlessParameters.getParameterValue("idle-time-low", Integer.class));
		// overrideSettings.setIdleTimeHigh(headlessParameters.getParameterValue("idle-time-high", Integer.class));
		// overrideSettings.setIdleTimeEnd(headlessParameters.getParameterValue("idle-time-end", Integer.class));
		// Set verbose Logging
		// overrideSettings.setActionPlanVerboseLogger(headlessParameters.getParameterValue("actionSets-verboseLogging", Boolean.class));
		// overrideSettings.setUseRouletteWheel(headlessParameters.getParameterValue("use-roulette-wheel", Boolean.class));
		// overrideSettings.setUseLegacyCheck(headlessParameters.getParameterValue("use-legacy-check", Boolean.class));
		// overrideSettings.setUseGuidedMoves(headlessParameters.getParameterValue("use-guided-moves", Boolean.class));
		final String scenarioFile = options.scenarioFileName;
		if (scenarioFile == null || scenarioFile.isEmpty()) {
			System.err.println("No scenario specified");
			return false;
		}

		OptimisationPlan optimisationPlan = ScenarioUtils.createDefaultOptimisationPlan();
		assert optimisationPlan != null;
		optimisationPlan.getStages().clear();

		updateOptimiserSettings((LNGScenarioModel) sdp.getScenario(), optimisationPlan, overrideSettings, headlessParameters);

		// Add in required extensions, but do not run custom hooks.
		optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(optimisationPlan, true, false);

		// assert overrideSettings.getOutputName() != null;//
		// final String outputFolderName = overrideSettings.getOutputName() == null ? getFolderNameFromSettings(optimisationPlan) : getFolderNameFromSettings(overrideSettings);
		// final String outputFolderName = getFolderNameFromSettings(overrideSettings);

		// Ensure dir structure is in place
		boolean exportLogs = options.outputLoggingFolder != null;
		if (options.outputLoggingFolder != null) {
			new File(options.outputLoggingFolder).mkdirs();
		}

		final JSONObject outputNodeRoot = new JSONObject(); // root for output data at present, want to insert into 
		final JSONArray stages = new JSONArray();
		
		if (jsonOutput != null) {
			jsonOutput.getMetrics().setStages(outputNodeRoot);
		}
		
		outputNodeRoot.put("stages", stages);

		// Create logging module
		final int num_threads = getNumThreads(headlessParameters);
		final @NonNull CleanableExecutorService executorService = new SimpleCleanableExecutorService(Executors.newFixedThreadPool(num_threads), num_threads);

		final Map<String, LSOLogger> phaseToLoggerMap = new ConcurrentHashMap<>();

		final ActionSetLogger actionSetLogger = optimisationPlan.getUserSettings().isBuildActionSets() ? new ActionSetLogger() : null;
		try {

			final AbstractRunnerHook runnerHook = !exportLogs ? null : new AbstractRunnerHook() {

				@Override
				protected void doEndStageJob(@NonNull final String stage, final int jobID, @Nullable final Injector injector) {

					final String stageAndJobID = getStageAndJobID();
					final LSOLogger logger = phaseToLoggerMap.remove(stageAndJobID);
					if (logger != null) {
						final LSOLoggingExporter lsoLoggingExporter = new LSOLoggingExporter(stages, stageAndJobID, logger);
						lsoLoggingExporter.exportData("best-fitness", "current-fitness");
					}
				}

			};

			// Create logging module
			final IOptimiserInjectorService localOverrides = new IOptimiserInjectorService() {
				@Override
				public Module requestModule(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
					return null;
				}

				@Override
				@Nullable
				public List<@NonNull Module> requestModuleOverrides(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
					if (moduleType == ModuleType.Module_EvaluationParametersModule) {
						return Collections.<@NonNull Module> singletonList(new EvaluationSettingsOverrideModule(overrideSettings));
					}
					if (moduleType == ModuleType.Module_OptimisationParametersModule) {
						return Collections.<@NonNull Module> singletonList(new OptimisationSettingsOverrideModule(overrideSettings));
					}
					if (moduleType == ModuleType.Module_Optimisation) {
						final LinkedList<@NonNull Module> modules = new LinkedList<>();
						if (exportLogs) {
							modules.add(createLoggingModule(phaseToLoggerMap, actionSetLogger, runnerHook));
						}
						modules.add(new LNGOptimsationOverrideModule(overrideSettings));
						return modules;
					}
					return null;
				}
			};

			final List<String> hints = new LinkedList<>();
			hints.add(LNGTransformerHelper.HINT_OPTIMISE_LSO);

			try {

				final LNGScenarioRunner runner = LNGOptimisationBuilder.begin(sdp) //
						.withRunnerHook(runnerHook) //
						.withOptimiserInjectorService(localOverrides) //
						.withHints(hints.toArray(new String[hints.size()])) //
						.withOptimisationPlan(optimisationPlan) //
						.withThreadCount(num_threads) //
						.buildDefaultRunner() //
						.getScenarioRunner();

				final long startTime = System.currentTimeMillis();
				runner.evaluateInitialState();
				System.out.println("LNGResult(");
				System.out.println("\tscenario='" + scenarioFile + "',");
				if (options.outputScenarioFileName != null) {
					System.out.println("\toutput='" + options.outputScenarioFileName + "',");
				}
				// System.out.println("\titerations=" + optimiserSettings.getAnnealingSettings().getIterations() + ",");
				// System.out.println("\tseed=" + optimiserSettings.getSeed() + ",");
				// System.out.println("\tcooling=" + optimiserSettings.getAnnealingSettings().getCooling() + ",");
				// System.out.println("\tepochLength=" + optimiserSettings.getAnnealingSettings().getEpochLength() + ",");
				// System.out.println("\ttemp=" + optimiserSettings.getAnnealingSettings().getInitialTemperature() + ",");

				System.err.println("Starting run...");

				IMultiStateResult result = runner.runWithProgress(monitor);
				runner.applyBest(result);

				final long runTime = System.currentTimeMillis() - startTime;
				final Schedule finalSchedule = runner.getSchedule();
				if (finalSchedule == null) {
					System.err.println("Error optimising scenario");
				}

				System.out.println("\truntime=" + runTime + ",");
				
				jsonOutput.getMetrics().setRuntime(runTime);

				System.err.println("Optimised!");

				if (options.outputScenarioFileName != null) {
					new File(options.outputScenarioFileName).getParentFile().mkdirs();
					saveScenario(options.outputScenarioFileName, scenarioModelRecord);
				}
				if (exportLogs) {
					exportData(phaseToLoggerMap, actionSetLogger, options.outputLoggingFolder, jsonFilePath, overrideSettings.isActionPlanVerboseLogger());
				}

				return true;
			} catch (final Exception e) {
				System.out.println("Headless Error");
				System.err.println("Headless Error:" + e.getMessage());
				e.printStackTrace();
			}

		} finally {
			executorService.shutdownNow();
		}
		return false;
	}

	private void updateOptimiserSettings(final LNGScenarioModel rootObject, final OptimisationPlan plan, final SettingsOverride settingsOverride, final HeadlessParameters headlessParameters) {

		final List<ConstraintAndFitnessSettings> constraintsAndFitnesses = new LinkedList<>();

		final Integer seed = headlessParameters.getParameterValue("seed", Integer.class);

		@NonNull
		final JMap lsoSettings = headlessParameters.getParameterValue("simulated-annealing", JMap.class);
		final Double coolingFactor = lsoSettings.getValue("cooling-factor", Double.class);
		final Integer initialTemperature = lsoSettings.getValue("initial-temperature", Integer.class);
		final Integer epochLength = lsoSettings.getValue("epoch-length", Integer.class);
		createDateRanges(plan, headlessParameters);
		// set Similarity Settings
		SimilaritySettings similaritySettings = createSimilaritySettings(settingsOverride, headlessParameters, plan.getUserSettings());

		{
			final LocalSearchOptimisationStage stage = ScenarioUtils.createDefaultLSOParameters(ScenarioUtils.createDefaultConstraintAndFitnessSettings());
			stage.setSeed(seed);

			stage.getAnnealingSettings().setIterations(headlessParameters.getParameterValue("iterations", Integer.class));

			// LSO Settings
			stage.getAnnealingSettings().setCooling(coolingFactor);
			stage.getAnnealingSettings().setInitialTemperature(initialTemperature);
			stage.getAnnealingSettings().setEpochLength(epochLength);

			// Restarting
			final JMap restartingSettings = headlessParameters.getParameterValue("restarting", JMap.class);
			stage.getAnnealingSettings().setRestarting(restartingSettings.getValue("active", Boolean.class));
			stage.getAnnealingSettings().setRestartIterationsThreshold(restartingSettings.getValue("threshold", Integer.class));

			// Similarity
			stage.getConstraintAndFitnessSettings().setSimilaritySettings(createSimilaritySettings(settingsOverride, headlessParameters, plan.getUserSettings()));

			final ParallelOptimisationStage<LocalSearchOptimisationStage> pStage = ParametersFactory.eINSTANCE.createParallelOptimisationStage();
			pStage.setTemplate(stage);
			pStage.setJobCount(headlessParameters.getParameterValue("lso-jobs", Integer.class));

			// Add to list to manipulate
			constraintsAndFitnesses.add(stage.getConstraintAndFitnessSettings());

			plan.getStages().add(pStage);
		}
		// Hill-Climbing
		final JMap hillClimbingSettings = headlessParameters.getParameterValue("hill-climbing", JMap.class);
		if (hillClimbingSettings.getValue("active", Boolean.class)) {
			final HillClimbOptimisationStage stage = ScenarioUtils.createDefaultHillClimbingParameters(ScenarioUtils.createDefaultConstraintAndFitnessSettings());
			stage.getAnnealingSettings().setIterations(hillClimbingSettings.getValue("iterations", Integer.class));
			stage.setSeed(seed);

			stage.getAnnealingSettings().setInitialTemperature(initialTemperature);
			stage.getAnnealingSettings().setEpochLength(epochLength);
			stage.getAnnealingSettings().setCooling(coolingFactor);
			// Similarity
			stage.getConstraintAndFitnessSettings().setSimilaritySettings(createSimilaritySettings(settingsOverride, headlessParameters, plan.getUserSettings()));

			final ParallelOptimisationStage<HillClimbOptimisationStage> pStage = ParametersFactory.eINSTANCE.createParallelOptimisationStage();
			pStage.setTemplate(stage);
			// pStage.setJobCount(headlessParameters.getParameter("clean-state-jobs", Integer.class));

			// Add to list to manipulate
			constraintsAndFitnesses.add(stage.getConstraintAndFitnessSettings());

			plan.getStages().add(pStage);
		}
		// Action Sets
		final JMap actionSettings = headlessParameters.getParameterValue("action-sets-data", JMap.class);
		if (headlessParameters.getParameterValue("action-sets", Boolean.class)) {
			final ActionPlanOptimisationStage stage = ScenarioUtils.createDefaultActionPlanParameters(ScenarioUtils.createDefaultConstraintAndFitnessSettings());
			stage.setTotalEvaluations(actionSettings.getValue("total-evals", Integer.class));
			stage.setInRunEvaluations(actionSettings.getValue("in-run-evals", Integer.class));
			stage.setSearchDepth(actionSettings.getValue("max-search-depth", Integer.class));
			plan.getUserSettings().setBuildActionSets(headlessParameters.getParameterValue("action-sets", Boolean.class));

			// Add to list to manipulate
			constraintsAndFitnesses.add(stage.getConstraintAndFitnessSettings());

			plan.getStages().add(stage);
		}

		// Scenario settings
		plan.getUserSettings().setShippingOnly(headlessParameters.getParameterValue("shipping-only-optimisation", Boolean.class));
		plan.getUserSettings().setGenerateCharterOuts(headlessParameters.getParameterValue("charter-outs-optimisation", Boolean.class));
		plan.getUserSettings().setWithSpotCargoMarkets(headlessParameters.getParameterValue("spot-market-optimisation", Boolean.class));
		// action sets

		for (final ConstraintAndFitnessSettings settings : constraintsAndFitnesses) {
			createObjectives(settings, headlessParameters.getParameterValue("objectives", JMap.class));
		}
		setLatenessParameters(settingsOverride, headlessParameters);

		createPromptDates(rootObject, headlessParameters);
		setMoveDistributions(settingsOverride, headlessParameters);
		settingsOverride.setShuffleCutoff(headlessParameters.getParameterValue("shuffle-cutoff", Integer.class));
	}

	private void createPromptDates(final LNGScenarioModel rootObject, final HeadlessParameters parameters) {
		final JMap periodSettings = parameters.getParameterValue("period-data", JMap.class);
		final LocalDate promptStart = periodSettings.getValue("prompt-start", LocalDate.class);
		final LocalDate promptEnd = periodSettings.getValue("prompt-end", LocalDate.class);
		if (promptStart != null) {
			rootObject.setPromptPeriodStart(promptStart);
		} else {
			rootObject.setPromptPeriodStart(null);
		}
		if (promptEnd != null) {
			rootObject.setPromptPeriodEnd(promptEnd);
		} else {
			rootObject.setPromptPeriodEnd(null);
		}
	}

	private void setMoveDistributions(final SettingsOverride overrideSettings, final HeadlessParameters headlessParameters) {
		overrideSettings.setEqualMoveDistributions(headlessParameters.getParameterValue("equal-moves-distribution", Boolean.class));
		overrideSettings.setUseRouletteWheel(headlessParameters.getParameterValue("roulette-wheel", Boolean.class));
		final JMap moves = headlessParameters.getParameterValue("move-distributions", JMap.class);
		final Map<String, Double> moveDistributionsMap = new HashMap<>();
		for (final String key : moves.getKeySet()) {
			moveDistributionsMap.put(key, moves.getValue(key, Double.class));
		}
		overrideSettings.setMoveMap(moveDistributionsMap);
	}

	private void setLatenessParameters(final SettingsOverride overrideSettings, final HeadlessParameters headlessParameters) {
		final JMap lateness = headlessParameters.getParameterValue("lateness-weights", JMap.class);
		final Map<String, Integer> latenessMap = new HashMap<>();
		for (final String key : lateness.getKeySet()) {
			latenessMap.put(key, lateness.getValue(key, Integer.class));
		}
		overrideSettings.setlatenessMap(latenessMap);
	}

	private SimilaritySettings createSimilaritySettings(final SettingsOverride overrideSettings, final HeadlessParameters headlessParameters, UserSettings settings) {
		SimilarityMode mode = getSimilarityModeFromParameters(headlessParameters.getParameterValue("similarity-mode", String.class));
		if (mode != null) {
			settings.setSimilarityMode(mode);
			return OptimisationHelper.createSimilaritySettings(mode, settings.getPeriodStartDate(), settings.getPeriodEnd());
		} else {
			final JMap similarity = headlessParameters.getParameterValue("similarity", JMap.class);
			final Map<String, Integer> similarityMap = new HashMap<>();
			for (final String key : similarity.getKeySet()) {
				similarityMap.put(key, similarity.getValue(key, Integer.class));
			}
			// TODO: not sure why we need to put things into JMAPS?
			return ScenarioUtils.createSimilaritySettings(similarityMap.get("low-thresh"), similarityMap.get("low-weight"), similarityMap.get("med-thresh"), similarityMap.get("med-weight"),
					similarityMap.get("high-thresh"), similarityMap.get("high-weight"), similarityMap.get("out-of-bounds-weight"));
		}
	}

	private SimilarityMode getSimilarityModeFromParameters(@NonNull String parameter) {
		return SimilarityMode.getByName(parameter);
	}

	private void createDateRanges(final OptimisationPlan plan, final HeadlessParameters headlessParameters) {
		final JMap periodSettings = headlessParameters.getParameterValue("period-data", JMap.class);
		final YearMonth dateBefore = periodSettings.getValue("period-optimisation-date-before", YearMonth.class);
		final YearMonth dateAfterYM = periodSettings.getValue("period-optimisation-date-after", YearMonth.class);
		if (dateBefore != null) {
			plan.getUserSettings().setPeriodEnd(dateBefore);
		}
		if (dateAfterYM != null) {
			final LocalDate dateAfterLD = LocalDate.of(dateAfterYM.getYear(), dateAfterYM.getMonth(), 1);
			plan.getUserSettings().setPeriodStartDate(dateAfterLD);
		}
	}

	private void createObjectives(final ConstraintAndFitnessSettings settings, final JMap jMap) {
		if (jMap != null) {
			settings.getObjectives().clear();
			for (final String objectiveName : jMap.getJMap().keySet()) {
				if (jMap.getClass(objectiveName) == Double.class) {
					settings.getObjectives().add(ScenarioUtils.createObjective(objectiveName, jMap.getValue(objectiveName, Double.class)));
				} else {
					settings.getObjectives().add(ScenarioUtils.createObjective(objectiveName, jMap.getValue(objectiveName, Integer.class)));

				}
			}
		}
	}

	private String getFolderNameFromSettings(final OptimisationPlan settings) {
		final Map<String, Object> nameMap = getSettingsFileNameMap(settings);
		String name = "";
		int count = 0;
		for (final String key : nameMap.keySet()) {
			name += String.format("%s-%s%s", key, nameMap.get(key), ++count < nameMap.size() ? "-" : "");
		}
		return name;
	}

	// private String getFolderNameFromSettings(final SettingsOverride settings) {
	// return settings.getOutputName();
	// }

	private Map<String, Object> getSettingsFileNameMap(final OptimisationPlan settings) {
		final Map<String, Object> nameMap = new LinkedHashMap<>();
		// nameMap.put("seed", settings.getSeed());
		// nameMap.put("iterations", settings.getAnnealingSettings().getIterations());
		// nameMap.put("temperature", settings.getAnnealingSettings().getInitialTemperature());
		// nameMap.put("epochLength", settings.getAnnealingSettings().getEpochLength());
		// nameMap.put("cooling", settings.getAnnealingSettings().getCooling());
		// nameMap.put("shippingOnly", settings.isShippingOnly());
		// nameMap.put("gco", settings.isGenerateCharterOuts());
		return nameMap;
	}

	private void exportData(final Map<String, LSOLogger> loggerMap, final ActionSetLogger actionSetLogger, final String path, final String jsonFilePath, final boolean verbose) {
		// // first export logging data
		// for (final String phase : IRunnerHook.PHASE_ORDER) {
		// final LSOLogger logger = loggerMap.get(phase);
		// if (logger != null) {
		// final LSOLoggingExporter lsoLoggingExporter = new LSOLoggingExporter(path, phase, foldername, logger);
		// lsoLoggingExporter.exportData("best-fitness", "current-fitness");
		// }
		// }
		if (actionSetLogger != null) {
			System.out.println(verbose);
			if (!verbose)
				actionSetLogger.shortExport(Paths.get(path).toString(), "actionSets");
			else
				actionSetLogger.export(Paths.get(path).toString(), "action");
		}
		// HeadlessJSONParser.copyJSONFile(jsonFilePath, Paths.get(path, "parameters.json").toString());

		final PrintWriter writer = WriterFactory.getWriter(Paths.get(path, "machineData.txt").toString());
		writer.write(String.format("maxCPUs,%s", Runtime.getRuntime().availableProcessors()));
		writer.close();

	}

	private int getNumThreads(final LNGHeadlessParameters headlessParameters) {
		final int processorsFromParams = headlessParameters.getParameterValue("pool-size", Integer.class);
		final int recommended = Math.max(1, Runtime.getRuntime().availableProcessors());
		return Math.min(processorsFromParams, recommended);
	}

	@NonNull
	private Module createLoggingModule(final Map<String, LSOLogger> phaseToLoggerMap, final ActionSetLogger actionSetLogger, final AbstractRunnerHook runnerHook) {
		final LoggingModule loggingModule = new LoggingModule(phaseToLoggerMap, actionSetLogger, runnerHook, 10_000);
		return loggingModule;
	}
	//
	// private void addFitnessTraceExporter(final List<IRunExporter> exporters, final String path, final String outputFolderName) {
	// final FitnessTraceExporter exporter = new FitnessTraceExporter();
	// addExporter(exporters, path, outputFolderName, "fitnessTrace.txt", exporter);
	// }
	//
	// private void addExporter(final List<IRunExporter> exporters, final String path, final String outputFolderName, final String filename, final IRunExporter exporter) {
	// exporter.setOutputFile(Paths.get(path, outputFolderName), filename);
	// exporters.add(exporter);
	// }

	// private void addLatenessExporter(final List<IRunExporter> exporters, final String path, final String outputFolderName) {
	// final GeneralAnnotationsExporter exporter = new GeneralAnnotationsExporter();
	// addExporter(exporters, path, outputFolderName, "generalAnnotationsReport.txt", exporter);
	// }

	private Pair<JSONParseResult, LNGHeadlessParameters> setParametersFromJSON(final SettingsOverride settings, final String json) {
		final LNGHeadlessParameters headlessParameters = new LNGHeadlessParameters();
		final HeadlessJSONParser headlessJSONParser = new HeadlessJSONParser();
		final Map<String, Pair<Object, Class<?>>> parsed = headlessJSONParser.parseJSON(json);
		final JSONParseResult result = headlessParameters.setParametersFromJSON(parsed);

		return new Pair<>(result, headlessParameters);
	}

	private void saveScenario(final String outputFile, final ScenarioModelRecord modelRecord) throws IOException {

		ScenarioStorageUtil.storeToFile(modelRecord, new File(outputFile));
	}

}
