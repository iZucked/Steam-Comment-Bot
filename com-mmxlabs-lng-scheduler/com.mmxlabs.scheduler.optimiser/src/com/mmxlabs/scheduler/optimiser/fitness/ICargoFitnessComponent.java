/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;

/**
 * Base interface for all cargo scheduler / allocator related components.
 * 
 * @author hinton
 * 
 */
public interface ICargoFitnessComponent extends IFitnessComponent {
	/**
	 * Initialise the fitness component and obtain any data required from the {@link IOptimisationData} object.
	 * 
	 * @param data
	 */
	void init(@NonNull IPhaseOptimisationData data);

	/**
	 * Reject the last evaluation (relates to {@link IFitnessComponent#getFitness()}
	 */
	void rejectLastEvaluation();

	/**
	 * Accept the last evaluation (for {@link IFitnessComponent#getFitness()}
	 */
	void acceptLastEvaluation();
}
