/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.concurrent.CleanableExecutorService;

/**
 * Optimiser for lightweight cargo scheduling
 * 
 * @author alex
 *
 */
public interface ILightWeightSequenceOptimiser {
	List<List<Integer>> optimise(ILightWeightOptimisationData lightWeightOptimisationData, List<ILightWeightConstraintChecker> constraintCheckers, List<ILightWeightFitnessFunction> fitnessFunctions,
			@NonNull CleanableExecutorService executorService, IProgressMonitor monitor);
}
