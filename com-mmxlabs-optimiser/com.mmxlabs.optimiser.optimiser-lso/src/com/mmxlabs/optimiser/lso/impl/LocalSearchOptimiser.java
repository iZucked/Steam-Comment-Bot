/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import static com.google.common.base.Preconditions.checkState;

import java.util.Collection;

import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.impl.AbstractSequencesOptimiser;
import com.mmxlabs.optimiser.lso.ILocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.IMoveGenerator;

/**
 * Main class implementing a Local Search Optimiser. While the actual optimisation loop is left to sub-classes, this class provides the bulk of the implementation to simplify such loops.
 * 
 * @author Simon Goodall
 * 
 */
public abstract class LocalSearchOptimiser extends AbstractSequencesOptimiser implements ILocalSearchOptimiser {

	private IMoveGenerator moveGenerator;

	/**
	 * Initialise method checking the object has all the correct pieces of data to be able to perform the {@link #optimise(IOptimisationContext, Collection, Object)} method. Throws an
	 * {@link IllegalStateException} on error.
	 */
	@Override
	public void init() {

		super.init();
		checkState(moveGenerator != null, "Move Generator is not set");
	}

	public final void setMoveGenerator(final IMoveGenerator moveGenerator) {
		this.moveGenerator = moveGenerator;
	}

	@Override
	public final IMoveGenerator getMoveGenerator() {
		return moveGenerator;
	}
}
