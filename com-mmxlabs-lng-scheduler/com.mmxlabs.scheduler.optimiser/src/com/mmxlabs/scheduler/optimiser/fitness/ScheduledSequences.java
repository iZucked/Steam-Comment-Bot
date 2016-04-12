/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import com.mmxlabs.scheduler.optimiser.schedule.IdleTimeChecker;
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
public final class ScheduledSequences extends ArrayList<@NonNull ScheduledSequence> {
	private static final long serialVersionUID = 1L;

	private final Map<IPortSlot, Long> unusedSlotGroupValue = new HashMap<>();
	private final Map<IPortSlot, Long> capacityViolationSum = new HashMap<>();
	private final Map<IPortSlot, Pair<Interval, Long>> latenessSum = new HashMap<>();
	private final Map<IPortSlot, Long> weightedLatenessSum = new HashMap<>();
	private final Map<VoyagePlan, Long> voyagePlanGroupValue = new HashMap<>();
	private final Map<IResource, ScheduledSequence> resourceToScheduledSequenceMap = new HashMap<>();
	private final Set<@NonNull IPortSlot> lateSlots = new HashSet<>();
	private final Map<IPortSlot, Integer> violatingIdleHours = new HashMap<>();
	private final Map<IPortSlot, Long> weightedIdleCost = new HashMap<>();

	/**
	 * 
	 * @param resource
	 * @param startTime
	 * @param voyagePlans
	 * @param arrivalTimes
	 */
	public void addScheduledSequence(final @NonNull IResource resource, final @NonNull ISequence sequence, final int startTime,
			final List<@NonNull Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>> voyagePlans) {
		final ScheduledSequence scheduledSequence = new ScheduledSequence(resource, sequence, startTime, voyagePlans);
		resourceToScheduledSequenceMap.put(resource, scheduledSequence);
		add(scheduledSequence);
	}

	@Override
	public boolean add(@NonNull final ScheduledSequence scheduledSequence) {
		resourceToScheduledSequenceMap.put(scheduledSequence.getResource(), scheduledSequence);
		return super.add(scheduledSequence);
	}

	public void setUnusedSlotGroupValue(@NonNull final IPortSlot portSlot, final long groupValue) {
		unusedSlotGroupValue.put(portSlot, groupValue);
	}

	public long getUnusedSlotGroupValue(@NonNull final IPortSlot portSlot) {
		if (unusedSlotGroupValue.containsKey(portSlot)) {
			return unusedSlotGroupValue.get(portSlot);
		}
		return 0L;
	}

	public void setVoyagePlanGroupValue(@NonNull final VoyagePlan plan, final long groupValue) {
		voyagePlanGroupValue.put(plan, groupValue);
	}

	public long getVoyagePlanGroupValue(@NonNull final VoyagePlan plan) {
		if (voyagePlanGroupValue.containsKey(plan)) {
			return voyagePlanGroupValue.get(plan);
		}
		return 0L;

	}

	public void addCapacityViolation(final @NonNull IPortSlot portSlot) {
		long sum = 0L;
		if (capacityViolationSum.containsKey(portSlot)) {
			sum = capacityViolationSum.get(portSlot);
		}
		capacityViolationSum.put(portSlot, 1 + sum);
	}

	public long getCapacityViolationCount(final @NonNull IPortSlot portSlot) {
		if (capacityViolationSum.containsKey(portSlot)) {
			return capacityViolationSum.get(portSlot);
		}
		return 0L;
	}

	public ScheduledSequence getScheduledSequenceForResource(final @NonNull IResource resource) {
		return resourceToScheduledSequenceMap.get(resource);
	}

	public void addLatenessCost(@NonNull final IPortSlot portSlot, final Pair<Interval, Long> lateness) {
		latenessSum.put(portSlot, lateness);
	}

	public void addWeightedLatenessCost(@NonNull final IPortSlot portSlot, final long weightedLateness) {
		weightedLatenessSum.put(portSlot, (long) weightedLateness);
	}

	public @Nullable Pair<Interval, Long> getLatenessCost(@NonNull final IPortSlot portSlot) {
		if (latenessSum.containsKey(portSlot)) {
			return latenessSum.get(portSlot);
		} else {
			return null;
		}
	}

	public long getWeightedLatenessCost(final @NonNull IPortSlot portSlot) {
		if (weightedLatenessSum.containsKey(portSlot)) {
			return weightedLatenessSum.get(portSlot);
		} else {
			return 0L;
		}
	}

	public long getTotalWeightedLateness() {
		long sum = 0L;
		for (final long lateness : weightedLatenessSum.values()) {
			sum += lateness;
		}
		return sum;
	}

	public void resetLateSlots() {
		lateSlots.clear();
	}

	public void addLateSlot(final @NonNull IPortSlot slot) {
		lateSlots.add(slot);
	}

	public void addIdleHoursViolation(@NonNull IPortSlot slot, int violatingHours) {
		violatingIdleHours.put(slot, violatingHours);
	}

	public long getIdleTimeViolationHours(final @NonNull IPortSlot portSlot) {
		if (violatingIdleHours.containsKey(portSlot)) {
			return violatingIdleHours.get(portSlot);
		}
		return 0L;
	}

	public void addIdleWeightedCost(@NonNull IPortSlot slot, long cost) {
		weightedIdleCost.put(slot, cost);
	}
	
	public long getIdleWeightedCost(final @NonNull IPortSlot portSlot) {
		if (weightedIdleCost.containsKey(portSlot)) {
			return weightedIdleCost.get(portSlot);
		}
		return 0L;
	}

	public boolean isLateSlot(@NonNull IPortSlot slot) {
		return lateSlots.contains(slot);
	}

	public @NonNull Set<@NonNull IPortSlot> getLateSlotsSet() {
		final Set<@NonNull IPortSlot> duplicateSet = new HashSet<>();
		duplicateSet.addAll(lateSlots);
		return duplicateSet;
	}
}
