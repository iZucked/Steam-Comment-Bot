/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.fitness.components.CharterCostFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.CostComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.DistanceComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.LatenessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.RouteCostFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.CargoAllocatingSchedulerComponent;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlanAnnotator;

/**
 * {@link IFitnessCore} which schedules {@link ISequences} objects using an
 * {@link ISequenceScheduler}. Various {@link IFitnessComponent}s implementing
 * {@link ICargoSchedulerFitnessComponent} calculate fitnesses on the scheduled
 * {@link ISequences}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class CargoSchedulerFitnessCore<T> implements IFitnessCore<T> {

	private final List<ICargoSchedulerFitnessComponent<T>> components;

	private ISequenceScheduler<T> scheduler;

	private ISchedulerFactory<T> schedulerFactory;

	public CargoSchedulerFitnessCore() {
		components = new ArrayList<ICargoSchedulerFitnessComponent<T>>(5);
		components
				.add(new DistanceComponent<T>(
						CargoSchedulerFitnessCoreFactory.DISTANCE_COMPONENT_NAME,
						this));
		components
				.add(new LatenessComponent<T>(
						CargoSchedulerFitnessCoreFactory.LATENESS_COMPONENT_NAME,
						this));

		components.add(new CostComponent<T>(
				CargoSchedulerFitnessCoreFactory.COST_LNG_COMPONENT_NAME,
				CollectionsUtil.makeArrayList(FuelComponent.NBO,
						FuelComponent.FBO, FuelComponent.IdleNBO), this));

		components.add(new CostComponent<T>(
				CargoSchedulerFitnessCoreFactory.COST_BASE_COMPONENT_NAME,
				CollectionsUtil
						.makeArrayList(FuelComponent.Base,
								FuelComponent.Base_Supplemental,
								FuelComponent.IdleBase), this));

		components.add(new CharterCostFitnessComponent<T>(
				CargoSchedulerFitnessCoreFactory.CHARTER_COST_COMPONENT_NAME,
				SchedulerConstants.DCP_vesselProvider, // not sure if this
														// should be here or
														// somewhere else
				this));

		components.add(new RouteCostFitnessComponent<T>(
				CargoSchedulerFitnessCoreFactory.ROUTE_PRICE_COMPONENT_NAME,
				SchedulerConstants.DCP_routePriceProvider,
				SchedulerConstants.DCP_vesselProvider, this));

		components
				.add(new CargoAllocatingSchedulerComponent<T>(
						CargoSchedulerFitnessCoreFactory.CARGO_ALLOCATION_COMPONENT_NAME,
						SchedulerConstants.DCP_vesselProvider,
						SchedulerConstants.DCP_totalVolumeLimitProvider,
						SchedulerConstants.DCP_portSlotsProvider, this));
	}

	@Override
	public void accepted(final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {

		for (final ICargoSchedulerFitnessComponent<T> component : components) {
			component.acceptLastEvaluation();
		}
	}

	final VoyagePlanIterator<T> planIterator = new VoyagePlanIterator<T>();

	@Override
	public boolean evaluate(final ISequences<T> sequences) {
		return planIterator.iterateSchedulerComponents(components,
				scheduler.schedule(sequences));
	}

	@Override
	public boolean evaluate(final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {
		// At the moment, we fully evaluate even when only some routes have been
		// changed; in truth, because the question for components is whether the
		// <em>schedule</em> has changed (incl. arrival times), and changing one
		// sequence can change times in other sequences because the scheduler
		// components can have global effects, we really need to have a
		// cooperation with the scheduler here to make this any use.
		return planIterator.iterateSchedulerComponents(components,
				scheduler.schedule(sequences));
	}

	@Override
	public Collection<IFitnessComponent<T>> getFitnessComponents() {
		return new ArrayList<IFitnessComponent<T>>(components);
	}

	@Override
	public void init(final IOptimisationData<T> data) {
		scheduler = schedulerFactory.createScheduler(data, components);

		// Notify fitness components that a new optimisation is beginning
		for (final ICargoSchedulerFitnessComponent<T> c : components) {
			c.init(data);
		}
	}

	@Override
	public void dispose() {

		for (final ICargoSchedulerFitnessComponent<T> c : components) {
			c.dispose();
		}
		components.clear();
		scheduler.dispose();
	}

	public List<ICargoSchedulerFitnessComponent<T>> getCargoSchedulerFitnessComponent() {
		return components;
	}

	public void setSchedulerFactory(ISchedulerFactory<T> schedulerFactory) {
		this.schedulerFactory = schedulerFactory;
	}

	@Override
	public void annotate(final ISequences<T> sequences,
			final IAnnotatedSolution<T> solution) {
		final ScheduledSequences schedule = scheduler.schedule(sequences);
		// Do basic voyageplan annotation
		final VoyagePlanAnnotator<T> annotator = new VoyagePlanAnnotator<T>();

		@SuppressWarnings("unchecked")
		final IPortSlotProvider<T> portSlotProvider = solution
				.getContext()
				.getOptimisationData()
				.getDataComponentProvider(
						SchedulerConstants.DCP_portSlotsProvider,
						IPortSlotProvider.class);

		annotator.setPortSlotProvider(portSlotProvider);

		for (final ScheduledSequence scheduledSequence : schedule) {
			final IResource resource = scheduledSequence.getResource();
			final ISequence<T> sequence = sequences.getSequence(resource);

			if (sequence.size() > 0) {
				annotator.annotateFromScheduledSequence(scheduledSequence,
						solution);
			}
		}

		// set up per-route fitness map, which components can put their fitness
		// in
		{
			final Map<IResource, Map<String, Long>> fitnessPerRoute = new HashMap<IResource, Map<String, Long>>();
			for (final IResource resource : solution.getSequences()
					.getResources()) {
				fitnessPerRoute.put(resource, new HashMap<String, Long>());
			}

			solution.setGeneralAnnotation(
					SchedulerConstants.G_AI_fitnessPerRoute, fitnessPerRoute);
		}

		// Allow components to do any extra annotations
		planIterator
				.annotateSchedulerComponents(components, schedule, solution);
	}
}
