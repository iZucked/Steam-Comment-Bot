/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * The Null {@link ISequencesManipulator} implementation. This implementation does nothing.
 * 
 * @author Simon Goodall
 * 
 */
public final class NullSequencesManipulator implements ISequencesManipulator {

	@Override
	public void manipulate(final IModifiableSequences sequence) {
		// Do nothing
	}

	@Override
	public void dispose() {
		// Do nothing
	}

	@Override
	public void init(IOptimisationData data) {
		// Do nothing
	}
}
