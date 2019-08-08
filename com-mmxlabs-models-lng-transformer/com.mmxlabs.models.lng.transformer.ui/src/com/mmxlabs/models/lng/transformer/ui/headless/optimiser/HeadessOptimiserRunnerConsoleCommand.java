/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.optimiser;

import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.fasterxml.jackson.databind.ObjectMapper;

//From OSGI prompt, run e.g. 
//osgi> optimise C:/dev/bm/bm.lingo C:/dev/bm/options.json
public class HeadessOptimiserRunnerConsoleCommand implements CommandProvider {

	private static SimpleDateFormat sdfTimestamp = new SimpleDateFormat("yyyyMMdd_HHmmss");

	public void _optimise(CommandInterpreter ci) throws Exception {

		String scenarioFileName = null;
		String paramsFileName = null;
		String outputPath = null;
		String outputName = null;

		String arg = null;
		while ((arg = ci.nextArgument()) != null) {

			if (scenarioFileName == null) {
				scenarioFileName = arg;
			} else if (paramsFileName == null) {
				paramsFileName = arg;
			} else if (outputPath == null) {
				outputPath = arg;
			} else if (outputName == null) {
				outputName = arg;
			}
		}
		//
		// ObjectMapper mapper = new ObjectMapper();
		// mapper.registerModule(new JavaTimeModule());
		// mapper.registerModule(new Jdk8Module());
		//
		HeadlessOptimiserRunner.Options options = new HeadlessOptimiserRunner.Options();
		options.jsonFile = paramsFileName;
		options.scenarioFileName = scenarioFileName;
		options.outputScenarioFileName = outputName;
		options.outputLoggingFolder = outputPath;

		File scenarioFile = new File(scenarioFileName);
		ThreadGroup tg = new ThreadGroup("Console optimiser");

		new Thread(tg, "Console optimiser") {
			@Override
			public void run() {
				final String timestampStr = getTimestamp();
				PrintStream pst = System.out;
				// if (options.filename != null && !options.filename.equals("")) {
				// final String filenameStr = scenarioFile.getAbsolutePath().replace(scenarioFile.getName(), "") + options.filename + "_"+timestampStr+ ".csv";
				// try {
				// pst = new PrintStream(new File(filenameStr));
				// } catch (FileNotFoundException e) {
				// System.err.printf("Error opening options.filename, %s outputting to stdout instead:\n", filenameStr);
				// e.printStackTrace(System.err);
				// }
				// }
				pst.println("Run,#Threads,Time(millis),GCTime(millis)");
				// for (int threads = minThreads; threads <= maxThreads; threads *= 2)
				{
					// options.threadCount = threads;
					// HeadlessOptimiserJSON json = setOptionParamsInJSONOutputObject(options, scenarioFile, threads);

					// Do the runs with this set of parameters.
					// for (int run = 1; run <= options.numRuns; run++)
					{
						HeadlessOptimiserRunner runner = new HeadlessOptimiserRunner();
						try {
							runner.run(scenarioFile, options, new NullProgressMonitor(), null);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					// Write out the result of all runs.
					try {
						ObjectMapper mapper = new ObjectMapper();
						// mapper.writerWithDefaultPrettyPrinter().writeValue(new File(scenarioFile.getAbsoluteFile() + "." + UUID.randomUUID().toString() + ".json"), json);
					} catch (Exception e) {
						System.err.println("Error writing to file:");
						e.printStackTrace();
					}
				}
			}

			// private HeadlessOptimiserJSON setOptionParamsInJSONOutputObject(HeadlessOptimiserRunner.Options options, File scenarioFile, int threads) {
			// //Set up JSON object to write all results out with.
			// HeadlessOptimiserJSON json = HeadlessOptimiserJSONTransformer.createJSONResultObject();
			// json.getMeta().setClient("V");
			// json.getMeta().setScenario(scenarioFile.getName());
			// json.getMeta().setMachineType(getMachineInfo());
			// json.getMeta().setVersion("Dev");
			//
			// json.getParams().setCores(threads);
			// json.getParams().getOptioniserProperties().setOptions(OptOptions.getInstance().toString());
			// json.getParams().getOptioniserProperties().setIterations(options.iterations);
			//
			// return json;
			// }
		}.start();
	}

	private String getTimestamp() {
		return sdfTimestamp.format(new Date());
	}

	private String getMachineInfo() {
		String machInfo = "AvailableProcessors:" + Integer.toString(Runtime.getRuntime().availableProcessors());
		return machInfo;
	}

	@Override
	public String getHelp() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("optimiser scenario.lingo parameters.json\n\t");
		return buffer.toString();
	}

}
