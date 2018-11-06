package com.mmxlabs.scenario.service.ui.dnd;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

public interface IScenarioInstanceChangeHandler {

	boolean applyChange(ScenarioInstance scenarioInstance, IChangeSource changeSource);
}
