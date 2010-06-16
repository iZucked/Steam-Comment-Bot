package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.fitness.IAnnotatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ISchedulerElementAnnotations;

/**
 * Implementation of {@link IAnnotatedSequence}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class AnnotatedSequence<T> implements IAnnotatedSequence<T> {

	private final Map<T, ISchedulerElementAnnotations> annotations;

	public AnnotatedSequence() {
		annotations = new HashMap<T, ISchedulerElementAnnotations>();
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

		final ISchedulerElementAnnotations elementAnnotations;

		if (annotations.containsKey(element)) {
			elementAnnotations = annotations.get(element);
		} else {
			elementAnnotations = new SchedulerElementAnnotations();
			annotations.put(element, elementAnnotations);
		}
		elementAnnotations.put(key, value);
	}

	@Override
	public void dispose() {

		// TODO: Do we need to "dispose" of each element?
		annotations.clear();
	}
}
