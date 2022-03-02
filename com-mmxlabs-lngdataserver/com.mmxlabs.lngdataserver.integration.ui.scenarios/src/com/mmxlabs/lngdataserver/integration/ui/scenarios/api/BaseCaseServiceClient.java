/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.hub.common.http.IProgressListener;
import com.mmxlabs.hub.common.http.ProgressRequestBody;
import com.mmxlabs.hub.common.http.ProgressResponseBody;
import com.mmxlabs.scenario.service.model.util.encryption.DataStreamReencrypter;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Builder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSource;

public class BaseCaseServiceClient {

	private static final String UNEXPECTED_CODE = "Unexpected code: ";
	private static final String BASECASE_UPLOAD_URL = "/scenarios/v1/basecase/upload";
	private static final String BASECASE_UPLOAD_ARCHIVE_URL = "/scenarios/v1/basecase/uploadarchive";
	private static final String BASECASE_DOWNLOAD_URL = "/scenarios/v1/basecase/";
	private static final String BASECASE_CURRENT_URL = "/scenarios/v1/basecase/current";

	private static final String LOCK_CHECK_URL = "/scenarios/v1/basecase/islocked";
	private static final String LOCK_BY_URL = "/scenarios/v1/basecase/lockedby";
	private static final String LOCK_URL = "/scenarios/v1/basecase/lock";
	private static final String LOCK_STATE_URL = "/scenarios/v1/basecase/lockState";
	private static final String UNLOCK_URL = "/scenarios/v1/basecase/unlock";
	private static final String FORCE_UNLOCK_URL = "/scenarios/v1/basecase/forceunlock";
	
	private static final String SCENARIO_CLOUD_UPLOAD_URL = "/scenarios/v1/cloud/opti/upload";

	private final OkHttpClient httpClient = HttpClientUtil.basicBuilder()//
			.build();

	private final okhttp3.MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

	private boolean needsLocking = false;
	private boolean isLocked = false;
	private boolean lockedByMe = false;
	private String lockedBy = null;

	public static final BaseCaseServiceClient INSTANCE = new BaseCaseServiceClient();

	private BaseCaseServiceClient() {

	}

	public String uploadBaseCase(final File file, //
			final String scenarioName, ///
			final String notes, String pricingVersion, final IProgressListener progressListener) throws IOException {
		Builder builder = new MultipartBody.Builder() //
				.setType(MultipartBody.FORM) //
				.addFormDataPart("pricingVersionUUID", pricingVersion) //
				.addFormDataPart("portsVersionUUID", "") //
				.addFormDataPart("vesselsVersionUUID", "") //
				.addFormDataPart("distancesVersionUUID", "") //
				.addFormDataPart("basecase", scenarioName, RequestBody.create(mediaType, file)) //
		;
		if (notes != null) {
			builder.addFormDataPart("notes", notes);
		}
		RequestBody requestBody = builder.build();

		if (progressListener != null) {
			requestBody = new ProgressRequestBody(requestBody, progressListener);
		}

		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(BASECASE_UPLOAD_URL);
		if (requestBuilder == null) {
			return null;
		}

		final Request request = requestBuilder //
				.post(requestBody).build();

		// Check the response
		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				if (response.code() == 409) {
					throw new BasecaseServiceLockedException();
				}
				throw new IOException("Unexpected code " + response);
			}

			return response.body().string();
		}
	}

	public void uploadBaseCaseArchive(final File file, //
			final String uuid, ///
			final IProgressListener progressListener) throws IOException {
		RequestBody requestBody = new MultipartBody.Builder() //
				.setType(MultipartBody.FORM) //
				.addFormDataPart("uuid", uuid) //
				.addFormDataPart("archive", uuid + ".zip", RequestBody.create(mediaType, file)) //
				.build();

		if (progressListener != null) {
			requestBody = new ProgressRequestBody(requestBody, progressListener);
		}
		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(BASECASE_UPLOAD_ARCHIVE_URL);
		if (requestBuilder == null) {
			return;
		}

		final Request request = requestBuilder //
				.post(requestBody) //
				.build();

		// Check the response
		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				if (response.code() == 404) {
					// 404: Endpoint not defined - old server version
					return;
				} else if (response.code() == 405) {
					// POST return a 405 instead of 404
					// 405: Endpoint not defined - old server version
					return;
				}
				if (response.code() == 503) {
					// 503: Service unavailable - not configured on server, so do not report an
					// error for this code.
					return;

				}
				throw new IOException("Unexpected code " + response);
			}
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

		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(String.format("%s%s", BASECASE_DOWNLOAD_URL, uuid));
		if (requestBuilder == null) {
			return false;
		}

		final Request request = requestBuilder //
				.build();

		try (Response response = localHttpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException(UNEXPECTED_CODE + response);
			}
			try (FileOutputStream out = new FileOutputStream(file)) {
				try (BufferedSource bufferedSource = response.body().source()) {
					DataStreamReencrypter.reencryptScenario(bufferedSource.inputStream(), out);
					return true;
				}
			}
		}
	}

	public String getCurrentBaseCase() throws IOException {

		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(BASECASE_CURRENT_URL);
		if (requestBuilder == null) {
			return null;
		}

		final Request request = requestBuilder //
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
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

		if (!DataHubServiceProvider.getInstance().isOnlineAndLoggedIn()) {
			return null;
		}

		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(BASECASE_CURRENT_URL + "/" + uuid);
		if (requestBuilder == null) {
			return null;
		}

		final Request request = requestBuilder //
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException(UNEXPECTED_CODE + response);
			}
			return response.body().string();
		}
	}

	public String getBaseCaseDetails(final String uuid) throws IOException {

		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(BASECASE_DOWNLOAD_URL + uuid + "/details");
		if (requestBuilder == null) {
			return null;
		}

		final Request request = requestBuilder //
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
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

		if (!DataHubServiceProvider.getInstance().isOnlineAndLoggedIn()) {
			return;
		}

		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(LOCK_STATE_URL);
		if (requestBuilder == null) {
			return;
		}

		needsLocking = true;
		{
			final Request request = requestBuilder //
					.build();

			try (Response response = httpClient.newCall(request).execute()) {
				if (!response.isSuccessful()) {
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

		if (!DataHubServiceProvider.getInstance().isOnlineAndLoggedIn()) {
			return false;
		}

		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(LOCK_URL);
		if (requestBuilder == null) {
			return false;
		}

		{
			final Request request = requestBuilder //
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
		if (!DataHubServiceProvider.getInstance().isOnlineAndLoggedIn()) {
			return false;
		}

		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(UNLOCK_URL);
		if (requestBuilder == null) {
			return false;
		}

		{
			final Request request = requestBuilder //
					.build();

			try (Response response = httpClient.newCall(request).execute()) {
				if (!response.isSuccessful()) {
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

	public synchronized boolean forceUnlock() throws IOException {

		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(FORCE_UNLOCK_URL);
		if (requestBuilder == null) {
			return false;
		}

		{
			final Request request = requestBuilder //
					.build();

			try (Response response = httpClient.newCall(request).execute()) {
				if (!response.isSuccessful()) {
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

}
