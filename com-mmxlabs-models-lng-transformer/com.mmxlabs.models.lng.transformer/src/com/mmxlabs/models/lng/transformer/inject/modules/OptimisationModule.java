/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
 * @since 3.0
 */
public class OptimisationModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new LocalSearchOptimiserModule());
		install(new MoveGeneratorModule());
		install(new LinearFitnessEvaluatorModule());
	}

	@Provides
	@Singleton
	ISchedulerFactory provideSchedulerFactory(final Injector injector) {
		final ISchedulerFactory factory = new ISchedulerFactory() {

			@Override
			public ISequenceScheduler createScheduler(final IOptimisationData data, final Collection<ICargoSchedulerFitnessComponent> schedulerComponents) {

				final ScheduleFitnessEvaluator scheduleEvaluator = new ScheduleFitnessEvaluator();
				// TODO: If we can change this API, then we can avoid the need for the ISchedulerFactory and this provider
				scheduleEvaluator.setFitnessComponents(schedulerComponents);
				injector.injectMembers(scheduleEvaluator);

				final DirectRandomSequenceScheduler scheduler = new DirectRandomSequenceScheduler();
				scheduler.setScheduleEvaluator(scheduleEvaluator);
				injector.injectMembers(scheduler);
				return scheduler;
			}
		};
		return factory;
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
