/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso;

import java.util.Collection;

import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;

/**
 * {@link IFitnessCombiner} implementations are used to combine the raw fitness values from {@link IFitnessComponent}s into a single fitness value
 * 
 * @author Simon Goodall
 * 
 */
public interface IFitnessCombiner {

	/**
	 * Combine the previously evaluated fitnesses in the given {@link IFitnessComponent}s into a single fitness value.
	 * 
	 * @param fitnessComponents
	 * @return
	 */
	long calculateFitness(Collection<IFitnessComponent> fitnessComponents);

}