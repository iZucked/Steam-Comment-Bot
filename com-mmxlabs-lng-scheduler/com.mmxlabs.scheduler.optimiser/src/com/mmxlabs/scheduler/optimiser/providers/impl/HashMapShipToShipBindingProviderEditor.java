/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IShipToShipBindingProviderEditor;

/**
 * @since 5.0
 */
public class HashMapShipToShipBindingProviderEditor implements IShipToShipBindingProviderEditor {

	private final String name;

	private final Map<IPortSlot, IPortSlot> transferElementMap = new HashMap<IPortSlot, IPortSlot>();

	public HashMapShipToShipBindingProviderEditor(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		transferElementMap.clear();
	}

	@Override
	public IPortSlot getConverseTransferElement(final IPortSlot slot) {
		return transferElementMap.get(slot);
	}

	@Override
	public void setConverseTransferElement(final IPortSlot a, final IPortSlot b) {
		transferElementMap.put(a, b);
		transferElementMap.put(b, a);
	}

}
