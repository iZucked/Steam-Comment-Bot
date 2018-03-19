package com.mmxlabs.lngdataserver.integration.ui.scenarios.api;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.eclipse.ui.internal.handlers.DisplayHelpHandler;

import com.mmxlabs.lngdataserver.server.UpstreamUrlProvider;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

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

	public static void main(String[] args) throws IOException {
		BaseCaseServiceClient c = new BaseCaseServiceClient();
		String o = c.uploadBaseCase(new File("c://temp//test-data.txt"));
		System.out.println(o);

	}

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
			if (!response.isSuccessful())
				throw new IOException("Unexpected code " + response);

			String responseStr = response.body().string();
			return responseStr;
		}
	}

	public boolean downloadTo(String uuid, File file) throws IOException {
		OkHttpClient httpClient = new OkHttpClient.Builder() //
				.build();

		String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();

		Request request = new Request.Builder() //
				.url(String.format("%s%s/%s", upstreamURL, BASECASE_DOWNLOAD_URL, uuid)) //
				.header("Authorization", Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword()))//
				.build();

		Response response = httpClient.newCall(request).execute();
		if (!response.isSuccessful()) {
			throw new IOException("Unexpected code: " + response);
		}
		try (BufferedSource bufferedSource = response.body().source()) {
			BufferedSink bufferedSink = Okio.buffer(Okio.sink(file));
			bufferedSink.writeAll(bufferedSource);
			bufferedSink.close();
		}
		// TODO: Is it a valid .lingo file?
		Date date = response.headers().getDate("Last-Modified");
		return true;
	}

	public String getCurrentBaseCase() throws IOException {
		OkHttpClient httpClient = new OkHttpClient.Builder() //
				.build();

		String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();

		Request request = new Request.Builder() //
				.url(upstreamURL + BASECASE_CURRENT_URL) //
				.header("Authorization", Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword()))//
				.build();

		Response response = httpClient.newCall(request).execute();
		if (!response.isSuccessful()) {
			throw new IOException("Unexpected code: " + response);
		}
		String value = response.body().string();

		return value;
	}

	private ScheduledThreadPoolExecutor pollTaskExecutor;
	private ScheduledFuture<?> task;

	public void start(File baseCaseFolder, Consumer<File> callback) {
		this.baseCaseFolder = baseCaseFolder;
		pollTaskExecutor = new ScheduledThreadPoolExecutor(1);
		boolean[] firstRun = { true };
		task = pollTaskExecutor.scheduleAtFixedRate(() -> {
			try {
				// Connect to service.
				// Does the current match known current?
				String uuid = getCurrentBaseCase();
				{
					File target = new File(baseCaseFolder.getAbsolutePath() + File.separator + uuid + ".lingo");
					if (!target.exists()) {
						try {
							downloadTo(uuid, target);
							callback.accept(target);
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
							callback.accept(target);
						}
						firstRun[0] = false;
					}
				}
			} catch (Exception e) {

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
