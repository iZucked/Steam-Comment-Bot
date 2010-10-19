/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.core.scenario;

import java.util.List;

import com.mmxlabs.optimiser.core.IResource;

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

	/**
	 * Returns a list of all the sequence elements in the optimisation.
	 * 
	 * @return
	 */
	List<T> getSequenceElements();

	// Collection<IResourceType> getResourceTypes();

	/**
	 * Returns a list of all the {@link IResource}s in the optimisation.
	 */
	List<IResource> getResources();

	/**
	 * Accessor for specialised data objects. Each object should have a unique
	 * key and an expected class type.
	 * 
	 * @param <U>
	 * @param key
	 * @param clazz
	 * @return
	 */
	<U extends IDataComponentProvider> U getDataComponentProvider(String key,
			Class<U> clazz);

	/**
	 * Returns true if the named {@link IDataComponentProvider} exists.
	 * 
	 * @param key
	 * @return
	 */
	boolean hasDataComponentProvider(String key);

	/**
	 * Notify {@link IOptimisationData} that it is no longer required and clean
	 * up internal references. This should also invoke dispose on any contained
	 * {@link IDataComponentProvider} implementations.
	 */
	void dispose();

}
