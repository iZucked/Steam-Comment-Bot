/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.vessels;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VesselsUploaderClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(VesselsUploaderClient.class);
	private static final OkHttpClient CLIENT = new OkHttpClient();
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	private VesselsUploaderClient() {

	}

	public static boolean saveVersion(String baseUrl, VesselsVersion version) throws IOException {
		String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(version);

		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(baseUrl + "/vessels/sync/versions").post(body).build();
		try (Response response = CLIENT.newCall(request).execute()) {

			if (!response.isSuccessful()) {
				LOGGER.error("Error publishing version: " + response.message());
				return false;
			}
			return true;
		}
	}
}
