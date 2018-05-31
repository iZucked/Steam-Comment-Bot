/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler;

import java.util.List;

/**
 * Optimiser for lightweight cargo scheduling
 * @author alex
 *
 */
public interface ILightWeightSequenceOptimiser {
	List<List<Integer>> optimise(ILightWeightOptimisationData lightWeightOptimisationData,
			List<ILightWeightConstraintChecker> constraintCheckers,
			List<ILightWeightFitnessFunction> fitnessFunctions);
}
