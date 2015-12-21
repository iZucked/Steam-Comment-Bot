/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.ArrayList;
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
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.impl.ConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessHelper;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeModule;
import com.mmxlabs.optimiser.core.modules.ConstraintCheckerInstantiatorModule;
import com.mmxlabs.optimiser.core.modules.EvaluationProcessInstantiatorModule;
import com.mmxlabs.optimiser.core.modules.FitnessFunctionInstantiatorModule;
import com.mmxlabs.optimiser.core.modules.OptimiserContextModule;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortTypeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.VesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.VesselStartDateCharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcessFactory;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.fitness.components.LatenessComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.UnconstrainedVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.DirectRandomSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.ConstrainedInitialSequenceBuilder;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.IInitialSequenceBuilder;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;

public class ScheduleTestModule extends AbstractModule {

	private final static int DEFAULT_VPO_CACHE_SIZE = 20000;

	private final IOptimisationData data;

	public ScheduleTestModule(final IOptimisationData data) {
		this.data = data;
	}

	@Override
	protected void configure() {
		install(new PerChainUnitScopeModule());
		install(new OptimiserContextModule());
		install(new ConstraintCheckerInstantiatorModule());
		install(new FitnessFunctionInstantiatorModule());
		install(new EvaluationProcessInstantiatorModule());

		bind(IFitnessHelper.class).to(FitnessHelper.class);

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

		bind(IVolumeAllocator.class).to(UnconstrainedVolumeAllocator.class).in(Singleton.class);
		bind(ICharterRateCalculator.class).to(VesselStartDateCharterRateCalculator.class);

		bind(VoyagePlanOptimiser.class);

		bind(IVesselBaseFuelCalculator.class).to(VesselBaseFuelCalculator.class);
		bind(VesselBaseFuelCalculator.class).in(Singleton.class);

		// if (Platform.isRunning()) {
		// bind(IFitnessFunctionRegistry.class).toProvider(service(IFitnessFunctionRegistry.class).single());
		// bind(IConstraintCheckerRegistry.class).toProvider(service(IConstraintCheckerRegistry.class).single());
		// bind(IEvaluationProcessRegistry.class).toProvider(service(IEvaluationProcessRegistry.class).single());
		// }

		// bind(IOptimisationTransformer.class).to(OptimisationTransformer.class).in(Singleton.class);
	}

	@Provides
	private IVoyagePlanOptimiser provideVoyagePlanOptimiser(final VoyagePlanOptimiser delegate) {
		final CachingVoyagePlanOptimiser cachingVoyagePlanOptimiser = new CachingVoyagePlanOptimiser(delegate, DEFAULT_VPO_CACHE_SIZE);
		return cachingVoyagePlanOptimiser;
	}

	@Provides
	@Singleton
	private ISequenceScheduler provideSchedulerFactory(final Injector injector) {
		// final ISchedulerFactory factory = new ISchedulerFactory() {
		//
		// @Override
		// public ISequenceScheduler createScheduler(final IOptimisationData data, final Collection<ICargoSchedulerFitnessComponent> schedulerComponents) {
		// //
		// final ScheduleFitnessEvaluator scheduleEvaluator = new ScheduleFitnessEvaluator();
		// // TODO: If we can change this API, then we can avoid the need for the ISchedulerFactory and this provider
		// scheduleEvaluator.setFitnessComponents(schedulerComponents);
		// injector.injectMembers(scheduleEvaluator);

		final DirectRandomSequenceScheduler scheduler = new DirectRandomSequenceScheduler();
		// scheduler.setScheduleEvaluator(scheduleEvaluator);
		injector.injectMembers(scheduler);
		return scheduler;
		// }
		// };
		// return factory;
	}

	@Provides
	@Singleton
	private IConstraintCheckerRegistry createConstraintRegistry(final Injector injector) {
		final IConstraintCheckerRegistry constraintRegistry = new ConstraintCheckerRegistry();

		final OrderedSequenceElementsConstraintCheckerFactory constraintFactory = new OrderedSequenceElementsConstraintCheckerFactory();
		constraintRegistry.registerConstraintCheckerFactory(constraintFactory);
		injector.injectMembers(constraintFactory);

		final ResourceAllocationConstraintCheckerFactory constraintFactory2 = new ResourceAllocationConstraintCheckerFactory();
		constraintRegistry.registerConstraintCheckerFactory(constraintFactory2);
		injector.injectMembers(constraintFactory2);

		final PortTypeConstraintCheckerFactory constraintFactory3 = new PortTypeConstraintCheckerFactory();
		constraintRegistry.registerConstraintCheckerFactory(constraintFactory3);
		injector.injectMembers(constraintFactory3);

		return constraintRegistry;
	}

