package com.mmxlabs.lingo.its.datahub;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.mmxlabs.hub.DataHubActivator;
import com.mmxlabs.hub.preferences.DataHubPreferenceConstants;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.BaseCaseServiceClient;

@Testcontainers
@Tag(TestCategories.HUB_TEST)
public class UserPermissionsTest {

	@Container
	public static DockerComposeContainer environment = new DockerComposeContainer(new File("data/docker/thing.yaml")) //
	.withExposedService("datahub", 8000)
	.waitingFor("datahub", Wait.forHttp("/ping").forStatusCode(200))
	;

	private static HubTestHelper hubHelper;

	@BeforeAll
	public static void initHelper() {
		String host = environment.getServiceHost("datahub", 8000);
		int port = environment.getServicePort("datahub", 8000);

		String baseUrl = String.format("http://%s:%d", host, port);
		DataHubActivator.getDefault().getPreferenceStore().setValue(DataHubPreferenceConstants.P_DATAHUB_URL_KEY, baseUrl);
	}

	@BeforeEach
	public void resetLocking() throws Exception {
		hubHelper.asBaseCaseUser();
		BaseCaseServiceClient.INSTANCE.forceUnlock();
	}

	@Test
	public void testLocking() throws Exception {
		hubHelper.asBaseCaseUser();

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
	public void testLockedByOther() throws Exception {
		hubHelper.asBaseCaseUser();

		Assertions.assertFalse(BaseCaseServiceClient.INSTANCE.isServiceLocked());
		BaseCaseServiceClient.INSTANCE.lock();
		BaseCaseServiceClient.INSTANCE.updateLockedState();

		Assertions.assertTrue(BaseCaseServiceClient.INSTANCE.isServiceLocked());
		Assertions.assertTrue(BaseCaseServiceClient.INSTANCE.isServiceLockedByMe());

		// Switch users, make sure we cannot unlock
		hubHelper.asAlternativeBaseCaseUser();
		BaseCaseServiceClient.INSTANCE.updateLockedState();
		Assertions.assertTrue(BaseCaseServiceClient.INSTANCE.isServiceLocked());
		Assertions.assertFalse(BaseCaseServiceClient.INSTANCE.isServiceLockedByMe());

		BaseCaseServiceClient.INSTANCE.unlock();
		BaseCaseServiceClient.INSTANCE.updateLockedState();

		Assertions.assertTrue(BaseCaseServiceClient.INSTANCE.isServiceLocked());
		Assertions.assertFalse(BaseCaseServiceClient.INSTANCE.isServiceLockedByMe());

	}

}
