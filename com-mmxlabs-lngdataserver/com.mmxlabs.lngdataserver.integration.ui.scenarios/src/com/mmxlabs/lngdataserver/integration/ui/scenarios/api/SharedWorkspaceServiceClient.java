/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mmxlabs.common.Pair;
import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.hub.common.http.IProgressListener;
import com.mmxlabs.hub.common.http.ProgressRequestBody;
import com.mmxlabs.hub.common.http.ProgressResponseBody;
import com.mmxlabs.scenario.service.model.util.encryption.DataStreamReencrypter;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSource;

public class SharedWorkspaceServiceClient {

	private static final String SCENARIO_UPLOAD_URL = "/scenarios/v1/shared/upload";
	private static final String SCENARIO_DOWNLOAD_URL = "/scenarios/v1/shared/";
	private static final String SCENARIO_DELETE_URL = "/scenarios/v1/shared/";
	private static final String SCENARIO_LIST_URL = "/scenarios/v1/shared/scenarios";
	private static final String SCENARIO_MOVE_URL = "/scenarios/v1/shared/move/";
	private static final String SCENARIO_LAST_MODIFIED_URL = "/scenarios/v1/shared/lastModified";

	private final OkHttpClient httpClient = HttpClientUtil.basicBuilder() //
			.build();

	private final okhttp3.MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

	public String uploadScenario(final File file, final String path, final IProgressListener progressListener) throws IOException {

		RequestBody requestBody = new MultipartBody.Builder() //
				.setType(MultipartBody.FORM) //
				.addFormDataPart("scenario", "scenario.lingo", RequestBody.create(mediaType, file))//
				.addFormDataPart("path", path) //
				.build();

		if (progressListener != null) {
			requestBody = new ProgressRequestBody(requestBody, progressListener);
		}

		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(SCENARIO_UPLOAD_URL);
		if (requestBuilder == null) {
			return null;
		}

		final Request request = requestBuilder //
				.post(requestBody) //
				.build();

		// Check the response
		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				if (response.code() == 409) {
					throw new IOException("Scenario already exists " + path);
				}

				throw new IOException("Unexpected code " + response);
			}

			final String jsonData = response.body().string();
			final JSONObject jsonObject = new JSONObject(jsonData);
			return jsonObject.getString("uuid");
		}
	}

	public boolean downloadTo(final String uuid, final File file, final IProgressListener progressListener) throws Exception {
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

		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(String.format("%s%s", SCENARIO_DOWNLOAD_URL, uuid));
		if (requestBuilder == null) {
			return false;
		}

		final Request request = requestBuilder //
				.build();

		try (Response response = localHttpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code: " + response);
			}
			try (FileOutputStream out = new FileOutputStream(file)) {
				try (BufferedSource bufferedSource = response.body().source()) {
					DataStreamReencrypter.reencryptScenario(bufferedSource.inputStream(), out);
					return true;
				}
			}
		}
	}

	public String getBaseCaseDetails(final String uuid) throws IOException {

		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(SCENARIO_DOWNLOAD_URL + uuid + "/details");
		if (requestBuilder == null) {
			return null;
		}

		final Request request = requestBuilder //
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code: " + response);
			}
			final String value = response.body().string();

			return value;
		}
	}

	public Pair<String, Instant> getScenarios() throws IOException {

		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(SCENARIO_LIST_URL);
		if (requestBuilder == null) {
			return null;
		}

		final Request request = requestBuilder //
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code: " + response);
			}
			final String date = response.headers().get("MMX-LastModified");
			if (date == null) {
				return null;
			}
			final Instant lastModified = Instant.ofEpochSecond(Long.parseLong(date));
			final String jsonData = response.body().string();
			return new Pair<>(jsonData, lastModified);
		}
	}

	public void deleteScenario(final String uuid) throws IOException {

		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(SCENARIO_DELETE_URL + uuid);
		if (requestBuilder == null) {
			return;
		}

		final Request request = requestBuilder //
				.delete() //
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code: " + response);
			}
		}
	}

	public Instant getLastModified() {

		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(SCENARIO_LAST_MODIFIED_URL);
		if (requestBuilder == null) {
			return null;
		}

		final Request request = requestBuilder //
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (response.isSuccessful()) {
				final String date = response.body().string();
				final Instant lastModified = Instant.ofEpochSecond(Long.parseLong(date));
				return lastModified;
				// throw new IOException("Unexpected code: " + response);
			}
		} catch (final Exception e) {

		}
		return null;
	}

	public void rename(final String uuid, final String newPath) throws IOException {

		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(SCENARIO_MOVE_URL + uuid);
		if (requestBuilder == null) {
			return;
		}

		final RequestBody requestBody = new MultipartBody.Builder() //
				.setType(MultipartBody.FORM) //
				.addFormDataPart("path", newPath) //
				.build();

		final Request request = requestBuilder //
				.post(requestBody) //
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {

				if (response.code() == 409) {
					throw new IOException("Scenario already exists " + newPath);
				}

				throw new IOException("Unexpected code: " + response);
			}
		}
	}

	public List<SharedScenarioRecord> parseScenariosJSONData(final String jsonData) {
		final JSONArray jObject = new JSONArray(jsonData);
		final List<SharedScenarioRecord> l = new LinkedList<>();
		for (int i = 0; i < jObject.length(); ++i) {
			final JSONObject versionObject = jObject.getJSONObject(i);

			SharedScenarioRecord record = new SharedScenarioRecord();
			record.uuid = versionObject.getString("uuid");
			record.creator = versionObject.getString("creator");
			record.pathString = versionObject.getString("path");

			final String creationDate = versionObject.getString("creationDate");
			record.creationDate = Instant.parse(creationDate);
			l.add(record);
		}
		return l;
	}
}
