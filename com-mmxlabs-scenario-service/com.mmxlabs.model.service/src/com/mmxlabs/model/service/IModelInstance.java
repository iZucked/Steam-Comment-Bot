/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.model.service;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;

/**
 * The {@link IModelInstance} is a wrapper around a real EObject instance provided by the {@link IModelService} This wrapper provides additional API for I/O operations.
 * 
 * @author Simon Goodall
 * 
 */
public interface IModelInstance {

	/**
	 * Return the model instance.
	 * 
	 * @return
	 * @throws IOException
	 */
	EObject getModel() throws IOException;

	/**
	 * Similar to {@link #getModel()} except exceptions are caught and logged and null is returned instead.
	 * 
	 * @since 2.0
	 */
	EObject getModelSafe();

	/**
	 * Request the current model state should be saved.
	 * 
	 * @throws IOException
	 */
	void save() throws IOException;

	void saveWithMany() throws IOException;

	/**
	 * Request the current model should be deleted.
	 * 
	 * @throws IOException
	 */
	void delete() throws IOException;

	/**
	 * Rollback changes to last saved state.
	 * 
	 * TODO: Should this be here or part of EditingDomain?
	 */
	void rollback();

	/**
	 * @return true iff this model instance has been changed since last save.
	 */
	boolean isDirty();

	URI getURI();

	/**
	 * @since 2.0
	 */
	void unload();

	/**
	 * Returns true if the model has been successfully loaded.
	 * 
	 * @since 2.0
	 */
	boolean isLoaded();

	/**
	 * Unloads the model and removes this model and {@link URI} from the model service
	 * 
	 * @since 2.0
	 */
	void dispose();

}
