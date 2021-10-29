/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessApplicationOptions;
import com.mmxlabs.models.lng.transformer.ui.headless.common.CustomTypeResolverBuilder;
import com.mmxlabs.models.lng.transformer.ui.headless.common.HeadlessRunnerUtils;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserJSON;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserJSONTransformer;
import com.mmxlabs.models.lng.transformer.ui.headless.optimiser.HeadlessOptimiserRunner;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

/**
 * Headless Optimisation Runner
 * 
 * @author Simon Goodall
 * 
 */

public class HeadlessOptimiserOneshotApplication extends HeadlessGenericApplication {

	protected static final String INPUT_SCENARIO = "scenario";
	protected static final String OUTPUT_SCENARIO = "outputScenario";
	protected static final String OUTPUT_FOLDER = "outputFolder";
	protected static final String PARAMS_FILE = "params";

	@Override
	public Object start(final IApplicationContext context) throws IOException {
		try {
			return dostart(context);
		} catch (Throwable t) {
			t.printStackTrace();
			return HeadlessGenericApplication.EXIT_CODE_EXCEPTION;
		}
	}

	public Object dostart(final IApplicationContext context) throws Exception {
		// get the command line
		readCommandLine();
		setupBasicFields();
		// check the license
		doCheckLicense();

		// log the user in and initialise related features
		HeadlessUtils.initAccessControl();

		final int numThreads = LNGScenarioChainBuilder.getNumberOfAvailableCores();

		final UserSettings userSettings;
		{
			final String paramsFile = commandLine.getOptionValue(PARAMS_FILE);
			final File f = new File(paramsFile);
			if (f.exists() && f.isFile()) {
				userSettings = getUserSettings(f);
			} else {
				userSettings = null;
			}
			if (userSettings == null) {
				throw new FileNotFoundException("Parameters file is missing");
			}
		}

		final String scenarioFilename = commandLine.getOptionValue(INPUT_SCENARIO);
		if (scenarioFilename == null || scenarioFilename.isEmpty()) {
			throw new FileNotFoundException("Scenario file is missing");

		}

		final File scenarioFile = new File(scenarioFilename);
		if (!(scenarioFile.exists() && scenarioFile.isFile())) {
			throw new FileNotFoundException("Scenario file is missing");
		}

		final String outputScenarioFileName = commandLine.getOptionValue(OUTPUT_SCENARIO);
		final String outputLoggingFolder = commandLine.getOptionValue(OUTPUT_FOLDER);

		File loggingFolder = null;
		if (outputLoggingFolder != null) {
			// Create logging folder, or throw an exception if there is a problem
			loggingFolder = new File(outputLoggingFolder);
			if (loggingFolder.exists() && !loggingFolder.isDirectory()) {
				throw new FileNotFoundException("Logging folder is a file not a folder");
			}
			if (!loggingFolder.exists()) {
				if (!loggingFolder.mkdirs()) {
					throw new FileNotFoundException("Unable to create folder");
				}
			}
		}

		final HeadlessOptimiserJSON json = (new HeadlessOptimiserJSONTransformer()).createJSONResultObject(getDefaultMachineInfo(), scenarioFile, numThreads);
//		writeMetaFields(json, scenarioFile, hOptions, numThreads);
		{
			json.getMeta().setCheckSum(mD5Checksum(scenarioFile));
//			json.getMeta().setUseCase(hOptions.useCase);
			json.getMeta().setClient(clientCode);
			json.getMeta().setVersion(buildVersion);
			json.getMeta().setMachineType(machineInfo);
//			json.getMeta().setCustomInfo(hOptions.customInfo);
			json.getMeta().setMaxHeapSize(Runtime.getRuntime().maxMemory());
			json.getParams().setCores(numThreads);
		}

		HeadlessOptimiserRunner runner = new HeadlessOptimiserRunner();

		ConsoleProgressMonitor monitor = new ConsoleProgressMonitor();

		boolean exportLogs = outputLoggingFolder != null;
		// Get the root object
		try {
			ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(scenarioFile.toURI().toURL(), (modelRecord, scenarioDataProvider) -> {
				runner.doRun(scenarioDataProvider, userSettings, exportLogs, outputScenarioFileName, outputLoggingFolder, json, numThreads, SubMonitor.convert(monitor));
			});
		} catch (Exception e) {
			throw new IOException("Error running optimisation", e);
		}

		HeadlessRunnerUtils.renameInvalidBsonFields(json.getMetrics().getStages());

		if (loggingFolder != null) {
			File outFile = new File(loggingFolder, UUID.randomUUID().toString() + ".json"); // create output log

			try {
				final ObjectMapper mapper = new ObjectMapper();
				mapper.registerModule(new JavaTimeModule());
				mapper.registerModule(new Jdk8Module());

				final CustomTypeResolverBuilder resolver = new CustomTypeResolverBuilder();
				resolver.init(JsonTypeInfo.Id.CLASS, null);
				resolver.inclusion(JsonTypeInfo.As.PROPERTY);
				resolver.typeProperty("@class");
				mapper.setDefaultTyping(resolver);

				mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
				mapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);

				mapper.writerWithDefaultPrettyPrinter().writeValue(outFile, json);
			} catch (final Exception e) {
				throw new IOException("Error writing log file", e);
			}
		}

