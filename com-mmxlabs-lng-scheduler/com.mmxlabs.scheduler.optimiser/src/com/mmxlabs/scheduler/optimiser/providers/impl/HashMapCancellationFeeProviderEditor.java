/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.ICancellationFeeProviderEditor;

/**
 * @author berkan
 *
 */
public class HashMapCancellationFeeProviderEditor implements ICancellationFeeProviderEditor {

	private final Map<IPortSlot, Long> map = new HashMap<IPortSlot, Long>();	
	
	@Override
	public long getCancellationFee(IPortSlot portSlot) {
		if (map.containsKey(portSlot)) {
			return map.get(portSlot);
		}

		return 0;
	}
	
	@Override
	public void setCancellationFee(IPortSlot portSlot, long fee) {
		map.put(portSlot, fee);
	}
}
