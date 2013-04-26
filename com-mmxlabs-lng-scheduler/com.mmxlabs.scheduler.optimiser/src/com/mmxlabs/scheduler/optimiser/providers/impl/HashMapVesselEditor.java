/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;

/**
 * Implementation of {@link IVesselProviderEditor} using a {@link HashMap} as the backing implementation.
 * 
 * @author Simon Goodall
 * 
 */
public final class HashMapVesselEditor implements IVesselProviderEditor {

	private final String name;

	private final Map<IResource, IVessel> resourceVesselMap = new HashMap<IResource, IVessel>();
	private final Map<IVessel, IResource> vesselResourceMap = new HashMap<IVessel, IResource>();

	public HashMapVesselEditor(final String name) {
		this.name = name;
	}

	@Override
	public IVessel getVessel(final IResource resource) {
		if (resourceVesselMap.containsKey(resource)) {
			return resourceVesselMap.get(resource);
		}

		// TODO: Error?
		return null;
	}

	@Override
	public IResource getResource(final IVessel vessel) {
		if (vesselResourceMap.containsKey(vessel)) {
			return vesselResourceMap.get(vessel);
		}

		// TODO: Error?
		return null;
	}

	@Override
	public void setVesselResource(final IResource resource, final IVessel vessel) {
		vesselResourceMap.put(vessel, resource);
		resourceVesselMap.put(resource, vessel);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		vesselResourceMap.clear();
		resourceVesselMap.clear();
	}
}
