/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.hub;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.widgets.Shell;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.hub.auth.AuthenticationManager;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.hub.info.DatahubInformation;
import com.mmxlabs.hub.preferences.DataHubPreferenceConstants;
import com.mmxlabs.hub.preferences.TLSPreferenceConstants;

/**
 * This class manages connections to the data hub.
 * <p>
 * The base URL is loaded from the preference store, and authentication info requested via the UI for the associated web domain. Call UpstreamUrlProvider.INSTANCE.getBaseURL() to get the base URL for
 * data hub requests.
 * <p>
 * N.B. {@link #getBaseUrlIfAvailable()} will not work until the UpstreamUrlProvider has finished initialising, which may not happen immediately at program start, or if the connection goes down later.
 *
 */
public class UpstreamUrlProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpstreamUrlProvider.class);

	// initialise http client with small connection timeout
	private static final int CONNECTION_TIMEOUT_IN_SECONDS = 5;
	private static final int READ_TIMEOUT_IN_SECONDS = 1;
	private static final int WRITE_TIMEOUT_IN_SECONDS = 1;

	// Avoids repeated error reporting
	private static final ConcurrentHashMap<String, Object> reportedError = new ConcurrentHashMap<>();

	public static final UpstreamUrlProvider INSTANCE = new UpstreamUrlProvider();

	private final IPreferenceStore preferenceStore;
	private final Set<IUpstreamDetailChangedListener> detailListeners = new LinkedHashSet<>();
	private final Set<IUpstreamServiceChangedListener> workspaceListeners = new LinkedHashSet<>();

	private boolean baseCaseServiceEnabled = false;
	private boolean teamServiceEnabled = false;
	private boolean forceBasicAuthenticationEnabled = false;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private boolean connectionValid = false;
	private DatahubInformation currentInformation = null;
	/**
	 * If there is no reported information from the upstream hub, fallback to "basic" auth mode.
	 */
	private static final DatahubInformation fallackInformation = new DatahubInformation("legacy", "basic");

	private String currentBaseURL;

	// DataHub endpoints
	public static final String INFORMATION_ENDPOINT = "/info";
	public static final String URI_AFTER_SUCCESSFULL_AUTHENTICATION = "/authenticated";
	public static final String OAUTH_LOGIN_PATH = "/oauth2/authorization/azure";
	public static final String BASIC_LOGIN_PATH = "/api/login";
	public static final String BASIC_LOGIN_FORM_PATH = "/login/local";
	public static final String USER_PERMISSIONS_ENDPOINT = "/user/permissions";
	public static final String HOME_PATH = "/ui/";
	public static final String LOGOUT_PATH = "/logout";

	AuthenticationManager authenticationManager = AuthenticationManager.getInstance();

	private UpstreamUrlProvider() {
		if (DataHubActivator.getDefault() != null) {
			preferenceStore = DataHubActivator.getDefault().getPreferenceStore();
		} else {
			preferenceStore = new PreferenceStore();
		}
		preferenceStore.addPropertyChangeListener(listener);

		baseCaseServiceEnabled = Boolean.TRUE.equals(preferenceStore.getBoolean(DataHubPreferenceConstants.P_ENABLE_BASE_CASE_SERVICE_KEY));
		teamServiceEnabled = Boolean.TRUE.equals(preferenceStore.getBoolean(DataHubPreferenceConstants.P_ENABLE_TEAM_SERVICE_KEY));
		forceBasicAuthenticationEnabled = Boolean.TRUE.equals(preferenceStore.getBoolean(DataHubPreferenceConstants.P_FORCE_BASIC_AUTH));
		authenticationManager.setForceBasicAuthentication(forceBasicAuthenticationEnabled);

		HttpClientUtil.setDisableSSLHostnameCheck(Boolean.TRUE.equals(preferenceStore.getBoolean(DataHubPreferenceConstants.P_DISABLE_SSL_HOSTNAME_CHECK)));

		HttpClientUtil.setUseJavaTrustStore(Boolean.TRUE.equals(preferenceStore.getBoolean(TLSPreferenceConstants.P_TLS_USE_JAVA_TRUSTSTORE)));
		HttpClientUtil.setUseWindowsTrustStore(Boolean.TRUE.equals(preferenceStore.getBoolean(TLSPreferenceConstants.P_TLS_USE_WINDOWS_TRUSTSTORE)));

		// Any changes to the http client config will clear the client cache
		HttpClientUtil.addInvalidationListener(cacheWithAuth::invalidateAll);
		HttpClientUtil.addInvalidationListener(cacheWithoutAuth::invalidateAll);
	}

	public void start() {
		// Schedule a "is alive" check every minute....
		scheduler.scheduleAtFixedRate(this::isUpstreamAvailable, 1, 1, TimeUnit.MINUTES);
		// ... and do one now as first invocation can be a bit delayed.
		// Defer to avoid potential deadlock
		ForkJoinPool.commonPool().execute(this::isUpstreamAvailable);
	}

	@Override
	protected void finalize() throws Throwable {
		scheduler.shutdown();
		super.finalize();
	}

	private final IPropertyChangeListener listener = event -> {
		switch (event.getProperty()) {
		case DataHubPreferenceConstants.P_DATAHUB_URL_KEY:
			isUpstreamAvailable();
			fireChangedListeners();
			break;
		case DataHubPreferenceConstants.P_DISABLE_SSL_HOSTNAME_CHECK:
			HttpClientUtil.setDisableSSLHostnameCheck(Boolean.TRUE.equals(event.getNewValue()));
			isUpstreamAvailable();
			fireChangedListeners();
			break;
		case DataHubPreferenceConstants.P_ENABLE_BASE_CASE_SERVICE_KEY:
			baseCaseServiceEnabled = Boolean.TRUE.equals(event.getNewValue());
			fireServiceChangedListeners(IUpstreamServiceChangedListener.Service.BaseCaseWorkspace);
			break;
		case DataHubPreferenceConstants.P_ENABLE_TEAM_SERVICE_KEY:
			teamServiceEnabled = Boolean.TRUE.equals(event.getNewValue());
			fireServiceChangedListeners(IUpstreamServiceChangedListener.Service.TeamWorkspace);
			break;
		case DataHubPreferenceConstants.P_FORCE_BASIC_AUTH:
			forceBasicAuthenticationEnabled = Boolean.TRUE.equals(event.getNewValue());
			authenticationManager.setForceBasicAuthentication(forceBasicAuthenticationEnabled);
			fireChangedListeners();
			break;
		case TLSPreferenceConstants.P_TLS_USE_JAVA_TRUSTSTORE:
			HttpClientUtil.setUseJavaTrustStore(Boolean.TRUE.equals(event.getNewValue()));
			isUpstreamAvailable();
			fireChangedListeners();
			break;
		case TLSPreferenceConstants.P_TLS_USE_WINDOWS_TRUSTSTORE:
			HttpClientUtil.setUseWindowsTrustStore(Boolean.TRUE.equals(event.getNewValue()));
			isUpstreamAvailable();
			fireChangedListeners();
			break;
		default:
			break;
		}
	};

	/**
	 * Returns the current URL for the upstream service, or an empty string if no service is currently available.
	 * <p>
	 * <b>N.B. The caller of this method is responsible for guarding against possible failure by checking the return value.</b>
	 * 
	 * @return
	 */
	public String getBaseUrlIfAvailable() {
		String url = null;
		if (connectionValid) {
			url = currentBaseURL;
		}
		if (url != null) {
			return url;
		}
		return "";
	}

	public void registerDetailsChangedLister(final IUpstreamDetailChangedListener listener) {
		detailListeners.add(listener);
	}

	public void deregisterDetailsChangedLister(final IUpstreamDetailChangedListener listener) {
		detailListeners.remove(listener);
	}

	public void registerServiceChangedLister(final IUpstreamServiceChangedListener listener) {
		workspaceListeners.add(listener);
	}

	public void deregisterServiceChangedLister(final IUpstreamServiceChangedListener listener) {
		workspaceListeners.remove(listener);
	}

	public enum StateReason {
		UNKNOWN_ERROR, EMPTY_URL, INVALID_URL, HUB_ONLINE
	}

	public static class OnlineState {
		private final StateReason reason;
		private final String message;
		private final Exception exception;

		public OnlineState(final StateReason reason, final String message, final Exception exception) {
			this.reason = reason;
			this.message = message;
			this.exception = exception;
		}

		public static @NonNull OnlineState emptyURL() {
			return new OnlineState(StateReason.EMPTY_URL, null, null);
		}

		public static @NonNull OnlineState invalidURL() {
			return new OnlineState(StateReason.INVALID_URL, null, null);
		}

		public static @NonNull OnlineState online() {
			return new OnlineState(StateReason.HUB_ONLINE, null, null);
		}

		public static @NonNull OnlineState error(final String msg, final Exception e) {
			return new OnlineState(StateReason.UNKNOWN_ERROR, msg, e);
		}

		public StateReason getReason() {
			return reason;
		}

		public String getMessage() {
			return message;
		}

		public Exception getException() {
			return exception;
		}
	}

	public synchronized OnlineState isUpstreamAvailable() {
		return isUpstreamAvailable((Shell) null);
	}

	/*
	 * Gets the Datahub URL from preferences and calls testUpstreamAvailability This pings the Datahub
	 */
	public synchronized void updateOnlineStatus() {

		try {

			String baseUrl = preferenceStore.getString(DataHubPreferenceConstants.P_DATAHUB_URL_KEY);
			if (baseUrl == null || baseUrl.isEmpty()) {
				DataHubServiceProvider.getInstance().setOnlineState(false);
				return;
			}

			baseUrl = stripTrailingForwardSlash(baseUrl);

			if (testUpstreamAvailability(baseUrl).getReason() != StateReason.HUB_ONLINE) {
				DataHubServiceProvider.getInstance().setOnlineState(false);
				return;
			}

			final Optional<DatahubInformation> upstreamInformation = getUpstreamInformation(baseUrl);
			if (upstreamInformation.isPresent()) {
				currentInformation = upstreamInformation.get();
			} else {
				currentInformation = fallackInformation;
			}

			// Update current information scheme
			DataHubServiceProvider.getInstance().setDatahubInformation(currentInformation);

			DataHubServiceProvider.getInstance().setOnlineState(true);
			authenticationManager.updateAuthenticationScheme(baseUrl, currentInformation.getAuthenticationScheme());
		} catch (final Exception e) {
			// Ignore exceptions
		}
	}

	/*
	 * Gets the Datahub URL from preferences and calls testUpstreamAvailability This pings the Datahub
	 */
	public synchronized OnlineState isUpstreamAvailable(@Nullable final Shell optionalShell) {

		boolean valid = false;
		try {

			String baseUrl = preferenceStore.getString(DataHubPreferenceConstants.P_DATAHUB_URL_KEY);
			if (baseUrl == null || baseUrl.isEmpty()) {
				return OnlineState.emptyURL();
			}

			baseUrl = stripTrailingForwardSlash(baseUrl);

			{
				final OnlineState state = testUpstreamAvailability(baseUrl);
				if (state.getReason() != StateReason.HUB_ONLINE) {
					DataHubServiceProvider.getInstance().setOnlineState(false);
					return state;
				}
			}

			final Optional<DatahubInformation> upstreamInformation = getUpstreamInformation(baseUrl);
			if (upstreamInformation.isPresent()) {
				currentInformation = upstreamInformation.get();
			} else {
				currentInformation = fallackInformation;
			}
			// Update current information scheme
			DataHubServiceProvider.getInstance().setDatahubInformation(currentInformation);

			DataHubServiceProvider.getInstance().setOnlineState(true);

			authenticationManager.updateAuthenticationScheme(baseUrl, currentInformation.getAuthenticationScheme());

			DataHubServiceProvider.getInstance().setLoggedInState(authenticationManager.isAuthenticated());

			valid = true;
			currentBaseURL = baseUrl;
			connectionValid = true;

			return OnlineState.online();
		} catch (final NullPointerException npe) {
			LOGGER.error("Failed to get url from secure preferences: " + npe.getMessage());
			return OnlineState.error("Unknown error", npe);
		} catch (final Exception e) {
			LOGGER.error(e.getMessage());
			return OnlineState.error("Unknown error", e);
		} finally {
			// Set state to invalid if we didn't complete the checks successfully
			if (!valid) {
				connectionValid = false;
				currentBaseURL = null;
			}
		}
	}

	public static String stripTrailingForwardSlash(String baseUrl) {
		if (baseUrl.charAt(baseUrl.length() - 1) == '/') {
			baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
		}
		return baseUrl;
	}

	public @NonNull OnlineState testUpstreamAvailability(final String url) {

		if (url == null || url.isEmpty()) {
			return OnlineState.emptyURL();
		}
		if (!url.startsWith("http") || url.charAt(url.length() - 1) == '/') {
			return OnlineState.invalidURL();
		}

		final HttpGet pingRequest = new HttpGet(url + "/ping");
		final var p = cacheWithoutAuth.getUnchecked(URIUtils.extractHost(pingRequest.getURI()));
		final CloseableHttpClient httpClient = p.getFirst();
		try (final var pingResponse = httpClient.execute(pingRequest)) {
			final int reponseCode = pingResponse.getStatusLine().getStatusCode();
			if (HttpClientUtil.isSuccessful(reponseCode)) {
				// Clear any logged errors
				reportedError.remove(url);
				return OnlineState.online();
			} else {
				// Check for specific error codes
				if (reponseCode == 502) {
					return OnlineState.error("Error finding Data Hub endpoint - bad gateway", null);
				}
			}
		} catch (final UnknownHostException e) {
			if (!reportedError.containsKey(url)) {
				reportedError.put(url, new Object());
				LOGGER.error("Error finding Data Hub host", e);
			}
			return OnlineState.error("Error finding Data Hub host", e);
		} catch (final SSLPeerUnverifiedException e) {
			if (!reportedError.containsKey(url)) {
				reportedError.put(url, new Object());
				LOGGER.error("Error SSL server hostname mismatch", e);
			}
			return OnlineState.error("Error SSL server hostname mismatch", e);
		} catch (final SSLException e) {
			if (!reportedError.containsKey(url)) {
				reportedError.put(url, new Object());
				LOGGER.error("Error validating server SSL certificates", e);
			}
			return OnlineState.error("Error validating server SSL certificates", e);

		} catch (final SocketTimeoutException e) {
			if (!reportedError.containsKey(url)) {
				reportedError.put(url, new Object());
				LOGGER.warn("Connection attempt to upstream server timed out", e);
			}
			return OnlineState.error("Connection attempt to upstream server timed out", e);

		} catch (final IOException e) {
			if (!reportedError.containsKey(url)) {
				reportedError.put(url, new Object());
				LOGGER.error("Error reaching upstream server", e);
			}
			return OnlineState.error("Error reaching upstream server", e);
		}

		// Clear any logged errors
		reportedError.remove(url);

		return OnlineState.error("Error finding Data Hub endpoint", null);
	}

	public boolean isAvailable() {
		final String url = getBaseUrlIfAvailable();
		return (url != null && !url.isEmpty());
	}

	private void fireChangedListeners() {
		for (final IUpstreamDetailChangedListener l : detailListeners) {
			try {
				l.changed();
			} catch (final Exception e) {
				// Ignore
			}
		}
	}

	private void fireServiceChangedListeners(final IUpstreamServiceChangedListener.Service serviceType) {
		for (final IUpstreamServiceChangedListener l : workspaceListeners) {
			try {
				l.changed(serviceType);
			} catch (final Exception e) {
				// Ignore
			}
		}
	}

	public boolean isBaseCaseServiceEnabled() {
		return baseCaseServiceEnabled;
	}

	public boolean isTeamServiceEnabled() {
		return teamServiceEnabled;
	}

	private Optional<DatahubInformation> getUpstreamInformation(String baseUrl) throws Exception {
		Optional<DatahubInformation> datahubInformation = Optional.empty();

		if (baseUrl == null || baseUrl.isEmpty()) {
			return datahubInformation;
		}
		baseUrl = stripTrailingForwardSlash(baseUrl);
		final URI url = new URI(baseUrl + INFORMATION_ENDPOINT);

		final var p = cacheWithoutAuth.getUnchecked(URIUtils.extractHost(url));
		if (p == null) {
			return datahubInformation;
		}
		final var client = p.getFirst();
		final HttpGet request = new HttpGet(url);
		try (var response = client.execute(request)) {
			final int statusCode = response.getStatusLine().getStatusCode();
			if (HttpClientUtil.isSuccessful(statusCode)) {
				// bind response to pojo
				final ObjectMapper objectMapper = new ObjectMapper();
				datahubInformation = Optional.of(objectMapper.readValue(response.getEntity().getContent(), DatahubInformation.class));
			}
		}

		if (datahubInformation.isPresent() && datahubInformation.get() != null) {
			return datahubInformation;
		} else {
			return Optional.empty();
		}
	}

	private final LoadingCache<HttpHost, Pair<CloseableHttpClient, BasicCookieStore>> cacheWithoutAuth = CacheBuilder.newBuilder() //

			.removalListener(new RemovalListener<HttpHost, Pair<CloseableHttpClient, BasicCookieStore>>() {
				@Override
				public void onRemoval(final RemovalNotification<HttpHost, Pair<CloseableHttpClient, BasicCookieStore>> notification) {
					try {
						notification.getValue().getFirst().close();
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
			})
			.build(new CacheLoader<HttpHost, Pair<CloseableHttpClient, BasicCookieStore>>() {

				@Override
				public Pair<CloseableHttpClient, BasicCookieStore> load(final HttpHost baseUrl) throws Exception {
					final HttpClientBuilder builder = HttpClientUtil.createBasicHttpClient(baseUrl, false);

					final BasicCookieStore cookieStore = new BasicCookieStore();
					builder.setDefaultCookieStore(cookieStore);

					final CloseableHttpClient client = builder.build();

					return Pair.of(client, cookieStore);
				}

			});

	private final LoadingCache<HttpHost, Pair<CloseableHttpClient, BasicCookieStore>> cacheWithAuth = CacheBuilder.newBuilder() //

			.removalListener(new RemovalListener<HttpHost, Pair<CloseableHttpClient, BasicCookieStore>>() {
				@Override
				public void onRemoval(final RemovalNotification<HttpHost, Pair<CloseableHttpClient, BasicCookieStore>> notification) {
					try {
						notification.getValue().getFirst().close();
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
			})
			.build(new CacheLoader<HttpHost, Pair<CloseableHttpClient, BasicCookieStore>>() {

				@Override
				public Pair<CloseableHttpClient, BasicCookieStore> load(final HttpHost baseUrl) throws Exception {
					final HttpClientBuilder builder = HttpClientUtil.createBasicHttpClient(baseUrl, false);

					final BasicCookieStore cookieStore = new BasicCookieStore();
					builder.setDefaultCookieStore(cookieStore);

					// New Data Hubs may have CSRF tokens enabled.
					// This requires mutating calls (i.e. POST or DELETE) to send the CSRF token in the header.
					// The token could be stored in a cookie or the session state on the server.

					// In either case, calling the /csrf endpoint will return a JSON object with the token to use and it may set the cookie.

					final URI requestURI = new URI(baseUrl + "/csrf");
					final HttpGet request = new HttpGet(requestURI);
					// The endpoint may be protected, so add in auth tokens
					authenticationManager.addAuthToRequest(request, cookieStore);
					// If we used basic auth, then include the header for future requests.
					builder.setDefaultHeaders(Lists.newArrayList(request.getAllHeaders()));

					// Create an interceptor to add the CSRF token to all request used by the final http client.
					final Header[] csrf = new Header[1];
					builder.addInterceptorLast(new HttpRequestInterceptor() {

						@Override
						public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
							// Add the CSRF token if available
							if (csrf[0] != null) {
								request.addHeader(csrf[0]);
							}
						}
					});

					// Build our client
					final CloseableHttpClient client = builder.build();

					// Issue our first request to lookup the token, if the endpoint is there.
					// If the endpoint is not there, then this will fail and we can ignore.
					try (var response = client.execute(request)) {
						// We found the endpoint!
						if (response.getStatusLine().getStatusCode() == 200) {
							try {
								final String value = EntityUtils.toString(response.getEntity());
								final JSONObject jsonObject = (JSONObject) new JSONTokener(value).nextValue();
								// This may be the XOR'd token. If the backend doesn't have the XOR'd part fully enabled, then the CSRF will fail.
								// The alternative would be to grab the token from the cookie (if used) as this will not be XOR'd.
								final String s = jsonObject.getString("token");
								csrf[0] = new BasicHeader(jsonObject.getString("headerName"), s);
							} catch (final Exception e) {
								// Error parsing the CSRF body
								e.printStackTrace();
							}
						}

					}

					return Pair.of(client, cookieStore);
				}

			});

	/**
	 * A small class to hold the {@link HttpClient} and the {@link BasicClientCookie} (as this cannot be accessed from the client).
	 * 
	 * @author Simon Goodall
	 *
	 * @param <T>
	 */
	public record HttpClientRecord<T extends HttpRequestBase> (CloseableHttpClient client, // The client
			T request, // The constructed request object
			BasicCookieStore cookieStore // The cookie store (e.g containing the session token or csrf tokens)
	) {

		public CloseableHttpResponse execute() throws IOException {
			return client.execute(request);
		}

		public <U> @Nullable U execute(final ResponseHandler<U> responseHandler) throws IOException {
			return execute(null, responseHandler);
		}

		public <U> @Nullable U execute(@Nullable final Consumer<T> requestCusomiser, final ResponseHandler<U> responseHandler) throws IOException {
			if (requestCusomiser != null) {
				requestCusomiser.accept(request);
			}

			return client.execute(request, responseHandler);
		}
	}

	public <T extends HttpRequestBase> @Nullable HttpClientRecord<T> makeRequest(@NonNull final String uriFragment, @NonNull final Function<URI, T> func) {
		final String baseUrlIfAvailable = getBaseUrlIfAvailable();

		if (baseUrlIfAvailable.isEmpty()) {
			return null;
		}
		try {
			final URI requestURI = new URI(baseUrlIfAvailable + uriFragment);
			final Pair<CloseableHttpClient, BasicCookieStore> p = cacheWithAuth.getUnchecked(URIUtils.extractHost(requestURI));
			if (p != null) {
				final T request = func.apply(requestURI);
				return new HttpClientRecord<>(p.getFirst(), request, p.getSecond());
			}
		} catch (final URISyntaxException e) {
			return null;
		}
		return null;
	}
}
