/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
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

		final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);

		if (volumeAllocatedSequences == null) {
			return true;
		}
		return checkConstraints(volumeAllocatedSequences);
	}

	public boolean checkConstraints(@NonNull final ProfitAndLossSequences volumeAllocatedSequences) {

		// State does not need to be kept, but needed for unit testing
		totalSoftViolations = 0;
		currentViolatedSlots = new HashSet<>();
		triggeredViolations = new ArrayList<>();

		for (final VolumeAllocatedSequence volumeAllocatedSequence : volumeAllocatedSequences) {

			for (final VoyagePlanRecord vpr : volumeAllocatedSequence.getVoyagePlanRecords()) {
				for (final IPortSlot slot : vpr.getPortTimesRecord().getSlots()) {
					final List<CapacityViolationType> violations = vpr.getCapacityViolations(slot);

					for (final CapacityViolationType violation : violations) {
						if (CapacityViolationChecker.isHardViolation(violation)) {
							triggeredViolations.add(violation);
							currentViolatedSlots.add(slot);
						} else {
							++totalSoftViolations;
						}
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
			if (!currentViolatedSlots.isEmpty() || totalSoftViolations > initialSoftTotalViolations) {
				return false;
			}
		} // If this is the first run, then set the initial state
		else {
			initialViolatedSlots = currentViolatedSlots;
			initialSoftTotalViolations = totalSoftViolations;
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
