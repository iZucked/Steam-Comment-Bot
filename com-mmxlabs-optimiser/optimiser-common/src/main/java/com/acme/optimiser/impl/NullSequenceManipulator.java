package com.acme.optimiser.impl;

import com.acme.optimiser.IModifiableSequence;
import com.acme.optimiser.ISequenceManipulator;

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
