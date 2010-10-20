/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import java.util.Collections;
import java.util.List;

import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.IIndexedObject;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProviderEditor;

public class IndexedTimeWindowEditor<T extends IIndexedObject> implements
		ITimeWindowDataComponentProviderEditor<T> {

	private final IIndexMap<T, List<ITimeWindow>> timeWindowsByElement = new ArrayIndexMap<T, List<ITimeWindow>>();

	private final String name;

	public IndexedTimeWindowEditor(String name) {
		super();
		this.name = name;
	}

	@Override
	public List<ITimeWindow> getTimeWindows(final T element) {
		final List<ITimeWindow> windows = timeWindowsByElement
				.maybeGet(element);
		if (windows == null)
			return Collections.emptyList();
		else
			return windows;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		timeWindowsByElement.clear();
	}

	@Override
	public void setTimeWindows(final T element,
			final List<ITimeWindow> timeWindows) {
		timeWindowsByElement.set(element, timeWindows);
	}

}
