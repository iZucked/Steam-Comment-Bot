package com.mmxlabs.optimiser.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.IAnnotatedSequence;
import com.mmxlabs.optimiser.IElementAnnotations;

/**
 * Implementation of {@link IAnnotatedSequence}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class AnnotatedSequence<T> implements IAnnotatedSequence<T> {

	private final Map<T, IElementAnnotations> annotations;

	public AnnotatedSequence() {
		annotations = new HashMap<T, IElementAnnotations>();
	}

	@Override
	public <U> U getAnnotation(final T element, final String key,
			final Class<U> clz) {
		if (annotations.containsKey(element)) {
			return annotations.get(element).get(key, clz);
		}

		return null;
	}

	@Override
	public void setAnnotation(final T element, final String key,
			final Object value) {

		final IElementAnnotations elementAnnotations;

		if (annotations.containsKey(element)) {
			elementAnnotations = annotations.get(element);
		} else {
			elementAnnotations = new ElementAnnotations();
			annotations.put(element, elementAnnotations);
		}
		elementAnnotations.put(key, value);
	}

	@Override
	public void dispose() {

		annotations.clear();
	}

	@Override
	public boolean hasAnnotation(final T element, final String key) {
		if (annotations.containsKey(element)) {
			return annotations.get(element).containsKey(key);
		}
		return false;

	}
}
