/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
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
import com.mmxlabs.lingo.app.headless.utils.IntegerParameter;
import com.mmxlabs.lingo.app.headless.utils.JSONParseResult;
import com.mmxlabs.lingo.app.headless.utils.LNGHeadlessParameters;
import com.mmxlabs.lingo.app.headless.utils.StringParameter;
import com.mmxlabs.models.lng.parameters.AnnealingSettings;
import com.mmxlabs.models.lng.parameters.IndividualSolutionImprovementSettings;
import com.mmxlabs.models.lng.parameters.OptimisationRange;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.lng.transformer.ui.AbstractRunnerHook;
import com.mmxlabs.models.lng.transformer.ui.IRunnerHook;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.migration.scenario.MigrationHelper;
import com.mmxlabs.optimiser.common.logging.ILoggingDataStore;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.logging.LSOLogger;
import com.mmxlabs.optimiser.lso.logging.LSOLoggingExporter;
import com.mmxlabs.rcp.common.viewfactory.ReplaceableViewManager;
import com.mmxlabs.scenario.service.manifest.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService.ModuleType;

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
		final String[] commandLineArgs = Platform.getCommandLineArgs();
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
		// set scenario file
		overrideSettings.setScenario(headlessParameters.getParameter("scenario", StringParameter.class).getValue());

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

		final OptimiserSettings optimiserSettings = rootObject.getParameters() == null ? ScenarioUtils.createDefaultSettings() : rootObject.getParameters();
		assert optimiserSettings != null;

		updateOptimiserSettings(rootObject, optimiserSettings, overrideSettings, headlessParameters);

		final String outputFolderName = overrideSettings.getOutputName() == null ? getFolderNameFromSettings(optimiserSettings) : getFolderNameFromSettings(overrideSettings);

		addFitnessTraceExporter(exporters, path, outputFolderName);
		addLatenessExporter(exporters, path, outputFolderName);

		final LNGScenarioRunner runner = new LNGScenarioRunner(rootObject, LNGScenarioRunner.createExtendedSettings(optimiserSettings), LNGTransformer.HINT_OPTIMISE_LSO);

		final Map<String, LSOLogger> phaseToLoggerMap = new HashMap<>();

		AbstractRunnerHook runnerHook = new AbstractRunnerHook() {
			@Override
			public void beginPhase(String phase, Injector injector) {
				super.beginPhase(phase, injector);
				for (IRunExporter exporter : exporters) {
					exporter.setPhase(phase, injector);
				}
			}

			@Override
			public void endPhase(String phase) {
				super.endPhase(phase);
				for (IRunExporter exporter : exporters) {
					exporter.exportData();
				}
			}
		};
		runner.setRunnerHook(runnerHook);

		final IOptimiserProgressMonitor monitor = new RunExporterProgressMonitor(exporters);

		// FIXME: Replace with an IParameterMode thing
		final EnumMap<ModuleType, List<Module>> localOverrides = Maps.newEnumMap(IOptimiserInjectorService.ModuleType.class);
		localOverrides.put(IOptimiserInjectorService.ModuleType.Module_ParametersModule, Collections.<Module> singletonList(new SettingsOverrideModule(overrideSettings)));

		// Create logging module
		final Module loggingModule = createLoggingModule(phaseToLoggerMap, runnerHook);
		runner.init(monitor, loggingModule, localOverrides);

		final long startTime = System.currentTimeMillis();
		runner.evaluateInitialState();
		System.out.println("LNGResult(");
		System.out.println("\tscenario='" + scenarioFile + "',");
		if (outputFile != null) {
			System.out.println("\toutput='" + outputFile + "',");
		}
		System.out.println("\titerations=" + optimiserSettings.getAnnealingSettings().getIterations() + ",");
		System.out.println("\tseed=" + optimiserSettings.getSeed() + ",");
		System.out.println("\tcooling=" + optimiserSettings.getAnnealingSettings().getCooling() + ",");
		System.out.println("\tepochLength=" + optimiserSettings.getAnnealingSettings().getEpochLength() + ",");
		System.out.println("\ttemp=" + optimiserSettings.getAnnealingSettings().getInitialTemperature() + ",");

		System.err.println("Starting run...");

		runner.run();

		final long runTime = System.currentTimeMillis() - startTime;
		final Schedule finalSchedule = runner.getFinalSchedule();
		if (finalSchedule == null) {
			System.err.println("Error optimising scenario");
		}

		System.out.println("\truntime=" + runTime + ",");

		System.err.println("Optimised!");

		if (outputFile != null) {
			saveScenario(Paths.get(path, outputFolderName, outputFile).toString(), instance);
		}

		exportData(phaseToLoggerMap, path, outputFolderName, jsonFilePath);

		return IApplication.EXIT_OK;

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

	private Module createLoggingModule(Map<String, LSOLogger> phaseToLoggerMap, AbstractRunnerHook runnerHook) {
		// final LSOLogger lsoLogger = new LSOLogger(1000);
		final LoggingModule loggingModule = new LoggingModule(phaseToLoggerMap, runnerHook, 1000);
		return loggingModule;
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

	private void updateOptimiserSettings(final LNGScenarioModel rootObject, final OptimiserSettings settings, final SettingsOverride settingsOverride, final HeadlessParameters headlessParameters) {
		// standard settings
		settings.setSeed(headlessParameters.getParameter("seed", IntegerParameter.class).getValue());
		// LSO settings
		final AnnealingSettings annealingSettings = settings.getAnnealingSettings();
		annealingSettings.setInitialTemperature(headlessParameters.getParameterValue("sa-temperature", Integer.class));
		annealingSettings.setEpochLength(headlessParameters.getParameterValue("sa-epoch-length", Integer.class));
		annealingSettings.setIterations(headlessParameters.getParameterValue("iterations", Integer.class));
		annealingSettings.setCooling(headlessParameters.getParameterValue("sa-cooling", Double.class));
		annealingSettings.setRestarting(headlessParameters.getParameterValue("restarting-useRestarting", Boolean.class));
		annealingSettings.setRestartIterationsThreshold(headlessParameters.getParameterValue("restarting-restartThreshold", Integer.class));
		setHillClimbingParameters(settings, headlessParameters);
		// Scenario settings
		settings.setShippingOnly(headlessParameters.getParameterValue("shippingonly-optimisation", Boolean.class));
		settings.setGenerateCharterOuts(headlessParameters.getParameterValue("generatedcharterouts-optimisation", Boolean.class));
		createObjectives(settings, headlessParameters.getParameterValue("objectives", DoubleMap.class));
		createDateRanges(settings, headlessParameters);
		setLatenessParameters(settingsOverride, headlessParameters);
		createPromptDates(rootObject, headlessParameters);
		setSimilarityParameters(settingsOverride, headlessParameters);
	}

	private void setHillClimbingParameters(final OptimiserSettings settings, final HeadlessParameters parameters) {
		final IndividualSolutionImprovementSettings solutionImprovementSettings = ParametersFactory.eINSTANCE.createIndividualSolutionImprovementSettings();
		solutionImprovementSettings.setImprovingSolutions(parameters.getParameterValue("hillClimbing-useHillClimbing", Boolean.class));
		solutionImprovementSettings.setIterations(parameters.getParameterValue("hillClimbing-iterations", Integer.class));
		settings.setSolutionImprovementSettings(solutionImprovementSettings);
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

	private void createDateRanges(final OptimiserSettings settings, final HeadlessParameters headlessParameters) {
		final YearMonth dateBefore = headlessParameters.getParameterValue("periodOptimisationDateBefore", YearMonth.class);
		final YearMonth dateAfter = headlessParameters.getParameterValue("periodOptimisationDateAfter", YearMonth.class);
		final OptimisationRange range = ParametersFactory.eINSTANCE.createOptimisationRange();
		if (dateBefore != null) {
			range.setOptimiseBefore(dateBefore);
		}
		if (dateAfter != null) {
			range.setOptimiseAfter(dateAfter);
		}
		settings.setRange(range);
	}

	private void createObjectives(final OptimiserSettings settings, final DoubleMap doubleMap) {
		final ParametersFactory parametersFactory = ParametersFactory.eINSTANCE;
		settings.getObjectives().clear();
		for (final String objectiveName : doubleMap.getDoubleMap().keySet()) {
			settings.getObjectives().add(ScenarioUtils.createObjective(parametersFactory, objectiveName, doubleMap.getDoubleMap().get(objectiveName)));
		}
	}

	private String getFolderNameFromSettings(final OptimiserSettings settings) {
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

	private Map<String, Object> getSettingsFileNameMap(final OptimiserSettings settings) {
		final Map<String, Object> nameMap = new LinkedHashMap<>();
		nameMap.put("seed", settings.getSeed());
		nameMap.put("iterations", settings.getAnnealingSettings().getIterations());
		nameMap.put("temperature", settings.getAnnealingSettings().getInitialTemperature());
		nameMap.put("epochLength", settings.getAnnealingSettings().getEpochLength());
		nameMap.put("cooling", settings.getAnnealingSettings().getCooling());
		nameMap.put("shippingOnly", settings.isShippingOnly());
		nameMap.put("gco", settings.isGenerateCharterOuts());
		return nameMap;
	}

	private void exportData(final Map<String, LSOLogger> loggerMap, final String path, final String foldername, final String jsonFilePath) {
		// first export logging data
		for (final String phase : IRunnerHook.PHASE_ORDER) {
			final LSOLogger logger = loggerMap.get(phase);
			if (logger != null) {
				final LSOLoggingExporter lsoLoggingExporter = new LSOLoggingExporter(path, phase, foldername, logger);
				lsoLoggingExporter.exportData("best-fitness", "current-fitness");
			}
		}

		HeadlessJSONParser.copyJSONFile(jsonFilePath, Paths.get(path, foldername, "parameters.json").toString());
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
