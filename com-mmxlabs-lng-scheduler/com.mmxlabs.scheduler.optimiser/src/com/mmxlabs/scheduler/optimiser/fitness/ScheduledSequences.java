/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.scheduler.optimiser.annotations.IHeelLevelAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
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
public final class ScheduledSequences extends ArrayList<ScheduledSequence> {
	private static final long serialVersionUID = 1L;

	private final Map<IPortSlot, Long> unusedSlotGroupValue = new HashMap<>();
	private final Map<IPortSlot, Long> capacityViolationSum = new HashMap<>();
	private final Map<VoyagePlan, Long> voyagePlanGroupValue = new HashMap<>();
	private final Map<IResource, ScheduledSequence> resourceToScheduledSequenceMap = new HashMap<>();

	/**
	 * 
	 * @param resource
	 * @param startTime
	 * @param voyagePlans
	 * @param arrivalTimes
	 */
	public void addScheduledSequence(final IResource resource, final ISequence sequence, final int startTime,
			final List<Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>> voyagePlans) {
		final ScheduledSequence scheduledSequence = new ScheduledSequence(resource, sequence, startTime, voyagePlans);
		resourceToScheduledSequenceMap.put(resource, scheduledSequence);
		add(scheduledSequence);
	}

	@Override
	public boolean add(ScheduledSequence scheduledSequence) {
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

	public void addCapacityViolation(final IPortSlot portSlot) {
		long sum = 0L;
		if (capacityViolationSum.containsKey(portSlot)) {
			sum = capacityViolationSum.get(portSlot);
		}
		capacityViolationSum.put(portSlot, 1 + sum);
	}

	public long getCapacityViolationCount(final IPortSlot portSlot) {
		if (capacityViolationSum.containsKey(portSlot)) {
			return capacityViolationSum.get(portSlot);
		}
		return 0L;
	}
	
	public ScheduledSequence getScheduledSequenceForResource(final IResource resource) {
		return resourceToScheduledSequenceMap.get(resource);
	}
}
