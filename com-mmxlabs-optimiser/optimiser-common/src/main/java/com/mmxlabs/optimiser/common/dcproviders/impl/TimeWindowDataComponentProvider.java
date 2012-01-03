/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProviderEditor;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Implementation of {@link ITimeWindowDataComponentEditor}
 * 
 * @author Simon Goodall
 * 
 */
public final class TimeWindowDataComponentProvider implements ITimeWindowDataComponentProviderEditor {

	private final String name;

	private final Map<ISequenceElement, List<ITimeWindow>> timeWindowsMap;

	public TimeWindowDataComponentProvider(final String name) {
		this.name = name;
		this.timeWindowsMap = new HashMap<ISequenceElement, List<ITimeWindow>>();
	}

	@Override
	public List<ITimeWindow> getTimeWindows(final ISequenceElement element) {
		if (timeWindowsMap.containsKey(element)) {
			return timeWindowsMap.get(element);
		}
		return Collections.emptyList();
	}

	@Override
	public void setTimeWindows(final ISequenceElement element, final List<ITimeWindow> timeWindows) {
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
