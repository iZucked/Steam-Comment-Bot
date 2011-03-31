/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
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
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.ILocalSearchOptimiser;

/**
 * {@link IOptimisationConstructor} build an {@link IOptimisationConstructor}
 * and {@link ILocalSearchOptimiser} to run an optimisation from the given
 * {@link IOptimisationData}. A list of enabled {@link IConstraintChecker}s and
 * a {@link Map} of {@link IFitnessComponent} to weight. Additional properties
 * are provided through a {@link Properties} object.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IOptimisationConstructor<T> {

	void init(IOptimisationData<T> optimisationData, Properties properties,
			Map<String, Double> fitnessWeights,
			List<String> constraintCheckers,
			IOptimiserProgressMonitor<T> monitor);

	void dispose();

	ILocalSearchOptimiser<T> getOptimiser();

	IOptimisationContext<T> getOptimisationContext();
}