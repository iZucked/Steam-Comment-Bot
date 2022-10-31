/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.datahub;

import java.io.IOException;
import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.auth.BasicAuthenticationManager;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.BaseCaseServiceClient;
import com.mmxlabs.rcp.common.appversion.VersionHelper;

@Testcontainers
@Tag(TestCategories.HUB_TEST)
public class UserPermissionsTest {

	static final Logger logger = LoggerFactory.getLogger(UserPermissionsTest.class);

	public static final int DATAHUB_PORT = 8090;
	public final String REPORT_NAME = "test";

	static List<Integer> portPool = List.of(8090, 8091, 8092, 8093, 8094, 8095, 8096, 8097, 8098, 8099);
	static int timeout = 60000;
	static int availablePort = HubTestHelper.waitForAvailablePort(portPool, timeout);

	private static SWTWorkbenchBot bot;
	static String datahubHost;
	static String upstreamUrl;

	static final String CONTAINER = HubTestHelper.getContainerString(VersionHelper.getInstance().getClientCode());

	@Container
	public static GenericContainer datahubContainer = HubTestHelper.createDataHubContainer(CONTAINER, availablePort, DATAHUB_PORT, false);

	public void asBaseCaseUser() {
		BasicAuthenticationManager.getInstance().withCredentials("u_basecase", "u_basecase");
		UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
	}

	public void asAlternativeBaseCaseUser() {
		BasicAuthenticationManager.getInstance().withCredentials("test", "test");
		UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
	}

	@BeforeAll
	public static void beforeAll() {
		datahubHost = datahubContainer.getHost();
		upstreamUrl = String.format("http://%s:%s", datahubHost, availablePort);
		logger.info(upstreamUrl);
		bot = new SWTWorkbenchBot();
		HubTestHelper.setDatahubUrl(upstreamUrl);
		// force trigger refresh
		UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
	}

	@AfterAll
	public static void afterAll() {
		bot.resetWorkbench();
		bot = null;
	}

	@BeforeEach
	public void resetLocking() throws IOException {
		asBaseCaseUser();
		BaseCaseServiceClient.INSTANCE.forceUnlock();
		bot.resetWorkbench();
	}

	@Test
	public void testLocking() throws IOException {
		asBaseCaseUser();

		Assertions.assertFalse(BaseCaseServiceClient.INSTANCE.isServiceLocked());
		BaseCaseServiceClient.INSTANCE.lock();
		BaseCaseServiceClient.INSTANCE.updateLockedState();

		Assertions.assertTrue(BaseCaseServiceClient.INSTANCE.isServiceLocked());
		Assertions.assertTrue(BaseCaseServiceClient.INSTANCE.isServiceLockedByMe());

		BaseCaseServiceClient.INSTANCE.unlock();
		BaseCaseServiceClient.INSTANCE.updateLockedState();

		Assertions.assertFalse(BaseCaseServiceClient.INSTANCE.isServiceLocked());
		Assertions.assertFalse(BaseCaseServiceClient.INSTANCE.isServiceLockedByMe());
	}

	@Test
	public void testLockedByOther() throws IOException {
		asBaseCaseUser();

		Assertions.assertFalse(BaseCaseServiceClient.INSTANCE.isServiceLocked());
		BaseCaseServiceClient.INSTANCE.lock();
		BaseCaseServiceClient.INSTANCE.updateLockedState();

		Assertions.assertTrue(BaseCaseServiceClient.INSTANCE.isServiceLocked());
		Assertions.assertTrue(BaseCaseServiceClient.INSTANCE.isServiceLockedByMe());

		// Switch users, make sure we cannot unlock
		asAlternativeBaseCaseUser();
		BaseCaseServiceClient.INSTANCE.updateLockedState();
		Assertions.assertTrue(BaseCaseServiceClient.INSTANCE.isServiceLocked());
		Assertions.assertFalse(BaseCaseServiceClient.INSTANCE.isServiceLockedByMe());

		BaseCaseServiceClient.INSTANCE.unlock();
		BaseCaseServiceClient.INSTANCE.updateLockedState();

		Assertions.assertTrue(BaseCaseServiceClient.INSTANCE.isServiceLocked());
		Assertions.assertFalse(BaseCaseServiceClient.INSTANCE.isServiceLockedByMe());
	}

}
