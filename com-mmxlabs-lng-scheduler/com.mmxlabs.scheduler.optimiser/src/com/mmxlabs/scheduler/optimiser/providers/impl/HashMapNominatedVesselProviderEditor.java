/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProviderEditor;

/**
 */
public class HashMapNominatedVesselProviderEditor implements INominatedVesselProviderEditor {

	private final Map<ISequenceElement, IVessel> elementToVesselMap = new HashMap<>();
	private final Map<IResource, IVessel> resourceToVesselMap = new HashMap<>();

	@Override
	@Nullable
	public IVessel getNominatedVessel(@NonNull final ISequenceElement element) {
		return elementToVesselMap.get(element);
	}

	@Override
	@Nullable
	public IVessel getNominatedVessel(@NonNull final IResource resource) {
		return resourceToVesselMap.get(resource);
	}

	@Override
	public void setNominatedVessel(@NonNull final ISequenceElement element, @NonNull final IResource resource, @NonNull final IVessel vessel) {
		elementToVesselMap.put(element, vessel);
		resourceToVesselMap.put(resource, vessel);
	}
}
