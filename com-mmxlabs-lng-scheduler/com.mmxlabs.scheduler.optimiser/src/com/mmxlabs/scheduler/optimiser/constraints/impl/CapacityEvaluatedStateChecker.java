/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import com.mmxlabs.scheduler.optimiser.schedule.CapacityViolationChecker;
import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;

/**
 * 
 * {@link ICargoSchedulerFitnessComponent} implementation to calculate a fitness based on capacity violations.
 * 
 * @author Simon Goodall
 * 
 */
public final class CapacityEvaluatedStateChecker implements IEvaluatedStateConstraintChecker {

	private long initialSoftTotalViolations = 0;
	private List<CapacityViolationType> initialTriggeredViolations;

	public List<CapacityViolationType> allViolations;
	public List<CapacityViolationType> initialAllViolations;

	private int totalSoftViolations = 0;
	private final @NonNull String name;
	private boolean initialised;
	private Set<IPortSlot> initialViolatedSlots = null;
	private Set<IPortSlot> currentViolatedSlots;
	private List<CapacityViolationType> triggeredViolations;

	private int flexibleSoftViolations = 0;

	public CapacityEvaluatedStateChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences rawSequences, @NonNull final ISequences fullSequences, @NonNull final IEvaluationState evaluationState) {

		final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);

		if (volumeAllocatedSequences == null) {
			return true;
		}

		// State does not need to be kept, but needed for unit testing
		totalSoftViolations = 0;
		currentViolatedSlots = new HashSet<>();
		triggeredViolations = new ArrayList<>();
		allViolations = new ArrayList<>();

		for (final VolumeAllocatedSequence volumeAllocatedSequence : volumeAllocatedSequences) {
			final List<@NonNull IPortSlot> slots = volumeAllocatedSequence.getSequenceSlots();

			for (final IPortSlot slot : slots) {
				final List<@NonNull CapacityViolationType> violations = volumeAllocatedSequence.getCapacityViolations(slot);

				for (@NonNull
				final CapacityViolationType violation : violations) {
					if (CapacityViolationChecker.isHardViolation(violation)) {
						triggeredViolations.add(violation);
						currentViolatedSlots.add(slot);
						allViolations.add(violation);
					} else {
						allViolations.add(violation);
						++totalSoftViolations;
					}
				}
			}
		}

		if (initialViolatedSlots != null) {
			// Take flex into account before checks
			if (flexibleSoftViolations == Integer.MAX_VALUE) {
				totalSoftViolations = 0;
			} else {
				totalSoftViolations = Math.max(0, totalSoftViolations - flexibleSoftViolations);
			}

			currentViolatedSlots.removeAll(initialViolatedSlots);
			if (currentViolatedSlots.size() > 0 || totalSoftViolations > initialSoftTotalViolations) {
				return false;
			}
		} // If this is the first run, then set the initial state
		else {
			initialViolatedSlots = currentViolatedSlots;
			initialSoftTotalViolations = totalSoftViolations;
			initialAllViolations = allViolations;
			initialised = true;
			return true;
		}

		return true;
	}

	@Override
	public @NonNull String getName() {
		return name;
	}

	public List<CapacityViolationType> getTriggeredViolations() {
		return triggeredViolations;
	}

	public Set<IPortSlot> getCurrentViolatedSlots() {
		return currentViolatedSlots;
	}

	public long getCurrentSoftViolations() {
		return totalSoftViolations;
	}

	public long getInitialSoftViolations() {
		return initialSoftTotalViolations;
	}

	public boolean isInitialised() {
		return initialised;
	}

	public Set<IPortSlot> getInitialViolatedSlots() {
		return initialViolatedSlots;
	}

	public List<CapacityViolationType> getInitialTriggeredViolations() {
		return initialTriggeredViolations;
	}

	@Override
	public void acceptSequences(@NonNull ISequences rawSequences, @NonNull ISequences fullSequences, @NonNull IEvaluationState evaluationState) {
		initialViolatedSlots = null;
		checkConstraints(rawSequences, fullSequences, evaluationState);
	}

	public int getFlexibleSoftViolations() {
		return flexibleSoftViolations;
	}

	/**
	 * Set the number of flexible slot violations permitted. Integer.MAX_VALUE allows any soft violations. 0 disallows any soft violation (over and above the initial level)
	 * 
	 * @param flexibleSoftViolations
	 */

	public void setFlexibleSoftViolations(int flexibleSoftViolations) {
		this.flexibleSoftViolations = flexibleSoftViolations;
	}
}
