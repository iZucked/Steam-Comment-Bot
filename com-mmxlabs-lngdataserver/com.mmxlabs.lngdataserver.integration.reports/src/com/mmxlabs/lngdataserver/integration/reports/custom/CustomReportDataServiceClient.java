/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.custom;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.eclipse.jdt.annotation.Nullable;

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

public class CustomReportDataServiceClient {

	private static final String SCENARIO_UPLOAD_URL = "/reports/v1/team/custom/upload";
	private static final String SCENARIO_DELETE_URL = "/reports/v1/team/custom/remove";
	private static final String SCENARIO_ENDPOINT_URL = "/reports/v1/team/custom";
	private static final String SCENARIO_LAST_MODIFIED_URL = "/reports/v1/team/custom/mod/lastModified";

	public String upload(final String uuid, final String contentType, final File file, final IProgressListener progressListener) throws IOException {

		final String requestURL = String.format("%s/%s", SCENARIO_UPLOAD_URL, uuid);
		return DataHubServiceProvider.getInstance().doPostRequest(requestURL, request -> {

			final MultipartEntityBuilder formDataBuilder = MultipartEntityBuilder.create();
			formDataBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

			formDataBuilder.addTextBody("uuid", uuid);
			formDataBuilder.addTextBody("contentType", contentType);
			formDataBuilder.addBinaryBody("report", file, ContentType.DEFAULT_BINARY, "data.json");

			final HttpEntity entity = formDataBuilder.build();
			request.setEntity(new ProgressHttpEntityWrapper(entity, progressListener));
		}, response -> {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				if (responseCode == 409) {
					throw new IOException("Data already exists " + "/" + uuid);
				}

				throw new IOException("Unexpected code " + response);
			}
			return uuid;
		});
	}

	public boolean downloadTo(final String uuid, final File file, final IProgressListener progressListener) throws IOException {

		final String requestURL = String.format("%s/%s", SCENARIO_ENDPOINT_URL, uuid);
		return DataHubServiceProvider.getInstance().doGetRequestAsBoolean(requestURL, response -> {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				throw new IOException("Unexpected code: " + responseCode);
			}
			final ProgressHttpEntityWrapper w = new ProgressHttpEntityWrapper(response.getEntity(), progressListener);
			try (FileOutputStream fos = new FileOutputStream(file)) {
				w.writeTo(fos);
				return true;
			}
		});
	}

	public Pair<String, Instant> getRecords() throws IOException {

		return DataHubServiceProvider.getInstance().doGetRequest(SCENARIO_ENDPOINT_URL, response -> {
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

	public void deleteData(final String uuid) throws IOException {

		final String requestURL = String.format("%s/%s", SCENARIO_DELETE_URL, uuid);
		DataHubServiceProvider.getInstance().doDeleteRequest(requestURL, response -> {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				throw new IOException("Unexpected code: " + responseCode);
			}
		});
	}

	public @Nullable Instant getLastModified() {

		try {
			return DataHubServiceProvider.getInstance().doGetRequest(SCENARIO_LAST_MODIFIED_URL, response -> {
				final int responseCode = response.getStatusLine().getStatusCode();
				if (HttpClientUtil.isSuccessful(responseCode)) {
					final String date = EntityUtils.toString(response.getEntity());
					final Instant lastModified = Instant.ofEpochSecond(Long.parseLong(date));
					return lastModified;
				}
				return null;
			});
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//
	private static final TypeReference<List<CustomReportDataRecord>> TYPE_GDR_LIST = new TypeReference<List<CustomReportDataRecord>>() {
	};

	public static List<CustomReportDataRecord> parseRecordsJSONData(final String jsonData) {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		try {
			return mapper.readValue(jsonData, TYPE_GDR_LIST);
		} catch (final JsonParseException e) {
			e.printStackTrace();
		} catch (final JsonMappingException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

}
