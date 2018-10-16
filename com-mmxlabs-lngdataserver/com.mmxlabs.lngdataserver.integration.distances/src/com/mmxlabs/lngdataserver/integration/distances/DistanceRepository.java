/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.distances;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.http.auth.AuthenticationException;
import org.eclipse.jdt.annotation.Nullable;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.commons.impl.AbstractDataRepository;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.lngdataserver.server.UpstreamUrlProvider;
import com.mmxlabs.lngdataservice.client.distances.ApiClient;
import com.mmxlabs.lngdataservice.client.distances.ApiException;
import com.mmxlabs.lngdataservice.client.distances.api.DistancesApi;
import com.mmxlabs.lngdataservice.client.distances.model.Version;

import okhttp3.Request;
import okhttp3.Response;

/**
 * This implementation is not thread-safe.
 * 
 * @author Robert Erdin
 */
public class DistanceRepository extends AbstractDataRepository {

	public static DistanceRepository INSTANCE = new DistanceRepository();

	private static final Logger LOG = LoggerFactory.getLogger(DistanceRepository.class);

	private static final String SYNC_ENDPOINT = "/distances/sync/publish";
	private static final String SYNC_VERSION_ENDPOINT = "/distances/sync/versions/";

	private final DistancesApi localApi;
	private final DistancesApi upstreamApi;

	private DistanceRepository() {
		localApi = new DistancesApi(new ApiClient());
		upstreamApi = new DistancesApi(new ApiClient());

		BackEndUrlProvider.INSTANCE.addAvailableListener(() -> {
			String localURL = BackEndUrlProvider.INSTANCE.getUrl();
			if (localURL != null) {
				localApi.getApiClient().setBasePath(localURL);
			}
		});

		doHandleUpstreamURLChange();
	}

	@Override
	public boolean isReady() {
		if (backendUrl != null) {
			return true;
		} else if (BackEndUrlProvider.INSTANCE.isAvailable()) {
			backendUrl = BackEndUrlProvider.INSTANCE.getUrl();
			localApi.getApiClient().setBasePath(backendUrl);
			return true;
		} else {
			return false;
		}
	}

	private void ensureReady() {
		if (!isReady()) {
			throw new IllegalStateException("Distances back-end not ready yet");
		}
	}

	@Override
	public List<DataVersion> getVersions() {
		ensureReady();
		try {
			return localApi.getVersionsUsingGET().stream().map(v -> {
				final LocalDateTime createdAt = fromDateTimeAtUTC(v.getCreatedAt());
				return new DataVersion(v.getIdentifier(), createdAt, false);
			}).collect(Collectors.toList());
		} catch (final ApiException e) {
			LOG.error("Error fetching distances versions" + e.getMessage());
			throw new RuntimeException("Error fetching distances versions", e);
		}
	}

	@Override
	public List<DataVersion> getUpstreamVersions() {
		ensureReady();
		try {
			return upstreamApi.getVersionsUsingGET().stream().map(v -> {
				final LocalDateTime createdAt = fromDateTimeAtUTC(v.getCreatedAt());
				return new DataVersion(v.getIdentifier(), createdAt, v.isPublished());
			}).collect(Collectors.toList());
		} catch (final ApiException e) {
			LOG.error("Error fetching distances versions" + e.getMessage());
			throw new RuntimeException("Error fetching distances versions", e);
		}
	}

//	@Override
//	public DataVersion getUpstreamVersion(String identifier) {
//		ensureReady();
//		try {
//			Version v = upstreamApi.getFullVersionUsingGET(identifier);
//			final LocalDateTime createdAt = fromDateTimeAtUTC(v.getCreatedAt());
//			return new DataVersion(v.getIdentifier(), createdAt, true);
//		} catch (final Exception e) {
//			LOG.error("Error fetching specific distances version" + e.getMessage());
//			throw new RuntimeException("Error fetching specific distances version", e);
//		}
//	}

	public IDistanceProvider getDistances(final String version) {

		try {

			final Map<Via, Map<String, Map<String, Double>>> result = UpstreamDistancesFetcher.getDistances(BackEndUrlProvider.INSTANCE.getUrl(), version, "", "");
			return new DefaultDistanceProvider(version, result);
		} catch (AuthenticationException | IOException | ParseException e) {
			LOG.error("Error fetching versions from upstream service", e);
			throw new RuntimeException("Error fetching versions from upstream service", e);
		}
	}

	@Override
	public List<DataVersion> updateAvailable() throws ApiException {
		final List<DataVersion> upstreamVersions = getUpstreamVersions();
		final Set<String> localVersions = getVersions().stream().map(v -> v.getIdentifier()).collect(Collectors.toSet());
		upstreamVersions.removeIf(uv -> localVersions.contains(uv.getIdentifier()));
		return upstreamVersions.stream().map(v -> new DataVersion(v.getIdentifier(), v.getCreatedAt(), v.isPublished())).collect(Collectors.toList());
	}

	public List<DataVersion> getUpstreamDistances() throws ApiException {
		final List<Version> upstreamVersions = upstreamApi.getVersionsUsingGET();
		return upstreamVersions.stream().map(v -> new DataVersion(v.getIdentifier(), fromDateTimeAtUTC(v.getCreatedAt()), v.isPublished())).collect(Collectors.toList());
	}

	public @Nullable IDistanceProvider getLatestDistances() {

		final List<DataVersion> versions = getVersions();
		if (versions.isEmpty()) {
			return null;
		}
		final String version = versions.get(0).getIdentifier();

		try {
			final Map<Via, Map<String, Map<String, Double>>> result = UpstreamDistancesFetcher.getDistances(BackEndUrlProvider.INSTANCE.getUrl(), version, "", "");
			return new DefaultDistanceProvider(version, result);
		} catch (AuthenticationException | IOException | ParseException e) {
			LOG.error("Error fetching versions from upstream service", e);
			throw new RuntimeException("Error fetching versions from upstream service", e);
		}
	}

	@Override
	protected void doHandleUpstreamURLChange() {

		String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();
		if (upstreamURL != null && !upstreamURL.isEmpty()) {
			upstreamApi.getApiClient().setBasePath(upstreamURL);
			upstreamApi.getApiClient().setUsername(UpstreamUrlProvider.INSTANCE.getUsername());
			upstreamApi.getApiClient().setPassword(UpstreamUrlProvider.INSTANCE.getPassword());
			upstreamApi.getApiClient().getHttpClient().setAuthenticator(getAuthenticator());
		}
	}

	@Override
	protected boolean canWaitForNewLocalVersion() {
		return false;
	}

	@Override
	protected boolean canWaitForNewUpstreamVersion() {
		return false;
	}

	@Override
	protected String getSyncVersionEndpoint() {
		return SYNC_VERSION_ENDPOINT;
	}

	@Override
	protected String getVersionNotificationEndpoint() {
		return "/distances/version_notification";
	}
	

	public com.mmxlabs.lngdataserver.integration.distances.model.Version getLocalVersion(String versionTag) throws IOException {
		ensureReady();
		Request request = new Request.Builder().url(BackEndUrlProvider.INSTANCE.getUrl() + SYNC_VERSION_ENDPOINT + versionTag).build();
		try (Response response = CLIENT.newCall(request).execute()) {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.findAndRegisterModules();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			return mapper.readValue(response.body().byteStream(), com.mmxlabs.lngdataserver.integration.distances.model.Version.class);
		}

	}


}