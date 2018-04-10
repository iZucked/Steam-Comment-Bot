/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
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
import com.mmxlabs.lngdataserver.server.UpstreamUrlProvider;

import okhttp3.Credentials;
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

	public String uploadScenario(File file, String path, IProgressListener progressListener) throws IOException {

		okhttp3.MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
		RequestBody requestBody = new MultipartBody.Builder() //
				.setType(MultipartBody.FORM) //
				.addFormDataPart("scenario", "scenario.lingo", RequestBody.create(mediaType, file))//
				.addFormDataPart("path", path) //
				.build();

		String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();
		if (progressListener != null) {
			requestBody = new ProgressRequestBody(requestBody, progressListener);
		}

		Request request = new Request.Builder() //
				.url(upstreamURL + SCENARIO_UPLOAD_URL) //
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

			String jsonData = response.body().string();
			final JSONObject Jobject = new JSONObject(jsonData);
			final String uuidString = Jobject.getString("uuid");
			return uuidString;
		}
	}

	public boolean downloadTo(String uuid, File file) throws IOException {
		OkHttpClient httpClient = new OkHttpClient.Builder() //
				.build();

		String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();

		Request request = new Request.Builder() //
				.url(String.format("%s%s%s", upstreamURL, SCENARIO_DOWNLOAD_URL, uuid)) //
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
			return true;
		}
	}

	public static String getBaseCaseDetails(String uuid) throws IOException {
		OkHttpClient httpClient = new OkHttpClient.Builder() //
				.build();

		String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return null;
		}
		Request request = new Request.Builder() //
				.url(upstreamURL + SCENARIO_DOWNLOAD_URL + uuid + "/details") //
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

	public static Pair<List<Pair<String, String>>, Instant> getScenarios() throws IOException {
		OkHttpClient httpClient = new OkHttpClient.Builder() //
				.build();

		String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return null;
		}
		Request request = new Request.Builder() //
				.url(upstreamURL + SCENARIO_LIST_URL) //
				.header("Authorization", Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword()))//
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code: " + response);
			}
			String jsonData = response.body().string();
			// final String jsonData = response.body().string();
			final JSONArray Jobject = new JSONArray(jsonData);
			List<Pair<String, String>> l = new LinkedList<>();
			for (int i = 0; i < Jobject.length(); ++i) {
				final JSONObject versionObject = Jobject.getJSONObject(i);
				final String uuidString = versionObject.getString("uuid");
				final String pathString = versionObject.getString("path");
				l.add(new Pair<>(uuidString, pathString));
			}
			String date = response.headers().get("MMX-LastModified");
			if (date != null) {
				Instant lastModified = Instant.ofEpochSecond(Long.parseLong(date));
				return new Pair<>(l, lastModified);
			}
		}
		return null;
	}

	public static void deleteScenario(String uuid) throws IOException {
		OkHttpClient httpClient = new OkHttpClient.Builder() //
				.build();

		String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return;
		}
		Request request = new Request.Builder() //
				.url(upstreamURL + SCENARIO_DELETE_URL + uuid) //
				.header("Authorization", Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword()))//
				.delete() //
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code: " + response);
			}
		}
	}

	public Instant getLastModified() {
		OkHttpClient httpClient = new OkHttpClient.Builder() //
				.build();

		String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return null;
		}
		Request request = new Request.Builder() //
				.url(upstreamURL + SCENARIO_LAST_MODIFIED_URL) //
				.header("Authorization", Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword()))//
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (response.isSuccessful()) {
				String date = response.body().string();
				Instant lastModified = Instant.ofEpochSecond(Long.parseLong(date));
				return lastModified;
				// throw new IOException("Unexpected code: " + response);
			}
		} catch (Exception e) {

		}
		return null;
	}

	public void rename(String uuid, String newPath) throws IOException {
		OkHttpClient httpClient = new OkHttpClient.Builder() //
				.build();

		String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return;
		}

		RequestBody requestBody = new MultipartBody.Builder() //
				.setType(MultipartBody.FORM) //
				.addFormDataPart("path", newPath) //
				.build();
		Request request = new Request.Builder() //
				.url(upstreamURL + SCENARIO_MOVE_URL + uuid) //
				.header("Authorization", Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword()))//
				.post(requestBody) //
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code: " + response);
			}
		}
	}
}
