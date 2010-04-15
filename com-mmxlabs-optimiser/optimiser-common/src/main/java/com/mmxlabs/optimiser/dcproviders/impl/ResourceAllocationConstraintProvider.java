package com.mmxlabs.optimiser.dcproviders.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.dcproviders.IResourceAllocationConstraintDataComponentProvider;
import com.mmxlabs.optimiser.scenario.IDataComponentProvider;

/**
 * {@link IDataComponentProvider} implementation to provide {@link IResource}
 * allocation constraints for sequence elements.
 * 
 * @author Simon Goodall
 * 
 */
public final class ResourceAllocationConstraintProvider implements
		IResourceAllocationConstraintDataComponentProvider {

	private final String name;

	private final Map<Object, Collection<IResource>> allowedResourceMap;

	public ResourceAllocationConstraintProvider(final String name) {
		this.name = name;
		this.allowedResourceMap = new HashMap<Object, Collection<IResource>>();
	}

	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.optimiser.dcproviders.IResourceAllocationDataComponentProvider
	 * #getAllowedResources(java.lang.Object)
	 */
	// TODO: Should this be templated?
	public Collection<IResource> getAllowedResources(final Object element) {

		if (allowedResourceMap.containsKey(element)) {
			return allowedResourceMap.get(element);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.optimiser.dcproviders.IResourceAllocationDataComponentProvider
	 * #setAllowedResources(java.lang.Object, java.util.Collection)
	 */
	public void setAllowedResources(final Object element,
			final Collection<IResource> resources) {
		allowedResourceMap.put(element, resources);
	}

}
