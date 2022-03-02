/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.mmxlabs.lingo.its.tests.AbstractReportTester.ReportType;
import com.mmxlabs.lingo.reports.IAnalyticSolutionGenerator;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.IReportContentsGenerator;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.PaperDealAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.ui.ScenarioResultImpl;

/**
 * Helper class to open up a view, set the scenario selection provider to the
 * given instance and adapt the result to a {@link IReportContents} instance.
 * 
 * @author Simon Goodall
 * 
 */
public class ReportTester {

	private static final Logger LOG = LoggerFactory.getLogger(ReportTester.class);
	private static final UnaryOperator<String> stripWhitespace = s -> s.replaceAll("\\s+", "").replaceAll("\r\n", "").replaceAll("\n", "");

	/**
	 * Runs a report test and returns the result
	 * 
	 * @param instance
	 * @param scenarioDataProvider
	 * @param reportID
	 * @param type
	 * @param elementID
	 * @return
	 * @throws Exception
	 */
	public static String runReportsTest(final ScenarioModelRecord instance, @NonNull final IScenarioDataProvider scenarioDataProvider, final String reportID, final ReportType type) throws Exception {
		Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);

		final String result[] = new String[1];

		// A side-effect is the initial evaluation.
		LNGScenarioRunnerCreator.withLegacyEvaluationRunner(scenarioDataProvider, true, runner -> {

			final ScenarioResult scenarioResult = new ScenarioResultImpl(instance, ScenarioModelUtil.getScheduleModel(runner.getScenarioDataProvider()));

			final ReportTesterHelper reportTester = new ReportTesterHelper();

			reportTester.runReportTest(reportID, null, null, IReportContentsGenerator.class, (generator) -> {
				IReportContents reportContents = generator.getReportContents(null, scenarioResult, null);
				if (type == ReportType.REPORT_HTML) {
					result[0] = reportContents.getHTMLContents();
				} else if (type == ReportType.REPORT_JSON) {
					result[0] = reportContents.getJSONContents();
				} else {
					throw new IllegalArgumentException();
				}
			});

		});

