/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.common.Pair;
import com.mmxlabs.hub.common.http.IProgressListener;
import com.mmxlabs.hub.common.http.ProgressRequestBody;
import com.mmxlabs.hub.common.http.ProgressResponseBody;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.preferences.CloudOptimiserPreferenceConstants;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.internal.Activator;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Builder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class CloudOptimisationDataServiceClient {

//	public CloudOptimisationDataServiceClient() {
//		preferences = InstanceScope.INSTANCE.getNode("com.mmxlabs.lingo.optimisation.service.url");
//		optimisationServiceURL = preferences.get("URL", OPTI_CLOUD_BASE_URL);
//	}

	private static final String LIST_SCENARIOS_URL = "/scenarios";
	private static final String LIST_RESULTS_URL = "/results";
	private static final String SCENARIO_RESULT_URL = "/result";
	private static final String JOB_STATUS_URL = "/status";
	private static final String SCENARIO_CLOUD_UPLOAD_URL = "/scenario"; // for localhost - "/scenarios/v1/cloud/opti/upload"
//	private static final String OPTI_CLOUD_BASE_URL = "https://gw.mmxlabs.com"; // "https://wzgy9ex061.execute-api.eu-west-2.amazonaws.com/dev/"
//	private final IEclipsePreferences preferences;
//	private final String optimisationServiceURL;

	private final OkHttpClient httpClient = com.mmxlabs.hub.common.http.HttpClientUtil.basicBuilder() //
			.build();

	private final okhttp3.MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

	private Instant lastSuccessfulAccess = Instant.EPOCH.plusNanos(1);

	public Instant getLastSuccessfulAccess() {
		return this.lastSuccessfulAccess;
	}

	private Request.Builder makeRequestBuilder(String baseUrl, String urlPath) {

		if (baseUrl == null) {
			return null;
		}

		if (!baseUrl.startsWith("https://")) {
			return null;
		}

		Request.Builder builder = new Request.Builder() //
				.url(baseUrl + urlPath);

		return builder;

	}

	private String getGateway() {
		String gateway = Activator.getDefault().getPreferenceStore().getString(CloudOptimiserPreferenceConstants.P_GATEWAY_URL_KEY);
		if (gateway != null && gateway.endsWith("/")) {
			gateway = gateway.substring(0, gateway.length() - 1);
		}
		return gateway;

	}

	public String upload(final File scenario, //
			final String checksum, //
			final String scenarioName, //
			final IProgressListener progressListener) throws IOException {
		Builder builder = new MultipartBody.Builder() //
				.setType(MultipartBody.FORM) //
				.addFormDataPart("sha256", checksum) //
				.addFormDataPart("scenario", scenarioName + ".zip", RequestBody.create(mediaType, scenario)) //
		;
		RequestBody requestBody = builder.build();

		if (progressListener != null) {
			requestBody = new ProgressRequestBody(requestBody, progressListener);
		}

		final Request.Builder requestBuilder = makeRequestBuilder(getGateway(), SCENARIO_CLOUD_UPLOAD_URL);
		if (requestBuilder == null) {
			return null;
		}

		final Request request = requestBuilder //
				.post(requestBody).build();

		// Check the response
		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code " + response);
			}
			return response.body().string();
		}
	}

	public boolean downloadTo(final String jobid, final File file, final IProgressListener progressListener) throws IOException {
		OkHttpClient.Builder clientBuilder = httpClient.newBuilder();
		if (progressListener != null) {
			clientBuilder = clientBuilder.addNetworkInterceptor(new Interceptor() {
				@Override
				public Response intercept(final Chain chain) throws IOException {
					final Response originalResponse = chain.proceed(chain.request());
					return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(), progressListener)).build();
				}
			});
		}
		final OkHttpClient localHttpClient = clientBuilder //
				.build();

		final String requestURL = String.format("%s/%s", SCENARIO_RESULT_URL, jobid);
		final Request.Builder requestBuilder = makeRequestBuilder(getGateway(), requestURL);
		if (requestBuilder == null) {
			return false;
		}

		final Request request = requestBuilder //
				.build();

		try (Response response = localHttpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				response.body().close();
				throw new IOException("Unexpected code: " + response);
			}
			try (BufferedSource bufferedSource = response.body().source()) {
				final BufferedSink bufferedSink = Okio.buffer(Okio.sink(file));
				bufferedSink.writeAll(bufferedSource);
				bufferedSink.close();
				return true;
			}
		}
	}

	public String getJobStatus(final @NonNull String jobid) throws IOException {
		final String requestURL = String.format("%s/%s", JOB_STATUS_URL, jobid);
		final Request.Builder requestBuilder = makeRequestBuilder(getGateway(), requestURL);
		if (requestBuilder == null) {
			return null;
		}

		final Request request = requestBuilder //
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				if (response.code() == 404) {
					return "{ \"status\": \"notfound\" }";// return "Scenario and results are not in s3, please submit again";
				}
				throw new IOException("Unexpected code: " + response);
			}
			return response.body().string();
		}
	}

	public boolean isJobComplete(final @NonNull String jobid) throws IOException {
		final String response = getJobStatus(jobid);
		final ObjectMapper mapper = new ObjectMapper();
		try {
			final JsonNode actualObj = mapper.readTree(response);
			final String state = actualObj.get("status").textValue();
			if (state != null) {
				if ("complete".equalsIgnoreCase(state)) {
					return true;
				}
			} else {
				throw new IOException("Unexpected error!");
			}
		} catch (final IOException e) {
			throw new IOException("Unexpected error: " + e.getMessage());
		}
		return false;
	}

	/**
	 * Lists scenarios in the job queue -OR- Lists scenarios which have been
	 * optimised
	 * 
	 * @return
	 * @throws IOException
	 */
	public Pair<String, Instant> listContents(final boolean listFinished) throws IOException {

		final String url;
		if (listFinished) {
			url = LIST_RESULTS_URL;
		} else {
			url = LIST_SCENARIOS_URL;
		}

		final Request.Builder requestBuilder = makeRequestBuilder(getGateway(), url);
		if (requestBuilder == null) {
			return null;
		}

		final Request request = requestBuilder //
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code: " + response);
			}
			// Only update the last successful access time, only if we list accomplished
			// tasks
			if (listFinished) {
				lastSuccessfulAccess = Instant.now();
			}
			final String jsonData = response.body().string();
			return new Pair<>(jsonData, lastSuccessfulAccess);
		}
	}

	//
	private static final TypeReference<List<CloudOptimisationDataResultRecord>> TYPE_GDR_LIST = new TypeReference<List<CloudOptimisationDataResultRecord>>() {
	};

	public static List<CloudOptimisationDataResultRecord> parseRecordsJSONData(final String jsonData) {
		if (jsonData != null && !jsonData.isEmpty()) {
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
		}
		return new ArrayList<>();
	}

	public static String getJSON(final List<CloudOptimisationDataResultRecord> records) {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		try {
			return mapper.writeValueAsString(records);
		} catch (final JsonParseException e) {
			e.printStackTrace();
		} catch (final JsonMappingException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
