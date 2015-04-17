/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import com.google.common.collect.Maps;
import com.google.inject.Module;
import com.mmxlabs.lingo.app.headless.exporter.FitnessTraceExporter;
import com.mmxlabs.lingo.app.headless.exporter.IRunExporter;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.migration.scenario.MigrationHelper;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;
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
	private static final String SCENARIO = "scenario";
	private static final String OUTPUT = "output";
	private static final String ITERATIONS = "iterations";
	private static final String SEED = "seed";

	@Override
	public Object start(final IApplicationContext context) throws Exception {

		final List<IRunExporter> exporters = new LinkedList<IRunExporter>();
		final String[] commandLineArgs = Platform.getCommandLineArgs();
		final SettingsOverride settings = new SettingsOverride();
		if (!parseOptions(commandLineArgs, settings, exporters)) {
			return IApplication.EXIT_OK;
		}

		final String scenarioFile = settings.getScenario();
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

		final String outputFile = settings.getOutput();
		{

		}

		final OptimiserSettings optimiserSettings = rootObject.getPortfolioModel().getParameters() == null ? ScenarioUtils.createDefaultSettings() : rootObject.getPortfolioModel().getParameters();
		assert optimiserSettings != null;

		final LNGScenarioRunner runner = new LNGScenarioRunner(rootObject, LNGScenarioRunner.createExtendedSettings(optimiserSettings), LNGTransformer.HINT_OPTIMISE_LSO);

		final IOptimiserProgressMonitor monitor = new RunExporterProgressMonitor(exporters);

		// FIXME: Replace with an IParameterMode thing
		final EnumMap<ModuleType, List<Module>> localOverrides = Maps.newEnumMap(IOptimiserInjectorService.ModuleType.class);
		localOverrides.put(IOptimiserInjectorService.ModuleType.Module_ParametersModule, Collections.<Module> singletonList(new SettingsOverrideModule(settings)));

		runner.init(monitor, null, localOverrides);

		for (final IRunExporter exporter : exporters) {
			exporter.setScenarioRunner(runner);
		}
		runner.evaluateInitialState();

		System.out.println("LNGResult(");
		System.out.println("\tscenario='" + scenarioFile + "',");
		if (outputFile != null) {
			System.out.println("\toutput='" + outputFile + "',");
		}
		System.out.println("\titerations=" + settings.getIterations() + ",");
		System.out.println("\tseed=" + settings.getSeed() + ",");

		System.err.println("Starting run...");

		final long startTime = System.currentTimeMillis();

		runner.run();

		final long runTime = System.currentTimeMillis() - startTime;
		final Schedule finalSchedule = runner.getFinalSchedule();
		if (finalSchedule == null) {
			System.err.println("Error optimising scenario");
		}

		System.out.println("\truntime=" + runTime / 1000 + ",");

		System.err.println("Optimised!");

		if (outputFile != null) {
			saveScenario(outputFile, instance);
		}

		for (final IRunExporter exporter : exporters) {
			exporter.exportData();
		}

		return IApplication.EXIT_OK;

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
		options.addOption(OptionBuilder.withLongOpt(SCENARIO).withDescription("Input scenario file").hasArg().isRequired().create());
		options.addOption(ITERATIONS, true, "Number of iterations");
		options.addOption(SEED, true, "Random seed");

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
		options.addOption(
				"initialize",
				false,
				"(OSGi) initializes the configuration being run. All runtime related data structures and caches are refreshed. Any user/plug-in defined configuration data is not purged. No application is run, any product specifications are ignored and no UI is presented (e.g., the splash screen is not drawn)");
		options.addOption("install", true, "(OSGi) OSGi install area");
		options.addOption("keyring", true, "(OSGi) the location of the authorization database on disk. This argument has to be used together with the -password argument.");
		// various --launcher options

		options.addOption(
				"name",
				true,
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
		options.addOption(
				"showSplash",
				true,
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
			if (line.hasOption(SCENARIO)) {
				settings.setScenario(line.getOptionValue(SCENARIO));
			}
			if (line.hasOption(OUTPUT)) {
				settings.setOutput(line.getOptionValue(OUTPUT));
			}
			if (line.hasOption(ITERATIONS)) {
				settings.setIterations(Integer.parseInt(line.getOptionValue(ITERATIONS)));
			}
			if (line.hasOption(SEED)) {
				settings.setIterations(Integer.parseInt(line.getOptionValue(SEED)));
			}
			if (line.hasOption(FITNESS_TRACE)) {
				final FitnessTraceExporter exporter = new FitnessTraceExporter();
				exporter.setOutputFile(new File(line.getOptionValue(FITNESS_TRACE)));
				exporters.add(exporter);
			}
		} catch (final ParseException ex) {
			System.err.println("Bad option : " + ex.getMessage());
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Headless Application", options);
			return false;
		}

		return true;
	}
}
