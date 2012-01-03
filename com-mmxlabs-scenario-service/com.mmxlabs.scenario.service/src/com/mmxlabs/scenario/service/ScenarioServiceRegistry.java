/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.scenario.service.model.ScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;

public final class ScenarioServiceRegistry {

	private final List<IScenarioService> scenarioServices = new LinkedList<IScenarioService>();

	private final ScenarioModel scenarioModel;

	public ScenarioServiceRegistry() {

		scenarioModel = ScenarioServiceFactory.eINSTANCE.createScenarioModel();
	}

	public ScenarioModel getScenarioModel() {
		return scenarioModel;
	}

	public Collection<IScenarioService> getScenarioServices() {
		return scenarioServices;
	}

	public void bindScenarioService(final IScenarioService scenarioService) {
		scenarioServices.add(scenarioService);
		scenarioModel.getScenarioServices().add(scenarioService.getServiceModel());
	}

	public void unbindScenarioService(final IScenarioService scenarioService) {
		scenarioServices.remove(scenarioService);
		scenarioModel.getScenarioServices().remove(scenarioService.getServiceModel());
	}

}
