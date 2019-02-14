/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.commons.impl;

import java.io.IOException;
import java.net.Proxy;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.commons.IDataRepository;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.lngdataserver.server.UpstreamUrlProvider;
import com.mmxlabs.rcp.common.RunnerHelper;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * This implementation is not thread-safe.
 * 
 * @author Robert Erdin
 */
public abstract class AbstractDataRepository<T> implements IDataRepository {

	protected interface SimpleVersion {
		public String getIdentifier();

		public LocalDateTime getCreatedAt();
	}

	private static final String CONST_AUTHORIZATION = "Authorization";

	private static final Logger LOG = LoggerFactory.getLogger(AbstractDataRepository.class);

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	protected boolean listenForNewLocalVersions;
	protected boolean listenForNewUpstreamVersions;
	protected final List<Runnable> newLocalVersionCallbacks = new LinkedList<>();
	protected final List<Runnable> newUpstreamVersionCallbacks = new LinkedList<>();

	protected Thread localVersionThread;
	protected Thread upstreamVersionThread;

	protected final OkHttpClient CLIENT = new okhttp3.OkHttpClient();

	protected final OkHttpClient LONG_POLLING_CLIENT = new OkHttpClient().newBuilder() //
			.readTimeout(Integer.MAX_VALUE, TimeUnit.MILLISECONDS) //
			.build();

	private final String type;

	private final Class<T> dataVersionType;

	protected final TypeReference<List<T>> TYPE_VERSIONS_LIST = new TypeReference<List<T>>() {
	};

	public AbstractDataRepository(final String repoType, final Class<T> dataVersionType) {
		this.type = repoType;
		this.dataVersionType = dataVersionType;
		UpstreamUrlProvider.INSTANCE.registerDetailsChangedLister(this::handleUpstreamURLChange);
	}

	protected String getUpstreamUrl() {
		if (!UpstreamUrlProvider.INSTANCE.isAvailable()) {
			return null;
		}

		return UpstreamUrlProvider.INSTANCE.getBaseURL();
	}

