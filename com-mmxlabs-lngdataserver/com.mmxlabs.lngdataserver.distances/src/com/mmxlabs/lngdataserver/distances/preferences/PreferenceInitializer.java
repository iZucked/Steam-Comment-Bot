package com.mmxlabs.lngdataserver.distances.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;

import com.mmxlabs.lngdataserver.distances.internal.Activator;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		// Default to production path
		DefaultScope.INSTANCE.getNode(Activator.PLUGIN_ID).put(PreferenceConstants.P_URL_KEY, "http://localhost:8090");
	}

}
