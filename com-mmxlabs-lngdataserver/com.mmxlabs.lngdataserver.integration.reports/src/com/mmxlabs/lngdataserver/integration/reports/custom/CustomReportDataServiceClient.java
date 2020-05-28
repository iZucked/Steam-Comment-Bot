/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.custom;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.common.Pair;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.hub.common.http.IProgressListener;
import com.mmxlabs.hub.common.http.ProgressRequestBody;
import com.mmxlabs.hub.common.http.ProgressResponseBody;
import com.mmxlabs.lngdataserver.server.UpstreamUrlProvider;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class CustomReportDataServiceClient {

	private static final String SCENARIO_UPLOAD_URL = "/reports/v1/team/custom/upload";
	private static final String SCENARIO_DELETE_URL = "/reports/v1/team/custom/remove";
	private static final String SCENARIO_ENDPOINT_URL = "/reports/v1/team/custom";
	private static final String SCENARIO_LAST_MODIFIED_URL = "/reports/v1/team/custom/mod/lastModified";

	private final OkHttpClient httpClient = com.mmxlabs.hub.common.http.HttpClientUtil.basicBuilder() //
			.build();

	public String upload(final String uuid, final String contentType, final File file, final IProgressListener progressListener) throws IOException {

		RequestBody requestBody = new MultipartBody.Builder() //
				.setType(MultipartBody.FORM) //
				.addFormDataPart("report", "data.json", RequestBody.create(HttpClientUtil.MEDIA_TYPE_FORM_DATA, file))//
				.addFormDataPart("uuid", uuid)
				.addFormDataPart("contentType", contentType) //
				.build();

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseUrlIfAvailable();
		if (progressListener != null) {
			requestBody = new ProgressRequestBody(requestBody, progressListener);
		}
		final String requestURL = String.format("%s%s/%s", upstreamURL, SCENARIO_UPLOAD_URL, uuid);
		final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
				.url(requestURL) //
				.post(requestBody).build();

		// Check the response
		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				response.body().close();
				if (response.code() == 409) {
					throw new IOException("Data already exists " +  "/" + uuid);
				}

				throw new IOException("Unexpected code " + response);
			}
			return uuid;
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

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseUrlIfAvailable();
		final String requestURL = String.format("%s%s/%s", upstreamURL, SCENARIO_ENDPOINT_URL, uuid);

		final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
				.url(requestURL) //
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

	public Pair<String, Instant> getRecords() throws IOException {

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseUrlIfAvailable();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return null;
		}
		final String requestURL = String.format("%s%s", upstreamURL, SCENARIO_ENDPOINT_URL);
		final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
				.url(requestURL) //
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

	public void deleteData(final String uuid) throws IOException {

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseUrlIfAvailable();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return;
		}
		final String requestURL = String.format("%s%s/%s", upstreamURL, SCENARIO_DELETE_URL, uuid);
		final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
				.url(requestURL) //
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code: " + response);
			}
		}
	}

	public Instant getLastModified() {

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseUrlIfAvailable();
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
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//
	private static final TypeReference<List<CustomReportDataRecord>> TYPE_GDR_LIST = new TypeReference<List<CustomReportDataRecord>>() {
	};

	public static @Nullable List<CustomReportDataRecord> parseRecordsJSONData(final String jsonData) {
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
