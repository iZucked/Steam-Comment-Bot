/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;

/**
 * 
 * {@link ICargoSchedulerFitnessComponent} implementation to calculate a fitness based on weighted lateness.
 * 
 * @author Alex Churchill
 * 
 */
public final class LatenessEvaluatedStateChecker implements IEvaluatedStateConstraintChecker {

	private final @NonNull String name;

	private long initialPromptLateness = 0;
	private long initialTotalLateness = 0;
	private Set<IPortSlot> lateSlots = null;

	public LatenessEvaluatedStateChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	public @NonNull String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences rawSequences, @NonNull final ISequences fullSequences, @NonNull final IEvaluationState evaluationState) {

		final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
		if (volumeAllocatedSequences == null) {
			return true;
		}

		long promptLateness = 0;
		long totalLateness = 0;

		for (VolumeAllocatedSequence volumeAllocatedSequence : volumeAllocatedSequences) {

			@NonNull
			final Set<@NonNull IPortSlot> lateSlotsSet = volumeAllocatedSequence.getLateSlotsSet();
			boolean newLateness = false;
			for (final IPortSlot lateSlot : lateSlotsSet) {

				if (lateSlots != null && !lateSlots.contains(lateSlot)) {
					newLateness = true;
				}
				final Interval latenessInterval = volumeAllocatedSequence.getLatenessInterval(lateSlot);
				if (latenessInterval != null) {
					final long lateness = volumeAllocatedSequence.getLatenessWithFlex(lateSlot);
					if (latenessInterval == Interval.PROMPT) {
						promptLateness += lateness;
					}
					totalLateness += lateness;
				}
				if (lateSlots != null) {
					if ((promptLateness > initialPromptLateness || totalLateness > initialTotalLateness) || newLateness == true) {
						return false;
					}
				}
			}
		}
		// If this is the first run, then set the initial state
		if (lateSlots == null) {
			lateSlots = new HashSet<>();
			for (VolumeAllocatedSequence volumeAllocatedSequence : volumeAllocatedSequences) {
				lateSlots.addAll(volumeAllocatedSequence.getLateSlotsSet());
			}
			initialPromptLateness = promptLateness;
			initialTotalLateness = totalLateness;

			return true;
		}

		return true;
	}
}
