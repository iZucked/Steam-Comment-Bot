package com.mmxlabs.lngdataserver.integration.distances;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.http.auth.AuthenticationException;
import org.eclipse.jdt.annotation.Nullable;
import org.joda.time.DateTime;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.commons.impl.AbstractDataRepository;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.lngdataserver.server.UpstreamUrlProvider;
import com.mmxlabs.lngdataservice.client.distances.ApiClient;
import com.mmxlabs.lngdataservice.client.distances.ApiException;
import com.mmxlabs.lngdataservice.client.distances.api.DistancesApi;
import com.mmxlabs.lngdataservice.client.distances.auth.Authentication;
import com.mmxlabs.lngdataservice.client.distances.auth.HttpBasicAuth;
import com.mmxlabs.lngdataservice.client.distances.model.Version;

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

		newUpstreamURL();
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
				final LocalDateTime createdAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(v.getCreatedAt().getMillis()), ZoneId.of("UTC"));
				return new DataVersion(v.getIdentifier(), createdAt, v.getPublished());
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
				final LocalDateTime createdAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(v.getCreatedAt().getMillis()), ZoneId.of("UTC"));
				return new DataVersion(v.getIdentifier(), createdAt, v.getPublished());
			}).collect(Collectors.toList());
		} catch (final ApiException e) {
			LOG.error("Error fetching distances versions" + e.getMessage());
			throw new RuntimeException("Error fetching distances versions", e);
		}
	}

	public IDistanceProvider getDistances(final String version) {

		try {

			final Map<Via, Map<String, Map<String, Integer>>> result = UpstreamDistancesFetcher.getDistances(UpstreamUrlProvider.INSTANCE.getBaseURL(), version,
					UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword());
			return new DefaultDistanceProvider(version, result);
		} catch (AuthenticationException | IOException | ParseException e) {
			LOG.error("Error fetching versions from upstream service", e);
			throw new RuntimeException("Error fetching versions from upstream service", e);
		}
	}

	@Override
	public List<DataVersion> updateAvailable() throws ApiException {
		final List<Version> upstreamVersions = upstreamApi.getVersionsUsingGET();
		final Set<String> localVersions = getVersions().stream().map(v -> v.getIdentifier()).collect(Collectors.toSet());
		upstreamVersions.removeIf(uv -> localVersions.contains(uv.getIdentifier()));
		return upstreamVersions.stream().map(v -> new DataVersion(v.getIdentifier(), fromDateTimeAtUTC(v.getCreatedAt()), v.getPublished())).collect(Collectors.toList());
	}

	public List<DataVersion> getUpstreamDistances() throws ApiException {
		final List<Version> upstreamVersions = upstreamApi.getVersionsUsingGET();
		return upstreamVersions.stream().map(v -> new DataVersion(v.getIdentifier(), fromDateTimeAtUTC(v.getCreatedAt()), v.getPublished())).collect(Collectors.toList());
	}

	public @Nullable IDistanceProvider getLatestDistances() {

		final List<DataVersion> versions = getVersions();
		if (versions.isEmpty()) {
			return null;
		}
		final String version = versions.get(0).getIdentifier();

		try {
			final Map<Via, Map<String, Map<String, Integer>>> result = UpstreamDistancesFetcher.getDistances(UpstreamUrlProvider.INSTANCE.getBaseURL(), version,
					UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword());
			return new DefaultDistanceProvider(version, result);
		} catch (AuthenticationException | IOException | ParseException e) {
			LOG.error("Error fetching versions from upstream service", e);
			throw new RuntimeException("Error fetching versions from upstream service", e);
		}
	}

	@Override
	protected void newUpstreamURL() {

		String upstreamURL = UpstreamUrlProvider.INSTANCE.getBaseURL();

		upstreamApi.getApiClient().setBasePath(upstreamURL);
		upstreamApi.getApiClient().setUsername(UpstreamUrlProvider.INSTANCE.getUsername());
		upstreamApi.getApiClient().setPassword(UpstreamUrlProvider.INSTANCE.getPassword());
		upstreamApi.getApiClient().getHttpClient().setAuthenticator(getAuthenticator());
	}

	protected static LocalDateTime fromDateTimeAtUTC(final DateTime dateTime) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(dateTime.getMillis()), ZoneId.of("UTC"));
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
	protected String getSyncVersionEndpoint() {
		return SYNC_VERSION_ENDPOINT;
	}

	@Override
	protected String getVersionNotificationEndpoint() {
		return "/distances/version_notification";
	}

}