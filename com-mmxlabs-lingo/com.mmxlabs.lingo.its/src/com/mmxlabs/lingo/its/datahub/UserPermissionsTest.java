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
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.auth.BasicAuthenticationManager;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.BaseCaseServiceClient;

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

	// @formatter:off
	@Container
	public static GenericContainer datahubContainer = new FixedHostPortGenericContainer("docker.mmxlabs.com/datahub-v:1.9.1-SNAPSHOT")
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

	public void asBaseCaseUser() {
		BasicAuthenticationManager.getInstance().withCredentials("u_basecase", "u_basecase");
		UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
	}

	public void asAlternativeBaseCaseUser() {
		BasicAuthenticationManager.getInstance().withCredentials("simon", "simon");
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
