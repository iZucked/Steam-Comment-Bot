/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.datahub;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withText;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.mmxlabs.hub.DataHubActivator;
import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.preferences.DataHubPreferenceConstants;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.rcp.common.appversion.VersionHelper;

@Testcontainers
@Tag(TestCategories.HUB_TEST)
public class BasicAuthenticationTests {
	private static DataHubServiceProvider datahubServiceProvider = DataHubServiceProvider.getInstance();
	static final Logger logger = LoggerFactory.getLogger(BasicAuthenticationTests.class);

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

	@BeforeAll
	private static void beforeAll() {
		datahubHost = datahubContainer.getHost();
		upstreamUrl = String.format("http://%s:%s", datahubHost, availablePort);
		logger.info(upstreamUrl);
		bot = new SWTWorkbenchBot();
		HubTestHelper.setDatahubUrl(upstreamUrl);
		enableBasicAuthentication();
		// force trigger refresh
		UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
	}

	public static void enableBasicAuthentication() {
		DataHubActivator.getDefault().getPreferenceStore().setValue(DataHubPreferenceConstants.P_FORCE_BASIC_AUTH, true);
	}

	/*
	 * Got this setup & teardown example for the creator of SWTBot himself in his
	 * PhD thesis:
	 * https://www.theseus.fi/bitstream/handle/10024/7470/Mazurkiewicz_Milosz.pdf
	 *
	 */
	@AfterAll
	public static void afterAll() throws Exception {
		bot.resetWorkbench();
		bot = null;
	}

	@BeforeEach
	public void beforeEach() {
		bot.resetWorkbench();
	}

	public static void openDatahubPreferencePage() {
		bot.menu("Window").menu("Preferences").click();
		bot.shell("Preferences").activate();
		bot.tree().getTreeItem("Data Hub").select().expand().click();
	}

	@Test
	public void forceLocalAuthenticationIsDisabled() {
		openDatahubPreferencePage();
		assertFalse(bot.checkBox("Force local authentication").isEnabled());
	}

	@Test
	public void testBasicAuthenticationLogin() throws InterruptedException {
		openDatahubPreferencePage();
		// logout if necessary
		if ("Logout".equals(bot.buttonWithId("login").getText())) {
			bot.buttonWithId("login").click();
			Thread.sleep(2000);
		}
		bot.buttonWithId("login").click();
		Matcher<Shell> basicLoginShellMatcher = withText("Data Hub Basic Login");
		bot.waitUntil(Conditions.waitForShell(basicLoginShellMatcher));
		bot.textWithLabel("Username: ").setText("test");
		bot.textWithLabel("Password: ").setText("test");
		bot.button("OK").click();

		// force update online state refresh
		UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
		assertTrue(datahubServiceProvider.isLoggedIn());
	}

}
