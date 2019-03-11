/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.api;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.commons.http.IProgressListener;
import com.mmxlabs.lngdataserver.commons.http.ProgressRequestBody;
import com.mmxlabs.lngdataserver.commons.http.ProgressResponseBody;
import com.mmxlabs.lngdataserver.server.HttpClientUtil;
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

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();
		if (progressListener != null) {
			requestBody = new ProgressRequestBody(requestBody, progressListener);
		}

		final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
				.url(upstreamURL + SCENARIO_UPLOAD_URL) //
				.post(requestBody).build();

		// Check the response
		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				response.body().close();
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

		final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
				.url(String.format("%s%s%s", upstreamURL, SCENARIO_DOWNLOAD_URL, uuid)) //
				.build();

		try (Response response = localHttpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				response.body().close();
				throw new IOException("Unexpected code: " + response);
			}
			try (BufferedSource bufferedSource = response.body().source()) {
				final BufferedSink bufferedSink = Okio.buffer(Okio.sink(file));
				bufferedSink.writeAll(bufferedSource);
				bufferedSink.close();
				return true;
			}
		}
	}

	public String getBaseCaseDetails(final String uuid) throws IOException {

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return null;
		}
		final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
				.url(upstreamURL + SCENARIO_DOWNLOAD_URL + uuid + "/details") //
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				response.body().close();
				throw new IOException("Unexpected code: " + response);
			}
			final String value = response.body().string();

			return value;
		}
	}

	public Pair<String, Instant> getScenarios() throws IOException {

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return null;
		}
		final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
				.url(upstreamURL + SCENARIO_LIST_URL) //
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

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return;
		}
		final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
				.url(upstreamURL + SCENARIO_DELETE_URL + uuid) //
				.delete() //
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code: " + response);
			}
		}
	}

	public Instant getLastModified() {

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return null;
		}
		final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
				.url(upstreamURL + SCENARIO_LAST_MODIFIED_URL) //
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

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return;
		}

		final RequestBody requestBody = new MultipartBody.Builder() //
				.setType(MultipartBody.FORM) //
				.addFormDataPart("path", newPath) //
				.build();
		final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
				.url(upstreamURL + SCENARIO_MOVE_URL + uuid) //
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
