/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.providers.ISpotMarketSlotsProviderEditor;

public final class HashMapSpotMarketSlotsEditor implements ISpotMarketSlotsProviderEditor {

	private final Map<ISequenceElement, Boolean> spotMarketSlots = new HashMap<ISequenceElement, Boolean>();

	@Override
	public void setSpotMarketSlot(final ISequenceElement sequenceElement, final boolean isSpotMarketSlot) {
		spotMarketSlots.put(sequenceElement, isSpotMarketSlot);
	}

	@Override
	public boolean isSpotMarketSlot(final ISequenceElement sequenceElement) {

		if (spotMarketSlots.containsKey(sequenceElement)) {
			return spotMarketSlots.get(sequenceElement);
		}

		return false;
	}
}
