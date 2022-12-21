/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.rcp.common.RunnerHelper;

public class ReportsServiceClient {

	private static final String REPORT_GET_URL = "/scenarios/v1/reports/";

	private File baseCaseFolder;

	public @Nullable String uploadReportData(String type, String version, String content, String uuid, String uploadURL, String fileExtension) throws IOException {

		HttpPost request = new HttpPost();
		final var httpClient = DataHubServiceProvider.getInstance().makeRequest(uploadURL + "/" + uuid + "/" + type, request);
		if (httpClient == null) {
			return null;
		}

		final MultipartEntityBuilder formDataBuilder = MultipartEntityBuilder.create();
		formDataBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		formDataBuilder.addTextBody("version", version);
		formDataBuilder.addBinaryBody("report", content.getBytes(StandardCharsets.UTF_8), ContentType.DEFAULT_BINARY, type + fileExtension);

		final HttpEntity entity = formDataBuilder.build();

		request.setEntity(entity);

		// Check the response
		try (var response = httpClient.execute(request)) {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				if (responseCode == 404) {
					// 404: Endpoint not defined - old server version
					return "404: Endpoint not defined - old server version";
				} else if (responseCode == 405) {
					// POST return a 405 instead of 404
					// 405: Endpoint not defined - old server version
					return "405: Endpoint not defined - old server version";
				}
				if (responseCode == 503) {
					// 503: Service unavailable - not configured on server, so do not report an
					// error for this code.
					return "503: Service unavailable - not configured on server, so do not report an error for this code";
				}
				throw new IOException("Unexpected code " + response);
			}

			return EntityUtils.toString(response.getEntity());
		}
	}

	public boolean downloadTo(String uuid, File file, BiConsumer<File, Instant> callback) throws IOException {

		final String requestURL = String.format("%s/%s", REPORT_GET_URL, uuid);

		final HttpGet request = new HttpGet();
		final var httpClient = DataHubServiceProvider.getInstance().makeRequest(requestURL, request);
		if (httpClient == null) {
			return false;
		}

		try (var response = httpClient.execute(request)) {
			final int responseCode = response.getStatusLine().getStatusCode();
			if (!HttpClientUtil.isSuccessful(responseCode)) {
				throw new IOException("Unexpected code: " + response);
			}
			try (FileOutputStream fos = new FileOutputStream(file)) {
				response.getEntity().writeTo(fos);
			}
			// TODO: Is it a valid .lingo file?
			final String date = HttpClientUtil.getHeaderValue(response, "MMX-CreationDate");
			if (date != null) {
				Instant creationDate = Instant.ofEpochSecond(Long.parseLong(date));
				callback.accept(file, creationDate);
				return true;
			}

			return false;
		}
	}

	private ScheduledThreadPoolExecutor pollTaskExecutor;
	private ScheduledFuture<?> task;

	public void start(File baseCaseFolder, BiConsumer<File, Instant> callback) {
		this.baseCaseFolder = baseCaseFolder;
		pollTaskExecutor = new ScheduledThreadPoolExecutor(1);
		boolean[] firstRun = { true };
		task = pollTaskExecutor.scheduleAtFixedRate(() -> {
			try {
				// Connect to service.
				// Does the current match known current?
				String uuid = BaseCaseServiceClient.INSTANCE.getCurrentBaseCase();
				{
					File target = new File(baseCaseFolder.getAbsolutePath() + File.separator + uuid + ".lingo");
					if (!target.exists()) {
						try {
							downloadTo(uuid, target, callback);
							// callback.accept(target, date);
							firstRun[0] = false;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// updateScenarioModel(target);
						//
						// cleanup();

						// Download scenario to folder

						// Update internal references

						// Update data model

						// Delete old data?

					} else {
						if (firstRun[0]) {
							// callback.accept(target);
						}
						firstRun[0] = false;
					}
				}
			} catch (Exception e) {
				int ii = 0;
			}

		}, 0, 5, TimeUnit.MINUTES);

	}

	private void cleanup() {
		// TODO Auto-generated method stub

	}

	private void updateScenarioModel(File target) {
		// TODO Auto-generated method stub
		RunnerHelper.asyncExec(() -> {
			// Update scenario model
		});
	}

	public void stop() {
		task.cancel(true);

		pollTaskExecutor.shutdown();
	}

	public File getBaseCaseFolder() {
		return baseCaseFolder;
	}

	public void setBaseCaseFolder(File baseCaseFolder) {
		this.baseCaseFolder = baseCaseFolder;
	}
}
