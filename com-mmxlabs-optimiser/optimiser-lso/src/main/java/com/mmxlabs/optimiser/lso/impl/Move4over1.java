package com.mmxlabs.optimiser.lso.impl;

import java.util.Collection;

import com.mmxlabs.optimiser.IModifiableSequences;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.lso.IMove;

/**
 * The {@link Move4over1} move type allows two non-overlapping segments within a
 * single {@link ISequence} to be swapped.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public class Move4over1<T> implements IMove<T> {

	@Override
	public void apply(final IModifiableSequences<T> sequences) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public Collection<IResource> getAffectedResources() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public boolean validate(final ISequences<T> sequences) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
