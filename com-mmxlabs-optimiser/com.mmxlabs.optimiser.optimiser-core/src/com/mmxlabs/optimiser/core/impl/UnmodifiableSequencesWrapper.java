/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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
	private ISequences wrapped;

	public UnmodifiableSequencesWrapper(@NonNull final ISequences wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	@NonNull
	public ImmutableList<@NonNull IResource> getResources() {
		return wrapped.getResources();
	}

	@Override
	@NonNull
	public ISequence getSequence(@NonNull final IResource resource) {
		final ISequence seq = wrapped.getSequence(resource);
		if (seq == null) {
			throw new IllegalArgumentException("Unknown resource or index");
		}
		return seq;
	}

	@Override
	@NonNull
	public ISequence getSequence(final int index) {
		final ISequence seq = wrapped.getSequence(index);
		if (seq == null) {
			throw new IllegalArgumentException("Unknown resource or index");
		}
		return seq;
	}

	@Override
	@NonNull
	public ImmutableMap<@NonNull IResource, @NonNull ISequence> getSequences() {
		return wrapped.getSequences();
	}

	@Override
	public int size() {
		return wrapped.size();
	}

	@Override
	@NonNull
	public ImmutableList<@NonNull ISequenceElement> getUnusedElements() {
		return wrapped.getUnusedElements();
	}
}
