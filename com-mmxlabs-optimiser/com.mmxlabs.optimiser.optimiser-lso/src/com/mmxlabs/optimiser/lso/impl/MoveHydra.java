/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.Collection;

import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISegment;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;

/**
 * The {@link MoveHydra} allows multiple {@link ISegment}s from a single {@link ISequence} to be moved to multiple other {@link ISequence}s.
 * 
 * @author Simon Goodall
 * 
 */
public class MoveHydra implements IMove {

	@Override
	public void apply(final IModifiableSequences sequences) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public Collection<IResource> getAffectedResources() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public boolean validate(final ISequences sequences) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

}
