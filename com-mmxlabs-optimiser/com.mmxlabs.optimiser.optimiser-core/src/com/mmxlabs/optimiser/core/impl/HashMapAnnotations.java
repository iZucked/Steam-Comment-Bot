/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

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
	public void setAnnotation(@NonNull final ISequenceElement element, @NonNull final String key, @NonNull final IElementAnnotation value) {
		Map<String, IElementAnnotation> inner = contents.get(element);
		if (inner == null) {
			inner = new HashMap<String, IElementAnnotation>();
		}
		inner.put(key, value);
		contents.put(element, inner);
	}

	@Override
	public <U extends IElementAnnotation> U getAnnotation(@NonNull final ISequenceElement element, @NonNull final String key, @NonNull final Class<U> clz) {
		final Map<String, IElementAnnotation> inner = contents.get(element);
		return clz.cast(inner == null ? null : inner.get(key));
	}

	@Override
	public boolean hasAnnotation(@NonNull final ISequenceElement element, @NonNull final String key) {
		return contents.containsKey(element) && contents.get(element).containsKey(key);
	}

	@Override
	@NonNull
	public Iterable<String> getAnnotationNames(@NonNull final ISequenceElement element) {
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
	@SuppressWarnings("null")
	@Override
	@NonNull
	public Map<String, IElementAnnotation> getAnnotations(@NonNull final ISequenceElement element) {
		final Map<String, IElementAnnotation> inner = contents.get(element);
		if (inner != null) {
			return Collections.unmodifiableMap(inner);
		} else {
			return Collections.emptyMap();
		}
	}

}
