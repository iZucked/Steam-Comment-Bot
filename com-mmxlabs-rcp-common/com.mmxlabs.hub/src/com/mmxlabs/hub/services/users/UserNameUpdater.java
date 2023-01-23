/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.hub.services.users;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.common.http.HttpClientUtil;

public class UserNameUpdater {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserNameUpdater.class);

	public static void refreshUserId() {
		try {
			final String userId = getCurrentUserId();
			if (userId != null) {
				UsernameProvider.INSTANCE.setUserId(userId);
			}
		} catch (final Exception e) {
			// Ignore
		}
	}

	public static String getDisplayName(final String userId) {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new Jdk8Module());

		try {
			return DataHubServiceProvider.getInstance().doPostRequest("/user/displayname", request -> {

				final MultipartEntityBuilder formDataBuilder = MultipartEntityBuilder.create();
				formDataBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
				formDataBuilder.addTextBody("userId", userId);
				final HttpEntity entity = formDataBuilder.build();

				request.setEntity(entity);
			}, response -> {
				final int responseCode = response.getStatusLine().getStatusCode();
				if (!HttpClientUtil.isSuccessful(responseCode)) {
					if (responseCode == 404) {
						// Assume older hub without this endpoint
						return userId;
					}
					if (responseCode == 204) {
						// Endpoint supported, but no mapping found
						return userId;
					}
					return null;
				}
				final UserNameRecord userNameRecord = mapper.readValue(response.getEntity().getContent(), UserNameRecord.class);
				return userNameRecord.getDisplayName();
			});
		} catch (final Exception e) {
			// ignore
		}
		return userId;
	}

	private static String getCurrentUserId() throws Exception {

		return DataHubServiceProvider.getInstance().doGetRequest("/user/id", response -> {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				throw new IOException("Unexpected code: " + response);
			}
			return EntityUtils.toString(response.getEntity());
		});
	}
}
