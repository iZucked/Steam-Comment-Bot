/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessApplicationOptions;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessGenericJSON.Meta;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessGenericJSON.ScenarioMeta;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.AbstractJobRunner;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.util.encryption.impl.CloudOptimisationSharedCipherProvider;
import com.mmxlabs.scenario.service.model.util.encryption.impl.KeyFileLoader;
import com.mmxlabs.scenario.service.model.util.encryption.impl.keyfiles.KeyFileV2;

/**
 * Headless Optimisation Runner
 * 
 * @author Simon Goodall
 * 
 */

public abstract class HeadlessCloudOptimiserApplication extends HeadlessGenericApplication {

	protected static final String INPUT_SCENARIO = "scenario";
	protected static final String OUTPUT_SCENARIO = "outputScenario";
	protected static final String OUTPUT_FOLDER = "outputFolder";
	protected static final String PARAMS_FILE = "params";

	@Override
	public Object start(final IApplicationContext context) throws IOException {
		try {
			return dostart(createRunner());
		} catch (Throwable t) {
			t.printStackTrace();
			return HeadlessGenericApplication.EXIT_CODE_EXCEPTION;
		}
	}

	protected abstract AbstractJobRunner createRunner();

	public Object dostart(AbstractJobRunner runner) throws Exception {
		// get the command line
		readCommandLine();
		setupBasicFields();
		// check the license
		doCheckLicense();

		// log the user in and initialise related features
		HeadlessUtils.initAccessControl();

		final int numThreads = LNGScenarioChainBuilder.getNumberOfAvailableCores();

		{
			final String paramsFile = commandLine.getOptionValue(PARAMS_FILE);
			final File f = new File(paramsFile);
			if (f.exists() && f.isFile()) {
				runner.withParams(f);
			} else {
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
		//
		final String outputLoggingFolder = commandLine.getOptionValue(OUTPUT_FOLDER);

		ConsoleProgressMonitor monitor = new ConsoleProgressMonitor();
		final String outputScenarioFileName = commandLine.getOptionValue(OUTPUT_SCENARIO);

		// Get the root object
		try {
			KeyFileV2 keyfile = KeyFileLoader.getCloudOptimisationKeyFileV2();
			CloudOptimisationSharedCipherProvider scenarioCipherProvider = new CloudOptimisationSharedCipherProvider(keyfile);

			ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(scenarioFile.toURI().toURL(), (modelRecord, scenarioDataProvider) -> {

				runner.withScenario(scenarioDataProvider);

				// Hack to get metadata
				List<HeadlessApplicationOptions> hOptionsList = getHeadlessOptions();
				
				final Meta meta = writeMetaFields(scenarioFile, hOptionsList.get(0));
				meta.setScenario(scenarioFile.getName());
				
				runner.withLogging(meta);
				
//				final ScenarioMeta scenarioMeta =  writeOptimisationMetrics(scenarioDataProvider);
//				runner.withLogging(scenarioMeta);
				// end hack
								
				AbstractSolutionSet result = runner.run(numThreads, monitor);

				// final File resultOutput = new File(outputScenarioFileName + ".xmi");
				HeadlessUtils.saveResult(result, scenarioDataProvider, new File(outputScenarioFileName), scenarioCipherProvider);
				//
//				 ScenarioStorageUtil.storeCopyToFile(scenarioDataProvider, new File(outputScenarioFileName), scenarioCipherProvider);

			}, scenarioCipherProvider);
		} catch (Exception e) {
			throw new IOException("Error running job", e);
		}

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

		if (loggingFolder != null) {
			
			try {
				runner.saveLogs(new File(loggingFolder, UUID.randomUUID().toString() + ".json"));
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
		// options.addOption(OptionBuilder.withLongOpt(EXTRA_CONFIG_FILE).withDescription("File listing a batch of jobs to run").hasArg().create());

		// options.addOption(JSON, true, "JSON file containing parameters for algorithm being run");
		//
		options.addOption(OptionBuilder.withLongOpt(OUTPUT_SCENARIO).withDescription("Output scenario file").hasArg().create());
		//
		options.addOption(OptionBuilder.withLongOpt(OUTPUT_FOLDER).withDescription("Path to directory for output files").hasArg().create());
		
		options.addOption(OptionBuilder.withLongOpt(JOB_TYPE).withDescription("The type of job to run").hasArg().create());
		//
		//
		// options.addOption(OptionBuilder.withLongOpt(CUSTOM_INFO).withDescription("Custom information (using name=val syntax)").hasArg().create());
		// options.addOption(OptionBuilder.withLongOpt(USE_CASE).withDescription("Use-case handle").hasArg().create());

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

	protected void runAndWriteResults(final int run, final HeadlessApplicationOptions hOptions, final File scenarioFile, final File outputFile, final int threads) throws Exception {
		throw new IllegalStateException("Unexpected code path");
	}
}
