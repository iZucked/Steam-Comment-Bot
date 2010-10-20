/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.core.impl;

import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;

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

	@Override
	public void dispose() {
		// Do nothing
	}
}
