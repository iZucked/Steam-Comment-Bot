/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.scheduler.optimiser.annotations.IHeelLevelAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class VolumeAllocatedSequence {
	private final @NonNull IResource resource;
	private final @NonNull ISequence sequence;
	private final int startTime;
	private final List<@NonNull Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>> voyagePlans;

	// Cached Lookup data
	private final Map<IPortSlot, Integer> portSlotToTimeMap = new HashMap<>();
	private final Map<IPortSlot, VoyagePlan> portSlotToVoyagePlanMap = new HashMap<>();
	private final Map<IPortSlot, IPortTimesRecord> portSlotToPortTimesRecordMap = new HashMap<>();
	private final Map<IPortSlot, IHeelLevelAnnotation> portSlotToHeelLevelAnnotationMap = new HashMap<>();
	private final List<@NonNull IPortSlot> sequencePortSlots;

	private final @NonNull Set<@NonNull IPortSlot> lateSlots = new HashSet<>();

	private static class SlotRecord {
		public int arrivalTime;
		public VoyagePlan voyagePlan;
		public IPortTimesRecord portTimesRecord;
		public IHeelLevelAnnotation heelLevelAnnotation;
		public int violatingIdleHours;
		public long weightedIdleCost;
		public long weightedLatenessSum;
		public long capacityViolationSum;
		public Pair<Interval, Long> latenessSum;
		public List<@NonNull CapacityViolationType> capacityViolations = new ArrayList<>();

		public List<CapacityViolationType> getCapacityViolations() {
			return capacityViolations;
		}

	}

	private final Map<IPortSlot, SlotRecord> slotRecords;

	/**
	 */
	public VolumeAllocatedSequence(final @NonNull IResource resource, final @NonNull ISequence sequence, final int startTime,
			final List<@NonNull Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>> voyagePlans) {
		super();
		this.sequence = sequence;
		this.startTime = startTime;
		this.voyagePlans = voyagePlans;
		this.resource = resource;
		this.sequencePortSlots = new ArrayList<>(sequence.size());

		this.slotRecords = new HashMap<>(sequence.size());

		// Build the lookup data!
		buildLookup();
	}

	protected @NonNull ISequence getSequence() {
		return sequence;
	}

	public @NonNull IResource getResource() {
		return resource;
	}

	public int getStartTime() {
		return startTime;
	}

	public List<@NonNull Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>> getVoyagePlans() {
		return voyagePlans;
	}

	public int getArrivalTime(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).arrivalTime;
	}

	public VoyagePlan getVoyagePlan(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).voyagePlan;
	}

	public List<@NonNull IPortSlot> getSequenceSlots() {
		return sequencePortSlots;
	}

	public IPortTimesRecord getPortTimesRecord(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).portTimesRecord;
	}

	@Nullable
	public IAllocationAnnotation getAllocationAnnotation(final @NonNull IPortSlot portSlot) {
		final IPortTimesRecord portTimesRecord = getPortTimesRecord(portSlot);
		if (portTimesRecord instanceof IAllocationAnnotation) {
			return (IAllocationAnnotation) portTimesRecord;
		}
		return null;

	}

	public void addCapacityViolation(final @NonNull IPortSlot portSlot, @NonNull CapacityViolationType cvt) {

		@NonNull
		final SlotRecord record = getOrExceptionSlotRecord(portSlot);
		record.capacityViolationSum += 1;
		record.capacityViolations.add(cvt);
	}

	public long getCapacityViolationCount(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).capacityViolationSum;
	}

	public void addLatenessCost(@NonNull final IPortSlot portSlot, final Pair<Interval, Long> lateness) {
		getOrExceptionSlotRecord(portSlot).latenessSum = lateness;
	}

	public void addWeightedLatenessCost(@NonNull final IPortSlot portSlot, final long weightedLateness) {
		getOrExceptionSlotRecord(portSlot).weightedLatenessSum = weightedLateness;
	}

	public @Nullable Pair<Interval, Long> getLatenessCost(@NonNull final IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).latenessSum;
	}

	public long getWeightedLatenessCost(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).weightedLatenessSum;

	}

	public void addLateSlot(final @NonNull IPortSlot portSlot) {
		lateSlots.add(portSlot);
	}

	public void addIdleHoursViolation(@NonNull final IPortSlot portSlot, final int violatingHours) {
		getOrExceptionSlotRecord(portSlot).violatingIdleHours = violatingHours;
	}

	public long getIdleTimeViolationHours(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).violatingIdleHours;
	}

	public void addIdleWeightedCost(@NonNull final IPortSlot slot, final long cost) {
		getOrExceptionSlotRecord(slot).weightedIdleCost = cost;
	}

	public long getIdleWeightedCost(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).weightedIdleCost;
	}

	public List<@NonNull CapacityViolationType> getCapacityViolations(final @NonNull IPortSlot portSlot) {
		return getOrExceptionSlotRecord(portSlot).capacityViolations;
	}

	public boolean isLateSlot(final @NonNull IPortSlot slot) {
		return lateSlots.contains(slot);
	}

	public @NonNull Set<@NonNull IPortSlot> getLateSlotsSet() {
		return lateSlots;
	}

	private @NonNull SlotRecord getOrCreateSlotRecord(final @NonNull IPortSlot slot) {
		SlotRecord allocation = slotRecords.get(slot);
		if (allocation == null) {
			allocation = new SlotRecord();
			slotRecords.put(slot, allocation);
		}
		return allocation;
	}

	private @NonNull SlotRecord getOrExceptionSlotRecord(final @NonNull IPortSlot slot) {
		final SlotRecord allocation = slotRecords.get(slot);
		if (allocation == null) {
			throw new IllegalArgumentException("Port slot is not part of this sequence");
		}
		return allocation;
	}

	/**
	 * Builds the lookup data. TODO: More efficient if this is done as the sequence data is built up!
	 */
	private void buildLookup() {
		final VoyagePlanIterator vpi = new VoyagePlanIterator(this);

		// Lists to store the voyageplan data
		while (vpi.hasNextObject()) {
			final Object e = vpi.nextObject();
			if (e instanceof PortDetails) {
				final PortDetails details = (PortDetails) e;
				final IPortSlot portSlot = details.getOptions().getPortSlot();
				final int currentTime = vpi.getCurrentTime();

				// Set mapping between slot and time / current plan
				sequencePortSlots.add(portSlot);
				@NonNull
				final SlotRecord record = getOrCreateSlotRecord(portSlot);
				record.arrivalTime = currentTime;
				record.voyagePlan = vpi.getCurrentPlan();
				record.portTimesRecord = vpi.getCurrentPortTimeRecord();
				Map<IPortSlot, IHeelLevelAnnotation> currentHeelLevelAnnotations = vpi.getCurrentHeelLevelAnnotations();
				if (currentHeelLevelAnnotations != null) {
					record.heelLevelAnnotation = currentHeelLevelAnnotations.get(portSlot);
				}

			}
		}
	}

	public final boolean isEqual(@NonNull final VolumeAllocatedSequence other) {

		return this.startTime == other.startTime //
				&& Objects.deepEquals(this.resource, other.resource) //
				&& Objects.deepEquals(this.portSlotToHeelLevelAnnotationMap, other.portSlotToHeelLevelAnnotationMap) //
				&& Objects.deepEquals(this.portSlotToPortTimesRecordMap, other.portSlotToPortTimesRecordMap) //
				&& Objects.deepEquals(this.portSlotToTimeMap, other.portSlotToTimeMap) //
				&& Objects.deepEquals(this.portSlotToVoyagePlanMap, other.portSlotToVoyagePlanMap) //
				&& Objects.deepEquals(this.sequencePortSlots, other.sequencePortSlots) //
				&& Objects.deepEquals(this.sequence, other.sequence) //
				&& Objects.deepEquals(this.voyagePlans, other.voyagePlans);
	}

}