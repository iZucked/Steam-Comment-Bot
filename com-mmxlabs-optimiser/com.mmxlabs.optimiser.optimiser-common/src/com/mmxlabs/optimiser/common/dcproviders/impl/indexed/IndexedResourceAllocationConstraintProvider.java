/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProviderEditor;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

public final class IndexedResourceAllocationConstraintProvider implements IResourceAllocationConstraintDataComponentProviderEditor {

	private final IIndexMap<@NonNull ISequenceElement, @Nullable Collection<@NonNull IResource>> allowedResources = new ArrayIndexMap<>();

	@Override
	@Nullable
	public Collection<@NonNull IResource> getAllowedResources(@NonNull final ISequenceElement element) {
		return allowedResources.maybeGet(element);
	}

	@Override
	public void setAllowedResources(@NonNull final ISequenceElement element, @Nullable final Collection<@NonNull IResource> resources) {
		allowedResources.set(element, resources);
	}
}
