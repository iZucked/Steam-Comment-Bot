package com.mmxlabs.model.service;

import java.io.IOException;

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
	 */
	EObject getModel();

	/**
	 * Request the current model state should be saved.
	 * 
	 * @throws IOException
	 */
	void save() throws IOException;

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

}
