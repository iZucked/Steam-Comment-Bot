/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import java.util.Collection;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * A selection provider interface for the scenario service.
 * 
 * @author Simon Goodall
 * 
 */
public interface IScenarioServiceSelectionProvider {

	/**
	 * Returns the currently selected {@link ScenarioInstance}s.
	 * 
	 * @return
	 */
	Collection<ScenarioInstance> getSelection();

	void addSelectionChangedListener(IScenarioServiceSelectionChangedListener listener);

	void removeSelectionChangedListener(IScenarioServiceSelectionChangedListener listener);

	boolean isSelected(ScenarioInstance instance);
}
