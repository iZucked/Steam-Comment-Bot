/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;

public final class HashMapPortSlotEditor<T> implements
		IPortSlotProviderEditor<T> {

	private final String name;

	private final Map<T, IPortSlot> portSlotMap = new HashMap<T, IPortSlot>();
	private final Map<IPortSlot, T> elementMap = new HashMap<IPortSlot, T>();

	public HashMapPortSlotEditor(final String name) {
		this.name = name;
	}

	@Override
	public void setPortSlot(final T element, final IPortSlot portSlot) {
		portSlotMap.put(element, portSlot);
		elementMap.put(portSlot, element);
	}

	@Override
	public IPortSlot getPortSlot(final T element) {

		if (portSlotMap.containsKey(element)) {
			return portSlotMap.get(element);
		}
		return null;
	}

	@Override
	public void dispose() {
		portSlotMap.clear();
		elementMap.clear();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public T getElement(IPortSlot portSlot) {
		if (elementMap.containsKey(portSlot)) {
			return elementMap.get(portSlot);
		}
		return null;
	}
}
