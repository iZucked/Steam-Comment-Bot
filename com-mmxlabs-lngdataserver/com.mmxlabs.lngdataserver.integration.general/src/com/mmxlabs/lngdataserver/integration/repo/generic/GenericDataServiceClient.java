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

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
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
import com.mmxlabs.hub.common.http.ProgressHttpEntityWrapper;
import com.mmxlabs.scenario.service.model.util.encryption.DataStreamReencrypter;

public class GenericDataServiceClient {

	private static final String SCENARIO_UPLOAD_URL = "/data/v1/upload";
	private static final String SCENARIO_DOWNLOAD_URL = "/data/v1/get";
	private static final String SCENARIO_DELETE_URL = "/data/v1/delete";
	private static final String SCENARIO_LIST_URL = "/data/v1/list";

	private static final String SCENARIO_LAST_MODIFIED_URL = "/data/v1/lastModified";

	public String upload(final String type, final String uuid, final String contentType, final File file, final IProgressListener progressListener) throws IOException {

		final String requestURL = String.format("%s/%s/%s", SCENARIO_UPLOAD_URL, type, uuid);
		return DataHubServiceProvider.getInstance().doRequest(requestURL, HttpPost::new, request -> {

			final MultipartEntityBuilder formDataBuilder = MultipartEntityBuilder.create();
			formDataBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			formDataBuilder.addBinaryBody("data", file, ContentType.DEFAULT_BINARY, "data.json");
			formDataBuilder.addTextBody("contentType", contentType);
			final HttpEntity requestEntity = formDataBuilder.build();

			request.setEntity(new ProgressHttpEntityWrapper(requestEntity, progressListener));
		}, response -> {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				if (responseCode == 409) {
					throw new IOException("Data already exists " + type + "/" + uuid);
				}

				throw new IOException("Unexpected code " + response);
			}

			final String jsonData = EntityUtils.toString(response.getEntity());
			final JSONObject jsonObject = new JSONObject(jsonData);
			final String uuidString = jsonObject.getString("uuid");
			return uuidString;
		});
	}

	public boolean downloadTo(final String type, final String uuid, final File file, final IProgressListener progressListener) throws Exception {

		final String requestURL = String.format("%s/%s/%s", SCENARIO_DOWNLOAD_URL, type, uuid);
		return DataHubServiceProvider.getInstance().doRequestAsBoolean(requestURL, HttpGet::new, response -> {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				throw new IOException("Unexpected code: " + response);
			}
			ProgressHttpEntityWrapper w = new ProgressHttpEntityWrapper(response.getEntity(), progressListener);
			try (FileOutputStream out = new FileOutputStream(file)) {
				DataStreamReencrypter.reencryptData(w.getContent(), out);
				return true;
			} catch (Exception e) {
				throw new IOException(e);
			}
		});

	}

	public Pair<String, Instant> getRecords(final Collection<String> types) throws IOException {

		if (types.isEmpty()) {
			return null;
		}

		final String typesList = String.join(",", types);
		final String requestURL = String.format("%s/%s", SCENARIO_LIST_URL, typesList);

		return DataHubServiceProvider.getInstance().doGetRequest(requestURL, response -> {
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

	public void deleteData(final String type, final String uuid) throws IOException {

		final String requestURL = String.format("%s/%s/%s", SCENARIO_DELETE_URL, type, uuid);
		DataHubServiceProvider.getInstance().doDeleteRequest(requestURL, response -> {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				throw new IOException("Unexpected code: " + responseCode);
			}
		});
	}

	public Instant getLastModified() {
		try {
			return DataHubServiceProvider.getInstance().doGetRequest(SCENARIO_LAST_MODIFIED_URL, response -> {
				final int responseCode = response.getStatusLine().getStatusCode();
				if (!HttpClientUtil.isSuccessful(responseCode)) {
					final String date = EntityUtils.toString(response.getEntity());
					final Instant lastModified = Instant.ofEpochSecond(Long.parseLong(date));
					return lastModified;
				}
				return null;
			});
		} catch (IOException e) {
			// TODO: Log exception?
			return null;
		}
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
