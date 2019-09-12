/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.api;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lngdataserver.commons.http.IProgressListener;
import com.mmxlabs.lngdataserver.commons.http.ProgressRequestBody;
import com.mmxlabs.lngdataserver.commons.http.ProgressResponseBody;
import com.mmxlabs.lngdataserver.server.HttpClientUtil;
import com.mmxlabs.lngdataserver.server.UpstreamUrlProvider;

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

	private static final String LOCK_CHECK_URL = "/scenarios/v1/basecase/islocked";
	private static final String LOCK_BY_URL = "/scenarios/v1/basecase/lockedby";
	private static final String LOCK_URL = "/scenarios/v1/basecase/lock";
	private static final String LOCK_STATE_URL = "/scenarios/v1/basecase/lockState";
	private static final String UNLOCK_URL = "/scenarios/v1/basecase/unlock";

	private final OkHttpClient httpClient = HttpClientUtil.basicBuilder()//
			.build();

	private final okhttp3.MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

	private boolean needsLocking = false;
	private boolean isLocked = false;
	private boolean lockedByMe = false;
	private String lockedBy = null;

	public static BaseCaseServiceClient INSTANCE = new BaseCaseServiceClient();

	private BaseCaseServiceClient() {

	}

	public String uploadBaseCase(final File file, //
			final String scenarioName, ///
			String pricingVersion,
			final IProgressListener progressListener) throws IOException {
		RequestBody requestBody = new MultipartBody.Builder() //
				.setType(MultipartBody.FORM) //
				.addFormDataPart("pricingVersionUUID", pricingVersion) //
				.addFormDataPart("portsVersionUUID", "") //
				.addFormDataPart("vesselsVersionUUID", "") //
				.addFormDataPart("distancesVersionUUID", "") //
				.addFormDataPart("basecase", scenarioName, RequestBody.create(mediaType, file)) //
				.build();

		if (progressListener != null) {
			requestBody = new ProgressRequestBody(requestBody, progressListener);
		}

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseUrlIfAvailable();

		final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
				.url(upstreamURL + BASECASE_UPLOAD_URL) //
				.post(requestBody).build();

		// Check the response
		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				response.body().close();
				if (response.code() == 409) {
					throw new BasecaseServiceLockedException();
				}
				throw new IOException("Unexpected code " + response);
			}

			return response.body().string();
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

		final Request request = UpstreamUrlProvider.INSTANCE.makeRequest().url(String.format("%s%s%s", upstreamURL, BASECASE_DOWNLOAD_URL, uuid)) //
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
		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseUrlIfAvailable();

		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return null;
		}

		if (!UpstreamUrlProvider.INSTANCE.isAvailable()) {
			return null;
		}

		final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
				.url(upstreamURL + BASECASE_CURRENT_URL) //
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

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseUrlIfAvailable();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return null;
		}
		final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
				.url(upstreamURL + BASECASE_CURRENT_URL + "/" + uuid) //
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

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseUrlIfAvailable();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return null;
		}
		final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
				.url(upstreamURL + BASECASE_DOWNLOAD_URL + uuid + "/details") //
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
		final String creationDate = versionObject.getString("creationDate");
		record.creationDate = Instant.parse(creationDate);
		return record;
	}

	public boolean isServiceLocked() {
		return isLocked;
	}

	public boolean isServiceLockedByMe() {
		return lockedByMe;
	}

	public String getLockedBy() {
		return lockedBy;
	}

	public synchronized void updateLockedState() throws IOException {

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseUrlIfAvailable();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return;
		}
		needsLocking = true;
		{
			final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
					.url(upstreamURL + LOCK_STATE_URL) //
					.build();

			try (Response response = httpClient.newCall(request).execute()) {
				if (!response.isSuccessful()) {
					response.body().close();
					if (response.code() == 404) {
						needsLocking = false;
						isLocked = false;
						lockedByMe = false;
						lockedBy = null;
						return;
						// Unsupported API
					}
					throw new IOException(UNEXPECTED_CODE + response);
				}

				LockResult lockResult = new ObjectMapper().readValue(response.body().string(), LockResult.class);

				isLocked = lockResult.isLocked;
				lockedBy = lockResult.lockedBy;
				lockedByMe = lockResult.lockedByMe;
			}
		}
	}

	public boolean canPublish() {
		return lockedByMe || !isLocked;
	}

	public synchronized boolean lock() throws IOException {

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseUrlIfAvailable();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return false;
		}
		{
			final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
					.url(upstreamURL + LOCK_URL) //
					.build();

			try (Response response = httpClient.newCall(request).execute()) {
				if (!response.isSuccessful()) {
					response.body().close();
					if (response.code() == 404) {
						return true;
						// Unsupported API
					}
					throw new IOException(UNEXPECTED_CODE + response);
				}
				return true;
			} finally {
				updateLockedState();
			}
		}
	}

	public synchronized boolean unlock() throws IOException {

		final String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseUrlIfAvailable();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return false;
		}
		{
			final Request request = UpstreamUrlProvider.INSTANCE.makeRequest() //
					.url(upstreamURL + UNLOCK_URL) //
					.build();

			try (Response response = httpClient.newCall(request).execute()) {
				if (!response.isSuccessful()) {
					response.body().close();
					if (response.code() == 404) {
						return true;
						// Unsupported API
					}
					throw new IOException(UNEXPECTED_CODE + response);
				}
				return true;
			} finally {
				updateLockedState();
			}
		}
	}

	public boolean needsLocking() {
		return needsLocking;
	}

}
