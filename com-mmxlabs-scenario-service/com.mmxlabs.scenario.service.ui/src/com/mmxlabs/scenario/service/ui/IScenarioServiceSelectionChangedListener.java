/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import java.util.Collection;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Listener which notifies on selection changed events.
 * 
 * @author Simon Goodall
 * 
 */
public interface IScenarioServiceSelectionChangedListener {
	void deselected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioInstance> deselected);
	void selected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioInstance> selected);
	void pinned(final IScenarioServiceSelectionProvider provider, final ScenarioInstance oldPin, final ScenarioInstance newPin);
}
