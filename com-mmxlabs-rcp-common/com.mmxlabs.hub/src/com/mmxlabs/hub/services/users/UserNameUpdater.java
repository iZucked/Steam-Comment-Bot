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
import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.common.http.HttpClientUtil;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserNameUpdater {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserNameUpdater.class);

	private static final OkHttpClient httpClient = HttpClientUtil.basicBuilder() //
			.build();

	public static void refreshUserId() {
		try {
			String userId = getCurrentUserId();
			if (userId != null) {
				UsernameProvider.INSTANCE.setUserId(userId);
			}
		} catch (Exception e) {
			// Ignore
		}
	}

	public static String getDisplayName(String userId) {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new Jdk8Module());

		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder("/user/displayname");
		if (requestBuilder == null) {
			return userId;
		}
		RequestBody requestBody = new MultipartBody.Builder() //
				.setType(MultipartBody.FORM) //
				.addFormDataPart("userId", userId) //
				.build();

		final Request request = requestBuilder //
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

	private static String getCurrentUserId() throws Exception {

		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder("/user/id");
		if (requestBuilder == null) {
			return null;
		}

		final Request request = requestBuilder //
				.build();

		OkHttpClient.Builder clientBuilder = httpClient.newBuilder();
		final OkHttpClient localHttpClient = clientBuilder //
				.build();
		try (Response response = localHttpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code: " + response);
			}
			return response.body().string();
		}
	}
}
