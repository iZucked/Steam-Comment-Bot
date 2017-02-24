/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.ArrayList;
import java.util.EnumSet;
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
import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;

/**
 * 
 * {@link ICargoSchedulerFitnessComponent} implementation to calculate a fitness based on capacity violations.
 * 
 * @author Simon Goodall
 * 
 */
public final class CapacityEvaluatedStateChecker implements IEvaluatedStateConstraintChecker {

	private long initialTotalViolations = 0;
	private List<CapacityViolationType> initialTriggeredViolations;

	public List<CapacityViolationType> allViolations;
	public List<CapacityViolationType> initialAllViolations;

	private long totalViolations = 0;
	private final @NonNull String name;
	private boolean initialised;
	private Set<IPortSlot> initialViolatedSlots = null;
	private Set<IPortSlot> currentViolatedSlots;
	private List<CapacityViolationType> triggeredViolations;

	private final Set<CapacityViolationType> targetViolations = EnumSet.of(//
			CapacityViolationType.MIN_DISCHARGE, //
			CapacityViolationType.MAX_DISCHARGE, //
			CapacityViolationType.MIN_LOAD, //
			CapacityViolationType.MAX_LOAD, //
			CapacityViolationType.VESSEL_CAPACITY);

	public CapacityEvaluatedStateChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences rawSequences, @NonNull final ISequences fullSequences, @NonNull final IEvaluationState evaluationState) {

		final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);

		if (volumeAllocatedSequences == null) {
			return true;
		}

		totalViolations = volumeAllocatedSequences.stream()//
				.mapToLong(seq -> seq.getSequenceSlots().stream() //
						.mapToLong(slot -> seq.getCapacityViolationCount(slot)).sum())
				.sum();

		currentViolatedSlots = new HashSet<IPortSlot>();
		triggeredViolations = new ArrayList<CapacityViolationType>();
		allViolations = new ArrayList<CapacityViolationType>();

		for (final VolumeAllocatedSequence volumeAllocatedSequence : volumeAllocatedSequences) {
			final List<@NonNull IPortSlot> slots = volumeAllocatedSequence.getSequenceSlots();

			for (final IPortSlot slot : slots) {
				final List<@NonNull CapacityViolationType> violations = volumeAllocatedSequence.getCapacityViolations(slot);

				for (@NonNull
				final CapacityViolationType violation : violations) {
					allViolations.add(violation);
					if (targetViolations.contains(violation)) {
						triggeredViolations.add(violation);
						currentViolatedSlots.add(slot);
					}
				}

			}
		}

		if (initialViolatedSlots != null) {
			currentViolatedSlots.removeAll(initialViolatedSlots);
			if (totalViolations > initialTotalViolations || currentViolatedSlots.size() > 0) {
				return false;
			}
		} // If this is the first run, then set the initial state
		else {
			initialViolatedSlots = currentViolatedSlots;
			initialTotalViolations = totalViolations;
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

	public long getTotalViolations() {
		return totalViolations;
	}

	public long getInitialViolations() {
		return initialTotalViolations;
	}

	public boolean isInitialised() {
		return initialised;
	}

	public Set<IPortSlot> getViolatedSlots() {
		return initialViolatedSlots;
	}

	public List<CapacityViolationType> getInitialTriggeredViolations() {
		return initialTriggeredViolations;
	}

}
