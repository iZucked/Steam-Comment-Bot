/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Interface designed for use with unit tests. Some reports work with the active editor, however during unit test this information is not always present. This interface is designed to be obtained via
 * viewPart#getAdapter() and can then be used to set the {@link ScenarioInstance} editor input
 * 
 * @author Simon Goodall
 *
 */
public interface IProvideEditorInputScenario {

	void provideScenarioInstance(@Nullable ScenarioInstance scenarioInstance);
}
