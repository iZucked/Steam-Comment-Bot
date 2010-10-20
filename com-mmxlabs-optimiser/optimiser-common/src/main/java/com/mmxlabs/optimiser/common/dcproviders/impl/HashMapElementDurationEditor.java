/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.common.dcproviders.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProviderEditor;
import com.mmxlabs.optimiser.core.IResource;

/**
 * Implementation of {@link IElementDurationProviderEditor} using
 * {@link HashMap}s as backing implementation.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class HashMapElementDurationEditor<T> implements
		IElementDurationProviderEditor<T> {

	private final String name;

	private final Map<IResource, Map<T, Integer>> durations;

	private final Map<T, Integer> nonResourceDurations;
	
	private int defaultValue;

	public HashMapElementDurationEditor(final String name) {
		this.name = name;
		this.durations = new HashMap<IResource, Map<T, Integer>>();
		this.nonResourceDurations = new HashMap<T, Integer>();
		this.defaultValue = 0;
	}

	@Override
	public void setElementDuration(final T element, final IResource resource,
			final int duration) {
		final Map<T, Integer> map;
		if (durations.containsKey(resource)) {
			map = durations.get(resource);
		} else {
			map = new HashMap<T, Integer>();
			durations.put(resource, map);
		}
		map.put(element, duration);
	}

	@Override
	public int getElementDuration(final T element, final IResource resource) {
		if (durations.containsKey(resource)) {
			final Map<T, Integer> map = durations.get(resource);
			if (map.containsKey(element)) {
				return map.get(element);
			}
		}
		final Integer x = nonResourceDurations.get(element);
		return x == null ? defaultValue : x;
	}

	@Override
	public void dispose() {
		durations.clear();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void setDefaultValue(int defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public void setElementDuration(T element, int durationHours) {
		nonResourceDurations.put(element, durationHours);
	}
}
