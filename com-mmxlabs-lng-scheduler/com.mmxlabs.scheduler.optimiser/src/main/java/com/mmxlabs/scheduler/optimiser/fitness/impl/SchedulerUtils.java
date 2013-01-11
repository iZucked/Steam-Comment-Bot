/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.Collection;

import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoAllocationFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ITotalVolumeLimitProvider;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.FastCargoAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.UnconstrainedCargoAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.DirectRandomSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.ScheduleEvaluator;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;

public final class SchedulerUtils {
	private final static int DEFAULT_VPO_CACHE_SIZE = 20000;

	private SchedulerUtils() {

	}

	/**
	 * Create a new {@link ISequenceScheduler} instance.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ISequenceScheduler createSimpleSequenceScheduler(final IOptimisationData data) {

		final SimpleSequenceScheduler scheduler = new SimpleSequenceScheduler();

		scheduler.setDistanceProvider(data.getDataComponentProvider(SchedulerConstants.DCP_portDistanceProvider, IMultiMatrixProvider.class));
		scheduler.setDurationsProvider(data.getDataComponentProvider(SchedulerConstants.DCP_elementDurationsProvider, IElementDurationProvider.class));
		scheduler.setPortProvider(data.getDataComponentProvider(SchedulerConstants.DCP_portProvider, IPortProvider.class));
		scheduler.setTimeWindowProvider(data.getDataComponentProvider(SchedulerConstants.DCP_timeWindowProvider, ITimeWindowDataComponentProvider.class));
		scheduler.setPortSlotProvider(data.getDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, IPortSlotProvider.class));
		scheduler.setPortTypeProvider(data.getDataComponentProvider(SchedulerConstants.DCP_portTypeProvider, IPortTypeProvider.class));

		scheduler.setVesselProvider(data.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class));

		scheduler.setVoyagePlanOptimiser(createVPO(data, DEFAULT_VPO_CACHE_SIZE));

		scheduler.init();

		return scheduler;
	}

	public static IVoyagePlanOptimiser createVoyagePlanOptimiser(final IOptimisationData data) {
		final LNGVoyageCalculator voyageCalculator = new LNGVoyageCalculator();

		voyageCalculator.setRouteCostDataComponentProvider(data.getDataComponentProvider(SchedulerConstants.DCP_routePriceProvider, IRouteCostProvider.class));

		voyageCalculator.init();

		final VoyagePlanOptimiser voyagePlanOptimiser = new VoyagePlanOptimiser(voyageCalculator);
		return voyagePlanOptimiser;
	}

	public static IVoyagePlanOptimiser createCachingVoyagePlanOptimiser(final IOptimisationData data, final int cacheSize) {
		// duplicate code due to type erasure

		final LNGVoyageCalculator voyageCalculator = new LNGVoyageCalculator();

		voyageCalculator.setRouteCostDataComponentProvider(data.getDataComponentProvider(SchedulerConstants.DCP_routePriceProvider, IRouteCostProvider.class));

		voyageCalculator.init();

		final VoyagePlanOptimiser voyagePlanOptimiser = new VoyagePlanOptimiser(voyageCalculator);

		if (cacheSize == 0) {
			return voyagePlanOptimiser;
		} else {
			return new CachingVoyagePlanOptimiser(voyagePlanOptimiser, cacheSize);
		}
	}

	public static IVoyagePlanOptimiser createVPO(final IOptimisationData data, final int vpoCacheSize) {
		// return createVoyagePlanOptimiser();
		return createCachingVoyagePlanOptimiser(data, vpoCacheSize);
		// return new CheckingVPO(
		// (IVoyagePlanOptimiser)createCachingVoyagePlanOptimiser(vpoCacheSize),
		// (IVoyagePlanOptimiser)createVoyagePlanOptimiser());
	}

	//
	// public static ISequenceScheduler createGASequenceScheduler(
	// final IOptimisationData data,
	// final Collection<ICargoSchedulerFitnessComponent> fitnessComponents) {
	// return createGASequenceScheduler(data, fitnessComponents,
	// DEFAULT_VPO_CACHE_SIZE);
	// }

	/**
	 * Set all the standard DCPs for any abstract sequence scheduler
	 * 
	 * @param
	 * @param data
	 * @param scheduler
	 */
	@SuppressWarnings("unchecked")
	public static void setDataComponentProviders(final IOptimisationData data, final AbstractSequenceScheduler scheduler) {
		final IMultiMatrixProvider<IPort, Integer> distanceProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portDistanceProvider, IMultiMatrixProvider.class);

		scheduler.setDistanceProvider(distanceProvider);

