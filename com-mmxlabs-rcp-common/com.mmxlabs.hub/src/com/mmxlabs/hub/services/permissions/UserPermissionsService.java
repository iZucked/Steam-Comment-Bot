/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.hub.services.permissions;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.common.http.HttpClientUtil;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserPermissionsService implements IUserPermissionsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserPermissionsService.class);

	private final OkHttpClient httpClient = HttpClientUtil.basicBuilder()//
			.build();

	public static final UserPermissionsService INSTANCE = new UserPermissionsService();

	private boolean hubSupportsPermissions = false;

	private UserPermissions userPermissions = new UserPermissions();

	private static final String UNEXPECTED_CODE = "Unexpected code: ";

	protected boolean userPermissionsAreSet = false;

	public boolean hasUserPermissions() {
		return userPermissionsAreSet;
	}

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

		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(UpstreamUrlProvider.USER_PERMISSIONS_ENDPOINT);
		if (requestBuilder == null) {
			return;
		}

		hubSupportsPermissions = true;
		final Request request = requestBuilder //
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				if (response.code() == 404) {
					hubSupportsPermissions = false;
				} else if (response.code() == 401) {
					throw new IOException(UNEXPECTED_CODE + response);
				}
			} else {
				String responseString = response.body().string();
				userPermissions = new ObjectMapper().readValue(responseString, UserPermissions.class);
				userPermissionsAreSet = true;
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
