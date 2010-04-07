package com.mmxlabs.optimiser.scenario;

import java.util.Collection;

import com.mmxlabs.optimiser.IOptimisationContext;

/**
 * An interface to the optimisation data, comprising accessors for core
 * attributes and a generic component provider mechanism for the specialised
 * requirements of particular optimisations.
 * 
 * @author proshun
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IOptimisationData<T> {

	IOptimisationContext<T> getContext();

	Collection<IJobSite<?>> getJobs();

	Collection<IResourceType> getResourceTypes();

	Collection<IResource> getResources();

	/** Accessor for specialised data. */
	<U extends IDataComponentProvider> U getDataComponentProvider(
			Class<U> clazz, String key);
}
