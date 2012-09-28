/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.components;

import java.util.Collection;
import java.util.Iterator;

import javax.inject.Inject;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.contracts.IEntityValueCalculator;
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
	private final String entityProviderKey;
	private final String slotProviderKey;
	private final String vesselProviderKey;
	private final String name;

	@Inject
	private IEntityProvider entityProvider;
	// private IEntity shippingEntity;

	private long lastEvaluation, lastAcceptance;
	@Inject
	private IVesselProvider vesselProvider;
	// private IPortSlotProvider slotProvider;
	@Inject
	private IEntityValueCalculator entityValueCalculator;

	private long calibrationZeroLine = Long.MIN_VALUE;

	public ProfitAndLossAllocationComponent(final String profitComponentName, final String entityProviderKey, final String vesselProviderKey, final String slotProviderKey,
			final CargoSchedulerFitnessCore cargoSchedulerFitnessCore) {
		this.name = profitComponentName;
		this.entityProviderKey = entityProviderKey;
		this.vesselProviderKey = vesselProviderKey;
		this.slotProviderKey = slotProviderKey;
		this.core = cargoSchedulerFitnessCore;
	}

	@Override
	public void init(final IOptimisationData data) {
		// this.entityValueCalculator = new DefaultEntityValueCalculator(entityProviderKey, vesselProviderKey, slotProviderKey);
		// this.entityValueCalculator.init(data);
		//
		// this.entityProvider = data.getDataComponentProvider(entityProviderKey, IEntityProvider.class);
		// this.vesselProvider = data.getDataComponentProvider(vesselProviderKey, IVesselProvider.class);
		// this.slotProvider = data.getDataComponentProvider(slotProviderKey, IPortSlotProvider.class);
		// if (entityProvider != null) {
		// this.shippingEntity = entityProvider.getShippingEntity();
		// }
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
	public long evaluate(final ScheduledSequences solution, final Collection<IAllocationAnnotation> allocations) {
		return evaluateAndMaybeAnnotate(solution, allocations, null);
	}

	private long evaluateAndMaybeAnnotate(final ScheduledSequences solution, final Collection<IAllocationAnnotation> allocations, final IAnnotatedSolution annotatedSolution) {
		if (entityProvider == null) {
			return 0;
		}
		final Iterator<IAllocationAnnotation> allocationIterator = allocations.iterator();
		IAllocationAnnotation currentAllocation = null;
		if (allocationIterator.hasNext()) {
			currentAllocation = allocationIterator.next();
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
					if ((currentAllocation != null) && ((firstDetails.getPortSlot() == currentAllocation.getLoadOption()) && (lastDetails.getPortSlot() == currentAllocation.getDischargeOption()))) {
						cargo = true;
						final long cargoGroupValue = entityValueCalculator.evaluate(plan, currentAllocation, vessel, sequence.getStartTime(), annotatedSolution);
						accumulator += cargoGroupValue;
						if (allocationIterator.hasNext()) {
							currentAllocation = allocationIterator.next();
						} else {
							currentAllocation = null;
						}
					} else if ((vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE) && plan.getSequence().length == 4) {
						firstDetails = (PortDetails) plan.getSequence()[1];
						lastDetails = (PortDetails) plan.getSequence()[2];
						if ((currentAllocation != null) && ((firstDetails.getPortSlot() == currentAllocation.getLoadOption()) && (lastDetails.getPortSlot() == currentAllocation.getDischargeOption()))) {
							cargo = true;
							// TODO: Perhaps use the real slot time rather than always load?
							// TODO: Does it matter really?
							final long cargoGroupValue = entityValueCalculator.evaluate(plan, currentAllocation, vessel, currentAllocation.getLoadTime(), annotatedSolution);
							accumulator += cargoGroupValue;
							if (allocationIterator.hasNext()) {
								currentAllocation = allocationIterator.next();
							} else {
								currentAllocation = null;
							}
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
			planDuration += (o instanceof VoyageDetails) ? (((VoyageDetails) o).getIdleTime() + ((VoyageDetails) o).getTravelTime()) : ((PortDetails) o).getVisitDuration();
		}
		return planDuration;
	}
}
