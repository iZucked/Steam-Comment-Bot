/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import static org.ops4j.peaberry.Peaberry.service;

import java.util.Collection;
import java.util.Random;

import javax.inject.Singleton;

import org.eclipse.core.runtime.Platform;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.optimiser.OptimiserModel;
import com.mmxlabs.models.lng.optimiser.OptimiserSettings;
import com.mmxlabs.models.lng.transformer.IOptimisationTransformer;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.OptimisationTransformer;
import com.mmxlabs.models.lng.transformer.ResourcelessModelEntityMap;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.transformer.util.ScenarioUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.modules.OptimiserCoreModule;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.modules.LinearFitnessEvaluatorModule;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;
import com.mmxlabs.optimiser.lso.modules.MoveGeneratorModule;
import com.mmxlabs.optimiser.lso.movegenerators.impl.CompoundMoveGenerator;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ISchedulerFactory;
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.UnconstrainedCargoAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.CachingVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.DirectRandomSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.ScheduleEvaluator;
import com.mmxlabs.scheduler.optimiser.lso.ConstrainedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.manipulators.SequencesManipulatorModule;
import com.mmxlabs.scheduler.optimiser.peaberry.SchedulerModule;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;

/**
 * Main entry point to create {@link LNGScenarioTransformer}. This uses injection to populate the data structures.
 * 
 * @since 2.0
 */
public class LNGTransformerModule extends AbstractModule {

	private final static int DEFAULT_VPO_CACHE_SIZE = 20000;

	private final MMXRootObject scenario;

	public LNGTransformerModule(final MMXRootObject scenario) {
		this.scenario = scenario;
	}

	@Override
	protected void configure() {
		install(new ScheduleBuilderModule());
		install(new LocalSearchOptimiserModule());
		install(new MoveGeneratorModule());
		install(new SequencesManipulatorModule());
		install(new SchedulerModule());
		install(new OptimiserCoreModule());
		install(new LinearFitnessEvaluatorModule());

		bind(MMXRootObject.class).toInstance(scenario);

		bind(LNGScenarioTransformer.class).in(Singleton.class);

		bind(SeriesParser.class).in(Singleton.class);

		bind(DateAndCurveHelper.class).in(Singleton.class);

		bind(ResourcelessModelEntityMap.class).in(Singleton.class);
		bind(ModelEntityMap.class).to(ResourcelessModelEntityMap.class).in(Singleton.class);

		bind(ILNGVoyageCalculator.class).to(LNGVoyageCalculator.class);
		bind(LNGVoyageCalculator.class).in(Singleton.class);

		bind(ICargoAllocator.class).to(UnconstrainedCargoAllocator.class).in(Singleton.class);

		bind(VoyagePlanOptimiser.class);

		if (Platform.isRunning()) {
			bind(IFitnessFunctionRegistry.class).toProvider(service(IFitnessFunctionRegistry.class).single());
			bind(IConstraintCheckerRegistry.class).toProvider(service(IConstraintCheckerRegistry.class).single());
			bind(IEvaluationProcessRegistry.class).toProvider(service(IEvaluationProcessRegistry.class).single());
		}

		bind(IOptimisationTransformer.class).to(OptimisationTransformer.class).in(Singleton.class);
	}

	@Provides
	IVoyagePlanOptimiser provideVoyagePlanOptimiser(final VoyagePlanOptimiser delegate) {
		final CachingVoyagePlanOptimiser cachingVoyagePlanOptimiser = new CachingVoyagePlanOptimiser(delegate, DEFAULT_VPO_CACHE_SIZE);
		return cachingVoyagePlanOptimiser;
	}

	@Provides
	@Singleton
	ISchedulerFactory provideSchedulerFactory(final Injector injector) {
		final ISchedulerFactory factory = new ISchedulerFactory() {

			@Override
			public ISequenceScheduler createScheduler(final IOptimisationData data, final Collection<ICargoSchedulerFitnessComponent> schedulerComponents) {

				final ScheduleEvaluator scheduleEvaluator = new ScheduleEvaluator();
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
	IOptimisationData provideOptimisationData(final LNGScenarioTransformer lngScenarioTransformer, final ResourcelessModelEntityMap entities) throws IncompleteScenarioException {
		final IOptimisationData optimisationData = lngScenarioTransformer.createOptimisationData(entities);

		return optimisationData;
	}

	@Provides
	@Singleton
	/**
	 * Utility method for getting the current optimisation settings from this scenario. TODO maybe put this in another file/model somewhere else.
	 * 
	 * @return
	 */
	OptimiserSettings getOptimisationSettings(MMXRootObject rootObject) {
		final OptimiserModel om = rootObject.getSubModel(OptimiserModel.class);
		if (om != null) {
			// select settings
			final OptimiserSettings x = om.getActiveSetting();
			if (x != null)
				return x;
		}
		// if (defaultSettings == null) {
		OptimiserSettings defaultSettings = ScenarioUtils.createDefaultSettings();
		if (om != null) {
			om.getSettings().add(defaultSettings);
			om.setActiveSetting(defaultSettings);
		}
		// }
		return defaultSettings;
	}

	@Provides
	@Singleton
	@Named("Initial")
	private ISequences provideInitialSequences(final IOptimisationTransformer optimisationTransformer, final IOptimisationData data, final ResourcelessModelEntityMap mem) {

		final ISequences sequences = optimisationTransformer.createInitialSequences(data, mem);

		return sequences;
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
