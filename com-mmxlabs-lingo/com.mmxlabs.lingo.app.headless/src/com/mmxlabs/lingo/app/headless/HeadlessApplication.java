/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.license.features.pluginxml.PluginRegistryHook;
import com.mmxlabs.license.ssl.LicenseChecker;
import com.mmxlabs.license.ssl.LicenseChecker.LicenseState;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserJSON;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserJSONTransformer;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserRunner;
import com.mmxlabs.rcp.common.viewfactory.ReplaceableViewManager;
import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionOptimiserLogger;

/**
 * Headless Optimisation Runner
 * 
 * @author Simon Goodall
 * 
 */

public class HeadlessApplication implements IApplication {

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

		HeadlessOptimiserRunner.Options options = new HeadlessOptimiserRunner.Options();
		if (!parseOptions(commandLineArgs, options)) {
			System.err.println("Error parsing the command line settings");
			return IApplication.EXIT_OK;
		}
		
		int threads = 1;
		
		HeadlessOptimiserJSON json = setOptionParamsInJSONOutputObject(options, options.scenarioFileName, threads);		
		
		File scenarioFile = new File(options.scenarioFileName);
		File outputFile = new File(options.outputLoggingFolder + "/" + UUID.randomUUID().toString() + ".json");
		runAndWriteResults(options, scenarioFile, outputFile, json);

		/*
		 * if (options.turnPerfOptsOn) { OptOptions.getInstance().setAllOnOff(options.turnPerfOptsOn); }
		 */
		/*
		{
			HeadlessOptimiserRunner runner = new HeadlessOptimiserRunner();
			try {
				runner.run(scenarioFile, options, new NullProgressMonitor(), null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		*/

		return IApplication.EXIT_OK;
	}
	
	private HeadlessOptimiserJSON setOptionParamsInJSONOutputObject(HeadlessOptimiserRunner.Options options, String scenarioFileName,
			int threads) {
		HeadlessOptimiserJSON json = HeadlessOptimiserJSONTransformer.createJSONResultObject();

		json.getMeta().setClient("V");
		json.getMeta().setScenario(scenarioFileName);
		//json.getMeta().setMachineType(getMachineInfo());
		json.getMeta().setVersion("Dev");

		json.getParams().setCores(threads);
		// json.getParams().getOptioniserProperties().setOptions(OptOptions.getInstance().toString());
		/*
		json.getParams().getOptioniserProperties().setIterations(options.iterations);
		json.getParams().getOptioniserProperties().setLoadIds(options.loadIds.toArray(new String[0]));
		json.getParams().getOptioniserProperties().setDischargeIds(options.dischargeIds.toArray(new String[0]));
		json.getParams().getOptioniserProperties().setEventIds(options.eventsIds.toArray(new String[0]));
		*/
		
		return json ;
	}

	private void runAndWriteResults(HeadlessOptimiserRunner.Options options, File scenarioFile, File outputFile, HeadlessOptimiserJSON json) throws Exception {
		SlotInsertionOptimiserLogger logger = new SlotInsertionOptimiserLogger();
		
		HeadlessOptimiserRunner runner = new HeadlessOptimiserRunner();
		runner.run(scenarioFile, options, new NullProgressMonitor(), null, json); //
		renameInvalidBsonFields(json.getMetrics().getStages());
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.registerModule(new Jdk8Module());
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
			//mapper.setDateFormat(df);
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			mapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
			
			mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, json);
		} catch (Exception e) {
			System.err.println("Error writing to file:");
			e.printStackTrace();
		}
		
	}
	
	private boolean isValidBsonFieldname(String name) {
		return name.matches("^[_0-9a-zA-Z\\-]+$");
	}
	
	private String makeValidBsonFieldname(String name) {
		// force only alphanumeric characters, underscore & hyphen
		String result = name.replaceAll("[^_a-zA-Z0-9\\-]", "_");
		
		// don't allow an empty string
		if (result.equals("")) {
			result = "_";
		}
		
		return result;

	}
	
	private void renameInvalidBsonFields(JSONObject object) {
		Set keys = object.keySet();
		
		List<Object> badNames = new LinkedList<>(); 				
		
		for (Object key: keys) {
			String name = key.toString();			
			Object value = object.get(key);

			if (!isValidBsonFieldname(name)) {
				badNames.add(key);
			}
			
			if (value instanceof JSONObject) {
				renameInvalidBsonFields((JSONObject) value);
			}
			else if (value instanceof JSONArray) {
				renameInvalidBsonFields((JSONArray) value);
			}
		}
		
		for (Object key: badNames) {
			String name = key.toString();
			String newName = makeValidBsonFieldname(name);
			if (object.containsKey(newName)) {
				throw new RuntimeException("Tried to ".format("Tried to rename invalid BSON name '%s' to '%s' but encountered a name collision.", name, newName));
			}
			else {
				object.put(newName, object.get(key));
				object.remove(key);
			}
			
		}
  
	}
	
	private void renameInvalidBsonFields(JSONArray object) {
		for (Object child: object) {
			if (child instanceof JSONObject) {
				renameInvalidBsonFields((JSONObject) child);
			}
			else if (child instanceof JSONArray) {
				renameInvalidBsonFields((JSONArray) child);
			}
		}
		
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
	private boolean parseOptions(final String[] commandLineArgs, final HeadlessOptimiserRunner.Options settings) {

		// create the Options
		final Options options = new Options();

		// Headless application options

		options.addOption(OptionBuilder.withLongOpt(INPUT_SCENARIO).withDescription("inut scenario file").hasArg().isRequired().create());
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
			final CommandLine line = parser.parse(options, commandLineArgs);
			if (line.hasOption(OUTPUT_SCENARIO)) {
				settings.outputScenarioFileName = line.getOptionValue(OUTPUT_SCENARIO);
			}
			if (line.hasOption(OUTPUT_FOLDER)) {
				settings.outputLoggingFolder = line.getOptionValue(OUTPUT_FOLDER);
			}
			if (line.hasOption(JSON)) {
				settings.jsonFile = line.getOptionValue(JSON);
			}
			if (line.hasOption(INPUT_SCENARIO)) {
				settings.scenarioFileName = line.getOptionValue(INPUT_SCENARIO);
			}

		} catch (final ParseException ex) {
			System.err.println("Bad option : " + ex.getMessage());
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Headless Application", options);
			return false;
		}

		return true;
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

}
