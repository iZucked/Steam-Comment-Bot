package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IMinTravelTimeProviderEditor;

@NonNullByDefault
public class DefaultMinTravelTimeProviderEditor implements IMinTravelTimeProviderEditor {

	private final Map<IPortSlot, Integer> minTravelTimes = new HashMap<>();

	@Override
	public @Nullable Integer getMinTravelTime(final IPortSlot slot) {
		return minTravelTimes.get(slot);
	}

	@Override
	public void setMinTravelTime(final IPortSlot slot, final int minTravelTime) {
		if (minTravelTime <= 0) {
			throw new IllegalArgumentException(String.format("Minimum travel time for slot %s cannot be zero or less", slot.getId()));
		}
		minTravelTimes.put(slot, minTravelTime);
	}

}
