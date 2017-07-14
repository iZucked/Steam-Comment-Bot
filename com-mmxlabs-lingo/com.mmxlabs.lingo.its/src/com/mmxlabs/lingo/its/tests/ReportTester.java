/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.junit.Assert;
import org.junit.Assume;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.util.CheckedBiConsumer;
import com.mmxlabs.common.util.CheckedConsumer;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 * Helper class to open up a view, set the scenario selection provider to the given instance and adapt the result to a {@link IReportContents} instance.
 * 
 * @author Simon Goodall
 * 
 */
public class ReportTester {

	private static final Logger LOG = LoggerFactory.getLogger(ReportTester.class);

	// Never commit as true
	private static final TestMode storeReports = TestMode.Run;

	public static void testReportsWithElement(final ScenarioInstance instance, @NonNull LNGScenarioModel scenarioModel, final URL scenarioURL, final String reportID, final String shortName,
			final String extension, String elementID, @Nullable Consumer<ScenarioInstance> preAction) throws Exception {
		testReports(instance, scenarioModel, scenarioURL, reportID, shortName, extension, (t) -> {

			Object target = null;
			ScheduleModel scheduleModel = scenarioModel.getScheduleModel();
			Schedule schedule = scheduleModel.getSchedule();
			if (schedule != null) {
				for (CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
					if (elementID.equals(cargoAllocation.getName())) {
						target = cargoAllocation;
						break;
					}
				}
			}

			Assert.assertNotNull("Target element not found", elementID);
			final Object pTarget = target;

			// Step 1 open the view, release UI thread
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					final ESelectionService selectionService = PlatformUI.getWorkbench().getService(ESelectionService.class);
					Assert.assertNotNull(selectionService);
					selectionService.setPostSelection(pTarget);
				}
			});

			if (preAction != null) {
				preAction.accept(t);
			}

		});
	}

	public static void testReports(final @NonNull ScenarioInstance instance, @NonNull LNGScenarioModel scenarioModel, final URL scenarioURL, final String reportID, final String shortName,
			final String extension, @Nullable Consumer<ScenarioInstance> preAction) throws Exception {

		Assume.assumeTrue(storeReports != TestMode.Skip);

		// A side-effect is the initial evaluation.
		// A side-effect is the initial evaluation.
		LNGScenarioRunnerCreator.withLegacyEvaluationRunner(scenarioModel, true, runner -> {

			if (preAction != null) {
				preAction.accept(instance);
			}

			final ReportTesterHelper reportTester = new ReportTesterHelper();
			ScenarioResult scenarioResult = new ScenarioResult(instance, ScenarioModelUtil.getScheduleModel(scenarioModel));
			final IReportContents reportContents = reportTester.getReportContents(scenarioResult, reportID);

			Assert.assertNotNull(reportContents);
			final String actualContents = reportContents.getStringContents();
			Assert.assertNotNull(actualContents);
			if (storeReports == TestMode.Generate) {

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
				if (!expectedOutputBuilder.toString().equals(actualContents)) {
					LOG.warn("Expected " + expectedOutputBuilder.toString());
					LOG.warn("Actual " + actualContents);
				}
				Assert.assertEquals(expectedOutputBuilder.toString(), actualContents);
			}
		});
	}

	public static void testPinDiffReports(final ScenarioInstance pinInstance, ModelReference pinModelReference, ScenarioInstance refInstance, ModelReference refModelReference, final URL scenarioURL,
			final String reportID, final String shortName, final String extension) throws Exception {
		Assume.assumeTrue(storeReports != TestMode.Skip);

		// A side-effect is the initial evaluation.

		LNGScenarioRunnerCreator.withLegacyEvaluationRunner((LNGScenarioModel) pinModelReference.getInstance(), true, pinRunner -> {
			ScenarioResult pinResult = new ScenarioResult(pinInstance, ScenarioModelUtil.getScheduleModel(pinRunner.getScenario()));
			LNGScenarioRunnerCreator.withLegacyEvaluationRunner((LNGScenarioModel) refModelReference.getInstance(), true, refRunner -> {
				ScenarioResult refResult = new ScenarioResult(refInstance, ScenarioModelUtil.getScheduleModel(refRunner.getScenario()));

				final ReportTesterHelper reportTester = new ReportTesterHelper();
				final IReportContents reportContents = reportTester.getReportContents(pinResult, refResult, reportID);

				Assert.assertNotNull(reportContents);
				final String actualContents = reportContents.getStringContents();
				Assert.assertNotNull(actualContents);
				if (storeReports == TestMode.Generate) {

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
					if (!expectedOutputBuilder.toString().equals(actualContents)) {
						LOG.warn("Expected " + expectedOutputBuilder.toString());
						LOG.warn("Actual " + actualContents);
					}
					Assert.assertEquals(expectedOutputBuilder.toString(), actualContents);
				}

			});
		});
	}

	public static void testActionPlanReport(final List<Pair<ScenarioInstance, ModelReference>> orderedInstances, final URL scenarioURL, final String reportID, final String shortName,
			final String extension) throws Exception {
		Assume.assumeTrue(storeReports != TestMode.Skip);

		CheckedConsumer<List<ScenarioResult>, Exception> c = orderedResults -> {

			final ReportTesterHelper reportTester = new ReportTesterHelper();

			final IReportContents reportContents = reportTester.getActionPlanReportContents(orderedResults, reportID);

			Assert.assertNotNull(reportContents);
			final String actualContents = reportContents.getStringContents();
			Assert.assertNotNull(actualContents);
			if (storeReports == TestMode.Generate) {

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
				if (!expectedOutputBuilder.toString().equals(actualContents)) {
					LOG.warn("Expected " + expectedOutputBuilder.toString());
					LOG.warn("Actual " + actualContents);
				}
				Assert.assertEquals(expectedOutputBuilder.toString(), actualContents);
			}
		};

		List<ScenarioResult> orderedResults = new LinkedList<>();
		final CheckedBiConsumer<Integer, CheckedBiConsumer, Exception> f = (a, b) -> {
			if (a == orderedInstances.size()) {
				c.accept(orderedResults);
				return;
			}

			Pair<ScenarioInstance, ModelReference> p = orderedInstances.get(a);
			LNGScenarioModel scenarioModel = (LNGScenarioModel) p.getSecond().getInstance();
			try {
				LNGScenarioRunnerCreator.withLegacyEvaluationRunner(scenarioModel, false, pinRunner -> {
					// Perform eval
				});
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			ScenarioResult result = new ScenarioResult(p.getFirst(), ScenarioModelUtil.getScheduleModel(scenarioModel));
			orderedResults.add(result);

			b.accept(a + 1, b);
		};
		f.accept(0, f);
	}
}
