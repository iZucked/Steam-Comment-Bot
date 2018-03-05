package com.mmxlabs.lngdataserver.integration.distances;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.http.auth.AuthenticationException;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.preference.IPreferenceStore;
import org.joda.time.DateTime;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataservice.client.distances.ApiClient;
import com.mmxlabs.lngdataservice.client.distances.ApiException;
import com.mmxlabs.lngdataservice.client.distances.api.DistancesApi;
import com.mmxlabs.common.Triple;
import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.commons.impl.AbstractDataRepository;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import com.mmxlabs.lngdataservice.client.distances.model.Version;
import com.mmxlabs.lngdataservice.client.distances.model.PublishRequest;

/**
 * This implementation is not thread-safe.
 * 
 * @author Robert Erdin
 */
public class DistanceRepository extends AbstractDataRepository {

	private static final Logger LOG = LoggerFactory.getLogger(DistanceRepository.class);

	private static final String SYNC_ENDPOINT = "/distances/sync/publish";
	private static final String SYNC_VERSION_ENDPOINT = "/distances/sync/versions/";

	private final DistancesApi distancesApi;
	private final DistancesApi upstreamDistancesApi;
	// used for long polling... timeout set to infinite
	// Infinite timeout means listening for version can't effectively be cancelled
	private final DistancesApi waitingDistancesApi;
	private final DistancesApi upstreamWaitingDistancesApi;

	public DistanceRepository(@Nullable IPreferenceStore preferenceStore) {
		this(preferenceStore, null);
	}

	public DistanceRepository(@Nullable IPreferenceStore preferenceStore, final String localURL) {
		super(preferenceStore, localURL);
		distancesApi = new DistancesApi(new ApiClient());
		upstreamDistancesApi = new DistancesApi(new ApiClient());
		waitingDistancesApi = new DistancesApi(new ApiClient());
		upstreamWaitingDistancesApi = new DistancesApi(new ApiClient());
		if (localURL != null) {
			distancesApi.getApiClient().setBasePath(localURL);
			waitingDistancesApi.getApiClient().setBasePath(localURL);
			waitingDistancesApi.getApiClient().getHttpClient().setReadTimeout(0, TimeUnit.MILLISECONDS);
		}
		newUpstreamURL(upstreamUrl);
	}

