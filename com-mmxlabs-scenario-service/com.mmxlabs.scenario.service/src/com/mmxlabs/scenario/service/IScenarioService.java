/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;

public interface IScenarioService {

	/**
	 * Returns the name for this service
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Return the EMF logical model of the service
	 * 
	 * @return
	 */
	ScenarioService getServiceModel();

	/**
	 * Returns true if a scenario exists with the specified UUID
	 * 
	 * @param uuid
	 * @return
	 */
	boolean exists(String uuid);

	ScenarioInstance getScenarioInstance(String uuid);

	String saveAs(ScenarioInstance instance);

	ScenarioInstance copyTo(ScenarioInstance from, int flags);

	void delete(ScenarioInstance instance);

	void delete(String uuid);
}
