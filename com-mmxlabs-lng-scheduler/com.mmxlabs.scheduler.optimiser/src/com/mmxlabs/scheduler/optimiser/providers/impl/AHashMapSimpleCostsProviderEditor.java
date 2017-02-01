/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.ISimpleCostsProviderEditor;

public abstract class AHashMapSimpleCostsProviderEditor implements ISimpleCostsProviderEditor {

	private final Map<IPortSlot, Long> map = new HashMap<IPortSlot, Long>();
	
	@Override
	public long getCostsValue(final IPortSlot portSlot) {
		if (map.containsKey(portSlot)) {
			return map.get(portSlot);
		}

		return 0;
	}

	@Override
	public void setCostsValue(final IPortSlot portSlot, final long value) {
		map.put(portSlot, value);
	}
}
