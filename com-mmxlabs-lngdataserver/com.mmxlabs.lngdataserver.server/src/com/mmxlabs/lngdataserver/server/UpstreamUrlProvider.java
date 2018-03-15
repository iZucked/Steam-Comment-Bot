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

	private IPreferenceStore preferenceStore;

	public UpstreamUrlProvider() {

		preferenceStore = Activator.getDefault().getPreferenceStore();
		preferenceStore.addPropertyChangeListener(listener);

		// ISecurePreferences root = SecurePreferencesFactory.getDefault();
		// ISecurePreferences node = root.node("datahub/upstream");
		// node.put("password", myPassword, true);
	}

	private final IPropertyChangeListener listener = new IPropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			switch (event.getProperty()) {
			case StandardDateRepositoryPreferenceConstants.P_URL_KEY:
				// case StandardDateRepositoryPreferenceConstants.P_USERNAME_KEY:
				// case StandardDateRepositoryPreferenceConstants.P_PASSWORD_KEY:
				//
				// boolean listen = listenForNewUpstreamVersions;
				// if (listen) {
				// stopListeningForNewUpstreamVersions();
				// }
				// upstreamUrl = getUpstreamUrl();
				// newUpstreamURL(upstreamUrl);
				//
				// if (listen) {
				// startListenForNewUpstreamVersions();
				// }
				//
				// break;
				// default:
			}
		}
	};

	private Set<IUpstreamDetailChangedListener> listeners = new HashSet<>();

	public String getBaseURL() {
//		String url = preferenceStore.getString(StandardDateRepositoryPreferenceConstants.P_URL_KEY);
//		if (url == null) {
//			return "";
//		}
//		return url;
		return "https://kubera.mmxlabs.lan:9090/";
	}

	public String getUsername() {
		return "ds_admin";// l..preferenceStore.getString(StandardDateRepositoryPreferenceConstants.P_USERNAME_KEY);
	}

	public String getPassword() {
		// Best to use char[] for password.
		return "ds_admin";// preferenceStore.getString(StandardDateRepositoryPreferenceConstants.P_PASSWORD_KEY).toCharArray();
	}
	//
	// public @Nullable Pair<String, String> getCredientals() {
	// ISecurePreferences root = SecurePreferencesFactory.getDefault();
	// if (root.nodeExists("datahub/upstream")) {
	// ISecurePreferences node = root.node("datahub/upstream");
	// String username = node.get("username", null);
	// String password = node.get("password", null);
	//
	// return new Pair<>(username, password);
	// }
	// return null;
	// }

	public void registerDetailsChangedLister(IUpstreamDetailChangedListener listener) {
		listeners.add(listener);
	}

	public void deregisterDetailsChangedLister(IUpstreamDetailChangedListener listener) {
		listeners.remove(listener);
	}
}