		return result[0];
	}

	/**
	 * New (as of 2021-05-17) way to run report tests with an element. Bug Simon to
	 * clean up the old code if this is still here more than a month later.
	 * 
	 * @param instance
	 * @param scenarioDataProvider
	 * @param reportID
	 * @param type
	 * @param elementID
	 * @return
	 * @throws Exception
	 */
	public static String runReportsTestWithElement(final ScenarioModelRecord instance, @NonNull final IScenarioDataProvider scenarioDataProvider, final String reportID, final ReportType type,
			final String elementID) throws Exception {
		Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);

		final String result[] = new String[1];

		// A side-effect is the initial evaluation.
		LNGScenarioRunnerCreator.withLegacyEvaluationRunner(scenarioDataProvider, true, runner -> {

			Object target = null;
			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(scenarioDataProvider);
			final Schedule schedule = scheduleModel.getSchedule();
			if (schedule != null) {
				for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
					if (elementID.equals(cargoAllocation.getName())) {
						target = cargoAllocation;
						break;
					}
				}
				if (target == null) {
					for (final PaperDealAllocation pda : schedule.getPaperDealAllocations()) {
						final PaperDeal pd = pda.getPaperDeal();
						if (pd != null && elementID.equals(pd.getName())) {
							target = pd;
							break;
						}
					}
				}
			}

			Assertions.assertNotNull("Target element not found", elementID);
			final Object pTarget = target;

			final ScenarioResult scenarioResult = new ScenarioResultImpl(instance, ScenarioModelUtil.getScheduleModel(runner.getScenarioDataProvider()));

			final ReportTesterHelper reportTester = new ReportTesterHelper();

			reportTester.runReportTest(reportID, null, null, IReportContentsGenerator.class, generator -> {
				IReportContents reportContents = generator.getReportContents(null, scenarioResult, Lists.newArrayList(pTarget));
				if (type == ReportType.REPORT_HTML) {
					result[0] = reportContents.getHTMLContents();
				} else if (type == ReportType.REPORT_JSON) {
					result[0] = reportContents.getJSONContents();
				} else {
					throw new IllegalArgumentException();
				}
			});

		});

		return result[0];
	}

	public static void testReportsWithElement(final ScenarioModelRecord instance, @NonNull final IScenarioDataProvider scenarioDataProvider, final URL scenarioURL, final String reportID,
			final String shortName, final ReportType type, final String elementID, @Nullable final Consumer<ScenarioModelRecord> preAction) throws Exception {

		String actualContents = runReportsTestWithElement(instance, scenarioDataProvider, reportID, type, elementID);

		Assertions.assertNotNull(actualContents);
		if (TestingModes.ReportTestMode == TestMode.Generate) {

			final URL expectedReportOutput = new URL(FileLocator.toFileURL(new URL(scenarioURL.toString())).toString().replaceAll(" ", "%20"));

			final File f1 = new File(expectedReportOutput.toURI());
			final String slash = f1.isDirectory() ? "/" : "";
			final File file2 = new File(f1.getAbsoluteFile() + slash + "reports" + "." + shortName + "." + type.getExtension());
			try (PrintWriter pw = new PrintWriter(file2, StandardCharsets.UTF_8.name())) {
				pw.print(actualContents);
			}
		} else {
			final URL expectedReportOutput = new URL(
					FileLocator.toFileURL(new URL(scenarioURL.toString() + "reports" + "." + shortName + "." + type.getExtension())).toString().replaceAll(" ", "%20"));
			final StringBuilder expectedOutputBuilder = new StringBuilder();
			{
				try (InputStream is = expectedReportOutput.openStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
					String line = reader.readLine();
					if (line != null) {
						expectedOutputBuilder.append(line);
					}
					while (line != null) {
						line = reader.readLine();
						if (line != null) {
							expectedOutputBuilder.append(System.lineSeparator());
							expectedOutputBuilder.append(line);
						}
					}
				}
			}
			doValidate(actualContents, expectedOutputBuilder);

		}
	}

	public static void testReports(final @NonNull ScenarioModelRecord modelRecord, @NonNull final IScenarioDataProvider scenarioDataProvider, final URL scenarioURL, final String reportID,
			final String shortName, final ReportType type, @Nullable final Consumer<ScenarioModelRecord> preAction) throws Exception {

		Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);

		// A side-effect is the initial evaluation.
		LNGScenarioRunnerCreator.withLegacyEvaluationRunner(scenarioDataProvider, true, runner -> {
		});
		testReports_NoEvaluate(modelRecord, scenarioDataProvider, scenarioURL, reportID, shortName, type, preAction);
	}

	public static void testReports_NoEvaluate(final @NonNull ScenarioModelRecord modelRecord, @NonNull final IScenarioDataProvider scenarioDataProvider, final URL scenarioURL, final String reportID,
			final String shortName, ReportType type, @Nullable final Consumer<ScenarioModelRecord> preAction) throws Exception {

		if (preAction != null) {
			preAction.accept(modelRecord);
		}

		final ScenarioResult scenarioResult = new ScenarioResultImpl(modelRecord, ScenarioModelUtil.getScheduleModel(scenarioDataProvider));

		final ReportTesterHelper reportTester = new ReportTesterHelper();
		String[] result = new String[1];
		reportTester.runReportTest(reportID, null, null, IReportContentsGenerator.class, (generator) -> {
			IReportContents reportContents = generator.getReportContents(null, scenarioResult, null);
			if (type == ReportType.REPORT_HTML) {
				result[0] = reportContents.getHTMLContents();
			} else if (type == ReportType.REPORT_JSON) {
				result[0] = reportContents.getJSONContents();
			} else {
				throw new IllegalArgumentException();
			}
		});

		final String actualContents = result[0];

		Assertions.assertNotNull(actualContents);
		if (TestingModes.ReportTestMode == TestMode.Generate) {

			final URL expectedReportOutput = new URL(FileLocator.toFileURL(new URL(scenarioURL.toString())).toString().replaceAll(" ", "%20"));

			final File f1 = new File(expectedReportOutput.toURI());
			final String slash = f1.isDirectory() ? "/" : "";
			final File file2 = new File(f1.getAbsoluteFile() + slash + "reports" + "." + shortName + "." + type.getExtension());
			try (PrintWriter pw = new PrintWriter(file2, StandardCharsets.UTF_8.name())) {
				pw.print(actualContents);
			}
		} else {
			final URL expectedReportOutput = new URL(
					FileLocator.toFileURL(new URL(scenarioURL.toString() + "reports" + "." + shortName + "." + type.getExtension())).toString().replaceAll(" ", "%20"));
			final StringBuilder expectedOutputBuilder = new StringBuilder();
			{
				try (InputStream is = expectedReportOutput.openStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
					String line = reader.readLine();
					if (line != null) {
						expectedOutputBuilder.append(line);
					}
					while (line != null) {
						line = reader.readLine();
						if (line != null) {
							expectedOutputBuilder.append(System.lineSeparator());
							expectedOutputBuilder.append(line);
						}
					}
				}
			}
			doValidate(actualContents, expectedOutputBuilder);

		}

	}

	public static void testPinDiffReports(final URL pinScenarioURL, final URL refScenarioURL, final URL dataURL, final String reportID, final String shortName, final String extension)
			throws Exception {

		ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(pinScenarioURL, (pinModelRecord, pinScenarioDataProvider) -> {
			ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(refScenarioURL, (refModelRecord, refScenarioDataProvider) -> {
				ReportTester.testPinDiffReports(pinModelRecord, pinScenarioDataProvider, refModelRecord, refScenarioDataProvider, dataURL, ReportTesterHelper.CHANGESET_REPORT_ID,
						ReportTesterHelper.CHANGESET_REPORT_SHORTNAME, "html");

			});
		});
	}

	public static void testPinDiffReports(final ScenarioModelRecord pinInstance, final IScenarioDataProvider pinModelDataProvider, final ScenarioModelRecord refInstance,
			final IScenarioDataProvider refModelDataProvider, final URL scenarioURL, final String reportID, final String shortName, final String extension) throws Exception {
		Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);

		// A side-effect is the initial evaluation.

		LNGScenarioRunnerCreator.withLegacyEvaluationRunner(pinModelDataProvider, true, pinRunner -> {
			final ScenarioResult pinResult = new ScenarioResultImpl(pinInstance, ScenarioModelUtil.getScheduleModel(pinRunner.getScenarioDataProvider()));
			LNGScenarioRunnerCreator.withLegacyEvaluationRunner(refModelDataProvider, true, refRunner -> {
				final ScenarioResult refResult = new ScenarioResultImpl(refInstance, ScenarioModelUtil.getScheduleModel(refRunner.getScenarioDataProvider()));

				final ReportTesterHelper reportTester = new ReportTesterHelper();
				final String[] result = new String[1];

				reportTester.runReportTest(reportID, null, null, IReportContentsGenerator.class, (generator) -> {
					result[0] = generator.getReportContents(pinResult, refResult, null).getHTMLContents();
				});

				Assertions.assertNotNull(result[0]);
				final String actualContents = result[0];
				Assertions.assertNotNull(actualContents);
				if (TestingModes.ReportTestMode == TestMode.Generate) {

					final URL expectedReportOutput = new URL(FileLocator.toFileURL(new URL(scenarioURL.toString())).toString().replaceAll(" ", "%20"));

					final File f1 = new File(expectedReportOutput.toURI());
					final String slash = f1.isDirectory() ? "/" : "";
					final File file2 = new File(f1.getAbsoluteFile() + slash + "reports" + "." + shortName + "." + extension);
					try (PrintWriter pw = new PrintWriter(file2, StandardCharsets.UTF_8.name())) {
						pw.print(actualContents);
					}
				} else {
					final URL expectedReportOutput = new URL(FileLocator.toFileURL(new URL(scenarioURL.toString() + "reports" + "." + shortName + "." + extension)).toString().replaceAll(" ", "%20"));
					final StringBuilder expectedOutputBuilder = new StringBuilder();
					{
						try (InputStream is = expectedReportOutput.openStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
							String line = reader.readLine();
							if (line != null) {
								expectedOutputBuilder.append(line);
							}
							while (line != null) {
								line = reader.readLine();
								if (line != null) {
									expectedOutputBuilder.append("\n");
									expectedOutputBuilder.append(line);
								}
							}
						}
					}
					doValidate(actualContents, expectedOutputBuilder);
				}

			});
		});
	}

	private static void doValidate(final String actualContents, final StringBuilder expectedOutputBuilder) {
		final String expectedOutput = expectedOutputBuilder.toString();
		final boolean valid = stripWhitespace.apply(expectedOutput

				.replaceAll("Sep([^t])", "Sept$1") // Java 16 Compat
				.replaceAll("\\(EET\\)", "(TRT)") // Java 16 Compat (New turkey timezone)
		).equals(stripWhitespace.apply(actualContents.replaceAll("Sep([^t])", "Sept$1") // Java 16 Compat
				.replaceAll("\\(EET\\)", "(TRT)") // Java 16 Compat (New turkey timezone)
		));
		if (!valid) {
			LOG.warn("Expected " + expectedOutput);
			LOG.warn("Actual " + actualContents);
			Assertions.assertEquals(expectedOutput, actualContents);
		}
	}

	public static String generatePinDiffReport(String reportID, ScenarioResult pinResult, ScenarioResult refResult) throws Exception {

		final ReportTesterHelper reportTester = new ReportTesterHelper();
		final String[] result = new String[1];

		reportTester.runReportTest(reportID, null, null, IReportContentsGenerator.class, generator -> result[0] = generator.getReportContents(pinResult, refResult, null).getHTMLContents());

		Assertions.assertNotNull(result[0]);
		final String actualContents = result[0];
		Assertions.assertNotNull(actualContents);
		return actualContents;

	}
	
	public static String generateAnalyticsSolutionReport(String reportID, AnalyticsSolution solution) throws Exception {
		
		final ReportTesterHelper reportTester = new ReportTesterHelper();
		final String[] result = new String[1];
		
		reportTester.runReportTest(reportID, null, null, IAnalyticSolutionGenerator.class, generator -> result[0] = generator.getReportContents(solution).getHTMLContents());
		
		Assertions.assertNotNull(result[0]);
		final String actualContents = result[0];
		Assertions.assertNotNull(actualContents);
		return actualContents;
		
	}
}
