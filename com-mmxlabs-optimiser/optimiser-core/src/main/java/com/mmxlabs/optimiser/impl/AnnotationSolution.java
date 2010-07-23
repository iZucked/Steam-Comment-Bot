package com.mmxlabs.optimiser.impl;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.IAnnotatedSequence;
import com.mmxlabs.optimiser.IAnnotatedSolution;
import com.mmxlabs.optimiser.IOptimisationContext;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequences;

/**
 * Implementation of {@link IAnnotatedSolution}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class AnnotationSolution<T> implements IAnnotatedSolution<T> {

	private ISequences<T> sequences;

	private IOptimisationContext<T> context;

	private final Map<IResource, IAnnotatedSequence<T>> annotatedSequences = new HashMap<IResource, IAnnotatedSequence<T>>();;

	@Override
	public IAnnotatedSequence<T> getAnnotatedSequence(final IResource resource) {
		return annotatedSequences.get(resource);
	}

	public void setAnnotatedSequence(final IResource resource,
			final IAnnotatedSequence<T> annotatedSequence) {
		annotatedSequences.put(resource, annotatedSequence);
	}

	@Override
	public ISequences<T> getSequences() {
		return sequences;
	}

	public void setSequences(final ISequences<T> sequences) {
		this.sequences = sequences;
	}

	@Override
	public IOptimisationContext<T> getContext() {
		return context;
	}

	public void setContext(final IOptimisationContext<T> context) {
		this.context = context;
	}

	@Override
	public void dispose() {
		context = null;
		sequences = null;
		annotatedSequences.clear();
	}
}