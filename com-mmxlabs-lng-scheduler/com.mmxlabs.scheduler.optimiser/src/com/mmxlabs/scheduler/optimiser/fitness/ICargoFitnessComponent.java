/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;

/**
 * Base interface for all cargo scheduler / allocator related components.
 * 
 * @author hinton
 * 
 */
public interface ICargoFitnessComponent extends IFitnessComponent {

	/**
	 * Reject the last evaluation (relates to {@link IFitnessComponent#getFitness()}
	 */
	void rejectLastEvaluation();

	/**
	 * Accept the last evaluation (for {@link IFitnessComponent#getFitness()}
	 */
	void acceptLastEvaluation();
}
