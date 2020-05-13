/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.server;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.hub.services.user.IUserPermissionsService;
import com.mmxlabs.hub.services.user.UserPermissions;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserPermissionsService implements IUserPermissionsService {

	private static final String USER_PERMISSIONS = "/user/permissions";

	private static final String UNEXPECTED_CODE = "Unexpected code: ";

	private final OkHttpClient httpClient = HttpClientUtil.basicBuilder()//
			.build();

	public static final UserPermissionsService INSTANCE = new UserPermissionsService();

	private boolean hubSupportsPermissions = false;

	private UserPermissions userPermissions = new UserPermissions();

	private UserPermissionsService() {

		Thread updateThread = new Thread("Hub Permission update thread") {
			@Override
			public void run() {

				while (true) {
					try {
						updateUserPermissions();
					} catch (final IOException e1) {
						// e1.printStackTrace();
					}

					try {
						Thread.sleep(10_000);
					} catch (final InterruptedException e) {
						interrupt(); // preserve interruption status
						return;
					}
				}
			}

		};
		updateThread.start();
		try {
			updateUserPermissions();
		} catch (IOException e) {
			// Ignore errors
		}

	}

	public synchronized void updateUserPermissions() throws IOException {

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseUrlIfAvailable();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return;
		}
		hubSupportsPermissions = true;
		{
			final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
					.url(upstreamURL + USER_PERMISSIONS) //
					.build();

			try (Response response = httpClient.newCall(request).execute()) {
				if (!response.isSuccessful()) {
					response.body().close();
					if (response.code() == 404) {
						hubSupportsPermissions = false;
						return;
						// Unsupported API
					}
					throw new IOException(UNEXPECTED_CODE + response);
				}

				this.userPermissions = new ObjectMapper().readValue(response.body().string(), UserPermissions.class);
			}
		}
	}

	@Override
	public synchronized boolean isPermitted(String service, String permission) {
		if (!hubSupportsPermissions) {
			return true;
		}
		UserPermissions up = this.userPermissions;
		if (up != null) {
			Map<String, List<String>> services = up.getServices();
			if (services != null) {
				List<String> list = services.get(service);
				if (list != null) {
					return list.contains(permission);
				}
			}
		}

		return false;
	}

	@Override
	public boolean hubSupportsPermissions() {
		return hubSupportsPermissions;
	}

}
