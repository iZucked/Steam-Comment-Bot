package com.mmxlabs.optimiser.lso.modules;

import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessHelper;
import com.mmxlabs.optimiser.lso.IThresholder;
import com.mmxlabs.optimiser.lso.impl.LinearFitnessCombiner;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.optimiser.lso.impl.thresholders.InstrumentingThresholder;
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
	}

	@Provides
	@Singleton
	private IFitnessEvaluator createFitnessEvaluator(final IThresholder thresholder, final InstrumentingMoveGenerator img, @Named(LINEAR_FITNESS_WEIGHTS_MAP) final Map<String, Double> weightsMap,
			final List<IFitnessComponent> fitnessComponents) {
		// create a linear Fitness evaluator.

		final FitnessHelper fitnessHelper = new FitnessHelper();

		final LinearSimulatedAnnealingFitnessEvaluator fitnessEvaluator = new LinearSimulatedAnnealingFitnessEvaluator();

		fitnessEvaluator.setFitnessComponents(fitnessComponents);
		fitnessEvaluator.setFitnessHelper(fitnessHelper);

		final LinearFitnessCombiner combiner = new LinearFitnessCombiner();
		combiner.setFitnessComponentWeights(weightsMap);

		fitnessEvaluator.setFitnessCombiner(combiner);

		final IThresholder wrappedThresholder = LocalSearchOptimiserModule.instrumenting ? new InstrumentingThresholder(thresholder, img) : thresholder;

		fitnessEvaluator.setThresholder(wrappedThresholder);

		fitnessEvaluator.init();

		return fitnessEvaluator;

	}
}
