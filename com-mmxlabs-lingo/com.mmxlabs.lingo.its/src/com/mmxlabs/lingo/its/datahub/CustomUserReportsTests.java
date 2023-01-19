/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.datahub;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit5.SWTBotJunit5Extension;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.mmxlabs.hub.DataHubActivator;
import com.mmxlabs.hub.preferences.DataHubPreferenceConstants;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.reports.customizable.CustomReportDefinition;
import com.mmxlabs.lingo.reports.customizable.CustomReportsManagerDialog;
import com.mmxlabs.lingo.reports.customizable.CustomReportsRegistry;
import com.mmxlabs.rcp.common.appversion.VersionHelper;

@Testcontainers
@Tag(TestCategories.HUB_TEST)
@ExtendWith(SWTBotJunit5Extension.class)
public class CustomUserReportsTests {

	static final Logger logger = LoggerFactory.getLogger(CustomUserReportsTests.class);

	public static final int DATAHUB_PORT = 8090;
	static List<Integer> portPool = List.of(8090, 8091, 8092, 8093, 8094, 8095, 8096, 8097, 8098, 8099);
	static int timeout = 60000;
	static int availablePort = HubTestHelper.waitForAvailablePort(portPool, timeout);

	private static SWTWorkbenchBot bot;
	static String datahubHost;
	static String upstreamUrl;

	static final String CONTAINER = HubTestHelper.getContainerString(VersionHelper.getInstance().getClientCode());

	@Container
	public static GenericContainer datahubContainer = HubTestHelper.createDataHubContainer(CONTAINER, availablePort, DATAHUB_PORT, false);

	String REPORT_NAME = "test";

	@BeforeAll
	private static void beforeAll() {
		bot = new SWTWorkbenchBot();
	}

	@AfterAll
	public static void afterAll() {
		deleteAllUserReports();
		bot.resetWorkbench();
		bot = null;
	}

	@BeforeEach
	public void beforeEach() {
		bot.resetWorkbench();
		deleteAllUserReports();
	}

	public static void setDatahubUrl() {
		DataHubActivator.getDefault().getPreferenceStore().setValue(DataHubPreferenceConstants.P_DATAHUB_URL_KEY, upstreamUrl);
	}

	public void openReportsManager() {
		bot.menu("Window").menu("Reports Manager...").click();
	}

	public void createReport(String name) {
		bot.button("New").click();
		bot.textWithId(CustomReportsManagerDialog.WIDGET_REPORT_NAME_TEXT).setText(name);
		bot.button("OK").click();
		bot.button("Save").click();
	}

	public void renameReport(String from, String to) {
		bot.table().select(from);
		bot.button("Rename").click();
		bot.textWithId(CustomReportsManagerDialog.WIDGET_REPORT_NAME_TEXT).setText(to);
		bot.button("OK").click();
	}

	public void deleteReport(String name) {
		bot.table().select(name);
		bot.button("Delete").click();
		bot.button("Yes").click();
	}

	public static void deleteAllUserReports() {
		// delete all intermediate user reports
		List<CustomReportDefinition> userReports = CustomReportsRegistry.getInstance().readUserCustomReportDefinitions();
		userReports.forEach(userReport -> CustomReportsRegistry.getInstance().deleteUserReport(userReport));
	}

	public boolean viewWithTitleExists(SWTBotShell[] shells, @NonNull String title) {
		boolean exists = false;
		for (SWTBotShell shell : shells) {
			logger.info(shell.getText());
			if (title.equals(shell.getText())) {
				exists = true;
			}
		}
		return exists;
	}

	@Test
	public void newButtonIsEnabledWhenOpenningReportsManager() {
		openReportsManager();
		assertTrue(bot.button("New").isEnabled());
	}

	@Test
	public void buttonsAreEnabledWhenReportIsSelected() {
		openReportsManager();
		createReport(REPORT_NAME);
		bot.table().select(REPORT_NAME);

		assertTrue(bot.button("Copy").isEnabled());
		assertTrue(bot.button("Rename").isEnabled());
		assertTrue(bot.button("Delete").isEnabled());
		// assertTrue(bot.button("Add to Team").isEnabled());
	}

	@Test
	public void warningMessageIsDisplayedWhenChangingSeletedReportWithoutSaving() {
		String anotherTeamReportName = "anotherTest";
		openReportsManager();
		createReport(REPORT_NAME);
		createReport(anotherTeamReportName);

		bot.table().select(REPORT_NAME);
		bot.tableWithLabel("Disabled Columns").select("Ballast Canal Date");
		// bot.table().select("Ballast Canal Date");
		bot.button("Show").click();
		bot.table().select(anotherTeamReportName);

		// Warning dialog pops up
		SWTBotShell[] shells = bot.shells();
		assertTrue(viewWithTitleExists(shells, "Warning"));
	}

	@Test
	public void createUserReport() {
		openReportsManager();
		createReport(REPORT_NAME);
		List<CustomReportDefinition> userReports = CustomReportsRegistry.getInstance().readUserCustomReportDefinitions();
		// @formatter:off
		Optional<CustomReportDefinition> optionalUserReport = userReports.stream()
				.filter(userReport -> userReport.getName().equals("test"))
				.findFirst();
		// @formatter:on

		assertTrue(optionalUserReport.isPresent());
	}

	@Test
	public void createReportWIthConflictingNameIsNotAllowed() {
		openReportsManager();
		createReport(REPORT_NAME);
		// create test report with exising name
		bot.button("New").click();
		bot.textWithId(CustomReportsManagerDialog.WIDGET_REPORT_NAME_TEXT).setText(REPORT_NAME);

		assertFalse(bot.button("OK").isEnabled());
	}

	@Test
	public void renameUserReport() {
		String newUserReportName = "anotherTestName";
		openReportsManager();
		createReport(REPORT_NAME);
		renameReport(REPORT_NAME, newUserReportName);
		List<CustomReportDefinition> userReports = CustomReportsRegistry.getInstance().readUserCustomReportDefinitions();
		// @formatter:off
		Optional<CustomReportDefinition> optionalUserReport = userReports.stream()
				.filter(userReport -> userReport.getName().equals(newUserReportName))
				.findFirst();
		// @formatter:on

		assertTrue(optionalUserReport.isPresent());
	}

	@Test
	public void renameExistingReportToConflictingNameIsNotAllowed() {
		String newUserReportName = "anotherTestName";
		openReportsManager();
		createReport(REPORT_NAME);
		createReport(newUserReportName);

		// rename test report to new user report
		bot.table().select(newUserReportName);
		bot.button("Rename").click();
		bot.textWithId(CustomReportsManagerDialog.WIDGET_REPORT_NAME_TEXT).setText(REPORT_NAME);

		// verify that OK button is disabled
		assertFalse(bot.button("OK").isEnabled());
	}

	@Test
	public void deleteUserReport() {
		openReportsManager();
		createReport(REPORT_NAME);
		deleteReport(REPORT_NAME);
		List<CustomReportDefinition> userReports = CustomReportsRegistry.getInstance().readUserCustomReportDefinitions();
		// @formatter:off
		Long numberUserReports = userReports.stream()
				.filter(userReport -> userReport.getName().equals(REPORT_NAME))
				.count();
		// @formatter:on
		assertTrue(numberUserReports.equals(0L));
	}

}
