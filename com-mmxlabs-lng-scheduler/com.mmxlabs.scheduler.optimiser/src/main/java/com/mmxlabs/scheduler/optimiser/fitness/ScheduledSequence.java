/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.List;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public final class ScheduledSequence {
	final int startTime;
	final List<VoyagePlan> voyagePlans;
	private IResource resource;
	
	public ScheduledSequence(final IResource resource, final int startTime, final List<VoyagePlan> voyagePlans) {
		super();
		this.startTime = startTime;
		this.voyagePlans = voyagePlans;
		this.resource = resource;
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
}