/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private final Map<IResource, ISequence> wrappedSequences = new HashMap<IResource, ISequence>();

	private ISequences wrapped;

	public UnmodifiableSequencesWrapper(final ISequences wrapped) {
		setSequences(wrapped);
	}

	@Override
	public List<IResource> getResources() {
		return wrapped.getResources();
	}

	@Override
	public ISequence getSequence(final IResource resource) {
		return wrappedSequences.get(resource);
	}

	@Override
	public ISequence getSequence(final int index) {
		return wrappedSequences.get(getResources().get(index));
	}

	@Override
	public Map<IResource, ISequence> getSequences() {
		return wrappedSequences;
	}

	/**
	 * Generate a new map of {@link UnmodifiableSequenceWrapper} objects based on the given {@link ISequences} object.
	 * 
	 * @param wrapped
	 */
	public void setSequences(final ISequences wrapped) {
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
	public List<ISequenceElement> getUnusedElements() {
		return wrapped.getUnusedElements();
	}
}
