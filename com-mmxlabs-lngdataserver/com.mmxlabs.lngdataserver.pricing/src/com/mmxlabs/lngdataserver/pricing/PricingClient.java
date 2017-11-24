package com.mmxlabs.lngdataserver.pricing;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lngdataserver.pricing.model.Curve;
import com.mmxlabs.lngdataserver.pricing.model.PublishRequest;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PricingClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(PricingClient.class);
	private static final OkHttpClient CLIENT = new OkHttpClient();
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	public static List<PricingVersion> getVersions(String baseUrl) throws IOException {
		Request pricingRequest = new Request.Builder().url(baseUrl + "/pricing/versions").build();
		Response pricingResponse = CLIENT.newCall(pricingRequest).execute();

		List<com.mmxlabs.lngdataserver.pricing.model.Version> pricingVersions = new ObjectMapper().readValue(pricingResponse.body().byteStream(),
				new TypeReference<List<com.mmxlabs.lngdataserver.pricing.model.Version>>() {
				});

		return pricingVersions.stream().sorted((v1, v2) -> v2.getCreatedAt().compareTo(v1.getCreatedAt())).map(v -> new PricingVersion(v.getIdentifier(), v.getCreatedAt(), v.isPublished()))
				.collect(Collectors.toList());
	}

	public static void publishVersion(String version, String baseUrl) throws IOException {
		
		PublishRequest publishRequest = new PublishRequest();
		publishRequest.setVersion(version);
		publishRequest.setUpstreamUrl("http://localhost:8096/pricing/sync/versions");
		
		String json = new ObjectMapper().writeValueAsString(publishRequest);

		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(baseUrl + "/pricing/sync/publish").post(body).build();
		Response response = CLIENT.newCall(request).execute();
		
		if(!response.isSuccessful()) {
			LOGGER.error("Error publishing version: " + response.message());
		}
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
				com.mmxlabs.lngdataserver.pricing.model.Version newVersion = new ObjectMapper().readValue(response.body().byteStream(), com.mmxlabs.lngdataserver.pricing.model.Version.class);
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
