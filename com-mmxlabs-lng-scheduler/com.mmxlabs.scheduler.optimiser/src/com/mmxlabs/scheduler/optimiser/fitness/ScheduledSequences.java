/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.annotations.IHeelLevelAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
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

	// TODO: Need better mechanism for this stuff!
	private Map<VoyagePlan, IAllocationAnnotation> allocations = null;
	private Map<IPortSlot, IHeelLevelAnnotation> heelLevels = null;
	private final Map<IPortSlot, Long> unusedSlotGroupValue = new HashMap<>();
	private final Map<IPortSlot, Long> capacityViolationSum = new HashMap<>();
	private final Map<VoyagePlan, Long> voyagePlanGroupValue = new HashMap<>();

	public final Map<VoyagePlan, IAllocationAnnotation> getAllocations() {
		return allocations;
	}

	public final void setAllocations(final Map<VoyagePlan, IAllocationAnnotation> allocations) {
		this.allocations = allocations;
	}

	public final Map<IPortSlot, IHeelLevelAnnotation> getHeelLevels() {
		return heelLevels;
	}

	public final void setHeelLevels(final Map<IPortSlot, IHeelLevelAnnotation> heelLevels) {
		this.heelLevels = heelLevels;
	}

	/**
	 * 
	 * @param resource
	 * @param startTime
	 * @param voyagePlans
	 * @param arrivalTimes
	 */
	public void addScheduledSequence(final IResource resource, final int startTime, final List<VoyagePlan> voyagePlans, final int[] arrivalTimes) {
		add(new ScheduledSequence(resource, startTime, voyagePlans, arrivalTimes));
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
}
