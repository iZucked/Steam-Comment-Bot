/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lngdataserver.commons.model.RenameRequest;
import com.mmxlabs.lngdataserver.integration.client.pricing.model.Curve;
import com.mmxlabs.lngdataserver.integration.client.pricing.model.Version;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PricingClient {

	private static final String SYNC_VERSION_ENDPOINT = "/pricing/sync/versions/";

	private static final Logger LOGGER = LoggerFactory.getLogger(PricingClient.class);
	private static final OkHttpClient CLIENT = new OkHttpClient();
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	private static final TypeReference<List<Version>> TYPE_VERSIONS_LIST = new TypeReference<List<Version>>() {
	};

	public static List<PricingVersion> getVersions(final String baseUrl) throws IOException {
		return getVersions(baseUrl, null, null);
	}

	public static List<PricingVersion> getVersions(final String baseUrl, final String username, final String password) throws IOException {
		final Request pricingRequest = createRequestBuilder(baseUrl + "/pricing/versions", username, password).build();
		try (Response pricingResponse = CLIENT.newCall(pricingRequest).execute()) {
			if (!pricingResponse.isSuccessful()) {
				final String body = pricingResponse.body().string();
				throw new RuntimeException("Error making request to " + baseUrl + ". Reason " + pricingResponse.message() + " Response body is " + body);
			} else {

				final List<Version> pricingVersions = new ObjectMapper().readValue(pricingResponse.body().byteStream(), TYPE_VERSIONS_LIST);

				return pricingVersions.stream().filter(v -> v.getIdentifier() != null).sorted((v1, v2) -> v2.getCreatedAt().compareTo(v1.getCreatedAt()))
						.map(v -> new PricingVersion(v.getIdentifier(), v.getCreatedAt(), v.isPublished(), v.isCurrent())).collect(Collectors.toList());
			}
		}
	}

	private static Request.Builder createRequestBuilder(final String url, final String username, final String password) {
		final Request.Builder pricingRequestBuilder = new Request.Builder().url(url);
		if (username != null && password != null) {
			pricingRequestBuilder.addHeader("Authorization", Credentials.basic(username, password));
		}
		return pricingRequestBuilder;
	}

	public static boolean saveVersion(final String baseUrl, final Version version) throws IOException {

		final String json = new ObjectMapper().writeValueAsString(version);

		final RequestBody body = RequestBody.create(JSON, json);
		final Request request = new Request.Builder().url(baseUrl + SYNC_VERSION_ENDPOINT).post(body).build();
		try (Response response = CLIENT.newCall(request).execute()) {

			if (!response.isSuccessful()) {
				LOGGER.error("Error publishing version: " + response.message());
				return false;
			}
			return true;
		}
	}

	public static boolean deleteVersion(final String baseUrl, final String version) throws IOException {

		final Request request = new Request.Builder().url(baseUrl + "/pricing/version/" + version).delete().build();
		try (Response response = CLIENT.newCall(request).execute()) {

			if (!response.isSuccessful()) {
				LOGGER.error("Error deleting version: " + response.message());
				return false;
			}
			return true;
		}
	}

	public static boolean renameVersion(final String baseUrl, final String oldVersion, final String newVersion) throws IOException {
		final RenameRequest renameRequest = new RenameRequest();
		renameRequest.setName(newVersion);
		renameRequest.setVersion(oldVersion);

		final String json = new ObjectMapper().writeValueAsString(renameRequest);

		final RequestBody body = RequestBody.create(JSON, json);
		final Request request = new Request.Builder().url(baseUrl + "/pricing/version/rename/").patch(body).build();
		try (Response response = CLIENT.newCall(request).execute()) {

			if (!response.isSuccessful()) {
				LOGGER.error("Error renaming version: " + response.message());
				return false;
			}
			return true;
		}
	}

	public static boolean setCurrentVersion(final String baseUrl, final String version) throws IOException {

		final RequestBody body = RequestBody.create(JSON, "{}");
		final Request request = new Request.Builder().url(baseUrl + "/pricing/version/current/" + version).patch(body).build();
		try (Response response = CLIENT.newCall(request).execute()) {

			if (!response.isSuccessful()) {
				LOGGER.error("Error setting current version: " + response.message());
				return false;
			}
			return true;
		}
	}

	public static CompletableFuture<String> notifyOnNewVersion(final String baseUrl, final String username, final String password) {
		final OkHttpClient longPollingClient = CLIENT.newBuilder().readTimeout(Integer.MAX_VALUE, TimeUnit.MILLISECONDS).build();
		final String url = baseUrl + "/pricing/version_notification";
		LOGGER.debug("Calling url {}", url);
		final CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
			Builder builder = new Request.Builder().url(url);
			if (username != null && !username.isEmpty()) {
				builder = builder.addHeader("Authorization", Credentials.basic(username, password));
			}
			final Request request = builder.build();
			Response response = null;
			try {
				response = longPollingClient.newCall(request).execute();
				final Version newVersion = new ObjectMapper().readValue(response.body().byteStream(), Version.class);
				return newVersion.getIdentifier();
			} catch (final IOException e) {
				LOGGER.error("Error waiting for new version");
				throw new RuntimeException("Error waiting for new version");
			} finally {
				if (response != null) {
					response.close();
				}
			}
		});
		return completableFuture;
	}

	public static List<Curve> getCommodityCurves(final String baseUrl, final String version) throws IOException {
		return getCurves(baseUrl, version, "/commodities");
	}

	public static List<Curve> getFuelCurves(final String baseUrl, final String version) throws IOException {
		return getCurves(baseUrl, version, "/basefuel");
	}

	public static List<Curve> getCurrencyCurves(final String baseUrl, final String version) throws IOException {
		return getCurves(baseUrl, version, "/currencies");
	}

	public static List<Curve> getCharterCurves(final String baseUrl, final String version) throws IOException {
		return getCurves(baseUrl, version, "/charter");
	}

	public static List<Curve> getCurves(final String baseUrl, final String version, final String group) throws IOException {
		final String url = baseUrl + group + "?v=" + version;
		LOGGER.debug("Calling url {}", url);
		final Request request = new Request.Builder().url(url).build();
		try (Response response = CLIENT.newCall(request).execute()) {
			return new ObjectMapper().readValue(response.body().byteStream(), new TypeReference<List<Curve>>() {
			});
		}
	}

	public static <T> T getCurve(final String baseUrl, final String version, final String curve, final Class<T> type) throws IOException {
		final Request request = new Request.Builder().url(baseUrl + "/curves/" + curve.replaceAll("\\.","_") + "?v=" + version).build();
		try (Response response = CLIENT.newCall(request).execute()) {
			if (response.isSuccessful()) {
				final T result = new ObjectMapper().readValue(response.body().byteStream(), type);
				return result;
			} else {
				System.out.println(response.body().string());
				throw new RuntimeException("Error getting curves");
			}
		}
	}

}
