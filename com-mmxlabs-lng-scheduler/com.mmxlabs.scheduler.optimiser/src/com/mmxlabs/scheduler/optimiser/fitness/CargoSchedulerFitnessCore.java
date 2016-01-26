/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Injector;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.fitness.components.CapacityComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.CharterCostFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.CostComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.LatenessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.PortCostFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.ProfitAndLossAllocationComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.RouteCostFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.ScheduleFitnessEvaluator;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

/**
 * {@link IFitnessCore} which schedules {@link ISequences} objects using an {@link ISequenceScheduler}. Various {@link IFitnessComponent}s implementing {@link ICargoSchedulerFitnessComponent}
 * calculate fitnesses on the scheduled {@link ISequences}.
 * 
 * @author Simon Goodall
 * 
 */
public final class CargoSchedulerFitnessCore implements IFitnessCore {

	private final List<ICargoSchedulerFitnessComponent> schedulerComponents = new ArrayList<>(8);
	private final List<ICargoFitnessComponent> allComponents = new ArrayList<>();

	private final ScheduleFitnessEvaluator evaluator = new ScheduleFitnessEvaluator();

	@Inject
	private Injector injector;

	@Inject
	public void injectComponents() {
		for (final Object obj : allComponents) {
			injector.injectMembers(obj);
		}
	}

	private void init() {
		final List<ICargoSchedulerFitnessComponent> localComponents = new ArrayList<>(8);
		localComponents.add(new LatenessComponent(CargoSchedulerFitnessCoreFactory.LATENESS_COMPONENT_NAME, this));

		localComponents.add(new CapacityComponent(CargoSchedulerFitnessCoreFactory.CAPACITY_COMPONENT_NAME, this));

		localComponents.add(new CostComponent(CargoSchedulerFitnessCoreFactory.COST_LNG_COMPONENT_NAME, CollectionsUtil.makeArrayList(FuelComponent.NBO, FuelComponent.FBO, FuelComponent.IdleNBO),
				this));

		localComponents.add(new CostComponent(CargoSchedulerFitnessCoreFactory.COST_BASE_COMPONENT_NAME, CollectionsUtil.makeArrayList(FuelComponent.Base, FuelComponent.Base_Supplemental,
				FuelComponent.IdleBase, FuelComponent.PilotLight, FuelComponent.IdlePilotLight), this));

		localComponents.add(new CostComponent(CargoSchedulerFitnessCoreFactory.COST_COOLDOWN_COMPONENT_NAME, CollectionsUtil.makeArrayList(FuelComponent.Cooldown), this));

		localComponents.add(new CharterCostFitnessComponent(CargoSchedulerFitnessCoreFactory.CHARTER_COST_COMPONENT_NAME, this));

		localComponents.add(new RouteCostFitnessComponent(CargoSchedulerFitnessCoreFactory.ROUTE_PRICE_COMPONENT_NAME, this));

		// schedulerComponents.add(new CargoAllocatingSchedulerComponent(CargoSchedulerFitnessCoreFactory.CARGO_ALLOCATION_COMPONENT_NAME, SchedulerConstants.DCP_vesselProvider,
		// SchedulerConstants.DCP_totalVolumeLimitProvider, SchedulerConstants.DCP_portSlotsProvider, this));

		// schedulerComponents.add(new CharterOutFitnessComponent(CargoSchedulerFitnessCoreFactory.CHARTER_REVENUE_COMPONENT_NAME, SchedulerConstants.DCP_vesselProvider, this));

		localComponents.add(new PortCostFitnessComponent(CargoSchedulerFitnessCoreFactory.PORT_COST_COMPONENT_NAME, this));

		localComponents.add(new ProfitAndLossAllocationComponent(CargoSchedulerFitnessCoreFactory.PROFIT_COMPONENT_NAME, this));

		schedulerComponents.addAll(localComponents);
		allComponents.addAll(localComponents);
	}

	public CargoSchedulerFitnessCore(final Iterable<ICargoFitnessComponentProvider> externalComponentProviders) {
		init();
		if (externalComponentProviders != null) {
			for (final ICargoFitnessComponentProvider provider : externalComponentProviders) {
				final ICargoFitnessComponent component = provider.createComponent(this);
				allComponents.add(component);
				if (component instanceof ICargoSchedulerFitnessComponent) {
					schedulerComponents.add((ICargoSchedulerFitnessComponent) component);
				}
			}
		}
		evaluator.setFitnessComponents(schedulerComponents);
	}

	@Override
	public void accepted(final ISequences sequences, final Collection<IResource> affectedResources) {
		// we can clear the affected resources from last move, because
		// the scheduler doesn't need to reset its calculations for those resources
		for (final ICargoSchedulerFitnessComponent component : schedulerComponents) {
			component.acceptLastEvaluation();
		}
	}

	@Override
	public boolean evaluate(final ISequences sequences, @NonNull final IEvaluationState evaluationState) {

		final ScheduledSequences scheduledSequences = evaluationState.getData(SchedulerEvaluationProcess.SCHEDULED_SEQUENCES, ScheduledSequences.class);
		if (scheduledSequences != null) {
			return evaluator.evaluateSchedule(sequences, scheduledSequences) != Long.MAX_VALUE;
		}
		return false;
	}

	@Override
	public boolean evaluate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @Nullable final Collection<IResource> affectedResources) {
		final ScheduledSequences scheduledSequences = evaluationState.getData(SchedulerEvaluationProcess.SCHEDULED_SEQUENCES, ScheduledSequences.class);
		if (scheduledSequences != null) {
			return evaluator.evaluateSchedule(sequences, scheduledSequences) != Long.MAX_VALUE;
		}

		return false;
	}

	@Override
	public Collection<IFitnessComponent> getFitnessComponents() {
		return new ArrayList<IFitnessComponent>(allComponents);
	}

	@Override
	public void init(final IOptimisationData data) {
		// scheduler = schedulerFactory.createScheduler(data, schedulerComponents);
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
		// scheduler = null;
	}

	public List<ICargoFitnessComponent> getCargoSchedulerFitnessComponent() {
		return allComponents;
	}

	@Override
	public void annotate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @NonNull final IAnnotatedSolution solution) {
		// set up per-route fitness map, which components can put their fitness
		// in
		{
			final Map<IResource, Map<String, Long>> fitnessPerRoute = new HashMap<IResource, Map<String, Long>>();
			for (final IResource resource : solution.getFullSequences().getResources()) {
				fitnessPerRoute.put(resource, new HashMap<String, Long>());
			}

			solution.setGeneralAnnotation(SchedulerConstants.G_AI_fitnessPerRoute, fitnessPerRoute);
//			solution.setGeneralAnnotation(SchedulerConstants.G_AI_scheduledSequence, evaluationState.getData(SchedulerEvaluationProcess.SCHEDULED_SEQUENCES, ScheduledSequences.class));
		}
	}
}
