/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProviderEditor;

/**
 * Implementation of {@link IVesselPrIVirtualVesselSlotProviderEditoroviderEditor} using a {@link HashMap} as the backing implementation.
 * 
 * @author Simon Goodall
 * 
 */
public final class HashMapVirtualVesselSlotProviderEditor implements IVirtualVesselSlotProviderEditor {

	private final Map<ISequenceElement, IVesselAvailability> elementVesselMap = new HashMap<>();
	private final Map<IVesselAvailability, ISequenceElement> vesselElementMap = new HashMap<>();

	@Override
	public IVesselAvailability getVesselAvailabilityForElement(final ISequenceElement element) {
		if (elementVesselMap.containsKey(element)) {
			return elementVesselMap.get(element);
		}

		// TODO: Error?
		return null;
	}

	@Override
	public ISequenceElement getElementForVesselAvailability(final IVesselAvailability vesselAvailability) {
		if (vesselElementMap.containsKey(vesselAvailability)) {
			return vesselElementMap.get(vesselAvailability);
		}

		// TODO: Error?
		return null;
	}

	@Override
	public void setVesselAvailabilityForElement(final IVesselAvailability vesselAvailability, final ISequenceElement element) {
		vesselElementMap.put(vesselAvailability, element);
		elementVesselMap.put(element, vesselAvailability);
	}
}
