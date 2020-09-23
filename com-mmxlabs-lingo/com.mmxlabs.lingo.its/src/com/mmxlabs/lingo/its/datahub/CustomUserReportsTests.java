/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.datahub;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.mmxlabs.hub.DataHubActivator;
import com.mmxlabs.hub.preferences.DataHubPreferenceConstants;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.reports.customizable.CustomReportDefinition;
import com.mmxlabs.lingo.reports.customizable.CustomReportsRegistry;

@Testcontainers
@Tag(TestCategories.HUB_TEST)
public class CustomUserReportsTests {

	static final Logger logger = LoggerFactory.getLogger(CustomUserReportsTests.class);

	public static final int DATAHUB_PORT = 8090;
	static List<Integer> portPool = List.of(8090, 8091, 8092, 8093, 8094, 8095, 8096, 8097, 8098, 8099);
	static int timeout = 60000;
	static int availablePort = HubTestHelper.waitForAvailablePort(portPool, timeout);

	private static SWTWorkbenchBot bot;
	static String datahubHost;
	static String upstreamUrl;

	// @formatter:off
	@Container
	public static GenericContainer datahubContainer = new FixedHostPortGenericContainer("docker.mmxlabs.com/datahub-v:1.8.1-SNAPSHOT")
	.withFixedExposedPort(availablePort, DATAHUB_PORT)
	.withExposedPorts(DATAHUB_PORT)
	.withEnv("PORT", Integer.toString(DATAHUB_PORT))
	.withEnv("PROTOCOL", "http")
	.withEnv("HOSTNAME", "localhost")
	.withEnv("AUTHENTICATION_SCHEME", "basic")
	.withEnv("INSTANCE_TAG", "docker")
	.withEnv("DB_HOST", "0.0.0.0")
	.withEnv("DB_PORT", "27000")
	.withEnv("AZURE_CLIENT_ID", "e52d83a9-40c0-42ae-aae3-d5054ef24919")
	.withEnv("AZURE_TENANT_ID", "dceff11f-6e74-436e-b9a0-65f9697b8472")
	.withEnv("AZURE_CLIENT_SECRET", "@t:1uqW2cYN1iH7S]RQqBiHgchhvEr/[")
	.withEnv("AZURE_GROUPS", "MinimaxUsers, MinimaxLingo, MinimaxBasecase")
	.withEnv("AZURE_BASECASE_GROUP_ID", "452fe6d8-7360-47fd-b5b5-8cd8108d9233")
	.withEnv("AZURE_BASECASE_GROUP_NAME", "MinimaxBasecase")
	.withEnv("AZURE_LINGO_GROUP_ID", "80a34e39-50d3-405d-88b0-3522307dfed8")
	.withEnv("AZURE_LINGO_GROUP_NAME", "MinimaxLingo")
	.withEnv("AZURE_USERS_GROUP_ID", "287686ff-d399-4875-b86d-1dd9426973d6")
	.withEnv("AZURE_USERS_GROUP_NAME", "MinimaxUsers")
	.waitingFor(Wait.forLogMessage(".*Started ServerConnector.*", 1));
	// @formatter:on

	String REPORT_NAME = "test";

	@BeforeAll
	private static void beforeAll() {
		bot = new SWTWorkbenchBot();
	}

	@AfterAll
	public static void afterAll() {
		deleteAllUserReports();
		bot.resetWorkbench();
		bot.button("Cancel").click();
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
		bot.textWithLabel("Choose name for the new custom report.").setText(name);
		bot.button("OK").click();
		bot.button("Save").click();
	}

	public void renameReport(String from, String to) {
		bot.table().select(from);
		bot.button("Rename").click();
		bot.textWithLabel("Choose a new name for the report.").setText(to);
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
			System.out.println(shell.getId());
			System.out.println(shell.getText());
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
		bot.textWithLabel("Choose name for the new custom report.").setText(REPORT_NAME);

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
		bot.textWithLabel("Choose a new name for the report.").setText(REPORT_NAME);

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
