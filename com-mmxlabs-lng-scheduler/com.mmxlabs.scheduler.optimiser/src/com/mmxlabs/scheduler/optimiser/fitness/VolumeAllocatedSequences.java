/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.annotations.IHeelLevelAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Represents a solution undergoing evaluation as a list of lists of voyageplans, start times, resources etc.
 * 
 * Also stores information about cargo allocations and load prices, once they have been filled in and updated.
 * 
 * @author hinton
 * 
 */
public class VolumeAllocatedSequences extends ArrayList<@NonNull VolumeAllocatedSequence> {
	private static final long serialVersionUID = 1L;

	private final Map<@NonNull IResource, @NonNull VolumeAllocatedSequence> resourceToScheduledSequenceMap = new HashMap<>();
	private final Map<IPortSlot, VolumeAllocatedSequence> slotToSequenceCache = new HashMap<>();
	private final Map<@NonNull VolumeAllocatedSequence, @NonNull IVesselAvailability> sequenceToAvailabilityMap = new HashMap<>();
	
	public VolumeAllocatedSequences(){
		
	}
	
	//Unit Test Constructor
	public VolumeAllocatedSequences(ArrayList<VolumeAllocatedSequence> list) {
			super.addAll(list);
	}


	//	@Override 
	@Deprecated
	public boolean add(@NonNull final VolumeAllocatedSequence scheduledSequence) {
		// Call the other #add(IVesselAvailability,VolumeAllocatedSequence) method
		throw new UnsupportedOperationException("");
	}

	public boolean add(@NonNull IVesselAvailability vesselAvailability, @NonNull final VolumeAllocatedSequence scheduledSequence) {
		resourceToScheduledSequenceMap.put(scheduledSequence.getResource(), scheduledSequence);
		sequenceToAvailabilityMap.put(scheduledSequence, vesselAvailability);

		for (final IPortSlot portSlot : scheduledSequence.getSequenceSlots()) {
			slotToSequenceCache.put(portSlot, scheduledSequence);
		}

		return super.add(scheduledSequence);
	}

	@NonNull
	public VolumeAllocatedSequence getScheduledSequenceForResource(final @NonNull IResource resource) {
		return resourceToScheduledSequenceMap.get(resource);
	}

	@Nullable
	public VolumeAllocatedSequence getScheduledSequence(final @NonNull IPortSlot portSlot) {
		return slotToSequenceCache.get(portSlot);
	}

	@Nullable
	public VoyagePlan getVoyagePlan(final @NonNull IPortSlot portSlot) {

		final VolumeAllocatedSequence sequence = getScheduledSequence(portSlot);
		if (sequence != null) {
			return sequence.getVoyagePlan(portSlot);
		}

		return null;
	}

	@Nullable
	public IVesselAvailability getVesselAvailability(final @NonNull IPortSlot portSlot) {

		final VolumeAllocatedSequence sequence = getScheduledSequence(portSlot);
		if (sequence != null) {
			return sequenceToAvailabilityMap.get(sequence);
		}

		return null;
	}

	@Nullable
	public Integer getVesselStartTime(final @NonNull IPortSlot portSlot) {
		final VolumeAllocatedSequence sequence = getScheduledSequence(portSlot);
		if (sequence != null) {
			return sequence.getStartTime();
		}
		return null;
	}
	
	@Nullable
	public Integer getVesselEndTime(final @NonNull IPortSlot portSlot) {
		final VolumeAllocatedSequence sequence = getScheduledSequence(portSlot);
		if (sequence != null) {
			List<@NonNull Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>> voyagePlans = sequence.getVoyagePlans();
			@NonNull
			Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord> triple = voyagePlans.get(voyagePlans.size() - 1);
			IPortTimesRecord third = triple.getThird();
			int endTime = third.getSlotTime(third.getSlots().get(0));
			int endDuration = third.getSlotDuration(third.getSlots().get(0));
			return endTime + endDuration;
		}
		return null;
	}


	@Nullable
	public IAllocationAnnotation getAllocationAnnotation(final @NonNull IPortSlot portSlot) {

		final VolumeAllocatedSequence sequence = getScheduledSequence(portSlot);
		if (sequence != null) {
			return sequence.getAllocationAnnotation(portSlot);
		}
		return null;
	}
}
