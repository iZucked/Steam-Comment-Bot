/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.license.features.pluginxml.PluginRegistryHook;
import com.mmxlabs.license.ssl.LicenseChecker;
import com.mmxlabs.license.ssl.LicenseChecker.LicenseState;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessOptioniserJSON;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessOptioniserJSONTransformer;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessOptioniserRunner;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessOptions;
import com.mmxlabs.rcp.common.viewfactory.ReplaceableViewManager;
import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionOptimiserLogger;

/**
 * Headless Optimisation Runner
 * 
 * @author Simon Goodall
 * 
 */

public class HeadlessOptioniserApplication implements IApplication {

	private static final String MACHINE_INFO = "machineInfo";
	private static final String CLIENT_CODE = "clientCode";
	private static final String BUILD_VERSION = "buildId";
	private static final String BATCH_FILE = "batchfile";

	private static final String INPUT_SCENARIO = "scenario";
	private static final String OUTPUT_SCENARIO = "outputScenario";
	private static final String OUTPUT_FOLDER = "outputFolder";
	private static final String JSON = "json";

	@Override
	public Object start(final IApplicationContext context) throws Exception {

		// check to see if the user has a valid license
		final LicenseState validity = LicenseChecker.checkLicense();
		if (validity != LicenseState.Valid) {
			System.err.println("License is invalid");
			return IApplication.EXIT_OK;
		}

		// log the user in and initialise related features
		initAccessControl();

		// read relevant settings from the command line
		String[] commandLineArgs = Platform.getCommandLineArgs();
		commandLineArgs = filterCommandLineArgs(commandLineArgs);

		CommandLine commandLine = parseOptions(commandLineArgs);
		if (commandLine == null) {
			System.err.println("Error parsing the command line settings");
			return IApplication.EXIT_OK;
		}

		String machineInfo = getMachineInfo();
		String clientCode = "V";
		if (commandLine.hasOption(CLIENT_CODE)) {
			clientCode = commandLine.getOptionValue(CLIENT_CODE);
		}
		String buildVersion = "Dev";
		if (commandLine.hasOption(BUILD_VERSION)) {
			buildVersion = commandLine.getOptionValue(BUILD_VERSION);
		}
		if (commandLine.hasOption(MACHINE_INFO)) {
			machineInfo = CharStreams.toString(new FileReader(commandLine.getOptionValue(MACHINE_INFO)));
		}

		List<HeadlessOptions> optionsList = new LinkedList<>();
		if (commandLine.hasOption(BATCH_FILE)) {

			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.registerModule(new Jdk8Module());
			optionsList.addAll(mapper.readValue(new File(commandLine.getOptionValue(BATCH_FILE)), new TypeReference<List<HeadlessOptioniserRunner.Options>>() {
			}));
		} else {
			HeadlessOptions settings = new HeadlessOptions();

			if (commandLine.hasOption(OUTPUT_SCENARIO)) {
				settings.outputScenarioFileName = commandLine.getOptionValue(OUTPUT_SCENARIO);
			}
			if (commandLine.hasOption(OUTPUT_FOLDER)) {
				settings.outputLoggingFolder = commandLine.getOptionValue(OUTPUT_FOLDER);
			}
			if (commandLine.hasOption(JSON)) {
				settings.jsonFile = commandLine.getOptionValue(JSON);
			}
			if (commandLine.hasOption(INPUT_SCENARIO)) {
				settings.scenarioFileName = commandLine.getOptionValue(INPUT_SCENARIO);
			}

			optionsList.add(settings);
		}

		for (HeadlessOptions hoptions : optionsList) {
			/*
			 * if (options.turnPerfOptsOn) { OptOptions.getInstance().setAllOnOff(options.turnPerfOptsOn); }
			 */
			File scenarioFile = new File(hoptions.scenarioFileName);
			{
				HeadlessOptioniserRunner runner = new HeadlessOptioniserRunner();
				try {

					ObjectMapper mapper = new ObjectMapper();
					mapper.registerModule(new JavaTimeModule());
					mapper.registerModule(new Jdk8Module());

					HeadlessOptioniserRunner.Options options = mapper.readValue(new File(hoptions.jsonFile), HeadlessOptioniserRunner.Options.class);

					final int minThreads = options.minWorkerThreads;
					final int maxThreads = options.maxWorkerThreads;
					if (options.turnPerfOptsOn) {
						// OptOptions.getInstance().setAllOnOff(options.turnPerfOptsOn);
					}
					SlotInsertionOptimiserLogger logger = new SlotInsertionOptimiserLogger();

					runner.run(1, scenarioFile, logger, options, null);

					HeadlessOptioniserJSON json = setOptionParamsInJSONOutputObject(options, scenarioFile, options.minOptioniserThreads);
					json.getMeta().setClient(clientCode);
					json.getMeta().setVersion(buildVersion);
					json.getMeta().setMachineType(machineInfo);

					HeadlessOptioniserJSONTransformer.addRunResult(logger, json);

					try {
						ObjectMapper mapper1 = new ObjectMapper();
						mapper1.writerWithDefaultPrettyPrinter().writeValue(new File(hoptions.outputLoggingFolder + "/" + UUID.randomUUID().toString() + ".json"), json);
					} catch (Exception e) {
						System.err.println("Error writing to file:");
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
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
		final List<String> commandLine = new ArrayList<>(commandLineArgs.length);
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

	@Override
	public void stop() {
		// Nothing special needed
	}

	@SuppressWarnings("static-access")
	private CommandLine parseOptions(final String[] commandLineArgs) {

		// create the Options
		final Options options = new Options();

		// Headless application options

		options.addOption(OptionBuilder.withLongOpt(MACHINE_INFO).withDescription("JSON file containing machine info").hasArg().create());
		options.addOption(OptionBuilder.withLongOpt(BUILD_VERSION).withDescription("Build Version").hasArg().create());
		options.addOption(OptionBuilder.withLongOpt(CLIENT_CODE).withDescription("Client code for build").hasArg().create());

		options.addOption(OptionBuilder.withLongOpt(BATCH_FILE).withDescription("file list a batch of jobs to run").hasArg().create());

		options.addOption(OptionBuilder.withLongOpt(INPUT_SCENARIO).withDescription("input scenario file").hasArg().create());
		options.addOption(OptionBuilder.withLongOpt(OUTPUT_SCENARIO).withDescription("Output scenario file").hasArg().create());
		options.addOption(OptionBuilder.withLongOpt(OUTPUT_FOLDER).withDescription("Path to directory for output files").hasArg().create());
		// options.addOption(OptionBuilder.withLongOpt(OUTPUT_NAME).withDescription("Output folder name (e.g. job number, datetime, uuid)").hasArg().create());
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

		// create the command line parser

		final CommandLineParser parser = new PosixParser();
		try {
			return parser.parse(options, commandLineArgs);
		} catch (final ParseException ex) {
			System.err.println("Bad option : " + ex.getMessage());
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Headless Application", options);
		}

		return null;
	}

	/**
	 * Logs the user in and initialises the application for their use.
	 */
	private void initAccessControl() {
		// Initialise feature enablements
		LicenseFeatures.initialiseFeatureEnablements();

		// Login our default user
		final Subject subject = SecurityUtils.getSubject();
		subject.login(new UsernamePasswordToken("user", "password"));

		PluginRegistryHook.initialisePluginXMLEnablements();

		ReplaceableViewManager.initialiseReplaceableViews();
	}

	private HeadlessOptioniserJSON setOptionParamsInJSONOutputObject(HeadlessOptioniserRunner.Options options, File scenarioFile, int threads) {
		// Set up JSON object to write all results out with.
		HeadlessOptioniserJSON json = HeadlessOptioniserJSONTransformer.createJSONResultObject();
		json.getMeta().setClient("V");
		json.getMeta().setScenario(scenarioFile.getName());
		json.getMeta().setMachineType(getMachineInfo());
		json.getMeta().setVersion("Dev");

		json.getParams().setCores(threads);
		// json.getParams().getOptioniserProperties().setOptions(OptOptions.getInstance().toString());
		json.getParams().getOptioniserProperties().setIterations(options.iterations);
		json.getParams().getOptioniserProperties().setLoadIds(options.loadIds.toArray(new String[0]));
		json.getParams().getOptioniserProperties().setDischargeIds(options.dischargeIds.toArray(new String[0]));
		json.getParams().getOptioniserProperties().setEventIds(options.eventsIds.toArray(new String[0]));
		return json;
	}

	// private String getTimestamp() {
	// return sdfTimestamp.format(new Date());
	// }

	private String getMachineInfo() {
		String machInfo = "AvailableProcessors:" + Integer.toString(Runtime.getRuntime().availableProcessors());
		return machInfo;
	}

}
