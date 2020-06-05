/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.hub;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleContext;

import com.mmxlabs.hub.preferences.DataHubPreferenceConstants;
import com.mmxlabs.hub.services.users.UserNameUpdater;

public class DataHubActivator extends Plugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.hub"; //$NON-NLS-1$

	private static DataHubActivator plugin;

	/**
	 * Storage for preferences.
	 */
	private ScopedPreferenceStore preferenceStore;
	private UserNameUpdater userNameUpdater;

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		
		// Trigger class load and property change hooks
		UpstreamUrlProvider.INSTANCE.isAvailable();

		userNameUpdater = new UserNameUpdater();
		userNameUpdater.start();
	}

	@Override
	public void stop(final BundleContext context) throws Exception {

		userNameUpdater.stop();
		plugin = null;
		super.stop(context);
	}

	public static DataHubActivator getDefault() {
		return plugin;
	}

	public IPreferenceStore getPreferenceStore() {
		// Create the preference store lazily.
		if (preferenceStore == null) {
			preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, "com.mmxlabs.hub");
		}
		// Legacy checks
		{
			String string = preferenceStore.getString(DataHubPreferenceConstants.P_DATAHUB_URL_KEY);
			if (string.isEmpty()) {
				ScopedPreferenceStore legacyStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, "com.mmxlabs.lngdataserver.server");
				if (!legacyStore.getString(DataHubPreferenceConstants.P_DATAHUB_URL_KEY).isEmpty()) {
					copy(legacyStore, preferenceStore, DataHubPreferenceConstants.P_DATAHUB_URL_KEY);
					copyBoolean(legacyStore, preferenceStore, DataHubPreferenceConstants.P_DISABLE_SSL_HOSTNAME_CHECK);
					copyBoolean(legacyStore, preferenceStore, DataHubPreferenceConstants.P_ENABLE_BASE_CASE_SERVICE_KEY);
					copyBoolean(legacyStore, preferenceStore, DataHubPreferenceConstants.P_ENABLE_TEAM_SERVICE_KEY);

					// Reset this field
					legacyStore.setValue(DataHubPreferenceConstants.P_DATAHUB_URL_KEY, "");
				}
			}
		}
		return preferenceStore;
	}
	
	private void copy(IPreferenceStore from, IPreferenceStore to, String key) {
		to.setValue(key, from.getString(key));
	}

	private void copyBoolean(IPreferenceStore from, IPreferenceStore to, String key) {
		to.setValue(key, from.getBoolean(key));
	}
}
