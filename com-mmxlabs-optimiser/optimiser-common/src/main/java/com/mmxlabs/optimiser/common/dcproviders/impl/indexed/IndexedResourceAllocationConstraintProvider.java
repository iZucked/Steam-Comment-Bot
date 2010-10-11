package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import java.util.Collection;

import com.mmxlabs.common.indexedobjects.AutoSizingArrayList;
import com.mmxlabs.common.indexedobjects.IIndexedObject;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProviderEditor;
import com.mmxlabs.optimiser.core.IResource;

public class IndexedResourceAllocationConstraintProvider<T extends IIndexedObject> implements
		IResourceAllocationConstraintDataComponentProviderEditor<T> {

	private final String name;

	private final AutoSizingArrayList<Collection<IResource>> allowedResources = new AutoSizingArrayList<Collection<IResource>>();

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
	public void setAllowedResources(T element,
			Collection<IResource> resources) {
		allowedResources.set(element, resources);
	}

}
