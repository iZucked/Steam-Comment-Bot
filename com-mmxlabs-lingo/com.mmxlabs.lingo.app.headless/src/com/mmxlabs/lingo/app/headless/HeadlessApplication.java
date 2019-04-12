/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.concurrent.CleanableExecutorService;
import com.mmxlabs.common.concurrent.SimpleCleanableExecutorService;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.license.features.pluginxml.PluginRegistryHook;
import com.mmxlabs.license.ssl.LicenseChecker;
import com.mmxlabs.license.ssl.LicenseChecker.LicenseState;
import com.mmxlabs.lingo.app.headless.exporter.FitnessTraceExporter;
import com.mmxlabs.lingo.app.headless.exporter.GeneralAnnotationsExporter;
import com.mmxlabs.lingo.app.headless.exporter.IRunExporter;
import com.mmxlabs.lingo.app.headless.utils.HeadlessJSONParser;
import com.mmxlabs.lingo.app.headless.utils.HeadlessParameters;
import com.mmxlabs.lingo.app.headless.utils.JMap;
import com.mmxlabs.lingo.app.headless.utils.JSONParseResult;
import com.mmxlabs.lingo.app.headless.utils.LNGHeadlessParameters;
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
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.optimiser.lso.logging.LSOLogger;
import com.mmxlabs.optimiser.lso.logging.LSOLoggingExporter;
import com.mmxlabs.rcp.common.viewfactory.ReplaceableViewManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

/**
 * Note duplication with various bits of ITS including ScenarioRunner and MigrationHelper
 * 
 * @author Simon Goodall
 * 
 */

public class HeadlessApplication implements IApplication {

	private static final String FITNESS_TRACE = "exportFitnessTrace";
	private static final String OUTPUT = "output";
	private static final String OUTPUT_PATH = "outputPath";
	private static final String OUTPUT_NAME = "outputFolderName";
	private static final String JSON = "json";

