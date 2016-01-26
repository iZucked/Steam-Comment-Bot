/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.lso.IMove;

/**
 * Interface for units that plug into the constrained move generator.
 * 
 * @author hinton
 * 
 */
public interface IConstrainedMoveGeneratorUnit {
	/**
	 * Called when the parent MG has the same named method called, after any lookup tables are built.
	 * 
	 * @param sequences
	 */
	void setSequences(ISequences sequences);

	/**
	 * Try to generate a move
	 * 
	 * @return
	 */
	IMove generateMove();
}
