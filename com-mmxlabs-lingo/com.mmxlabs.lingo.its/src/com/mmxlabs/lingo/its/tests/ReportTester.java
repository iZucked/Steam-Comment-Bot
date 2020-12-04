/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.util.CheckedBiConsumer;
import com.mmxlabs.common.util.CheckedConsumer;
import com.mmxlabs.lingo.its.tests.AbstractReportTester.ReportType;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.IReportContentsGenerator;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 * Helper class to open up a view, set the scenario selection provider to the given instance and adapt the result to a {@link IReportContents} instance.
 * 
 * @author Simon Goodall
 * 
 */
public class ReportTester {

	private static final Logger LOG = LoggerFactory.getLogger(ReportTester.class);
	private static final UnaryOperator<String> stripWhitespace = s -> s.replaceAll("\\s+", "").replaceAll("\r\n", "").replaceAll("\n", "");

	public static String generateReportsWithElement(final ScenarioModelRecord modelRecord, @NonNull final IScenarioDataProvider scenarioDataProvider, final String reportID, final ReportType type,
			final String elementID) throws Exception {

		Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);

		// A side-effect is the initial evaluation.
		LNGScenarioRunnerCreator.withLegacyEvaluationRunner(scenarioDataProvider, true, runner -> {
		});

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
		}

		Assertions.assertNotNull("Target element not found", elementID);
		final Object pTarget = target;

		Display.getDefault().syncExec(() -> {
			final ESelectionService selectionService = PlatformUI.getWorkbench().getService(ESelectionService.class);
			Assertions.assertNotNull(selectionService);
			selectionService.setPostSelection(pTarget);
		});

		final ReportTesterHelper reportTester = new ReportTesterHelper();
		final ScenarioResult scenarioResult = new ScenarioResult(modelRecord, ScenarioModelUtil.getScheduleModel(scenarioDataProvider));
		final IReportContents reportContents = reportTester.getReportContents(scenarioResult, reportID);

		Assertions.assertNotNull(reportContents);
		final String actualContents;

		switch (type) {
		case REPORT_HTML:
			actualContents = reportContents.getHTMLContents();
			break;
		case REPORT_JSON:
			actualContents = reportContents.getJSONContents();
			break;
		default:
			throw new IllegalArgumentException();
		}
		Assertions.assertNotNull(actualContents);
		return actualContents;
	}

	public static String generateReports(final ScenarioModelRecord modelRecord, @NonNull final IScenarioDataProvider scenarioDataProvider, final String reportID, final ReportType type)
			throws Exception {

		Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);

		// A side-effect is the initial evaluation.
		LNGScenarioRunnerCreator.withLegacyEvaluationRunner(scenarioDataProvider, true, runner -> {
		});

		final ReportTesterHelper reportTester = new ReportTesterHelper();
		final ScenarioResult scenarioResult = new ScenarioResult(modelRecord, ScenarioModelUtil.getScheduleModel(scenarioDataProvider));
		final IReportContents reportContents = reportTester.getReportContents(scenarioResult, reportID);

		Assertions.assertNotNull(reportContents);
		final String actualContents;

		switch (type) {
		case REPORT_HTML:
			actualContents = reportContents.getHTMLContents();
			break;
		case REPORT_JSON:
			actualContents = reportContents.getJSONContents();
			break;
		default:
			throw new IllegalArgumentException();
		}
		Assertions.assertNotNull(actualContents);
		return actualContents;
	}

	public static void testReportsWithElement(final ScenarioModelRecord instance, @NonNull final IScenarioDataProvider scenarioDataProvider, final URL scenarioURL, final String reportID,
			final String shortName, final ReportType type, final String elementID, @Nullable final Consumer<ScenarioModelRecord> preAction) throws Exception {
		testReports(instance, scenarioDataProvider, scenarioURL, reportID, shortName, type, (t) -> {

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
			}

			Assertions.assertNotNull("Target element not found", elementID);
			final Object pTarget = target;

			// Step 1 open the view, release UI thread
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					final ESelectionService selectionService = PlatformUI.getWorkbench().getService(ESelectionService.class);
					Assertions.assertNotNull(selectionService);
					selectionService.setPostSelection(pTarget);
				}
			});

			if (preAction != null) {
				preAction.accept(t);
			}

		});
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

		final ReportTesterHelper reportTester = new ReportTesterHelper();
		final ScenarioResult scenarioResult = new ScenarioResult(modelRecord, ScenarioModelUtil.getScheduleModel(scenarioDataProvider));
		final IReportContents reportContents = reportTester.getReportContents(scenarioResult, reportID);

		Assertions.assertNotNull(reportContents);
		final String actualContents;

		switch (type) {
		case REPORT_HTML:
			actualContents = reportContents.getHTMLContents();
			break;
		case REPORT_JSON:
			actualContents = reportContents.getJSONContents();
			break;
		default:
			throw new IllegalArgumentException();
		}
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

	public static void testPinDiffReports(final URL pinScenarioURL, final URL refScenarioURL, final String reportID, final String shortName, final String extension) throws Exception {

		ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(pinScenarioURL, (pinModelRecord, pinScenarioDataProvider) -> {
			ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(refScenarioURL, (refModelRecord, refScenarioDataProvider) -> {
				ReportTester.testPinDiffReports(pinModelRecord, pinScenarioDataProvider, refModelRecord, refScenarioDataProvider, pinScenarioURL, ReportTesterHelper.CHANGESET_REPORT_ID,
						ReportTesterHelper.CHANGESET_REPORT_SHORTNAME, "html");

			});
		});
	}
	
	public static void testVesselReportDiffs(final URL pinScenarioURL, final URL refScenarioURL, final String reportID, final String shortName, final String extension) throws Exception {

		ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(pinScenarioURL, (pinModelRecord, pinScenarioDataProvider) -> {
			ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(refScenarioURL, (refModelRecord, refScenarioDataProvider) -> {
				ReportTester.testPinDiffReports(pinModelRecord, pinScenarioDataProvider, refModelRecord, refScenarioDataProvider, pinScenarioURL, reportID,
						shortName, "html");

			});
		});
	}

	public static void testPinDiffReports(final ScenarioModelRecord pinInstance, final IScenarioDataProvider pinModelDataProvider, final ScenarioModelRecord refInstance,
			final IScenarioDataProvider refModelDataProvider, final URL scenarioURL, final String reportID, final String shortName, final String extension) throws Exception {
		Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);

		// A side-effect is the initial evaluation.

		LNGScenarioRunnerCreator.withLegacyEvaluationRunner(pinModelDataProvider, true, pinRunner -> {
			final ScenarioResult pinResult = new ScenarioResult(pinInstance, ScenarioModelUtil.getScheduleModel(pinRunner.getScenarioDataProvider()));
			LNGScenarioRunnerCreator.withLegacyEvaluationRunner(refModelDataProvider, true, refRunner -> {
				final ScenarioResult refResult = new ScenarioResult(refInstance, ScenarioModelUtil.getScheduleModel(refRunner.getScenarioDataProvider()));

				final ReportTesterHelper reportTester = new ReportTesterHelper();
				final String result[] = new String[1];

				reportTester.runReportTest(reportID, null, null, IReportContentsGenerator.class, (generator) -> {
					result[0] = generator.getStringContents(pinResult, refResult);
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

	public static void testActionPlanReport(final URL baseScenarioURL, final List<URL> scenarioURLs, final String reportID, final String shortName, final String extension) throws Exception {

		final CheckedConsumer<List<Pair<ScenarioModelRecord, IScenarioDataProvider>>, Exception> executeTestConsumer = orderedInstances -> {

			ReportTester.testActionPlanReport(orderedInstances, baseScenarioURL, ReportTesterHelper.ACTIONPLAN_REPORT_ID, ReportTesterHelper.ACTIONPLAN_REPORT_SHORTNAME, "html");
		};

		final List<Pair<ScenarioModelRecord, IScenarioDataProvider>> orderedInstances = new LinkedList<>();
		final CheckedBiConsumer<Integer, CheckedBiConsumer, Exception> generateDataConsumer = (index, action) -> {
			if (index == scenarioURLs.size()) {
				executeTestConsumer.accept(orderedInstances);
				return;
			}
			final URL url = scenarioURLs.get(index);
			ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(url, (instance, scenarioDataProvider) -> {
				orderedInstances.add(new Pair<>(instance, scenarioDataProvider));
				action.accept(index + 1, action);
			});
		};
		generateDataConsumer.accept(0, generateDataConsumer);
	}

	public static void testActionPlanReport(final List<Pair<ScenarioModelRecord, IScenarioDataProvider>> orderedInstances, final URL scenarioURL, final String reportID, final String shortName,
			final String extension) throws Exception {
		Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);

		final CheckedConsumer<List<ScenarioResult>, Exception> c = orderedResults -> {

			final ReportTesterHelper reportTester = new ReportTesterHelper();

			final IReportContents reportContents = reportTester.getActionPlanReportContents(orderedResults, reportID);

			Assertions.assertNotNull(reportContents);
			final String actualContents = reportContents.getHTMLContents();
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
		};

		final List<ScenarioResult> orderedResults = new LinkedList<>();
		final CheckedBiConsumer<Integer, CheckedBiConsumer, Exception> f = (a, b) -> {
			if (a == orderedInstances.size()) {
				c.accept(orderedResults);
				return;
			}

			final Pair<ScenarioModelRecord, IScenarioDataProvider> p = orderedInstances.get(a);
			final IScenarioDataProvider scenarioDataProvider = p.getSecond();
			try {
				LNGScenarioRunnerCreator.withLegacyEvaluationRunner(scenarioDataProvider, false, pinRunner -> {
					// Perform eval
				});
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
			final ScenarioResult result = new ScenarioResult(p.getFirst(), ScenarioModelUtil.getScheduleModel(scenarioDataProvider));
			orderedResults.add(result);

			b.accept(a + 1, b);
		};
		f.accept(0, f);
	}

	private static void doValidate(final String actualContents, final StringBuilder expectedOutputBuilder) {
		final String expectedOutput = expectedOutputBuilder.toString();
		final boolean valid = stripWhitespace.apply(expectedOutput).equals(stripWhitespace.apply(actualContents));
		if (!valid) {
			LOG.warn("Expected " + expectedOutput);
			LOG.warn("Actual " + actualContents);
			Assertions.assertEquals(expectedOutput, actualContents);
		}
	}

	public static String generatePinDiffReport(String reportID, ScenarioResult pinResult, ScenarioResult refResult) throws Exception {

		final ReportTesterHelper reportTester = new ReportTesterHelper();
		final String result[] = new String[1];

		reportTester.runReportTest(reportID, null, null, IReportContentsGenerator.class, generator -> result[0] = generator.getStringContents(pinResult, refResult));

		Assertions.assertNotNull(result[0]);
		final String actualContents = result[0];
		Assertions.assertNotNull(actualContents);
		return actualContents;

	}
}
