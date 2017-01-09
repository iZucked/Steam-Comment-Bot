/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports;

import java.util.Collection;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Used to collect data elements from potentially multiple scenarios when compiling reports (collectElements(...) typically returns a list of RowData for each schedule instance).
 * 
 * @author proshun
 */
public interface IScenarioInstanceElementCollector {
	/**
	 * Notify implementors that we are beginning a new element collection process. Expect zero or more calls to {@link #collectElements(LNGScenarioModel, boolean)} followed by a call to
	 * {@link #endCollecting()}
	 */
	public void beginCollecting(boolean pinDiffMode);

	/**
	 * Notify implementors that all scenarios have been presented. Implementors should finalise any data processing. No more calls to {@link #collectElements(LNGScenarioModel, boolean)} are expected.
	 */
	public void endCollecting();

	/**
	 * Notify the implementor of each scenario under consideration. Passes in a flag to indicate that the given scenario is pinned. Up to one pinned scenario is expected to be presented at one time.
	 * 
	 */
	public Collection<? extends Object> collectElements(ScenarioInstance scenarioInstance, final LNGScenarioModel rootObject, final boolean isPinned);
}
