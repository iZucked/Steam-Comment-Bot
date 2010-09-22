package com.mmxlabs.scheduler.optimiser.fitness.impl;


import java.util.Collection;

import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.ga.GASequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.ga.IndividualEvaluator;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;

public final class SchedulerUtils {

	private SchedulerUtils() {

	}

	/**
	 * Create a new {@link ISequenceScheduler} instance.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> ISequenceScheduler<T> createSimpleSequenceScheduler(
			final IOptimisationData<T> data) {

		final SimpleSequenceScheduler<T> scheduler = new SimpleSequenceScheduler<T>();

		scheduler.setDistanceProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_portDistanceProvider,
				IMultiMatrixProvider.class));
		scheduler.setDurationsProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_elementDurationsProvider,
				IElementDurationProvider.class));
		scheduler.setPortProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_portProvider, IPortProvider.class));
		scheduler.setTimeWindowProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_timeWindowProvider,
				ITimeWindowDataComponentProvider.class));
		scheduler.setPortSlotProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_portSlotsProvider,
				IPortSlotProvider.class));
		scheduler.setPortTypeProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_portTypeProvider,
				IPortTypeProvider.class));

		scheduler.setVesselProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_vesselProvider, IVesselProvider.class));

		scheduler.setVoyagePlanOptimiser((IVoyagePlanOptimiser<T>) 
				createVPO());

		scheduler.init();

		return scheduler;
	}
	
	public static <T> IVoyagePlanOptimiser<T> createVoyagePlanOptimiser() {
		final LNGVoyageCalculator<T> voyageCalculator = new LNGVoyageCalculator<T>();

		final VoyagePlanOptimiser<T> voyagePlanOptimiser = new VoyagePlanOptimiser<T>();
		voyagePlanOptimiser.setVoyageCalculator(voyageCalculator);
		return voyagePlanOptimiser;
	}
	
	
	public static <T> IVoyagePlanOptimiser<T> createCachingVoyagePlanOptimiser() {
		//duplicate code due to type erasure
		
		final LNGVoyageCalculator<T> voyageCalculator = new LNGVoyageCalculator<T>();

		final VoyagePlanOptimiser<T> voyagePlanOptimiser = new VoyagePlanOptimiser<T>();
		voyagePlanOptimiser.setVoyageCalculator(voyageCalculator);
		
		return new CachingVoyagePlanOptimiser<T>(voyagePlanOptimiser, 6000);
	}

	public static <T> IVoyagePlanOptimiser<T> createVPO(){
		return createCachingVoyagePlanOptimiser();
//		return new CheckingVPO<T>((IVoyagePlanOptimiser<T>)createVoyagePlanOptimiser(), 
//				(IVoyagePlanOptimiser<T>)createCachingVoyagePlanOptimiser());
	}
	
	/**
	 * Create a new {@link ISequenceScheduler} instance.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> ISequenceScheduler<T> createGASequenceScheduler(
			final IOptimisationData<T> data,
			final Collection<ICargoSchedulerFitnessComponent<T>> fitnessComponents) {

		final GASequenceScheduler<T> scheduler = new GASequenceScheduler<T>();

		final IMultiMatrixProvider<IPort, Integer> distanceProvider = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_portDistanceProvider,
						IMultiMatrixProvider.class);
		
		scheduler.setDistanceProvider(distanceProvider);
		
		final IElementDurationProvider<T> durationsProvider = data.getDataComponentProvider(
				SchedulerConstants.DCP_elementDurationsProvider,
				IElementDurationProvider.class);
		scheduler.setDurationsProvider(durationsProvider);
		
		final IPortProvider portProvider = data.getDataComponentProvider(
				SchedulerConstants.DCP_portProvider, IPortProvider.class);
		scheduler.setPortProvider(portProvider);
		
		final ITimeWindowDataComponentProvider timeWindowProvider = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_timeWindowProvider,
						ITimeWindowDataComponentProvider.class);
		scheduler.setTimeWindowProvider(timeWindowProvider);
		
		final IPortSlotProvider<T> portSlotProvider = data.getDataComponentProvider(
				SchedulerConstants.DCP_portSlotsProvider,
				IPortSlotProvider.class);
		
		scheduler.setPortSlotProvider(portSlotProvider);
		
		scheduler.setPortTypeProvider(data.getDataComponentProvider(
				SchedulerConstants.DCP_portTypeProvider,
				IPortTypeProvider.class));

		final IVesselProvider vesselProvider = data.getDataComponentProvider(
				SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
		scheduler.setVesselProvider(vesselProvider);


		scheduler.setVoyagePlanOptimiser(
				(IVoyagePlanOptimiser<T>) createVPO());

		// Set GA params
		final IndividualEvaluator<T> individualEvaluator = new IndividualEvaluator<T>();
		individualEvaluator.setSequenceScheduler(scheduler);
		individualEvaluator.setTimeWindowProvider(timeWindowProvider);
		individualEvaluator.setFitnessComponents(fitnessComponents);
		individualEvaluator.setDistanceProvider(distanceProvider);
		individualEvaluator.setPortProvider(portProvider);
		individualEvaluator.setDurationsProvider(durationsProvider);
		individualEvaluator.setVesselProvider(vesselProvider);

		// TODO: Use same weights as passed into LinearCombiner?
		// individualEvaluator.setFitnessComponentWeights(null);
		individualEvaluator.init();

		scheduler.setIndividualEvaluator(individualEvaluator);

		// TODO: Pull from external API parameters held in the
		// IOptimisationContext
		// Set
		scheduler.setMutateThreshold(0.01f);
		// Population of 40 individuals
		scheduler.setPopulationSize(40);
		// Retain top 10 each iteration
		scheduler.setTopN(10);
		// Have 2 iterations for every byte in the individuals
		scheduler.setIterationsByteMultiplier(2);

		scheduler.init();

		return scheduler;
	}
}