	@Override
	public List<DataVersion> getVersions() {
		try {
			return distancesApi.getVersionsUsingGET().stream().map(v -> {
				final LocalDateTime createdAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(v.getCreatedAt().getMillis()), ZoneId.of("UTC"));
				return new DataVersion(v.getIdentifier(), createdAt, v.getPublished());
			}).collect(Collectors.toList());
		} catch (final ApiException e) {
			LOG.error("Error fetching distances versions" + e.getMessage());
			throw new RuntimeException("Error fetching distances versions", e);
		}
	}

	@Override
	protected CompletableFuture<String> waitForNewLocalVersion() {

		final CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
			Version futureVersion;
			try {
				futureVersion = waitingDistancesApi.getDistanceUpdateUsingGET();
			} catch (final ApiException e) {
				throw new RuntimeException(e);
			}
			return futureVersion.getIdentifier();
		});
		return completableFuture;
	}

	@Override
	protected CompletableFuture<String> waitForNewUpstreamVersion() {

		final CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
			Version futureVersion;
			try {
				futureVersion = upstreamWaitingDistancesApi.getDistanceUpdateUsingGET();
			} catch (final ApiException e) {
				throw new RuntimeException(e);
			}
			return futureVersion.getIdentifier();
		});
		return completableFuture;
	}

	@Override
	public boolean isReady() {
		if (backendUrl != null) {
			return true;
		} else if (BackEndUrlProvider.INSTANCE.isAvailable()) {
			backendUrl = BackEndUrlProvider.INSTANCE.getUrl();
			distancesApi.getApiClient().setBasePath(backendUrl);
			waitingDistancesApi.getApiClient().setBasePath(backendUrl);
			waitingDistancesApi.getApiClient().getHttpClient().setReadTimeout(0, TimeUnit.MILLISECONDS);
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
	public void publishVersion(final String version) throws Exception {
		final PublishRequest publishRequest = new PublishRequest();
		publishRequest.setVersion(version);
		publishRequest.setUpstreamUrl(upstreamUrl + SYNC_VERSION_ENDPOINT);
		distancesApi.postSyncRequestUsingPOST(publishRequest);
	}

	@Override
	public void syncUpstreamVersion(final String version) throws Exception {

		final OkHttpClient httpclient = upstreamDistancesApi.getApiClient().getHttpClient();

		// Pull down the version data
		final Request pullRequest = new Request.Builder().url(upstreamUrl + SYNC_VERSION_ENDPOINT + version).get().build();
		final Response pullResponse = httpclient.newCall(pullRequest).execute();
		final String json = pullResponse.body().string();

		// Post the data to local repo
		final RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
		final Request postRequest = new Request.Builder().url(backendUrl + SYNC_VERSION_ENDPOINT).post(body).build();
		final Response postResponse = httpclient.newCall(postRequest).execute();
		// TODO: Check return code etc
	}

	public IDistanceProvider getDistances(final String version) {

		try {
			final Triple<String, String, String> serviceAuth = getServiceAuth();
			final Map<Via, Map<String, Map<String, Integer>>> result = UpstreamDistancesFetcher.getDistances(serviceAuth.getFirst(), version, serviceAuth.getSecond(), serviceAuth.getThird());
			return new DefaultDistanceProvider(version, result);
		} catch (AuthenticationException | IOException | ParseException e) {
			LOG.error("Error fetching versions from upstream service", e);
			throw new RuntimeException("Error fetching versions from upstream service", e);
		}
	}

	@Override
	public List<DataVersion> updateAvailable() throws ApiException {
		final List<Version> upstreamVersions = upstreamDistancesApi.getVersionsUsingGET();
		final Set<String> localVersions = getVersions().stream().map(v -> v.getIdentifier()).collect(Collectors.toSet());
		upstreamVersions.removeIf(uv -> localVersions.contains(uv.getIdentifier()));
		return upstreamVersions.stream().map(v -> new DataVersion(v.getIdentifier(), fromDateTimeAtUTC(v.getCreatedAt()), v.getPublished())).collect(Collectors.toList());
	}

	public List<DataVersion> getUpstreamDistances() throws ApiException {
		final List<Version> upstreamVersions = upstreamDistancesApi.getVersionsUsingGET();
		return upstreamVersions.stream().map(v -> new DataVersion(v.getIdentifier(), fromDateTimeAtUTC(v.getCreatedAt()), v.getPublished())).collect(Collectors.toList());
	}

	public @Nullable IDistanceProvider getLatestDistances() {

		final List<DataVersion> versions = getVersions();
		if (versions.isEmpty()) {
			return null;
		}
		final String version = versions.get(0).getIdentifier();

		try {
			final Triple<String, String, String> serviceAuth = getServiceAuth();
			final Map<Via, Map<String, Map<String, Integer>>> result = UpstreamDistancesFetcher.getDistances(serviceAuth.getFirst(), version, serviceAuth.getSecond(), serviceAuth.getThird());
			return new DefaultDistanceProvider(version, result);
		} catch (AuthenticationException | IOException | ParseException e) {
			LOG.error("Error fetching versions from upstream service", e);
			throw new RuntimeException("Error fetching versions from upstream service", e);
		}
	}

	@Override
	protected void newUpstreamURL(String upstreamURL) {
		if (upstreamURL == null)
			upstreamURL = "";
		upstreamDistancesApi.getApiClient().setBasePath(upstreamURL);
		upstreamWaitingDistancesApi.getApiClient().setBasePath(upstreamURL);
		upstreamWaitingDistancesApi.getApiClient().getHttpClient().setReadTimeout(0, TimeUnit.MILLISECONDS);
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

}