/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.license.features.pluginxml.PluginRegistryHook;
import com.mmxlabs.license.ssl.LicenseChecker;
import com.mmxlabs.license.ssl.LicenseChecker.LicenseState;
import com.mmxlabs.lingo.app.headless.exporter.FitnessTraceExporter;
import com.mmxlabs.lingo.app.headless.exporter.GeneralAnnotationsExporter;
import com.mmxlabs.lingo.app.headless.exporter.IRunExporter;
import com.mmxlabs.lingo.app.headless.utils.DoubleMap;
import com.mmxlabs.lingo.app.headless.utils.HeadlessJSONParser;
import com.mmxlabs.lingo.app.headless.utils.HeadlessParameters;
import com.mmxlabs.lingo.app.headless.utils.JSONParseResult;
import com.mmxlabs.lingo.app.headless.utils.LNGHeadlessParameters;
import com.mmxlabs.lingo.app.headless.utils.StringParameter;
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParallelOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.AbstractRunnerHook;
import com.mmxlabs.models.lng.transformer.ui.ActionSetLogger;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunnerUtils;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.migration.scenario.MigrationHelper;
import com.mmxlabs.optimiser.lso.logging.LSOLogger;
import com.mmxlabs.optimiser.lso.logging.LSOLoggingExporter;
import com.mmxlabs.rcp.common.viewfactory.ReplaceableViewManager;
import com.mmxlabs.scenario.service.manifest.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
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

			return IApplication.EXIT_OK;
		}

		initAccessControl();

		final List<IRunExporter> exporters = new LinkedList<IRunExporter>();
		String[] commandLineArgs = Platform.getCommandLineArgs();
		commandLineArgs = filterCommandLineArgs(commandLineArgs);
		final SettingsOverride overrideSettings = new SettingsOverride();
		if (!parseOptions(commandLineArgs, overrideSettings, exporters)) {
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
		//Set Idle time levels
		overrideSettings.setIdleTimeLow(headlessParameters.getParameterValue("idle-time-low", Integer.class));
		overrideSettings.setIdleTimeHigh(headlessParameters.getParameterValue("idle-time-high", Integer.class));
		overrideSettings.setIdleTimeEnd(headlessParameters.getParameterValue("idle-time-end", Integer.class));
		// set scenario file
		overrideSettings
				.setScenario(headlessParameters.getParameter("scenario-path", StringParameter.class).getValue() + "/" + headlessParameters.getParameter("scenario", StringParameter.class).getValue());
		// Set verbose Logging
		overrideSettings.setActionPlanVerboseLogger(headlessParameters.getParameterValue("actionSets-verboseLogging", Boolean.class));
		final String scenarioFile = overrideSettings.getScenario();
		if (scenarioFile == null || scenarioFile.isEmpty()) {
			System.err.println("No scenario specified");
			return IApplication.EXIT_OK;
		}

		// Get the root object
		final ScenarioInstance instance = loadScenario(scenarioFile);
		if (instance == null) {
			System.err.println("Unable to load scenario");
			return IApplication.EXIT_OK;
		}

		final LNGScenarioModel rootObject = (LNGScenarioModel) instance.getInstance();

		if (rootObject == null) {
			System.err.println("Unable to load scenario");
			return IApplication.EXIT_OK;
		}

		final String outputFile = overrideSettings.getOutput();

		OptimisationPlan optimisationPlan = ScenarioUtils.createDefaultOptimisationPlan();
		assert optimisationPlan != null;
		optimisationPlan.getStages().clear();

		updateOptimiserSettings(rootObject, optimisationPlan, overrideSettings, headlessParameters);

		// Add in required extensions, but do not run custom hooks.
		optimisationPlan = LNGScenarioRunnerUtils.createExtendedSettings(optimisationPlan, true, false);

		assert overrideSettings.getOutputName() != null;//
		// final String outputFolderName = overrideSettings.getOutputName() == null ? getFolderNameFromSettings(optimisationPlan) : getFolderNameFromSettings(overrideSettings);
		final String outputFolderName = getFolderNameFromSettings(overrideSettings);

		// Ensure dir structure is in place
		Paths.get(path, outputFolderName).toFile().mkdirs();

		// final IOptimiserProgressMonitor monitor = new RunExporterProgressMonitor(exporters);

		// Create logging module
		final int no_threads = getNumThreads(headlessParameters);
		final @NonNull ExecutorService executorService = Executors.newFixedThreadPool(no_threads);

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
						return Collections.<@NonNull Module> singletonList(createLoggingModule(phaseToLoggerMap, actionSetLogger, runnerHook));
					}
					return null;
				}
			};

			final List<String> hints = new LinkedList<>();
			hints.add(LNGTransformerHelper.HINT_OPTIMISE_LSO);

			try {
				final LNGScenarioRunner runner = new LNGScenarioRunner(executorService, rootObject, null, optimisationPlan, LNGSchedulerJobUtils.createLocalEditingDomain(), null, localOverrides,
						runnerHook, false, hints.toArray(new String[hints.size()]));

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

				runner.run();

				final long runTime = System.currentTimeMillis() - startTime;
				final Schedule finalSchedule = runner.getSchedule();
				if (finalSchedule == null) {
					System.err.println("Error optimising scenario");
				}

				System.out.println("\truntime=" + runTime + ",");

				System.err.println("Optimised!");

				if (outputFile != null) {
					saveScenario(Paths.get(path, outputFolderName, outputFile).toString(), instance);
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
		return IApplication.EXIT_OK;

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
		final int processorsFromParams = headlessParameters.getParameterValue("actionSets-maxThreads", Integer.class);
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

	private ScenarioInstance loadScenario(final String scenarioFile) throws IOException {

		// Create instance and preload scenario.
		final ScenarioInstance instance = ScenarioStorageUtil.loadInstanceFromFile(scenarioFile);

		if (instance == null) {
			return null;
		}

		MigrationHelper.migrateAndLoad(instance);

		return instance;
	}

	private void saveScenario(final String outputFile, final ScenarioInstance instance) throws IOException {

		ScenarioStorageUtil.storeToFile(instance, new File(outputFile));
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

		if (headlessParameters.getParameterValue("do-clean-state", Boolean.class)) {
			final CleanStateOptimisationStage stage = ScenarioUtils.createDefaultCleanStateParameters(ScenarioUtils.createDefaultConstraintAndFitnessSettings());
			stage.setSeed(headlessParameters.getParameterValue("seed", Integer.class));

			stage.getAnnealingSettings().setIterations(headlessParameters.getParameterValue("clean-state-iterations", Integer.class));

			stage.getAnnealingSettings().setInitialTemperature(headlessParameters.getParameterValue("sa-temperature", Integer.class));
			stage.getAnnealingSettings().setEpochLength(headlessParameters.getParameterValue("sa-epoch-length", Integer.class));
			stage.getAnnealingSettings().setCooling(headlessParameters.getParameterValue("sa-cooling", Double.class));

			final ParallelOptimisationStage<CleanStateOptimisationStage> pStage = ParametersFactory.eINSTANCE.createParallelOptimisationStage();

			// Add to list to manipulate
			constraintsAndFitnesses.add(stage.getConstraintAndFitnessSettings());

			pStage.setTemplate(stage);
			pStage.setJobCount(headlessParameters.getParameterValue("clean-state-jobs", Integer.class));

			plan.getStages().add(pStage);
		}

		{
			final LocalSearchOptimisationStage stage = ScenarioUtils.createDefaultLSOParameters(ScenarioUtils.createDefaultConstraintAndFitnessSettings());
			stage.setSeed(headlessParameters.getParameterValue("seed", Integer.class));

			stage.getAnnealingSettings().setIterations(headlessParameters.getParameterValue("iterations", Integer.class));
			stage.getAnnealingSettings().setInitialTemperature(headlessParameters.getParameterValue("sa-temperature", Integer.class));
			stage.getAnnealingSettings().setEpochLength(headlessParameters.getParameterValue("sa-epoch-length", Integer.class));
			stage.getAnnealingSettings().setCooling(headlessParameters.getParameterValue("sa-cooling", Double.class));
			stage.getAnnealingSettings().setRestarting(headlessParameters.getParameterValue("restarting-useRestarting", Boolean.class));
			stage.getAnnealingSettings().setRestartIterationsThreshold(headlessParameters.getParameterValue("restarting-restartThreshold", Integer.class));

			final ParallelOptimisationStage<LocalSearchOptimisationStage> pStage = ParametersFactory.eINSTANCE.createParallelOptimisationStage();
			pStage.setTemplate(stage);
			pStage.setJobCount(headlessParameters.getParameterValue("lso-jobs", Integer.class));

			// Add to list to manipulate
			constraintsAndFitnesses.add(stage.getConstraintAndFitnessSettings());

			plan.getStages().add(pStage);
		}
		if (headlessParameters.getParameterValue("hillClimbing-useHillClimbing", Boolean.class)) {
			final HillClimbOptimisationStage stage = ScenarioUtils.createDefaultHillClimbingParameters(ScenarioUtils.createDefaultConstraintAndFitnessSettings());
			stage.getAnnealingSettings().setIterations(headlessParameters.getParameterValue("hillClimbing-iterations", Integer.class));
			stage.setSeed(headlessParameters.getParameterValue("seed", Integer.class));

			stage.getAnnealingSettings().setInitialTemperature(headlessParameters.getParameterValue("sa-temperature", Integer.class));
			stage.getAnnealingSettings().setEpochLength(headlessParameters.getParameterValue("sa-epoch-length", Integer.class));
			stage.getAnnealingSettings().setCooling(headlessParameters.getParameterValue("sa-cooling", Double.class));

			final ParallelOptimisationStage<HillClimbOptimisationStage> pStage = ParametersFactory.eINSTANCE.createParallelOptimisationStage();
			pStage.setTemplate(stage);
			// pStage.setJobCount(headlessParameters.getParameter("clean-state-jobs", Integer.class));

			// Add to list to manipulate
			constraintsAndFitnesses.add(stage.getConstraintAndFitnessSettings());

			plan.getStages().add(pStage);
		}

		if (headlessParameters.getParameterValue("actionSets-buildActionSets", Boolean.class)) {
			final ActionPlanOptimisationStage stage = ScenarioUtils.createDefaultActionPlanParameters(ScenarioUtils.createDefaultConstraintAndFitnessSettings());
			stage.setTotalEvaluations(headlessParameters.getParameterValue("actionSets-totalEvals", Integer.class));
			stage.setInRunEvaluations(headlessParameters.getParameterValue("actionSets-inRunEvals", Integer.class));
			stage.setSearchDepth(headlessParameters.getParameterValue("actionSets-maxSearchDepth", Integer.class));
			plan.getUserSettings().setBuildActionSets(headlessParameters.getParameterValue("actionSets-buildActionSets", Boolean.class));

			// Add to list to manipulate
			constraintsAndFitnesses.add(stage.getConstraintAndFitnessSettings());

			plan.getStages().add(stage);
		}

		// Scenario settings
		plan.getUserSettings().setShippingOnly(headlessParameters.getParameterValue("shippingonly-optimisation", Boolean.class));
		plan.getUserSettings().setGenerateCharterOuts(headlessParameters.getParameterValue("generatedcharterouts-optimisation", Boolean.class));
		// action sets

		createDateRanges(plan, headlessParameters);

		for (final ConstraintAndFitnessSettings settings : constraintsAndFitnesses) {
			createObjectives(settings, headlessParameters.getParameterValue("objectives", DoubleMap.class));
		}
		setLatenessParameters(settingsOverride, headlessParameters);
		setSimilarityParameters(settingsOverride, headlessParameters);
		setMoveOverrides(settingsOverride, headlessParameters);
		createPromptDates(rootObject, headlessParameters);
	}

	private void setMoveOverrides(final SettingsOverride settingsOverride, final HeadlessParameters headlessParameters) {
		settingsOverride.setMovesUseLoopingSCMG(headlessParameters.getParameterValue("moves-useLoopingSCMG", Boolean.class));
	}

	private void createPromptDates(final LNGScenarioModel rootObject, final HeadlessParameters parameters) {
		final LocalDate promptStart = parameters.getParameterValue("promptStart", LocalDate.class);
		final LocalDate promptEnd = parameters.getParameterValue("promptEnd", LocalDate.class);
		if (promptStart != null) {
			rootObject.setPromptPeriodStart(promptStart);
		} else {
			rootObject.unsetPromptPeriodStart();
		}
		if (promptEnd != null) {
			rootObject.setPromptPeriodEnd(promptEnd);
		} else {
			rootObject.unsetPromptPeriodEnd();
		}
	}

	private void setLatenessParameters(final SettingsOverride overrideSettings, final HeadlessParameters headlessParameters) {
		final Map<String, Integer> latenessParameterMap = new HashMap<String, Integer>();
		for (final String latenessKey : SettingsOverride.latenessComponentParameters) {
			latenessParameterMap.put(latenessKey, headlessParameters.getParameterValue(latenessKey, Integer.class));
		}
		overrideSettings.setlatenessParameterMap(latenessParameterMap);
	}

	private void setSimilarityParameters(final SettingsOverride overrideSettings, final HeadlessParameters headlessParameters) {
		final Map<String, Integer> similarityParameterMap = new HashMap<String, Integer>();
		for (final String key : SettingsOverride.similarityComponentParameters) {
			similarityParameterMap.put(key, headlessParameters.getParameterValue(key, Integer.class));
		}
		overrideSettings.setSimilarityParameterMap(similarityParameterMap);
	}

	private void createDateRanges(final OptimisationPlan plan, final HeadlessParameters headlessParameters) {
		final YearMonth dateBefore = headlessParameters.getParameterValue("periodOptimisationDateBefore", YearMonth.class);
		final YearMonth dateAfter = headlessParameters.getParameterValue("periodOptimisationDateAfter", YearMonth.class);
		if (dateBefore != null) {
			plan.getUserSettings().setPeriodEnd(dateBefore);
		}
		if (dateAfter != null) {
			plan.getUserSettings().setPeriodStart(dateAfter);
		}
	}

	private void createObjectives(final ConstraintAndFitnessSettings settings, final DoubleMap doubleMap) {
		settings.getObjectives().clear();
		for (final String objectiveName : doubleMap.getDoubleMap().keySet()) {
			settings.getObjectives().add(ScenarioUtils.createObjective(objectiveName, doubleMap.getDoubleMap().get(objectiveName)));
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
