/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.datahub;

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withText;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.mmxlabs.hub.DataHubActivator;
import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.auth.BasicAuthenticationManager;
import com.mmxlabs.hub.auth.OAuthManager;
import com.mmxlabs.hub.preferences.DataHubPreferenceConstants;
import com.mmxlabs.lingo.its.tests.category.TestCategories;

@Testcontainers
@Tag(TestCategories.HUB_TEST)
public class HubTests {

	static final Logger logger = LoggerFactory.getLogger(HubTests.class);
	static final String oauthHubContainer = "datahub-oauth";
	static final String basicHubContainer = "datahub-basic";

	// @formatter:off
	@Container
	public static final DockerComposeContainer<?> datahubContainer = new DockerComposeContainer(new File("data/docker/docker-compose.yml"))
	.withExposedService(oauthHubContainer, 8090)
	.withExposedService(basicHubContainer, 8091)
	.waitingFor("datahub-oauth", Wait.forHttp("/ping").forStatusCode(200))
	.waitingFor("datahub-basic", Wait.forHttp("/ping").forStatusCode(200))
	.withLocalCompose(false);
	// @formatter:on

	private static SWTWorkbenchBot bot;
	static String datahubBasicHost;
	static int datahubBasicPort;
	static String datahubOAuthHost;
	static int datahubOAuthPort;
	static String basicUpstreamUrl;
	static String oauthUpstreamUrl;

	private static BasicAuthenticationManager basicAuthenticationManager = BasicAuthenticationManager.getInstance();
	private static OAuthManager oauthManager = OAuthManager.getInstance();

	@BeforeAll
	private static void beforeAll() {
		datahubBasicHost = datahubContainer.getServiceHost(basicHubContainer, 8091);
		datahubBasicPort = datahubContainer.getServicePort(basicHubContainer, 8091);
		datahubOAuthHost = datahubContainer.getServiceHost(oauthHubContainer, 8090);
		datahubOAuthPort = datahubContainer.getServicePort(oauthHubContainer, 8090);

		basicUpstreamUrl = String.format("http://%s:%s", datahubBasicHost, datahubBasicPort);
		oauthUpstreamUrl = String.format("http://%s:%s", datahubOAuthHost, datahubOAuthPort);
		bot = new SWTWorkbenchBot();
		enableBasicAuthentication();
		setDatahubUrl(basicUpstreamUrl);
		// force trigger refresh
		UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
		// slow down tests' playback speed to 500ms
		// SWTBotPreferences.PLAYBACK_DELAY = 500;
		// increase timeout to 10 seconds
		SWTBotPreferences.TIMEOUT = 20000;
		// keyboard layout necessary as we type text in tests:
		// https://wiki.eclipse.org/SWTBot/Keyboard_Layouts
		SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
	}

	public static void enableBasicAuthentication() {
		DataHubActivator.getDefault().getPreferenceStore().setValue(DataHubPreferenceConstants.P_FORCE_BASIC_AUTH, true);
	}

	public static void setDatahubUrl(String upstreamUrl) {
		DataHubActivator.getDefault().getPreferenceStore().setValue(DataHubPreferenceConstants.P_DATAHUB_URL_KEY, upstreamUrl);
	}

	public void basicLogin() {
		bot.button("Login").click();
		Matcher<Shell> basicLoginShellMatcher = withText("Data Hub Basic Login");
		bot.waitUntil(Conditions.waitForShell(basicLoginShellMatcher));
		bot.textWithLabel("Username: ").setText("test");
		bot.textWithLabel("Password: ").setText("test");
		bot.button("OK").click();
	}

	/*
	 * Got this setup & teardown example for the creator of SWTBot himself in his
	 * PhD thesis:
	 * https://www.theseus.fi/bitstream/handle/10024/7470/Mazurkiewicz_Milosz.pdf
	 */
	@AfterAll
	public static void afterAll() {
		bot.resetWorkbench();
		bot = null;
	}

	@BeforeEach
	public void beforeEach() {
		bot.resetWorkbench();
		basicAuthenticationManager.logout(basicUpstreamUrl, null);
	}

	public static void openDatahubPreferencePage() {
		SWTBotMenu menuWindow = bot.menu("Window");
		SWTBotMenu menuWindowPreferences = menuWindow.menu("Preferences");
		menuWindowPreferences.click();
		bot.shell("Preferences").activate();
		bot.tree().getTreeItem("Data Hub").select().expand().click();
	}

	@Test
	public void changingHubsUpdatesLoginState() throws InterruptedException {
		openDatahubPreferencePage();
		if (!basicAuthenticationManager.isAuthenticated(basicUpstreamUrl)) {
			basicLogin();
		}
		Thread.sleep(2000);
		Matcher<Widget> urlTextField = withText("&URL");
		bot.waitUntil(Conditions.waitForWidget(urlTextField));
		bot.textWithLabel("&URL").setText(oauthUpstreamUrl);
		bot.button("Apply").click();
		// logout if necessary
		if ("Logout".equals(bot.buttonWithId("login").getText())) {
			bot.buttonWithId("login").click();
			Thread.sleep(2000);
		}
		assertTrue(basicAuthenticationManager.isAuthenticated(basicUpstreamUrl));
		assertFalse(oauthManager.isAuthenticated(oauthUpstreamUrl));
	}

	@Test
	public void changingAuthSchemeUpdatesLoginState() throws InterruptedException {
		openDatahubPreferencePage();
		boolean basicAuthenticated = basicAuthenticationManager.isAuthenticated(basicUpstreamUrl);
		Matcher<Widget> urlTextField = withText("&URL");
		bot.waitUntil(Conditions.waitForWidget(urlTextField));
		bot.textWithLabel("&URL").setText(oauthUpstreamUrl);
		bot.button("Apply").click();
		Thread.sleep(1000);
		bot.checkBox("Force local authentication").click();
		bot.button("Apply and Close").click();
		if (basicAuthenticated) {
			assertTrue(basicAuthenticationManager.isAuthenticated(basicUpstreamUrl));
		} else {
			assertFalse(basicAuthenticationManager.isAuthenticated(basicUpstreamUrl));
		}
	}

	@Test
	public void changingUrlWithoutApplyingDisablesLoginButton() throws InterruptedException {
		openDatahubPreferencePage();
		// type backspace character to trigger change
		Matcher<Widget> urlTextField = withText("&URL");
		bot.waitUntil(Conditions.waitForWidget(urlTextField));
		logger.info(Boolean.toString(bot.buttonWithId("login").isEnabled()));

		bot.textWithLabel("&URL").setFocus();
		bot.textWithLabel("&URL").setText("anything").pressShortcut(Keystrokes.TAB);
		bot.buttonWithId("login").setFocus();
		
		logger.info(Boolean.toString(bot.buttonWithId("login").isEnabled()));
		Thread.sleep(3000);
		logger.info(Boolean.toString(bot.buttonWithId("login").isEnabled()));
		SWTBotShell[] swtshells = bot.shells();
		for (SWTBotShell shell : swtshells) {
			logger.info(shell.getText());
		}
		assertFalse(bot.buttonWithId("login").isEnabled());
	}

	@Test
	public void changingAuthSchemeDisablesLoginButton() throws InterruptedException {
		openDatahubPreferencePage();
		bot.checkBox("Force local authentication").click();
		Thread.sleep(1000);
		assertFalse(bot.buttonWithId("login").isEnabled());
	}
}
