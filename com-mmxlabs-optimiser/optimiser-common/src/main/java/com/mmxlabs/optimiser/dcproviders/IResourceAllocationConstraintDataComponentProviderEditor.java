package com.mmxlabs.optimiser.dcproviders;

import java.util.Collection;

import com.mmxlabs.optimiser.IResource;

public interface IResourceAllocationConstraintDataComponentProviderEditor
		extends IResourceAllocationConstraintDataComponentProvider {

	void setAllowedResources(final Object element,
			final Collection<IResource> resources);

}
