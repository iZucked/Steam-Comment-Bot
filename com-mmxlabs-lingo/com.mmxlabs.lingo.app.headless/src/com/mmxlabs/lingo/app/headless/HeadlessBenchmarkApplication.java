/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

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
import org.eclipse.jdt.annotation.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.mmxlabs.common.util.TriConsumer;
import com.mmxlabs.license.ssl.LicenseChecker.InvalidLicenseException;
import com.mmxlabs.lingo.app.headless.HeadlessGenericApplication.InvalidCommandLineException;
import com.mmxlabs.models.lng.transformer.jobs.IJobRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioChainBuilder;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessGenericJSON.Meta;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.JobRegistry;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.optimisation.OptimisationJobRunner;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.optioniser.OptioniserJobRunner;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox.SandboxJobRunner;
import com.mmxlabs.rcp.common.appversion.VersionHelper;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

/**
 * Headless Benchark Runner for dynamic tests folder structure
 * 
 * @author Simon Goodall
 * 
 */

public class HeadlessBenchmarkApplication implements IApplication {

	private static final String MACHINE_INFO = "machineInfo";
	private static final String CLIENT_CODE = "clientCode";
	private static final String BUILD_VERSION = "buildId";

	private static final String INPUT_FOLDER = "inputFolder";
	private static final String OUTPUT_FOLDER = "outputFolder";
	private static final String NUM_RUNS = "numRuns";
	private static final String CUSTOM_INFO = "custom";
	private static final String USE_CASE = "useCase";

	protected String clientCode;
	protected JSONObject machineInfo;
	protected String buildVersion;
	protected CommandLine commandLine;

	@Override
	public Object start(final IApplicationContext context) throws Exception {

		try {
			// check the license
			HeadlessUtils.doCheckLicense();
			// log the user in and initialise related features
			HeadlessUtils.initAccessControl();
			// get the command line
			readCommandLine();

			setupBasicFields();
		} catch (InvalidLicenseException | InvalidCommandLineException e) {
			return IApplication.EXIT_OK;
		}

		final String baseFolder = HeadlessUtils.commandLineParameterOrValue(commandLine, INPUT_FOLDER, null);
		if (baseFolder != null) {
			final File baseDirectory = new File(baseFolder);
			if (baseDirectory.exists() && baseDirectory.isDirectory()) {
				doRun(baseDirectory);
				;
			}
		}

		return IApplication.EXIT_OK;
	}

	@Override
	public void stop() {
		// Nothing special needed
	}

	public void doRun(final File baseDirectory) {
		File outputFolder = null;
		final int numRuns = 5;

		final String outputFolderName = HeadlessUtils.commandLineParameterOrValue(commandLine, OUTPUT_FOLDER, null);
		if (outputFolderName != null) {
			outputFolder = new File(outputFolderName);
			outputFolder.mkdirs();
		}
		final String useCase = HeadlessUtils.commandLineParameterOrValue(commandLine, USE_CASE, null);

		searchForBenchmarks(baseDirectory, "sandbox-", SandboxJobRunner.JOB_TYPE, outputFolder, numRuns, useCase);
		searchForBenchmarks(baseDirectory, "optimiser-", OptimisationJobRunner.JOB_TYPE, outputFolder, numRuns, useCase);
		searchForBenchmarks(baseDirectory, "optioniser--", OptioniserJobRunner.JOB_TYPE, outputFolder, numRuns, useCase);
	}

