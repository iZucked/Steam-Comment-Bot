package com.mmxlabs.optimiser.lso.modules;

import java.util.List;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.impl.DefaultLocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.optimiser.lso.movegenerators.impl.InstrumentingMoveGenerator;

/**
 * A {@link Guice} module to provide a Local Search optimiser
 * 
 * @author Simon Goodall, Tom Hinton
 * @since 2.0
 */
public class LocalSearchOptimiserModule extends AbstractModule {

	public static final boolean instrumenting = true;

	public static final String LSO_NUMBER_OF_ITERATIONS = "LSO-NumberOfIterations";
	public static final String RANDOM_SEED = "RandomSeed";

	@Override
	protected void configure() {
	}

	@Provides
	@Singleton
	LocalSearchOptimiser buildOptimiser(final Injector injector, final IOptimisationContext context, final ISequencesManipulator manipulator, final IMoveGenerator moveGenerator,
			final InstrumentingMoveGenerator instrumentingMoveGenerator, final IFitnessEvaluator fitnessEvaluator, @Named(RANDOM_SEED) final long seed,
			@Named(LSO_NUMBER_OF_ITERATIONS) final int numberOfIterations, final List<IConstraintChecker> constraintCheckers) {

		final DefaultLocalSearchOptimiser lso = new DefaultLocalSearchOptimiser();

		lso.setNumberOfIterations(numberOfIterations);

		// .. Should not be performed here, but needs to be somewhere.
		// Need to co-ordinate with AnalyticsTransformer over the init method. Analytics Transformer creates the opt data manually.
		// final ChainedSequencesManipulator sequencesManipulator = injector.getInstance(ChainedSequencesManipulator.class);
		manipulator.init(context.getOptimisationData());

		lso.setSequenceManipulator(manipulator);

		lso.setMoveGenerator(instrumenting ? instrumentingMoveGenerator : moveGenerator);
		lso.setFitnessEvaluator(fitnessEvaluator);
		lso.setConstraintCheckers(constraintCheckers);

		lso.setReportInterval(Math.max(10, numberOfIterations / 100));

		return lso;
	}
}