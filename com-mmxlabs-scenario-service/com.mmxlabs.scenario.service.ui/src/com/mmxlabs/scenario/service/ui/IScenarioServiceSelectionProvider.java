/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
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

	/**
	 * Set pinned scenario. Do not block.
	 */
	void setPinnedInstance(ScenarioInstance referenceInstance);

	/**
	 * Set pinned scenario. If block is true, do not return until UI is fully refreshed.
	 */
	void setPinnedInstance(ScenarioInstance referenceInstance, boolean block);

	void addSelectionChangedListener(IScenarioServiceSelectionChangedListener listener);

	void removeSelectionChangedListener(IScenarioServiceSelectionChangedListener listener);

	boolean isSelected(ScenarioInstance instance);

	/**
	 * Deselect all scenarios. Do not block.
	 */
	void deselectAll();

	/**
	 * Deselect all scenarios. If block is true, do not return until UI is fully refreshed.
	 */
	void deselectAll(boolean block);

	/**
	 * Select a scenario. Do not block.
	 */
	void select(ScenarioInstance scenarioInstance);

	/**
	 * Select a scenario. If block is true, do not return until UI is fully refreshed.
	 */
	void select(ScenarioInstance scenarioInstance, boolean block);

	/**
	 * Deselect a scenario. Do not block.
	 */
	void deselect(ScenarioInstance scenarioInstance);

	/**
	 * Deselect a scenario. If block is true, do not return until UI is fully refreshed.
	 */
	void deselect(ScenarioInstance scenarioInstance, boolean block);

}
