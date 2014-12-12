/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import java.util.Collection;
import java.util.Random;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.modules.LinearFitnessEvaluatorModule;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;
import com.mmxlabs.optimiser.lso.modules.MoveGeneratorModule;
import com.mmxlabs.optimiser.lso.movegenerators.impl.CompoundMoveGenerator;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ISchedulerFactory;
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.DirectRandomSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.ScheduleFitnessEvaluator;
import com.mmxlabs.scheduler.optimiser.lso.ConstrainedMoveGenerator;

/**
 * This {@link Module} configures the default schedule optimisation classes.
 * 
 */
public class OptimisationModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new LocalSearchOptimiserModule());
		install(new MoveGeneratorModule());
		install(new LinearFitnessEvaluatorModule());
		bind (ScheduleFitnessEvaluator.class).in(Singleton.class);
		bind (DirectRandomSequenceScheduler.class).in(Singleton.class);
		bind (ISequenceScheduler.class).to(DirectRandomSequenceScheduler.class);
	}

	@Provides
	@Singleton
	private IMoveGenerator provideMoveGenerator(final ConstrainedMoveGenerator normalMoveGenerator, @Named(LocalSearchOptimiserModule.RANDOM_SEED) long seed) {

		final CompoundMoveGenerator moveGenerator = new CompoundMoveGenerator();

		moveGenerator.addGenerator(normalMoveGenerator, 1);
		moveGenerator.setRandom(new Random(seed));

		return moveGenerator;
	}

}
