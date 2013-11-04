package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProviderEditor;

public class HashMapShippingHoursRestrictionProviderEditor implements IShippingHoursRestrictionProviderEditor {

	private final String name;

	private final Map<ISequenceElement, Integer> map = new HashMap<>();

	public HashMapShippingHoursRestrictionProviderEditor(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		map.clear();
	}

	@Override
	public int getShippingHoursRestriction(@NonNull final ISequenceElement element) {
		if (map.containsKey(element)) {
			return map.get(element);
		}
		return IShippingHoursRestrictionProviderEditor.RESTRICTION_UNDEFINED;
	}

	@Override
	public void setShippingHoursRestriction(@NonNull final ISequenceElement element, final int hours) {
		map.put(element, hours);
	}
}
