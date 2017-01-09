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
import java.util.function.Consumer;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Helper class to open up a view, set the scenario selection provider to the given instance and adapt the result to a {@link IReportContents} instance.
 * 
 * @author Simon Goodall
 * 
 */
public class ReportTester {

	private static final Logger LOG = LoggerFactory.getLogger(ReportTester.class);

	// Never commit as true
	private static final boolean storeReports = false;

	public static void testReports(final ScenarioInstance instance, final URL scenarioURL, final String reportID, final String shortName, final String extension, @Nullable Consumer<ScenarioInstance> preAction) throws Exception {

		// A side-effect is the initial evaluation.
		final LNGScenarioRunner runner = LNGScenarioRunnerCreator.createScenarioRunnerForEvaluation((LNGScenarioModel) instance.getInstance(), true);

		if (preAction != null) {
			preAction.accept(instance);
		}
		
		final ReportTesterHelper reportTester = new ReportTesterHelper();
		final IReportContents reportContents = reportTester.getReportContents(instance, reportID);

		Assert.assertNotNull(reportContents);
		final String actualContents = reportContents.getStringContents();
		Assert.assertNotNull(actualContents);
		if (storeReports) {

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

		// A side-effect is the initial evaluation.
		final LNGScenarioRunner pinRunner = LNGScenarioRunnerCreator.createScenarioRunnerForEvaluation((LNGScenarioModel) pinInstance.getInstance(), true);
		final LNGScenarioRunner refRunner = LNGScenarioRunnerCreator.createScenarioRunnerForEvaluation((LNGScenarioModel) refInstance.getInstance(), true);

		final ReportTesterHelper reportTester = new ReportTesterHelper();
		final IReportContents reportContents = reportTester.getReportContents(pinInstance, refInstance, reportID);

		Assert.assertNotNull(reportContents);
		final String actualContents = reportContents.getStringContents();
		Assert.assertNotNull(actualContents);
		if (storeReports) {

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
