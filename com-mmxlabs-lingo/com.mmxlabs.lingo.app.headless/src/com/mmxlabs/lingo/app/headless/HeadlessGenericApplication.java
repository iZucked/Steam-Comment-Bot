/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.io.CharStreams;
import com.mmxlabs.license.ssl.LicenseChecker;
import com.mmxlabs.license.ssl.LicenseChecker.InvalidLicenseException;
import com.mmxlabs.license.ssl.LicenseChecker.LicenseState;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessApplicationOptions;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessGenericJSON;
import com.mmxlabs.rcp.common.json.EMFJacksonModule;

/**
 * <p>
 * Abstract base class for "headless" applications meant to run some algorithm
 * (e.g. the optimiser or the optioniser) on a scenario and collect performance
 * metrics.
 * </p>
 * 
 * <p>
 * Child classes must implement
 * {@link #runAndWriteResults(int, HeadlessApplicationOptions, File, File, int)
 * runAndWriteResults} to run the algorithm and write the results.
 * <ul>
 * <li>Note that this invites a fair amount of redundancy in the child class
 * methods. In future, this class may be refactored further, to reduce
 * redundancy and remove some responsibilities from the child classes.</li>
 * </ul>
 * </p>
 * 
 * @author simonmcgregor
 *
 */
public abstract class HeadlessGenericApplication implements IApplication {

