/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProviderEditor;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Implementation of {@link IElementDurationProviderEditor} using {@link HashMap}s as backing implementation.
 * 
 * @author Simon Goodall
 * 
 */
public final class HashMapElementDurationEditor implements IElementDurationProviderEditor {

	private final Map<IResource, Map<ISequenceElement, Integer>> durations;

	private final Map<ISequenceElement, Integer> nonResourceDurations;

	private int defaultValue;

	public HashMapElementDurationEditor() {
		this.durations = new HashMap<IResource, Map<ISequenceElement, Integer>>();
		this.nonResourceDurations = new HashMap<ISequenceElement, Integer>();
		this.defaultValue = 0;
	}

	@Override
	public void setElementDuration(final ISequenceElement element, final IResource resource, final int duration) {
		final Map<ISequenceElement, Integer> map;
		if (durations.containsKey(resource)) {
			map = durations.get(resource);
		} else {
			map = new HashMap<ISequenceElement, Integer>();
			durations.put(resource, map);
		}
		map.put(element, duration);
	}

	@Override
	public int getElementDuration(final ISequenceElement element, final IResource resource) {
		if (durations.containsKey(resource)) {
			final Map<ISequenceElement, Integer> map = durations.get(resource);
			if (map.containsKey(element)) {
				return map.get(element);
			}
		}
		final Integer x = nonResourceDurations.get(element);
		return x == null ? defaultValue : x;
	}

	@Override
	public int getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void setDefaultValue(final int defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public void setElementDuration(final ISequenceElement element, final int durationHours) {
		nonResourceDurations.put(element, durationHours);
	}
}
