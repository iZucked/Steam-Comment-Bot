/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.repo.generic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.common.Pair;
import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.hub.common.http.IProgressListener;
import com.mmxlabs.hub.common.http.ProgressRequestBody;
import com.mmxlabs.hub.common.http.ProgressResponseBody;
import com.mmxlabs.scenario.service.model.util.encryption.DataStreamReencrypter;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSource;

public class GenericDataServiceClient {

	private static final String SCENARIO_UPLOAD_URL = "/data/v1/upload";
	private static final String SCENARIO_DOWNLOAD_URL = "/data/v1/get";
	private static final String SCENARIO_DELETE_URL = "/data/v1/delete";
	private static final String SCENARIO_LIST_URL = "/data/v1/list";

	private static final String SCENARIO_LAST_MODIFIED_URL = "/data/v1/lastModified";

	private final OkHttpClient httpClient = HttpClientUtil.basicBuilder() //
			.build();

	public String upload(final String type, final String uuid, final String contentType, final File file, final IProgressListener progressListener) throws IOException {

		RequestBody requestBody = new MultipartBody.Builder() //
				.setType(MultipartBody.FORM) //
				.addFormDataPart("data", "data.json", RequestBody.create(HttpClientUtil.MEDIA_TYPE_FORM_DATA, file))//
				.addFormDataPart("contentType", contentType) //
				.build();

		if (progressListener != null) {
			requestBody = new ProgressRequestBody(requestBody, progressListener);
		}

		final String requestURL = String.format("%s/%s/%s", SCENARIO_UPLOAD_URL, type, uuid);
		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(requestURL);
		if (requestBuilder != null) {
			final Request request = requestBuilder //
					.post(requestBody) //
					.build();

			// Check the response
			try (Response response = httpClient.newCall(request).execute()) {
				if (!response.isSuccessful()) {
					if (response.code() == 409) {
						throw new IOException("Data already exists " + type + "/" + uuid);
					}

					throw new IOException("Unexpected code " + response);
				}

				final String jsonData = response.body().string();
				final JSONObject jsonObject = new JSONObject(jsonData);
				final String uuidString = jsonObject.getString("uuid");
				return uuidString;
			}
		}
		return null;
	}

	public boolean downloadTo(final String type, final String uuid, final File file, final IProgressListener progressListener) throws Exception {
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

		final String requestURL = String.format("%s/%s/%s", SCENARIO_DOWNLOAD_URL, type, uuid);
		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(requestURL);
		if (requestBuilder != null) {

			final Request request = requestBuilder.build();

			final OkHttpClient localHttpClient = clientBuilder.build();
			try (Response response = localHttpClient.newCall(request).execute()) {
				if (!response.isSuccessful()) {
					throw new IOException("Unexpected code: " + response);
				}

				try (FileOutputStream out = new FileOutputStream(file)) {
					try (BufferedSource bufferedSource = response.body().source()) {
						DataStreamReencrypter.reencryptData(bufferedSource.inputStream(), out);
						return true;
					}
				}
			}
		}
		return false;
	}

	public Pair<String, Instant> getRecords(final Collection<String> types) throws IOException {

		if (types.isEmpty()) {
			return null;
		}

		if (!DataHubServiceProvider.getInstance().isOnlineAndLoggedIn()) {
			return null;
		}

		final String typesList = String.join(",", types);
		final String requestURL = String.format("%s/%s", SCENARIO_LIST_URL, typesList);
		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(requestURL);
		if (requestBuilder == null) {
			return null;
		}

		final Request request = requestBuilder.build();
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

	public void deleteData(final String type, final String uuid) throws IOException {

		final String requestURL = String.format("%s/%s/%s", SCENARIO_DELETE_URL, type, uuid);
		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(requestURL);
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
			}
		} catch (final Exception e) {

		}
		return null;
	}

	//
	private static final TypeReference<List<GenericDataRecord>> TYPE_GDR_LIST = new TypeReference<List<GenericDataRecord>>() {
	};

	public static @Nullable List<GenericDataRecord> parseRecordsJSONData(final String jsonData) {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		try {
			return mapper.readValue(jsonData, TYPE_GDR_LIST);
		} catch (final JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
