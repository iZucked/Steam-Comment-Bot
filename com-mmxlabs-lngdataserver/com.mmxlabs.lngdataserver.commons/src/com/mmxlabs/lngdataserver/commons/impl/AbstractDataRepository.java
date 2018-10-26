/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.commons.impl;

import java.io.IOException;
import java.net.Proxy;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.commons.IDataRepository;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.lngdataserver.server.UpstreamUrlProvider;

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
public abstract class AbstractDataRepository implements IDataRepository {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractDataRepository.class);

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	// protected final Triple<String, String, String> auth;
	protected String backendUrl;
	// protected String upstreamUrl;
	protected boolean listenForNewLocalVersions;
	protected boolean listenForNewUpstreamVersions;
	protected final List<Consumer<DataVersion>> newLocalVersionCallbacks = new LinkedList<>();
	protected final List<Consumer<DataVersion>> newUpstreamVersionCallbacks = new LinkedList<>();

	protected Thread localVersionThread;
	protected Thread upstreamVersionThread;

	protected final OkHttpClient CLIENT = new okhttp3.OkHttpClient();

	protected final OkHttpClient LONG_POLLING_CLIENT = new OkHttpClient().newBuilder() //
			.readTimeout(Integer.MAX_VALUE, TimeUnit.MILLISECONDS) //
			.build();

	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss+00:00");

	protected static LocalDateTime fromDateTimeAtUTC(final String dateTime) {
		return LocalDateTime.parse(dateTime, DATE_FORMATTER);
	}

	public AbstractDataRepository() {
		UpstreamUrlProvider.INSTANCE.registerDetailsChangedLister(() -> doHandleUpstreamURLChange());
		// upstreamUrl = getUpstreamUrl();
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
						final Boolean newVersionAvailable = newVersionFuture.get();
						if (Boolean.TRUE == newVersionAvailable) {
							final List<DataVersion> versions = getLocalVersions();
							for (final DataVersion v : versions) {
								newLocalVersionCallbacks.forEach(c -> c.accept(v));
							}
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
		String upstreamUrl = getUpstreamUrl();
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
					final Boolean versionAvailable = newVersionFuture.get();
					if (versionAvailable == Boolean.TRUE) {
						final List<DataVersion> versions = getUpstreamVersions();
						for (final DataVersion v : versions) {
							newUpstreamVersionCallbacks.forEach(c -> c.accept(v));
						}
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
	public void registerLocalVersionListener(final Consumer<DataVersion> versionConsumer) {
		newLocalVersionCallbacks.add(versionConsumer);
	}

	@Override
	public void registerUpstreamVersionListener(final Consumer<DataVersion> versionConsumer) {
		newUpstreamVersionCallbacks.add(versionConsumer);
	}

	// protected OkHttpClient buildClientWithBasicAuth() {
	// Triple<String, String, String> auth = new Triple(UpstreamUrlProvider.INSTANCE.getBaseURL(), UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword());
	// if (auth != null) {
	// OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
	// clientBuilder.authenticator(new Authenticator() {
	// @Override
	// public Request authenticate(Route route, Response response) throws IOException {
	// if (responseCount(response) >= 3) {
	// return null;
	// }
	// String credential = Credentials.basic(auth.getSecond(), auth.getThird());
	// return response.request().newBuilder().header("Authorization", credential).build();
	// }
	// });
	// return clientBuilder.build();
	// }
	//
	// return new OkHttpClient();
	// }

	private int responseCount(Response response) {
		int result = 1;
		while ((response = response.priorResponse()) != null) {
			result++;
		}
		return result;
	}

	protected com.squareup.okhttp.Authenticator getAuthenticator() {
		return new com.squareup.okhttp.Authenticator() {
			@Override
			public com.squareup.okhttp.Request authenticate(Proxy proxy, com.squareup.okhttp.Response response) throws IOException {
				String credential = Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword());
				return response.request().newBuilder().header("Authorization", credential).build();
			}

			@Override
			public com.squareup.okhttp.Request authenticateProxy(Proxy proxy, com.squareup.okhttp.Response response) throws IOException {
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

	protected abstract void doHandleUpstreamURLChange();

	public boolean hasUpstream() {
		String upstreamUrl = getUpstreamUrl();
		return upstreamUrl != null && !upstreamUrl.isEmpty();
	}

	@Override
	public boolean publishVersion(final String version) throws Exception {

		// load in the version data
		final Request pullRequest = new Request.Builder().url(backendUrl + getSyncVersionEndpoint() + version).get().build();
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
				.addHeader("Authorization", credential);
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
		final Request postRequest = new Request.Builder().url(backendUrl + getSyncVersionEndpoint()).post(body).build();
		try (final Response postResponse = CLIENT.newCall(postRequest).execute()) {
			// TODO: Check return code etc
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
		final CompletableFuture<Boolean> completableFuture = CompletableFuture.supplyAsync(() -> {
			final Request.Builder requestBuilder = upstream ? createUpstreamRequestBuilder(url)
					: new Request.Builder() //
							.url(url);
			final Request request = requestBuilder.build();
			Response response = null;
			try {
				response = LONG_POLLING_CLIENT.newCall(request).execute();
				if (response.isSuccessful()) {
					// Version newVersion = new ObjectMapper().readValue(response.body().byteStream(), Version.class);
					return Boolean.TRUE;
				}
				return Boolean.FALSE;
			} catch (final IOException e) {
				LOG.error("Error waiting for new version");
				throw new RuntimeException("Error waiting for new version");
			} finally {
				if (response != null) {
					response.body().close();
				}
			}
		});
		return completableFuture;
	}

	protected abstract String getVersionNotificationEndpoint();

	protected abstract String getSyncVersionEndpoint();

	public boolean hasLocalVersion(String version) {
		if (version == null || version.isEmpty()) {
			return false;
		}

		List<DataVersion> versions = getLocalVersions();

		if (versions != null) {
			for (DataVersion v : versions) {
				if (version.equals(v.getIdentifier())) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean hasUpstreamVersion(String version) {
		if (version == null || version.isEmpty()) {
			return false;
		}

		List<DataVersion> versions = getUpstreamVersions();

		if (versions != null) {
			for (DataVersion v : versions) {
				if (version.equals(v.getIdentifier())) {
					return true;
				}
			}
		}

		return false;
	}
}