/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.server.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;

public class DatahubPreferenceInitializer extends AbstractPreferenceInitializer {
	private static final String NODE = "com.mmxlabs.lngdataserver.server";

	@Override
	public void initializeDefaultPreferences() {
		DefaultScope.INSTANCE.getNode(NODE).put(StandardDateRepositoryPreferenceConstants.P_URL_KEY, "");
		DefaultScope.INSTANCE.getNode(NODE).put(StandardDateRepositoryPreferenceConstants.P_USERNAME_KEY, "");
		DefaultScope.INSTANCE.getNode(NODE).put(StandardDateRepositoryPreferenceConstants.P_PASSWORD_KEY, "");

	}

}
