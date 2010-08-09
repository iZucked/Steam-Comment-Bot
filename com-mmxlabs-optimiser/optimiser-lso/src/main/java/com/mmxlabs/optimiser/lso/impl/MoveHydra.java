package com.mmxlabs.optimiser.lso.impl;

import java.util.Collection;

import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISegment;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;

/**
 * The {@link MoveHydra} allows multiple {@link ISegment}s from a single
 * {@link ISequence} to be moved to multiple other {@link ISequence}s.
 * 
 * @author Simon Goodall
 * 
 * @param <T> Sequence element type.
 */
public class MoveHydra<T> implements IMove<T> {

	@Override
	public void apply(IModifiableSequences<T> sequences) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public Collection<IResource> getAffectedResources() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public boolean validate(ISequences<T> sequences) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

}
