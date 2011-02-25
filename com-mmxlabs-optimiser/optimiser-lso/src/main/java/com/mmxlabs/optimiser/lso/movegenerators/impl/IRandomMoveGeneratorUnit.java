/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.movegenerators.impl;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;

/**
 * Interface used by the {@link RandomMoveGenerator} to generate a {@link IMove}
 * of a particular type. Implementations need to be registered with the
 * {@link RandomMoveGenerator} before they will be used.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IRandomMoveGeneratorUnit<T> {

	IMove<T> generateRandomMove(RandomMoveGenerator<T> moveGenerator,
			ISequences<T> sequences);

}
