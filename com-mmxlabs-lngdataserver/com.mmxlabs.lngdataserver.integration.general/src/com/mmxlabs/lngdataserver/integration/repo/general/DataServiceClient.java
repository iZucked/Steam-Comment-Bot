/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.repo.general;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.common.Pair;
import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.hub.common.http.IProgressListener;
import com.mmxlabs.hub.common.http.ProgressHttpEntityWrapper;
import com.mmxlabs.scenario.service.model.util.encryption.EncryptionUtils;

public class DataServiceClient {
	private static final Logger LOG = LoggerFactory.getLogger(DataServiceClient.class);

	private static final TypeReference<List<GeneralDataRecord>> TYPE_LIST = new TypeReference<List<GeneralDataRecord>>() {
	};

	public void upload(final TypeRecord typeRecord, final String json, final IProgressListener progressListener) throws IOException {
		final HttpPost request = new HttpPost();
		final var httpClient = DataHubServiceProvider.getInstance().makeRequest(typeRecord.getUploadURL(), request);
		if (httpClient == null) {
			return;
		}

		final StringEntity requestEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
		request.setEntity(new ProgressHttpEntityWrapper(requestEntity, progressListener));

		// Check the response
		try (var response = httpClient.execute(request)) {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				if (responseCode == 409) {
					// Data version already exists, we didn't need to upload again....
					// throw new IOException("Data already exists");
					return;
				}
				throw new IOException("Unexpected code " + response);
			}
		}
	}

	public void downloadTo(final TypeRecord typeRecord, final String uuid, final File file, final IProgressListener progressListener) throws Exception {

		final HttpGet request = new HttpGet();
		final var httpClient = DataHubServiceProvider.getInstance().makeRequest(typeRecord.getDownloadURL(uuid), request);
		if (httpClient == null) {
			return;
		}
		try (var response = httpClient.execute(request)) {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				throw new IOException("Unexpected code: " + response);
			}
			final ProgressHttpEntityWrapper w = new ProgressHttpEntityWrapper(response.getEntity(), progressListener);
			try (FileOutputStream fos = new FileOutputStream(file)) {
				EncryptionUtils.encrypt(fos, os -> {
					typeRecord.writeHeader(os);
					w.writeTo(os);
				});
			}
		}
	}

	public @Nullable Pair<String, Instant> getRecords(final TypeRecord typeRecord) throws IOException {

		if (!DataHubServiceProvider.getInstance().isOnlineAndLoggedIn()) {
			return null;
		}

		final HttpGet request = new HttpGet();
		final var httpClient = DataHubServiceProvider.getInstance().makeRequest(typeRecord.getListURL(), request);
		if (httpClient == null) {
			return null;
		}

		try (var response = httpClient.execute(request)) {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				throw new IOException("Unexpected code: " + responseCode);
			}
			final Instant lastModified = Instant.now();
			final String jsonData = EntityUtils.toString(response.getEntity());
			return new Pair<>(jsonData, lastModified);
		}
	}

	public void deleteData(final TypeRecord typeRecord, final String uuid) throws IOException {

		final HttpDelete request = new HttpDelete();
		final var httpClient = DataHubServiceProvider.getInstance().makeRequest(typeRecord.getDeleteURL(uuid), request);
		if (httpClient == null) {
			return;
		}

		try (var response = httpClient.execute(request)) {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				throw new IOException("Unexpected code: " + response);
			}
		}
	}

	public String getCurrentVersion(final TypeRecord typeRecord) throws IOException {

		if (typeRecord.getCurrentURL() == null) {
			return null;
		}

		final HttpGet request = new HttpGet();
		final var httpClient = DataHubServiceProvider.getInstance().makeRequest(typeRecord.getCurrentURL(), request);
		if (httpClient == null) {
			return null;
		}

		try (var response = httpClient.execute(request)) {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				// 404 Not found is a valid response if there is no current pricing version
				if (responseCode != 404) {
					throw new IOException("Unexpected code: " + response);
				}
				return "";
			}
			return EntityUtils.toString(response.getEntity());
		}
	}

	public @Nullable List<GeneralDataRecord> parseRecordsJSONData(final TypeRecord typeRecord, final String jsonData) {
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
}
