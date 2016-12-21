/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
	Collection<ScenarioResult> getSelection();

	/**
	 * @return the currently pinned instance; this is always a member of the selection.
	 */
	@Nullable
	ScenarioResult getPinnedInstance();

	/**
	 * Set pinned scenario. Do not block.
	 */
	void setPinnedInstance(@Nullable ScenarioResult pinnedResult);

	void setPinnedInstance(@Nullable ScenarioInstance pinnedInstance);

	/**
	 * Set pinned scenario. If block is true, do not return until UI is fully refreshed.
	 */
	void setPinnedInstance(@Nullable ScenarioResult referenceInstance, boolean block);

	void setPinnedPair(@NonNull ScenarioResult pinInstance, @NonNull ScenarioResult otherInstance, boolean block);

	void addSelectionChangedListener(@NonNull IScenarioServiceSelectionChangedListener listener);

	void removeSelectionChangedListener(@NonNull IScenarioServiceSelectionChangedListener listener);

	boolean isSelected(@NonNull ScenarioResult scenarioResult);

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
	void select(@NonNull ScenarioResult scenarioInstance);

	/**
	 * Select a scenario. If block is true, do not return until UI is fully refreshed.
	 */
	void select(@NonNull ScenarioResult scenarioInstance, boolean block);

	/**
	 * Deselect a scenario. Do not block.
	 */
	void deselect(@NonNull ScenarioResult scenarioResult);

	void deselect(@NonNull ScenarioInstance scenarioInstance);

	/**
	 * Deselect a scenario. If block is true, do not return until UI is fully refreshed.
	 */
	void deselect(@NonNull ScenarioResult scenarioResult, boolean block);

	void deselect(@NonNull ScenarioInstance scenarioInstance, boolean block);

	boolean isPinned(@NonNull ScenarioResult result);

	boolean isPinned(@NonNull ScenarioInstance instance);

}
