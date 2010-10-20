/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.core.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * Generate a {@link ISequences} implementation which wraps another
 * {@link ISequences} implementation and creates
 * {@link UnmodifiableSequenceWrapper} for each {@link ISequence} in the wrapped
 * sequences.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class UnmodifiableSequencesWrapper<T> implements ISequences<T> {

	private final Map<IResource, ISequence<T>> wrappedSequences = new HashMap<IResource, ISequence<T>>();

	private ISequences<T> wrapped;

	public UnmodifiableSequencesWrapper(final ISequences<T> wrapped) {
		setSequences(wrapped);
	}

	@Override
	public List<IResource> getResources() {
		return wrapped.getResources();
	}

	@Override
	public ISequence<T> getSequence(final IResource resource) {
		return wrappedSequences.get(resource);
	}

	@Override
	public ISequence<T> getSequence(final int index) {
		return wrappedSequences.get(getResources().get(index));
	}

	@Override
	public Map<IResource, ISequence<T>> getSequences() {
		return wrappedSequences;
	}

	/**
	 * Generate a new map of {@link UnmodifiableSequenceWrapper} objects based
	 * on the given {@link ISequences} object.
	 * 
	 * @param wrapped
	 */
	public void setSequences(final ISequences<T> wrapped) {
		this.wrapped = wrapped;
		wrappedSequences.clear();
		final Map<IResource, ISequence<T>> sequences = wrapped.getSequences();
		for (final Map.Entry<IResource, ISequence<T>> entry : sequences
				.entrySet()) {
			final IResource resource = entry.getKey();
			final ISequence<T> sequence = entry.getValue();

			wrappedSequences.put(resource, new UnmodifiableSequenceWrapper<T>(
					sequence));
		}
	}

	@Override
	public int size() {

		return wrapped.size();
	}
}
