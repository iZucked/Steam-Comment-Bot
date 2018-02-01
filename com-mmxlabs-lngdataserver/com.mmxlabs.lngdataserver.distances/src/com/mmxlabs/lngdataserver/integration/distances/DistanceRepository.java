package com.mmxlabs.lngdataserver.integration.distances;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.http.auth.AuthenticationException;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.preference.IPreferenceStore;
import org.joda.time.DateTime;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.ApiClient;
import com.mmxlabs.ApiException;
import com.mmxlabs.common.Triple;
import com.mmxlabs.lngdataserver.DistancesApi;
import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.integration.distances.internal.Activator;
import com.mmxlabs.lngdataserver.integration.distances.preferences.PreferenceConstants;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;

import io.swagger.client.model.PublishRequest;
import io.swagger.client.model.Version;

/**
 * This implementation is not thread-safe.
 * 
 * @author Robert Erdin
 */
public class DistanceRepository {
	
	private static final String SYNC_ENDPOINT = "/distances/sync/publish";
	
	private DistancesApi distancesApi = new DistancesApi(new ApiClient());
	// used for long polling... timeout set to infinite
	// Infinite timeout means listening for version can't effectively be cancelled
	private DistancesApi waitingDistancesApi = new DistancesApi(new ApiClient());
	
	private DistancesApi upstreamDistancesApi = new DistancesApi(new ApiClient());

	private static final Logger LOG = LoggerFactory.getLogger(DistanceRepository.class);
	private Triple<String, String, String> auth;
	private String backendUrl;
	private String upstreamUrl;
	private boolean listenForNewVersions;
	private final List<Consumer<String>> newVersionCallbacks = new LinkedList<Consumer<String>>();

	public DistanceRepository() {
		auth = getUserServiceAuth();
		upstreamUrl = getUpstreamUrl();
		upstreamDistancesApi.getApiClient().setBasePath(upstreamUrl);
	}

	public DistanceRepository(String url) {
		auth = new Triple<>(url, "", "");
	}

	private Triple<String, String, String> getUserServiceAuth() {
		final IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
		final String url = getUpstreamUrl();
		final String username = prefs.getString(PreferenceConstants.P_USERNAME_KEY);
		final String password = prefs.getString(PreferenceConstants.P_PASSWORD_KEY);

		return new Triple<>(url, username, password);
	}
	
	private String getUpstreamUrl() {
		final IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
		final String url = prefs.getString(PreferenceConstants.P_URL_KEY);
		if ("".equals(url)) {
			throw new RuntimeException("No URL found for upstream distance repository");
		}
		return url;
	}

	public List<DataVersion> getVersions() {
		try {
			return distancesApi.getVersionsUsingGET().stream().map(v -> {
				LocalDateTime createdAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(v.getCreatedAt().getMillis()), ZoneId.of("UTC"));
				return new DataVersion(v.getIdentifier(), createdAt, v.getPublished());
			}).collect(Collectors.toList());
		} catch (ApiException e) {
			LOG.error("Error fetchinng distances versions" + e.getMessage());
			throw new RuntimeException("Error fetching distances versionns", e);
		}
	}
	
	public void listenForNewVersions() {
		listenForNewVersions = true;
		
		new Thread(() ->  {
			while(listenForNewVersions) {
				CompletableFuture<String> newVersion = waitForNewVersion();
				try {
					String version = newVersion.get();
					newVersionCallbacks.forEach(c -> c.accept(version));
				} catch (InterruptedException e) {
					LOG.error(e.getMessage());
				} catch (ExecutionException e) {
					LOG.error(e.getMessage());
				}
				
				// make sure not everything is blocked in case of consecutive failure
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}).start();
	}
	
	public void stopListeningForNewVersions() {
		listenForNewVersions = false;
	}
	
	public void registerVersionListener(Consumer<String> versionConsumer) {
		newVersionCallbacks.add(versionConsumer);
	}
	
	private CompletableFuture<String> waitForNewVersion(){

		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
			Version futureVersion;
			try {
				futureVersion = distancesApi.getDistanceUpdateUsingGET();
			} catch (ApiException e) {
				throw new RuntimeException(e);
			}
			return futureVersion.getIdentifier();
		});
		return completableFuture;
	}
	
	public boolean isReady() {
		if (backendUrl != null) {
			return true;
		}else if(BackEndUrlProvider.INSTANCE.isAvailable()) {
			backendUrl = BackEndUrlProvider.INSTANCE.getUrl();
			distancesApi.getApiClient().setBasePath(backendUrl);
			waitingDistancesApi.getApiClient().setBasePath(backendUrl);
			waitingDistancesApi.getApiClient().getHttpClient().setReadTimeout(0, TimeUnit.MILLISECONDS);
			return true;
		}else {
			return false;
		}
	}
	
	private void ensureReady() {
		if (!isReady()) {
			throw new IllegalStateException("Distances back-end not ready yet");
		}
	}
	
	public void publishVersion(String version) throws ApiException {
		PublishRequest publishRequest = new PublishRequest();
		publishRequest.setVersion(version);
		publishRequest.setUpstreamUrl(upstreamUrl + SYNC_ENDPOINT);
		distancesApi.postSyncRequestUsingPOST(publishRequest);
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

	private Triple<String, String, String> getServiceAuth() {
		return auth;
	}
	
	public List<DataVersion> updateAvailable() throws ApiException{
		List<Version> upstreamVersions = upstreamDistancesApi.getVersionsUsingGET();
		Set<String> localVersions = getVersions().stream().map(v -> v.getIdentifier()).collect(Collectors.toSet());
		upstreamVersions.removeIf(uv -> localVersions.contains(uv.getIdentifier()));
		return upstreamVersions.stream()
				.map(v -> new DataVersion(v.getIdentifier(), fromDateTimeAtUTC(v.getCreatedAt()), v.getPublished()))
				.collect(Collectors.toList());
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
	
	private static LocalDateTime fromDateTimeAtUTC(DateTime dateTime) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(dateTime.getMillis()), ZoneId.of("UTC"));
	}
}