package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProviderEditor;

public class HashMapShippingHoursRestrictionProviderEditor implements IShippingHoursRestrictionProviderEditor {

	private final String name;

	private final Map<ISequenceElement, Integer> hoursMap = new HashMap<>();
	private final Map<ISequenceElement, ITimeWindow> baseTimeMap = new HashMap<>();

	public HashMapShippingHoursRestrictionProviderEditor(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		hoursMap.clear();
		baseTimeMap.clear();
	}

	@Override
	public int getShippingHoursRestriction(@NonNull final ISequenceElement element) {
		if (hoursMap.containsKey(element)) {
			return hoursMap.get(element);
		}
		return IShippingHoursRestrictionProviderEditor.RESTRICTION_UNDEFINED;
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
}