	/** New exception type for scenario types that can't be handled */
	@SuppressWarnings("serial")
	public static class UnhandledScenarioTypeException extends RuntimeException {
		public UnhandledScenarioTypeException(@NonNull String message) {
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

	protected String clientCode;
	protected String machineInfo;
	protected String buildVersion;
	protected CommandLine commandLine;

	@Override
	public Object start(final IApplicationContext context) throws IOException {
		try {
			// check the license
			doCheckLicense();
			// log the user in and initialise related features
			HeadlessUtils.initAccessControl();
			// get the command line
			readCommandLine();

			setupBasicFields();
		} catch (InvalidLicenseException | InvalidCommandLineException e) {
			return IApplication.EXIT_OK;
		}

		List<HeadlessApplicationOptions> optionsList = getHeadlessOptions();

		for (HeadlessApplicationOptions hOptions : optionsList) {
			runScenarioMultipleTimes(hOptions);
		}

		return IApplication.EXIT_OK;
	}

	/**
	 * Runs a particular scenario multiple times, if necessary. Delegates the "real"
	 * work to the method runAndWriteResults implemented by the child class.
	 * 
	 * @param hOptions
	 */
	protected void runScenarioMultipleTimes(HeadlessApplicationOptions hOptions) {
		hOptions.validate();
		final String scenarioFilename = hOptions.scenarioFileName;

		if (scenarioFilename == null || scenarioFilename.isEmpty()) {
			System.err.println("No scenario specified.");
			return;
		}

		File scenarioFile = new File(scenarioFilename);

		if (scenarioFile.exists() == false) {
			System.err.println(String.format("Scenario file %s does not exist.", scenarioFilename));
			return;
		}

		try {
			int numRuns = hOptions.numRuns;

			for (int run = 1; run <= numRuns; run++) {

				File outFile = null;
				if (hOptions.outputLoggingFolder != null) {
					new File(hOptions.outputLoggingFolder).mkdirs(); // create directory if necessary
					outFile = new File(hOptions.outputLoggingFolder + "/" + UUID.randomUUID().toString() + ".json"); // create output log
				}

				try {
					int threads = 1; // we don't try anything complex with multiple threads
					runAndWriteResults(run, hOptions, scenarioFile, outFile, threads);
					System.out.println(String.format("Results written to file %s", outFile.getName()));
				} catch (Exception e) {
					if (outFile != null) {
						outFile.delete(); // clean up output file if anything went wrong
					}

					writeRunFailure(hOptions, run, e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected abstract String getAlgorithmName();

	protected void writeMetaFields(HeadlessGenericJSON<?, ?> json, File scenarioFile, HeadlessApplicationOptions hOptions, int threads) {
		json.getMeta().setCheckSum(mD5Checksum(scenarioFile));
		json.getMeta().setUseCase(hOptions.useCase);
		json.getMeta().setClient(clientCode);
		json.getMeta().setVersion(buildVersion);
		json.getMeta().setMachineType(machineInfo);
		json.getMeta().setCustomInfo(hOptions.customInfo);
		json.getMeta().setMaxHeapSize(Runtime.getRuntime().maxMemory());
		json.getParams().setCores(threads);
	}

	private void writeRunFailure(HeadlessApplicationOptions hOptions, int run, Exception e) {
		if (hOptions.outputLoggingFolder != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);

			String escapedStackTrace = String.valueOf(new JsonStringEncoder().quoteAsString(sw.toString()));

			// ugly hack: hardcode failure log
			String resultFmt = "{\n" + "  \"type\" : \"%s\",\n" + "  \"run\" : \"%d\",\n" + "  \"meta\" : {\n" + "    \"machineType\" : \"%s\",\n" + "    \"scenario\" : \"%s\",\n"
					+ "    \"date\" : \"%s\",\n" + "    \"version\" : \"%s\",\n" + "    \"client\" : \"%s\"\n" + "  },\n" + "  \"error\" : \"%s\"" + "}";

			String result = String.format(resultFmt, getAlgorithmName(), run, machineInfo, hOptions.scenarioFileName, LocalDateTime.now().toString(), buildVersion, clientCode, escapedStackTrace);

			new File(hOptions.outputLoggingFolder).mkdirs(); // create directory if necessary
			File outFile = new File(hOptions.outputLoggingFolder + "/" + UUID.randomUUID().toString() + ".json"); // create output log

			try {
				pw = new PrintWriter(outFile);
				pw.print(result);
				pw.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	/**
	 * Runs the appropriate algorithm and saves the results in the specified file.
	 * 
	 * @param run          The index of this run in a possible batch of multiple
	 *                     runs (this may be used e.g. to set a seed).
	 * @param hOptions     General parameter options for the headless run (e.g.
	 *                     scenario filename, etc.)
	 * @param options      Special parameter options for the algorithm being run.
	 * @param scenarioFile The scenario being run.
	 * @param outFile      The file to output performance data to.
	 * @param threads      The number of threads being used.
	 * @throws Exception
	 */
	protected abstract void runAndWriteResults(int run, HeadlessApplicationOptions hOptions, File scenarioFile, File outFile, int threads) throws Exception;

	/**
	 * Checks the user license, throwing an exception if there is a problem.
	 * 
	 * @throws LicenseChecker.InvalidLicenseException
	 */
	protected void doCheckLicense() throws LicenseChecker.InvalidLicenseException {
		// check to see if the user has a valid license
		final LicenseState validity = LicenseChecker.checkLicense();
		if (validity != LicenseState.Valid) {
			System.err.println("License is invalid");
			throw new LicenseChecker.InvalidLicenseException();
		}
	}

	/**
	 * Initialises the commandLine field by reading the command line, throwing an
	 * exception if there is a problem.
	 * 
	 * @throws InvalidCommandLineException
	 */
	protected void readCommandLine() throws InvalidCommandLineException {
		// read relevant settings from the command line
		String[] commandLineArgs = Platform.getCommandLineArgs();
		commandLineArgs = filterCommandLineArgs(commandLineArgs);

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
		// get the client code from the command line
		if (commandLine.hasOption(CLIENT_CODE)) {
			clientCode = commandLine.getOptionValue(CLIENT_CODE);
		} else {
			System.err.println("Command line must specify client code.");
			throw new InvalidCommandLineException();
		}

		// get the build version from the command line
		if (commandLine.hasOption(BUILD_VERSION)) {
			buildVersion = commandLine.getOptionValue(BUILD_VERSION);
		} else {
			buildVersion = "Dev";
		}

		machineInfo = getDefaultMachineInfo();
		// get the machine info from a file specified in the command line
		if (commandLine.hasOption(MACHINE_INFO)) {
			try {
				machineInfo = CharStreams.toString(new FileReader(commandLine.getOptionValue(MACHINE_INFO)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Reads a list of HeadlessApplicationOptions objects from a batch config file,
	 * or creates a list of a single item that is populated from command-line
	 * options.
	 * 
	 * @return
	 * @throws IOException
	 */
	protected List<HeadlessApplicationOptions> getHeadlessOptions() throws IOException {
		List<HeadlessApplicationOptions> optionsList = new LinkedList<>();
		if (commandLine.hasOption(BATCH_FILE)) {
			// add list of options from batch file; these can be overridden by command-line
			// options
			ObjectMapper mapper = new ObjectMapper();
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
		for (HeadlessApplicationOptions options : optionsList) {
			overwriteCustomInfo(options.customInfo);
			options.outputLoggingFolder = commandLineParameterOrValue(OUTPUT_FOLDER, options.outputLoggingFolder);
			options.outputScenarioFileName = commandLineParameterOrValue(OUTPUT_SCENARIO, options.outputScenarioFileName);
			options.algorithmConfigFile = commandLineParameterOrValue(JSON, options.algorithmConfigFile);
			options.scenarioFileName = commandLineParameterOrValue(INPUT_SCENARIO, options.scenarioFileName);
			options.numRuns = Integer.parseInt(commandLineParameterOrValue(NUM_RUNS, Integer.toString(options.numRuns)));
			options.useCase = commandLineParameterOrValue(USE_CASE, options.useCase);
		}

		return optionsList;
	}

	protected void overwriteCustomInfo(Map<String, String> info) {
		String[] customValues = commandLine.getOptionValues(CUSTOM_INFO);

		if (customValues != null) {
			for (String custom : customValues) {
				String[] fields = custom.split("=");

				if (fields.length != 2) {
					throw new IllegalArgumentException(String.format("Custom argument '%s' needs to be in 'key=val' format", custom));
				}

				String key = fields[0];
				String val = fields[1];

				if (info.containsKey(key)) {
					String oldValue = info.get(key);
					if (oldValue.equals(val) == false) {
						String overrideWarning = "Overriding existing custom field '%s' with command line value: '%s' -> '%s'";
						System.err.println(String.format(overrideWarning, key, info.get(key), val));
					}
				}

				info.put(key, val);
			}
		}

	}

	/**
	 * Returns the value of a command line parameter, or a specified existing value.
	 * If the existing value is not null, and the command line parameter is present,
	 * a warning is printed to stderr.
	 */
	protected String commandLineParameterOrValue(String commandLineOptionName, String oldValue) {
		if (commandLine.hasOption(commandLineOptionName)) {
			String newValue = commandLine.getOptionValue(commandLineOptionName);

			if (oldValue != null && oldValue.equals(newValue) == false) {
				String overrideWarning = "Overriding existing value with command line option %s: '%s' -> '%s'";
				System.err.println(String.format(overrideWarning, commandLineOptionName, oldValue, newValue));
			}

			return newValue;

		}

		return oldValue;
	}

	/**
	 * Reads the options for the appropriate algorithm from a JSON file, using
	 * Jackson.
	 * 
	 * @param filename
	 * @return
	 */
	protected <T> T getAlgorithmOptionsFromJSON(String filename, Class<T> clazz) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.registerModule(new Jdk8Module());
		mapper.enable(Feature.ALLOW_COMMENTS);

		try {
			return mapper.readValue(new File(filename), clazz);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Filter out invalid command line items that getopt cannot work with
	 * 
	 * @param commandLineArgs
	 * @return
	 */
	protected String[] filterCommandLineArgs(final String[] commandLineArgs) {
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

	protected String getDefaultMachineInfo() {
		String machInfo = "AvailableProcessors:" + Integer.toString(Runtime.getRuntime().availableProcessors());
		return machInfo;
	}

	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}

	public String mD5Checksum(File input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			// If source is a directory, collect MD5 sum of all contents.
			if (input.isDirectory()) {
				for (File f : input.listFiles()) {
					if (f.isFile()) {
						try (InputStream in = new FileInputStream(f)) {
							byte[] block = new byte[4096];
							int length;
							while ((length = in.read(block)) > 0) {
								digest.update(block, 0, length);
							}
						}
					}
				}
			} else {

				try (InputStream in = new FileInputStream(input)) {
					byte[] block = new byte[4096];
					int length;
					while ((length = in.read(block)) > 0) {
						digest.update(block, 0, length);
					}
				}
			}
			String result = bytesToHex(digest.digest());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void writeRunFailure(File loggingFolder, Throwable e) {
		if (loggingFolder != null) {
			try (StringWriter sw = new StringWriter()) {
				try (PrintWriter pw = new PrintWriter(sw)) {
					e.printStackTrace(pw);
				}

				File outFile = new File(loggingFolder, "failure.json"); // create output log
				JSONObject obj = new JSONObject();
				obj.put("error", sw.toString());
				try (PrintWriter pw = new PrintWriter(outFile)) {
					obj.write(pw);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
