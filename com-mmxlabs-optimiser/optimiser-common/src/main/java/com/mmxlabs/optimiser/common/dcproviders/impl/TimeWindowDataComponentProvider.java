/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.common.dcproviders.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProviderEditor;

/**
 * Implementation of {@link ITimeWindowDataComponentEditor}
 * 
 * @author Simon Goodall
 * 
 */
public final class TimeWindowDataComponentProvider<T> implements
		ITimeWindowDataComponentProviderEditor<T> {

	private final String name;

	private final Map<Object, List<ITimeWindow>> timeWindowsMap;

	public TimeWindowDataComponentProvider(final String name) {
		this.name = name;
		this.timeWindowsMap = new HashMap<Object, List<ITimeWindow>>();
	}

	@Override
	public List<ITimeWindow> getTimeWindows(final T element) {
		if (timeWindowsMap.containsKey(element)) {
			return timeWindowsMap.get(element);
		}
		return Collections.emptyList();
	}

	@Override
	public void setTimeWindows(final T element,
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
