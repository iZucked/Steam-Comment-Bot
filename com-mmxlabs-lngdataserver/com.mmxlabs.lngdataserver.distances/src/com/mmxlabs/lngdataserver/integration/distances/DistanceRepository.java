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
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
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
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import io.swagger.client.model.PublishRequest;
import io.swagger.client.model.Version;

/**
 * This implementation is not thread-safe.
 * 
 * @author Robert Erdin
 */
public class DistanceRepository {

	private static final String SYNC_ENDPOINT = "/distances/sync/publish";
	private static final String SYNC_VERSION_ENDPOINT = "/distances/sync/versions/";

	private final DistancesApi distancesApi = new DistancesApi(new ApiClient());
	// used for long polling... timeout set to infinite
	// Infinite timeout means listening for version can't effectively be cancelled
	private final DistancesApi waitingDistancesApi = new DistancesApi(new ApiClient());

	private final DistancesApi upstreamDistancesApi = new DistancesApi(new ApiClient());
	private final DistancesApi upstreamWaitingDistancesApi = new DistancesApi(new ApiClient());

	private static final Logger LOG = LoggerFactory.getLogger(DistanceRepository.class);
	private final Triple<String, String, String> auth;
	private String backendUrl;
	private String upstreamUrl;
	private boolean listenForNewLocalVersions;
	private boolean listenForNewUpstreamVersions;
	private final List<Consumer<String>> newLocalVersionCallbacks = new LinkedList<>();
	private final List<Consumer<String>> newUpstreamVersionCallbacks = new LinkedList<>();
	private final IPropertyChangeListener listener = new IPropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			switch (event.getProperty()) {
			case PreferenceConstants.P_URL_KEY:
			case PreferenceConstants.P_USERNAME_KEY:
			case PreferenceConstants.P_PASSWORD_KEY:

				boolean listen = listenForNewUpstreamVersions;
				if (listen) {
					stopListeningForNewUpstreamVersions();
				}
				upstreamUrl = getUpstreamUrl();
				upstreamDistancesApi.getApiClient().setBasePath(upstreamUrl);
				upstreamWaitingDistancesApi.getApiClient().setBasePath(upstreamUrl);
				upstreamWaitingDistancesApi.getApiClient().getHttpClient().setReadTimeout(0, TimeUnit.MILLISECONDS);

				if (listen) {
					listenForNewUpstreamVersions();
				}

				break;
			default:
			}
		}
	};

	private Thread localVersionThread;
	private Thread upstreamVersionThread;

	public DistanceRepository() {
		auth = getUserServiceAuth();
		upstreamUrl = getUpstreamUrl();
		upstreamDistancesApi.getApiClient().setBasePath(upstreamUrl);
		upstreamWaitingDistancesApi.getApiClient().setBasePath(upstreamUrl);
		upstreamWaitingDistancesApi.getApiClient().getHttpClient().setReadTimeout(0, TimeUnit.MILLISECONDS);

	}

	public DistanceRepository(final String url) {
		auth = new Triple<>(url, "", "");
		upstreamUrl = getUpstreamUrl();
		upstreamDistancesApi.getApiClient().setBasePath(upstreamUrl);
		upstreamWaitingDistancesApi.getApiClient().setBasePath(upstreamUrl);
		upstreamWaitingDistancesApi.getApiClient().getHttpClient().setReadTimeout(0, TimeUnit.MILLISECONDS);

	}

	public void listenToPreferenceChanges() {
		final IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
		prefs.addPropertyChangeListener(listener);
	}

	public void stopListenToPreferenceChanges() {
		final IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
		prefs.removePropertyChangeListener(listener);
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
				final LocalDateTime createdAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(v.getCreatedAt().getMillis()), ZoneId.of("UTC"));
				return new DataVersion(v.getIdentifier(), createdAt, v.getPublished());
			}).collect(Collectors.toList());
		} catch (final ApiException e) {
			LOG.error("Error fetchinng distances versions" + e.getMessage());
			throw new RuntimeException("Error fetching distances versions", e);
		}
	}

	public void listenForNewLocalVersions() {
		listenForNewLocalVersions = true;

		localVersionThread = new Thread(() -> {
			while (listenForNewLocalVersions) {
				final CompletableFuture<String> newVersion = waitForNewVersion();
				try {
					final String version = newVersion.get();
					newLocalVersionCallbacks.forEach(c -> c.accept(version));
				} catch (final InterruptedException e) {
					LOG.error(e.getMessage());
				} catch (final ExecutionException e) {
					LOG.error(e.getMessage());
				}

				// make sure not everything is blocked in case of consecutive failure
				try {
					Thread.sleep(1000);
				} catch (final InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		});
		localVersionThread.start();
	}

	public void listenForNewUpstreamVersions() {
		listenForNewUpstreamVersions = true;
		if (upstreamUrl == null && upstreamUrl.trim().isEmpty()) {
			// No URL, do not try and connect
			return;
		}
		upstreamVersionThread = new Thread(() -> {
			while (listenForNewUpstreamVersions) {
				final CompletableFuture<String> newVersion = waitForNewUpstreamVersion();
				try {
					final String version = newVersion.get();
					System.out.println("New version " + version);
					newUpstreamVersionCallbacks.forEach(c -> c.accept(version));
				} catch (final InterruptedException e) {
					if (!listenForNewUpstreamVersions) {
						return;
					}
				} catch (final ExecutionException e) {
					LOG.error(e.getMessage());
				}

				// make sure not everything is blocked in case of consecutive failure
				try {
					Thread.sleep(1000);
				} catch (final InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		});
		upstreamVersionThread.setName("DataServer: Distances upstream listener");
		upstreamVersionThread.start();
	}

	public void stopListeningForNewLocalVersions() {
		listenForNewLocalVersions = false;
		if (localVersionThread != null) {
			localVersionThread.interrupt();
			localVersionThread = null;
		}
	}

	public void stopListeningForNewUpstreamVersions() {
		listenForNewUpstreamVersions = false;
		if (upstreamVersionThread != null) {
			upstreamVersionThread.interrupt();
			upstreamVersionThread = null;
		}
	}

	public void registerLocalVersionListener(final Consumer<String> versionConsumer) {
		newLocalVersionCallbacks.add(versionConsumer);
	}

	public void registerUpstreamVersionListener(final Consumer<String> versionConsumer) {
		newUpstreamVersionCallbacks.add(versionConsumer);
	}

	private CompletableFuture<String> waitForNewVersion() {

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

	private CompletableFuture<String> waitForNewUpstreamVersion() {

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

	public void publishVersion(final String version) throws ApiException {
		final PublishRequest publishRequest = new PublishRequest();
		publishRequest.setVersion(version);
		publishRequest.setUpstreamUrl(upstreamUrl + SYNC_VERSION_ENDPOINT);
		distancesApi.postSyncRequestUsingPOST(publishRequest);
	}

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

	private Triple<String, String, String> getServiceAuth() {
		return auth;
	}

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

	private static LocalDateTime fromDateTimeAtUTC(final DateTime dateTime) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(dateTime.getMillis()), ZoneId.of("UTC"));
	}
}