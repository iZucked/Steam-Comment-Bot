/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

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
	public IVesselAvailability getVesselAvailability(final IResource resource) {
		if (resourceVesselAvailabilityMap.containsKey(resource)) {
			return resourceVesselAvailabilityMap.get(resource);
		}

		// TODO: Error?
		return null;
	}

	@Override
	public IResource getResource(final IVesselAvailability vesselAvailability) {
		if (vesselAvailabilityResourceMap.containsKey(vesselAvailability)) {
			return vesselAvailabilityResourceMap.get(vesselAvailability);
		}

		// TODO: Error?
		return null;
	}

	@Override
	public void setVesselAvailabilityResource(final IResource resource, final IVesselAvailability vesselAvailability) {
		vesselAvailabilityResourceMap.put(vesselAvailability, resource);
		resourceVesselAvailabilityMap.put(resource, vesselAvailability);
	}
}
