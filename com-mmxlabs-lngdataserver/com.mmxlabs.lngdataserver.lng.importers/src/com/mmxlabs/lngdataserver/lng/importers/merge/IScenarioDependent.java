package com.mmxlabs.lngdataserver.lng.importers.merge;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public interface IScenarioDependent {
	public void update(String targetName, String sourceName, LNGScenarioModel target, LNGScenarioModel source);
}