	@Provides
	@Singleton
	private IFitnessFunctionRegistry createFitnessRegistry(final Injector injector) {
		final FitnessFunctionRegistry fitnessRegistry = new FitnessFunctionRegistry();

		final CargoSchedulerFitnessCoreFactory factory = new CargoSchedulerFitnessCoreFactory();
		injector.injectMembers(factory);

		fitnessRegistry.registerFitnessCoreFactory(factory);
		return fitnessRegistry;
	}

	@Provides
	@Singleton
	private IEvaluationProcessRegistry createEvaluationProcessRegistry(final Injector injector) {
		final IEvaluationProcessRegistry evaluationProcessRegistry = new EvaluationProcessRegistry();

		final SchedulerEvaluationProcessFactory factory = new SchedulerEvaluationProcessFactory();
		injector.injectMembers(factory);

		evaluationProcessRegistry.registerEvaluationProcessFactory(factory);

		return evaluationProcessRegistry;
	}

	@Provides
	@Singleton
	@Named(ConstraintCheckerInstantiatorModule.ENABLED_CONSTRAINT_NAMES)
	private List<String> provideEnabledConstraintNames(final IConstraintCheckerRegistry registry) {
		final List<String> result = new ArrayList<String>(registry.getConstraintCheckerNames());
		return result;
	}

	@Provides
	@Singleton
	@Named(FitnessFunctionInstantiatorModule.ENABLED_FITNESS_NAMES)
	private List<String> provideEnabledFitnessFunctionNames(final IFitnessFunctionRegistry registry) {
		final List<String> result = new ArrayList<String>(registry.getFitnessComponentNames());
		return result;
	}

	@Provides
	@Singleton
	@Named(EvaluationProcessInstantiatorModule.ENABLED_EVALUATION_PROCESS_NAMES)
	private List<String> provideEnabledEvaluationProcessNames(final IEvaluationProcessRegistry registry) {
		final List<String> result = new ArrayList<String>(registry.getEvaluationProcessNames());
		return result;
	}

	@Provides
	@Singleton
	private IInitialSequenceBuilder provideIInitialSequenceBuilder(final Injector injector, final List<IPairwiseConstraintChecker> pairwiseCheckers) {
		final IInitialSequenceBuilder builder = new ConstrainedInitialSequenceBuilder(pairwiseCheckers);
		injector.injectMembers(builder);
		return builder;
	}

	@Provides
	@Singleton
	@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL)
	private ISequences provideInitialSequences(final IInitialSequenceBuilder sequenceBuilder) {

		final ISequences sequences = sequenceBuilder.createInitialSequences(data, null, null, Collections.<ISequenceElement, ISequenceElement> emptyMap());

		return sequences;
	}

	@Provides
	@Singleton
	@Named(OptimiserConstants.SEQUENCE_TYPE_INPUT)
	private ISequences provideInputSequences(@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL) ISequences initialSequences) {

		return initialSequences;
	}

	@Provides
	@Singleton
	private ILatenessComponentParameters provideLatenessParameters() {
		final LatenessComponentParameters lcp = new LatenessComponentParameters();

		lcp.setThreshold(Interval.PROMPT, 24);
		lcp.setLowWeight(Interval.PROMPT, 1);
		lcp.setHighWeight(Interval.PROMPT, 1);

		lcp.setThreshold(Interval.MID_TERM, 24);
		lcp.setLowWeight(Interval.MID_TERM, 1);
		lcp.setHighWeight(Interval.MID_TERM, 1);

		lcp.setThreshold(Interval.BEYOND, 24);
		lcp.setLowWeight(Interval.BEYOND, 1);
		lcp.setHighWeight(Interval.BEYOND, 1);
		return lcp;
	}
}
