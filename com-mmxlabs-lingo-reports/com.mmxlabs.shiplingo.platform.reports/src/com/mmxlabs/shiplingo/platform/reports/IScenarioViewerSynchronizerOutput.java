package com.mmxlabs.shiplingo.platform.reports;

import java.util.Collection;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public interface IScenarioViewerSynchronizerOutput {
		public Collection<Object> getCollectedElements();
		public ScenarioInstance getScenarioInstance(Object object);
		public MMXRootObject getRootObject(Object object);
	}