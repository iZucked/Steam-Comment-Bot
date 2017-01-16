/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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
	void deselected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioResult> deselected, boolean block);

	/**
	 * Notify listener of selected set of scenarios. If block is true, do not return until UI is fully refreshed.
	 */
	void selected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioResult> selected, boolean block);

	/**
	 * Notify listener of pinned scenario. If block is true, do not return until UI is fully refreshed.
	 */
	void pinned(final IScenarioServiceSelectionProvider provider, final ScenarioResult oldPin, final ScenarioResult newPin, boolean block);

	void selectionChanged(@Nullable ScenarioResult pinned, @NonNull Collection<ScenarioResult> others, boolean block);
}
