/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.io.File;
import java.io.FileReader;
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
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.io.CharStreams;
import com.mmxlabs.license.ssl.LicenseChecker;
import com.mmxlabs.license.ssl.LicenseChecker.LicenseState;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserJSON;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserJSONTransformer;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserRunner;

/**
 * Headless Optimisation Runner
 * 
 * @author Simon Goodall
 * 
 */

public class HeadlessApplication implements IApplication {

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
		HeadlessUtils.initAccessControl();

		// read relevant settings from the command line
		String[] commandLineArgs = Platform.getCommandLineArgs();
		commandLineArgs = filterCommandLineArgs(commandLineArgs);

		final CommandLine commandLine = parseOptions(commandLineArgs);

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

		List<HeadlessOptimiserRunner.Options> optionsList = new LinkedList<>();
		if (commandLine.hasOption(BATCH_FILE)) {

			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.registerModule(new Jdk8Module());
			optionsList.addAll(mapper.readValue(new File(commandLine.getOptionValue(BATCH_FILE)), new TypeReference<List<HeadlessOptimiserRunner.Options>>() {
			}));
		} else {
			HeadlessOptimiserRunner.Options settings = new HeadlessOptimiserRunner.Options();

			if (commandLine.hasOption(OUTPUT_SCENARIO)) {
				settings.outputScenarioFileName = commandLine.getOptionValue(OUTPUT_SCENARIO);
			}
			if (commandLine.hasOption(JSON)) {
				settings.jsonFile = commandLine.getOptionValue(JSON);
			}
			if (commandLine.hasOption(INPUT_SCENARIO)) {
				settings.scenarioFileName = commandLine.getOptionValue(INPUT_SCENARIO);
			}

			optionsList.add(settings);
		}

		int threads = 1;

		for (HeadlessOptimiserRunner.Options hoptions : optionsList) {
			if (commandLine.hasOption(OUTPUT_FOLDER)) {
				hoptions.outputLoggingFolder = commandLine.getOptionValue(OUTPUT_FOLDER);
			}

			HeadlessOptimiserJSON json = setOptionParamsInJSONOutputObject(hoptions, hoptions.scenarioFileName, threads);

			File scenarioFile = new File(hoptions.scenarioFileName);
			File outputFile = new File(hoptions.outputLoggingFolder + "/" + UUID.randomUUID().toString() + ".json");
			runAndWriteResults(hoptions, scenarioFile, outputFile, json);
		}
		/*
		 * if (options.turnPerfOptsOn) { OptOptions.getInstance().setAllOnOff(options.turnPerfOptsOn); }
		 */
		/*
		 * { HeadlessOptimiserRunner runner = new HeadlessOptimiserRunner(); try { runner.run(scenarioFile, options, new NullProgressMonitor(), null, null); } catch (Exception e) {
		 * e.printStackTrace(); } }
		 */

		return IApplication.EXIT_OK;
	}

	private void runAndWriteResults(HeadlessOptimiserRunner.Options options, File scenarioFile, File outputFile, HeadlessOptimiserJSON json) throws Exception {

		HeadlessOptimiserRunner runner = new HeadlessOptimiserRunner();
		runner.run(scenarioFile, options, new NullProgressMonitor(), null, json); //
		renameInvalidBsonFields(json.getMetrics().getStages());

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.registerModule(new Jdk8Module());

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
			// mapper.setDateFormat(df);
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			mapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);

			mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, json);
		} catch (Exception e) {
			System.err.println("Error writing to file:");
			e.printStackTrace();
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
	private CommandLine parseOptions(final String[] commandLineArgs) {

		// create the Options
		final Options options = HeadlessUtils.getRequiredOsgiOptions();

		// Headless application options

		options.addOption(OptionBuilder.withLongOpt(MACHINE_INFO).withDescription("JSON file containing machine info").hasArg().create());
		options.addOption(OptionBuilder.withLongOpt(BUILD_VERSION).withDescription("Build Version").hasArg().create());
		options.addOption(OptionBuilder.withLongOpt(CLIENT_CODE).withDescription("Client code for build").hasArg().create());

		options.addOption(OptionBuilder.withLongOpt(BATCH_FILE).withDescription("file list a batch of jobs to run").hasArg().create());

		options.addOption(OptionBuilder.withLongOpt(INPUT_SCENARIO).withDescription("inut scenario file").hasArg().create());
		options.addOption(OptionBuilder.withLongOpt(OUTPUT_SCENARIO).withDescription("Output scenario file").hasArg().create());
		options.addOption(OptionBuilder.withLongOpt(OUTPUT_FOLDER).withDescription("Path to directory for output files").hasArg().create());
		// options.addOption(OptionBuilder.withLongOpt(OUTPUT_NAME).withDescription("Output folder name (e.g. job number, datetime, uuid)").hasArg().create());
		options.addOption(JSON, true, "json");

		// create the command line parser

		final CommandLineParser parser = new PosixParser();
		try {
			return parser.parse(options, commandLineArgs);
		} catch (final ParseException ex) {
			System.err.println("Bad option : " + ex.getMessage());
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Headless Application", options);
			return null;
		}
	}

	private HeadlessOptimiserJSON setOptionParamsInJSONOutputObject(HeadlessOptimiserRunner.Options options, String scenarioFileName, int threads) {
		HeadlessOptimiserJSON json = HeadlessOptimiserJSONTransformer.createJSONResultObject();

		json.getMeta().setClient("V");
		json.getMeta().setScenario(scenarioFileName);
		// json.getMeta().setMachineType(getMachineInfo());
		json.getMeta().setVersion("Dev");

		json.getParams().setCores(threads);
		// json.getParams().getOptioniserProperties().setOptions(OptOptions.getInstance().toString());
		/*
		 * json.getParams().getOptioniserProperties().setIterations(options.iterations); json.getParams().getOptioniserProperties().setLoadIds(options.loadIds.toArray(new String[0]));
		 * json.getParams().getOptioniserProperties().setDischargeIds(options.dischargeIds.toArray(new String[0])); json.getParams().getOptioniserProperties().setEventIds(options.eventsIds.toArray(new
		 * String[0]));
		 */

		return json;
	}

	private String getMachineInfo() {
		String machInfo = "AvailableProcessors:" + Integer.toString(Runtime.getRuntime().availableProcessors());
		return machInfo;
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
		if (object == null) {
			return;
		}
		
		Set keys = object.keySet();

		List<Object> badNames = new LinkedList<>();

		for (Object key : keys) {
			String name = key.toString();
			Object value = object.get(key);

			if (!isValidBsonFieldname(name)) {
				badNames.add(key);
			}

			if (value instanceof JSONObject) {
				renameInvalidBsonFields((JSONObject) value);
			} else if (value instanceof JSONArray) {
				renameInvalidBsonFields((JSONArray) value);
			}
		}

		for (Object key : badNames) {
			String name = key.toString();
			String newName = makeValidBsonFieldname(name);
			if (object.containsKey(newName)) {
				throw new RuntimeException("Tried to ".format("Tried to rename invalid BSON name '%s' to '%s' but encountered a name collision.", name, newName));
			} else {
				object.put(newName, object.get(key));
				object.remove(key);
			}

		}

	}

	private void renameInvalidBsonFields(JSONArray object) {
		for (Object child : object) {
			if (child instanceof JSONObject) {
				renameInvalidBsonFields((JSONObject) child);
			} else if (child instanceof JSONArray) {
				renameInvalidBsonFields((JSONArray) child);
			}
		}

	}
}