	@Override
	public Object start(final IApplicationContext context) throws Exception {

		final LicenseState validity = LicenseChecker.checkLicense();
		if (validity != LicenseState.Valid) {
			System.err.println("Licence is invalid");
			return IApplication.EXIT_OK;
		}

		initAccessControl();

		final List<IRunExporter> exporters = new LinkedList<IRunExporter>();
		String[] commandLineArgs = Platform.getCommandLineArgs();
		commandLineArgs = filterCommandLineArgs(commandLineArgs);
		final SettingsOverride overrideSettings = new SettingsOverride();
		if (!parseOptions(commandLineArgs, overrideSettings, exporters)) {
			System.err.println("Error parsing the command line settings");
			return IApplication.EXIT_OK;
		}

		final String jsonFilePath = overrideSettings.getJSON();
		final Pair<JSONParseResult, LNGHeadlessParameters> jsonParse = setParametersFromJSON(overrideSettings, jsonFilePath);
		if (!jsonParse.getFirst().allRequirementsPassed()) {
			// json parse failed
			for (final String error : jsonParse.getFirst().getRequiredMissingText()) {
				System.err.println(error);
			}
			return IApplication.EXIT_OK;
		}
		final LNGHeadlessParameters headlessParameters = jsonParse.getSecond();
		// set output file
		final String path = overrideSettings.getOutputPath();
		// Set Idle time levels
		// overrideSettings.setIdleTimeLow(headlessParameters.getParameterValue("idle-time-low", Integer.class));
		// overrideSettings.setIdleTimeHigh(headlessParameters.getParameterValue("idle-time-high", Integer.class));
		// overrideSettings.setIdleTimeEnd(headlessParameters.getParameterValue("idle-time-end", Integer.class));
		// set scenario file
		overrideSettings.setScenario(headlessParameters.getParameterValue("scenario-path", String.class) + "/" + headlessParameters.getParameterValue("scenario", String.class));
		// Set verbose Logging
		// overrideSettings.setActionPlanVerboseLogger(headlessParameters.getParameterValue("actionSets-verboseLogging", Boolean.class));
		// overrideSettings.setUseRouletteWheel(headlessParameters.getParameterValue("use-roulette-wheel", Boolean.class));
		// overrideSettings.setUseLegacyCheck(headlessParameters.getParameterValue("use-legacy-check", Boolean.class));
		// overrideSettings.setUseGuidedMoves(headlessParameters.getParameterValue("use-guided-moves", Boolean.class));
		final String scenarioFile = overrideSettings.getScenario();
		if (scenarioFile == null || scenarioFile.isEmpty()) {
			System.err.println("No scenario specified");
			return IApplication.EXIT_OK;
		}

		// Get the root object
		return ScenarioStorageUtil.withExternalScenarioFromResourceURL(new File(scenarioFile).toURL(), (modelRecord, scenarioDataProvider) -> {
			if (modelRecord == null) {
				System.err.println("Unable to load scenario");
				return Integer.valueOf(IApplication.EXIT_OK);
			}

			if (scenarioDataProvider.getScenario() == null) {
				System.err.println("Unable to load scenario");
				return Integer.valueOf(IApplication.EXIT_OK);
			}

			final String outputFile = overrideSettings.getOutput();

			OptimisationPlan optimisationPlan = ScenarioUtils.createDefaultOptimisationPlan();
			assert optimisationPlan != null;
			optimisationPlan.getStages().clear();

			updateOptimiserSettings((LNGScenarioModel) scenarioDataProvider.getScenario(), optimisationPlan, overrideSettings, headlessParameters);

			// Add in required extensions, but do not run custom hooks.
			optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(optimisationPlan, true, false);

			assert overrideSettings.getOutputName() != null;//
			// final String outputFolderName = overrideSettings.getOutputName() == null ? getFolderNameFromSettings(optimisationPlan) : getFolderNameFromSettings(overrideSettings);
			final String outputFolderName = getFolderNameFromSettings(overrideSettings);

			// Ensure dir structure is in place
			Paths.get(path, outputFolderName).toFile().mkdirs();

			// final IOptimiserProgressMonitor monitor = new RunExporterProgressMonitor(exporters);

			// Create logging module
			final int num_threads = getNumThreads(headlessParameters);
			final @NonNull CleanableExecutorService executorService = new SimpleCleanableExecutorService(Executors.newFixedThreadPool(num_threads), num_threads);

			final Map<String, LSOLogger> phaseToLoggerMap = new ConcurrentHashMap<>();

			final ActionSetLogger actionSetLogger = optimisationPlan.getUserSettings().isBuildActionSets() ? new ActionSetLogger() : null;
			// System.out.println("DEBUGGER");
			try {

				final AbstractRunnerHook runnerHook = new AbstractRunnerHook() {

					@Override
					protected void doEndStageJob(@NonNull final String stage, final int jobID, @Nullable final Injector injector) {

						final String stageAndJobID = getStageAndJobID();
						final LSOLogger logger = phaseToLoggerMap.remove(stageAndJobID);
						if (logger != null) {
							final LSOLoggingExporter lsoLoggingExporter = new LSOLoggingExporter(path, stageAndJobID, outputFolderName, logger);
							lsoLoggingExporter.exportData("best-fitness", "current-fitness");
						}
						// exportData(phaseToLoggerMap, actionSetLogger, path, outputFolderName, jsonFilePath);
					}

					// @Override
					// public void reportSequences(String phase, final ISequences rawSequences) {
					// switch (phase) {
					//
					// case IRunnerHook.PHASE_LSO:
					// case IRunnerHook.PHASE_HILL:
					// case IRunnerHook.PHASE_INITIAL:
					// // save(rawSequences, phase);
					// break;
					// case IRunnerHook.PHASE_ACTION_SETS:
					// break;
					// }
					// }
					//
					// @Override
					// public ISequences getPrestoredSequences(String phase) {
					// switch (phase) {
					// case IRunnerHook.PHASE_LSO:
					// case IRunnerHook.PHASE_HILL:
					// // return load(phase);
					// case IRunnerHook.PHASE_INITIAL:
					// case IRunnerHook.PHASE_ACTION_SETS:
					// break;
					//
					// }
					// return null;
					// }
					//
					// private void save(final @NonNull ISequences rawSequences, final @NonNull String type) {
					// try {
					// final String suffix = instance.getName() + "." + type + ".sequences";
					// final File file2 = new File("/home/ubuntu/scenarios/" + suffix);
					// // final File file2 = new File("c:\\Temp1\\" + suffix);
					// try (FileOutputStream fos = new FileOutputStream(file2)) {
					// final Injector injector = getInjector();
					// assert (injector != null);
					// SequencesSerialiser.save(injector.getInstance(IOptimisationData.class), rawSequences, fos);
					// }
					// } catch (final Exception e) {
					// }
					// }
					//
					// @Nullable
					// private ISequences load(final @NonNull String type) {
					// try {
					// final String suffix = instance.getName() + "." + type + ".sequences";
					// final File file2 = new File("/home/ubuntu/scenarios/" + suffix);
					// // final File file2 = new File("c:\\Temp1\\" + suffix);
					// try (FileInputStream fos = new FileInputStream(file2)) {
					// final Injector injector = getInjector();
					// assert (injector != null);
					// return SequencesSerialiser.load(injector.getInstance(IOptimisationData.class), fos);
					// }
					// } catch (final Exception e) {
					// }
					// return null;
					// }

				};

				// Create logging module

				// FIXME: Replace with an IParameterMode thing
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
							final LinkedList<@NonNull Module> modules = new LinkedList<@NonNull Module>();
							modules.add(createLoggingModule(phaseToLoggerMap, actionSetLogger, runnerHook));
							modules.add(new LNGOptimsationOverrideModule(overrideSettings));
							return modules;
						}
						return null;
					}
				};

				final List<String> hints = new LinkedList<>();
				hints.add(LNGTransformerHelper.HINT_OPTIMISE_LSO);

				try {

					final LNGScenarioRunner runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
							.withRunnerHook(runnerHook) //
							.withOptimiserInjectorService(localOverrides) //
							.withHints(hints.toArray(new String[hints.size()])) //
							.withOptimisationPlan(optimisationPlan) //
							.withThreadCount(num_threads) //
							.buildDefaultRunner() //
							.getScenarioRunner();

					// final LNGScenarioRunner runner = new LNGScenarioRunner(executorService, scenarioDataProvider, null, optimisationPlan,
					// scenarioDataProvider.getEditingDomain(), null, localOverrides,
					// runnerHook, false, hints.toArray(new String[hints.size()]));

					// FIXME
					// runner.init(monitor);

					final long startTime = System.currentTimeMillis();
					runner.evaluateInitialState();
					System.out.println("LNGResult(");
					System.out.println("\tscenario='" + scenarioFile + "',");
					if (outputFile != null) {
						System.out.println("\toutput='" + outputFile + "',");
					}
					// System.out.println("\titerations=" + optimiserSettings.getAnnealingSettings().getIterations() + ",");
					// System.out.println("\tseed=" + optimiserSettings.getSeed() + ",");
					// System.out.println("\tcooling=" + optimiserSettings.getAnnealingSettings().getCooling() + ",");
					// System.out.println("\tepochLength=" + optimiserSettings.getAnnealingSettings().getEpochLength() + ",");
					// System.out.println("\ttemp=" + optimiserSettings.getAnnealingSettings().getInitialTemperature() + ",");

					System.err.println("Starting run...");

					runner.runAndApplyBest();

					final long runTime = System.currentTimeMillis() - startTime;
					final Schedule finalSchedule = runner.getSchedule();
					if (finalSchedule == null) {
						System.err.println("Error optimising scenario");
					}

					System.out.println("\truntime=" + runTime + ",");

					System.err.println("Optimised!");

					if (outputFile != null) {
						saveScenario(Paths.get(path, outputFolderName, outputFile).toString(), modelRecord);
					}

					exportData(phaseToLoggerMap, actionSetLogger, path, outputFolderName, jsonFilePath, overrideSettings.isActionPlanVerboseLogger());
				} catch (final Exception e) {
					System.out.println("Headless Error");
					System.err.println("Headless Error:" + e.getMessage());
					e.printStackTrace();
				}

			} finally {
				executorService.shutdownNow();
			}
			return Integer.valueOf(IApplication.EXIT_OK);
		});
	}

	/**
	 * Filter out invalid command line items that getopt cannot work with
	 * 
	 * @param commandLineArgs
	 * @return
	 */
	private String[] filterCommandLineArgs(final String[] commandLineArgs) {
		final List<String> commandLine = new ArrayList<String>(commandLineArgs.length);
		int skip = 0;
		for (final String arg : commandLineArgs) {
			if (skip != 0) {
				--skip;
				continue;
			}
			if (arg.equals("-eclipse.keyring")) {
				skip = 1;
				continue;
			}
			if (arg.equals("-eclipse.password")) {
				skip = 1;
				continue;
			}
			commandLine.add(arg);

		}
		return commandLine.toArray(new String[commandLine.size()]);
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

	private void addFitnessTraceExporter(final List<IRunExporter> exporters, final String path, final String outputFolderName) {
		final FitnessTraceExporter exporter = new FitnessTraceExporter();
		addExporter(exporters, path, outputFolderName, "fitnessTrace.txt", exporter);
	}

	private void addExporter(final List<IRunExporter> exporters, final String path, final String outputFolderName, final String filename, final IRunExporter exporter) {
		exporter.setOutputFile(Paths.get(path, outputFolderName), filename);
		exporters.add(exporter);
	}

	private void addLatenessExporter(final List<IRunExporter> exporters, final String path, final String outputFolderName) {
		final GeneralAnnotationsExporter exporter = new GeneralAnnotationsExporter();
		addExporter(exporters, path, outputFolderName, "generalAnnotationsReport.txt", exporter);
	}

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

	@Override
	public void stop() {

	}

	@SuppressWarnings("static-access")
	private boolean parseOptions(final String[] commandLineArgs, final SettingsOverride settings, final List<IRunExporter> exporters) {

		// create the Options
		final Options options = new Options();

		options.addOption(OptionBuilder.withLongOpt(OUTPUT).withDescription("Output scenario file").hasArg().create());
		options.addOption(OptionBuilder.withLongOpt(OUTPUT_PATH).withDescription("Path to directory for output files").hasArg().isRequired().create());
		options.addOption(OptionBuilder.withLongOpt(OUTPUT_NAME).withDescription("Output folder name (e.g. job number, datetime, uuid)").hasArg().create());
		options.addOption(JSON, true, "json");

		// Options for OSGi/Eclipse compat - not used, but needs to be specified
		options.addOption("application", true, "(OSGi) Application ID");
		options.addOption("arch", true, "(OSGi) Set arch");
		options.addOption("clean", false, "(OSGi) Clean any framework cached data");
		options.addOption("configuration", true, "(OSGi) OSGi configuration area");
		options.addOption(OptionBuilder.withLongOpt("console").withDescription("[port] (OSGi) Enable OSGi Console").hasOptionalArg().create());
		options.addOption("consoleLog", false, "(OSGi) Enable logging to console");
		options.addOption("data", true, "(OSGi) OSGi instance area");
		options.addOption(OptionBuilder.withLongOpt("debug").withDescription("[options file] (OSGi) debug mode").hasOptionalArg().create());
		options.addOption(OptionBuilder.withLongOpt("dev").withDescription("[entires] (OSGi) dev mode").hasOptionalArg().create());
		// options.addOption("eclipse.keyring", true, "(Equinox) Set to override location of the default secure storage");
		// options.addOption("eclipse.password", true,
		// "(Equinox) If specified, the secure storage treats contents of the file as a default password. When not set, password providers are used to obtain a password.");
		options.addOption("feature", true, "(Equinox) equivalent to setting eclipse.product to <feature id>");
		options.addOption("framework", true, "(Equinox) equivalent to setting osgi.framework to <location>");
		options.addOption("initialize", false,
				"(OSGi) initializes the configuration being run. All runtime related data structures and caches are refreshed. Any user/plug-in defined configuration data is not purged. No application is run, any product specifications are ignored and no UI is presented (e.g., the splash screen is not drawn)");
		options.addOption("install", true, "(OSGi) OSGi install area");
		options.addOption("keyring", true, "(OSGi) the location of the authorization database on disk. This argument has to be used together with the -password argument.");
		// various --launcher options

		options.addOption("name", true,
				"(OSGi) The name to be displayed in the task bar item for the splash screen when the application starts up (not applicable on Windows). Also used as the title of error dialogs opened by the launcher. When not set, the name is the name of the executable.");

		options.addOption("nl", true, "(OSGi) equivalent to setting osgi.nl to <locale>");
		options.addOption("noExit", false, "(OSGi) equivalent to setting osgi.noShutdown to \"true\"");
		options.addOption("noLazyRegistryCacheLoading", false, "(OSGi) equivalent to setting eclipse.noLazyRegistryCacheLoading to \"true\"");
		options.addOption("noRegistryCache", false, "(OSGi)equivalent to setting eclipse.noRegistryCache to \"true\"");
		options.addOption("nosplash", false, "(OSGi) Disable splash screen");
		options.addOption("os", true, "(OSGi) equivalent to setting osgi.os to <operating system>");
		options.addOption("password", true, "(OSGi) the password for the authorization database");
		options.addOption("pluginCustomization", true, "(OSGi) equivalent to setting eclipse.pluginCustomization to <location>");
		options.addOption("product", true, "(OSGi) Product ID");

		options.addOption("registryMultiLanguage", false, "(OSGi) equivalent to setting eclipse.registry.MultiLanguage to \"true\"");
		options.addOption("showSplash", true,
				"(OSGi) The location of jar used to startup eclipse. The jar referred to should have the Main-Class attribute set to org.eclipse.equinox.launcher.Main. If this parameter is not set, the executable will look in the plugins directory for theorg.eclipse.equinox.launcher bundle with the highest version.");

		options.addOption("startup", true, "(OSGi) Set location of startup jar");
		options.addOption("user", true, "(OSGi) OSGi user area");
		options.addOption("vm", true, "(OSGi) used to locate the Java VM");
		options.addOption(OptionBuilder.withLongOpt("vmargs").withDescription("(OSGi) Java VM arguments").hasArgs().create());
		options.addOption("ws", true, "(OSGi) Set window system");

		// Memory command line args (Not used by headless, but added to maintain compat with main laucher arg set.
		options.addOption("automem", false, "(LiNGO) Automatically determine upper bound for heap size");
		options.addOption("noautomem", false, "(LiNGO) Do not automatically determine upper bound for heap size");

		// Enable Fitness Exporter
		options.addOption(OptionBuilder.withLongOpt(FITNESS_TRACE).isRequired(false).withDescription("Export fitness trace to a file").hasArg().create());

		// create the command line parser
		final CommandLineParser parser = new PosixParser();
		try {
			final CommandLine line = parser.parse(options, commandLineArgs);
			if (line.hasOption(OUTPUT)) {
				settings.setOutput(line.getOptionValue(OUTPUT));
			}
			if (line.hasOption(OUTPUT_PATH)) {
				settings.setOutputPath(line.getOptionValue(OUTPUT_PATH));
			}
			if (line.hasOption(JSON)) {
				settings.setJSON(line.getOptionValue(JSON));
			}
			if (line.hasOption(OUTPUT_NAME)) {
				settings.setOutputName(line.getOptionValue(OUTPUT_NAME));
			}

		} catch (final ParseException ex) {
			System.err.println("Bad option : " + ex.getMessage());
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Headless Application", options);
			return false;
		}

		return true;
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

	private String getFolderNameFromSettings(final SettingsOverride settings) {
		return settings.getOutputName();
	}

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

	private void exportData(final Map<String, LSOLogger> loggerMap, final ActionSetLogger actionSetLogger, final String path, final String foldername, final String jsonFilePath,
			final boolean verbose) {
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
				actionSetLogger.shortExport(Paths.get(path, foldername).toString(), "actionSets");
			else
				actionSetLogger.export(Paths.get(path, foldername).toString(), "action");
		}
		HeadlessJSONParser.copyJSONFile(jsonFilePath, Paths.get(path, foldername, "parameters.json").toString());

		final PrintWriter writer = WriterFactory.getWriter(Paths.get(path, foldername, "machineData.txt").toString());
		writer.write(String.format("maxCPUs,%s", Runtime.getRuntime().availableProcessors()));
		writer.close();

	}

	private void initAccessControl() {
		// Initialise feature enablements
		LicenseFeatures.initialiseFeatureEnablements();

		// Login our default user
		final Subject subject = SecurityUtils.getSubject();
		subject.login(new UsernamePasswordToken("user", "password"));

		PluginRegistryHook.initialisePluginXMLEnablements();

		ReplaceableViewManager.initialiseReplaceableViews();
	}

}
