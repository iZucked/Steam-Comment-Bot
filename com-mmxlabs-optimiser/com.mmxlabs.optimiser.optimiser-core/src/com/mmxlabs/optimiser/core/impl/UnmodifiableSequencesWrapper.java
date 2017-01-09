/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * Generate a {@link ISequences} implementation which wraps another {@link ISequences} implementation and creates {@link UnmodifiableSequenceWrapper} for each {@link ISequence} in the wrapped
 * sequences.
 * 
 * @author Simon Goodall
 * 
 */
public final class UnmodifiableSequencesWrapper implements ISequences {

	@NonNull
	private final Map<@NonNull IResource, @NonNull ISequence> wrappedSequences = new HashMap<>();

	@NonNull
	private ISequences wrapped;

	public UnmodifiableSequencesWrapper(@NonNull final ISequences wrapped) {
		this.wrapped = wrapped;
		setSequences(wrapped);
	}

	@Override
	@NonNull
	public List<@NonNull IResource> getResources() {
		return wrapped.getResources();
	}

	@Override
	@NonNull
	public ISequence getSequence(@NonNull final IResource resource) {
		final ISequence seq = wrappedSequences.get(resource);
		if (seq == null) {
			throw new IllegalArgumentException("Unknown resource or index");
		}
		return seq;
	}

	@Override
	@NonNull
	public ISequence getSequence(final int index) {
		final ISequence seq = wrappedSequences.get(getResources().get(index));
		if (seq == null) {
			throw new IllegalArgumentException("Unknown resource or index");
		}
		return seq;
	}

	@Override
	@NonNull
	public Map<@NonNull IResource, @NonNull ISequence> getSequences() {
		return wrappedSequences;
	}

	/**
	 * Generate a new map of {@link UnmodifiableSequenceWrapper} objects based on the given {@link ISequences} object.
	 * 
	 * @param wrapped
	 */
	public void setSequences(@NonNull final ISequences wrapped) {
		this.wrapped = wrapped;
		wrappedSequences.clear();
		final Map<IResource, ISequence> sequences = wrapped.getSequences();
		for (final Map.Entry<IResource, ISequence> entry : sequences.entrySet()) {
			final IResource resource = entry.getKey();
			final ISequence sequence = entry.getValue();

			wrappedSequences.put(resource, new UnmodifiableSequenceWrapper(sequence));
		}
	}

	@Override
	public int size() {

		return wrapped.size();
	}

	@Override
	@NonNull
	public List<@NonNull ISequenceElement> getUnusedElements() {
		return wrapped.getUnusedElements();
	}
}
