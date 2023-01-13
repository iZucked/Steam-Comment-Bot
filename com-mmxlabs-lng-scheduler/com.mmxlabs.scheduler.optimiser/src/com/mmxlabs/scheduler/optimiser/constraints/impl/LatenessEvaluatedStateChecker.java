/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.scheduler.optimiser.components.IMaintenanceVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord;
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord.LatenessRecord;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;

/**
 * 
 * {@link IEvaluatedStateConstraintChecker} implementation to calculate a fitness
 * based on weighted lateness.
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

		final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
		if (volumeAllocatedSequences == null) {
			return true;
		}

		long promptLateness = 0;
		long totalLateness = 0;

		for (final VolumeAllocatedSequence volumeAllocatedSequence : volumeAllocatedSequences) {
			IPortSlot lastPortSlot = null;
			for (final VoyagePlanRecord vpr : volumeAllocatedSequence.getVoyagePlanRecords()) {
				lastPortSlot = vpr.getPortTimesRecord().getFirstSlot();
				// Unwrap the original slot from the generated slot
				if (lastPortSlot instanceof final IMaintenanceVesselEventPortSlot e) {
					lastPortSlot = e.getFormerPortSlot();
				}

				final Set<@NonNull IPortSlot> lateSlotsSet = vpr.getLateSlotsSet();
				boolean newLateness = false;
				for (final IPortSlot lateSlot : lateSlotsSet) {
					IPortSlot lateSlotForSet = lateSlot;
					// Unwrap the original slot from the generated slot. The we need the original
					// slot for the lateSlots check, but the generated one for the VPR calls.
					if (lateSlot instanceof final IMaintenanceVesselEventPortSlot e) {
						lateSlotForSet = e.getFormerPortSlot();
					}
					if (lateSlots != null && !lateSlots.contains(lateSlotForSet)) {
						newLateness = true;
					}
					final Interval latenessInterval = vpr.getLatenessInterval(lateSlot);
					if (latenessInterval != null) {
						final long lateness = vpr.getLatenessWithFlex(lateSlot);
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

			// Check end event
			boolean newLateness = false;
			final LatenessRecord maxDurationLatenessRecord = volumeAllocatedSequence.getMaxDurationLatenessRecord();
			if (lastPortSlot != null && maxDurationLatenessRecord != null) {
				if (lateSlots != null && !lateSlots.contains(lastPortSlot)) {
					newLateness = true;
				}
				final Interval latenessInterval = maxDurationLatenessRecord.interval;
				if (latenessInterval != null) {
					final long lateness = maxDurationLatenessRecord.latenessWithFlex;
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
			for (final VolumeAllocatedSequence volumeAllocatedSequence : volumeAllocatedSequences) {
				IPortSlot lastPortSlot = null;
				for (final VoyagePlanRecord vpr : volumeAllocatedSequence.getVoyagePlanRecords()) {
					lateSlots.addAll(vpr.getLateSlotsSet());
					lastPortSlot = vpr.getPortTimesRecord().getFirstSlot();
				}
				final LatenessRecord maxDurationLatenessRecord = volumeAllocatedSequence.getMaxDurationLatenessRecord();
				if (lastPortSlot != null && maxDurationLatenessRecord != null) {
					lateSlots.add(lastPortSlot);
				}
			}
			initialPromptLateness = promptLateness;
			initialTotalLateness = totalLateness;

			// Make sure we have unwrapped the original slot
			for (final var ps : new LinkedList<>(lateSlots)) {
				if (ps instanceof final IMaintenanceVesselEventPortSlot e) {
					lateSlots.remove(ps);
					lateSlots.add(e.getFormerPortSlot());
				}
			}

			return true;
		}

		return true;
	}

	@Override
	public void acceptSequences(@NonNull final ISequences rawSequences, @NonNull final ISequences fullSequences, @NonNull final IEvaluationState evaluationState) {
		lateSlots = null;
		checkConstraints(rawSequences, fullSequences, evaluationState);
	}
}
