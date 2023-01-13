/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;

/**
 * 
 * {@link ICargoSchedulerFitnessComponent} implementation to calculate a fitness
 * based on idle time violations.
 * 
 * @author Alex Churchill
 * 
 */
public final class IdleTimeComponent extends AbstractPerRouteSchedulerFitnessComponent implements IFitnessComponent {

	private long sequenceTotalExcessIdleCost = 0;
	private IResource resource;

	public IdleTimeComponent(@NonNull final String name, @NonNull final CargoSchedulerFitnessCore core) {
		super(name, core);
	}

	@Override
	public void startEvaluation(@NonNull ProfitAndLossSequences scheduledSequences) {
		sequenceTotalExcessIdleCost = 0;
		super.startEvaluation(scheduledSequences);
	}

	@Override
	protected boolean reallyStartSequence(@NonNull final IResource resource) {
		this.resource = resource;
		sequenceTotalExcessIdleCost = 0;
		return true;
	}

	@Override
	protected boolean reallyEvaluateObject(@NonNull final Object object, final int time) {
		if (object instanceof PortDetails detail) {

			final @NonNull IPortSlot portSlot = detail.getOptions().getPortSlot();

			final VolumeAllocatedSequence volumeAllocatedSequence = profitAndLossSequences.getScheduledSequenceForResource(resource);
			if (volumeAllocatedSequence != null) {
				VoyagePlanRecord vpr = volumeAllocatedSequence.getVoyagePlanRecord(portSlot);
				sequenceTotalExcessIdleCost += vpr.getIdleWeightedCost(portSlot);
			}
		}
		return true;
	}

	@Override
	protected long endSequenceAndGetCost() {

		this.resource = null;
		return sequenceTotalExcessIdleCost;
	}
}
