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
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.annotation.Nullable;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.hub.common.http.IProgressListener;
import com.mmxlabs.hub.common.http.ProgressHttpEntityWrapper;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.debug.BaseCaseDebugContants;
import com.mmxlabs.scenario.service.model.util.encryption.DataStreamReencrypter;

public class BaseCaseServiceClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseCaseServiceClient.class);

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
			final @Nullable String notes,
			final String pricingVersion, final String paperVersion,
			final IProgressListener progressListener) throws IOException {

		return DataHubServiceProvider.getInstance().doRequest(BASECASE_UPLOAD_URL, HttpPost::new, request -> {

			final MultipartEntityBuilder formDataBuilder = MultipartEntityBuilder.create();
			formDataBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			formDataBuilder.addTextBody("pricingVersionUUID", pricingVersion);
			formDataBuilder.addTextBody("paperVersionUUID", paperVersion);
			formDataBuilder.addTextBody("portsVersionUUID", "");
			formDataBuilder.addTextBody("vesselsVersionUUID", "");
			formDataBuilder.addTextBody("distancesVersionUUID", "");
			formDataBuilder.addBinaryBody("basecase", file, ContentType.DEFAULT_BINARY, scenarioName);

			if (notes != null) {
				formDataBuilder.addTextBody("notes", notes);
			}

			final HttpEntity entity = formDataBuilder.build();

			request.setEntity(new ProgressHttpEntityWrapper(entity, progressListener));
		}, response -> {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				if (responseCode == 409) {
					throw new BasecaseServiceLockedException();
				}
				throw new IOException("Unexpected code " + responseCode);
			}

			return EntityUtils.toString(response.getEntity());
		});
	}

	public void uploadBaseCaseArchive(final File file, //
			final String uuid, ///
			final IProgressListener progressListener) throws IOException {

		DataHubServiceProvider.getInstance().doPostRequest(BASECASE_UPLOAD_ARCHIVE_URL, request -> {

			final MultipartEntityBuilder formDataBuilder = MultipartEntityBuilder.create();
			formDataBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			formDataBuilder.addTextBody("uuid", uuid);
			formDataBuilder.addBinaryBody("archive", file, ContentType.DEFAULT_BINARY, uuid + ".zip");

			final HttpEntity entity = formDataBuilder.build();

			request.setEntity(new ProgressHttpEntityWrapper(entity, progressListener));
		}, response -> {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				if (responseCode == 404) {
					// 404: Endpoint not defined - old server version
					return null;
				} else if (responseCode == 405) {
					// POST return a 405 instead of 404
					// 405: Endpoint not defined - old server version
					return null;
				}
				if (responseCode == 503) {
					// 503: Service unavailable - not configured on server, so do not report an
					// error for this code.
					return null;

				}
				throw new IOException("Unexpected code " + response);
			}
			return null;
		});
	}

	public boolean downloadTo(final String uuid, final File file, final IProgressListener progressListener) throws Exception {

		return DataHubServiceProvider.getInstance().doGetRequestAsBoolean(String.format("%s%s", BASECASE_DOWNLOAD_URL, uuid), response -> {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				if (Platform.getDebugBoolean(BaseCaseDebugContants.DEBUG_DOWNLOAD)) {
					LOGGER.trace("Bad status code downloading base case " + responseCode);
				}
				throw new IOException(UNEXPECTED_CODE + responseCode);
			}

			ProgressHttpEntityWrapper w = new ProgressHttpEntityWrapper(response.getEntity(), progressListener);
			try (FileOutputStream out = new FileOutputStream(file)) {
				DataStreamReencrypter.reencryptScenario(w.getContent(), out);
				return true;
			} catch (Exception e) {
				if (Platform.getDebugBoolean(BaseCaseDebugContants.DEBUG_DOWNLOAD)) {
					LOGGER.trace("Exception saving base case " + responseCode);
				}
				throw new IOException(e);
			}
		});
	}

	public String getCurrentBaseCase() throws IOException {

		return DataHubServiceProvider.getInstance().doRequest(BASECASE_CURRENT_URL, HttpGet::new, response -> {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				// 404 Not found is a valid response if there is no current basecase
				if (responseCode != 404) {
					throw new IOException(UNEXPECTED_CODE + responseCode);
				}
				return "";
			}
			return EntityUtils.toString(response.getEntity());
		});
	}

	public String setCurrentBaseCase(final String uuid) throws IOException {

		return DataHubServiceProvider.getInstance().doRequest(BASECASE_CURRENT_URL + "/" + uuid, HttpGet::new, response -> {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				throw new IOException(UNEXPECTED_CODE + response);
			}
			return EntityUtils.toString(response.getEntity());
		});
	}

	public String getBaseCaseDetails(final String uuid) throws IOException {

		return DataHubServiceProvider.getInstance().doRequest(BASECASE_DOWNLOAD_URL + uuid + "/details", HttpGet::new, response -> {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				throw new IOException(UNEXPECTED_CODE + responseCode);
			}
			return EntityUtils.toString(response.getEntity());
		});
	}

	public BaseCaseRecord parseScenariosJSONData(final String jsonData) {
		final JSONObject versionObject = new JSONObject(jsonData);
		final BaseCaseRecord bcRecord = new BaseCaseRecord();
		bcRecord.uuid = versionObject.getString("uuid");
		bcRecord.creator = versionObject.getString("creator");
		if (!versionObject.isNull("originalName")) {
			bcRecord.originalName = versionObject.getString("originalName");
		} else {
			bcRecord.originalName = bcRecord.uuid;
		}
		final String creationDate = versionObject.getString("creationDate");
		bcRecord.creationDate = Instant.parse(creationDate);
		return bcRecord;
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

		needsLocking = true;
		DataHubServiceProvider.getInstance().doGetRequest(LOCK_STATE_URL, response -> {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				if (responseCode == 404) {
					needsLocking = false;
					isLocked = false;
					lockedByMe = false;
					lockedBy = null;
					return null;
					// Unsupported API
				}
				throw new IOException(UNEXPECTED_CODE + response);
			}

			final LockResult lockResult = new ObjectMapper().readValue(response.getEntity().getContent(), LockResult.class);

			isLocked = lockResult.isLocked;
			lockedBy = lockResult.lockedBy;
			lockedByMe = lockResult.lockedByMe;

			return null;
		});
	}

	public boolean canPublish() {
		return lockedByMe || !isLocked;
	}

	public synchronized boolean lock() throws IOException {

		try {
			return DataHubServiceProvider.getInstance().doGetRequestAsBoolean(LOCK_URL, response -> {
				final int responseCode = response.getStatusLine().getStatusCode();
				if (!HttpClientUtil.isSuccessful(responseCode)) {
					if (responseCode == 404) {
						return true;
						// Unsupported API
					}
					throw new IOException(UNEXPECTED_CODE + response);
				}
				return true;
			});
		} finally {
			updateLockedState();
		}
	}

	public synchronized boolean unlock() throws IOException {
		try {
			return DataHubServiceProvider.getInstance().doGetRequestAsBoolean(UNLOCK_URL, response -> {
				final int responseCode = response.getStatusLine().getStatusCode();
				if (!HttpClientUtil.isSuccessful(responseCode)) {
					if (responseCode == 404) {
						return true;
						// Unsupported API
					}
					throw new IOException(UNEXPECTED_CODE + response);
				}
				return true;
			});
		} finally {
			updateLockedState();
		}
	}

	public boolean needsLocking() {
		return needsLocking;
	}

	public synchronized boolean forceUnlock() throws IOException {
		try {
			return DataHubServiceProvider.getInstance().doGetRequestAsBoolean(FORCE_UNLOCK_URL, response -> {
				final int responseCode = response.getStatusLine().getStatusCode();
				if (!HttpClientUtil.isSuccessful(responseCode)) {
					if (responseCode == 404) {
						return true;
						// Unsupported API
					}
					throw new IOException(UNEXPECTED_CODE + response);
				}
				return true;
			});
		} finally {
			updateLockedState();
		}
	}
}
