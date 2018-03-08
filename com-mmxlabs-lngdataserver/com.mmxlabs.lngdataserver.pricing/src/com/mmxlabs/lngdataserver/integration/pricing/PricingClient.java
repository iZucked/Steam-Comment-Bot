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
import com.mmxlabs.lngdataserver.integration.client.pricing.model.Curve;
import com.mmxlabs.lngdataserver.integration.client.pricing.model.Version;
import com.mmxlabs.lngdataserver.commons.model.PublishRequest;
import com.mmxlabs.lngdataserver.commons.model.RenameRequest;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PricingClient {

	private static final String SYNC_VERSION_ENDPOINT = "/pricing/sync/versions/";

	private static final Logger LOGGER = LoggerFactory.getLogger(PricingClient.class);
	private static final OkHttpClient CLIENT = new OkHttpClient();
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	public static List<PricingVersion> getVersions(String baseUrl) throws IOException {
		Request pricingRequest = new Request.Builder().url(baseUrl + "/pricing/versions").build();
		Response pricingResponse = CLIENT.newCall(pricingRequest).execute();

		List<Version> pricingVersions = new ObjectMapper().readValue(pricingResponse.body().byteStream(),
				new TypeReference<List<Version>>() {
				});

		return pricingVersions.stream().filter(v -> v.getIdentifier() != null).sorted((v1, v2) -> v2.getCreatedAt().compareTo(v1.getCreatedAt()))
				.map(v -> new PricingVersion(v.getIdentifier(), v.getCreatedAt(), v.isPublished(), v.isCurrent())).collect(Collectors.toList());
	}

	public static void publishVersion(String version, String baseUrl, String upstreamUrl) throws IOException {

		PublishRequest publishRequest = new PublishRequest();
		publishRequest.setVersion(version);
		publishRequest.setUpstreamUrl(upstreamUrl + SYNC_VERSION_ENDPOINT);

		String json = new ObjectMapper().writeValueAsString(publishRequest);

		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(baseUrl + "/pricing/sync/publish").post(body).build();
		Response response = CLIENT.newCall(request).execute();

		if (!response.isSuccessful()) {
			LOGGER.error("Error publishing version: " + response.message());
		}
	}

	public static void getUpstreamVersion(String baseUrl, String upstreamUrl, String version) throws IOException {

		// Pull down the version data
		final Request pullRequest = new Request.Builder().url(upstreamUrl + SYNC_VERSION_ENDPOINT + version).get().build();
		final Response pullResponse = CLIENT.newCall(pullRequest).execute();
		final String json = pullResponse.body().string();

		// Post the data to local repo
		final RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
		final Request postRequest = new Request.Builder().url(baseUrl + SYNC_VERSION_ENDPOINT).post(body).build();
		final Response postResponse = CLIENT.newCall(postRequest).execute();
	}

	public static boolean saveVersion(String baseUrl, Version version) throws IOException {

		String json = new ObjectMapper().writeValueAsString(version);

		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(baseUrl + SYNC_VERSION_ENDPOINT).post(body).build();
		Response response = CLIENT.newCall(request).execute();

		if (!response.isSuccessful()) {
			LOGGER.error("Error publishing version: " + response.message());
			return false;
		}
		return true;
	}
	
	public static boolean deleteVersion(String baseUrl, String version) throws IOException {

		Request request = new Request.Builder().url(baseUrl + "/pricing/version/" + version).delete().build();
		Response response = CLIENT.newCall(request).execute();

		if (!response.isSuccessful()) {
			LOGGER.error("Error deleting version: " + response.message());
			return false;
		}
		return true;
	}
	
	public static boolean renameVersion(String baseUrl, String oldVersion, String newVersion) throws IOException {
		RenameRequest renameRequest = new RenameRequest();
		renameRequest.setName(newVersion);
		renameRequest.setVersion(oldVersion);
		
		String json = new ObjectMapper().writeValueAsString(renameRequest);

		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(baseUrl + "/pricing/version/rename/").patch(body).build();
		Response response = CLIENT.newCall(request).execute();

		if (!response.isSuccessful()) {
			LOGGER.error("Error renaming version: " + response.message());
			return false;
		}
		return true;
	}
	
	public static boolean setCurrentVersion(String baseUrl, String version) throws IOException {

		RequestBody body = RequestBody.create(JSON, "{}");
		Request request = new Request.Builder().url(baseUrl + "/pricing/version/current/" + version).patch(body).build();
		Response response = CLIENT.newCall(request).execute();

		if (!response.isSuccessful()) {
			LOGGER.error("Error setting current version: " + response.message());
			return false;
		}
		return true;
	}

	public static CompletableFuture<String> notifyOnNewVersion(String baseUrl) {
		OkHttpClient longPollingClient = CLIENT.newBuilder().readTimeout(Integer.MAX_VALUE, TimeUnit.MILLISECONDS).build();
		String url = baseUrl + "/pricing/version_notification";
		LOGGER.debug("Calling url {}", url);
		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
			Request request = new Request.Builder().url(url).build();
			Response response;
			try {
				response = longPollingClient.newCall(request).execute();
				Version newVersion = new ObjectMapper().readValue(response.body().byteStream(), Version.class);
				return newVersion.getIdentifier();
			} catch (IOException e) {
				LOGGER.error("Error waiting for new version");
				throw new RuntimeException("Error waiting for new version");
			}
		});
		return completableFuture;
	}

	public static List<Curve> getCommodityCurves(String baseUrl, String version) throws IOException {
		return getCurves(baseUrl, version, "/commodities");
	}

	public static List<Curve> getFuelCurves(String baseUrl, String version) throws IOException {
		return getCurves(baseUrl, version, "/basefuel");
	}

	public static List<Curve> getCurrencyCurves(String baseUrl, String version) throws IOException {
		return getCurves(baseUrl, version, "/currencies");
	}

	public static List<Curve> getCharterCurves(String baseUrl, String version) throws IOException {
		return getCurves(baseUrl, version, "/charter");
	}

	public static List<Curve> getCurves(String baseUrl, String version, String group) throws IOException {
		String url = baseUrl + group + "?v=" + version;
		LOGGER.debug("Calling url {}", url);
		Request request = new Request.Builder().url(url).build();
		Response response = CLIENT.newCall(request).execute();
		return new ObjectMapper().readValue(response.body().byteStream(), new TypeReference<List<Curve>>() {
		});
	}

	public static <T> T getCurve(String baseUrl, String version, String curve, Class<T> type) throws IOException {
		Request request = new Request.Builder().url(baseUrl + "/curves/" + curve + "?v=" + version).build();
		Response response = CLIENT.newCall(request).execute();
		T result = new ObjectMapper().readValue(response.body().byteStream(), type);
		return result;
	}

}
