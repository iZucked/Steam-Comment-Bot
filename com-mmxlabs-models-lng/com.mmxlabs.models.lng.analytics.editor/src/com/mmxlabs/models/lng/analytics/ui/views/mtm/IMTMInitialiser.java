/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.mtm;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.MTMModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public interface IMTMInitialiser {
	/**
	 * 
	 * @returns false if nothing was added
 	 */
	boolean initialiseModel(final @NonNull LNGScenarioModel scenarioModel, final @NonNull MTMModel model);
}
