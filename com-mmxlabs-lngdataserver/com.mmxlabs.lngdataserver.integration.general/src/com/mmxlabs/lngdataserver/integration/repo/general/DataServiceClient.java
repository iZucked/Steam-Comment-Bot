/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.repo.general;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.commons.http.IProgressListener;
import com.mmxlabs.lngdataserver.commons.http.ProgressRequestBody;
import com.mmxlabs.lngdataserver.commons.http.ProgressResponseBody;
import com.mmxlabs.lngdataserver.server.HttpClientUtil;
import com.mmxlabs.lngdataserver.server.UpstreamUrlProvider;
import com.mmxlabs.scenario.service.model.util.encryption.EncryptionUtils;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class DataServiceClient {
	private static final Logger LOG = LoggerFactory.getLogger(DataServiceClient.class);

	private static final String CONST_AUTHORIZATION = "Authorization";

	private static final OkHttpClient httpClient = HttpClientUtil.basicBuilder().build();//

	protected static final OkHttpClient LONG_POLLING_CLIENT = HttpClientUtil.basicBuilder() //
			.readTimeout(Integer.MAX_VALUE, TimeUnit.MILLISECONDS) //
			.build();

	private static final TypeReference<List<GeneralDataRecord>> TYPE_LIST = new TypeReference<List<GeneralDataRecord>>() {
	};

	public void upload(TypeRecord typeRecord, final String json, final IProgressListener progressListener) throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new Jdk8Module());
		RequestBody requestBody = RequestBody.create(HttpClientUtil.MEDIA_TYPE_JSON, json);

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseUrlIfAvailable();
		if (progressListener != null) {
			requestBody = new ProgressRequestBody(requestBody, progressListener);
		}
		final String requestURL = String.format("%s%s", upstreamURL, typeRecord.getUploadURL());
		final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
				.url(requestURL) //
				.post(requestBody).build();

		// Check the response
		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				if (response.code() == 409) {
					throw new IOException("Data already exists");
				}
				throw new IOException("Unexpected code " + response);
			}
		}
	}

	public void downloadTo(TypeRecord typeRecord, final String uuid, final File file, final IProgressListener progressListener) throws Exception {
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
		final String requestURL = String.format("%s%s", upstreamURL, typeRecord.getDownloadURL(uuid));

		final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
				.url(requestURL) //
				.build();

		try (Response response = localHttpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				response.body().close();
				throw new IOException("Unexpected code: " + response);
			}
			try (BufferedSource bufferedSource = response.body().source()) {
				try (FileOutputStream fos = new FileOutputStream(file)) {
					EncryptionUtils.encrypt(fos, os -> {
						typeRecord.writeHeader(os);
						try (final BufferedSink bufferedSink = Okio.buffer(Okio.sink(os))) {
							bufferedSink.writeAll(bufferedSource);
						}
					});
				}
			}
		}
	}

	public Pair<String, Instant> getRecords(TypeRecord typeRecord) throws IOException {

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseUrlIfAvailable();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return null;
		}

		final String requestURL = String.format("%s%s", upstreamURL, typeRecord.getListURL());
		final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
				.url(requestURL) //
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code: " + response);
			}
			final Instant lastModified = Instant.now();
			final String jsonData = response.body().string();
			return new Pair<>(jsonData, lastModified);
		}
	}

	public void deleteData(TypeRecord typeRecord, final String uuid) throws IOException {

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseUrlIfAvailable();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return;
		}
		final String requestURL = String.format("%s%s", upstreamURL, typeRecord.getDeleteURL(uuid));
		final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
				.url(requestURL) //
				.delete() //
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code: " + response);
			}
		}
	}

	public @Nullable List<GeneralDataRecord> parseRecordsJSONData(TypeRecord typeRecord, final String jsonData) {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		try {
			return mapper.addMixIn(GeneralDataRecord.class, typeRecord.getMixin()).readValue(jsonData, TYPE_LIST);
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

	protected CompletableFuture<Boolean> notifyOnNewVersion(TypeRecord typeRecord) {
		final String baseUrl = UpstreamUrlProvider.INSTANCE.getBaseUrlIfAvailable();

		if (baseUrl == null || baseUrl.isEmpty()) {
			return null;
		}
		final String requestURL = baseUrl + typeRecord.getVersionNotificationEndpoint();
		LOG.debug("Calling url {}", requestURL);
		return CompletableFuture.supplyAsync(() -> {
			final Request.Builder requestBuilder = UpstreamUrlProvider.INSTANCE.makeRequest() //
					.url(requestURL) //
			;
			final Request request = requestBuilder.build();
			try (Response response = LONG_POLLING_CLIENT.newCall(request).execute()) {
				if (response.isSuccessful()) {
					return Boolean.TRUE;
				}
				return Boolean.FALSE;
			} catch (final IOException e) {
				LOG.error("Error waiting for new version");
				throw new RuntimeException("Error waiting for new version");
			}
		});
	}

}
