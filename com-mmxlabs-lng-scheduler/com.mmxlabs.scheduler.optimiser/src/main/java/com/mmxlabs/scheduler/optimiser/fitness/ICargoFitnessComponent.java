/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * Base interface for all cargo scheduler / allocator related components.
 * 
 * @author hinton
 * 
 */
public interface ICargoFitnessComponent<T> extends IFitnessComponent<T> {
	/**
	 * Initialise the fitness component and obtain any data required from the {@link IOptimisationData} object.
	 * 
	 * @param data
	 */
	void init(IOptimisationData<T> data);

	/**
	 * Reject the last evaluation (relates to {@link IFitnessComponent#getFitness()}
	 */
	void rejectLastEvaluation();

	/**
	 * Accept the last evaluation (for {@link IFitnessComponent#getFitness()}
	 */
	void acceptLastEvaluation();

	/**
	 * Clean up references as this component is no longer required.
	 */
	void dispose();
}
