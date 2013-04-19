/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports;

import java.util.Collection;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public interface IScenarioInstanceElementCollector {
	/**
	 * Notify implementors that we are beginning a new element collection process. Expect zero or more calls to {@link #collectElements(LNGScenarioModel, boolean)} followed by a call to
	 * {@link #endCollecting()}
	 */
	public void beginCollecting();

	/**
	 * Notify implementors that all scenarios have been presented. Implementors should finalise any data processing. No more calls to {@link #collectElements(LNGScenarioModel, boolean)} are expected.
	 */
	public void endCollecting();

	/**
	 * Notify the implementor of each scenario under consideration. Passes in a flag to inidicate that the given scenario is pinned. Up to one pinned scenario is expected to be presented at one time.
	 * 
	 * @since 3.0
	 */
	public Collection<? extends Object> collectElements(final LNGScenarioModel rootObject, final boolean isPinned);
}