	@Override
	public void startListenForNewLocalVersions() {
		listenForNewLocalVersions = true;
		if (!canWaitForNewLocalVersion()) {
			return;
		}
		localVersionThread = new Thread(() -> {
			while (listenForNewLocalVersions) {
				final CompletableFuture<Boolean> newVersionFuture = waitForNewLocalVersion();
				try {
					if (newVersionFuture != null) {
						newLocalVersionCallbacks.forEach(Runnable::run);
						final Boolean newVersionAvailable = newVersionFuture.get();
						if (Boolean.TRUE == newVersionAvailable) {
							newLocalVersionCallbacks.forEach(Runnable::run);
						}
					}
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

	@Override
	public void startListenForNewUpstreamVersions() {
		listenForNewUpstreamVersions = true;
		if (!canWaitForNewUpstreamVersion()) {
			return;
		}
		final String upstreamUrl = getUpstreamUrl();
		if (upstreamUrl == null || upstreamUrl.trim().isEmpty()) {
			// No URL, do not try and connect
			return;
		}
		upstreamVersionThread = new Thread(() -> {
			while (listenForNewUpstreamVersions) {
				final CompletableFuture<Boolean> newVersionFuture = waitForNewUpstreamVersion();
				try {
					if (newVersionFuture == null) {
						return;
					}
					newUpstreamVersionCallbacks.forEach(Runnable::run);
					final Boolean versionAvailable = newVersionFuture.get();
					if (versionAvailable == Boolean.TRUE) {
						newUpstreamVersionCallbacks.forEach(Runnable::run);
					}
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
		upstreamVersionThread.setName("DataHub Upstream listener: " + getClass().getName());
		upstreamVersionThread.start();
	}

	@Override
	public void stopListeningForNewLocalVersions() {
		listenForNewLocalVersions = false;
		if (localVersionThread != null) {
			localVersionThread.interrupt();
			localVersionThread = null;
		}
	}

	@Override
	public void stopListeningForNewUpstreamVersions() {
		listenForNewUpstreamVersions = false;
		if (upstreamVersionThread != null) {
			upstreamVersionThread.interrupt();
			upstreamVersionThread = null;
		}
	}

	@Override
	public void registerLocalVersionListener(final Runnable versionConsumer) {
		newLocalVersionCallbacks.add(versionConsumer);
	}

	@Override
	public void registerUpstreamVersionListener(final Runnable versionConsumer) {
		newUpstreamVersionCallbacks.add(versionConsumer);
	}

	public Runnable registerDefaultUpstreamVersionListener() {
		final Runnable runnable = () -> RunnerHelper.asyncExec(c -> {
			try {
				updateAvailable().forEach(v -> {
					try {
						syncUpstreamVersion(v.getIdentifier());
					} catch (final Exception e) {
						e.printStackTrace();
					}
				});
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
		registerUpstreamVersionListener(runnable);
		return runnable;
	}

	protected com.squareup.okhttp.Authenticator getAuthenticator() {
		return new com.squareup.okhttp.Authenticator() {
			@Override
			public com.squareup.okhttp.Request authenticate(final Proxy proxy, final com.squareup.okhttp.Response response) throws IOException {
				final String credential = Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword());
				return response.request().newBuilder().header(CONST_AUTHORIZATION, credential).build();
			}

			@Override
			public com.squareup.okhttp.Request authenticateProxy(final Proxy proxy, final com.squareup.okhttp.Response response) throws IOException {
				return null;
			}
		};
	}

	protected boolean canWaitForNewLocalVersion() {
		return false;
	}

	protected boolean canWaitForNewUpstreamVersion() {
		return false;
	}

	protected void handleUpstreamURLChange() {
		// Restart change listener after detail change
		boolean restartChangeListener = false;
		if (listenForNewUpstreamVersions) {
			restartChangeListener = true;
			stopListeningForNewUpstreamVersions();
		}

		doHandleUpstreamURLChange();

		if (restartChangeListener) {
			startListenForNewUpstreamVersions();
		}
	}

	protected void doHandleUpstreamURLChange() {

	}

	public boolean hasUpstream() {
		final String upstreamUrl = getUpstreamUrl();
		return upstreamUrl != null && !upstreamUrl.isEmpty();
	}

	@Override
	public boolean publishVersion(final String version) throws Exception {

		// load in the version data
		final Request pullRequest = new Request.Builder().url(BackEndUrlProvider.INSTANCE.getUrl() + getSyncVersionEndpoint() + version).get().build();
		final String json;
		try (final Response pullResponse = CLIENT.newCall(pullRequest).execute()) {
			if (!pullResponse.isSuccessful()) {
				return false;
			}
			json = pullResponse.body().string();
		}

		// Post the data to upstream repo
		final RequestBody body = RequestBody.create(JSON, json);
		final Request postRequest = createUpstreamRequestBuilder(getUpstreamUrl() + getSyncVersionEndpoint()) //
				.post(body).build();
		try (Response postResponse = CLIENT.newCall(postRequest).execute()) {
			return postResponse.isSuccessful();
		}
	}

	protected Request.Builder createUpstreamRequestBuilder(final String url) {
		final String credential = Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword());
		return new Request.Builder() //
				.url(url) //
				.addHeader(CONST_AUTHORIZATION, credential);
	}

	@Override
	public boolean syncUpstreamVersion(final String version) throws Exception {

		// Pull down the version data
		final Request pullRequest = createUpstreamRequestBuilder(getUpstreamUrl() + getSyncVersionEndpoint() + version) //
				.get().build();
		final String json;

		try (final Response pullResponse = CLIENT.newCall(pullRequest).execute()) {
			if (!pullResponse.isSuccessful()) {
				return false;
			}
			json = pullResponse.body().string();
		}
		// Post the data to local repo
		final RequestBody body = RequestBody.create(JSON, json);
		final Request postRequest = new Request.Builder().url(BackEndUrlProvider.INSTANCE.getUrl() + getSyncVersionEndpoint()).post(body).build();
		try (final Response postResponse = CLIENT.newCall(postRequest).execute()) {
			return postResponse.isSuccessful();
		}
	}

	@Override
	public boolean publishUpstreamVersion(final String versionJSON) throws Exception {

		final RequestBody body = RequestBody.create(JSON, versionJSON);
		final Request postRequest = createUpstreamRequestBuilder(getUpstreamUrl() + getSyncVersionEndpoint()).post(body).build();
		try (final Response postResponse = CLIENT.newCall(postRequest).execute()) {
			return postResponse.isSuccessful();
		}
	}

	protected CompletableFuture<Boolean> waitForNewLocalVersion() {
		return notifyOnNewVersion(false);
	}

	protected CompletableFuture<Boolean> waitForNewUpstreamVersion() {
		return notifyOnNewVersion(true);
	}

	protected CompletableFuture<Boolean> notifyOnNewVersion(final boolean upstream) {
		final String baseUrl = upstream ? UpstreamUrlProvider.INSTANCE.getBaseURL() : BackEndUrlProvider.INSTANCE.getUrl();

		if (baseUrl == null || baseUrl.isEmpty()) {
			return null;
		}
		final String url = baseUrl + getVersionNotificationEndpoint();
		LOG.debug("Calling url {}", url);
		return CompletableFuture.supplyAsync(() -> {
			final Request.Builder requestBuilder = upstream ? createUpstreamRequestBuilder(url)
					: new Request.Builder() //
							.url(url);
			final Request request = requestBuilder.build();
			try (Response response = LONG_POLLING_CLIENT.newCall(request).execute()) {
				if (response.isSuccessful()) {
					return Boolean.TRUE;
				}
				return Boolean.FALSE;
			} catch (final IOException e) {
				LOG.error("Error waiting for new version");
				throw new RuntimeException("Error waiting for new version");
			}
		});
	}

	protected abstract String getVersionNotificationEndpoint();

	protected abstract String getSyncVersionEndpoint();

	public boolean hasLocalVersion(final String version) {
		if (version == null || version.isEmpty()) {
			return false;
		}

		final List<DataVersion> versions = getLocalVersions();

		if (versions != null) {
			for (final DataVersion v : versions) {
				if (version.equals(v.getIdentifier())) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean hasUpstreamVersion(final String version) {
		if (version == null || version.isEmpty()) {
			return false;
		}

		final List<DataVersion> versions = getUpstreamVersions();

		if (versions != null) {
			for (final DataVersion v : versions) {
				if (version.equals(v.getIdentifier())) {
					return true;
				}
			}
		}

		return false;
	}

	public static Request.@NonNull Builder createRequestBuilder(final @NonNull String url, final @Nullable String username, final @Nullable String password) {
		final Request.@NonNull Builder requestBuilder = new Request.Builder().url(url);
		if (username != null && password != null) {
			requestBuilder.addHeader(CONST_AUTHORIZATION, Credentials.basic(username, password));
		}
		return requestBuilder;
	}

	@Override
	public boolean isReady() {
		return BackEndUrlProvider.INSTANCE.isAvailable();
	}

	protected void ensureReady() {
		if (!isReady()) {
			throw new IllegalStateException(type + " back-end not ready yet");
		}
	}

	@Override
	public List<DataVersion> getUpstreamVersions() {
		ensureReady();
		try {
			return getVersions(UpstreamUrlProvider.INSTANCE.getBaseURL(), UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword());
		} catch (final Exception e) {
			final String msg = String.format("Error fetching upstream %s versions", type.toLowerCase());
			LOG.error(msg + e.getMessage());
			throw new RuntimeException(msg, e);
		}
	}

	public List<DataVersion> getVersions(final String baseUrl, final String username, final String password) throws IOException {
		final Request request = createRequestBuilder(baseUrl + getVersionsURL(), username, password).build();
		try (Response response = CLIENT.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				final String body = response.body().string();
				throw new RuntimeException("Error making request to " + baseUrl + ". Reason " + response.message() + " Response body is " + body);
			} else {
				final ObjectMapper mapper = createObjectMapper();
				final List<T> hubVersions = mapper.readValue(response.body().byteStream(), mapper.getTypeFactory().constructCollectionType(List.class, dataVersionType));
				return hubVersions.stream() //
						.map(this::wrap) //
						.filter(v -> v.getIdentifier() != null) //
						.filter(v -> v.getCreatedAt() != null) //
						.sorted((v1, v2) -> v2.getCreatedAt().compareTo(v1.getCreatedAt())) //
						.map(v -> new DataVersion(v.getIdentifier(), v.getCreatedAt(), false, false)) //
						.collect(Collectors.toList());
			}
		}
	}

	protected ObjectMapper createObjectMapper() {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}

	protected abstract String getVersionsURL();

	@Override
	public List<DataVersion> getLocalVersions() {
		ensureReady();
		try {
			return getVersions(BackEndUrlProvider.INSTANCE.getUrl(), null, null);
		} catch (final Exception e) {
			final String msg = String.format("Error fetching local %s versions", type.toLowerCase());

			LOG.error(msg + e.getMessage());
			throw new RuntimeException(msg, e);
		}
	}

	@Override
	public List<DataVersion> updateAvailable() throws Exception {
		final List<DataVersion> upstreamVersions = getUpstreamVersions();
		final Set<String> localVersions = getLocalVersions().stream().map(DataVersion::getIdentifier).collect(Collectors.toSet());
		upstreamVersions.removeIf(uv -> localVersions.contains(uv.getIdentifier()));
		return upstreamVersions.stream().map(v -> new DataVersion(v.getIdentifier(), v.getCreatedAt(), v.isPublished())).collect(Collectors.toList());
	}

	public T getLocalVersion(final String versionTag) throws IOException {
		final String url = getSyncVersionEndpoint();
		final String baseURL = BackEndUrlProvider.INSTANCE.getUrl();
		final Request request = new Request.Builder().url(baseURL + url + versionTag).build();
		try (Response response = CLIENT.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				final String body = response.body().string();
				throw new RuntimeException("Error making request to " + url + ". Reason " + response.message() + " Response body is " + body);
			} else {
				final ObjectMapper mapper = new ObjectMapper();
				mapper.findAndRegisterModules();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

				return mapper.readValue(response.body().byteStream(), dataVersionType);
			}
		}
	}

	protected abstract SimpleVersion wrap(T version);
}