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
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.junit.Assert;
import org.junit.Assume;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
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

	public static void testReportsWithElement(final ScenarioInstance instance, final URL scenarioURL, final String reportID, final String shortName, final String extension, String elementID,
			@Nullable Consumer<ScenarioInstance> preAction) throws Exception {
		testReports(instance, scenarioURL, reportID, shortName, extension, (t) -> {

			LNGScenarioModel lngScenarioModel = (LNGScenarioModel) instance.getInstance();
			Object target = null;
			ScheduleModel scheduleModel = lngScenarioModel.getScheduleModel();
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

	public static void testReports(final ScenarioInstance instance, final URL scenarioURL, final String reportID, final String shortName, final String extension,
			@Nullable Consumer<ScenarioInstance> preAction) throws Exception {

		Assume.assumeTrue(storeReports != TestMode.Skip);

		// A side-effect is the initial evaluation.
		LNGScenarioModel lngScenarioModel = (LNGScenarioModel) instance.getInstance();
		final LNGScenarioRunner runner = LNGScenarioRunnerCreator.createScenarioRunnerForEvaluation(lngScenarioModel, true);

		if (preAction != null) {
			preAction.accept(instance);
		}

		final ReportTesterHelper reportTester = new ReportTesterHelper();
		ScenarioResult scenarioResult = new ScenarioResult(instance, ScenarioModelUtil.getScheduleModel(lngScenarioModel));
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
	}

	public static void testPinDiffReports(final ScenarioInstance pinInstance, ScenarioInstance refInstance, final URL scenarioURL, final String reportID, final String shortName,
			final String extension) throws Exception {
		Assume.assumeTrue(storeReports != TestMode.Skip);

		// A side-effect is the initial evaluation.
		final LNGScenarioRunner pinRunner = LNGScenarioRunnerCreator.createScenarioRunnerForEvaluation((LNGScenarioModel) pinInstance.getInstance(), true);
		final LNGScenarioRunner refRunner = LNGScenarioRunnerCreator.createScenarioRunnerForEvaluation((LNGScenarioModel) refInstance.getInstance(), true);

		final ReportTesterHelper reportTester = new ReportTesterHelper();

		ScenarioResult pinResult = new ScenarioResult(pinInstance, ScenarioModelUtil.getScheduleModel((LNGScenarioModel) pinInstance.getInstance()));
		ScenarioResult refResult = new ScenarioResult(refInstance, ScenarioModelUtil.getScheduleModel((LNGScenarioModel) refInstance.getInstance()));

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
	}

	public static void testActionPlanReport(final List<ScenarioInstance> orderedInstances, final URL scenarioURL, final String reportID, final String shortName, final String extension)
			throws Exception {
		Assume.assumeTrue(storeReports != TestMode.Skip);

		final ReportTesterHelper reportTester = new ReportTesterHelper();

		List<ScenarioResult> orderedResults = new LinkedList<>();
		for (ScenarioInstance instance : orderedInstances) {
			// A side-effect is the initial evaluation.
			LNGScenarioRunnerCreator.createScenarioRunnerForEvaluation((LNGScenarioModel) instance.getInstance(), false);

			ScenarioResult result = new ScenarioResult(instance, ScenarioModelUtil.getScheduleModel((LNGScenarioModel) instance.getInstance()));
			orderedResults.add(result);
		}

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
	}
}
