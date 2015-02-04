/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProviderEditor;

/**
 */
public class HashMapShippingHoursRestrictionProviderEditor implements IShippingHoursRestrictionProviderEditor {

	private final Map<ISequenceElement, Integer> hoursMap = new HashMap<>();
	private final Map<ISequenceElement, ITimeWindow> baseTimeMap = new HashMap<>();
	private final Map<IVessel, Integer> referenceSpeeds = new HashMap<>();

	@Override
	public int getShippingHoursRestriction(@NonNull final ISequenceElement element) {
		if (hoursMap.containsKey(element)) {
			return hoursMap.get(element);
		}
		return RESTRICTION_UNDEFINED;
	}

	@Override
	public ITimeWindow getBaseTime(@NonNull final ISequenceElement element) {
		if (baseTimeMap.containsKey(element)) {
			return baseTimeMap.get(element);
		}
		return null;
	}

	@Override
	public void setShippingHoursRestriction(@NonNull final ISequenceElement element, final @NonNull ITimeWindow baseTime, final int hours) {
		hoursMap.put(element, hours);
		baseTimeMap.put(element, baseTime);
	}

	@Override
	public boolean isDivertable(@NonNull final ISequenceElement element) {
		return hoursMap.containsKey(element);
	}

	@Override
	public int getReferenceSpeed(@NonNull final IVessel vessel) {
		return referenceSpeeds.get(vessel);
	}

	@Override
	public void setReferenceSpeed(@NonNull final IVessel vessel, final int referenceSpeed) {
		referenceSpeeds.put(vessel, referenceSpeed);
	}

}
