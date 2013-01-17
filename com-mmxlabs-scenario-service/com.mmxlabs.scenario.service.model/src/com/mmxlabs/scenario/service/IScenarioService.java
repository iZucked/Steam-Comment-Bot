/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.scenario.service.model.Container;
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

	/**
	 * Retrieve the scenario instance with the given unique ID
	 * 
	 * @param uuid
	 * @return
	 */
	ScenarioInstance getScenarioInstance(String uuid);

	/**
	 * Insert some new models into the service to create a new scenario instance
	 * 
	 * @param container
	 *            the destination in this logical model where the new scenario instance will go
	 * @param dependencies
	 *            other scenario instances which the new instance will depend upon
	 * @param models
	 *            the new models which this scenario will own; these ought not be in any resources at this point. New resources and storage will be sorted out for them by this scenario service.
	 * @return a new scenario instance, which will contain a new root object as its instance attribute
	 * @throws IOException
	 */
	ScenarioInstance insert(final Container container, final Collection<ScenarioInstance> dependencies, final Collection<EObject> models) throws IOException;

	/**
	 * Create a duplicate of the given scenario and place it in the destination. Any models owned by the original will be duplicated into new resources; dependencies of the original will not be
	 * duplicated.
	 * 
	 * @param original
	 * @param destination
	 * @return the new, duplicated instance.
	 * @throws IOException
	 */
	ScenarioInstance duplicate(final ScenarioInstance original, final Container destination) throws IOException;

	/**
	 * Delete the given scenario instance from this scenario service. Throws an {@link IllegalArgumentException} if the container is owned by another {@link IScenarioService}. This method can also be
	 * used to delete models which are not contained by any {@link IScenarioService}. In such cases the sub-models will be deleted but no {@link IScenarioServiceListener} events will be invoked.
	 * 
	 * @param container
	 */
	void delete(Container container);

	/**
	 * Ensures that the given scenario instance's actual implementation (getInstance() method) is loaded and resolved.
	 * 
	 * @param instance
	 * @throws IOException
	 */
	EObject load(final ScenarioInstance instance) throws IOException;

	/**
	 * Cause the saving of the given instance.
	 * 
	 * @param instance
	 * @throws IOException
	 */
	void save(ScenarioInstance instance) throws IOException;

	public URI resolveURI(final String uriString);

	/**
	 * Register a {@link IScenarioServiceListener} with this scenario service. Adding a service multiple times has no effect.
	 * 
	 * @param listener
	 */
	void addScenarioServiceListener(IScenarioServiceListener listener);

	/**
	 * Remove a previously added {@link IScenarioServiceListener} to this scenario service.
	 * 
	 * @param listener
	 */
	void removeScenarioServiceListener(IScenarioServiceListener listener);

	/**
	 * @since 2.0
	 */
	void unload(ScenarioInstance model);
}
