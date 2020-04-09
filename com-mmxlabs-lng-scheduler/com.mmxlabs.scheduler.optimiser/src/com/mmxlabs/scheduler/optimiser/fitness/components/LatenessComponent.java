/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord;
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord.LatenessRecord;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;

/**
 * 
 * {@link ICargoSchedulerFitnessComponent} implementation to calculate a fitness
 * based on weighted lateness.
 * 
 * @author Alex Churchill
 * 
 */
public final class LatenessComponent extends AbstractPerRouteSchedulerFitnessComponent implements IFitnessComponent {

	private long sequenceAccumulator = 0;
	private IResource resource;

	public LatenessComponent(@NonNull final String name, @NonNull final CargoSchedulerFitnessCore core) {
		super(name, core);
	}

	@Override
	protected boolean reallyStartSequence(@NonNull final IResource resource) {
		this.resource = resource;
		sequenceAccumulator = 0;
		return true;
	}

	@Override
	protected boolean reallyEvaluateObject(@NonNull final Object object, final int time) {
		if (object instanceof PortDetails) {

			final PortDetails detail = (PortDetails) object;

			@NonNull
			final IPortSlot portSlot = detail.getOptions().getPortSlot();

			final VolumeAllocatedSequence volumeAllocatedSequence = profitAndLossSequences.getScheduledSequenceForResource(resource);
			if (volumeAllocatedSequence != null) {
				VoyagePlanRecord vpr = volumeAllocatedSequence.getVoyagePlanRecord(portSlot);
				if (vpr != null) {
					sequenceAccumulator += vpr.getWeightedLatenessCost(portSlot);
				}

				LatenessRecord record = volumeAllocatedSequence.getMaxDurationLatenessRecord();
				if (record != null) {
					sequenceAccumulator += record.weightedLateness;
				}
			}
		}
		return true;
	}

	@Override
	protected long endSequenceAndGetCost() {

		resource = null;
		return sequenceAccumulator;
	}
}
