/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.common.constraints.OrderedSequenceElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.impl.ConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.modules.OptimiserCoreModule;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortTypeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoAllocationFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ISchedulerFactory;
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.UnconstrainedCargoAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.DirectRandomSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.ScheduleEvaluator;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.ConstrainedInitialSequenceBuilder;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.IInitialSequenceBuilder;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;

public class ScheduleTestModule extends AbstractModule {

	private final static int DEFAULT_VPO_CACHE_SIZE = 20000;

	private static final String ENABLED_FITNESS_NAMES = "EnabledFitnessNames";
	private static final String ENABLED_CONSTRAINT_NAMES = "EnabledConstraintNames";

	private final IOptimisationData data;

	public ScheduleTestModule(final IOptimisationData data) {
		this.data = data;
	}

	@Override
	protected void configure() {
		install(new OptimiserCoreModule());

		bind(IOptimisationData.class).toInstance(data);

		// bind(LNGScenarioTransformer.class).in(Singleton.class);

		// bind(SeriesParser.class).in(Singleton.class);
		//
		// bind(DateAndCurveHelper.class).in(Singleton.class);
		//
		// bind(ResourcelessModelEntityMap.class).in(Singleton.class);
		// bind(ModelEntityMap.class).to(ResourcelessModelEntityMap.class).in(Singleton.class);

		bind(ILNGVoyageCalculator.class).to(LNGVoyageCalculator.class);
		bind(LNGVoyageCalculator.class).in(Singleton.class);

		bind(ICargoAllocator.class).to(UnconstrainedCargoAllocator.class).in(Singleton.class);

		bind(VoyagePlanOptimiser.class);

		// if (Platform.isRunning()) {
		// bind(IFitnessFunctionRegistry.class).toProvider(service(IFitnessFunctionRegistry.class).single());
		// bind(IConstraintCheckerRegistry.class).toProvider(service(IConstraintCheckerRegistry.class).single());
		// bind(IEvaluationProcessRegistry.class).toProvider(service(IEvaluationProcessRegistry.class).single());
		// }

		// bind(IOptimisationTransformer.class).to(OptimisationTransformer.class).in(Singleton.class);
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
			public ISequenceScheduler createScheduler(final IOptimisationData data, final Collection<ICargoSchedulerFitnessComponent> schedulerComponents,
					final Collection<ICargoAllocationFitnessComponent> allocationComponents) {

				final ScheduleEvaluator scheduleEvaluator = new ScheduleEvaluator();
				// TODO: If we can change this API, then we can avoid the need for the ISchedulerFactory and this provider
				scheduleEvaluator.setFitnessComponents(schedulerComponents, allocationComponents);
				injector.injectMembers(scheduleEvaluator);
				scheduleEvaluator.init();

				final DirectRandomSequenceScheduler scheduler = new DirectRandomSequenceScheduler();
				scheduler.setScheduleEvaluator(scheduleEvaluator);
				injector.injectMembers(scheduler);
				return scheduler;
			}
		};
		return factory;
	}

	//
	// @Provides
	// @Singleton
	// IOptimisationData provideOptimisationData(final LNGScenarioTransformer lngScenarioTransformer, final ResourcelessModelEntityMap entities) throws IncompleteScenarioException {
	// final IOptimisationData optimisationData = lngScenarioTransformer.createOptimisationData(entities);
	//
	// return optimisationData;
	// }

	// final IFitnessFunctionRegistry fitnessRegistry = createFitnessRegistry();
	// final IConstraintCheckerRegistry constraintRegistry = createConstraintRegistry();
	// final IEvaluationProcessRegistry evaluationProcessRegistry = createEvaluationProcessRegistry();

	@Provides
	@Singleton
	private IConstraintCheckerRegistry createConstraintRegistry(Injector injector) {
		final IConstraintCheckerRegistry constraintRegistry = new ConstraintCheckerRegistry();

		final OrderedSequenceElementsConstraintCheckerFactory constraintFactory = new OrderedSequenceElementsConstraintCheckerFactory(SchedulerConstants.DCP_orderedElementsProvider);
		constraintRegistry.registerConstraintCheckerFactory(constraintFactory);
		injector.injectMembers(constraintFactory);

		final ResourceAllocationConstraintCheckerFactory constraintFactory2 = new ResourceAllocationConstraintCheckerFactory(SchedulerConstants.DCP_resourceAllocationProvider);
		constraintRegistry.registerConstraintCheckerFactory(constraintFactory2);
		injector.injectMembers(constraintFactory2);

		final PortTypeConstraintCheckerFactory constraintFactory3 = new PortTypeConstraintCheckerFactory();
		constraintRegistry.registerConstraintCheckerFactory(constraintFactory3);
		injector.injectMembers(constraintFactory3);

		return constraintRegistry;
	}

	@Provides
	@Singleton
	private IFitnessFunctionRegistry createFitnessRegistry(Injector injector) {
		final FitnessFunctionRegistry fitnessRegistry = new FitnessFunctionRegistry();

		final CargoSchedulerFitnessCoreFactory factory = new CargoSchedulerFitnessCoreFactory();
		injector.injectMembers(factory);

		fitnessRegistry.registerFitnessCoreFactory(factory);
		return fitnessRegistry;
	}

	@Provides
	@Singleton
	private IEvaluationProcessRegistry createEvaluationProcessRegistry() {
		final IEvaluationProcessRegistry evaluationProcessRegistry = new EvaluationProcessRegistry();

		// TODO: Fill in

		return evaluationProcessRegistry;
	}

	// @Provides
	// @Singleton
	// private IOptimisationContext createOptimisationContext(final IOptimisationData data, @Named("Initial") final ISequences sequences, final IFitnessFunctionRegistry fitnessFunctionRegistry,
	// final IConstraintCheckerRegistry constraintCheckerRegistry, final IEvaluationProcessRegistry evaluationProcessRegistry,
	// @Named(ENABLED_CONSTRAINT_NAMES) final List<String> enabledConstraintNames, @Named(ENABLED_FITNESS_NAMES) final List<String> enabledFitnessNames) {
	//
	// final List<String> components = new ArrayList<String>(enabledFitnessNames);
	// components.retainAll(fitnessFunctionRegistry.getFitnessComponentNames());
	//
	// final List<String> checkers = new ArrayList<String>(enabledConstraintNames);
	// checkers.retainAll(constraintCheckerRegistry.getConstraintCheckerNames());
	//
	// // Enable all processes
	// // final List<String> evaluationProcesses = getEnabledEvaluationProcessNames();
	// // log.debug("Available evaluation processes: " + evaluationProcesses);
	// // evaluationProcesses.retainAll(evaluationProcessRegistry.getEvaluationProcessNames());
	//
	// final List<String> evaluationProcesses = new ArrayList<String>(evaluationProcessRegistry.getEvaluationProcessNames());
	//
	// return new OptimisationContext(data, sequences, components, fitnessFunctionRegistry, checkers, constraintCheckerRegistry, evaluationProcesses, evaluationProcessRegistry);
	// }

	@Provides
	@Singleton
	@Named(ENABLED_CONSTRAINT_NAMES)
	private List<String> provideEnabledConstraintNames(final IConstraintCheckerRegistry registry) {
		final List<String> result = new ArrayList<String>(registry.getConstraintCheckerNames());
		return result;
	}

	@Provides
	@Singleton
	@Named(ENABLED_FITNESS_NAMES)
	private List<String> provideEnabledFitnessFunctionNames(final IFitnessFunctionRegistry registry) {
		final List<String> result = new ArrayList<String>(registry.getFitnessComponentNames());
		return result;
	}

	@Provides
	@Singleton
	private IInitialSequenceBuilder provideIInitialSequenceBuilder(final List<IPairwiseConstraintChecker> pairwiseCheckers) {
		final IInitialSequenceBuilder builder = new ConstrainedInitialSequenceBuilder(pairwiseCheckers);

		return builder;
	}

	@Provides
	@Singleton
	@Named("Initial")
	private ISequences provideInitialSequences(final IInitialSequenceBuilder sequenceBuilder) {

		final ISequences sequences = sequenceBuilder.createInitialSequences(data, null, null, Collections.<ISequenceElement, ISequenceElement> emptyMap());

		return sequences;
	}

}
