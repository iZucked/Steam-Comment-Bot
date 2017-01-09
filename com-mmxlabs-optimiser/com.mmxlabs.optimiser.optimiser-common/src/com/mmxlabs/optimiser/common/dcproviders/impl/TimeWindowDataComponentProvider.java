/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

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
	private final Map<ISequenceElement, List<@NonNull ITimeWindow>> timeWindowsMap;

	public TimeWindowDataComponentProvider() {
		this.timeWindowsMap = new HashMap<>();
	}

	@Override
	public List<@NonNull ITimeWindow> getTimeWindows(final @NonNull ISequenceElement element) {
		if (timeWindowsMap.containsKey(element)) {
			return timeWindowsMap.getOrDefault(element, Collections.emptyList());
		}
		return Collections.emptyList();
	}

	@Override
	public void setTimeWindows(final ISequenceElement element, final List<@NonNull ITimeWindow> timeWindows) {
		this.timeWindowsMap.put(element, timeWindows);
	}
}
