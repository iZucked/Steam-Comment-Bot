/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.ArrayList;
import java.util.List;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @author hinton
 *
 */
public final class ScheduledSequences extends ArrayList<ScheduledSequence>  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void addScheduledSequence(final IResource resource, final int startTime, final List<VoyagePlan> voyagePlans) {
		add(new ScheduledSequence(resource, startTime, voyagePlans));
	}
}
