/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.server;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.lngdataserver.server.dialogs.AuthDetailsPromptDialog;
import com.mmxlabs.lngdataserver.server.internal.Activator;
import com.mmxlabs.lngdataserver.server.preferences.StandardDateRepositoryPreferenceConstants;
import com.mmxlabs.rcp.common.RunnerHelper;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpstreamUrlProvider {
	public static final UpstreamUrlProvider INSTANCE = new UpstreamUrlProvider();

	private final IPreferenceStore preferenceStore;
	private final Set<IUpstreamDetailChangedListener> detailListeners = new HashSet<>();
	private final Set<IUpstreamServiceChangedListener> workspaceListeners = new HashSet<>();

	private boolean hasDetails = false;
	private AtomicBoolean dialogOpen = new AtomicBoolean(false);

	private boolean baseCaseServiceEnabled = false;
	private boolean teamServiceEnabled = false;

	private UpstreamUrlProvider() {

		preferenceStore = Activator.getDefault().getPreferenceStore();
		preferenceStore.addPropertyChangeListener(listener);

		baseCaseServiceEnabled = Boolean.TRUE.equals(preferenceStore.getBoolean(StandardDateRepositoryPreferenceConstants.P_ENABLE_BASE_CASE_SERVICE_KEY));
		teamServiceEnabled = Boolean.TRUE.equals(preferenceStore.getBoolean(StandardDateRepositoryPreferenceConstants.P_ENABLE_TEAM_SERVICE_KEY));
	}

	private final IPropertyChangeListener listener = new IPropertyChangeListener() {
		@Override
		public void propertyChange(final PropertyChangeEvent event) {
			switch (event.getProperty()) {
			case StandardDateRepositoryPreferenceConstants.P_URL_KEY:
				// case StandardDateRepositoryPreferenceConstants.P_USERNAME_KEY:
				// case StandardDateRepositoryPreferenceConstants.P_PASSWORD_KEY:
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
			}
		}

	};

	protected String username;

	protected String password;

	public String getBaseURL() {
		String base_url = preferenceStore.getString(StandardDateRepositoryPreferenceConstants.P_URL_KEY);
		if (base_url == null || base_url.isEmpty()) {
			return "";
		}

		// Strip trailing forward slash if present
		if (base_url.charAt(base_url.length() - 1) == '/') {
			base_url = base_url.substring(0, base_url.length() - 1);
		}
		final String url = base_url;
		if (!testUpstreamAvailability(url)) {
			return "";
		}

		if (!checkCredentials(url, username, password)) {
			hasDetails = false;
		}

		if (!hasDetails) {
			ISecurePreferences preferences = SecurePreferencesFactory.getDefault();
			if (preferences.nodeExists("upstream")) {
				ISecurePreferences node = preferences.node("upstream");
				try {
					String storedUsername = node.get("username", "n/a");
					String storedPassword = node.get("password", "n/a");
					UpstreamUrlProvider.this.username = storedUsername;
					UpstreamUrlProvider.this.password = storedPassword;
					if (checkCredentials(url, username, password)) {
						hasDetails = true;
					}
				} catch (StorageException e1) {
					e1.printStackTrace();
				}
			}
		}

		if (!hasDetails) {
			final Display display = RunnerHelper.getWorkbenchDisplay();
			if (display == null) {
				return "";
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
				ForkJoinPool.commonPool().submit(() -> fireChangedListeners());
			}
		}

		if (!hasDetails) {
			return "";
		}

		return url;
	}

	public String getUsername() {
		return username;// preferenceStore.getString(StandardDateRepositoryPreferenceConstants.P_USERNAME_KEY);
	}

	public String getPassword() {
		return password;// preferenceStore.getString(StandardDateRepositoryPreferenceConstants.P_PASSWORD_KEY);
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

	static public boolean checkCredentials(String url, String username, String password) {

		if (username == null || username.isEmpty()) {
			return false;
		}

		if (password == null || password.isEmpty()) {
			return false;
		}

		final OkHttpClient httpClient = new okhttp3.OkHttpClient();

		Request loginRequest = new Request.Builder().url(url + "/api/login") //
				.addHeader("Authorization", Credentials.basic(username, password)) //
				.build();

		try {
			Response loginResponse = httpClient.newCall(loginRequest).execute();
			if (!loginResponse.isSuccessful()) {
				System.out.println("Bad credentials");
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	static public boolean testUpstreamAvailability(String url) {
		final OkHttpClient localClient = new okhttp3.OkHttpClient();
		Request pingRequest = null;
		try {
			pingRequest = new Request.Builder().url(url + "/ping").get().build();
		} catch (IllegalArgumentException e) {
			System.out.println("No valid url anymore");
			return false;
		}

		if (pingRequest == null) {
			return false;
		}

		try (final Response pingResponse = localClient.newCall(pingRequest).execute()) {
			if (!pingResponse.isSuccessful()) {
				return false;
			}
		} catch (IOException e) {
			// e.printStackTrace();
			System.out.println("No reachable upstream server");
			return false;
		}

		return true;
	}

	public boolean isAvailable() {
		final String url = getBaseURL();
		if (url == null || url.isEmpty()) {
			return false;
		}

		if (!url.startsWith("http")) {
			return false;
		}

		if (url.charAt(url.length() - 1) == '/') {
			return false;
		}

		if (!testUpstreamAvailability(getBaseURL())) {
			return false;
		}

		return true;
	}

	private void fireChangedListeners() {
		for (final IUpstreamDetailChangedListener l : detailListeners) {
			try {
				l.changed();
			} catch (final Exception e) {

			}
		}
	}

	private void fireServiceChangedListeners(IUpstreamServiceChangedListener.Service serviceType) {
		for (final IUpstreamServiceChangedListener l : workspaceListeners) {
			try {
				l.changed(serviceType);
			} catch (final Exception e) {

			}
		}
	}

	public boolean isBaseCaseServiceEnabled() {
		return baseCaseServiceEnabled;
	}

	public boolean isTeamServiceEnabled() {
		return teamServiceEnabled;
	}
}
