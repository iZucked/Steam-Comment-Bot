/// **
// * Copyright (C) Minimax Labs Ltd., 2010 - 2020
// * All rights reserved.
// */
// package com.mmxlabs.lingo.its.datahub;
//
// import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withText;
//
// import java.io.File;
//
// import org.eclipse.swt.widgets.Shell;
// import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
// import org.eclipse.swtbot.swt.finder.waits.Conditions;
// import org.hamcrest.Matcher;
// import org.junit.jupiter.api.AfterAll;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Tag;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.testcontainers.containers.DockerComposeContainer;
// import org.testcontainers.containers.wait.Wait;
// import org.testcontainers.junit.jupiter.Container;
// import org.testcontainers.junit.jupiter.Testcontainers;
//
// import com.mmxlabs.hub.DataHubActivator;
// import com.mmxlabs.hub.DataHubServiceProvider;
// import com.mmxlabs.hub.auth.AuthenticationManager;
// import com.mmxlabs.hub.preferences.DataHubPreferenceConstants;
// import com.mmxlabs.lingo.its.tests.category.TestCategories;
//
// @Testcontainers
// @Tag(TestCategories.HUB_TEST)
// public class HubTests {
//
// static final Logger logger = LoggerFactory.getLogger(HubTests.class);
//
//	// @formatter:off
//	@Container
//	public static DockerComposeContainer datahubContainer = new DockerComposeContainer(new File("data/docker/docker-compose.yml"))
//	.withExposedService("datahub-oauth", 8090)
//	.withExposedService("datahub-basic", 8091)
//	.waitingFor("datahub-oauth", Wait.forHttp("/ping").forStatusCode(200))
//	.withLocalCompose(true);
//	// @formatter:on
//
// private static SWTWorkbenchBot bot;
// static String datahubBasicHost;
// static int datahubBasicPort;
// static String datahubOAuthHost;
// static int datahubOAuthPort;
// static String basicUpstreamUrl;
// static String oauthUpstreamUrl;
//
// private static DataHubServiceProvider datahubServiceProvider = DataHubServiceProvider.getInstance();
// private static AuthenticationManager authenticationManager = AuthenticationManager.getInstance();
// private static HubTestHelper hubHelper;
//
// @BeforeAll
// private static void beforeAll() {
// datahubBasicHost = datahubContainer.getServiceHost("datahub-basic", 8091);
// datahubBasicPort = datahubContainer.getServicePort("datahub-basic", 8091);
// datahubOAuthHost = datahubContainer.getServiceHost("datahub-oauth", 8090);
// datahubOAuthPort = datahubContainer.getServicePort("datahub-oauth", 8090);
//
// basicUpstreamUrl = String.format("http://%s:%s", datahubBasicHost, datahubBasicPort);
// oauthUpstreamUrl = String.format("http://%s:%s", datahubOAuthHost, datahubOAuthPort);
// bot = new SWTWorkbenchBot();
// setDatahubUrl(basicUpstreamUrl);
// authenticationManager.logoutAll((Shell) null);
// }
//
// public static void setDatahubUrl(String upstreamUrl) {
// DataHubActivator.getDefault().getPreferenceStore().setValue(DataHubPreferenceConstants.P_DATAHUB_URL_KEY, upstreamUrl);
// // Matcher<Shell> loginShellMatcher = withText("LiNGO Login");
// // bot.waitUntil(Conditions.waitForShell(loginShellMatcher));
// // bot.shell("LiNGO Login").close();
// }
//
// public void basicLogin() {
// openDatahubPreferencePage();
// bot.button("Login").click();
// Matcher<Shell> basicLoginShellMatcher = withText("Data Hub Login");
// bot.waitUntil(Conditions.waitForShell(basicLoginShellMatcher));
// bot.textWithLabel("Username: ").setText("philippe");
// bot.textWithLabel("Password: ").setText("philippe");
// bot.button("OK").click();
// }
//
// /*
// * Got this setup & teardown example for the creator of SWTBot himself in his PhD thesis: https://www.theseus.fi/bitstream/handle/10024/7470/Mazurkiewicz_Milosz.pdf
// */
// @AfterAll
// public static void afterAll() {
// bot.resetWorkbench();
// }
//
// @BeforeEach
// public void beforeEach() {
// authenticationManager.logoutAll((Shell) null);
// bot.resetWorkbench();
// }
//
// public static void openDatahubPreferencePage() {
// bot.menu("Window").menu("Preferences").click();
// bot.shell("Preferences").activate();
// bot.tree().getTreeItem("Data Hub").select().expand().click();
// }
//
// @Test
// public void changingHubsUpdatesLoginState() {
// basicLogin();
// bot.textWithLabel("&URL").setText(oauthUpstreamUrl);
// bot.button("Apply").click();
// assertFalse(authenticationManager.isAuthenticated());
//
// // user should still be authenticated in basic hub
// bot.textWithLabel("&URL").setText(basicUpstreamUrl);
// bot.button("Apply and Close").click();
// assertTrue(authenticationManager.isAuthenticated());
// }
//
// @Test
// public void changingAuthSchemeUpdatesLoginState() {
// openDatahubPreferencePage();
// bot.textWithLabel("&URL").setText(oauthUpstreamUrl);
// bot.button("Apply").click();
// bot.checkBox("Force local authentication").click();
// bot.button("Apply and Close").click();
// assertFalse(authenticationManager.isAuthenticated());
// }
//
// @Test
// public void changeUrlWithoutApplyingDisablesLoginButton() {
// openDatahubPreferencePage();
// // type backspace character to trigger change
// bot.textWithLabel("&URL").typeText("anything");
// assertFalse(bot.button("Login").isEnabled());
// }
//
// @Test
// public void changingAuthSchemeDisablesLoginButton() {
// openDatahubPreferencePage();
// bot.checkBox("Force local authentication").click();
// assertFalse(bot.button("Login").isEnabled());
// }
// }
