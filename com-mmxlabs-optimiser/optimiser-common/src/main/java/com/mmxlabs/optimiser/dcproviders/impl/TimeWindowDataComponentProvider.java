package com.mmxlabs.optimiser.dcproviders.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.optimiser.dcproviders.ITimeWindowDataComponentProviderEditor;

/**
 * Implementation of {@link ITimeWindowDataComponentEditor}
 * 
 * @author Simon Goodall
 * 
 */
public final class TimeWindowDataComponentProvider implements
		ITimeWindowDataComponentProviderEditor {

	private final String name;

	private final Map<Object, List<ITimeWindow>> timeWindowsMap;

	public TimeWindowDataComponentProvider(final String name) {
		this.name = name;
		this.timeWindowsMap = new HashMap<Object, List<ITimeWindow>>();
	}

	@Override
	public List<ITimeWindow> getTimeWindows(final Object element) {
		if (timeWindowsMap.containsKey(element)) {
			return timeWindowsMap.get(element);
		}
		return Collections.emptyList();
	}

	@Override
	public void setTimeWindows(final Object element,
			final List<ITimeWindow> timeWindows) {
		this.timeWindowsMap.put(element, timeWindows);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		timeWindowsMap.clear();
	}
}
