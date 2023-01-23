/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

public class CloudOptimiserPreferenceInitializer extends AbstractPreferenceInitializer {
	private static final String NODE = "com.mmxlabs.lngdataserver.integration.ui.scenarios";

	@Override
	public void initializeDefaultPreferences() {
		// DefaultScope.INSTANCE.getNode(NODE).put(CloudOptimiserPreferenceConstants.P_GATEWAY_URL_KEY, "https://gw.mmxlabs.com/");
	}

}
