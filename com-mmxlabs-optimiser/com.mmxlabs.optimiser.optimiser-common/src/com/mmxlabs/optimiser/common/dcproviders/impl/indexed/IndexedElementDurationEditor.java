/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProviderEditor;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

public final class IndexedElementDurationEditor implements IElementDurationProviderEditor {

	private final IIndexMap<IResource, IIndexMap<ISequenceElement, Integer>> durationByElementByResource = new ArrayIndexMap<IResource, IIndexMap<ISequenceElement, Integer>>();

	private final IIndexMap<ISequenceElement, Integer> durationByElement = new ArrayIndexMap<ISequenceElement, Integer>();

	private int defaultDuration;

	@Override
	public int getElementDuration(final ISequenceElement element, final IResource resource) {
		final IIndexMap<ISequenceElement, Integer> byElementForResource = durationByElementByResource.maybeGet(resource);

		if (byElementForResource != null) {
			final Integer i = byElementForResource.maybeGet(element);
			if (i != null) {
				return i.intValue();
			}
		}

		final Integer i = durationByElement.maybeGet(element);
		if (i != null) {
			return i.intValue();
		}

		return defaultDuration;
	}

	@Override
	public int getDefaultValue() {
		return defaultDuration;
	}

	@Override
	public void setElementDuration(final ISequenceElement element, final IResource resource, final int duration) {
		IIndexMap<ISequenceElement, Integer> byElementForResource = durationByElementByResource.maybeGet(resource);
		if (byElementForResource == null) {
			byElementForResource = new ArrayIndexMap<ISequenceElement, Integer>();
			durationByElementByResource.set(resource, byElementForResource);
		}

		byElementForResource.set(element, duration);
	}

	@Override
	public void setDefaultValue(final int defaultValue) {
		defaultDuration = defaultValue;
	}

	@Override
	public void setElementDuration(final ISequenceElement element, final int durationHours) {
		durationByElement.set(element, durationHours);
	}
}
