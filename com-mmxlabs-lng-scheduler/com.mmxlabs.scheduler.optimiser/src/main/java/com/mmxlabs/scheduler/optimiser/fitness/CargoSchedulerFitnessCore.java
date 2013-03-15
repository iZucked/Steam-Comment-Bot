/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.CapacityComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.CharterCostFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.CostComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.LatenessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.PortCostFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.ProfitAndLossAllocationComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.RouteCostFitnessComponent;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProvider;
import com.mmxlabs.scheduler.optimiser.schedule.ScheduleCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

/**
 * {@link IFitnessCore} which schedules {@link ISequences} objects using an {@link ISequenceScheduler}. Various {@link IFitnessComponent}s implementing {@link ICargoSchedulerFitnessComponent}
 * calculate fitnesses on the scheduled {@link ISequences}.
 * 
 * @author Simon Goodall
 * 
 */
public final class CargoSchedulerFitnessCore implements IFitnessCore {

	private final List<ICargoSchedulerFitnessComponent> schedulerComponents;
	private final List<ICargoFitnessComponent> allComponents;
	private ISequenceScheduler scheduler;

	@Inject
	private ICalculatorProvider calculatorProvider;
	@Inject
	private ISchedulerFactory schedulerFactory;

	@Inject
	private ScheduleCalculator scheduleCalculator;

	public CargoSchedulerFitnessCore() {
		allComponents = new ArrayList<ICargoFitnessComponent>();
		schedulerComponents = new ArrayList<ICargoSchedulerFitnessComponent>(8);
		schedulerComponents.add(new LatenessComponent(CargoSchedulerFitnessCoreFactory.LATENESS_COMPONENT_NAME, SchedulerConstants.DCP_startEndRequirementProvider, this));

		schedulerComponents.add(new CapacityComponent(CargoSchedulerFitnessCoreFactory.CAPACITY_COMPONENT_NAME, this));

		schedulerComponents.add(new CostComponent(CargoSchedulerFitnessCoreFactory.COST_LNG_COMPONENT_NAME, CollectionsUtil.makeArrayList(FuelComponent.NBO, FuelComponent.FBO, FuelComponent.IdleNBO),
				this));

		schedulerComponents.add(new CostComponent(CargoSchedulerFitnessCoreFactory.COST_BASE_COMPONENT_NAME, CollectionsUtil.makeArrayList(FuelComponent.Base, FuelComponent.Base_Supplemental,
				FuelComponent.IdleBase, FuelComponent.PilotLight, FuelComponent.IdlePilotLight), this));

		schedulerComponents.add(new CostComponent(CargoSchedulerFitnessCoreFactory.COST_COOLDOWN_COMPONENT_NAME, CollectionsUtil.makeArrayList(FuelComponent.Cooldown), this));

		schedulerComponents.add(new CharterCostFitnessComponent(CargoSchedulerFitnessCoreFactory.CHARTER_COST_COMPONENT_NAME, SchedulerConstants.DCP_vesselProvider, // not sure if this
				// should be here or
				// somewhere else
				this));

		schedulerComponents.add(new RouteCostFitnessComponent(CargoSchedulerFitnessCoreFactory.ROUTE_PRICE_COMPONENT_NAME, SchedulerConstants.DCP_routePriceProvider,
				SchedulerConstants.DCP_vesselProvider, this));

		// schedulerComponents.add(new CargoAllocatingSchedulerComponent(CargoSchedulerFitnessCoreFactory.CARGO_ALLOCATION_COMPONENT_NAME, SchedulerConstants.DCP_vesselProvider,
		// SchedulerConstants.DCP_totalVolumeLimitProvider, SchedulerConstants.DCP_portSlotsProvider, this));

		// schedulerComponents.add(new CharterOutFitnessComponent(CargoSchedulerFitnessCoreFactory.CHARTER_REVENUE_COMPONENT_NAME, SchedulerConstants.DCP_vesselProvider, this));

		schedulerComponents.add(new PortCostFitnessComponent(CargoSchedulerFitnessCoreFactory.PORT_COST_COMPONENT_NAME, this, SchedulerConstants.DCP_portCostProvider,
				SchedulerConstants.DCP_vesselProvider, SchedulerConstants.DCP_portSlotsProvider));

		schedulerComponents.add(new ProfitAndLossAllocationComponent(CargoSchedulerFitnessCoreFactory.PROFIT_COMPONENT_NAME, this));

		allComponents.addAll(schedulerComponents);
	}

