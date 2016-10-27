/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.preferences;

import java.text.Normalizer.Form;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;

import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.views.formatters.Formatters;

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
		DefaultScope.INSTANCE.getNode(Activator.PLUGIN_ID).put(PreferenceConstants.P_LEEWAY_DAYS, "1");
		DefaultScope.INSTANCE.getNode(Activator.PLUGIN_ID).put(PreferenceConstants.P_REPORT_DURATION_FORMAT, Formatters.DurationMode.DAYS_HOURS.name());
	}
}
