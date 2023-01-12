/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.eclipse.jdt.annotation.Nullable;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.hub.common.http.IProgressListener;
import com.mmxlabs.hub.common.http.ProgressHttpEntityWrapper;
import com.mmxlabs.scenario.service.model.util.encryption.DataStreamReencrypter;

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

	private boolean needsLocking = false;
	private boolean isLocked = false;
	private boolean lockedByMe = false;
	private String lockedBy = null;

	public static final BaseCaseServiceClient INSTANCE = new BaseCaseServiceClient();

	private BaseCaseServiceClient() {

	}

	public String uploadBaseCase(final File file, //
			final String scenarioName, ///
			final @Nullable String notes, String pricingVersion, final IProgressListener progressListener) throws IOException {

		HttpPost request = new HttpPost();
		final var httpClient = DataHubServiceProvider.getInstance().makeRequest(BASECASE_UPLOAD_URL, request);
		if (httpClient == null) {
			return null;
		}

		final MultipartEntityBuilder formDataBuilder = MultipartEntityBuilder.create();
		formDataBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		formDataBuilder.addTextBody("pricingVersionUUID", pricingVersion);
		formDataBuilder.addTextBody("portsVersionUUID", "");
		formDataBuilder.addTextBody("vesselsVersionUUID", "");
		formDataBuilder.addTextBody("distancesVersionUUID", "");
		formDataBuilder.addBinaryBody("basecase", file, ContentType.DEFAULT_BINARY, scenarioName);

		if (notes != null) {
			formDataBuilder.addTextBody("notes", notes);
		}

		final HttpEntity entity = formDataBuilder.build();

		request.setEntity(new ProgressHttpEntityWrapper(entity, progressListener));

		// Check the response
		try (var response = httpClient.execute(request)) {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				if (responseCode == 409) {
					throw new BasecaseServiceLockedException();
				}
				throw new IOException("Unexpected code " + responseCode);
			}

			return EntityUtils.toString(response.getEntity());
		}
	}

	public void uploadBaseCaseArchive(final File file, //
			final String uuid, ///
			final IProgressListener progressListener) throws IOException {

		HttpPost request = new HttpPost();
		final var httpClient = DataHubServiceProvider.getInstance().makeRequest(BASECASE_UPLOAD_ARCHIVE_URL, request);
		if (httpClient == null) {
			return;
		}

		final MultipartEntityBuilder formDataBuilder = MultipartEntityBuilder.create();
		formDataBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		formDataBuilder.addTextBody("uuid", uuid);
		formDataBuilder.addBinaryBody("archive", file, ContentType.DEFAULT_BINARY, uuid + ".zip");

		final HttpEntity entity = formDataBuilder.build();

		request.setEntity(new ProgressHttpEntityWrapper(entity, progressListener));

		// Check the response
		try (var response = httpClient.execute(request)) {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				if (responseCode == 404) {
					// 404: Endpoint not defined - old server version
					return;
				} else if (responseCode == 405) {
					// POST return a 405 instead of 404
					// 405: Endpoint not defined - old server version
					return;
				}
				if (responseCode == 503) {
					// 503: Service unavailable - not configured on server, so do not report an
					// error for this code.
					return;

				}
				throw new IOException("Unexpected code " + response);
			}
		}
	}

	public boolean downloadTo(final String uuid, final File file, final IProgressListener progressListener) throws Exception {

		HttpGet request = new HttpGet();
		final var httpClient = DataHubServiceProvider.getInstance().makeRequest(String.format("%s%s", BASECASE_DOWNLOAD_URL, uuid), request);
		if (httpClient == null) {
			return false;
		}

		try (var response = httpClient.execute(request)) {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				throw new IOException(UNEXPECTED_CODE + responseCode);
			}

			ProgressHttpEntityWrapper w = new ProgressHttpEntityWrapper(response.getEntity(), progressListener);
			try (FileOutputStream out = new FileOutputStream(file)) {
				DataStreamReencrypter.reencryptScenario(w.getContent(), out);
				return true;
			}
		}
	}

	public String getCurrentBaseCase() throws IOException {

		HttpGet request = new HttpGet();
		final var httpClient = DataHubServiceProvider.getInstance().makeRequest(BASECASE_CURRENT_URL, request);
		if (httpClient == null) {
			return null;
		}

		try (var response = httpClient.execute(request)) {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				// 404 Not found is a valid response if there is no current basecase
				if (responseCode != 404) {
					throw new IOException(UNEXPECTED_CODE + responseCode);
				}
				return "";
			}
			return EntityUtils.toString(response.getEntity());
		}
	}

	public String setCurrentBaseCase(final String uuid) throws IOException {

		if (!DataHubServiceProvider.getInstance().isOnlineAndLoggedIn()) {
			return null;
		}

		HttpGet request = new HttpGet();
		final var httpClient = DataHubServiceProvider.getInstance().makeRequest(BASECASE_CURRENT_URL + "/" + uuid, request);
		if (httpClient == null) {
			return null;
		}

		try (var response = httpClient.execute(request)) {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				throw new IOException(UNEXPECTED_CODE + response);
			}
			return EntityUtils.toString(response.getEntity());
		}
	}

	public String getBaseCaseDetails(final String uuid) throws IOException {

		HttpGet request = new HttpGet();
		final var httpClient = DataHubServiceProvider.getInstance().makeRequest(BASECASE_DOWNLOAD_URL + uuid + "/details", request);
		if (httpClient == null) {
			return null;
		}

		try (var response = httpClient.execute(request)) {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				throw new IOException(UNEXPECTED_CODE + responseCode);
			}
			return EntityUtils.toString(response.getEntity());
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
		HttpGet request = new HttpGet();
		final var httpClient = DataHubServiceProvider.getInstance().makeRequest(LOCK_STATE_URL, request);
		if (httpClient == null) {
			return;
		}

		needsLocking = true;
		try (var response = httpClient.execute(request)) {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				if (responseCode == 404) {
					needsLocking = false;
					isLocked = false;
					lockedByMe = false;
					lockedBy = null;
					return;
					// Unsupported API
				}
				throw new IOException(UNEXPECTED_CODE + response);
			}

			LockResult lockResult = new ObjectMapper().readValue(response.getEntity().getContent(), LockResult.class);

			isLocked = lockResult.isLocked;
			lockedBy = lockResult.lockedBy;
			lockedByMe = lockResult.lockedByMe;
		}
	}

	public boolean canPublish() {
		return lockedByMe || !isLocked;
	}

	public synchronized boolean lock() throws IOException {

		if (!DataHubServiceProvider.getInstance().isOnlineAndLoggedIn()) {
			return false;
		}

		HttpGet request = new HttpGet();
		final var httpClient = DataHubServiceProvider.getInstance().makeRequest(LOCK_URL, request);
		if (httpClient == null) {
			return false;
		}

		try (var response = httpClient.execute(request)) {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				if (responseCode == 404) {
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

	public synchronized boolean unlock() throws IOException {
		if (!DataHubServiceProvider.getInstance().isOnlineAndLoggedIn()) {
			return false;
		}

		HttpGet request = new HttpGet();

		final var httpClient = DataHubServiceProvider.getInstance().makeRequest(UNLOCK_URL, request);
		if (httpClient == null) {
			return false;
		}

		try (var response = httpClient.execute(request)) {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				if (responseCode == 404) {
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

	public boolean needsLocking() {
		return needsLocking;
	}

	public synchronized boolean forceUnlock() throws IOException {

		HttpGet request = new HttpGet();
		final var httpClient = DataHubServiceProvider.getInstance().makeRequest(FORCE_UNLOCK_URL, request);
		if (httpClient == null) {
			return false;
		}

		try (var response = httpClient.execute(request)) {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				if (responseCode == 404) {
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
