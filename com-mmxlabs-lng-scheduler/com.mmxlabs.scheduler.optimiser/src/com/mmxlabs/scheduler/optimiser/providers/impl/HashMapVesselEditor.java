/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;

/**
 * Implementation of {@link IVesselProviderEditor} using a {@link HashMap} as the backing implementation.
 * 
 * @author Simon Goodall
 * 
 */
public final class HashMapVesselEditor implements IVesselProviderEditor {

	private final Map<IResource, IVesselAvailability> resourceVesselAvailabilityMap = new HashMap<>();
	private final Map<IVesselAvailability, IResource> vesselAvailabilityResourceMap = new HashMap<>();

	@Override
	@NonNull
	public IVesselAvailability getVesselAvailability(@NonNull final IResource resource) {
		if (resourceVesselAvailabilityMap.containsKey(resource)) {
			return resourceVesselAvailabilityMap.get(resource);
		}

		throw new IllegalArgumentException("Unknown resource");
	}

	@Override
	@NonNull
	public IResource getResource(@NonNull final IVesselAvailability vesselAvailability) {
		if (vesselAvailabilityResourceMap.containsKey(vesselAvailability)) {
			return vesselAvailabilityResourceMap.get(vesselAvailability);
		}
		throw new IllegalArgumentException("Unknown vesselAvailability");
	}

	@Override
	public void setVesselAvailabilityResource(@NonNull final IResource resource, @NonNull final IVesselAvailability vesselAvailability) {
		vesselAvailabilityResourceMap.put(vesselAvailability, resource);
		resourceVesselAvailabilityMap.put(resource, vesselAvailability);
	}
}
