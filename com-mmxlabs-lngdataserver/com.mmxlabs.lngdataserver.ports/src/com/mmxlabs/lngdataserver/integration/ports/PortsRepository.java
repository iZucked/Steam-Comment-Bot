package com.mmxlabs.lngdataserver.integration.ports;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.eclipse.jface.preference.IPreferenceStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.commons.impl.AbstractDataRepository;
import com.mmxlabs.lngdataserver.port.api.PortApi;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.lngdataservice.ports.model.PublishRequest;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
 

public class PortsRepository extends AbstractDataRepository {
	private static final Logger LOG = LoggerFactory.getLogger(PortsRepository.class);

	private static final String SYNC_VERSION_ENDPOINT = "/ports/sync/versions/";
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	
	private PortApi portsApi = new PortApi();
	private String backendUrl;

	public PortsRepository(IPreferenceStore preferenceStore, String localUrl) {
		super(preferenceStore, localUrl);
	}

	public boolean isReady() {
		if (backendUrl != null) {
			return true;
		} else if (BackEndUrlProvider.INSTANCE.isAvailable()) {
			backendUrl = BackEndUrlProvider.INSTANCE.getUrl();
			portsApi.getApiClient().setBasePath(backendUrl);
			return true;
		} else {
			return false;
		}
	}

	public List<DataVersion> getVersions() {
		ensureReady();
		try {
			return portsApi.fetchVersionsUsingGET().stream().map(v -> {
				final LocalDateTime createdAt =  LocalDateTime.now();//LocalDateTime.ofInstant(Instant.ofEpochMilli(v.getCreatedAt().getNano() / 1000L), ZoneId.of("UTC"));
				return new DataVersion(v.getIdentifier(), createdAt, /* v.isPublished() */ false);
			}).collect(Collectors.toList());
		} catch (final Exception e) {
			LOG.error("Error fetching ports versions" + e.getMessage());
			throw new RuntimeException("Error fetching ports versions", e);
		}
	}

	public IPortsProvider getPortsProvider(String versionTag) {
		try {
			return new DefaultPortsProvider(versionTag, portsApi.fetchAllUsingGET());
		} catch (Exception e) {
			// Pass
			System.out.println(e.getMessage());
		}
		return null;
	}

	private void ensureReady() {
		if (!isReady()) {
			throw new IllegalStateException("Ports back-end not ready yet");
		}
	}

	@Override
	public void syncUpstreamVersion(String version) throws Exception {
		// Pull down the version data
		final Request pullRequest = new Request.Builder().url(upstreamUrl + SYNC_VERSION_ENDPOINT + version).get().build();
		final Response pullResponse = portsApi.getApiClient().getHttpClient().newCall(pullRequest).execute();
		final String json = pullResponse.body().string();

		// Post the data to local repo
		final RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
		final Request postRequest = new Request.Builder().url(backendUrl + SYNC_VERSION_ENDPOINT).post(body).build();
		final Response postResponse = portsApi.getApiClient().getHttpClient().newCall(postRequest).execute();
	}

	@Override
	public void publishVersion(String version) throws Exception {
		PublishRequest publishRequest = new PublishRequest();
		publishRequest.setVersion(version);
		publishRequest.setUpstreamUrl(upstreamUrl + SYNC_VERSION_ENDPOINT);

		String json = new ObjectMapper().writeValueAsString(publishRequest);

		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(backendUrl + "/ports/sync/publish").post(body).build();
		Response response = portsApi.getApiClient().getHttpClient().newCall(request).execute();

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
		return CompletableFuture.completedFuture(null);
	}

	@Override
	protected CompletableFuture<String> waitForNewUpstreamVersion() {
		return CompletableFuture.completedFuture(null);
	}

	@Override
	protected void newUpstreamURL(String upstreamURL) {

	}
}
