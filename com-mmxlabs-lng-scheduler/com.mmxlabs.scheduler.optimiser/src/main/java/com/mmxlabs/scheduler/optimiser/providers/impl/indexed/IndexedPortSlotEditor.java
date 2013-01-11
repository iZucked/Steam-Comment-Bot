/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl.indexed;

import java.util.HashMap;

import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;

public final class IndexedPortSlotEditor implements IPortSlotProviderEditor {
	private final String name;
	private final IIndexMap<ISequenceElement, IPortSlot> slots = new ArrayIndexMap<ISequenceElement, IPortSlot>();

	// TODO maybe make this indexed as well, although the getElement method is not called in the main loop
	private final HashMap<IPortSlot, ISequenceElement> elements = new HashMap<IPortSlot, ISequenceElement>();

	public IndexedPortSlotEditor(final String name) {
		super();
		this.name = name;
	}

	@Override
	public final IPortSlot getPortSlot(final ISequenceElement element) {
		return slots.maybeGet(element);
	}

	@Override
	public final ISequenceElement getElement(final IPortSlot portSlot) {
		return elements.get(portSlot);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		elements.clear();
		slots.clear();
	}

	@Override
	public final void setPortSlot(final ISequenceElement element, final IPortSlot portSlot) {
		slots.set(element, portSlot);
		elements.put(portSlot, element);
	}
}
