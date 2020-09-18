package com.mmxlabs.lingo.its.datahub;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotBrowser;
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

import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.auth.OAuthAuthenticationManager;
import com.mmxlabs.lingo.its.tests.category.TestCategories;

@Testcontainers
@Tag(TestCategories.HUB_TEST)
public class OAuthAuthenticationTests {

	private static DataHubServiceProvider datahubServiceProvider = DataHubServiceProvider.getInstance();
	private static OAuthAuthenticationManager oauthAuthenticationManager = OAuthAuthenticationManager.getInstance();
	static final Logger logger = LoggerFactory.getLogger(OAuthAuthenticationTests.class);

	public static final int DATAHUB_PORT = 8090;
	static List<Integer> portPool = List.of(8090, 8091, 8092, 8093, 8094, 8095, 8096, 8097, 8098, 8099);
	static int timeout = 60000;
	static int availablePort = HubTestHelper.waitForAvailablePort(portPool, timeout);

	private static SWTWorkbenchBot bot;
	static String datahubHost;
	static String upstreamUrl;

	// @formatter:off
	@Container
	// we need a fixed port for this test because we cannot programatically modify the redirection-url on Azure
	// AD has port 8090 - 8099 set for testing purposes
	public static GenericContainer datahubContainer = new FixedHostPortGenericContainer("docker.mmxlabs.com/datahub-v-single:1.7.1-SNAPSHOT")
	.withFixedExposedPort(availablePort, DATAHUB_PORT)
	.withExposedPorts(DATAHUB_PORT)
	.withEnv("PORT", Integer.toString(DATAHUB_PORT))
	.withEnv("PROTOCOL", "http")
	.withEnv("HOSTNAME", "localhost")
	.withEnv("AUTHENTICATION_SCHEME", "oauth")
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
	}

	/*
	 * Got this setup & teardown example for the creator of SWTBot himself in his PhD thesis: https://www.theseus.fi/bitstream/handle/10024/7470/Mazurkiewicz_Milosz.pdf
	 */
	@AfterAll
	public static void afterAll() {
		bot = null;
	}

	@BeforeEach
	public void beforeEach() {
		bot.resetWorkbench();
	}

	public static void openDatahubPreferencePage() {
		// for some reason the shell isn't focus so we activate it here
		bot.menu("Window").menu("Preferences").click();
		bot.shell("Preferences").activate();
		bot.tree().getTreeItem("Data Hub").select().expand().click();
	}

	@Test
	public void checkDatahubConnection() {
		// force update online state refresh
		boolean isAvailable = UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
		assertTrue(datahubServiceProvider.isHubOnline());
	}

	@Test
	// we need to sleep in this test because we're waiting for the page microsoft login page to load
	public void oauthAuthenticationLogin() throws InterruptedException {
		openDatahubPreferencePage();
		// logout if necessary
		if ("Logout".equals(bot.buttonWithId("loginButtonId").getText())) {
			bot.buttonWithId("loginButtonId").click();
			bot.shellWithId("oauthShellId").close();
		}
		bot.button("Login").click();
		SWTBotBrowser browser = bot.browserWithId("oauthBrowserId");
		browser.waitForPageLoaded();

		// if the test is failing to often it might be because clickUserAnotherAccount is running before the page is loaded. In that case increase the sleep below
		Thread.sleep(3000);
		browser.execute(HubTestHelper.clickUserAnotherAccount);
		Thread.sleep(500);

		// fill in the email input
		browser.execute(HubTestHelper.fillEmail);
		browser.execute(HubTestHelper.focusEmail);
		browser.execute(HubTestHelper.clickNext);
		Thread.sleep(500);

		// fill in the password input
		browser.execute(HubTestHelper.fillPassword);
		browser.execute(HubTestHelper.focusPassword);
		browser.execute(HubTestHelper.clickNext);

		Thread.sleep(2000);
		// click yes on "stay signed in?" page
		browser.execute(HubTestHelper.clickNext);

		// force update online state refresh
		Thread.sleep(5000);
		assertTrue(datahubServiceProvider.isLoggedIn());
	}
}
