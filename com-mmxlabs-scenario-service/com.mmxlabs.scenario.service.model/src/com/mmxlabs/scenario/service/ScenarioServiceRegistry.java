/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.scenario.service.model.ScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;

/**
 * Registry used to hold references to {@link IScenarioService} instances.
 * 
 * @author Simon Goodall
 * 
 */
public final class ScenarioServiceRegistry {

	private final List<IScenarioService> scenarioServices = new LinkedList<IScenarioService>();

	private final ScenarioModel scenarioModel;

	public ScenarioServiceRegistry() {

		scenarioModel = ScenarioServiceFactory.eINSTANCE.createScenarioModel();
	}

	/**
	 * Returns the EMF logical model of the scenario service.
	 * 
	 * @return
	 */
	public ScenarioModel getScenarioModel() {
		return scenarioModel;
	}

	/**
	 * Returns an unmodifiable {@link Collection} of the registered {@link IScenarioService} instances.
	 * 
	 * @return
	 */
	public Collection<IScenarioService> getScenarioServices() {
		return Collections.unmodifiableCollection(scenarioServices);
	}

	/**
	 * Register an {@link IScenarioService} instance.
	 * 
	 * @param scenarioService
	 */
	public void bindScenarioService(final IScenarioService scenarioService) {
		// Add to local list
		scenarioServices.add(scenarioService);
		// Add to EMF model -- note, this can be blocking!
		new Thread() {
			public void run() {
				ScenarioService serviceModel = scenarioService.getServiceModel();
				// Avoid concurrent adds
				synchronized (scenarioModel) {
					scenarioModel.getScenarioServices().add(serviceModel);
				}
			}
		}.start();
	}

	/**
	 * Un-register an {@link IScenarioService} instance.
	 * 
	 * @param scenarioService
	 */
	public void unbindScenarioService(final IScenarioService scenarioService) {
		// Remove local list
		scenarioServices.remove(scenarioService);
		// Update EMF model
		synchronized (scenarioModel) {
			scenarioModel.getScenarioServices().remove(scenarioService.getServiceModel());
		}
	}
}
