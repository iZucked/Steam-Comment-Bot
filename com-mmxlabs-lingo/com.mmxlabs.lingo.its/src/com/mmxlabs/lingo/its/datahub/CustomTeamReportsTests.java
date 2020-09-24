package com.mmxlabs.lingo.its.datahub;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.auth.BasicAuthenticationManager;
import com.mmxlabs.lingo.reports.customizable.CustomReportDefinition;
import com.mmxlabs.lingo.reports.customizable.CustomReportsRegistry;

@Testcontainers
public class CustomTeamReportsTests {
	// Team reports require the lingo user to be authenticated. We can use either basic auth or oauth. We arbitrarily decide to use basic
	private static BasicAuthenticationManager basicAuthenticationManager = BasicAuthenticationManager.getInstance();
	static final Logger logger = LoggerFactory.getLogger(CustomTeamReportsTests.class);

	public static final int DATAHUB_PORT = 8090;
	public static final String USERNAME = "u_basecase";
	public static final String PASSWORD = "u_basecase";
	public final String REPORT_NAME = "test";

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

	@BeforeAll
	private static void beforeAll() {
		datahubHost = datahubContainer.getHost();
		upstreamUrl = String.format("http://%s:%s", datahubHost, availablePort);
		System.out.println(upstreamUrl);
		bot = new SWTWorkbenchBot();
		HubTestHelper.setDatahubUrl(upstreamUrl);
		// login with basic auth
		if (basicAuthenticationManager.checkCredentials(upstreamUrl, USERNAME, PASSWORD)) {
			basicAuthenticationManager.storeInSecurePreferences("username", USERNAME);
			basicAuthenticationManager.storeInSecurePreferences("password", PASSWORD);
		}
		// force trigger refresh
		UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
	}

	@AfterAll
	public static void afterAll() {
		deleteAllReports();
		basicAuthenticationManager.logout(upstreamUrl, (Shell) null);
		bot.resetWorkbench();
		bot = null;
	}

	@BeforeEach
	public void beforeEach() {
		bot.resetWorkbench();
		deleteAllReports();
	}

	public static void deleteAllReports() {
		deleteTeamReports();
		deleteUserReports();
	}

