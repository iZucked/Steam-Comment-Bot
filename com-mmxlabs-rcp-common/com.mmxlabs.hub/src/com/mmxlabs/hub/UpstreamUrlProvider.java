/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.hub;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.hub.auth.AuthenticationManager;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.hub.info.DatahubInformation;
import com.mmxlabs.hub.preferences.DataHubPreferenceConstants;
import com.mmxlabs.hub.preferences.DataHubPreferencePage;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

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

	private final OkHttpClient httpClient = HttpClientUtil.basicBuilder().build();

	// Avoids repeated error reporting
	private static final ConcurrentHashMap<String, Object> reportedError = new ConcurrentHashMap<>();

	public static final UpstreamUrlProvider INSTANCE = new UpstreamUrlProvider();

	private final IPreferenceStore preferenceStore;
	private final Set<IUpstreamDetailChangedListener> detailListeners = new HashSet<>();
	private final Set<IUpstreamServiceChangedListener> workspaceListeners = new HashSet<>();

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
	public static final String TOKEN_ENDPOINT = "/token";
	public static final String URI_AFTER_SUCCESSFULL_AUTHENTICATION = "/authenticated";
	public static final String OAUTH_LOGIN_PATH = "/oauth2/authorization/azure";
	public static final String BASIC_LOGIN_PATH = "/api/login";
	public static final String USER_PERMISSIONS_ENDPOINT = "/user/permissions";
	public static final String HOME_PATH = "/ui/#/dashboard";
	public static final String LOGOUT_PATH = "/logout";

	AuthenticationManager authenticationManager = AuthenticationManager.getInstance();

	// this bool ensures that only one authentication shell is launched
	private final AtomicBoolean authenticationDialogIsOpen = new AtomicBoolean(false);

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
			authenticationManager.logout((Shell) null);
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
			DataHubPreferencePage.setButtonText();
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

	// /**
	// * Returns the current URL for the upstream service, trying repeatedly if
	// * necessary (with 50ms pauses between each).
	// * <p>
	// * After maxRetries retries, the method gives up and returns an empty string.
	// *
	// * @param maxRetries
	// * @return
	// */
	// public String getBaseUrl(int maxRetries) {
	// String result = getBaseUrlIfAvailable();
	//
	// while ((result == null || result.isEmpty()) && maxRetries-- > 0) {
	// // wait 50ms
	// try {
	// Thread.sleep(50);
	// } catch (final InterruptedException e) {
	// // Restore the interrupted status if something happens while asleep
	// Thread.currentThread().interrupt();
	// }
	// // try again
	// result = getBaseUrlIfAvailable();
	// }
	//
	// return result;
	// }

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

	public synchronized boolean isUpstreamAvailable() {
		return isUpstreamAvailable((Shell) null);
	}

	/*
	 * Gets the Datahub URL from preferences and calls testUpstreamAvailability This pings the Datahub
	 */
	public synchronized boolean isUpstreamAvailable(@Nullable final Shell optionalShell) {

		boolean valid = false;
		try {

			String baseUrl = preferenceStore.getString(DataHubPreferenceConstants.P_DATAHUB_URL_KEY);
			if (baseUrl == null || baseUrl.isEmpty()) {
				return false;
			}

			baseUrl = stripTrailingForwardSlash(baseUrl);

			if (!testUpstreamAvailability(baseUrl)) {
				DataHubServiceProvider.getInstance().setOnlineState(false);
				return false;
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

			if (authenticationDialogIsOpen.compareAndSet(false, true) && !authenticationManager.isAuthenticated()) {
				authenticationManager.run(optionalShell);
				authenticationDialogIsOpen.compareAndSet(true, false);
			}

			if (!authenticationManager.isAuthenticated()) {
				DataHubServiceProvider.getInstance().setLoggedInState(false);
				return false;
			}

			valid = true;
			currentBaseURL = baseUrl;
			connectionValid = true;

			DataHubServiceProvider.getInstance().setLoggedInState(true);
		} catch (final Exception e) {
			// Ignore...?
			e.printStackTrace();
		} finally {
			// Set state to invalid if we didn't complete the checks successfully
			if (!valid) {
				connectionValid = false;
				currentBaseURL = null;
			}
		}
		return true;
	}

	public static String stripTrailingForwardSlash(String baseUrl) {
		if (baseUrl.charAt(baseUrl.length() - 1) == '/') {
			baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
		}
		return baseUrl;
	}

	public boolean testUpstreamAvailability(final String url) {

		if (url == null || url.isEmpty() || !url.startsWith("http") || url.charAt(url.length() - 1) == '/') {
			return false;
		}

		final Request pingRequest = new Request.Builder().url(url + "/ping").get().build();

		try (final Response pingResponse = httpClient.newCall(pingRequest).execute()) {
			if (pingResponse.isSuccessful()) {
				return true;
			}
		} catch (final UnknownHostException e) {
			if (!reportedError.containsKey(url)) {
				reportedError.put(url, new Object());
				LOGGER.error("Error finding Data Hub host", e);
			}
			return false;
		} catch (final SSLPeerUnverifiedException e) {
			if (!reportedError.containsKey(url)) {
				reportedError.put(url, new Object());
				LOGGER.error("Error SSL server hostname mismatch", e);
			}
			return false;
		} catch (final SSLException e) {
			if (!reportedError.containsKey(url)) {
				reportedError.put(url, new Object());
				LOGGER.error("Error validating server SSL certificates", e);
			}
			return false;
		} catch (final SocketTimeoutException e) {
			if (!reportedError.containsKey(url)) {
				reportedError.put(url, new Object());
				LOGGER.warn("Connection attempt to upstream server timed out", e);
			}
			return false;
		} catch (final IOException e) {
			if (!reportedError.containsKey(url)) {
				reportedError.put(url, new Object());
				LOGGER.error("Error reaching upstream server", e);
			}
			return false;
		}

		// Clear any logged errors
		reportedError.remove(url);

		return true;
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

	private Optional<DatahubInformation> getUpstreamInformation(String baseUrl) throws IOException {
		Optional<DatahubInformation> datahubInformation = Optional.empty();

		if (baseUrl == null || baseUrl.isEmpty()) {
			return datahubInformation;
		}
		baseUrl = stripTrailingForwardSlash(baseUrl);
		final String url = baseUrl + INFORMATION_ENDPOINT;

		final Request request = new Request.Builder().url(url).build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (response.isSuccessful()) {
				// bind response to pojo
				final ObjectMapper objectMapper = new ObjectMapper();
				datahubInformation = Optional.of(objectMapper.readValue(response.body().string(), DatahubInformation.class));
			}
		}

		if (datahubInformation.isPresent() && datahubInformation.get() != null) {
			return datahubInformation;
		} else {
			return Optional.empty();
		}
	}

	public @Nullable Builder makeRequestBuilder(@NonNull final String urlPath) {

		final String baseUrlIfAvailable = getBaseUrlIfAvailable();
		if (baseUrlIfAvailable.isEmpty()) {
			return null;
		}

		return authenticationManager.buildRequest().url(baseUrlIfAvailable + urlPath);
	}

	/**
	 * Returns true if there is a String representing the HUB url present. Only intended to be used by the application startup check
	 */
	public boolean hasADataHubURL() {
		final String baseUrl = preferenceStore.getString(DataHubPreferenceConstants.P_DATAHUB_URL_KEY);
		if (baseUrl == null || baseUrl.isEmpty()) {
			return false;
		}
		if (baseUrl.startsWith("http")) {
			return true;
		}
		return false;
	}
}
