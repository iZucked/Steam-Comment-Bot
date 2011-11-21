/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IAnnotations;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.contracts.IShippingPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.CharterCostFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.CharterOutFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.CostComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.DistanceComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.LatenessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.RouteCostFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlanAnnotator;
import com.mmxlabs.trading.optimiser.TradingConstants;
import com.mmxlabs.trading.optimiser.components.ProfitAndLossAllocationComponent;

/**
 * {@link IFitnessCore} which schedules {@link ISequences} objects using an {@link ISequenceScheduler}. Various {@link IFitnessComponent}s implementing {@link ICargoSchedulerFitnessComponent}
 * calculate fitnesses on the scheduled {@link ISequences}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class CargoSchedulerFitnessCore<T> implements IFitnessCore<T> {

	private final List<ICargoSchedulerFitnessComponent<T>> schedulerComponents;
	private final List<ICargoAllocationFitnessComponent<T>> allocationComponents;
	private final List<ICargoFitnessComponent<T>> allComponents;

	private ISequenceScheduler<T> scheduler;

	private ICalculatorProvider<T> calculatorProvider;

	private ISchedulerFactory<T> schedulerFactory;

	public CargoSchedulerFitnessCore() {
		allComponents = new ArrayList<ICargoFitnessComponent<T>>();
		allocationComponents = new ArrayList<ICargoAllocationFitnessComponent<T>>();
		schedulerComponents = new ArrayList<ICargoSchedulerFitnessComponent<T>>(5);
		schedulerComponents.add(new DistanceComponent<T>(CargoSchedulerFitnessCoreFactory.DISTANCE_COMPONENT_NAME, this));
		schedulerComponents.add(new LatenessComponent<T>(CargoSchedulerFitnessCoreFactory.LATENESS_COMPONENT_NAME, this));

		schedulerComponents
				.add(new CostComponent<T>(CargoSchedulerFitnessCoreFactory.COST_LNG_COMPONENT_NAME, CollectionsUtil.makeArrayList(FuelComponent.NBO, FuelComponent.FBO, FuelComponent.IdleNBO), this));

		schedulerComponents.add(new CostComponent<T>(CargoSchedulerFitnessCoreFactory.COST_BASE_COMPONENT_NAME, CollectionsUtil.makeArrayList(FuelComponent.Base, FuelComponent.Base_Supplemental,
				FuelComponent.IdleBase, FuelComponent.PilotLight, FuelComponent.IdlePilotLight), this));

		schedulerComponents.add(new CostComponent<T>(CargoSchedulerFitnessCoreFactory.COST_COOLDOWN_COMPONENT_NAME, CollectionsUtil.makeArrayList(FuelComponent.Cooldown), this));

		schedulerComponents.add(new CharterCostFitnessComponent<T>(CargoSchedulerFitnessCoreFactory.CHARTER_COST_COMPONENT_NAME, SchedulerConstants.DCP_vesselProvider, // not sure if this
																																								// should be here or
																																								// somewhere else
				this));

		schedulerComponents.add(new RouteCostFitnessComponent<T>(CargoSchedulerFitnessCoreFactory.ROUTE_PRICE_COMPONENT_NAME, SchedulerConstants.DCP_routePriceProvider,
				SchedulerConstants.DCP_vesselProvider,
				this));

		// schedulerComponents.add(new CargoAllocatingSchedulerComponent<T>(CargoSchedulerFitnessCoreFactory.CARGO_ALLOCATION_COMPONENT_NAME, SchedulerConstants.DCP_vesselProvider,
		// SchedulerConstants.DCP_totalVolumeLimitProvider, SchedulerConstants.DCP_portSlotsProvider, this));

		schedulerComponents.add(new CharterOutFitnessComponent<T>(CargoSchedulerFitnessCoreFactory.CHARTER_REVENUE_COMPONENT_NAME, SchedulerConstants.DCP_vesselProvider, this));

		allocationComponents.add(new ProfitAndLossAllocationComponent<T>(CargoSchedulerFitnessCoreFactory.PROFIT_COMPONENT_NAME, TradingConstants.DCP_entityProvider,
				SchedulerConstants.DCP_vesselProvider, SchedulerConstants.DCP_portSlotsProvider, this));
		
		allComponents.addAll(schedulerComponents);
		allComponents.addAll(allocationComponents);
	}

	@Override
	public void accepted(final ISequences<T> sequences, final Collection<IResource> affectedResources) {
		// we can clear the affected resources from last move, because
		// the scheduler doesn't need to reset its calculations for those resources
		lastAffectedResources.clear();
		for (final ICargoSchedulerFitnessComponent<T> component : schedulerComponents) {
			component.acceptLastEvaluation();
		}
		scheduler.acceptLastSchedule();
	}

	final VoyagePlanIterator<T> planIterator = new VoyagePlanIterator<T>();

	@Override
	public boolean evaluate(final ISequences<T> sequences) {
		for (final IShippingPriceCalculator<T> shippingCalculator : calculatorProvider.getShippingPriceCalculators()) {
			shippingCalculator.prepareEvaluation(sequences);
		}

		final ScheduledSequences scheduledSequences = scheduler.schedule(sequences, false);
		
		return scheduledSequences != null;
	}

	final LinkedHashSet<IResource> lastAffectedResources = new LinkedHashSet<IResource>();

	@Override
	public boolean evaluate(final ISequences<T> sequences, final Collection<IResource> affectedResources) {
		for (final IShippingPriceCalculator<T> shippingCalculator : calculatorProvider.getShippingPriceCalculators()) {
			shippingCalculator.prepareEvaluation(sequences);
		}

		// we do this stuff with lastAffectedResources because each evaluation we will definitely need to reschedule:
		// (a) resources that are changed by this move and (b) resources which were changed by last move which got rejected and so need recomputing again

		lastAffectedResources.addAll(affectedResources);
		try {
			final ScheduledSequences scheduledSequences = scheduler.schedule(sequences, false);
			
			return scheduledSequences != null;
		} finally {
			lastAffectedResources.clear();
			lastAffectedResources.addAll(affectedResources);
		}
		// final ScheduledSequences sequence = scheduler.schedule(sequences, false);

		// return planIterator.iterateSchedulerComponents(components,
		// scheduler.schedule(sequences, false));
	}

	@Override
	public Collection<IFitnessComponent<T>> getFitnessComponents() {
		return new ArrayList<IFitnessComponent<T>>(allComponents);
	}

	@Override
	public void init(final IOptimisationData<T> data) {
		scheduler = schedulerFactory.createScheduler(data, schedulerComponents, allocationComponents);

		calculatorProvider = data.getDataComponentProvider(SchedulerConstants.DCP_calculatorProvider, ICalculatorProvider.class);

		// Notify fitness components that a new optimisation is beginning
		for (final ICargoFitnessComponent<T> c : allComponents) {
			c.init(data);
		}
	}

	@Override
	public void dispose() {

		for (final ICargoFitnessComponent<T> c : allComponents) {
			c.dispose();
		}
		allComponents.clear();
		schedulerComponents.clear();
		allocationComponents.clear();
		scheduler.dispose();
	}

	public List<ICargoFitnessComponent<T>> getCargoSchedulerFitnessComponent() {
		return allComponents;
	}

	public void setSchedulerFactory(ISchedulerFactory<T> schedulerFactory) {
		this.schedulerFactory = schedulerFactory;
	}

	@Override
	public void annotate(final ISequences<T> sequences, final IAnnotatedSolution<T> solution, final boolean forExport) {
		lastAffectedResources.clear();
		lastAffectedResources.addAll(sequences.getResources());

		for (final IShippingPriceCalculator<T> shippingCalculator : calculatorProvider.getShippingPriceCalculators()) {
			shippingCalculator.prepareEvaluation(sequences);
		}

		@SuppressWarnings("unchecked")
		final IPortSlotProvider<T> portSlotProvider = solution.getContext().getOptimisationData().getDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, IPortSlotProvider.class);

		// re-evaluate everything
		final ScheduledSequences schedule = scheduler.schedule(sequences, forExport);

		// Setting these annotations here is a bit untidy, but it will do for now.
		solution.setGeneralAnnotation(SchedulerConstants.G_AI_allocations, schedule.getAllocations());

		final IAnnotations<T> elementAnnotations = solution.getElementAnnotations();

		// now add some more data for each load slot
		for (final IAllocationAnnotation annotation : schedule.getAllocations()) {
			final T loadElement = portSlotProvider.getElement(annotation.getLoadSlot());
			final T dischargeElement = portSlotProvider.getElement(annotation.getDischargeSlot());

			elementAnnotations.setAnnotation(loadElement, SchedulerConstants.AI_volumeAllocationInfo, annotation);
			elementAnnotations.setAnnotation(dischargeElement, SchedulerConstants.AI_volumeAllocationInfo, annotation);
		}

		// Do basic voyageplan annotation
		final VoyagePlanAnnotator<T> annotator = new VoyagePlanAnnotator<T>();


		annotator.setPortSlotProvider(portSlotProvider);

		for (final ScheduledSequence scheduledSequence : schedule) {
			final IResource resource = scheduledSequence.getResource();
			final ISequence<T> sequence = sequences.getSequence(resource);

			if (sequence.size() > 0) {
				annotator.annotateFromScheduledSequence(scheduledSequence, solution);
			}
		}

		// set up per-route fitness map, which components can put their fitness
		// in
		{
			final Map<IResource, Map<String, Long>> fitnessPerRoute = new HashMap<IResource, Map<String, Long>>();
			for (final IResource resource : solution.getSequences().getResources()) {
				fitnessPerRoute.put(resource, new HashMap<String, Long>());
			}

			solution.setGeneralAnnotation(SchedulerConstants.G_AI_fitnessPerRoute, fitnessPerRoute);
		}

		// Allow components to do any extra annotations
		planIterator.annotateSchedulerComponents(schedulerComponents, schedule, solution);

		for (final ICargoAllocationFitnessComponent<T> component : allocationComponents) {
			component.annotate(schedule, solution);
		}
	}
}
