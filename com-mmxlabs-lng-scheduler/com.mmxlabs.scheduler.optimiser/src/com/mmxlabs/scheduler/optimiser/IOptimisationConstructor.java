/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.optimiser.lso.ILocalSearchOptimiser;

/**
 * {@link IOptimisationConstructor} build an {@link IOptimisationConstructor} and {@link ILocalSearchOptimiser} to run an optimisation from the given {@link IPhaseOptimisationData}. A list of enabled
 * {@link IConstraintChecker}s and a {@link Map} of {@link IFitnessComponent} to weight. Additional properties are provided through a {@link Properties} object.
 * 
 * @author Simon Goodall
 * 
 */
public interface IOptimisationConstructor {

	void init(IPhaseOptimisationData optimisationData, Properties properties, Map<String, Double> fitnessWeights, List<String> constraintCheckers, IOptimiserProgressMonitor monitor);

	void dispose();

	ILocalSearchOptimiser getOptimiser();

	IOptimisationContext getOptimisationContext();
}