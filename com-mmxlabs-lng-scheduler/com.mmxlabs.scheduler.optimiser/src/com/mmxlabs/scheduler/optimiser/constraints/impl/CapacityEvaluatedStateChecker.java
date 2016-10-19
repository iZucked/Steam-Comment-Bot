/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.fitness.components.capacity.ICapacityEntry;
import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;

/**
 * 
 * {@link ICargoSchedulerFitnessComponent} implementation to calculate a fitness based on capacity violations.
 * 
 * @author Simon Goodall
 * 
 */
public final class CapacityEvaluatedStateChecker implements IEvaluatedStateConstraintChecker {

	private long initialViolations = 0;
	private List<CapacityViolationType> initialTriggeredViolations;
	public List<CapacityViolationType> getInitialTriggeredViolations() {
		return initialTriggeredViolations;
	}
	public List<CapacityViolationType> allViolations;
	public List<CapacityViolationType> allViolationsOriginal;
	
	private long totalViolations = 0;
	private final @NonNull String name;
	private boolean initialised;
	private Set<IPortSlot> violatedSlots = null;
	private Set<IPortSlot> currentViolatedSlots;
	private List<CapacityViolationType> triggeredViolations;
	
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
		return initialViolations;
	}


	public boolean isInitialised() {
		return initialised;
	}


	public Set<IPortSlot> getViolatedSlots() {
		return violatedSlots;
	}

	private Set<CapacityViolationType> targetViolations = EnumSet.of(CapacityViolationType.MIN_DISCHARGE,
			CapacityViolationType.MIN_LOAD,CapacityViolationType.MAX_LOAD,CapacityViolationType.MAX_DISCHARGE,
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
		
		for (VolumeAllocatedSequence volumeAllocatedSequence : volumeAllocatedSequences) {
			List<@NonNull IPortSlot> slots = volumeAllocatedSequence.getSequenceSlots();
			
			for( IPortSlot slot : slots){
				List<@NonNull CapacityViolationType> violations = volumeAllocatedSequence.getCapacityViolations(slot);
				
				for(@NonNull CapacityViolationType violation: violations){
					allViolations.add(violation);
					if(targetViolations.contains(violation)){
						triggeredViolations.add(violation);
						currentViolatedSlots.add(slot);
					}
				}
			
			}
		}

	if (violatedSlots != null) {
		currentViolatedSlots.removeAll(violatedSlots);
		if (totalViolations > initialViolations || currentViolatedSlots.size() > 0) {
			return false;
		}
	}// If this is the first run, then set the initial state
	else {
		violatedSlots = currentViolatedSlots;	
		initialViolations = totalViolations;
		allViolationsOriginal = allViolations;
		initialised = true;
		return true;
	}
	
	return true;
	}

	@Override
	public @NonNull String getName() {
		return name;
	}

}
