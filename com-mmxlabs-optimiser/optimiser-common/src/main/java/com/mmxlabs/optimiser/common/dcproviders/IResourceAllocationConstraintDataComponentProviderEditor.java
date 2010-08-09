package com.mmxlabs.optimiser.common.dcproviders;

import java.util.Collection;

import com.mmxlabs.optimiser.core.IResource;

public interface IResourceAllocationConstraintDataComponentProviderEditor
		extends IResourceAllocationConstraintDataComponentProvider {

	void setAllowedResources(final Object element,
			final Collection<IResource> resources);

}
