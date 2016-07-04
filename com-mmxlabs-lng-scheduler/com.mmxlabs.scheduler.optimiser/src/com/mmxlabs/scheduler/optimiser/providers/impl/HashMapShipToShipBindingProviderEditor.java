/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IShipToShipBindingProviderEditor;

/**
 */
public class HashMapShipToShipBindingProviderEditor implements IShipToShipBindingProviderEditor {

	private final Map<IPortSlot, IPortSlot> transferElementMap = new HashMap<IPortSlot, IPortSlot>();

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
