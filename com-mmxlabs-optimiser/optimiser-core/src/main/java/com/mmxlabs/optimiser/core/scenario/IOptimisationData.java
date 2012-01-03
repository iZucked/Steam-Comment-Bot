/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.scenario;

import java.util.Collection;
import java.util.List;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * An interface to the optimisation data, comprising accessors for core attributes and a generic component provider mechanism for the specialised requirements of particular optimisations.
 * 
 * @author proshun, Simon Goodall
 * 
 */
public interface IOptimisationData {

	/**
	 * Returns a list of all the sequence elements in the optimisation.
	 * 
	 * @return
	 */
	List<ISequenceElement> getSequenceElements();

	/**
	 * Returns a list of all the {@link IResource}s in the optimisation.
	 */
	List<IResource> getResources();

	/**
	 * Accessor for specialised data objects. Each object should have a unique key and an expected class type.
	 * 
	 * @param <U>
	 * @param key
	 * @param clazz
	 * @return
	 */
	<U extends IDataComponentProvider> U getDataComponentProvider(String key, Class<U> clazz);

	/**
	 * Returns true if the named {@link IDataComponentProvider} exists.
	 * 
	 * @param key
	 * @return
	 */
	boolean hasDataComponentProvider(String key);

	/**
	 * Returns the list of registered {@link IDataComponentProvider}s.
	 * 
	 * @return
	 */
	Collection<String> getDataComponentProviders();

	/**
	 * Notify {@link IOptimisationData} that it is no longer required and clean up internal references. This should also invoke dispose on any contained {@link IDataComponentProvider} implementations.
	 */
	void dispose();

}
