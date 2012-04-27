package com.mmxlabs.scenario.service.ui;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Listener which notifies on selection changed events.
 * 
 * @author Simon Goodall
 * 
 */
public interface IScenarioServiceSelectionChangedListener {

	/**
	 * The given {@link ScenarioInstance} is now part of the current selection.
	 * 
	 * @param provider
	 * @param instance
	 */
	void selected(IScenarioServiceSelectionProvider provider, ScenarioInstance instance);

	/**
	 * The given {@link ScenarioInstance} is no longer part of the current selection;
	 * 
	 * @param provider
	 * @param instance
	 */
	void deselected(IScenarioServiceSelectionProvider provider, ScenarioInstance instance);

}
