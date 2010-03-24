package com.mmxlabs.optimiser.impl;

import com.mmxlabs.optimiser.IModifiableSequences;
import com.mmxlabs.optimiser.ISequencesManipulator;

/**
 * The Null {@link ISequencesManipulator} implementation. This implementation
 * does nothing.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class NullSequencesManipulator<T> implements
		ISequencesManipulator<T> {

	@Override
	public void manipulate(final IModifiableSequences<T> sequence) {
		// Do nothing
	}
}
