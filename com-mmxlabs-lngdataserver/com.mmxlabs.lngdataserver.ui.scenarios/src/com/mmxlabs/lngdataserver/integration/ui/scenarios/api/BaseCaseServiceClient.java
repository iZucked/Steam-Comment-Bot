/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.api;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.common.util.TriConsumer;
import com.mmxlabs.lngdataserver.server.UpstreamUrlProvider;
import com.mmxlabs.rcp.common.RunnerHelper;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class BaseCaseServiceClient {

	private static final String BASECASE_UPLOAD_URL = "/scenarios/v1/basecase/upload";
	private static final String BASECASE_DOWNLOAD_URL = "/scenarios/v1/basecase/";
	private static final String BASECASE_CURRENT_URL = "/scenarios/v1/basecase/current";

	private File baseCaseFolder;

	public String uploadBaseCase(File file) throws IOException {

		okhttp3.MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
		RequestBody requestBody = new MultipartBody.Builder() //
				.setType(MultipartBody.FORM) //
				.addFormDataPart("basecase", "basecase.lingo", RequestBody.create(mediaType, file))//
				.build();

		String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();

		Request request = new Request.Builder() //
				.url(upstreamURL + BASECASE_UPLOAD_URL) //
				.header("Authorization", Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword()))//
				.post(requestBody).build();

		OkHttpClient httpClient = new OkHttpClient.Builder() //
				.build();

		// Check the response
		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				response.body().close();
				throw new IOException("Unexpected code " + response);
			}

			String responseStr = response.body().string();
			return responseStr;
		}
	}

	public boolean downloadTo(String uuid, File file, TriConsumer<File, String, Instant> callback) throws IOException {
		OkHttpClient httpClient = new OkHttpClient.Builder() //
				.build();

		String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();

		Request request = new Request.Builder() //
				.url(String.format("%s%s%s", upstreamURL, BASECASE_DOWNLOAD_URL, uuid)) //
				.header("Authorization", Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword()))//
				.build();

		Response response = httpClient.newCall(request).execute();
		if (!response.isSuccessful()) {
			response.body().close();
			throw new IOException("Unexpected code: " + response);
		}
		try (BufferedSource bufferedSource = response.body().source()) {
			BufferedSink bufferedSink = Okio.buffer(Okio.sink(file));
			bufferedSink.writeAll(bufferedSource);
			bufferedSink.close();
		}
		// TODO: Is it a valid .lingo file?
		String date = response.headers().get("MMX-CreationDate");
		if (date != null) {
			Instant creationDate = Instant.ofEpochSecond(Long.parseLong(date));
			callback.accept(file, uuid, creationDate);
			return true;
		}

		// , Long.toString(baseCaseRecord.getCreationDate().getEpochSecond())");
		return false;
	}

	public static String getCurrentBaseCase() throws IOException {
		String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();

		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return null;
		}

		if (!UpstreamUrlProvider.INSTANCE.isAvailable()) {
			return null;
		}

		OkHttpClient httpClient = new OkHttpClient.Builder() //
				.build();

		Request request = new Request.Builder() //
				.url(upstreamURL + BASECASE_CURRENT_URL) //
				.header("Authorization", Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword()))//
				.build();

		Response response = httpClient.newCall(request).execute();
		if (!response.isSuccessful()) {
			response.body().close();
			throw new IOException("Unexpected code: " + response);
		}
		String value = response.body().string();

		return value;
	}

	public static String setCurrentBaseCase(String uuid) throws IOException {
		OkHttpClient httpClient = new OkHttpClient.Builder() //
				.build();

		String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return null;
		}
		Request request = new Request.Builder() //
				.url(upstreamURL + BASECASE_CURRENT_URL + "/" + uuid) //
				.header("Authorization", Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword()))//
				.build();

		Response response = httpClient.newCall(request).execute();
		if (!response.isSuccessful()) {
			response.body().close();
			throw new IOException("Unexpected code: " + response);
		}
		String value = response.body().string();

		return value;
	}

	public static String getBaseCaseDetails(String uuid) throws IOException {
		OkHttpClient httpClient = new OkHttpClient.Builder() //
				.build();

		String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();
		if (upstreamURL == null || upstreamURL.isEmpty()) {
			return null;
		}
		Request request = new Request.Builder() //
				.url(upstreamURL + BASECASE_DOWNLOAD_URL + uuid + "/details") //
				.header("Authorization", Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword()))//
				.build();

		Response response = httpClient.newCall(request).execute();
		if (!response.isSuccessful()) {
			response.body().close();
			throw new IOException("Unexpected code: " + response);
		}
		String value = response.body().string();

		return value;
	}

	private ScheduledThreadPoolExecutor pollTaskExecutor;
	private ScheduledFuture<?> task;

	public void start(File baseCaseFolder, TriConsumer<File, String, Instant> callback) {
		this.baseCaseFolder = baseCaseFolder;
		pollTaskExecutor = new ScheduledThreadPoolExecutor(1);
		boolean[] firstRun = { true };
		task = pollTaskExecutor.scheduleAtFixedRate(() -> {
			try {
				// Connect to service.
				// Does the current match known current?
				String uuid = getCurrentBaseCase();
				if (uuid == null) {
					return;
				}
				{
					File target = new File(baseCaseFolder.getAbsolutePath() + File.separator + uuid + ".lingo");
					if (!target.exists()) {
						final Job background = new Job("Downloading basecase") {

							@Override
							public IStatus run(final IProgressMonitor monitor) {
								// Having a method called "main" in the stacktrace stops SpringBoot throwing an exception in the logging framework
								return main(monitor);
							}

							public IStatus main(final IProgressMonitor monitor) {
								monitor.beginTask("Downloading latest basecase...", IProgressMonitor.UNKNOWN);
								try {

									downloadTo(uuid, target, callback);

									firstRun[0] = false;
									monitor.worked(1);
								} catch (final Exception e) {
								} finally {
									monitor.done();
								}
								return Status.OK_STATUS;
							}
						};
						background.setSystem(false);
						background.setUser(true);
						background.setPriority(Job.LONG);

						background.schedule();
						System.out.println("Downloading basecase in background...");

						// callback.accept(target, date);
						// } catch (IOException e) {
						// // TODO Auto-generated catch block
						// e.printStackTrace();
						// }

						// updateScenarioModel(target);
						//
						// cleanup();

						// Download scenario to folder

						// Update internal references

						// Update data model

						// Delete old data?

					} else {
						if (firstRun[0]) {
							String details = getBaseCaseDetails(uuid);

							ObjectMapper mapper = new ObjectMapper();
							try {
								JsonNode actualObj = mapper.readTree(details);
								String creationDate = actualObj.get("creationDate").textValue();
								Instant instant = Instant.parse(creationDate);

								callback.accept(target, uuid, instant);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						firstRun[0] = false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				int ii = 0;

			}

		}, 60, 30, TimeUnit.SECONDS);

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
