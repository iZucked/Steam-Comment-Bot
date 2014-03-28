/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.mmxlabs.optimiser.core.IElementAnnotation;
import com.mmxlabs.optimiser.core.IElementAnnotationsMap;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * An {@link IElementAnnotationsMap} implementation backed by two levels of {@link HashMap}.
 * 
 * @author hinton
 * 
 */
public class HashMapAnnotations implements IElementAnnotationsMap {
	private final Map<ISequenceElement, Map<String, IElementAnnotation>> contents = new HashMap<ISequenceElement, Map<String, IElementAnnotation>>();

	@Override
	public void setAnnotation(final ISequenceElement element, final String key, final IElementAnnotation value) {
		Map<String, IElementAnnotation> inner = contents.get(element);
		if (inner == null) {
			inner = new HashMap<String, IElementAnnotation>();
		}
		inner.put(key, value);
		contents.put(element, inner);
	}

	@Override
	public <U extends IElementAnnotation> U getAnnotation(final ISequenceElement element, final String key, final Class<U> clz) {
		final Map<String, IElementAnnotation> inner = contents.get(element);
		return clz.cast(inner == null ? null : inner.get(key));
	}

	@Override
	public boolean hasAnnotation(final ISequenceElement element, final String key) {
		return contents.containsKey(element) && contents.get(element).containsKey(key);
	}

	@Override
	public Iterable<String> getAnnotationNames(final ISequenceElement element) {
		final Map<String, IElementAnnotation> inner = contents.get(element);
		final LinkedList<String> results = new LinkedList<String>();

		if (inner != null) {
			results.addAll(inner.keySet());
		}

		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.optimiser.core.IAnnotations#getAnnotations(java.lang.Object)
	 */
	@Override
	public Map<String, IElementAnnotation> getAnnotations(final ISequenceElement element) {
		final Map<String, IElementAnnotation> inner = contents.get(element);
		if (inner != null) {
			return Collections.unmodifiableMap(inner);
		} else {
			return Collections.emptyMap();
		}
	}

}
