/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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
	@NonNull
	Collection<ScenarioInstance> getSelection();

	/**
	 * @return the currently pinned instance; this is always a member of the selection.
	 */
	@Nullable
	ScenarioInstance getPinnedInstance();

	/**
	 * Set pinned scenario. Do not block.
	 */
	void setPinnedInstance(@Nullable ScenarioInstance referenceInstance);

	/**
	 * Set pinned scenario. If block is true, do not return until UI is fully refreshed.
	 */
	void setPinnedInstance(@Nullable ScenarioInstance referenceInstance, boolean block);

	void setPinnedPair(@NonNull ScenarioInstance pinInstance, @NonNull ScenarioInstance otherInstance, boolean block);

	void addSelectionChangedListener(@NonNull IScenarioServiceSelectionChangedListener listener);

	void removeSelectionChangedListener(@NonNull IScenarioServiceSelectionChangedListener listener);

	boolean isSelected(@NonNull ScenarioInstance instance);

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
	void select(@NonNull ScenarioInstance scenarioInstance);

	/**
	 * Select a scenario. If block is true, do not return until UI is fully refreshed.
	 */
	void select(@NonNull ScenarioInstance scenarioInstance, boolean block);

	/**
	 * Deselect a scenario. Do not block.
	 */
	void deselect(@NonNull ScenarioInstance scenarioInstance);

	/**
	 * Deselect a scenario. If block is true, do not return until UI is fully refreshed.
	 */
	void deselect(@NonNull ScenarioInstance scenarioInstance, boolean block);

}
