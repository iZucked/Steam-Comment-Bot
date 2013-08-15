/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.dirscan.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;

import com.mmxlabs.scenario.service.dirscan.internal.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {

		DefaultScope.INSTANCE.getNode(Activator.PLUGIN_ID).put(PreferenceConstants.P_ENABLED_KEY, "false");
		DefaultScope.INSTANCE.getNode(Activator.PLUGIN_ID).put(PreferenceConstants.P_NAME_KEY, "LNG Base Cases");
		DefaultScope.INSTANCE.getNode(Activator.PLUGIN_ID).put(PreferenceConstants.P_PATH_KEY, "");
	}
}
