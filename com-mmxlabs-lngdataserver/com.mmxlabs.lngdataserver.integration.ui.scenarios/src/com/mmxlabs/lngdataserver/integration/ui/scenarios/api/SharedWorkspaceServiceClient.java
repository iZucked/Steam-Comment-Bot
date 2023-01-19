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

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.eclipse.jdt.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mmxlabs.common.Pair;
import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.hub.common.http.IProgressListener;
import com.mmxlabs.hub.common.http.ProgressHttpEntityWrapper;
import com.mmxlabs.scenario.service.model.util.encryption.DataStreamReencrypter;

public class SharedWorkspaceServiceClient {

	private static final String SCENARIO_UPLOAD_URL = "/scenarios/v1/shared/upload";
	private static final String SCENARIO_DOWNLOAD_URL = "/scenarios/v1/shared/";
	private static final String SCENARIO_DELETE_URL = "/scenarios/v1/shared/";
	private static final String SCENARIO_LIST_URL = "/scenarios/v1/shared/scenarios";
	private static final String SCENARIO_MOVE_URL = "/scenarios/v1/shared/move/";
	private static final String SCENARIO_LAST_MODIFIED_URL = "/scenarios/v1/shared/lastModified";

	public String uploadScenario(final File file, final String path, final IProgressListener progressListener) throws IOException {

		return DataHubServiceProvider.getInstance().doRequest(SCENARIO_UPLOAD_URL, HttpPost::new, request -> {

			final MultipartEntityBuilder formDataBuilder = MultipartEntityBuilder.create();
			formDataBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			formDataBuilder.addTextBody("path", path);
			formDataBuilder.addBinaryBody("scenario", file, ContentType.DEFAULT_BINARY, "scenario.lingo");

			final HttpEntity entity = formDataBuilder.build();

			request.setEntity(new ProgressHttpEntityWrapper(entity, progressListener));
		}, response -> {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				if (responseCode == 409) {
					throw new IOException("Scenario already exists " + path);
				}

				throw new IOException("Unexpected code " + response);
			}

			final String jsonData = EntityUtils.toString(response.getEntity());
			final JSONObject jsonObject = new JSONObject(jsonData);
			return jsonObject.getString("uuid");
		});
	}

	public boolean downloadTo(final String uuid, final File file, final IProgressListener progressListener) throws IOException {

		return DataHubServiceProvider.getInstance().doGetRequestAsBoolean(String.format("%s%s", SCENARIO_DOWNLOAD_URL, uuid), response -> {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				throw new IOException("Unexpected code: " + response);
			}
			final ProgressHttpEntityWrapper w = new ProgressHttpEntityWrapper(response.getEntity(), progressListener);
			try (FileOutputStream out = new FileOutputStream(file)) {
				DataStreamReencrypter.reencryptScenario(w.getContent(), out);
				return true;
			} catch (Exception e) {
				throw new IOException(e);
			}
		});
	}

	public String getBaseCaseDetails(final String uuid) throws IOException {

		return DataHubServiceProvider.getInstance().doRequest(SCENARIO_DOWNLOAD_URL + uuid + "/details", HttpGet::new, response -> {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				throw new IOException("Unexpected code: " + response);
			}
			final String value = EntityUtils.toString(response.getEntity());

			return value;
		});
	}

	public Pair<String, Instant> getScenarios() throws IOException {

		return DataHubServiceProvider.getInstance().doRequest(SCENARIO_LIST_URL, HttpGet::new, response -> {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				throw new IOException("Unexpected code: " + response);
			}
			final String date = HttpClientUtil.getHeaderValue(response, "MMX-LastModified");
			if (date == null) {
				return null;
			}
			final Instant lastModified = Instant.ofEpochSecond(Long.parseLong(date));
			final String jsonData = EntityUtils.toString(response.getEntity());
			return new Pair<>(jsonData, lastModified);
		});
	}

	public void deleteScenario(final String uuid) throws IOException {

		DataHubServiceProvider.getInstance().doDeleteRequest(SCENARIO_DELETE_URL + uuid, response -> {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				throw new IOException("Unexpected code: " + response);
			}
		});
	}

	public @Nullable Instant getLastModified() {
		try {
			return DataHubServiceProvider.getInstance().doGetRequest(SCENARIO_LAST_MODIFIED_URL, response -> {
				final int responseCode = response.getStatusLine().getStatusCode();
				if (HttpClientUtil.isSuccessful(responseCode)) {
					final String date = EntityUtils.toString(response.getEntity());
					return Instant.ofEpochSecond(Long.parseLong(date));
				}
				return null;
			});
		} catch (

		final Exception e) {

		}
		return null;
	}

	public void rename(final String uuid, final String newPath) throws IOException {

		DataHubServiceProvider.getInstance().doPostRequest(SCENARIO_MOVE_URL + uuid, request -> {

			final MultipartEntityBuilder formDataBuilder = MultipartEntityBuilder.create();
			formDataBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			formDataBuilder.addTextBody("path", newPath);

			final HttpEntity entity = formDataBuilder.build();
			request.setEntity(entity);
		}, response -> {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				if (responseCode == 409) {
					throw new IOException("Scenario already exists " + newPath);
				}

				throw new IOException("Unexpected code: " + response);
			}
			return null;
		});
	}

	public List<SharedScenarioRecord> parseScenariosJSONData(final String jsonData) {
		final JSONArray jObject = new JSONArray(jsonData);
		final List<SharedScenarioRecord> l = new LinkedList<>();
		for (int i = 0; i < jObject.length(); ++i) {
			final JSONObject versionObject = jObject.getJSONObject(i);

			final SharedScenarioRecord record = new SharedScenarioRecord();
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
