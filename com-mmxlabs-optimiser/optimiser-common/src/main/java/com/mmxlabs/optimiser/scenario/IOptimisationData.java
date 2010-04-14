package com.mmxlabs.optimiser.scenario;

import java.util.Collection;
import java.util.List;

import com.mmxlabs.optimiser.IResource;

/**
 * An interface to the optimisation data, comprising accessors for core
 * attributes and a generic component provider mechanism for the specialised
 * requirements of particular optimisations.
 * 
 * @author proshun, Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IOptimisationData<T> {

	List<T> getSequenceElements();

	Collection<IResourceType> getResourceTypes();

	List<IResource> getResources();

	/** Accessor for specialised data. */
	<U extends IDataComponentProvider> U getDataComponentProvider(
			Class<U> clazz, String key);
}
