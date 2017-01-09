/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.indexedobjects.IIndexMap;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexMap;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProviderEditor;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

public final class IndexedElementDurationEditor implements IElementDurationProviderEditor {

	private final IIndexMap<@NonNull IResource, @Nullable IIndexMap<@NonNull ISequenceElement, @Nullable Integer>> durationByElementByResource = new ArrayIndexMap<>();

	private final IIndexMap<@NonNull ISequenceElement, @Nullable Integer> durationByElement = new ArrayIndexMap<>();

	private int defaultDuration;

	@Override
	public int getElementDuration(@NonNull final ISequenceElement element, @NonNull final IResource resource) {
		final IIndexMap<@NonNull ISequenceElement, @Nullable Integer> byElementForResource = durationByElementByResource.maybeGet(resource);

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
	public void setElementDuration(final @NonNull ISequenceElement element, @NonNull final IResource resource, final int duration) {
		IIndexMap<@NonNull ISequenceElement, @Nullable Integer> byElementForResource = durationByElementByResource.maybeGet(resource);
		if (byElementForResource == null) {
			byElementForResource = new ArrayIndexMap<>();
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
