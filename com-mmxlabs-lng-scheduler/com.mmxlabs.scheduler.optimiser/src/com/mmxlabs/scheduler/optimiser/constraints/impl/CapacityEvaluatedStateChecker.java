/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;

/**
 * 
 * {@link ICargoSchedulerFitnessComponent} implementation to calculate a fitness based on capacity violations.
 * 
 * @author Simon Goodall
 * 
 */
public final class CapacityEvaluatedStateChecker implements IEvaluatedStateConstraintChecker {

	private long initialViolations = 0;
	private final @NonNull String name;
	private boolean initialised;

	public CapacityEvaluatedStateChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	public boolean checkConstraints(@NonNull ISequences rawSequences, @NonNull ISequences fullSequences, @NonNull IEvaluationState evaluationState) {

		final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);

		if (volumeAllocatedSequences == null) {
			return true;
		}

		long totalViolations = volumeAllocatedSequences.stream()//
				.mapToLong(seq -> seq.getSequenceSlots().stream() //
						.mapToLong(slot -> seq.getCapacityViolationCount(slot)).sum())
				.sum();

		if (!initialised) {
			initialViolations = totalViolations;
			initialised = true;
			return true;
		}

		return totalViolations <= initialViolations;
	}

	@Override
	public @NonNull String getName() {
		return name;
	}
}
