/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders;

import java.util.Collection;

import com.mmxlabs.optimiser.core.IResource;

public interface IResourceAllocationConstraintDataComponentProviderEditor<T>
		extends IResourceAllocationConstraintDataComponentProvider<T> {

	void setAllowedResources(final T element,
			final Collection<IResource> resources);

}
