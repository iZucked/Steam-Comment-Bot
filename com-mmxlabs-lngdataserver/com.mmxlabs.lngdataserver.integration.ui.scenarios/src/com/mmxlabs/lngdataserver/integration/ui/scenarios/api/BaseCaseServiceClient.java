/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.api;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

import org.json.JSONObject;

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

	private static final String UNEXPECTED_CODE = "Unexpected code: ";
	private static final String BASECASE_UPLOAD_URL = "/scenarios/v1/basecase/upload";
	private static final String BASECASE_DOWNLOAD_URL = "/scenarios/v1/basecase/";
	private static final String BASECASE_CURRENT_URL = "/scenarios/v1/basecase/current";

	private final OkHttpClient httpClient = new OkHttpClient.Builder() //
			.build();

	private final okhttp3.MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

	public String uploadBaseCase(final File file, //
			final String scenarioName, ///
			final String pricingVersionUUID, //
			final String portsVersionUUID, //
			final String distancesVersionUUID, //
			final String vesselsVersionUUID, //
			final IProgressListener progressListener) throws IOException {
		if (pricingVersionUUID == null) {
			throw new IllegalArgumentException("Pricing version cannot be null");
		}
		RequestBody requestBody = new MultipartBody.Builder() //
				.setType(MultipartBody.FORM) //
				.addFormDataPart("pricingVersionUUID", pricingVersionUUID) //
				.addFormDataPart("portsVersionUUID", portsVersionUUID) //
				.addFormDataPart("vesselsVersionUUID", vesselsVersionUUID) //
				.addFormDataPart("distancesVersionUUID", distancesVersionUUID) //
				.addFormDataPart("basecase", scenarioName, RequestBody.create(mediaType, file)) //
				.build();

		if (progressListener != null) {
			requestBody = new ProgressRequestBody(requestBody, progressListener);
		}

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();

		final Request request = new Request.Builder() //
				.url(upstreamURL + BASECASE_UPLOAD_URL) //
				.header("Authorization", Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword()))//
				.post(requestBody).build();

		// Check the response
		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				response.body().close();
				throw new IOException("Unexpected code " + response);
			}

			final String responseStr = response.body().string();
			return responseStr;
		}
	}

	public boolean downloadTo(final String uuid, final File file, final IProgressListener progressListener) throws IOException {
		OkHttpClient.Builder clientBuilder = httpClient.newBuilder();
		if (progressListener != null) {
			clientBuilder = clientBuilder.addNetworkInterceptor(new Interceptor() {
				@Override
				public Response intercept(final Chain chain) throws IOException {
					final Response originalResponse = chain.proceed(chain.request());
					return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(), progressListener)).build();
				}
			});
		}
		final OkHttpClient localHttpClient = clientBuilder //
				.build();

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();

		final Request request = new Request.Builder() //
				.url(String.format("%s%s%s", upstreamURL, BASECASE_DOWNLOAD_URL, uuid)) //
				.header("Authorization", Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword()))//
				.build();

		try (Response response = localHttpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				response.body().close();
				throw new IOException(UNEXPECTED_CODE + response);
			}
			try (BufferedSource bufferedSource = response.body().source()) {
				final BufferedSink bufferedSink = Okio.buffer(Okio.sink(file));
				bufferedSink.writeAll(bufferedSource);
				bufferedSink.close();
			}
			return true;
		}
	}

	public String getCurrentBaseCase() throws IOException {
		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();

		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return null;
		}

		if (!UpstreamUrlProvider.INSTANCE.isAvailable()) {
			return null;
		}

		final Request request = new Request.Builder() //
				.url(upstreamURL + BASECASE_CURRENT_URL) //
				.header("Authorization", Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword()))//
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				response.body().close();
				// 404 Not found is a valid response if there is no current basecase
				if (response.code() != 404) {
					throw new IOException(UNEXPECTED_CODE + response);
				}
				return "";
			}
			return response.body().string();
		}
	}

	public String setCurrentBaseCase(final String uuid) throws IOException {

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return null;
		}
		final Request request = new Request.Builder() //
				.url(upstreamURL + BASECASE_CURRENT_URL + "/" + uuid) //
				.header("Authorization", Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword()))//
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				response.body().close();
				throw new IOException(UNEXPECTED_CODE + response);
			}
			return response.body().string();
		}
	}

	public String getBaseCaseDetails(final String uuid) throws IOException {

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return null;
		}
		final Request request = new Request.Builder() //
				.url(upstreamURL + BASECASE_DOWNLOAD_URL + uuid + "/details") //
				.header("Authorization", Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword()))//
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				response.body().close();
				throw new IOException(UNEXPECTED_CODE + response);
			}
			return response.body().string();
		}
	}

	public BaseCaseRecord parseScenariosJSONData(final String jsonData) {
		final JSONObject versionObject = new JSONObject(jsonData);
		final BaseCaseRecord record = new BaseCaseRecord();
		record.uuid = versionObject.getString("uuid");
		record.creator = versionObject.getString("creator");
		if (!versionObject.isNull("originalName")) {
			record.originalName = versionObject.getString("originalName");
		} else {
			record.originalName = record.uuid;
		}
		if (!versionObject.isNull("pricingVersionUUID")) {
			record.pricingVersionUUID = versionObject.getString("pricingVersionUUID");
		}
		final String creationDate = versionObject.getString("creationDate");
		record.creationDate = Instant.parse(creationDate);
		return record;
	}

}
