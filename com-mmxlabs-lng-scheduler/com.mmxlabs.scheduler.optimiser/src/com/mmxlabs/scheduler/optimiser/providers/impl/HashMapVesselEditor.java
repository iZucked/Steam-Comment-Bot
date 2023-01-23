/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;

/**
 * Implementation of {@link IVesselProviderEditor} using a {@link HashMap} as the backing implementation.
 * 
 * @author Simon Goodall
 * 
 */
public final class HashMapVesselEditor implements IVesselProviderEditor {

	private final Map<IResource, IVesselCharter> resourceVesselCharterMap = new HashMap<>();
	private final Map<IResource, Boolean> resourceOptionalMap = new HashMap<>();
	private final Map<IVesselCharter, IResource> vesselCharterResourceMap = new HashMap<>();
	private final List<IResource> resources = new LinkedList<IResource>();

	@Override
	@NonNull
	public IVesselCharter getVesselCharter(@NonNull final IResource resource) {
		if (resourceVesselCharterMap.containsKey(resource)) {
			return resourceVesselCharterMap.get(resource);
		}

		throw new IllegalArgumentException("Unknown resource");
	}

	@Override
	@NonNull
	public IResource getResource(@NonNull final IVesselCharter vesselCharter) {
		if (vesselCharterResourceMap.containsKey(vesselCharter)) {
			return vesselCharterResourceMap.get(vesselCharter);
		}
		throw new IllegalArgumentException("Unknown vesselCharter");
	}

	@Override
	public void setVesselCharterResource(@NonNull final IResource resource, @NonNull final IVesselCharter vesselCharter) {
		vesselCharterResourceMap.put(vesselCharter, resource);
		resourceVesselCharterMap.put(resource, vesselCharter);
		if (!resources.contains(resource)) {
			resources.add(resource);
		}
	}

	@Override
	public List<IResource> getSortedResources() {
		return resources;
	}

	@Override
	public boolean isResourceOptional(@NonNull IResource resource) {
		Boolean optional = resourceOptionalMap.get(resource);
		return optional == true;
	}

	@Override
	public void setResourceOptional(@NonNull IResource resource, boolean optional) {
		resourceOptionalMap.put(resource, optional);
	}
}
