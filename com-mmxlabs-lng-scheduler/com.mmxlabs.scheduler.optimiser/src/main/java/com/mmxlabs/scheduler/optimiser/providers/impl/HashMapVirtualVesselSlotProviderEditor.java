/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProviderEditor;

/**
 * Implementation of {@link IVesselPrIVirtualVesselSlotProviderEditoroviderEditor} using a {@link HashMap} as the backing implementation.
 * 
 * @author Simon Goodall
 * 
 */
public final class HashMapVirtualVesselSlotProviderEditor implements IVirtualVesselSlotProviderEditor {

	private final String name;

	private final Map<ISequenceElement, IVessel> elementVesselMap = new HashMap<ISequenceElement, IVessel>();
	private final Map<IVessel, ISequenceElement> vesselElementMap = new HashMap<IVessel, ISequenceElement>();

	public HashMapVirtualVesselSlotProviderEditor(final String name) {
		this.name = name;
	}

	@Override
	public IVessel getVesselForElement(final ISequenceElement element) {
		if (elementVesselMap.containsKey(element)) {
			return elementVesselMap.get(element);
		}

		// TODO: Error?
		return null;
	}

	@Override
	public ISequenceElement getElementForVessel(final IVessel vessel) {
		if (vesselElementMap.containsKey(vessel)) {
			return vesselElementMap.get(vessel);
		}

		// TODO: Error?
		return null;
	}

	@Override
	public void setVesselForElement(final IVessel vessel, ISequenceElement element) {
		vesselElementMap.put(vessel, element);
		elementVesselMap.put(element, vessel);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		vesselElementMap.clear();
		elementVesselMap.clear();
	}
}