	public static void deleteTeamReports() {
		try {
			CustomReportsRegistry.getInstance().refreshTeamReports();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<CustomReportDefinition> teamReports = CustomReportsRegistry.getInstance().readTeamCustomReportDefinitions();
		teamReports.forEach(teamReport -> CustomReportsRegistry.getInstance().deleteTeamReport(teamReport));
		teamReports.forEach(teamReport -> {
			try {
				CustomReportsRegistry.getInstance().removeFromDatahub(teamReport);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public static void deleteUserReports() {
		List<CustomReportDefinition> userReports = CustomReportsRegistry.getInstance().readUserCustomReportDefinitions();
		userReports.forEach(userReport -> CustomReportsRegistry.getInstance().deleteUserReport(userReport));
	}

	public void openUserReportsManager() {
		bot.menu("Window").menu("Reports Manager...").click();
	}

	public void openTeamReportsManager() {
		bot.menu("Window").menu("Reports Manager...").click();
		bot.radio("Team reports").click();
	}

	public void createUserReport(String name) {
		bot.button("New").click();
		bot.textWithLabel("Choose name for the new custom report.").setText(name);
		bot.button("OK").click();
		bot.button("Save").click();
	}

	public void createCustomTeamReport(String name) {
		bot.button("New").click();
		bot.textWithLabel("Choose name for the new custom report.").setText(name);
		bot.button("OK").click();
		bot.button("Publish").click();
	}

	@Test
	public void createTeamReport() throws InterruptedException, IOException {
		openTeamReportsManager();
		createCustomTeamReport("test1");
		CustomReportsRegistry.getInstance().refreshTeamReports();
		Thread.sleep(2000);
		List<CustomReportDefinition> teamReports = CustomReportsRegistry.getInstance().readTeamCustomReportDefinitions();
		// @formatter:off
		Optional<CustomReportDefinition> optionalTeamReport = teamReports.stream()
				.filter(teamReport -> teamReport.getName().equals("test1"))
				.findFirst();
		// @formatter:on

		// assertTrue(optionalTeamReport.isPresent());
		assertTrue(bot.table().containsItem("test1"));
	}

	@Test
	public void createUserReportThenAddToTeam() throws InterruptedException, IOException {
		openUserReportsManager();
		createUserReport("test2");

		// add user report to team reports
		bot.table().select("test2");
		bot.button("Add to Team").click();
		bot.button("OK").click();
		Thread.sleep(2000);
		CustomReportsRegistry.getInstance().refreshTeamReports();
		Thread.sleep(3000);
		List<CustomReportDefinition> teamReports = CustomReportsRegistry.getInstance().readTeamCustomReportDefinitions();
		// @formatter:off
		Optional<CustomReportDefinition> optionalTeamReport = teamReports.stream()
				.filter(teamReport -> teamReport.getName().equals("test2"))
				.findFirst();
		// @formatter:on

		assertTrue(optionalTeamReport.isPresent());
	}

	@Test
	public void copyTeamReport() throws InterruptedException {
		openTeamReportsManager();
		createCustomTeamReport("test3");
		bot.table().select("test3");

		// we need to sleep between ok and publish otherwise this test fails
		// this is because clicking ok writes the file to disk and publish should only happen once this is finished
		bot.button("Copy").click();
		bot.button("OK").click();
		Thread.sleep(1000);
		bot.button("Publish").click();
		Thread.sleep(1000);
		List<CustomReportDefinition> teamReports = CustomReportsRegistry.getInstance().readTeamCustomReportDefinitions();
		// @formatter:off
		Long numberOfTeamReports = teamReports.stream()
				.count();
		// @formatter:on
		assertTrue(Objects.equals(numberOfTeamReports, 2L));
	}

	@Test
	public void teamReports_areAvailable_whenLoggedOut() throws IOException {
		openTeamReportsManager();
		createCustomTeamReport("test4");
		basicAuthenticationManager.logout(upstreamUrl, (Shell) null);
		UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
		CustomReportsRegistry.getInstance().refreshTeamReports();
		assertTrue(bot.table().rowCount() != 0);

		// login again for future tests
		if (!basicAuthenticationManager.isAuthenticated(upstreamUrl) && basicAuthenticationManager.checkCredentials(upstreamUrl, USERNAME, PASSWORD)) {
			basicAuthenticationManager.storeInSecurePreferences("username", USERNAME);
			basicAuthenticationManager.storeInSecurePreferences("password", PASSWORD);
			// force trigger refresh
			UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
		}
	}

	@Test
	public void copyRenameDeleteAndAddtoTeam_areEnabled_whenReportIsSelected() {
		openTeamReportsManager();
		createCustomTeamReport("test5");
		bot.table().select("test5");
		assertTrue(bot.button("Copy").isEnabled());
		assertTrue(bot.button("Rename").isEnabled());
		assertTrue(bot.button("Delete").isEnabled());
		assertTrue(bot.button("Add to User").isEnabled());
	}

	@Test
	public void revertAndPublish_areEnabled_whenRowIsChanged() {
		openTeamReportsManager();
		createCustomTeamReport("test6");
		bot.table().select("test6");
		bot.checkBox("Cargoes").click();
		assertTrue(bot.button("Revert").isEnabled());
		assertTrue(bot.button("Publish").isEnabled());
		bot.button("Revert").click();
	}

	@Test
	public void revertAndPublish_areEnabled_whenDiffIsChanged() {
		openTeamReportsManager();
		createCustomTeamReport("test7");
		bot.table().select("test7");
		bot.tableWithLabel("Disabled Columns").select("Ballast Canal Date");
		bot.button("Show").click();
		assertTrue(bot.button("Revert").isEnabled());
		assertTrue(bot.button("Publish").isEnabled());
		bot.button("Revert").click();
	}

	@Test
	public void revertAndPublish_areEnabled_whenDisabledColumnIsChanged() {
		openTeamReportsManager();
		createCustomTeamReport("test8");
		bot.table().select("test8");
		bot.tableWithLabel("Disabled Columns").select("Ballast Canal Date");
		bot.button("Show").click();
		assertTrue(bot.button("Revert").isEnabled());
		assertTrue(bot.button("Publish").isEnabled());
		bot.button("Revert").click();
	}

	@Test
	public void revertAndPublish_areEnabled_whenEnabledColumnIsChanged() {
		openTeamReportsManager();
		createCustomTeamReport("test10");
		bot.table().select("test10");
		bot.tableWithLabel("Enabled Columns").select("Type");
		bot.button("Up").click();
		assertTrue(bot.button("Revert").isEnabled());
		assertTrue(bot.button("Publish").isEnabled());
		bot.button("Revert").click();
	}

	@Test
	public void renameTeamReport() throws InterruptedException, IOException {
		openTeamReportsManager();
		createCustomTeamReport("test11");
		bot.table().select("test11");
		bot.button("Rename").click();
		bot.textWithLabel("Choose a new name for the report.").setText("test11.2");
		bot.button("OK").click();

		CustomReportsRegistry.getInstance().refreshTeamReports();
		Thread.sleep(3000);
		List<CustomReportDefinition> teamReports = CustomReportsRegistry.getInstance().readTeamCustomReportDefinitions();
		// @formatter:off
		Optional<CustomReportDefinition> optionalTeamReport = teamReports.stream()
				.filter(teamReport -> teamReport.getName().equals("test11.2"))
				.findFirst();
		// @formatter:on

		// assertTrue(optionalTeamReport.isPresent());
		assertTrue(bot.table().containsItem("test11.2"));
	}

	@Test
	public void deleteTeamReport() throws InterruptedException, IOException {
		openTeamReportsManager();
		createCustomTeamReport("test12");
		bot.table().select("test12");
		bot.button("Delete").click();
		bot.button("Yes").click();
		Thread.sleep(2000);
		CustomReportsRegistry.getInstance().refreshTeamReports();
		Thread.sleep(3000);
		List<CustomReportDefinition> teamReports = CustomReportsRegistry.getInstance().readTeamCustomReportDefinitions();
		// @formatter:off
		Long numberOfTeamReports = teamReports.stream()
				.count();
		// @formatter:on

		// assertTrue(Objects.equals(numberOfTeamReports, 0L));
		assertFalse(bot.table().containsItem("test12"));
	}

}
