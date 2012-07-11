/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports;

import java.util.Collection;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public interface IScenarioViewerSynchronizerOutput {
	public Collection<Object> getCollectedElements();
	public ScenarioInstance getScenarioInstance(Object object);
	public boolean isPinned(Object object);
	public MMXRootObject getRootObject(Object object);
	public Collection<MMXRootObject> getRootObjects();
}