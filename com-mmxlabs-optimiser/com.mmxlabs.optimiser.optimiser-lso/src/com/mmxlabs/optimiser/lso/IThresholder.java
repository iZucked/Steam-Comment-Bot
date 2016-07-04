/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso;

import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;

/**
 * {@link IThresholder} implementations are used by {@link IFitnessEvaluator}s to determine whether a single delta value is acceptable.
 * 
 * @author Simon Goodall
 * 
 */
public interface IThresholder {

	/**
	 * Prepare {@link IThresholder} for use.
	 */
	void init();

	/**
	 * Return true if the given delta value is acceptable.
	 * 
	 * @param delta
	 * @return
	 */
	boolean accept(long delta);

	/**
	 * Step {@link IThresholder} for the next iteration.
	 */
	void step();
	
	/**
	 * Reset thresholder
	 */
	void reset();
}
