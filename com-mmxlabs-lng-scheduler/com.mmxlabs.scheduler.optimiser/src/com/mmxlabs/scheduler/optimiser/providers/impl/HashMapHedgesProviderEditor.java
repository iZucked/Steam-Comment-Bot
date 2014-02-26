/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IHedgesProviderEditor;

/**
 * Implementation of {@link IHedgesProviderEditor} using a {@link HashMap} as the backing implementation.
 * 
 * @author berkan
 *
 */
public class HashMapHedgesProviderEditor implements IHedgesProviderEditor {

	private final Map<IPortSlot, Long> map = new HashMap<IPortSlot, Long>();
	
	@Override
	public long getHedgeCost(final IPortSlot portSlot) {
		if (map.containsKey(portSlot)) {
			return map.get(portSlot);
		}

		return 0;
	}

	public void setHedgeCost(final IPortSlot portSlot, final long cost) {
		map.put(portSlot, cost);
	}
	
	@Override
	public String getName() {
		return "";
	}

	@Override
	public void dispose() {
		map.clear();
	}

}
