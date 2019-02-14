/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso;

import com.mmxlabs.optimiser.core.IOptimiser;
import com.mmxlabs.optimiser.core.ISequencesOptimiser;
import com.mmxlabs.optimiser.core.moves.IMove;

/**
 * Extended {@link IOptimiser} interface for a Local Search Optimiser
 * 
 * @author Simon Goodall
 * 
 */
public interface ILocalSearchOptimiser extends ISequencesOptimiser {

	/**
	 * Returns the {@link IMoveGenerator} used by this {@link ILocalSearchOptimiser} to generate a {@link IMove} each iteration.
	 * 
	 * @return
	 */
	IMoveGenerator getMoveGenerator();
}