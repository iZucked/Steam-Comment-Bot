package com.mmxlabs.optimiser.dcproviders.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.dcproviders.IResourceAllocationConstraintDataComponentProviderEditor;
import com.mmxlabs.optimiser.scenario.IDataComponentProvider;

/**
 * {@link IDataComponentProvider} implementation to provide {@link IResource}
 * allocation constraints for sequence elements.
 * 
 * @author Simon Goodall
 * 
 */
public final class ResourceAllocationConstraintProvider implements
		IResourceAllocationConstraintDataComponentProviderEditor {

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

	@Override
	public Collection<IResource> getAllowedResources(final Object element) {

		if (allowedResourceMap.containsKey(element)) {
			return allowedResourceMap.get(element);
		}

		return null;
	}

	@Override
	public void setAllowedResources(final Object element,
			final Collection<IResource> resources) {
		allowedResourceMap.put(element, resources);
	}

	@Override
	public void dispose() {
		allowedResourceMap.clear();
	}
}
