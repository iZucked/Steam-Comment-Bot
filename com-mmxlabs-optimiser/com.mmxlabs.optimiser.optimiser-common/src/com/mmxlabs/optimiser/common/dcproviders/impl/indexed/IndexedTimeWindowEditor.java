/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProviderEditor;
import com.mmxlabs.optimiser.core.ISequenceElement;

public class IndexedTimeWindowEditor implements ITimeWindowDataComponentProviderEditor {

	private final IIndexMap<@NonNull ISequenceElement, @Nullable List<@NonNull ITimeWindow>> timeWindowsByElement = new ArrayIndexMap<>();

	@SuppressWarnings("null")
	@Override
	@NonNull
	public List<@NonNull ITimeWindow> getTimeWindows(final @NonNull ISequenceElement element) {
		final List<@NonNull ITimeWindow> windows = timeWindowsByElement.maybeGet(element);
		if (windows == null) {
			return Collections.emptyList();
		} else {
			return windows;
		}
	}

	@Override
	public void setTimeWindows(final @NonNull ISequenceElement element, final @Nullable List<@NonNull ITimeWindow> timeWindows) {
		timeWindowsByElement.set(element, timeWindows);
	}
}