	public CargoSchedulerFitnessCore(final Iterable<ICargoFitnessComponentProvider> externalComponentProviders) {
		this();
		if (externalComponentProviders != null) {
			for (final ICargoFitnessComponentProvider provider : externalComponentProviders) {
				final ICargoFitnessComponent component = provider.createComponent(this);
				allComponents.add(component);
				if (component instanceof ICargoSchedulerFitnessComponent) {
					schedulerComponents.add((ICargoSchedulerFitnessComponent) component);
				}
			}
		}
	}

	@Override
	public void accepted(final ISequences sequences, final Collection<IResource> affectedResources) {
		// we can clear the affected resources from last move, because
		// the scheduler doesn't need to reset its calculations for those resources
		lastAffectedResources.clear();
		for (final ICargoSchedulerFitnessComponent component : schedulerComponents) {
			component.acceptLastEvaluation();
		}
		scheduler.acceptLastSchedule();
	}

	@Override
	public boolean evaluate(final ISequences sequences) {
		for (final ISalesPriceCalculator shippingCalculator : calculatorProvider.getSalesPriceCalculators()) {
			shippingCalculator.prepareEvaluation(sequences);
		}

		final ScheduledSequences scheduledSequences = scheduler.schedule(sequences, false);

		return scheduledSequences != null;
	}

	final LinkedHashSet<IResource> lastAffectedResources = new LinkedHashSet<IResource>();

	@Override
	public boolean evaluate(final ISequences sequences, final Collection<IResource> affectedResources) {
		for (final ISalesPriceCalculator shippingCalculator : calculatorProvider.getSalesPriceCalculators()) {
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
	}

	@Override
	public Collection<IFitnessComponent> getFitnessComponents() {
		return new ArrayList<IFitnessComponent>(allComponents);
	}

	// @Inject
	@Override
	public void init(final IOptimisationData data) {
		scheduler = schedulerFactory.createScheduler(data, schedulerComponents);
		// Notify fitness components that a new optimisation is beginning
		for (final ICargoFitnessComponent c : allComponents) {
			c.init(data);
		}
	}

	@Override
	public void dispose() {

		for (final ICargoFitnessComponent c : allComponents) {
			c.dispose();
		}
		allComponents.clear();
		schedulerComponents.clear();
		scheduler.dispose();
	}

	public List<ICargoFitnessComponent> getCargoSchedulerFitnessComponent() {
		return allComponents;
	}

	@Override
	public void annotate(final ISequences sequences, final IAnnotatedSolution solution, final boolean forExport) {
		lastAffectedResources.clear();
		lastAffectedResources.addAll(sequences.getResources());

		for (final ISalesPriceCalculator shippingCalculator : calculatorProvider.getSalesPriceCalculators()) {
			shippingCalculator.prepareEvaluation(sequences);
		}

		// re-evaluate everything
		final ScheduledSequences schedule = scheduler.schedule(sequences, forExport);

		// Setting these annotations here is a bit untidy, but it will do for now.
		solution.setGeneralAnnotation(SchedulerConstants.G_AI_allocations, schedule.getAllocations());

		// FIXME: This duplicates some of the earlier scheduler run.
		scheduleCalculator.calculateSchedule(sequences, schedule, solution);

		// set up per-route fitness map, which components can put their fitness
		// in
		{
			final Map<IResource, Map<String, Long>> fitnessPerRoute = new HashMap<IResource, Map<String, Long>>();
			for (final IResource resource : solution.getSequences().getResources()) {
				fitnessPerRoute.put(resource, new HashMap<String, Long>());
			}

			solution.setGeneralAnnotation(SchedulerConstants.G_AI_fitnessPerRoute, fitnessPerRoute);
		}
	}
}
