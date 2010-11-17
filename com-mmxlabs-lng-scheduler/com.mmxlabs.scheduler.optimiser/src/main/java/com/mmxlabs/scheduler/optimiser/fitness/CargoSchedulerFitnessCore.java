/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IResource;
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
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.CargoAllocatingComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.CargoAllocatingSchedulerComponent;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

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
				SchedulerConstants.DCP_vesselProvider,
				this));
		
		components.add(
				new CargoAllocatingSchedulerComponent<T>(
						CargoSchedulerFitnessCoreFactory.CARGO_ALLOCATION_COMPONENT_NAME,
						SchedulerConstants.DCP_vesselProvider,
						SchedulerConstants.DCP_totalVolumeLimitProvider,
						this));
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
		// perform a full evaluation

		return planIterator.iterateSchedulerComponents(components,
				scheduler.schedule(sequences));
	}

	@Override
	public boolean evaluate(final ISequences<T> sequences,
			final Collection<IResource> affectedResources) {

		// now we have some real horror - the scheduler needs to deal with which
		// sequences are changed
		// if we are to do this efficiently. Currently there is no
		// implementation of this, and it's hard to see
		// how there can be one if we really want to allow scheduler components
		// to depend on every route together.

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
}
