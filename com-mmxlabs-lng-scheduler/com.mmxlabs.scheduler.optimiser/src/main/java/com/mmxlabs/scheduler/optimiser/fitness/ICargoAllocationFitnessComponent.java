/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.Map;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Interface for cargo scheduler fitness components which need to see the full solution, including cargo allocations.
 * 
 * @author hinton
 * 
 */
public interface ICargoAllocationFitnessComponent extends ICargoFitnessComponent {
	/**
	 * Called to evaluate the given solution.
	 * 
	 * @param solution
	 * @param allocations
	 * @return fitness value, or Long.MAX_VALUE if there is a problem
	 */
	public long evaluate(ScheduledSequences solution, Map<VoyagePlan, IAllocationAnnotation> allocations);

	/**
	 * Called to annotate the given solution.
	 * 
	 * @param solution
	 * @param annotatedSolution
	 */
	public void annotate(ScheduledSequences solution, IAnnotatedSolution annotatedSolution);
}
