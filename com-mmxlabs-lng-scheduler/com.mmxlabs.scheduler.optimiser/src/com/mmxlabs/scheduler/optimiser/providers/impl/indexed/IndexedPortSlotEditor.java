/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl.indexed;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.WrappedSequenceElement;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public final class IndexedPortSlotEditor implements IPortSlotProviderEditor {
	private final IIndexMap<@NonNull ISequenceElement, @NonNull IPortSlot> slots = new ArrayIndexMap<>();

	// TODO maybe make this indexed as well, although the getElement method is not called in the main loop
	private final Map<@NonNull IPortSlot, @NonNull ISequenceElement> elements = new HashMap<>();

	@Override
	public final IPortSlot getPortSlot(final @NonNull ISequenceElement element) {
		if (element instanceof WrappedSequenceElement) {
			return ((WrappedSequenceElement) element).getPortSlot();
		}

		return slots.get(element);
	}

	@Override
	public final ISequenceElement getElement(final @NonNull IPortSlot portSlot) {
		if (portSlot.getPortType() == PortType.GeneratedCharterOut) {
			return new WrappedSequenceElement(portSlot);

		}
		return elements.get(portSlot);
	}

	@Override
	public final void setPortSlot(final @NonNull ISequenceElement element, final @NonNull IPortSlot portSlot) {
		slots.set(element, portSlot);
		elements.put(portSlot, element);
	}
}
