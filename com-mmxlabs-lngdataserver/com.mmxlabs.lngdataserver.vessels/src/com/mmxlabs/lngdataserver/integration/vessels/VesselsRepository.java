package com.mmxlabs.lngdataserver.integration.vessels;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.eclipse.jface.preference.IPreferenceStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.commons.impl.AbstractDataRepository;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.lngdataservice.client.vessel.model.PublishRequest;
import com.mmxlabs.lngdataservice.client.vessel.api.VesselsApi;
import com.mmxlabs.lngdataservice.client.vessel.model.Version;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VesselsRepository extends AbstractDataRepository {
	private static final Logger LOG = LoggerFactory.getLogger(VesselsRepository.class);
	private VesselsApi vesselsApi = new VesselsApi();
	private static final OkHttpClient CLIENT = new OkHttpClient();

	private static final String SYNC_VERSION_ENDPOINT = "/vessels/sync/versions/";
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	public VesselsRepository(IPreferenceStore preferenceStore, String localUrl) {
		super(preferenceStore, localUrl);
	}

	public boolean isReady() {
		if (backendUrl != null) {
			return true;
		} else if (BackEndUrlProvider.INSTANCE.isAvailable()) {
			backendUrl = BackEndUrlProvider.INSTANCE.getUrl();
			vesselsApi.getApiClient().setBasePath(backendUrl);
			return true;
		} else {
			return false;
		}
	}

	public List<DataVersion> getVersions() {
		ensureReady();
		try {
			return vesselsApi.getVersionsUsingGET().stream().map(v -> {
				final LocalDateTime createdAt = LocalDateTime.now();// LocalDateTime.ofInstant(Instant.ofEpochMilli(v.getCreatedAt().getNano() / 1000L), ZoneId.of("UTC"));
				return new DataVersion(v.getIdentifier(), createdAt, /* v.isPublished() */ false);
			}).collect(Collectors.toList());
		} catch (Exception e) {
			LOG.error("Error fetching vessels versions" + e.getMessage());
			throw new RuntimeException("Error fetching vessels versions", e);
		}
	}

	public IVesselsProvider getVesselsProvider(String versionTag) {
		try {
			return new DefaultVesselsProvider(versionTag, vesselsApi.getVesselsUsingGET());
		} catch (Exception e) {
			// Pass
		}
		return null;
	}

	private void ensureReady() {
		if (!isReady()) {
			throw new IllegalStateException("Pricing back-end not ready yet");
		}
	}

	@Override
	public void syncUpstreamVersion(String version) throws Exception {
		// Pull down the version data
		final Request pullRequest = new Request.Builder().url(upstreamUrl + SYNC_VERSION_ENDPOINT + version).get().build();
		final Response pullResponse = CLIENT.newCall(pullRequest).execute();
		final String json = pullResponse.body().string();

		// Post the data to local repo
		final RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
		final Request postRequest = new Request.Builder().url(backendUrl + SYNC_VERSION_ENDPOINT).post(body).build();
		final Response postResponse = CLIENT.newCall(postRequest).execute();
	}

	@Override
	public void publishVersion(String version) throws Exception {
		PublishRequest publishRequest = new PublishRequest();
		publishRequest.setVersion(version);
		publishRequest.setUpstreamUrl(upstreamUrl + SYNC_VERSION_ENDPOINT);

		String json = new ObjectMapper().writeValueAsString(publishRequest);

		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(backendUrl + "/vessels/sync/publish").post(body).build();
		Response response = CLIENT.newCall(request).execute();

		if (!response.isSuccessful()) {
			LOG.error("Error publishing version: " + response.message());
		}
	}

	@Override
	public List<DataVersion> updateAvailable() throws Exception {
		return Collections.emptyList();
	}

	@Override
	protected CompletableFuture<String> waitForNewLocalVersion() {
		return notifyOnNewVersion(backendUrl);
	}

	@Override
	protected boolean canWaitForNewLocalVersion() {
		return true;
	}

	@Override
	protected boolean canWaitForNewUpstreamVersion() {
		return true;
	}

	@Override
	protected CompletableFuture<String> waitForNewUpstreamVersion() {
		return notifyOnNewVersion(upstreamUrl);
	}

	@Override
	protected void newUpstreamURL(String upstreamURL) {

	}

	public static CompletableFuture<String> notifyOnNewVersion(String baseUrl) {

		OkHttpClient longPollingClient = CLIENT.newBuilder().readTimeout(Integer.MAX_VALUE, TimeUnit.MILLISECONDS).build();
		String url = baseUrl + "/vessels/version_notification";
		LOG.debug("Calling url {}", url);
		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
			Request request = new Request.Builder().url(url).build();
			Response response;
			try {
				response = longPollingClient.newCall(request).execute();
				Version newVersion = new ObjectMapper().readValue(response.body().byteStream(), Version.class);
				return newVersion.getIdentifier();
			} catch (IOException e) {
				LOG.error("Error waiting for new version");
				throw new RuntimeException("Error waiting for new version");
			}
		});
		return completableFuture;
	}
}
