/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import java.util.Collection;

import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProviderEditor;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

public final class IndexedResourceAllocationConstraintProvider implements IResourceAllocationConstraintDataComponentProviderEditor {

	private final IIndexMap<ISequenceElement, Collection<IResource>> allowedResources = new ArrayIndexMap<ISequenceElement, Collection<IResource>>();

	@Override
	public Collection<IResource> getAllowedResources(final ISequenceElement element) {
		return allowedResources.maybeGet(element);
	}

	@Override
	public void setAllowedResources(final ISequenceElement element, final Collection<IResource> resources) {
		allowedResources.set(element, resources);
	}
}
