package com.mmxlabs.lngdataserver.server;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import com.mmxlabs.lngdataserver.server.internal.Activator;
import com.mmxlabs.lngdataserver.server.preferences.StandardDateRepositoryPreferenceConstants;

public class UpstreamUrlProvider {
	public static final UpstreamUrlProvider INSTANCE = new UpstreamUrlProvider();

	private final IPreferenceStore preferenceStore;
	private final Set<IUpstreamDetailChangedListener> listeners = new HashSet<>();

	public UpstreamUrlProvider() {

		preferenceStore = Activator.getDefault().getPreferenceStore();
		preferenceStore.addPropertyChangeListener(listener);
	}

	private final IPropertyChangeListener listener = new IPropertyChangeListener() {
		@Override
		public void propertyChange(final PropertyChangeEvent event) {
			switch (event.getProperty()) {
			case StandardDateRepositoryPreferenceConstants.P_URL_KEY:
			case StandardDateRepositoryPreferenceConstants.P_USERNAME_KEY:
			case StandardDateRepositoryPreferenceConstants.P_PASSWORD_KEY:
				for (final IUpstreamDetailChangedListener l : listeners) {
					try {
						l.changed();
					} catch (final Exception e) {

					}
				}
			}
		}
	};

	public String getBaseURL() {
		final String url = preferenceStore.getString(StandardDateRepositoryPreferenceConstants.P_URL_KEY);
		if (url == null) {
			return "";
		}
		return url;
	}

	public String getUsername() {
		return preferenceStore.getString(StandardDateRepositoryPreferenceConstants.P_USERNAME_KEY);
	}

	public String getPassword() {
		return preferenceStore.getString(StandardDateRepositoryPreferenceConstants.P_PASSWORD_KEY);
	}

	public void registerDetailsChangedLister(final IUpstreamDetailChangedListener listener) {
		listeners.add(listener);
	}

	public void deregisterDetailsChangedLister(final IUpstreamDetailChangedListener listener) {
		listeners.remove(listener);
	}

	public boolean isAvailable() {
		final String url = getBaseURL();
		if (url == null || url.isEmpty()) {
			return false;
		}

		if (!url.startsWith("http")) {
			return false;
		}
		return true;
	}
}
