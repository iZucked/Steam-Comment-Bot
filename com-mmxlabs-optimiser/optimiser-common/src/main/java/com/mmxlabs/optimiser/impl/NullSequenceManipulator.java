package com.mmxlabs.optimiser.impl;

import com.mmxlabs.optimiser.IModifiableSequence;
import com.mmxlabs.optimiser.ISequenceManipulator;

/**
 * The Null {@link ISequenceManipulator} implementation. This implementation
 * does nothing.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class NullSequenceManipulator<T> implements
		ISequenceManipulator<T> {

	@Override
	public void manipulate(IModifiableSequence<T> sequence) {

	}

}
