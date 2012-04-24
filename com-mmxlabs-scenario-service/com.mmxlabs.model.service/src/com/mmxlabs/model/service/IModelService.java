package com.mmxlabs.model.service;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * A service to maintain EMF model instances in memory. The service hides all of the I/O related operations - traditionally via a {@link ResourceSet} and instead wraps models in a
 * {@link IModelInstance} objects.
 * 
 * @author Simon Goodall
 * 
 */
public interface IModelService {

	/**
	 * Return a cached (or possible just loaded and cached) model instance for the given {@link URI}.
	 * 
	 * @param uri
	 * @return
	 * @throws IOException
	 */
	IModelInstance getModel(URI uri) throws IOException;

	/**
	 * Save the current {@link IModelInstance} into the given {@link URI}. This assumes the model is currently not tied to any existing {@link URI} - i.e. this is a newly created model.
	 * 
	 * @param instance
	 * @param uri
	 * @throws IOException
	 */
	void saveAs(IModelInstance instance, URI uri) throws IOException;

	/**
	 * Perform a copy of the current {@link IModelInstance} to the given URI. Returns a new {@link IModelInstance} for the copy.
	 * 
	 * @param from
	 * @param to
	 * @throws IOException
	 */
	IModelInstance copyTo(IModelInstance from, URI to) throws IOException;

	/**
	 * Perform a copy of a model from one {@link URI} to another {@link URI}.
	 * 
	 * 
	 * TODO: Is this the persisted state or potential in memory state?
	 * 
	 * @param from
	 * @param to
	 * @throws IOException
	 */
	void copyTo(URI from, URI to) throws IOException;

	/**
	 * Delete the given {@link IModelInstance} instance.
	 * 
	 * @param instance
	 * @throws IOException
	 */
	void delete(IModelInstance instance) throws IOException;
}
