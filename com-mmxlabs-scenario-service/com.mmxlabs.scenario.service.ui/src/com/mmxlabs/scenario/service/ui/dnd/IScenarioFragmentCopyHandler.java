/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.dnd;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public interface IScenarioFragmentCopyHandler {

	boolean copy(@NonNull ScenarioFragment scenarioFragment, @NonNull ScenarioInstance scenarioInstance);

}
