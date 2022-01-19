/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.scenario.service.ScenarioResult;

/**
 * Used to collect data elements from potentially multiple scenarios when compiling reports (collectElements(...) typically returns a list of RowData for each schedule instance).
 * 
 * @author proshun
 */
@NonNullByDefault
public interface IScenarioInstanceElementCollector {
	/**
	 * Notify implementors that we are beginning a new element collection process. Expect zero or more calls to {@link #collectElements(LNGScenarioModel, boolean)} followed by a call to
	 * {@link #endCollecting()}
	 */
	void beginCollecting(boolean pinDiffMode);

	/**
	 * Notify implementors that all scenarios have been presented. Implementors should finalise any data processing. No more calls to {@link #collectElements(LNGScenarioModel, boolean)} are expected.
	 */
	void endCollecting();

	/**
	 * Notify the implementor of each scenario under consideration. Passes in a flag to indicate that the given scenario is pinned. Up to one pinned scenario is expected to be presented at one time.
	 * 
	 */
	Collection<EObject> collectElements(ScenarioResult scenarioResult, boolean isPinned);
}
