/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.optimiser.common.constraints.OrderedSequenceElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.impl.ConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.impl.EvaluatedStateConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.fitness.impl.FitnessHelper;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScopeModule;
import com.mmxlabs.optimiser.core.modules.ConstraintCheckerInstantiatorModule;
import com.mmxlabs.optimiser.core.modules.EvaluatedStateConstraintCheckerInstantiatorModule;
import com.mmxlabs.optimiser.core.modules.EvaluationProcessInstantiatorModule;
import com.mmxlabs.optimiser.core.modules.FitnessFunctionInstantiatorModule;
import com.mmxlabs.optimiser.core.modules.OptimiserContextModule;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.cache.CacheMode;
import com.mmxlabs.scheduler.optimiser.constraints.impl.CapacityEvaluatedStateCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LatenessEvaluatedStateCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortTypeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.VesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.curves.PriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.entities.impl.DefaultEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.evaluation.DefaultVoyagePlanEvaluator;
import com.mmxlabs.scheduler.optimiser.evaluation.IVoyagePlanEvaluator;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcessFactory;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.fitness.components.ExcessIdleTimeComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.fitness.components.LatenessComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.UnconstrainedVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.utils.IBoilOffHelper;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.utils.InPortBoilOffHelper;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.ConstrainedInitialSequenceBuilder;
import com.mmxlabs.scheduler.optimiser.initialsequencebuilder.IInitialSequenceBuilder;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProvider;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.ITimeWindowSchedulingCanalDistanceProvider;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.TimeWindowSchedulingCanalDistanceProvider;
import com.mmxlabs.scheduler.optimiser.scheduling.IArrivalTimeScheduler;
import com.mmxlabs.scheduler.optimiser.scheduling.TimeWindowScheduler;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;

public class ScheduleTestModule extends AbstractModule {

	private final @NonNull IOptimisationData data;

	private IPhaseOptimisationData pData;

	public ScheduleTestModule(final @NonNull IOptimisationData data, IPhaseOptimisationData pData) {
		this.data = data;
		this.pData = pData;
	}

