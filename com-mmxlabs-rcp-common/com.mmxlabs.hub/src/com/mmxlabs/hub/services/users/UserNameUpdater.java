/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.hub.services.users;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.mmxlabs.hub.IUpstreamDetailChangedListener;
import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.common.http.HttpClientUtil;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserNameUpdater {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserNameUpdater.class);

	private final IUpstreamDetailChangedListener detailChangedListener = this::refresh;


	private static final OkHttpClient httpClient = HttpClientUtil.basicBuilder() //
			.build();
 
	public void refresh() {
		final boolean available = UpstreamUrlProvider.INSTANCE.isAvailable();
		if (available) {
			try {
				String userId = getCurrentUserId();
				if (userId != null) {
					UsernameProvider.INSTANCE.setUserId(userId);
				}
			} catch (Exception e) {
				// Ignore
			}
		}
	}

	public static String getDisplayName(String userId) {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new Jdk8Module());

		RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("userId", userId).build();

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseUrlIfAvailable();

		final String requestURL = String.format("%s%s", upstreamURL, "/user/displayname");
		final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
				.url(requestURL) //
				.post(requestBody).build();

		// Check the response
		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				if (response.code() == 404) {
					// Assume older hub without this endpoint
					return userId;
				}
				return null;
			}
			UserNameRecord record = mapper.readValue(response.body().bytes(), UserNameRecord.class);
			return record.getDisplayName();
		} catch (Exception e) {
			// ignore
		}

		return userId;
	}

	public String getCurrentUserId() throws Exception {
		OkHttpClient.Builder clientBuilder = httpClient.newBuilder();

		final OkHttpClient localHttpClient = clientBuilder //
				.build();

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseUrlIfAvailable();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return null;
		}
		final String requestURL = String.format("%s%s", upstreamURL, "/user/id");

		final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
				.url(requestURL) //
				.build();

		try (Response response = localHttpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code: " + response);
			}
			return response.body().string();
		}
	}
}
