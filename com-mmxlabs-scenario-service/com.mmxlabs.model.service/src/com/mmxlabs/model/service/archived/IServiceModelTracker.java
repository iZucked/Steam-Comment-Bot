/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.model.service.archived;

import org.eclipse.emf.common.notify.Adapter;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * 
 * @author Simon Goodall
 *
 */
public interface IServiceModelTracker extends Adapter.Internal {
	void setScenarioInstance(final ScenarioInstance instance);
}
