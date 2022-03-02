/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public PreferenceInitializer() {
	}

	@Override
	public void initializeDefaultPreferences() {
		DefaultScope.INSTANCE.getNode("com.mmxlabs.models.lng.cargo.editor").put(PreferenceConstants.P_CONTRACTS_TO_CONSIDER_OPEN, "");

	}

}
