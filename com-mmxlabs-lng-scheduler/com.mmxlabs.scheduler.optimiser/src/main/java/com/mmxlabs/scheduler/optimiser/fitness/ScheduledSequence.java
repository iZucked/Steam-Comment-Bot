/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.List;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public final class ScheduledSequence {
	private final int startTime;
	private final List<VoyagePlan> voyagePlans;
	private final IResource resource;
	private final int[] arrivalTimes;

	/**
	 * @since 2.0
	 */
	public ScheduledSequence(final IResource resource, final int startTime, final List<VoyagePlan> voyagePlans, final int[] arrivalTimes) {
		super();
		this.startTime = startTime;
		this.voyagePlans = voyagePlans;
		this.resource = resource;
		this.arrivalTimes = arrivalTimes;
	}

	public IResource getResource() {
		return resource;
	}

	public int getStartTime() {
		return startTime;
	}

	public List<VoyagePlan> getVoyagePlans() {
		return voyagePlans;
	}

	/**
	 * @return
	 * @since 2.0
	 */
	public int[] getArrivalTimes() {
		return arrivalTimes;
	}
}