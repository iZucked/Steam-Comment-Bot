/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.dynamic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Lists;
import com.mmxlabs.lingo.its.tests.AbstractReportTester.ReportType;
import com.mmxlabs.lingo.its.tests.ReportTester;
import com.mmxlabs.lingo.its.tests.ReportTesterHelper;
import com.mmxlabs.lingo.its.tests.ReportTesterHelper.ReportRecord;
import com.mmxlabs.lingo.its.tests.TestMode;
import com.mmxlabs.lingo.its.tests.TestingModes;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.IReportContent;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.IReportPublisherExtension;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.ReportPublisherExtensionUtil;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.TodayProvider;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

public class ReportTestRunner {

	private static final Collection<String> IGNORED_PREFIXES;
	static {
		IGNORED_PREFIXES = new ArrayList<>();
		IGNORED_PREFIXES.add("value-matrix-");
	}

	public static List<DynamicNode> runHubReportTests(final File baseDirectory) throws IOException {
		Assumptions.assumeTrue(TestingModes.ReportTestMode != TestMode.Skip);

		final Iterable<IReportPublisherExtension> publishers = ReportPublisherExtensionUtil.getReportPublishers();
		final ArrayList<IReportPublisherExtension> reportRecords = Lists.newArrayList(publishers);

		final List<DynamicNode> allCases = new LinkedList<>();
		if (baseDirectory.exists() && baseDirectory.isDirectory()) {
			for (final File f : baseDirectory.listFiles()) {
				if (f.isDirectory()) {
					final String name = f.getName();
					final File scenarioFile = new File(f, "scenario.lingo");
					final File[] localFiles = f.listFiles();
					final boolean isIgnored = Arrays.stream(localFiles) //
							.filter(File::isFile) //
							.map(File::getName) //
							.filter(filename -> filename.endsWith(".json")) //
							.anyMatch(filename -> IGNORED_PREFIXES.stream().anyMatch(filename::startsWith));
					if (scenarioFile.exists() && !isIgnored) {
						final List<DynamicNode> scenarioCases = new LinkedList<>();

						for (final IReportPublisherExtension t : reportRecords) {
							for (final String version : t.getVersions()) {
								final String reportID = t.getReportType() + "-" + version;

								scenarioCases.add(DynamicTest.dynamicTest(name + " " + reportID, () -> {
									ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(scenarioFile.toURI().toURL(), (modelRecord, scenarioDataProvider) -> {

										final File todayFile = new File(scenarioFile.getParentFile(), "today.json");
										try {
											if (todayFile.exists()) {
												final ObjectMapper mapper = new ObjectMapper();
												mapper.registerModule(new JavaTimeModule());
												final LocalDateTime todayValue = mapper.readValue(todayFile, LocalDateTime.class);
												TodayProvider.getInstance().setOverrride(todayValue);
											}

											// Files.writeString(reportFile.toPath(), actual);
											final IReportContent reportContent = t.publishReport(version, scenarioDataProvider, ScenarioModelUtil.getScheduleModel(scenarioDataProvider));

											String actual = reportContent.getContent();
											final String ext = t.getFileExtension();
											if (".json".equals(ext)) {
												// Run JSON through pretty printer
												final ObjectMapper mapper = new ObjectMapper();
												final Object json = mapper.readValue(actual, Object.class);

												actual = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
											}

											final File resultsFolder = new File(scenarioFile.getParentFile(), "results");
											resultsFolder.mkdir();
											final File reportFile = new File(resultsFolder, "hub-" + reportID + ext);

											if (TestingModes.ReportTestMode == TestMode.Generate) {
												Files.writeString(reportFile.toPath(), actual);
											} else if (TestingModes.ReportTestMode == TestMode.Run) {
												final String expected = Files.readString(reportFile.toPath());
												Assertions.assertEquals(expected.replaceAll("\\r\\n", "\n") //
														, actual.replaceAll("\\r\\n", "\n") //
														, String.format("Mismatching result: %s for %s", reportID, f.getName()));
											}
										} finally {
											TodayProvider.getInstance().unsetOverrride();
										}
									});
								}));
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
					final File[] localFiles = f.listFiles();
					final boolean isIgnored = Arrays.stream(localFiles) //
							.filter(File::isFile) //
							.map(File::getName) //
							.filter(filename -> filename.endsWith(".json")) //
							.anyMatch(filename -> IGNORED_PREFIXES.stream().anyMatch(filename::startsWith));
					if (scenarioFile.exists() && !isIgnored) {
						final List<DynamicNode> scenarioCases = new LinkedList<>();

						for (final ReportRecord t : reportRecords) {
							final String reportID = t.reportID();
							scenarioCases.add(DynamicTest.dynamicTest(name + " " + t.fileNameCode(), () -> {
								ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(scenarioFile.toURI().toURL(), (modelRecord, scenarioDataProvider) -> {
									final String actual = ReportTester.runReportsTest(modelRecord, scenarioDataProvider, reportID, t.reportType());

									final File resultsFolder = new File(scenarioFile.getParentFile(), "results");
									resultsFolder.mkdir();
									final String ext = t.reportType() == ReportType.REPORT_HTML ? ".html" : ".json";
									final File reportFile = new File(resultsFolder, t.fileNameCode() + ext);

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
								final String reportID = t.reportID();
								elementCases.add(DynamicTest.dynamicTest(name + " " + t.fileNameCode(), () -> {
									ScenarioStorageUtil.withExternalScenarioFromResourceURLConsumer(scenarioFile.toURI().toURL(), (modelRecord, scenarioDataProvider) -> {
										final String actual = ReportTester.runReportsTestWithElement(modelRecord, scenarioDataProvider, reportID, t.reportType(), elementID);

										final File resultsFolder = new File(scenarioFile.getParentFile(), "results");
										resultsFolder.mkdir();

										final String encodedID = encodeElementID(elementID);
										final String ext = t.reportType() == ReportType.REPORT_HTML ? ".html" : ".json";

										final File reportFile = new File(resultsFolder, t.fileNameCode() + "." + encodedID + ext);

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
