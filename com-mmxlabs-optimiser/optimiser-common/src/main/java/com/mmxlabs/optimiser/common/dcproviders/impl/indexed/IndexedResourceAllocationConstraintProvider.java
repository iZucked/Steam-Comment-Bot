/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import java.util.Collection;

import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.IIndexedObject;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProviderEditor;
import com.mmxlabs.optimiser.core.IResource;

public final class IndexedResourceAllocationConstraintProvider<T extends IIndexedObject> implements
		IResourceAllocationConstraintDataComponentProviderEditor<T> {

	private final String name;

	private final IIndexMap<T, Collection<IResource>> allowedResources = 
		new ArrayIndexMap<T, Collection<IResource>>();

	public IndexedResourceAllocationConstraintProvider(String name) {
		super();
		this.name = name;
	}

	@Override
	public Collection<IResource> getAllowedResources(final T element) {
		return allowedResources.maybeGet(element);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		allowedResources.clear();
	}

	@Override
	public void setAllowedResources(final T element,
			final Collection<IResource> resources) {
		allowedResources.set(element, resources);
	}
}
