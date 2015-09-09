/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;

public final class HashMapPortSlotEditor implements IPortSlotProviderEditor {

	@NonNull
	private final Map<ISequenceElement, IPortSlot> portSlotMap = new HashMap<ISequenceElement, IPortSlot>();
	@NonNull
	private final Map<IPortSlot, ISequenceElement> elementMap = new HashMap<IPortSlot, ISequenceElement>();

	@Override
	public void setPortSlot(@NonNull final ISequenceElement element, @NonNull final IPortSlot portSlot) {
		portSlotMap.put(element, portSlot);
		elementMap.put(portSlot, element);
	}

	@SuppressWarnings("null")
	@Override
	public IPortSlot getPortSlot(@NonNull final ISequenceElement element) {

		if (portSlotMap.containsKey(element)) {
			return portSlotMap.get(element);
		}
		throw new IllegalArgumentException("Unknown sequence element");
	}

	@SuppressWarnings("null")
	@Override
	public ISequenceElement getElement(@NonNull final IPortSlot portSlot) {
		if (elementMap.containsKey(portSlot)) {
			return elementMap.get(portSlot);
		}
		throw new IllegalArgumentException("Unknown port slot");
	}
}