		return IApplication.EXIT_OK;
	}

	@SuppressWarnings("static-access")
	protected CommandLine parseOptions(final String[] commandLineArgs) {

		// create the Options
		final Options options = HeadlessUtils.getRequiredOsgiOptions();

		// Headless application options

		options.addOption(OptionBuilder.withLongOpt(MACHINE_INFO).withDescription("JSON file containing machine info").hasArg().create());

		// TODO: Merge in json_versions branch and read from there
		options.addOption(OptionBuilder.withLongOpt(BUILD_VERSION).withDescription("Build Version").hasArg().create());
		options.addOption(OptionBuilder.withLongOpt(CLIENT_CODE).withDescription("Client code for build").hasArg().create());

		options.addOption(OptionBuilder.withLongOpt(INPUT_SCENARIO).withDescription("Input scenario file").hasArg().create());
		options.addOption(OptionBuilder.withLongOpt(PARAMS_FILE).withDescription("File listing a batch of jobs to run").hasArg().create());
//		options.addOption(OptionBuilder.withLongOpt(EXTRA_CONFIG_FILE).withDescription("File listing a batch of jobs to run").hasArg().create());

//		options.addOption(JSON, true, "JSON file containing parameters for algorithm being run");
//		
		options.addOption(OptionBuilder.withLongOpt(OUTPUT_SCENARIO).withDescription("Output scenario file").hasArg().create());
//		
		options.addOption(OptionBuilder.withLongOpt(OUTPUT_FOLDER).withDescription("Path to directory for output files").hasArg().create());
//		
//		
//		options.addOption(OptionBuilder.withLongOpt(CUSTOM_INFO).withDescription("Custom information (using name=val syntax)").hasArg().create());
//		options.addOption(OptionBuilder.withLongOpt(USE_CASE).withDescription("Use-case handle").hasArg().create());

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

	public static @Nullable UserSettings getUserSettings(final File file) throws IOException {

		if (!file.exists()) {
			return null;
		} else {

			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.registerModule(new Jdk8Module());
			mapper.enable(Feature.ALLOW_COMMENTS);

			return mapper.readValue(file, UserSettingsImpl.class);
		}

	}

	protected void runAndWriteResults(final int run, final HeadlessApplicationOptions hOptions, final File scenarioFile, final File outputFile, final int threads) throws Exception {
		throw new IllegalStateException("Unexpected code path");
	}

	@Override
	protected String getAlgorithmName() {
		return "optimisation";
	}

}
