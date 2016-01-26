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
 * Listener which notifies on selection changed events.
 * 
 * @author Simon Goodall
 * 
 */
public interface IScenarioServiceSelectionChangedListener {
	/**
	 * Notify listener of deselected set of scenarios. If block is true, do not return until UI is fully refreshed.
	 */
	void deselected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioInstance> deselected, boolean block);

	/**
	 * Notify listener of selected set of scenarios. If block is true, do not return until UI is fully refreshed.
	 */
	void selected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioInstance> selected, boolean block);

	/**
	 * Notify listener of pinned scenario. If block is true, do not return until UI is fully refreshed.
	 */
	void pinned(final IScenarioServiceSelectionProvider provider, final ScenarioInstance oldPin, final ScenarioInstance newPin, boolean block);

	void selectionChanged(@Nullable ScenarioInstance pinned, @NonNull Collection<ScenarioInstance> others, boolean block);
}
