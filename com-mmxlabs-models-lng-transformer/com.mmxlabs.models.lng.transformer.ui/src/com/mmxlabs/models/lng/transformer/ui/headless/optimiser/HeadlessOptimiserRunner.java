/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.optimiser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.common.concurrent.CleanableExecutorService;
import com.mmxlabs.common.concurrent.SimpleCleanableExecutorService;
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.actionplan.ActionSetLogger;
import com.mmxlabs.models.lng.transformer.config.OptimiserConfigurationOptions;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.AbstractRunnerHook;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessApplicationOptions;
import com.mmxlabs.models.lng.transformer.ui.headless.LSOLoggingExporter;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.lso.logging.LSOLogger;
import com.mmxlabs.optimiser.lso.logging.LSOLogger.LoggingParameters;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.json.EMFJacksonModule;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class HeadlessOptimiserRunner {

	public void run(final File lingoFile, final HeadlessApplicationOptions options, IProgressMonitor monitor, final BiConsumer<ScenarioModelRecord, IScenarioDataProvider> completedHook,
			HeadlessOptimiserJSON jsonOutput) throws Exception {
		// Get the root object
		ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(lingoFile.toURI().toURL(), (modelRecord, scenarioDataProvider) -> {
			run(options, scenarioDataProvider, monitor, completedHook, jsonOutput);
		});
	}

	public void runFromCsvDirectory(final File csvDirectory, final HeadlessApplicationOptions options, IProgressMonitor monitor,
			final BiConsumer<ScenarioModelRecord, IScenarioDataProvider> completedHook, HeadlessOptimiserJSON jsonOutput) {

		try {
			URL urlRoot = csvDirectory.toURI().toURL();
			ServiceHelper.withCheckedOptionalServiceConsumer(IScenarioCipherProvider.class, scenarioCipherProvider -> {
				try (IScenarioDataProvider scenarioDataProvider = CopiedCSVImporter.importCSVScenario(urlRoot.toString())) {
					run(options, scenarioDataProvider, monitor, completedHook, jsonOutput);
				}
			});
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}

	}

	public void runFromCsvZipFile(final File zipFile, final HeadlessApplicationOptions options, IProgressMonitor monitor, final BiConsumer<ScenarioModelRecord, IScenarioDataProvider> completedHook,
			HeadlessOptimiserJSON jsonOutput) {

		try {
			String zipUriString = String.format("archive:%s!", zipFile.toURI().toString());

			ServiceHelper.withCheckedOptionalServiceConsumer(IScenarioCipherProvider.class, scenarioCipherProvider -> {
				try (IScenarioDataProvider scenarioDataProvider = CopiedCSVImporter.importCSVScenario(zipUriString)) {
					run(options, scenarioDataProvider, monitor, completedHook, jsonOutput);
				}
			});
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}

	}
	
	/**
	 * Returns the optimisation plan specified in the optimiser configuration options, or generates one from the user settings object 
	 * if there are no stages in the specified plan, or generates one from the default user settings if there are no user settings 
	 * (this also occurs if the {@link OptimiserConfigurationOptions} are null).
	 * 
	 * @param ocOptions
	 * @param sdp
	 * @return
	 */
	public static @NonNull OptimiserConfigurationOptions getOrCreateConfigOptions(HeadlessApplicationOptions options, @NonNull final IScenarioDataProvider sdp) {
		final OptimiserConfigurationOptions result;
		
		if (options.algorithmConfigFile != null) {
			result = OptimiserConfigurationOptions.readFromFile(options.algorithmConfigFile, options.customInfo);						
		}
		else {
			result = new OptimiserConfigurationOptions();
			
			final UserSettings optionSettings = options.getUserSettingsContent();
			final @NonNull UserSettings userSettings = (optionSettings == null ? ScenarioUtils.createDefaultUserSettings() : optionSettings);
			result.plan = OptimisationHelper.transformUserSettings(userSettings, null, sdp.getTypedScenario(LNGScenarioModel.class));
			result.overrideNumThreads(LNGScenarioChainBuilder.getNumberOfAvailableCores());
			
		}		
		
		return result;
	}

	public boolean run(final HeadlessApplicationOptions options, @NonNull final IScenarioDataProvider sdp, IProgressMonitor monitor,
			final BiConsumer<ScenarioModelRecord, IScenarioDataProvider> completedHook, HeadlessOptimiserJSON jsonOutput) {

		final OptimiserConfigurationOptions ocOptions = getOrCreateConfigOptions(options, sdp);		
		
		final int num_threads = ocOptions.getNumThreads();

		OptimisationPlan optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(ocOptions.plan, true, false); // incorporate the settings from the parameterModesRegistry

		// Ensure dir structure is in place
		boolean exportLogs = options.isLoggingExportRequired();

		final JSONArray jsonStagesStorage = new JSONArray(); // array for output data at present

		if (jsonOutput != null) {
			jsonOutput.getMetrics().setStages(jsonStagesStorage);
		}

		// Create logging module
		final @NonNull CleanableExecutorService executorService = new SimpleCleanableExecutorService(Executors.newFixedThreadPool(num_threads), num_threads);

		final Map<String, LSOLogger> phaseToLoggerMap = new ConcurrentHashMap<>();

		final boolean doesActionSet = optimisationPlan.getStages().stream().anyMatch(stage -> (stage instanceof ActionPlanOptimisationStage));
		final ActionSetLogger actionSetLogger = doesActionSet ? new ActionSetLogger() : null; 
		
		try {

			final AbstractRunnerHook runnerHook = !exportLogs ? null : new AbstractRunnerHook() {

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

			final boolean doInjections = OptimiserConfigurationOptions.requiresInjections(ocOptions) || exportLogs;
			
			// Create logging module
			final IOptimiserInjectorService localOverrides = (doInjections == false) ? null : new IOptimiserInjectorService() {
				@Override
				public Module requestModule(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
					return null;
				}

				@Override
				@Nullable
				public List<@NonNull Module> requestModuleOverrides(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
					if (moduleType == ModuleType.Module_EvaluationParametersModule) {
						if (ocOptions.injections != null) {
							return Collections.<@NonNull Module>singletonList(new EvaluationSettingsOverrideModule(ocOptions.injections));
						}
					}
					if (moduleType == ModuleType.Module_OptimisationParametersModule) {
						return Collections.<@NonNull Module>singletonList(new OptimisationSettingsOverrideModule());
					}
					if (moduleType == ModuleType.Module_Optimisation) {
						final LinkedList<@NonNull Module> modules = new LinkedList<>();
						if (exportLogs) {
							modules.add(createLoggingModule(options.loggingParameters, phaseToLoggerMap, actionSetLogger, runnerHook));
						}
						if (ocOptions.injections != null) {
							modules.add(new LNGOptimisationOverrideModule(ocOptions.injections));
						}
						return modules;
					}
					return null;
				}
			};

			final List<String> hints = new LinkedList<>();
			hints.add(LNGTransformerHelper.HINT_OPTIMISE_LSO);

			try {

				if (jsonOutput != null) {
					jsonOutput.setOptimisationPlan(optimisationPlan);
				}
				
				final LNGOptimisationBuilder runnerBuilder = LNGOptimisationBuilder.begin(sdp) //
						.withRunnerHook(runnerHook) //
						.withOptimiserInjectorService(localOverrides) //
						.withHints(hints.toArray(new String[hints.size()])) //
						.withOptimisationPlan(optimisationPlan) //
						.withThreadCount(num_threads) //
				;
				if (localOverrides != null) {
					runnerBuilder.withOptimiserInjectorService(localOverrides);
				}
				final LNGScenarioRunner runner = runnerBuilder //
						.buildDefaultRunner() //
						.getScenarioRunner();

				final long startTime = System.currentTimeMillis();
				runner.evaluateInitialState();
				System.out.println("LNGResult(");
				System.out.println("\tscenario='" + options.scenarioFileName + "',");
				if (options.outputScenarioFileName != null) {
					System.out.println("\toutput='" + options.outputScenarioFileName + "',");
				}

				System.err.println("Starting run...");

				IMultiStateResult result = runner.runWithProgress(monitor);
				runner.applyBest(result);

				final long runTime = System.currentTimeMillis() - startTime;
				final Schedule finalSchedule = runner.getSchedule();
				if (finalSchedule == null) {
					System.err.println("Error optimising scenario");
				}

				System.out.println("\truntime=" + runTime + ",");
				if (jsonOutput != null) {
					jsonOutput.getMetrics().setRuntime(runTime);
				}

				System.err.println("Optimised!");

				if (options.isScenarioOutputRequired()) {
					// TODO: make this work when there is no parent file specified
					File parentDirectory = new File(options.outputScenarioFileName).getParentFile();
					
					if (parentDirectory != null) {
						parentDirectory.mkdirs();
					}

					saveScenario(options.outputScenarioFileName, sdp);
				}
				if (exportLogs) {
					exportData(phaseToLoggerMap, actionSetLogger, options.outputLoggingFolder, false);
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

	private void exportData(final Map<String, LSOLogger> loggerMap, final ActionSetLogger actionSetLogger, final String path, final boolean verbose) {
		// // first export logging data
		if (actionSetLogger != null) {
			System.out.println(verbose);
			if (!verbose)
				actionSetLogger.shortExport(Paths.get(path).toString(), "actionSets");
			else
				actionSetLogger.export(Paths.get(path).toString(), "action");
		}

		final PrintWriter writer = WriterFactory.getWriter(Paths.get(path, "machineData.txt").toString());
		writer.write(String.format("maxCPUs,%s", Runtime.getRuntime().availableProcessors()));
		writer.close();

	}

	/**
	 * Creates a module for providing logging parameters to the {@link Injector} framework. 
	 */
	@NonNull
	private Module createLoggingModule(LoggingParameters loggingParameters, final Map<String, LSOLogger> phaseToLoggerMap, final ActionSetLogger actionSetLogger, final AbstractRunnerHook runnerHook) {

		final LoggingModule loggingModule = new LoggingModule(phaseToLoggerMap, actionSetLogger, runnerHook, loggingParameters);
		return loggingModule;
	}

	protected void saveScenario(final String outputFile, final @NonNull IScenarioDataProvider sdp) throws IOException {

		ScenarioStorageUtil.storeCopyToFile(sdp, new File(outputFile));
	}

	/**
	 * Debug method to allow an optimisation plan to be written as JSON for visual
	 * inspection.
	 * 
	 * @param plan
	 * @param filename
	 */
	private void writeOptimisationPlan(OptimisationPlan plan, String filename) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.registerModule(new Jdk8Module());
			mapper.registerModule(new EMFJacksonModule());

			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), plan);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
