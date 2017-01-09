/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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

	private final Map<@NonNull ISequenceElement, @Nullable Collection<@NonNull IResource>> allowedResourceMap = new HashMap<>();

	@Override
	public Collection<@NonNull IResource> getAllowedResources(final @NonNull ISequenceElement element) {

		return allowedResourceMap.getOrDefault(element, null);
	}

	@Override
	public void setAllowedResources(final @NonNull ISequenceElement element, final @Nullable Collection<@NonNull IResource> resources) {
		allowedResourceMap.put(element, resources);
	}
}
