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
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.io.CharStreams;
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
		HeadlessUtils.initAccessControl();

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
			optionsList.addAll(mapper.readValue(new File(commandLine.getOptionValue(BATCH_FILE)), new TypeReference<List<HeadlessOptions>>() {
			}));
		} else {
			HeadlessOptions settings = new HeadlessOptions();

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

		for (HeadlessOptions hoptions : optionsList) {
			if (commandLine.hasOption(OUTPUT_FOLDER)) {
				hoptions.outputLoggingFolder = commandLine.getOptionValue(OUTPUT_FOLDER);
			}
			/*
			 * if (options.turnPerfOptsOn) { OptOptions.getInstance().setAllOnOff(options.turnPerfOptsOn); }
			 */
			File scenarioFile = new File(hoptions.scenarioFileName);
			{
				try {

					ObjectMapper mapper = new ObjectMapper();
					mapper.registerModule(new JavaTimeModule());
					mapper.registerModule(new Jdk8Module());

					HeadlessOptioniserRunner.Options options = mapper.readValue(new File(hoptions.jsonFile), HeadlessOptioniserRunner.Options.class);
					
					if (options.turnPerfOptsOn) {
						// OptOptions.getInstance().setAllOnOff(options.turnPerfOptsOn);
					}
					
					int numRuns = options.numRuns;
					int iterations = options.iterations;

					for (int run = 1; run <= numRuns; run++) {
						
						int startTry = (run - 1) * iterations; //every run should start at a different point.

						File outFile = new File(hoptions.outputLoggingFolder + "/" + UUID.randomUUID().toString() + ".json");
						try {
							int threads = 1; // we don't try anything complex with multiple threads
							HeadlessOptioniserJSON json = setOptionParamsInJSONOutputObject(options, scenarioFile, threads);							
							json.getMeta().setClient(clientCode);
							json.getMeta().setVersion(buildVersion);
							json.getMeta().setMachineType(machineInfo);
							
							runAndWriteResults(startTry, options, scenarioFile, outFile, json);
						}
						catch (Exception e) {
							outFile.delete();
							throw e;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return IApplication.EXIT_OK;
	}
	
	private void runAndWriteResults(int startTry, HeadlessOptioniserRunner.Options options, File scenarioFile, File outputFile, HeadlessOptioniserJSON json) throws Exception {
		SlotInsertionOptimiserLogger logger = new SlotInsertionOptimiserLogger();
		
		HeadlessOptioniserRunner runner = new HeadlessOptioniserRunner();
		runner.run(startTry, scenarioFile, logger, options, null);
		
		HeadlessOptioniserJSONTransformer.addRunResult(startTry, logger, json);

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
		Options options = HeadlessUtils.getRequiredOsgiOptions();

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

	private String getMachineInfo() {
		String machInfo = "AvailableProcessors:" + Integer.toString(Runtime.getRuntime().availableProcessors());
		return machInfo;
	}
}
