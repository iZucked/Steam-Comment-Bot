/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.server;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.server.dialogs.AuthDetailsPromptDialog;
import com.mmxlabs.lngdataserver.server.preferences.StandardDateRepositoryPreferenceConstants;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.user.IUserNameProvider;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This class manages connections to the data hub, including authentication.
 * <p>
 * The base URL is loaded from the preference store, and authentication info requested via the UI for the associated web domain.
 * Call UpstreamUrlProvider.INSTANCE.getBaseURL() to get the base URL for data hub requests.
 * <p>
 * N.B. {@link #getBaseUrlIfAvailable()} will not work until the UpstreamUrlProvider has finished initialising, which may not happen immediately at program start, or if the 
 * connection goes down later.
 * 
 */
public class UpstreamUrlProvider implements IUserNameProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpstreamUrlProvider.class);

	private final OkHttpClient CLIENT = HttpClientUtil.basicBuilder().build();

	// Avoids repeated error reporting
	private static final ConcurrentHashMap<String, Object> reportedError = new ConcurrentHashMap<>();

	public static final UpstreamUrlProvider INSTANCE = new UpstreamUrlProvider();

	private final IPreferenceStore preferenceStore;
	private final Set<IUpstreamDetailChangedListener> detailListeners = new HashSet<>();
	private final Set<IUpstreamServiceChangedListener> workspaceListeners = new HashSet<>();

	private boolean hasDetails = false;
	private final AtomicBoolean dialogOpen = new AtomicBoolean(false);

	private boolean baseCaseServiceEnabled = false;
	private boolean teamServiceEnabled = false;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private boolean connectionValid = false;
	private String currentBaseURL;

	private UpstreamUrlProvider() {
		if (DataServerActivator.getDefault() != null) {
			preferenceStore = DataServerActivator.getDefault().getPreferenceStore();
		} else {
			preferenceStore = new PreferenceStore();
		}
		preferenceStore.addPropertyChangeListener(listener);

		baseCaseServiceEnabled = Boolean.TRUE.equals(preferenceStore.getBoolean(StandardDateRepositoryPreferenceConstants.P_ENABLE_BASE_CASE_SERVICE_KEY));
		teamServiceEnabled = Boolean.TRUE.equals(preferenceStore.getBoolean(StandardDateRepositoryPreferenceConstants.P_ENABLE_TEAM_SERVICE_KEY));
		HttpClientUtil.setDisableSSLHostnameCheck(Boolean.TRUE.equals(preferenceStore.getBoolean(StandardDateRepositoryPreferenceConstants.P_DISABLE_SSL_HOSTNAME_CHECK)));

		// Schedule a "is alive" check every minute....
		scheduler.scheduleAtFixedRate(this::testUpstreamAvailability, 1, 1, TimeUnit.MINUTES);
		// ... and do one now as first invocation can be a bit delayed.
		// Defer to avoid potential deadlock
		ForkJoinPool.commonPool().execute(this::testUpstreamAvailability);
	}

	@Override
	protected void finalize() throws Throwable {
		scheduler.shutdown();
		super.finalize();
	}

	private final IPropertyChangeListener listener = event -> {
		switch (event.getProperty()) {
		case StandardDateRepositoryPreferenceConstants.P_URL_KEY:
			testUpstreamAvailability();
			fireChangedListeners();
			break;
		case StandardDateRepositoryPreferenceConstants.P_DISABLE_SSL_HOSTNAME_CHECK:
			HttpClientUtil.setDisableSSLHostnameCheck(Boolean.TRUE.equals(event.getNewValue()));
			testUpstreamAvailability();
			fireChangedListeners();
			break;
		case StandardDateRepositoryPreferenceConstants.P_ENABLE_BASE_CASE_SERVICE_KEY:
			baseCaseServiceEnabled = Boolean.TRUE.equals(event.getNewValue());
			fireServiceChangedListeners(IUpstreamServiceChangedListener.Service.BaseCaseWorkspace);
			break;
		case StandardDateRepositoryPreferenceConstants.P_ENABLE_TEAM_SERVICE_KEY:
			teamServiceEnabled = Boolean.TRUE.equals(event.getNewValue());
			fireServiceChangedListeners(IUpstreamServiceChangedListener.Service.TeamWorkspace);
			break;
		default:
			break;
		}
	};

	protected String username;

	protected String password;

	/**
	 * Returns the current URL for the upstream service, or an empty string if no service is currently available.
	 * <p>
	 * <b>N.B. The caller of this method is responsible for guarding against possible failure by checking the return value.</b>
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
	
	/**
	 * Returns the current URL for the upstream service, trying repeatedly if necessary (with 50ms pauses between each).
	 * <p>
	 * After maxRetries retries, the method gives up and returns an empty string.
	 * @param maxRetries
	 * @return 
	 */
	public String getBaseUrl(int maxRetries) {
		String result = getBaseUrlIfAvailable();
		
		while ( (result == null || result.isEmpty()) && maxRetries-- > 0 ) {
			// wait 50ms
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
	             // Restore the interrupted status if something happens while asleep
	             Thread.currentThread().interrupt();
	        }
			// try again
			result = getBaseUrlIfAvailable();
		}
		
		return result;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
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

	public boolean checkCredentials(final String url, final String username, final String password) {

		if (username == null || username.isEmpty()) {
			return false;
		}

		if (password == null || password.isEmpty()) {
			return false;
		}

		final Request loginRequest = new Request.Builder() //
				.header("Authorization", Credentials.basic(username, password)) //
				.header("cache-control", "no-cache") //
				.url(url + "/api/login") //
				.build();

		try (Response loginResponse = CLIENT.newCall(loginRequest).execute()) {
			if (!loginResponse.isSuccessful()) {
				LOGGER.error("Invalid data hub credentials");
				return false;
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	public synchronized void testUpstreamAvailability() {
		testUpstreamAvailability((Display)null); 
	
	}
	public synchronized void testUpstreamAvailability(@Nullable Display optionalDisplay) {

		boolean valid = false;
		try {

			String baseUrl = preferenceStore.getString(StandardDateRepositoryPreferenceConstants.P_URL_KEY);
			if (baseUrl == null || baseUrl.isEmpty()) {

				return;
			}

			// Strip trailing forward slash if present
			if (baseUrl.charAt(baseUrl.length() - 1) == '/') {
				baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
			}
			final String url = baseUrl;
			if (!testUpstreamAvailability(url)) {

				return;
			}

			if (!checkCredentials(url, username, password)) {
				hasDetails = false;
			}

			if (!hasDetails) {
				final ISecurePreferences preferences = SecurePreferencesFactory.getDefault();
				if (preferences.nodeExists("upstream")) {
					final ISecurePreferences node = preferences.node("upstream");
					try {
						final String storedUsername = node.get("username", "n/a");
						final String storedPassword = node.get("password", "n/a");
						UpstreamUrlProvider.this.username = storedUsername;
						UpstreamUrlProvider.this.password = storedPassword;
						if (checkCredentials(url, username, password)) {
							hasDetails = true;
						}
					} catch (final StorageException e1) {
						e1.printStackTrace();
						return;
					}
				}
			}

			if (!hasDetails) {
				
				final Display display = optionalDisplay != null? optionalDisplay : RunnerHelper.getWorkbenchDisplay();
				if (display == null) {
					return;
				}

				if (dialogOpen.compareAndSet(false, true)) {
					display.syncExec(() -> {
						final AuthDetailsPromptDialog dialog = new AuthDetailsPromptDialog(display.getActiveShell());
						dialog.setUrl(url);
						dialog.setBlockOnOpen(true);
						if (dialog.open() == Window.OK) {
							UpstreamUrlProvider.this.username = dialog.getUsername();
							UpstreamUrlProvider.this.password = new String(dialog.getPassword());
							dialogOpen.compareAndSet(true, false);
						}
						hasDetails = true;
					});
					// Fire change listener without further blocking this call
					ForkJoinPool.commonPool().submit(this::fireChangedListeners);
				}
			}

			if (!hasDetails) {
				return;
			}
			valid = true;
			currentBaseURL = url;
			connectionValid = true;
		} finally {
			// Set state to invalid if we didn't complete the checks successfully
			if (!valid) {
				connectionValid = false;
				currentBaseURL = null;
			}
		}
	}

	public boolean testUpstreamAvailability(final String url) {

		if (url == null || url.isEmpty()) {
			return false;
		}

		if (!url.startsWith("http")) {
			return false;
		}

		if (url.charAt(url.length() - 1) == '/') {
			return false;
		}

		Request pingRequest = null;
		try {
			pingRequest = new Request.Builder().url(url + "/ping").get().build();
		} catch (final IllegalArgumentException e) {
			if (!reportedError.containsKey(url)) {
				reportedError.put(url, new Object());
				LOGGER.error("Invalid data hub URL {}", url);
			}
			return false;
		}

		if (pingRequest == null) {
			return false;
		}

		try (final Response pingResponse = CLIENT.newCall(pingRequest).execute()) {
			if (!pingResponse.isSuccessful()) {
				return false;
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

	/**
	 * Create a request builder with Authorization header.
	 */
	public Request.Builder makeRequest() {

		return new Request.Builder() //
				.header("Authorization", Credentials.basic(getUsername(), getPassword())) //
				.header("cache-control", "no-cache") //
		;
	}

}
