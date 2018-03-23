/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.server.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;

public class DatahubPreferenceInitializer extends AbstractPreferenceInitializer {

	public DatahubPreferenceInitializer() {
	}

	@Override
	public void initializeDefaultPreferences() {
		DefaultScope.INSTANCE.getNode("com.mmxlabs.lngdataserver.server").put(StandardDateRepositoryPreferenceConstants.P_URL_KEY, "");
		DefaultScope.INSTANCE.getNode("com.mmxlabs.lngdataserver.server").put(StandardDateRepositoryPreferenceConstants.P_USERNAME_KEY, "");
		DefaultScope.INSTANCE.getNode("com.mmxlabs.lngdataserver.server").put(StandardDateRepositoryPreferenceConstants.P_PASSWORD_KEY, "");

	}

}
