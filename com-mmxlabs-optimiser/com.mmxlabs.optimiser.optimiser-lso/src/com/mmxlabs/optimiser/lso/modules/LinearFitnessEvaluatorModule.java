/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.modules;

import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.core.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessHelper;
import com.mmxlabs.optimiser.lso.IFitnessCombiner;
import com.mmxlabs.optimiser.lso.IThresholder;
import com.mmxlabs.optimiser.lso.impl.LinearFitnessCombiner;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.optimiser.lso.movegenerators.impl.InstrumentingMoveGenerator;

/**
 * A {@link Guice} module to provide a Local Search optimiser
 * 
 * @author Simon Goodall, Tom Hinton
 * @since 2.0
 */
public class LinearFitnessEvaluatorModule extends AbstractModule {

	public static final String LINEAR_FITNESS_WEIGHTS_MAP = "LinearFitnessWeights";

	@Override
	protected void configure() {
		bind(IFitnessHelper.class).to(FitnessHelper.class);
	}

	@Provides
	@Singleton
	private IFitnessCombiner createFitnessCombiner(@Named(LINEAR_FITNESS_WEIGHTS_MAP) final Map<String, Double> weightsMap) {

		final LinearFitnessCombiner combiner = new LinearFitnessCombiner();
		combiner.setFitnessComponentWeights(weightsMap);
		return combiner;
	}

	@Provides
	@Singleton
	private IFitnessEvaluator createFitnessEvaluator(@NonNull Injector injector, @NonNull final IThresholder thresholder, @NonNull final InstrumentingMoveGenerator imAg,
			@NonNull final List<IFitnessComponent> fitnessComponents) {
		// create a linear Fitness evaluator.

		final LinearSimulatedAnnealingFitnessEvaluator fitnessEvaluator = new LinearSimulatedAnnealingFitnessEvaluator();
		injector.injectMembers(fitnessEvaluator);

		fitnessEvaluator.setFitnessComponents(fitnessComponents);

		fitnessEvaluator.init();

		return fitnessEvaluator;

	}

}
