/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.hub.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;

public class DataHubPreferenceInitializer extends AbstractPreferenceInitializer {
	private static final String NODE = "com.mmxlabs.hub";

	@Override
	public void initializeDefaultPreferences() {
		DefaultScope.INSTANCE.getNode(NODE).put(DataHubPreferenceConstants.P_DATAHUB_URL_KEY, "");
	}

}
