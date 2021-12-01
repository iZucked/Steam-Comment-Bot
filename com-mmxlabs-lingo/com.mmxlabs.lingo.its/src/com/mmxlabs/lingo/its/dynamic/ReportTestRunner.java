/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.dynamic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lingo.its.tests.AbstractReportTester.ReportType;
import com.mmxlabs.lingo.its.tests.ReportTester;
import com.mmxlabs.lingo.its.tests.ReportTesterHelper;
import com.mmxlabs.lingo.its.tests.ReportTesterHelper.ReportRecord;
import com.mmxlabs.lingo.its.tests.TestMode;
import com.mmxlabs.lingo.its.tests.TestingModes;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

public class ReportTestRunner {

	public static List<DynamicNode> runReportTests(final File baseDirectory, @Nullable final List<ReportRecord> extraReports) throws IOException {
		Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);

		final List<ReportRecord> reportRecords = ReportTesterHelper.createReportTests();
		if (extraReports != null) {
			reportRecords.addAll(extraReports);
		}

		final List<DynamicNode> allCases = new LinkedList<>();
		if (baseDirectory.exists() && baseDirectory.isDirectory()) {
			for (final File f : baseDirectory.listFiles()) {
				if (f.isDirectory()) {
					final String name = f.getName();
					final File scenarioFile = new File(f, "scenario.lingo");
					if (scenarioFile.exists()) {
						final List<DynamicNode> scenarioCases = new LinkedList<>();

						for (final ReportRecord t : reportRecords) {
							final String reportID = t.getReportID();
							scenarioCases.add(DynamicTest.dynamicTest(t.getFileNameCode(), () -> {
								ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(scenarioFile.toURI().toURL(), (modelRecord, scenarioDataProvider) -> {
									final String actual = ReportTester.runReportsTest(modelRecord, scenarioDataProvider, reportID, t.getReportType());

									final File resultsFolder = new File(scenarioFile.getParentFile(), "results");
									resultsFolder.mkdir();
									final String ext = t.getReportType() == ReportType.REPORT_HTML ? ".html" : ".json";
									final File reportFile = new File(resultsFolder, t.getFileNameCode() + ext);

									if (TestingModes.ReportTestMode == TestMode.Generate) {
										Files.writeString(reportFile.toPath(), actual);
									} else if (TestingModes.ReportTestMode == TestMode.Run) {
										final String expected = Files.readString(reportFile.toPath());
										Assertions.assertEquals(expected.replaceAll("\\r\\n", "\n")
												.replaceAll("Sep([^t])", "Sept$1") // Java 16 Compat
												.replaceAll("\\(EET\\)", "(TRT)") // Java 16 Compat (New turkey timezone)
												, actual.replaceAll("\\r\\n", "\n")
												.replaceAll("Sep([^t])", "Sept$1") // Java 16 Compat
												.replaceAll("\\(EET\\)", "(TRT)") // Java 16 Compat (New turkey timezone)
												, String.format("Mismatching result: %s for %s", reportID, f.getName()));
									}
								});
							}));
						}
						if (!scenarioCases.isEmpty()) {
							allCases.add(DynamicContainer.dynamicContainer(name, scenarioCases));
						}

					}
				}
			}
		}
		return allCases;
	}

	public static List<DynamicNode> runReportWithElementTests(final File baseDirectory) throws IOException {

		final List<DynamicNode> allCases = new LinkedList<>();
		if (baseDirectory.exists() && baseDirectory.isDirectory()) {
			for (final File f : baseDirectory.listFiles()) {
				if (f.isDirectory()) {
					final String name = f.getName();
					final File scenarioFile = new File(f, "scenario.lingo");
					final File cargoesFile = new File(f, "cargoes.json");
					if (scenarioFile.exists() && cargoesFile.exists()) {
						final ObjectMapper objectMapper = new ObjectMapper();
						final JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
						final List<String> cargoes = objectMapper.readValue(new File(scenarioFile.getParentFile(), "cargoes.json"), listType);
						final List<DynamicNode> scenarioCases = new LinkedList<>();

						for (final String elementID : cargoes) {
							final List<DynamicNode> elementCases = new LinkedList<>();

							for (final ReportRecord t : ReportTesterHelper.createSelectedElementReportTests()) {
								final String reportID = t.getReportID();
								elementCases.add(DynamicTest.dynamicTest(t.getFileNameCode(), () -> {
									ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(scenarioFile.toURI().toURL(), (modelRecord, scenarioDataProvider) -> {
										final String actual = ReportTester.runReportsTestWithElement(modelRecord, scenarioDataProvider, reportID, t.getReportType(), elementID);

										final File resultsFolder = new File(scenarioFile.getParentFile(), "results");
										resultsFolder.mkdir();

										final String encodedID = encodeElementID(elementID);
										final String ext = t.getReportType() == ReportType.REPORT_HTML ? ".html" : ".json";

										final File reportFile = new File(resultsFolder, t.getFileNameCode() + "." + encodedID + ext);

										if (TestingModes.ReportTestMode == TestMode.Generate) {
											Files.writeString(reportFile.toPath(), actual);
										} else if (TestingModes.ReportTestMode == TestMode.Run) {
											final String expected = Files.readString(reportFile.toPath());
											Assertions.assertEquals(expected.replaceAll("\\r\\n", "\n"), actual.replaceAll("\\r\\n", "\n"));
										}
									});
								}));
							}
							if (!elementCases.isEmpty()) {
								scenarioCases.add(DynamicContainer.dynamicContainer(elementID, elementCases));
							}
						}
						if (!scenarioCases.isEmpty()) {
							allCases.add(DynamicContainer.dynamicContainer(name, scenarioCases));
						}

					}
				}
			}
		}
		return allCases;
	}

	/**
	 * The element id will form part of the filename, so make sure we have safe chars
	 * 
	 * @param id
	 * @return
	 */
	private static String encodeElementID(final String id) {
		return id.replaceAll("[^-a-zA-Z0-9\\._]", "_");
	}

}
