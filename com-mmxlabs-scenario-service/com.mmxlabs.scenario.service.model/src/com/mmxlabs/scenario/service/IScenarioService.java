/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;

public interface IScenarioService {
	/**
	 * Returns the name for this service
	 * 
	 * @return
	 */
	@NonNull
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
	boolean exists(@NonNull String uuid);

	/**
	 * Retrieve the scenario instance with the given unique ID
	 * 
	 * @param uuid
	 * @return
	 */
	ScenarioInstance getScenarioInstance(@NonNull String uuid);

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
	ScenarioInstance insert(Container container, EObject rootObject) throws IOException;

	/**
	 * Create a duplicate of the given scenario and place it in the destination. Any models owned by the original will be duplicated into new resources; dependencies of the original will not be
	 * duplicated.
	 * 
	 * @param original
	 * @param destination
	 * @return the new, duplicated instance.
	 * @throws IOException
	 */
	ScenarioInstance duplicate(@NonNull ScenarioInstance original, @NonNull Container destination) throws IOException;

	/**
	 * Delete the given scenario instance from this scenario service. Throws an {@link IllegalArgumentException} if the container is owned by another {@link IScenarioService}. This method can also be
	 * used to delete models which are not contained by any {@link IScenarioService}. In such cases the sub-models will be deleted but no {@link IScenarioServiceListener} events will be invoked.
	 * 
	 * @param container
	 */
	void delete(@NonNull Container container);

	/**
	 * Ensures that the given scenario instance's actual implementation (getInstance() method) is loaded and resolved.
	 * 
	 * @param instance
	 * @throws IOException
	 */
	EObject load(@NonNull ScenarioInstance instance) throws IOException;

	/**
	 * Cause the saving of the given instance.
	 * 
	 * @param instance
	 * @throws IOException
	 */
	void save(@NonNull ScenarioInstance instance) throws IOException;

	/**
	 * Resolves the relative uri stored in a {@link ScenarioInstance} owned by this {@link IScenarioService} into a fully qualified {@link URI}
	 * 
	 * @param uriString
	 * @return
	 */
	@NonNull
	public URI resolveURI(@NonNull String uriString);

	/**
	 * Register a {@link IScenarioServiceListener} with this scenario service. Adding a service multiple times has no effect.
	 * 
	 * @param listener
	 */
	void addScenarioServiceListener(@NonNull IScenarioServiceListener listener);

	/**
	 * Remove a previously added {@link IScenarioServiceListener} to this scenario service.
	 * 
	 * @param listener
	 */
	void removeScenarioServiceListener(@NonNull IScenarioServiceListener listener);

	/**
	 */
	void unload(@NonNull ScenarioInstance model);

	/**
	 * Move the collection of elements under the destination.
	 * 
	 * @param containers
	 * @param container
	 */
	void moveInto(@NonNull List<Container> elements, @NonNull Container destination);

	/**
	 */
	void makeFolder(@NonNull Container parent, @NonNull String name);
}
