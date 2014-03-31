/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProviderEditor;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * {@link IDataComponentProvider} implementation to provide {@link IResource} allocation constraints for sequence elements.
 * 
 * @author Simon Goodall
 * 
 */
public final class ResourceAllocationConstraintProvider implements IResourceAllocationConstraintDataComponentProviderEditor {

	private final Map<ISequenceElement, Collection<IResource>> allowedResourceMap;

	public ResourceAllocationConstraintProvider() {
		this.allowedResourceMap = new HashMap<ISequenceElement, Collection<IResource>>();
	}

	@Override
	public Collection<IResource> getAllowedResources(final ISequenceElement element) {

		if (allowedResourceMap.containsKey(element)) {
			return allowedResourceMap.get(element);
		}

		return null;
	}

	@Override
	public void setAllowedResources(final ISequenceElement element, final Collection<IResource> resources) {
		allowedResourceMap.put(element, resources);
	}
}
