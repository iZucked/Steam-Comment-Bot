/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso;

import com.mmxlabs.optimiser.core.IOptimiser;
import com.mmxlabs.optimiser.core.ISequencesOptimiser;

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