/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.fitness.impl.ga;

public interface IIndividualEvaluator<T> {

	long evaluate(Individual individual);

	void dispose();

}
