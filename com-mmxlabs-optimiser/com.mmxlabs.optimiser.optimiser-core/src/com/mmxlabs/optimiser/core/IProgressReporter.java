/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

/**
 * 
 * @author Simon Goodall
 * 
 */
public interface IProgressReporter {

	void begin(int totalWork);

	void report(int workDone);

	/**
	 * Notify the optimisation has finished with the given solution as the best found.
	 * 
	 * @param optimiser
	 * @param bestFitness
	 * @param bestState
	 */
	void done();
}