		final IElementDurationProvider durationsProvider = data.getDataComponentProvider(SchedulerConstants.DCP_elementDurationsProvider, IElementDurationProvider.class);
		scheduler.setDurationsProvider(durationsProvider);

		final IPortProvider portProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portProvider, IPortProvider.class);
		scheduler.setPortProvider(portProvider);

		scheduler.setRouteCostProvider(data.getDataComponentProvider(SchedulerConstants.DCP_routePriceProvider, IRouteCostProvider.class));

		final ITimeWindowDataComponentProvider timeWindowProvider = data.getDataComponentProvider(SchedulerConstants.DCP_timeWindowProvider, ITimeWindowDataComponentProvider.class);
		scheduler.setTimeWindowProvider(timeWindowProvider);

		final IPortSlotProvider portSlotProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, IPortSlotProvider.class);

		scheduler.setPortSlotProvider(portSlotProvider);

		scheduler.setPortTypeProvider(data.getDataComponentProvider(SchedulerConstants.DCP_portTypeProvider, IPortTypeProvider.class));

		final IVesselProvider vesselProvider = data.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
		scheduler.setVesselProvider(vesselProvider);
	}

	// public static RandomSeparatedSequenceScheduler
	// createRandomSeparatedSequenceScheduler(
	// final IOptimisationData data,
	// final Collection<ICargoSchedulerFitnessComponent> fitnessComponents,
	// final int vpoCacheSize) {
	// final RandomSeparatedSequenceScheduler scheduler =
	// new RandomSeparatedSequenceScheduler();
	// final ScheduleEvaluator evaluator = new ScheduleEvaluator();
	//
	// //set up scheduler
	// setDataComponentProviders(data, scheduler);
	// scheduler.setVoyagePlanOptimiser((IVoyagePlanOptimiser)
	// createVPO(vpoCacheSize));
	// scheduler.setScheduleEvaluator(evaluator);
	//
	// evaluator.setFitnessComponents(fitnessComponents);
	//
	// return scheduler;
	// }
	//
	// public static RandomSeparatedSequenceScheduler
	// createRandomSeparatedSequenceScheduler(
	// final IOptimisationData data,
	// final Collection<ICargoSchedulerFitnessComponent> fitnessComponents) {
	// return createRandomSeparatedSequenceScheduler(data, fitnessComponents,
	// DEFAULT_VPO_CACHE_SIZE);
	// }
	//
	// public static RandomSequenceScheduler
	// createRandomSequenceScheduler(
	// final IOptimisationData data,
	// final Collection<ICargoSchedulerFitnessComponent> fitnessComponents,
	// int vpoCacheSize) {
	// final RandomSequenceScheduler scheduler = new
	// RandomSequenceScheduler();
	//
	// setDataComponentProviders(data, scheduler);
	//
	// scheduler
	// .setVoyagePlanOptimiser((IVoyagePlanOptimiser)
	// createVPO(vpoCacheSize));
	//
	// final IndividualEvaluator individualEvaluator =
	// createIndividualEvaluator(
	// scheduler, scheduler.getTimeWindowProvider(),
	// fitnessComponents, scheduler.getDistanceProvider(),
	// scheduler.getPortProvider(), scheduler.getDurationsProvider(),
	// scheduler.getVesselProvider());
	//
	// individualEvaluator.init();
	//
	// scheduler.setIndividualEvaluator(individualEvaluator);
	//
	// scheduler.setSampleMultiplier(50);
	//
	// return scheduler;
	// }
	//
	// private static IndividualEvaluator createIndividualEvaluator(
	// AbstractSequenceScheduler scheduler,
	// ITimeWindowDataComponentProvider timeWindowProvider,
	// Collection<ICargoSchedulerFitnessComponent> fitnessComponents,
	// IMultiMatrixProvider<IPort, Integer> distanceProvider,
	// IPortProvider portProvider,
	// IElementDurationProvider durationsProvider,
	// IVesselProvider vesselProvider) {
	// IndividualEvaluator result = new IndividualEvaluator();
	// result.setSequenceScheduler(scheduler);
	// result.setTimeWindowProvider(timeWindowProvider);
	// result.setFitnessComponents(fitnessComponents);
	// result.setDistanceProvider(distanceProvider);
	// result.setPortProvider(portProvider);
	// result.setDurationsProvider(durationsProvider);
	// result.setVesselProvider(vesselProvider);
	// return result;
	// }
	//
	// /**
	// * Create a new {@link ISequenceScheduler} instance.
	// *
	// * @param vpoCacheSize
	// *
	// * @return
	// */
	// @SuppressWarnings("unchecked")
	// public static ISequenceScheduler createGASequenceScheduler(
	// final IOptimisationData data,
	// final Collection<ICargoSchedulerFitnessComponent> fitnessComponents,
	// int vpoCacheSize) {
	//
	// final GASequenceScheduler scheduler = new GASequenceScheduler();
	//
	// setDataComponentProviders(data, scheduler);
	//
	// scheduler
	// .setVoyagePlanOptimiser((IVoyagePlanOptimiser)
	// createVPO(vpoCacheSize));
	//
	// // Set GA params
	// final IndividualEvaluator individualEvaluator =
	// createIndividualEvaluator(
	// scheduler, scheduler.getTimeWindowProvider(),
	// fitnessComponents, scheduler.getDistanceProvider(),
	// scheduler.getPortProvider(), scheduler.getDurationsProvider(),
	// scheduler.getVesselProvider());
	//
	// // TODO: Use same weights as passed into LinearCombiner?
	// // individualEvaluator.setFitnessComponentWeights(null);
	// individualEvaluator.init();
	//
	// scheduler.setIndividualEvaluator(individualEvaluator);
	//
	// // TODO: Pull from external API parameters held in the
	// // IOptimisationContext
	// // Set
	// scheduler.setMutateThreshold(0.01f);
	// // Population of 40 individuals
	// scheduler.setPopulationSize(40);
	// // Retain top 10 each iteration
	// scheduler.setTopN(10);
	// // Have 2 iterations for every byte in the individuals
	// scheduler.setIterationsByteMultiplier(1);
	//
	// scheduler.init();
	//
	// return scheduler;
	// }
	//
	// public static ISequenceScheduler createRandomSequenceScheduler(
	// IOptimisationData data, Collection<ICargoSchedulerFitnessComponent>
	// components) {
	// return createRandomSequenceScheduler(data, components,
	// DEFAULT_VPO_CACHE_SIZE);
	// }

	// public static RandomSeparatedSequenceScheduler
	// createRandomSeparatedSequenceScheduler(
	// final IOptimisationData data,
	// final Collection<ICargoSchedulerFitnessComponent> fitnessComponents,
	// final int vpoCacheSize) {
	// final RandomSeparatedSequenceScheduler scheduler =
	// new RandomSeparatedSequenceScheduler();
	// final ScheduleEvaluator evaluator = new ScheduleEvaluator();
	//
	// //set up scheduler
	// setDataComponentProviders(data, scheduler);
	// scheduler.setVoyagePlanOptimiser((IVoyagePlanOptimiser)
	// createVPO(vpoCacheSize));
	// scheduler.setScheduleEvaluator(evaluator);
	//
	// evaluator.setFitnessComponents(fitnessComponents);
	//
	// return scheduler;
	// }
	//
	// public static RandomSeparatedSequenceScheduler
	// createRandomSeparatedSequenceScheduler(
	// final IOptimisationData data,
	// final Collection<ICargoSchedulerFitnessComponent> fitnessComponents) {
	// return createRandomSeparatedSequenceScheduler(data, fitnessComponents,
	// DEFAULT_VPO_CACHE_SIZE);
	// }
	//
	// public static RandomSequenceScheduler
	// createRandomSequenceScheduler(
	// final IOptimisationData data,
	// final Collection<ICargoSchedulerFitnessComponent> fitnessComponents,
	// int vpoCacheSize) {
	// final RandomSequenceScheduler scheduler = new
	// RandomSequenceScheduler();
	//
	// setDataComponentProviders(data, scheduler);
	//
	// scheduler
	// .setVoyagePlanOptimiser((IVoyagePlanOptimiser)
	// createVPO(vpoCacheSize));
	//
	// final IndividualEvaluator individualEvaluator =
	// createIndividualEvaluator(
	// scheduler, scheduler.getTimeWindowProvider(),
	// fitnessComponents, scheduler.getDistanceProvider(),
	// scheduler.getPortProvider(), scheduler.getDurationsProvider(),
	// scheduler.getVesselProvider());
	//
	// individualEvaluator.init();
	//
	// scheduler.setIndividualEvaluator(individualEvaluator);
	//
	// scheduler.setSampleMultiplier(50);
	//
	// return scheduler;
	// }
	//
	// private static IndividualEvaluator createIndividualEvaluator(
	// AbstractSequenceScheduler scheduler,
	// ITimeWindowDataComponentProvider timeWindowProvider,
	// Collection<ICargoSchedulerFitnessComponent> fitnessComponents,
	// IMultiMatrixProvider<IPort, Integer> distanceProvider,
	// IPortProvider portProvider,
	// IElementDurationProvider durationsProvider,
	// IVesselProvider vesselProvider) {
	// IndividualEvaluator result = new IndividualEvaluator();
	// result.setSequenceScheduler(scheduler);
	// result.setTimeWindowProvider(timeWindowProvider);
	// result.setFitnessComponents(fitnessComponents);
	// result.setDistanceProvider(distanceProvider);
	// result.setPortProvider(portProvider);
	// result.setDurationsProvider(durationsProvider);
	// result.setVesselProvider(vesselProvider);
	// return result;
	// }
	//
	// /**
	// * Create a new {@link ISequenceScheduler} instance.
	// *
	// * @param vpoCacheSize
	// *
	// * @return
	// */
	// @SuppressWarnings("unchecked")
	// public static ISequenceScheduler createGASequenceScheduler(
	// final IOptimisationData data,
	// final Collection<ICargoSchedulerFitnessComponent> fitnessComponents,
	// int vpoCacheSize) {
	//
	// final GASequenceScheduler scheduler = new GASequenceScheduler();
	//
	// setDataComponentProviders(data, scheduler);
	//
	// scheduler
	// .setVoyagePlanOptimiser((IVoyagePlanOptimiser)
	// createVPO(vpoCacheSize));
	//
	// // Set GA params
	// final IndividualEvaluator individualEvaluator =
	// createIndividualEvaluator(
	// scheduler, scheduler.getTimeWindowProvider(),
	// fitnessComponents, scheduler.getDistanceProvider(),
	// scheduler.getPortProvider(), scheduler.getDurationsProvider(),
	// scheduler.getVesselProvider());
	//
	// // TODO: Use same weights as passed into LinearCombiner?
	// // individualEvaluator.setFitnessComponentWeights(null);
	// individualEvaluator.init();
	//
	// scheduler.setIndividualEvaluator(individualEvaluator);
	//
	// // TODO: Pull from external API parameters held in the
	// // IOptimisationContext
	// // Set
	// scheduler.setMutateThreshold(0.01f);
	// // Population of 40 individuals
	// scheduler.setPopulationSize(40);
	// // Retain top 10 each iteration
	// scheduler.setTopN(10);
	// // Have 2 iterations for every byte in the individuals
	// scheduler.setIterationsByteMultiplier(1);
	//
	// scheduler.init();
	//
	// return scheduler;
	// }
	//
	// public static ISequenceScheduler createRandomSequenceScheduler(
	// IOptimisationData data, Collection<ICargoSchedulerFitnessComponent>
	// components) {
	// return createRandomSequenceScheduler(data, components,
	// DEFAULT_VPO_CACHE_SIZE);
	// }

	public static DirectRandomSequenceScheduler createDirectRandomSequenceScheduler(final IOptimisationData data, final Collection<ICargoSchedulerFitnessComponent> components,
			final Collection<ICargoAllocationFitnessComponent> components2, final int vpoCacheSize) {

		final DirectRandomSequenceScheduler scheduler = new DirectRandomSequenceScheduler();
		final ScheduleEvaluator evaluator = new ScheduleEvaluator();

		// set up scheduler
		setDataComponentProviders(data, scheduler);
		scheduler.setVoyagePlanOptimiser(createVPO(data, vpoCacheSize));
		scheduler.setScheduleEvaluator(evaluator);

		// create cargo allocator

		final ITotalVolumeLimitProvider tvlp = data.getDataComponentProvider(SchedulerConstants.DCP_totalVolumeLimitProvider, ITotalVolumeLimitProvider.class);

		ICargoAllocator allocator;
		if (tvlp.isEmpty()) {
			allocator = new UnconstrainedCargoAllocator();
		} else {
			allocator = new FastCargoAllocator();
		}

		allocator.setVesselProvider(data.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class));
		allocator.setTotalVolumeLimitProvider(tvlp);
		allocator.init();

		evaluator.setFitnessComponents(components, components2);
		evaluator.setLoadPriceCalculators(data.getDataComponentProvider(SchedulerConstants.DCP_calculatorProvider, ICalculatorProvider.class).getLoadPriceCalculators());
		evaluator.setCargoAllocator(allocator);

		return scheduler;
	}

	public static DirectRandomSequenceScheduler createDirectRandomSequenceScheduler(final IOptimisationData data, final Collection<ICargoSchedulerFitnessComponent> components,
			final Collection<ICargoAllocationFitnessComponent> components2) {
		return createDirectRandomSequenceScheduler(data, components, components2, DEFAULT_VPO_CACHE_SIZE);
	}
}
