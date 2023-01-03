/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.api;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.rcp.common.RunnerHelper;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class ReportsServiceClient {

	private static final String REPORT_GET_URL = "/scenarios/v1/reports/";

	private File baseCaseFolder;

	private final OkHttpClient httpClient = HttpClientUtil.basicBuilder() //
			.build();

	private final okhttp3.MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

	public @Nullable String uploadReportData(String type, String version, String content, String uuid, String uploadURL, String fileExtension) throws IOException {

		RequestBody requestBody = new MultipartBody.Builder() //
				.setType(MultipartBody.FORM) //
				.addFormDataPart("version", version) //
				.addFormDataPart("report", type + fileExtension, RequestBody.create(mediaType, content))//
				.build();

		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(uploadURL + "/" + uuid + "/" + type);
		if (requestBuilder == null) {
			return null;
		}

		Request request = requestBuilder //
				.post(requestBody).build();

		// Check the response
		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				if (response.code() == 404) {
					// 404: Endpoint not defined - old server version
					return "404: Endpoint not defined - old server version";
				} else if (response.code() == 405) {
					// POST return a 405 instead of 404
					// 405: Endpoint not defined - old server version
					return "405: Endpoint not defined - old server version";
				}
				if (response.code() == 503) {
					// 503: Service unavailable - not configured on server, so do not report an
					// error for this code.
					return "503: Service unavailable - not configured on server, so do not report an error for this code";
				}
				throw new IOException("Unexpected code " + response);
			}

			String responseStr = response.body().string();
			return responseStr;
		}
	}

	public boolean downloadTo(String uuid, File file, BiConsumer<File, Instant> callback) throws IOException {

		final String requestURL = String.format("%s/%s", REPORT_GET_URL, uuid);
		final Request.Builder requestBuilder = DataHubServiceProvider.getInstance().makeRequestBuilder(requestURL);
		if (requestBuilder == null) {
			return false;
		}

		Request request = requestBuilder //
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code: " + response);
			}
			try (BufferedSource bufferedSource = response.body().source()) {
				try (BufferedSink bufferedSink = Okio.buffer(Okio.sink(file))) {
					bufferedSink.writeAll(bufferedSource);
				}
			}
			// TODO: Is it a valid .lingo file?
			String date = response.headers().get("MMX-CreationDate");
			if (date != null) {
				Instant creationDate = Instant.ofEpochSecond(Long.parseLong(date));
				callback.accept(file, creationDate);
				return true;
			}

			// , Long.toString(baseCaseRecord.getCreationDate().getEpochSecond())");
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
