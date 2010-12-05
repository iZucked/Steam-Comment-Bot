/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.mmxlabs.optimiser.core.IAnnotations;

/**
 * An {@link IAnnotations} implementation backed by two levels of hashmap.
 * 
 * @author hinton
 * 
 */
public class HashMapAnnotations<T> implements IAnnotations<T> {
	private final HashMap<T, HashMap<String, Object>> contents = new HashMap<T, HashMap<String, Object>>();

	@Override
	public void setAnnotation(final T element, final String key,
			final Object value) {
		HashMap<String, Object> inner = contents.get(element);
		if (inner == null) {
			inner = new HashMap<String, Object>();
		}
		inner.put(key, value);
		contents.put(element, inner);
	}

	@Override
	public <U> U getAnnotation(final T element, final String key,
			final Class<U> clz) {
		final HashMap<String, Object> inner = contents.get(element);
		return clz.cast(inner == null ? null : inner.get(key));
	}

	@Override
	public boolean hasAnnotation(final T element, final String key) {
		return contents.containsKey(element)
				&& contents.get(element).containsKey(key);
	}

	@Override
	public Iterable<String> getAnnotationNames(final T element) {
		final HashMap<String, Object> inner = contents.get(element);
		final LinkedList<String> results = new LinkedList<String>();
		
		if (inner != null) {
			results.addAll(inner.keySet());
		}
		
		return results;
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.optimiser.core.IAnnotations#getAnnotations(java.lang.Object)
	 */
	@Override
	public Map<String, Object> getAnnotations(T element) {
		final Map<String, Object> inner = contents.get(element);
		if (inner != null)
			return Collections.unmodifiableMap(inner);
		else
			return Collections.emptyMap();
	}

}
