/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.annotations.IHeelLevelAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.impl.UnusedSlotDetails;
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
	private Map<IPortSlot, UnusedSlotDetails> unusedSlotDetails = null;

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

	public final Map<IPortSlot, UnusedSlotDetails> getUnusedSlotDetails() {
		return unusedSlotDetails;
	}

	public final void setUnusedSlotDetails(final Map<IPortSlot, UnusedSlotDetails> unusedSlotDetails) {
		this.unusedSlotDetails = unusedSlotDetails;
	}

	/**
	 * 
	 * @param resource
	 * @param startTime
	 * @param voyagePlans
	 * @param arrivalTimes
	 * @since 2.0
	 */
	public void addScheduledSequence(final IResource resource, final int startTime, final List<VoyagePlan> voyagePlans, int[] arrivalTimes) {
		add(new ScheduledSequence(resource, startTime, voyagePlans, arrivalTimes));
	}

	// public void getUnusedSlotsAnnotations() {
	// // TODO Auto-generated method stub
	//
	// }
}
