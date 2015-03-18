/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders;

import java.util.Collection;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

public interface IResourceAllocationConstraintDataComponentProviderEditor extends IResourceAllocationConstraintDataComponentProvider {

	void setAllowedResources(final ISequenceElement element, final Collection<IResource> resources);

}
