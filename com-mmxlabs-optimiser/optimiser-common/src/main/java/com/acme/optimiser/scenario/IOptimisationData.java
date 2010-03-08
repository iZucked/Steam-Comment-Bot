package com.acme.optimiser.scenario;

import java.util.Collection;

import com.acme.optimiser.IOptimisationContext;

/**
 * An interface to the optimisation data, comprising accessors for core attributes 
 * and a generic component provider mechanism for the specialised requirements of 
 * particular optimisations.
 * 
 * @author proshun
 */
public interface IOptimisationData {

	IOptimisationContext getContext();
	
	Collection<IJobSite<?>> getJobs();

	Collection<IResourceType> getResourceTypes();

	Collection<IResource> getResources();

	/** Accessor for specialised data. */
	<T extends IDataComponentProvider> T getDataComponentProvider(Class<T> clazz, String key);
}