	public void searchForBenchmarks(final File baseDirectory, final String paramsPrefix, final String jobType, final File outputLoggingFolder, final int numRuns, @Nullable final String useCase) {

		final int numThreads = LNGScenarioChainBuilder.getNumberOfAvailableCores();

		final TriConsumer<File, File, File> consumer = (parentName, scenarioFile, paramsFile) -> {
			final String scenarioName = String.format("%s-%s", parentName.getName(), paramsFile.getName());
			try {
				ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(scenarioFile.toURI().toURL(), (modelRecord, sdp) -> {
					for (int run = 1; run <= numRuns; run++) {

						System.out.println("Running benchmark for " + scenarioName + " (" + run + ")");

						try {

							final IJobRunner runner = JobRegistry.INSTANCE.newRunner(jobType);

							if (runner == null) {
								throw new IllegalStateException("Unable to create job runner");
							}

							runner.withScenario(sdp);
							runner.withParams(paramsFile);

							final Meta meta = writeMetaFields(scenarioFile, useCase);
							meta.setScenario(scenarioName);
							runner.withLogging(meta);

							runner.run(numThreads, new ConsoleProgressMonitor());
							if (outputLoggingFolder != null) {
								final File outFile = new File(outputLoggingFolder, UUID.randomUUID().toString() + ".json"); // create output log
								runner.saveLogs(outFile);
							}
						} catch (final Exception e) {
							e.printStackTrace();
							if (outputLoggingFolder != null) {
								final File outFile = new File(outputLoggingFolder, UUID.randomUUID().toString() + ".json"); // create output log
								writeRunFailure(outFile, jobType, scenarioName, run, e);
							}
						}
					}
				});
			} catch (final Exception e) {
				e.printStackTrace();
			}
		};

		if (baseDirectory.exists() && baseDirectory.isDirectory()) {
			for (final File f : baseDirectory.listFiles()) {
				if (f.isDirectory()) {
					final File scenario = new File(f, "scenario.lingo");
					if (scenario.exists()) {
						for (final File paramsFile : f.listFiles()) {
							if (paramsFile.getName().startsWith(paramsPrefix) && paramsFile.getName().endsWith(".json")) {
								consumer.accept(f, scenario, paramsFile);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Initialises the commandLine field by reading the command line, throwing an exception if there is a problem.
	 * 
	 * @throws InvalidCommandLineException
	 */
	protected void readCommandLine() throws InvalidCommandLineException {
		// read relevant settings from the command line
		String[] commandLineArgs = Platform.getCommandLineArgs();
		commandLineArgs = HeadlessUtils.filterCommandLineArgs(commandLineArgs);

		commandLine = parseOptions(commandLineArgs);

		if (commandLine == null) {
			System.err.println("Error parsing the command line settings");
			throw new InvalidCommandLineException();
		}
	}

	@SuppressWarnings("static-access")
	protected CommandLine parseOptions(final String[] commandLineArgs) {

		// create the Options
		final Options options = HeadlessUtils.getRequiredOsgiOptions();

		// Headless application options

		options.addOption(OptionBuilder.withLongOpt(MACHINE_INFO).withDescription("JSON file containing machine info").hasArg().create());
		options.addOption(OptionBuilder.withLongOpt(BUILD_VERSION).withDescription("Build Version").hasArg().create());
		options.addOption(OptionBuilder.withLongOpt(CLIENT_CODE).withDescription("Client code for build").hasArg().create());

		options.addOption(OptionBuilder.withLongOpt(CUSTOM_INFO).withDescription("Custom information (using name=val syntax)").hasArg().create());
		options.addOption(OptionBuilder.withLongOpt(USE_CASE).withDescription("Use-case handle").hasArg().create());

		options.addOption(OptionBuilder.withLongOpt(INPUT_FOLDER).withDescription("Input folder").hasArg().create());
		options.addOption(OptionBuilder.withLongOpt(OUTPUT_FOLDER).withDescription("Path to directory for output files").hasArg().create());

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

	/**
	 * Initialises the clientCode, buildVersion and machineInfo fields.
	 * 
	 * @throws InvalidCommandLineException
	 */
	protected void setupBasicFields() throws InvalidCommandLineException {

		clientCode = VersionHelper.getInstance().getClientCode();
		buildVersion = VersionHelper.getInstance().getClientVersion();

		// get the build version from the command line
		if (commandLine.hasOption(BUILD_VERSION)) {
			buildVersion = commandLine.getOptionValue(BUILD_VERSION);
		} else if (buildVersion == null) {
			buildVersion = "Dev";
		}

		machineInfo = HeadlessUtils.getDefaultMachineInfo();
		// merge the machine info from a file specified in the command line
		if (commandLine.hasOption(MACHINE_INFO)) {
			try {
				final String infoStr = Files.readString(Path.of(commandLine.getOptionValue(MACHINE_INFO)), StandardCharsets.UTF_8);
				final JSONObject parsed = (JSONObject) new JSONParser().parse(infoStr);
				for (final var key : parsed.keySet()) {
					machineInfo.put(key.toString(), parsed.get(key.toString()));
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected Meta writeMetaFields(final File scenarioFile, final String useCase) {
		final Meta meta = new Meta();
		meta.setDate(LocalDateTime.now());
		meta.setCheckSum(HeadlessUtils.mD5Checksum(scenarioFile));
		meta.setUseCase(useCase);
		meta.setClient(clientCode);
		meta.setVersion(buildVersion);
		meta.setMachineType(machineInfo);
		// meta.setCustomInfo(hOptions.customInfo);
		meta.setMaxHeapSize(Runtime.getRuntime().maxMemory());
		// json.getParams().setCores(threads);

		return meta;
	}

	private void writeRunFailure(final File outFile, final String jobType, final String scenarioName, final int run, final Exception e) {
		final StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);

		final String escapedStackTrace = String.valueOf(new JsonStringEncoder().quoteAsString(sw.toString()));

		// ugly hack: hardcode failure log
		final String resultFmt = "{\n" + "  \"type\" : \"%s\",\n" + "  \"run\" : \"%d\",\n" + "  \"meta\" : {\n" + "    \"machineType\" : \"%s\",\n" + "    \"scenario\" : \"%s\",\n"
				+ "    \"date\" : \"%s\",\n" + "    \"version\" : \"%s\",\n" + "    \"client\" : \"%s\"\n" + "  },\n" + "  \"error\" : \"%s\"" + "}";

		final String result = String.format(resultFmt, jobType, run, machineInfo, scenarioName, LocalDateTime.now().toString(), buildVersion, clientCode, escapedStackTrace);

		try {
			pw = new PrintWriter(outFile);
			pw.print(result);
			pw.close();
		} catch (final FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
