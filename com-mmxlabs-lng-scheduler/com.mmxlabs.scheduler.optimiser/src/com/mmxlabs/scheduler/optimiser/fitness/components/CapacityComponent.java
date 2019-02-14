/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;

public final class CapacityComponent extends AbstractPerRouteSchedulerFitnessComponent implements IFitnessComponent {

	private static final long THRESHOLD_IN_M3 = OptimiserUnitConvertor.convertToInternalVolume(1000);

	private long sequenceAccumulator = 0;

	public CapacityComponent(@NonNull final String name, @NonNull final CargoSchedulerFitnessCore core) {
		super(name, core);
	}

	@Override
	protected boolean reallyStartSequence(@NonNull final IResource resource) {
		sequenceAccumulator = 0;
		return true;
	}

	@Override
	protected boolean reallyEvaluateObject(@NonNull final Object object, final int time) {
		final VolumeAllocatedSequences volumeAllocatedSequences = profitAndLossSequences.getVolumeAllocatedSequences();
		if (object instanceof PortDetails) {
			final PortDetails detail = (PortDetails) object;

			@NonNull
			final IPortSlot portSlot = detail.getOptions().getPortSlot();
			final VolumeAllocatedSequence volumeAllocatedSequence = volumeAllocatedSequences.getScheduledSequence(portSlot);
			if (volumeAllocatedSequence != null) {
				final List<@NonNull CapacityViolationType> capacityViolations = volumeAllocatedSequence.getCapacityViolations(portSlot);
				for (final CapacityViolationType cvt : capacityViolations) {
					if (cvt == CapacityViolationType.MIN_LOAD //
							|| cvt == CapacityViolationType.MIN_DISCHARGE //
							|| cvt == CapacityViolationType.MAX_LOAD //
							|| cvt == CapacityViolationType.MAX_DISCHARGE) {

						long volume = volumeAllocatedSequence.getCapacityViolationVolume(cvt, portSlot);

						// Violation may in in mmbtu, so convert to m3 equivalent
						boolean volumeIsM3;
						if (cvt == CapacityViolationType.MIN_LOAD || cvt == CapacityViolationType.MAX_LOAD) {
							volumeIsM3 = ((ILoadOption) portSlot).isVolumeSetInM3();
						} else {
							volumeIsM3 = ((IDischargeOption) portSlot).isVolumeSetInM3();
						}
						if (!volumeIsM3) {
							volume = Calculator.convertMMBTuToM3(volume, volumeAllocatedSequence.getAllocationAnnotation(portSlot).getSlotCargoCV(portSlot));
						}

						// Report only if violation is larger than 1000m3
						if (Math.abs(volume) > THRESHOLD_IN_M3) {
							sequenceAccumulator += Math.abs(volume);
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	protected long endSequenceAndGetCost() {
		return sequenceAccumulator;
	}

}
