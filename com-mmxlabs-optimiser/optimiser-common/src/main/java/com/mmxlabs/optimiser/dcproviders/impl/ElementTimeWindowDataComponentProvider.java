package com.mmxlabs.optimiser.dcproviders.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.optimiser.dcproviders.IElementTimeWindowDataComponentProvider;

/**
 * Implementation of {@link IElementTimeWindowDataComponentProvider}.
 * 
 * @author Simon Goodall
 * 
 */
public final class ElementTimeWindowDataComponentProvider implements
		IElementTimeWindowDataComponentProvider {

	private final String name;

	private final Map<Object, List<ITimeWindow>> timeWindowsMap;

	public ElementTimeWindowDataComponentProvider(final String name) {
		this.name = name;
		this.timeWindowsMap = new HashMap<Object, List<ITimeWindow>>();
	}

	@Override
	public List<ITimeWindow> getTimeWindows(Object element) {
		if (timeWindowsMap.containsKey(element)) {
			return timeWindowsMap.get(element);
		}
		return null;
	}

	@Override
	public void setTimeWindows(Object element, List<ITimeWindow> timeWindows) {
		this.timeWindowsMap.put(element, timeWindows);
	}

	@Override
	public String getName() {
		return name;
	}
}
