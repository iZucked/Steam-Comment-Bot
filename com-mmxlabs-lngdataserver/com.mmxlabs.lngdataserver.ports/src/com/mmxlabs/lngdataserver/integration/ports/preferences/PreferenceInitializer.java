package com.mmxlabs.lngdataserver.integration.ports.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;

import com.mmxlabs.lngdataserver.commons.impl.StandardDateRepositoryPreferenceConstants;
import com.mmxlabs.lngdataserver.integration.ports.internal.Activator;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		// Default to production path
		DefaultScope.INSTANCE.getNode(Activator.PLUGIN_ID).put(StandardDateRepositoryPreferenceConstants.P_URL_KEY, "http://localhost:8097");
	}

}
