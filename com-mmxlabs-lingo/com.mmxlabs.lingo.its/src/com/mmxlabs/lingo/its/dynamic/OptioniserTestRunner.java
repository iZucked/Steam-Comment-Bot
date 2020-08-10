/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.dynamic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.widgets.Display;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.common.util.TriConsumer;
import com.mmxlabs.lingo.its.tests.ReportTesterHelper;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessOptioniserRunner;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionOptimiserLogger;

/**
 * Work in progress class to run an optionser scenario and save optimisation view as table.
 * // TODO: Optimiser view does not work well in this mode, need to work in more of the reporter tester framework
 * // TODO: Can we use the Optimiser result verifier + .json file to ensure certain results? 
 */
public class OptioniserTestRunner {

	public static List<DynamicNode> runOptioniserTests(File baseDirectory) {

		final TriConsumer<List<DynamicNode>, File, File> consumer = (cases, scenarioFile, paramsFile) -> {
			cases.add(DynamicTest.dynamicTest(paramsFile.getName(), () -> {
				ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(scenarioFile.toURI().toURL(), (modelRecord, scenarioDataProvider) -> {

					// Clear existing results
					final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(scenarioDataProvider);
					analyticsModel.getOptimisations().clear();

					final HeadlessOptioniserRunner.Options options = getOptioniserSettings(paramsFile);

					HeadlessOptioniserRunner runner = new HeadlessOptioniserRunner();
					runner.run(0, (SlotInsertionOptimiserLogger) null, options, modelRecord, scenarioDataProvider, null);
					AnalyticsSolution as = new AnalyticsSolution(modelRecord, analyticsModel.getOptimisations().get(0), "Test");
					String reportID = as.getID();
					Display.getDefault().syncExec(() -> as.open());

					Thread.sleep(1000);
					Thread.yield();
					final String actualContents;
					{

						final ReportTesterHelper reportTester = new ReportTesterHelper();
						final IReportContents reportContents = reportTester.getReportContents(reportID, null, null);

						Assertions.assertNotNull(reportContents);

						// switch (type) {
						// case REPORT_HTML:
						actualContents = reportContents.getHTMLContents();
						// break;
						// case REPORT_JSON:
						// actualContents = reportContents.getJSONContents();
						// break;
						// default:
						// throw new IllegalArgumentException();
						// }
						Assertions.assertNotNull(actualContents);
					}
					// String viewId = ReportTester.generateReports(modelRecord, scenarioDataProvider, as.getID(), ReportType.REPORT_HTML);

					System.out.println(actualContents);

					final File resultsFolder = new File(scenarioFile.getParentFile(), "results");
					resultsFolder.mkdir();

					final File fitnessesFile = new File(resultsFolder, paramsFile.getName() + ".opti");

					Files.writeString(fitnessesFile.toPath(), actualContents);

				});
			}));
		};

		final List<DynamicNode> allCases = new LinkedList<>();
		if (baseDirectory.exists() && baseDirectory.isDirectory()) {
			for (final File f : baseDirectory.listFiles()) {
				if (f.isDirectory()) {
					final String name = f.getName();
					final File scenario = new File(f, "scenario.lingo");
					if (scenario.exists()) {
						final List<DynamicNode> cases = new LinkedList<>();

						for (final File params : f.listFiles()) {
							if (params.getName().startsWith("optioniser-") && params.getName().endsWith(".json")) {
								consumer.accept(cases, scenario, params);
							}
						}

						if (!cases.isEmpty()) {
							allCases.add(DynamicContainer.dynamicContainer(name, cases));
						}
					}
				}
			}
		}
		return allCases;
	}

	public static HeadlessOptioniserRunner.@Nullable Options getOptioniserSettings(final File file) {

		if (!file.exists()) {
			return null;
		} else {

			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.registerModule(new Jdk8Module());
			mapper.enable(Feature.ALLOW_COMMENTS);

			try {
				return mapper.readValue(file, HeadlessOptioniserRunner.Options.class);
			} catch (final IOException e) {
				e.printStackTrace();
				return null;
			}
		}

	}

}
