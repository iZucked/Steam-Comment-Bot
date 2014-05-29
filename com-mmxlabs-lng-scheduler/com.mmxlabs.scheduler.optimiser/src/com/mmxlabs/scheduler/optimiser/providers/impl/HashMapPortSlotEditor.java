/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;

public final class HashMapPortSlotEditor implements IPortSlotProviderEditor {

	private final Map<ISequenceElement, IPortSlot> portSlotMap = new HashMap<ISequenceElement, IPortSlot>();
	private final Map<IPortSlot, ISequenceElement> elementMap = new HashMap<IPortSlot, ISequenceElement>();

	@Override
	public void setPortSlot(final ISequenceElement element, final IPortSlot portSlot) {
		portSlotMap.put(element, portSlot);
		elementMap.put(portSlot, element);
	}

	@Override
	public IPortSlot getPortSlot(final ISequenceElement element) {

		if (portSlotMap.containsKey(element)) {
			return portSlotMap.get(element);
		}
		return null;
	}

	@Override
	public ISequenceElement getElement(final IPortSlot portSlot) {
		if (elementMap.containsKey(portSlot)) {
			return elementMap.get(portSlot);
		}
		return null;
	}
}
