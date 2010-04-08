package com.mmxlabs.optimiser.lso.impl;

import java.util.Collection;

import com.mmxlabs.optimiser.IModifiableSequences;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISegment;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.lso.IMove;

/**
 * The {@link MoveSnake} implements a snake-like move wherein {@link ISegment}s
 * move between multiple {@link ISequence}s. E.g. a segment is moved from
 * sequence A to sequence B, from sequence B to sequence C and from sequence C
 * to sequence A.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type.
 */
public class MoveSnake<T> implements IMove<T> {

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
