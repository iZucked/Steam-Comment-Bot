/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.pnl;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoAllocationFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IEntityProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Basic group P&L calculator.
 * 
 * TODO: should this be divided differently?
 * 
 * Maybe in the new design we want different P&L bits in different places, specifically charter out revenue.
 * 
 * @author hinton
 * 
 */
public class ProfitAndLossAllocationComponent implements ICargoAllocationFitnessComponent {
	private final CargoSchedulerFitnessCore core;
	private final String name;

	@Inject
	private IEntityProvider entityProvider;

	private long lastEvaluation, lastAcceptance;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IEntityValueCalculator entityValueCalculator;

	private long calibrationZeroLine = Long.MIN_VALUE;

	/**
	 * @since 2.0
	 */
	public ProfitAndLossAllocationComponent(final String profitComponentName, final CargoSchedulerFitnessCore cargoSchedulerFitnessCore) {
		this.name = profitComponentName;
		this.core = cargoSchedulerFitnessCore;
	}

	@Override
	public void init(final IOptimisationData data) {

	}

	@Override
	public void rejectLastEvaluation() {
	}

	@Override
	public void acceptLastEvaluation() {
		lastAcceptance = lastEvaluation;
	}

	@Override
	public void dispose() {
		entityProvider = null;
		vesselProvider = null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public long getFitness() {
		return lastEvaluation;
	}

	@Override
	public IFitnessCore getFitnessCore() {
		return core;
	}

	@Override
	public long evaluate(final ScheduledSequences solution, final Map<VoyagePlan, IAllocationAnnotation> allocations) {
		return evaluateAndMaybeAnnotate(solution, allocations, null);
	}

	private long evaluateAndMaybeAnnotate(final ScheduledSequences solution, final Map<VoyagePlan, IAllocationAnnotation> allocations, final IAnnotatedSolution annotatedSolution) {
		if (entityProvider == null) {
			return 0;
		}
		long accumulator = 0;

		for (final ScheduledSequence sequence : solution) {
			final IVessel vessel = vesselProvider.getVessel(sequence.getResource());

			int time = sequence.getStartTime();
			for (final VoyagePlan plan : sequence.getVoyagePlans()) {
				boolean cargo = false;
				if (plan.getSequence().length >= 3) {

					PortDetails firstDetails = (PortDetails) plan.getSequence()[0];
					PortDetails lastDetails = (PortDetails) plan.getSequence()[2];
					final IAllocationAnnotation currentAllocation = allocations.get(firstDetails);
					if ((currentAllocation != null) && ((firstDetails.getOptions().getPortSlot() == currentAllocation.getLoadOption()) && (lastDetails.getOptions().getPortSlot() == currentAllocation.getDischargeOption()))) {
						cargo = true;
						final long cargoGroupValue = entityValueCalculator.evaluate(plan, currentAllocation, vessel, sequence.getStartTime(), annotatedSolution);
						accumulator += cargoGroupValue;
					} else if ((vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE) && plan.getSequence().length == 4) {
						firstDetails = (PortDetails) plan.getSequence()[1];
						lastDetails = (PortDetails) plan.getSequence()[2];
						if ((currentAllocation != null) && ((firstDetails.getOptions().getPortSlot() == currentAllocation.getLoadOption()) && (lastDetails.getOptions().getPortSlot() == currentAllocation.getDischargeOption()))) {
							cargo = true;
							// TODO: Perhaps use the real slot time rather than always load?
							// TODO: Does it matter really?
							final long cargoGroupValue = entityValueCalculator.evaluate(plan, currentAllocation, vessel, currentAllocation.getLoadTime(), annotatedSolution);
							accumulator += cargoGroupValue;
						}
					}
				}

				if (!cargo) {
					final long otherGroupValue = entityValueCalculator.evaluate(plan, vessel, time, sequence.getStartTime(), annotatedSolution);
					accumulator += otherGroupValue;
				}
				time += getPlanDuration(plan);
			}
		}

		if (calibrationZeroLine == Long.MIN_VALUE) {
			calibrationZeroLine = accumulator;
		}

		lastEvaluation = (-accumulator) / Calculator.ScaleFactor;
		return lastEvaluation;
	}

	@Override
	public void annotate(final ScheduledSequences solution, final IAnnotatedSolution annotatedSolution) {
		if (entityProvider == null) {
			return;
		}
		evaluateAndMaybeAnnotate(solution, solution.getAllocations(), annotatedSolution);
	}

	private int getPlanDuration(final VoyagePlan plan) {
		return getPartialPlanDuration(plan, 0);
	}

	private int getPartialPlanDuration(final VoyagePlan plan, final int skip) {
		int planDuration = 0;
		final Object[] sequence = plan.getSequence();
		final int k = sequence.length - skip;
		for (int i = 0; i < k; i++) {
			final Object o = sequence[i];
			planDuration += (o instanceof VoyageDetails) ? (((VoyageDetails) o).getIdleTime() + ((VoyageDetails) o).getTravelTime()) : ((PortDetails) o).getOptions().getVisitDuration();
		}
		return planDuration;
	}
}
