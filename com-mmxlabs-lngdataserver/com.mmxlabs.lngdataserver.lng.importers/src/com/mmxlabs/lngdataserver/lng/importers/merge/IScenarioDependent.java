package com.mmxlabs.lngdataserver.lng.importers.merge;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

public interface IScenarioDependent {
	public void update(ScenarioInstance target, ScenarioInstance source);
}
