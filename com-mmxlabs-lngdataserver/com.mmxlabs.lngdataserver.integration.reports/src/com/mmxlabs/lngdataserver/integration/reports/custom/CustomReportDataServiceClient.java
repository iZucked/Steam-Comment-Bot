/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;

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
		final HttpPost request = new HttpPost();
		final var httpClient = DataHubServiceProvider.getInstance().makeRequest(requestURL, request);
		if (httpClient == null) {
			return null;
		}

		final MultipartEntityBuilder formDataBuilder = MultipartEntityBuilder.create();
		formDataBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		formDataBuilder.addTextBody("uuid", uuid);
		formDataBuilder.addTextBody("contentType", contentType);
		formDataBuilder.addBinaryBody("report", file, ContentType.DEFAULT_BINARY, "data.json");

		final HttpEntity entity = formDataBuilder.build();
		request.setEntity(new ProgressHttpEntityWrapper(entity, progressListener));

		// Check the response
		try (var response = httpClient.execute(request)) {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				if (responseCode == 409) {
					throw new IOException("Data already exists " + "/" + uuid);
				}

				throw new IOException("Unexpected code " + response);
			}
			return uuid;
		}
	}

	public boolean downloadTo(final String uuid, final File file, final IProgressListener progressListener) throws IOException {

		HttpGet request = new HttpGet();
		final String requestURL = String.format("%s/%s", SCENARIO_ENDPOINT_URL, uuid);
		final var httpClient = DataHubServiceProvider.getInstance().makeRequest(requestURL, request);
		if (httpClient == null) {
			return false;
		}

		try (var response = httpClient.execute(request)) {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				throw new IOException("Unexpected code: " + responseCode);
			}
			ProgressHttpEntityWrapper w = new ProgressHttpEntityWrapper(response.getEntity(), progressListener);
			try (FileOutputStream fos = new FileOutputStream(file)) {
				w.writeTo(fos);
				return true;
			}
		}
	}

	public Pair<String, Instant> getRecords() throws IOException {

		HttpGet request = new HttpGet();
		final var httpClient = DataHubServiceProvider.getInstance().makeRequest(SCENARIO_ENDPOINT_URL, request);
		if (httpClient == null) {
			return null;
		}

		try (var response = httpClient.execute(request)) {
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
		}
	}

	public void deleteData(final String uuid) throws IOException {

		final String requestURL = String.format("%s/%s", SCENARIO_DELETE_URL, uuid);
		HttpDelete request = new HttpDelete();
		final var httpClient = DataHubServiceProvider.getInstance().makeRequest(requestURL, request);
		if (httpClient == null) {
			return;
		}

		try (var response = httpClient.execute(request)) {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				throw new IOException("Unexpected code: " + responseCode);
			}
		}
	}

	public Instant getLastModified() {

		HttpGet request = new HttpGet();
		final var httpClient = DataHubServiceProvider.getInstance().makeRequest(SCENARIO_LAST_MODIFIED_URL, request);
		if (httpClient == null) {
			return null;
		}

		try (var response = httpClient.execute(request)) {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (HttpClientUtil.isSuccessful(responseCode)) {
				final String date = EntityUtils.toString(response.getEntity());
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
