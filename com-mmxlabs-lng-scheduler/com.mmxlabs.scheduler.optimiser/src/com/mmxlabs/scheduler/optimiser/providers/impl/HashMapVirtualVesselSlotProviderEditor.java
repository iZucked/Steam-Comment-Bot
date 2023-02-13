/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProviderEditor;

/**
 * Implementation of {@link IVesselPrIVirtualVesselSlotProviderEditoroviderEditor} using a {@link HashMap} as the backing implementation.
 * 
 * @author Simon Goodall
 * 
 */
public final class HashMapVirtualVesselSlotProviderEditor implements IVirtualVesselSlotProviderEditor {

	private final Map<ISequenceElement, IVesselCharter> elementVesselMap = new HashMap<>();
	private final Map<IVesselCharter, ISequenceElement> vesselElementMap = new HashMap<>();

	@Override
	public IVesselCharter getVesselCharterForElement(final ISequenceElement element) {
		if (elementVesselMap.containsKey(element)) {
			return elementVesselMap.get(element);
		}

		// TODO: Error?
		return null;
	}

	@Override
	public ISequenceElement getElementForVesselCharter(final IVesselCharter vesselCharter) {
		if (vesselElementMap.containsKey(vesselCharter)) {
			return vesselElementMap.get(vesselCharter);
		}

		// TODO: Error?
		return null;
	}

	@Override
	public void setVesselCharterForElement(final IVesselCharter vesselCharter, final ISequenceElement element) {
		vesselElementMap.put(vesselCharter, element);
		elementVesselMap.put(element, vesselCharter);
	}
}
