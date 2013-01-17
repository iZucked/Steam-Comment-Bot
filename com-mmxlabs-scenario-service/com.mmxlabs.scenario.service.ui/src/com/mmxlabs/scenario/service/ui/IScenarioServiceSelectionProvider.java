/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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

	/**
	 * @return the currently pinned instance; this is always a member of the selection.
	 */
	ScenarioInstance getPinnedInstance();

	void setPinnedInstance(ScenarioInstance referenceInstance);

	void addSelectionChangedListener(IScenarioServiceSelectionChangedListener listener);

	void removeSelectionChangedListener(IScenarioServiceSelectionChangedListener listener);

	boolean isSelected(ScenarioInstance instance);

	void deselectAll();

	void select(ScenarioInstance scenarioInstance);

	void deselect(ScenarioInstance scenarioInstance);

}
