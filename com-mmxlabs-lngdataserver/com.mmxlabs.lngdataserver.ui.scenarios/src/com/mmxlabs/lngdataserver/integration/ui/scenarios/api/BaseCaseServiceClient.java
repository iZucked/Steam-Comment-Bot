/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.api;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

import org.json.JSONObject;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.commons.http.IProgressListener;
import com.mmxlabs.lngdataserver.commons.http.ProgressRequestBody;
import com.mmxlabs.lngdataserver.commons.http.ProgressResponseBody;
import com.mmxlabs.lngdataserver.server.UpstreamUrlProvider;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class BaseCaseServiceClient {

	private static final String BASECASE_UPLOAD_URL = "/scenarios/v1/basecase/upload";
	private static final String BASECASE_DOWNLOAD_URL = "/scenarios/v1/basecase/";
	private static final String BASECASE_CURRENT_URL = "/scenarios/v1/basecase/current";

	// public String uploadBaseCase(File file, String portsVersionUUID, String vesselsVersionUUID, String pricingVersionUUID, String distancesVersionUUID) throws IOException {
	public String uploadBaseCase(File file, IProgressListener progressListener) throws IOException {

		okhttp3.MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
		RequestBody requestBody = new MultipartBody.Builder() //
				.setType(MultipartBody.FORM) //
				// .addFormDataPart("pricingVersion", pricingVersionUUID)
				// .addFormDataPart("portsVersion", portsVersionUUID)
				// .addFormDataPart("vesselsVersion", vesselsVersionUUID)
				// .addFormDataPart("distancesVersion", distancesVersionUUID)
				.addFormDataPart("basecase", "basecase.lingo", RequestBody.create(mediaType, file))//
				.build();
		
		if (progressListener != null) {
			requestBody = new ProgressRequestBody(requestBody, progressListener);
		}

		String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();

		Request request = new Request.Builder() //
				.url(upstreamURL + BASECASE_UPLOAD_URL) //
				.header("Authorization", Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword()))//
				.post(requestBody).build();

		OkHttpClient httpClient = new OkHttpClient.Builder() //
				.build();

		// Check the response
		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				response.body().close();
				throw new IOException("Unexpected code " + response);
			}

			String responseStr = response.body().string();
			return responseStr;
		}
	}

	public boolean downloadTo(String uuid, File file, IProgressListener progressListener) throws IOException {
		OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
		if (progressListener != null) {
			clientBuilder = clientBuilder.addNetworkInterceptor(new Interceptor() {
				@Override
				public Response intercept(Chain chain) throws IOException {
					Response originalResponse = chain.proceed(chain.request());
					return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(), progressListener)).build();
				}
			});
		}
		OkHttpClient httpClient = clientBuilder //
				.build();

		String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();

		Request request = new Request.Builder() //
				.url(String.format("%s%s%s", upstreamURL, BASECASE_DOWNLOAD_URL, uuid)) //
				.header("Authorization", Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword()))//
				.build();

		Response response = httpClient.newCall(request).execute();
		if (!response.isSuccessful()) {
			response.body().close();
			throw new IOException("Unexpected code: " + response);
		}
		try (BufferedSource bufferedSource = response.body().source()) {
			BufferedSink bufferedSink = Okio.buffer(Okio.sink(file));
			bufferedSink.writeAll(bufferedSource);
			bufferedSink.close();
		}
		return true;
	}

	public static String getCurrentBaseCase() throws IOException {
		String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();

		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return null;
		}

		if (!UpstreamUrlProvider.INSTANCE.isAvailable()) {
			return null;
		}

		OkHttpClient httpClient = new OkHttpClient.Builder() //
				.build();

		Request request = new Request.Builder() //
				.url(upstreamURL + BASECASE_CURRENT_URL) //
				.header("Authorization", Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword()))//
				.build();

		Response response = httpClient.newCall(request).execute();
		if (!response.isSuccessful()) {
			response.body().close();
			// 404 Not found is a valid response if there is no current basecase 
			if (response.code() != 404) {
				throw new IOException("Unexpected code: " + response);
			}
			return null;
		}
		String value = response.body().string();

		return value;
	}

	public static String setCurrentBaseCase(String uuid) throws IOException {
		OkHttpClient httpClient = new OkHttpClient.Builder() //
				.build();

		String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return null;
		}
		Request request = new Request.Builder() //
				.url(upstreamURL + BASECASE_CURRENT_URL + "/" + uuid) //
				.header("Authorization", Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword()))//
				.build();

		Response response = httpClient.newCall(request).execute();
		if (!response.isSuccessful()) {
			response.body().close();
			throw new IOException("Unexpected code: " + response);
		}
		String value = response.body().string();

		return value;
	}

	public static String getBaseCaseDetails(String uuid) throws IOException {
		OkHttpClient httpClient = new OkHttpClient.Builder() //
				.build();

		String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return null;
		}
		Request request = new Request.Builder() //
				.url(upstreamURL + BASECASE_DOWNLOAD_URL + uuid + "/details") //
				.header("Authorization", Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword()))//
				.build();

		Response response = httpClient.newCall(request).execute();
		if (!response.isSuccessful()) {
			response.body().close();
			throw new IOException("Unexpected code: " + response);
		}
		String value = response.body().string();

		return value;
	}

	public Pair<String, Instant> parseScenariosJSONData(String jsonData) {
		final JSONObject versionObject = new JSONObject(jsonData);

		final String uuidString = versionObject.getString("uuid");
		final String creationDate = versionObject.getString("creationDate");
		Instant instant = Instant.parse(creationDate);
		return new Pair<>(uuidString, instant);
	}
}
