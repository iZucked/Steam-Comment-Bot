/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import org.eclipse.jdt.annotation.NonNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.license.ssl.LicenseChecker.InvalidLicenseException;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessApplicationOptions;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessGenericJSON;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessGenericJSON.Meta;
import com.mmxlabs.rcp.common.appversion.VersionHelper;
import com.mmxlabs.rcp.common.json.EMFJacksonModule;

/**
 * <p>
 * Abstract base class for "headless" applications meant to run some algorithm (e.g. the optimiser or the optioniser) on a scenario and collect performance metrics.
 * </p>
 * 
 * <p>
 * Child classes must implement {@link #runAndWriteResults(int, HeadlessApplicationOptions, File, File, int) runAndWriteResults} to run the algorithm and write the results.
 * <ul>
 * <li>Note that this invites a fair amount of redundancy in the child class methods. In future, this class may be refactored further, to reduce redundancy and remove some responsibilities from the
 * child classes.</li>
 * </ul>
 * </p>
 * 
 * @author simonmcgregor
 *
 */
public abstract class HeadlessGenericApplication implements IApplication {

	public static final int EXIT_CODE_EXCEPTION = 100;

	/** New exception type for scenario types that can't be handled */
	@SuppressWarnings("serial")
	public static class UnhandledScenarioTypeException extends RuntimeException {
		public UnhandledScenarioTypeException(@NonNull final String message) {
			super(message);
		}
	}

	@SuppressWarnings("serial")
	public static class InvalidCommandLineException extends Exception {
	}

	protected static final String MACHINE_INFO = "machineInfo";
	protected static final String CLIENT_CODE = "clientCode";
	protected static final String BUILD_VERSION = "buildId";
	protected static final String BATCH_FILE = "batchfile";

	protected static final String INPUT_SCENARIO = "scenario";
	protected static final String OUTPUT_SCENARIO = "outputScenario";
	protected static final String OUTPUT_FOLDER = "outputFolder";
	protected static final String JSON = "json";
	protected static final String NUM_RUNS = "numRuns";
	protected static final String CUSTOM_INFO = "custom";
	protected static final String USE_CASE = "useCase";
	protected static final String PARAMS_FILE = "params";

	protected static final String JOB_TYPE = "jobtype";

	protected String clientCode;
	protected JSONObject machineInfo;
	protected String buildVersion;
	protected CommandLine commandLine;

	@Override
	public Object start(final IApplicationContext context) throws IOException {
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

		final List<HeadlessApplicationOptions> optionsList = getHeadlessOptions();

		for (final HeadlessApplicationOptions hOptions : optionsList) {
			runScenarioMultipleTimes(hOptions);
		}

		return IApplication.EXIT_OK;
	}

