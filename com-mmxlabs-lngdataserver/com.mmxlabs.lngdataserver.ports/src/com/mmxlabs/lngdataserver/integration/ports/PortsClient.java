package com.mmxlabs.lngdataserver.integration.ports;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lngdataservice.ports.model.Version;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PortsClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(PortsClient.class);
	private static final OkHttpClient CLIENT = new OkHttpClient();
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	public static boolean saveVersion(String baseUrl, Version version) throws IOException {

		String json = new ObjectMapper().writeValueAsString(version.getPorts());

		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(baseUrl + "/ports/bulk").post(body).build();
		Response response = CLIENT.newCall(request).execute();

		if (!response.isSuccessful()) {
			LOGGER.error("Error publishing version: " + response.message());
			return false;
		}
		return true;
	}
}
