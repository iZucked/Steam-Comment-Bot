/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

public interface IResourceAllocationConstraintDataComponentProviderEditor extends IResourceAllocationConstraintDataComponentProvider {

	void setAllowedResources(@NonNull final ISequenceElement element, @Nullable final Collection<@NonNull IResource> resources);

}
