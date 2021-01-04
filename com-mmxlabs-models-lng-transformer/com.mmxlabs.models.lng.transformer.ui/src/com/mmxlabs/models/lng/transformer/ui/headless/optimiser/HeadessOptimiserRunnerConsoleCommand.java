/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.optimiser;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.OptimisationMode;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.OptimisationStage;
import com.mmxlabs.models.lng.parameters.ParallelOptimisationStage;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.parameters.impl.UserSettingsImpl;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessApplicationOptions;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

//From OSGI prompt, run e.g. 
//osgi> optimise C:/dev/bm/bm.lingo C:/dev/bm/options.json
public class HeadessOptimiserRunnerConsoleCommand implements CommandProvider {

	private static SimpleDateFormat sdfTimestamp = new SimpleDateFormat("yyyyMMdd_HHmmss");

	public void _optimise2(CommandInterpreter ci) throws Exception {

		String scenarioFileName1 = "C:/temp/strategic2/StartingPoint1.lingo";
		String scenarioFileName2 = "C:/temp/strategic2/StartingPoint2.lingo";

		List<String> scenarios = Lists.newArrayList(scenarioFileName1, scenarioFileName2);
		// int[] spotCounts = { 4};
		// int[] seedCounts = {9, 12, 14 };
		// int[] itersCounts = { 400_000 };
		int[] spotCounts = { 2, 4, 6, 10 };
		int[] seedCounts = { 5, 8, 10, 15 };
		int[] itersCounts = { 400_000, 500_000, 600_000, 800_000 };

		File results = new File("C:/temp/strategic2/results2.csv");
		try (PrintWriter bw = new PrintWriter(results)) {

			for (String scenarioFileName : scenarios) {
				for (int spotCount : spotCounts) {
					for (int seedCount : seedCounts) {
						for (int iters : itersCounts) {

							HeadlessApplicationOptions options = new HeadlessApplicationOptions();
							// options.algorithmConfigFile = paramsFileName;
							options.scenarioFileName = scenarioFileName;
							options.outputScenarioFileName = "required";
							// options.outputLoggingFolder = outputPath;

							UserSettings userSettings = ScenarioUtils.createDefaultUserSettings();
							options.setUserSettings((UserSettingsImpl) userSettings);
							userSettings.setMode(OptimisationMode.STRATEGIC);
							userSettings.setSimilarityMode(SimilarityMode.OFF);
							userSettings.setWithCharterLength(true);
							userSettings.setWithSpotCargoMarkets(true);

							File scenarioFile = new File(scenarioFileName);
							{

								HeadlessOptimiserRunner runner = new HeadlessOptimiserRunner() {
									@Override
									protected void saveScenario(String outputFile, @NonNull IScenarioDataProvider sdp) throws IOException {
										AnalyticsModel m = ScenarioModelUtil.getAnalyticsModel(sdp);
										AbstractSolutionSet ss = m.getOptimisations().get(0);
										SolutionOption solutionOption = ss.getOptions().get(0);
										long pnl = ScheduleModelKPIUtils.getScheduleProfitAndLoss(solutionOption.getScheduleModel().getSchedule());
										bw.printf("%s,%s,%s,%d,%d\n", scenarioFileName, spotCount, seedCount, iters, pnl);
										bw.flush();
									}

									protected void customisePlan(OptimisationPlan optimisationPlan) {
										for (OptimisationStage stage : optimisationPlan.getStages()) {
											if (stage instanceof ParallelOptimisationStage<?>) {
												ParallelOptimisationStage<?> parallelOptimisationStage = (ParallelOptimisationStage<?>) stage;
												((ParallelOptimisationStage) stage).setJobCount(seedCount);
												if (parallelOptimisationStage.getTemplate() instanceof LocalSearchOptimisationStage) {
													LocalSearchOptimisationStage localSearchOptimisationStage = (LocalSearchOptimisationStage) parallelOptimisationStage.getTemplate();
													localSearchOptimisationStage.getAnnealingSettings().setIterations(iters);
												}
											}
										}
									}
								};

								try {
									// Get the root object
									ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(scenarioFile.toURI().toURL(), (modelRecord, sdp) -> {

										AnalyticsModel m = ScenarioModelUtil.getAnalyticsModel(sdp);
										m.getOptimisations().clear();

										SpotMarketsModel smm = ScenarioModelUtil.getSpotMarketsModel(sdp);
										for (SpotMarket sm : smm.getDesSalesSpotMarket().getMarkets()) {
											sm.getAvailability().setConstant(spotCount);
										}

										runner.run(options, sdp, new NullProgressMonitor(), null, null);
									});

								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}

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
		HeadlessApplicationOptions options = new HeadlessApplicationOptions();
		options.algorithmConfigFile = paramsFileName;
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
				// final String filenameStr =
				// scenarioFile.getAbsolutePath().replace(scenarioFile.getName(), "") +
				// options.filename + "_"+timestampStr+ ".csv";
				// try {
				// pst = new PrintStream(new File(filenameStr));
				// } catch (FileNotFoundException e) {
				// System.err.printf("Error opening options.filename, %s outputting to stdout
				// instead:\n", filenameStr);
				// e.printStackTrace(System.err);
				// }
				// }
				pst.println("Run,#Threads,Time(millis),GCTime(millis)");
				// for (int threads = minThreads; threads <= maxThreads; threads *= 2)
				{
					// options.threadCount = threads;
					// HeadlessOptimiserJSON json = setOptionParamsInJSONOutputObject(options,
					// scenarioFile, threads);

					// Do the runs with this set of parameters.
					// for (int run = 1; run <= options.numRuns; run++)
					{
						HeadlessOptimiserRunner runner = new HeadlessOptimiserRunner();
						try {
							runner.run(scenarioFile, options, new NullProgressMonitor(), null, null);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					// Write out the result of all runs.
					try {
						ObjectMapper mapper = new ObjectMapper();
						// mapper.writerWithDefaultPrettyPrinter().writeValue(new
						// File(scenarioFile.getAbsoluteFile() + "." + UUID.randomUUID().toString() +
						// ".json"), json);
					} catch (Exception e) {
						System.err.println("Error writing to file:");
						e.printStackTrace();
					}
				}
			}

			// private HeadlessOptimiserJSON
			// setOptionParamsInJSONOutputObject(HeadlessOptimiserRunner.Options options,
			// File scenarioFile, int threads) {
			// //Set up JSON object to write all results out with.
			// HeadlessOptimiserJSON json =
			// HeadlessOptimiserJSONTransformer.createJSONResultObject();
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
		return "optimiser scenario.lingo parameters.json\n\t";
	}

}
