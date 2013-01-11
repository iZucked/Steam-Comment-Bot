/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mmxlabs.optimiser.core.IResource;
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

	private Collection<IAllocationAnnotation> allocations = null;

	public final Collection<IAllocationAnnotation> getAllocations() {
		return allocations;
	}

	public final void setAllocations(final Collection<IAllocationAnnotation> allocations) {
		this.allocations = allocations;
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
}