	/**
	 * Runs a particular scenario multiple times, if necessary. Delegates the "real" work to the method runAndWriteResults implemented by the child class.
	 * 
	 * @param hOptions
	 */
	protected void runScenarioMultipleTimes(final HeadlessApplicationOptions hOptions) {
		hOptions.validate();
		final String scenarioFilename = hOptions.scenarioFileName;

		if (scenarioFilename == null || scenarioFilename.isEmpty()) {
			System.err.println("No scenario specified.");
			return;
		}

		final File scenarioFile = new File(scenarioFilename);

		if (scenarioFile.exists() == false) {
			System.err.println(String.format("Scenario file %s does not exist.", scenarioFilename));
			return;
		}

		try {
			final int numRuns = hOptions.numRuns;

			for (int run = 1; run <= numRuns; run++) {

				File outFile = null;
				if (hOptions.outputLoggingFolder != null) {
					new File(hOptions.outputLoggingFolder).mkdirs(); // create directory if necessary
					outFile = new File(hOptions.outputLoggingFolder + "/" + UUID.randomUUID().toString() + ".json"); // create output log
				}

				try {
					final int threads = 1; // we don't try anything complex with multiple threads
					runAndWriteResults(run, hOptions, scenarioFile, outFile, threads);
					System.out.println(String.format("Results written to file %s", outFile.getName()));
				} catch (final Exception e) {
					if (outFile != null) {
						outFile.delete(); // clean up output file if anything went wrong
					}

					writeRunFailure(hOptions, run, e);
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	protected abstract String getAlgorithmName();

	protected void writeMetaFields(final HeadlessGenericJSON<?, ?> json, final File scenarioFile, final HeadlessApplicationOptions hOptions, final int threads) {
		json.getMeta().setCheckSum(HeadlessUtils.mD5Checksum(scenarioFile));
		json.getMeta().setUseCase(hOptions.useCase);
		json.getMeta().setClient(clientCode);
		json.getMeta().setVersion(buildVersion);
		json.getMeta().setMachineType(machineInfo);
		json.getMeta().setCustomInfo(hOptions.customInfo);
		json.getMeta().setMaxHeapSize(Runtime.getRuntime().maxMemory());
		json.getParams().setCores(threads);
	}

	protected Meta writeMetaFields(final File scenarioFile, final HeadlessApplicationOptions hOptions) {
		final Meta meta = new Meta();
		meta.setDate(LocalDateTime.now());
		meta.setCheckSum(HeadlessUtils.mD5Checksum(scenarioFile));
		meta.setUseCase(hOptions.useCase);
		meta.setClient(clientCode);
		meta.setVersion(buildVersion);
		meta.setMachineType(machineInfo);
		meta.setCustomInfo(hOptions.customInfo);
		meta.setMaxHeapSize(Runtime.getRuntime().maxMemory());
		// json.getParams().setCores(threads);

		return meta;
	}

	private void writeRunFailure(final HeadlessApplicationOptions hOptions, final int run, final Exception e) {
		if (hOptions.outputLoggingFolder != null) {
			final StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);

			final JSONObject errorObject = new JSONObject();
			errorObject.put("type", getAlgorithmName());
			errorObject.put("run", run);
			final JSONObject metaObject = new JSONObject();

			metaObject.put("machineType", machineInfo);
			metaObject.put("scenario", hOptions.scenarioFileName);
			metaObject.put("date", LocalDateTime.now().toString());
			metaObject.put("version", buildVersion);
			metaObject.put("client", clientCode);
			errorObject.put("meta", metaObject);
			errorObject.put("error", sw.toString());

			final File logFolder = new File(hOptions.outputLoggingFolder);
			logFolder.mkdirs(); // create directory if necessary
			final File outFile = new File(logFolder, UUID.randomUUID().toString() + ".json"); // create output log

			try {
				pw = new PrintWriter(outFile);
				pw.print(errorObject);
				pw.close();
			} catch (final FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	/**
	 * Runs the appropriate algorithm and saves the results in the specified file.
	 * 
	 * @param run
	 *            The index of this run in a possible batch of multiple runs (this may be used e.g. to set a seed).
	 * @param hOptions
	 *            General parameter options for the headless run (e.g. scenario filename, etc.)
	 * @param options
	 *            Special parameter options for the algorithm being run.
	 * @param scenarioFile
	 *            The scenario being run.
	 * @param outFile
	 *            The file to output performance data to.
	 * @param threads
	 *            The number of threads being used.
	 * @throws Exception
	 */
	protected abstract void runAndWriteResults(int run, HeadlessApplicationOptions hOptions, File scenarioFile, File outFile, int threads) throws Exception;

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

	/**
	 * Reads a list of HeadlessApplicationOptions objects from a batch config file, or creates a list of a single item that is populated from command-line options.
	 * 
	 * @return
	 * @throws IOException
	 */
	protected List<HeadlessApplicationOptions> getHeadlessOptions() throws IOException {
		final List<HeadlessApplicationOptions> optionsList = new LinkedList<>();
		if (commandLine.hasOption(BATCH_FILE)) {
			// add list of options from batch file; these can be overridden by command-line
			// options
			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.registerModule(new Jdk8Module());
			mapper.registerModule(new EMFJacksonModule());
			mapper.enable(Feature.ALLOW_COMMENTS);

			optionsList.addAll(mapper.readValue(new File(commandLine.getOptionValue(BATCH_FILE)), new TypeReference<List<HeadlessApplicationOptions>>() {
			}));
		} else {
			// add blank options, to be populated from command line
			optionsList.add(new HeadlessApplicationOptions());
		}

		// override batch file options with command line options
		for (final HeadlessApplicationOptions options : optionsList) {
			overwriteCustomInfo(options.customInfo);
			options.outputLoggingFolder = HeadlessUtils.commandLineParameterOrValue(commandLine, OUTPUT_FOLDER, options.outputLoggingFolder);
			options.outputScenarioFileName = HeadlessUtils.commandLineParameterOrValue(commandLine, OUTPUT_SCENARIO, options.outputScenarioFileName);
			options.algorithmConfigFile = HeadlessUtils.commandLineParameterOrValue(commandLine, JSON, options.algorithmConfigFile);
			options.scenarioFileName = HeadlessUtils.commandLineParameterOrValue(commandLine, INPUT_SCENARIO, options.scenarioFileName);
			options.numRuns = Integer.parseInt(HeadlessUtils.commandLineParameterOrValue(commandLine, NUM_RUNS, Integer.toString(options.numRuns)));
			options.useCase = HeadlessUtils.commandLineParameterOrValue(commandLine, USE_CASE, options.useCase);
		}

		return optionsList;
	}

	protected void overwriteCustomInfo(final Map<String, String> info) {
		final String[] customValues = commandLine.getOptionValues(CUSTOM_INFO);

		if (customValues != null) {
			for (final String custom : customValues) {
				final String[] fields = custom.split("=");

				if (fields.length != 2) {
					throw new IllegalArgumentException(String.format("Custom argument '%s' needs to be in 'key=val' format", custom));
				}

				final String key = fields[0];
				final String val = fields[1];

				if (info.containsKey(key)) {
					final String oldValue = info.get(key);
					if (oldValue.equals(val) == false) {
						final String overrideWarning = "Overriding existing custom field '%s' with command line value: '%s' -> '%s'";
						System.err.println(String.format(overrideWarning, key, info.get(key), val));
					}
				}

				info.put(key, val);
			}
		}

	}

	/**
	 * Reads the options for the appropriate algorithm from a JSON file, using Jackson.
	 * 
	 * @param filename
	 * @return
	 */
	protected <T> T getAlgorithmOptionsFromJSON(final String filename, final Class<T> clazz) {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.registerModule(new Jdk8Module());
		mapper.enable(Feature.ALLOW_COMMENTS);

		try {
			return mapper.readValue(new File(filename), clazz);
		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void stop() {
		// Nothing special needed
	}

	@SuppressWarnings("static-access")
	protected CommandLine parseOptions(final String[] commandLineArgs) {

		// create the Options
		final Options options = HeadlessUtils.getRequiredOsgiOptions();

		// Headless application options

		options.addOption(OptionBuilder.withLongOpt(MACHINE_INFO).withDescription("JSON file containing machine info").hasArg().create());
		options.addOption(OptionBuilder.withLongOpt(BUILD_VERSION).withDescription("Build Version").hasArg().create());
		options.addOption(OptionBuilder.withLongOpt(CLIENT_CODE).withDescription("Client code for build").hasArg().create());

		options.addOption(OptionBuilder.withLongOpt(BATCH_FILE).withDescription("File listing a batch of jobs to run").hasArg().create());
		options.addOption(OptionBuilder.withLongOpt(CUSTOM_INFO).withDescription("Custom information (using name=val syntax)").hasArg().create());
		options.addOption(OptionBuilder.withLongOpt(USE_CASE).withDescription("Use-case handle").hasArg().create());

		options.addOption(OptionBuilder.withLongOpt(INPUT_SCENARIO).withDescription("Input scenario file").hasArg().create());
		options.addOption(OptionBuilder.withLongOpt(OUTPUT_SCENARIO).withDescription("Output scenario file").hasArg().create());
		options.addOption(OptionBuilder.withLongOpt(OUTPUT_FOLDER).withDescription("Path to directory for output files").hasArg().create());
		options.addOption(JSON, true, "JSON file containing parameters for algorithm being run");

		options.addOption(OptionBuilder.withLongOpt(JOB_TYPE).withDescription("The type of job to run").hasArg().create());

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

	public static void writeRunFailure(final File loggingFolder, final Throwable e) {
		if (loggingFolder != null) {
			try (StringWriter sw = new StringWriter()) {
				try (PrintWriter pw = new PrintWriter(sw)) {
					e.printStackTrace(pw);
				}

				final File outFile = new File(loggingFolder, "failure.json"); // create output log
				final JSONObject obj = new JSONObject();
				obj.put("error", sw.toString());
				try (PrintWriter pw = new PrintWriter(outFile)) {
					obj.writeJSONString(pw);
				}
			} catch (final Exception e1) {
				e1.printStackTrace();
			}
		}
	}

}
