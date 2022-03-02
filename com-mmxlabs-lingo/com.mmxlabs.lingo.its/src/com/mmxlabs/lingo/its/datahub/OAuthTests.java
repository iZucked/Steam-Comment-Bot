/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.datahub;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withText;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotBrowser;
import org.hamcrest.Matcher;
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
import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.UpstreamUrlProvider.StateReason;
import com.mmxlabs.hub.auth.OAuthManager;
import com.mmxlabs.hub.preferences.DataHubPreferenceConstants;
import com.mmxlabs.lingo.its.tests.category.TestCategories;

@Testcontainers
@Tag(TestCategories.HUB_TEST)
public class OAuthTests {

	private static DataHubServiceProvider datahubServiceProvider = DataHubServiceProvider.getInstance();
	private static OAuthManager oauthManager = OAuthManager.getInstance();
	static final Logger logger = LoggerFactory.getLogger(OAuthTests.class);

	public static final int DATAHUB_PORT = 8090;
	static List<Integer> portPool = List.of(8090, 8091, 8092, 8093, 8094, 8095, 8096, 8097, 8098, 8099);
	static int timeout = 60000;
	static int availablePort = HubTestHelper.waitForAvailablePort(portPool, timeout);

	private static SWTWorkbenchBot bot;
	static String datahubHost;
	static String upstreamUrl;

	// @formatter:off
	@Container
	// we need a fixed port for this test because we cannot programmatically modify the redirection-url on Azure
	// AD has port 8090 - 8099 set for testing purposes
	public static GenericContainer datahubContainer = new FixedHostPortGenericContainer("docker.mmxlabs.com/datahub-v:1.9.1-SNAPSHOT")
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
	.withEnv("AZURE_CLIENT_SECRET", "n-LV4_3vPJ3s1v.9MWwLy1.ZO-1oz17u54")
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
		logger.info(upstreamUrl);
		bot = new SWTWorkbenchBot();
		HubTestHelper.setDatahubUrl(upstreamUrl);
		enableOAuth();
		// slow down tests' playback speed to 500ms
		SWTBotPreferences.PLAYBACK_DELAY = 500;
		// increase timeout to 10 seconds
		SWTBotPreferences.TIMEOUT = 30000;
		// force trigger refresh
		UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
	}

	public static void enableOAuth() {
		DataHubActivator.getDefault().getPreferenceStore().setValue(DataHubPreferenceConstants.P_FORCE_BASIC_AUTH, false);
	}

	/*
	 * Got this setup & teardown example for the creator of SWTBot himself in his PhD thesis: https://www.theseus.fi/bitstream/handle/10024/7470/Mazurkiewicz_Milosz.pdf
	 */
	@AfterAll
	public static void afterAll() {
		bot.resetWorkbench();
		bot = null;
	}

	@BeforeEach
	public void beforeEach() {
		bot.resetWorkbench();
	}

	/**
	 * Helper function to open the datahub preference page
	 */
	public static void openDatahubPreferencePage() {
		// for some reason the shell isn't focus so we activate it here
		bot.menu("Window").menu("Preferences").click();
		bot.shell("Preferences").activate();
		bot.tree().getTreeItem("Data Hub").select().expand().click();
	}

	public static ICondition waitUntilAuthenticated() {
		return new ICondition() {

			@Override
			public boolean test() throws Exception {
				return datahubServiceProvider.isLoggedIn();
			}

			@Override
			public void init(SWTBot bot) {
				// nop
			}

			@Override
			public String getFailureMessage() {
				return "Timed out waiting for user to be authenticated";
			}
		};
	}

	/**
	 * If this test fails all other tests will fail too because the hub is not online
	 */
	@Test
	public void checkDatahubConnection() {
		assertTrue(UpstreamUrlProvider.INSTANCE.isUpstreamAvailable().getReason() == StateReason.HUB_ONLINE);
		assertTrue(datahubServiceProvider.isHubOnline());
	}

	/**
	 * Test OAuth login by executing JavaScript in the browser. Because bot.browser() returns void type we cannot use bot.waitUntil() conditions to check the contents of the page.
	 * bot.waitForPageLoaded() doesn't work for this use case because the url doesn't change during the authentication process
	 * <p>
	 * </p>
	 * If the tests fail to often try increasing the sleep time. If it keeps failing, make sure that test@minimaxlabs.com is able to login to {@link https://portal.azure.com} without any additional
	 * prompts
	 */
	@Test
	public void oauthLogin() throws InterruptedException {
		openDatahubPreferencePage();
		// logout if necessary
		if ("Logout".equals(bot.buttonWithId("login").getText())) {
			bot.buttonWithId("login").click();
			Thread.sleep(2000);
		}
		bot.buttonWithId("login").click();
		Thread.sleep(2000);

		// if user was previously authenticated, clikcing on Login will automatically log them back in
		if (!datahubServiceProvider.isLoggedIn()) {
			Matcher<Shell> oauthLoginShellMatcher = withText("Data Hub OAuth Login");
			bot.waitUntil(Conditions.waitForShell(oauthLoginShellMatcher));
			SWTBotBrowser browser = bot.browser();
			Thread.sleep(2000);

			// click on "use another account" button
			browser.execute(HubTestHelper.clickUserAnotherAccount);
			Thread.sleep(2000);

			// fill in the email input
			browser.execute(HubTestHelper.fillEmail);
			browser.execute(HubTestHelper.focusEmail);
			browser.execute(HubTestHelper.clickNext);
			Thread.sleep(2000);

			// fill in the password input
			browser.execute(HubTestHelper.fillPassword);
			browser.execute(HubTestHelper.focusPassword);
			browser.execute(HubTestHelper.clickNext);
			Thread.sleep(2000);

			// click yes on "stay signed in?" page
			browser.execute(HubTestHelper.clickNo);
			Thread.sleep(2000);

			// wait until authenticated status updates
			bot.waitUntil(waitUntilAuthenticated());
		} else {
			logger.info("datahub already logged in");
		}

		assertTrue(datahubServiceProvider.isLoggedIn());

	}
}
