/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
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
 * @param <T>
 *            Sequence element type
 */
public interface ILocalSearchOptimiser<T> extends ISequencesOptimiser<T> {

	/**
	 * Returns the {@link IMoveGenerator} used by this
	 * {@link ILocalSearchOptimiser} to generate a {@link IMove} each iteration.
	 * 
	 * @return
	 */
	IMoveGenerator<T> getMoveGenerator();
}