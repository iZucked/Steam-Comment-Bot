package com.mmxlabs.lngdataserver.commons.impl;

import java.io.IOException;
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
	protected String upstreamUrl;
	protected boolean listenForNewLocalVersions;
	protected boolean listenForNewUpstreamVersions;
	protected final List<Consumer<String>> newLocalVersionCallbacks = new LinkedList<>();
	protected final List<Consumer<String>> newUpstreamVersionCallbacks = new LinkedList<>();

	protected Thread localVersionThread;
	protected Thread upstreamVersionThread;

	public AbstractDataRepository() {
		UpstreamUrlProvider.INSTANCE.registerDetailsChangedLister(() -> newUpstreamURL());
		upstreamUrl = getUpstreamUrl();
	}

	@Override
	public void listenToPreferenceChanges() {
		// if (preferenceStore != null) {
		// preferenceStore.addPropertyChangeListener(listener);
		// }
	}

	@Override
	public void stopListenToPreferenceChanges() {
		// if (preferenceStore != null) {
		// preferenceStore.removePropertyChangeListener(listener);
		// }
	}
	//
	// protected Triple<String, String, String> getUserServiceAuth() {
	// IPreferenceStore p_preferenceStore = preferenceStore;
	// if (p_preferenceStore != null) {
	// final String url = p_preferenceStore.getString(StandardDateRepositoryPreferenceConstants.P_URL_KEY);
	// final String username = p_preferenceStore.getString(StandardDateRepositoryPreferenceConstants.P_USERNAME_KEY);
	// final String password = p_preferenceStore.getString(StandardDateRepositoryPreferenceConstants.P_PASSWORD_KEY);
	//
	// return new Triple<>(url, username, password);
	// }
	// return new Triple<>();
	// }

	protected String getUpstreamUrl() {
		return UpstreamUrlProvider.INSTANCE.getBaseURL();
		// if (preferenceStore != null) {
		// final String url = preferenceStore.getString(StandardDateRepositoryPreferenceConstants.P_URL_KEY);
		// return url;
		// }
		// return "";
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
							List<DataVersion> versions = getVersions();
							for (DataVersion v : versions) {
								newLocalVersionCallbacks.forEach(c -> c.accept(v.getIdentifier()));
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
		if (upstreamUrl == null || upstreamUrl.trim().isEmpty()) {
			// No URL, do not try and connect
			return;
		}
		upstreamVersionThread = new Thread(() -> {
			while (listenForNewUpstreamVersions) {
				final CompletableFuture<Boolean> newVersionFuture = waitForNewUpstreamVersion();
				try {
					final Boolean versionAvailable = newVersionFuture.get();
					if (versionAvailable == Boolean.TRUE) {
						List<DataVersion> versions = getUpstreamVersions();
						for (DataVersion v : versions) {
							newUpstreamVersionCallbacks.forEach(c -> c.accept(v.getIdentifier()));
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
	public void registerLocalVersionListener(final Consumer<String> versionConsumer) {
		newLocalVersionCallbacks.add(versionConsumer);
	}

	@Override
	public void registerUpstreamVersionListener(final Consumer<String> versionConsumer) {
		newUpstreamVersionCallbacks.add(versionConsumer);
	}
	//
	// protected Triple<String, String, String> getServiceAuth() {
	// return auth;
	// }

	// protected OkHttpClient buildClientWithBasicAuth() {
	// Triple<String, String, String> serviceAuth = getServiceAuth();
	// if (serviceAuth != null) {
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
	//
	// private int responseCount(Response response) {
	// int result = 1;
	// while ((response = response.priorResponse()) != null) {
	// result++;
	// }
	// return result;
	// }
	//
	// protected com.squareup.okhttp.Authenticator getAuthenticator() {
	// return new com.squareup.okhttp.Authenticator() {
	// @Override
	// public com.squareup.okhttp.Request authenticate(Proxy proxy, com.squareup.okhttp.Response response) throws IOException {
	// String credential = Credentials.basic("scott", "tiger");
	// return response.request().newBuilder().header("Authorization", credential).build();
	// }
	//
	// @Override
	// public com.squareup.okhttp.Request authenticateProxy(Proxy proxy, com.squareup.okhttp.Response response) throws IOException {
	// return null;
	// }
	// };
	// }
	protected boolean canWaitForNewLocalVersion() {
		return false;
	}

	protected boolean canWaitForNewUpstreamVersion() {
		return false;
	}

	protected abstract void newUpstreamURL();

	public boolean hasUpstream() {
		return upstreamUrl != null && !upstreamUrl.isEmpty();
	}

	@Override
	public boolean publishVersion(final String version) throws Exception {

		OkHttpClient localClient = new okhttp3.OkHttpClient();
		OkHttpClient upstreamClient = new okhttp3.OkHttpClient();

		// Pull down the version data

		final Request pullRequest = new Request.Builder().url(backendUrl + getSyncVersionEndpoint() + version).get().build();
		final Response pullResponse = localClient.newCall(pullRequest).execute();
		if (!pullResponse.isSuccessful()) {
			return false;
		}
		final String json = pullResponse.body().string();
		//
		// // Post the data to local repo

		String credential = Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword());

		final RequestBody body = RequestBody.create(JSON, json);
		final Request postRequest = createUpstreamRequestBuilder(upstreamUrl + getSyncVersionEndpoint()) //
				.post(body).build();
		final Response postResponse = upstreamClient.newCall(postRequest).execute();
		return postResponse.isSuccessful();
	}

	protected Request.Builder createUpstreamRequestBuilder(String url) {
		String credential = Credentials.basic(UpstreamUrlProvider.INSTANCE.getUsername(), UpstreamUrlProvider.INSTANCE.getPassword());
		return new Request.Builder() //
				.url(url) //
				.addHeader("Authorization", credential);
	}

	@Override
	public boolean syncUpstreamVersion(final String version) throws Exception {

		OkHttpClient localClient = new okhttp3.OkHttpClient();
		OkHttpClient upstreamClient = new okhttp3.OkHttpClient();

		// Pull down the version data
		final Request pullRequest = createUpstreamRequestBuilder(upstreamUrl + getSyncVersionEndpoint() + version) //
				.get().build();

		final Response pullResponse = upstreamClient.newCall(pullRequest).execute();
		if (!pullResponse.isSuccessful()) {
			return false;
		}
		final String json = pullResponse.body().string();

		// Post the data to local repo
		final RequestBody body = RequestBody.create(JSON, json);
		final Request postRequest = new Request.Builder().url(backendUrl + getSyncVersionEndpoint()).post(body).build();
		final Response postResponse = localClient.newCall(postRequest).execute();
		// TODO: Check return code etc

		return postResponse.isSuccessful();
	}

	protected CompletableFuture<Boolean> waitForNewLocalVersion() {
		return notifyOnNewVersion(false);
	}

	protected CompletableFuture<Boolean> waitForNewUpstreamVersion() {
		return notifyOnNewVersion(true);
	}

	protected CompletableFuture<Boolean> notifyOnNewVersion(boolean upstream) {
		String baseUrl = upstream ? UpstreamUrlProvider.INSTANCE.getBaseURL() : BackEndUrlProvider.INSTANCE.getUrl() ;

		if (baseUrl == null || baseUrl.isEmpty()) {
			return null;
		}

		OkHttpClient longPollingClient = new OkHttpClient().newBuilder() //
				.readTimeout(Integer.MAX_VALUE, TimeUnit.MILLISECONDS) //
				.build();

		String url = baseUrl + getVersionNotificationEndpoint();
		LOG.debug("Calling url {}", url);
		CompletableFuture<Boolean> completableFuture = CompletableFuture.supplyAsync(() -> {
			Request.Builder requestBuilder = upstream ? createUpstreamRequestBuilder(url)
					: new Request.Builder() //
							.url(url);
			Request request = requestBuilder.build();
			Response response;
			try {
				response = longPollingClient.newCall(request).execute();
				if (response.isSuccessful()) {
					// Version newVersion = new ObjectMapper().readValue(response.body().byteStream(), Version.class);
					return Boolean.TRUE;
				}
				return Boolean.FALSE;
			} catch (IOException e) {
				LOG.error("Error waiting for new version");
				throw new RuntimeException("Error waiting for new version");
			}
		});
		return completableFuture;
	}

	protected abstract String getVersionNotificationEndpoint();

	protected abstract String getSyncVersionEndpoint();
}