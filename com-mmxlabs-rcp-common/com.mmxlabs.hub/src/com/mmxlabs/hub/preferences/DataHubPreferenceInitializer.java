/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.hub.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;

import com.mmxlabs.hub.DataHubActivator;

public class DataHubPreferenceInitializer extends AbstractPreferenceInitializer {
	private static final String NODE = "com.mmxlabs.hub";

	@Override
	public void initializeDefaultPreferences() {
		DefaultScope.INSTANCE.getNode(NODE).put(DataHubPreferenceConstants.P_DATAHUB_URL_KEY, "");
		
		DataHubActivator.getDefault().getPreferenceStore().setDefault(TLSPreferenceConstants.P_TLS_USE_JAVA_TRUSTSTORE, false);
		DataHubActivator.getDefault().getPreferenceStore().setDefault(TLSPreferenceConstants.P_TLS_USE_WINDOWS_TRUSTSTORE, true);

	}

}