	@Override
	protected void configure() {
		install(new ThreadLocalScopeModule());
		install(new OptimiserContextModule());
		install(new ConstraintCheckerInstantiatorModule());
		install(new FitnessFunctionInstantiatorModule());
		install(new EvaluationProcessInstantiatorModule());
		install(new EvaluatedStateConstraintCheckerInstantiatorModule());

		bind(IFitnessHelper.class).to(FitnessHelper.class);

		bind(IOptimisationData.class).toInstance(data);
		bind(IPhaseOptimisationData.class).toInstance(pData);

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
		// bind(ICharterRateCalculator.class).to(VesselStartDateCharterRateCalculator.class);

		bind(IVoyagePlanner.class).to(VoyagePlanner.class);

		bind(VoyagePlanOptimiser.class);

		bind(IVesselBaseFuelCalculator.class).to(VesselBaseFuelCalculator.class);
		bind(VesselBaseFuelCalculator.class).in(Singleton.class);

		bind(IEntityValueCalculator.class).to(DefaultEntityValueCalculator.class);
		bind(IVoyagePlanEvaluator.class).to(DefaultVoyagePlanEvaluator.class);
		bind(IBoilOffHelper.class).toInstance(new InPortBoilOffHelper(true));

		bind(IArrivalTimeScheduler.class).to(TimeWindowScheduler.class);
		bind(IPriceIntervalProducer.class).to(PriceIntervalProducer.class);
		bind(ITimeWindowSchedulingCanalDistanceProvider.class).to(TimeWindowSchedulingCanalDistanceProvider.class);

		bind(long.class).annotatedWith(Names.named(SchedulerConstants.KEY_DEFAULT_MAX_VOLUME_IN_M3)).toInstance(OptimiserUnitConvertor.convertToInternalVolume(140_000));

		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_UsePriceBasedWindowTrimming)).toInstance(Boolean.FALSE);
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_UsePNLBasedWindowTrimming)).toInstance(Boolean.FALSE);
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_UseCanalSlotBasedWindowTrimming)).toInstance(Boolean.FALSE);
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_UseBestPanamaCanalIdleDaysWindowTrimming)).toInstance(Boolean.FALSE);
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_UseHeelRetention)).toInstance(Boolean.FALSE);

		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.OPTIMISE_PAPER_PNL)).toInstance(Boolean.FALSE);
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.GENERATED_PAPERS_IN_PNL)).toInstance(Boolean.FALSE);
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.COMPUTE_PAPER_PNL)).toInstance(Boolean.FALSE);
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.INDIVIDUAL_EXPOSURES)).toInstance(Boolean.FALSE);
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.COMPUTE_EXPOSURES)).toInstance(Boolean.FALSE);
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.RE_HEDGE_WITH_PAPERS)).toInstance(Boolean.FALSE);
		// Pricing bases
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.PRICING_BASES)).toInstance(Boolean.FALSE);
		
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.PROCESS_TRANSFER_MODEL)).toInstance(Boolean.FALSE);

		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.SCENARIO_TYPE_ADP)).toInstance(Boolean.FALSE);
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.SCENARIO_TYPE_LONG_TERM)).toInstance(Boolean.FALSE);

		bind(CacheMode.class).annotatedWith(Names.named(SchedulerConstants.Key_TimeWindowSchedulerCache)).toInstance(CacheMode.Off);
		bind(CacheMode.class).annotatedWith(Names.named(SchedulerConstants.Key_VoyagePlanEvaluatorCache)).toInstance(CacheMode.Off);
		bind(CacheMode.class).annotatedWith(Names.named(SchedulerConstants.Key_PNLBasedWindowTrimmerCache)).toInstance(CacheMode.Off);
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.HINT_DISABLE_CACHES)).toInstance(Boolean.FALSE);

		bind(int.class).annotatedWith(Names.named(SchedulerConstants.CHARTER_LENGTH_MIN_IDLE_HOURS)).toInstance(Integer.MAX_VALUE);
		bind(int.class).annotatedWith(Names.named(SchedulerConstants.COOLDOWN_MIN_IDLE_HOURS)).toInstance(Integer.MAX_VALUE);
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.Key_SchedulePurges)).toInstance(Boolean.FALSE);
		bind(boolean.class).annotatedWith(Names.named(SchedulerConstants.COMMERCIAL_VOLUME_OVERCAPACITY)).toInstance(Boolean.FALSE);

		bind(IIntegerIntervalCurve.class).annotatedWith(Names.named(SchedulerConstants.MIDNIGHT_ALIGNED_INTEGER_INTERVAL_CURVE)).toInstance(Mockito.mock(IIntegerIntervalCurve.class));

		bind(IPanamaBookingsProvider.class).toInstance(Mockito.mock(IPanamaBookingsProvider.class));
	}

	@Provides
	private IVoyagePlanOptimiser provideVoyagePlanOptimiser(final @NonNull VoyagePlanOptimiser delegate) {
		return delegate;
	}

	@Provides
	@Singleton
	private IConstraintCheckerRegistry createConstraintRegistry(final Injector injector) {
		final IConstraintCheckerRegistry constraintRegistry = new ConstraintCheckerRegistry();

		final OrderedSequenceElementsConstraintCheckerFactory constraintFactory = new OrderedSequenceElementsConstraintCheckerFactory();
		constraintRegistry.registerConstraintCheckerFactory(constraintFactory);

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
	private IEvaluatedStateConstraintCheckerRegistry createEvaluatedStateConstraintCheckerRegistry(final Injector injector) {
		final IEvaluatedStateConstraintCheckerRegistry evaluationProcessRegistry = new EvaluatedStateConstraintCheckerRegistry();

		{
			final LatenessEvaluatedStateCheckerFactory factory = new LatenessEvaluatedStateCheckerFactory();
			injector.injectMembers(factory);
			evaluationProcessRegistry.registerConstraintCheckerFactory(factory);
		}
		{
			final CapacityEvaluatedStateCheckerFactory factory = new CapacityEvaluatedStateCheckerFactory();
			injector.injectMembers(factory);
			evaluationProcessRegistry.registerConstraintCheckerFactory(factory);
		}

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
		final List<String> result = new ArrayList<>(registry.getFitnessComponentNames());
		return result;
	}

	@Provides
	@Singleton
	@Named(EvaluationProcessInstantiatorModule.ENABLED_EVALUATION_PROCESS_NAMES)
	private List<String> provideEnabledEvaluationProcessNames(final IEvaluationProcessRegistry registry) {
		final List<String> result = new ArrayList<>(registry.getEvaluationProcessNames());
		return result;
	}

	@Provides
	@Singleton
	@Named(EvaluatedStateConstraintCheckerInstantiatorModule.ENABLED_EVALUATED_STATE_CONSTRAINT_NAMES)
	private List<String> provideEnabledEvaluatedStateCheckerNames(final IEvaluatedStateConstraintCheckerRegistry registry) {
		final List<String> result = new ArrayList<>(registry.getConstraintCheckerNames());
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

		final ISequences sequences = sequenceBuilder.createInitialSequences(pData, null, null, Collections.<ISequenceElement, ISequenceElement> emptyMap());

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
	private IExcessIdleTimeComponentParameters provideIdleComponentParameters() {
		final ExcessIdleTimeComponentParameters idleParams = new ExcessIdleTimeComponentParameters();
		int highPeriodInDays = 15;
		int lowPeriodInDays = Math.max(0, highPeriodInDays - 2);
		idleParams.setThreshold(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.LOW, lowPeriodInDays * 24);
		idleParams.setThreshold(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.HIGH, highPeriodInDays * 24);
		idleParams.setWeight(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.LOW, 2_500);
		idleParams.setWeight(com.mmxlabs.scheduler.optimiser.fitness.components.IExcessIdleTimeComponentParameters.Interval.HIGH, 10_000);
		idleParams.setEndWeight(10_000);

		return idleParams;
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
