/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.mmxlabs.lngdataserver.integration.pricing.model.PricingVersion;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PricingUploadClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(PricingUploadClient.class);
	private static final OkHttpClient CLIENT = new OkHttpClient();
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	private PricingUploadClient() {

	}

	public static boolean saveVersion(final String baseUrl, final PricingVersion version) throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new Jdk8Module());
		final String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(version);
		final RequestBody body = RequestBody.create(JSON, json);
		final Request request = new Request.Builder().url(baseUrl + "/pricing/sync/versions").post(body).build();
		try (Response response = CLIENT.newCall(request).execute()) {

			if (!response.isSuccessful()) {
				LOGGER.error("Error publishing version: " + response.message());
				return false;
			}
			return true;
		}
	}
}
