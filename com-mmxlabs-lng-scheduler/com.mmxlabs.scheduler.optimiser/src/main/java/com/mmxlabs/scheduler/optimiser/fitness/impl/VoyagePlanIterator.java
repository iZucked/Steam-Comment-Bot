/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * A class to help you iterate through a list of voyage plans, adding up the time as you go
 * 
 * @author hinton
 * @noextend This class is not intended to be subclassed by clients.
 */
public class VoyagePlanIterator {
	private static final Object[] EMPTY_SEQUENCE = new Object[] {};
	private List<VoyagePlan> plans;
	private Iterator<VoyagePlan> planIterator;
	private VoyagePlan currentPlan;
	private Object[] currentSequence;
	private int currentPlanIndex;
	private int currentTimeIndex;

	private int currentTime;
	private int extraTime;
	private int arrivalTimes[];

	/**
	 * @since 2.0
	 */
	public final void setVoyagePlans(final IResource resource, final List<VoyagePlan> plans, int arrivalTimes[]) {
		this.plans = plans;
		this.arrivalTimes = arrivalTimes;
		reset();
	}

	public final void reset() {
		planIterator = plans.iterator();
		currentTime = 0;
		extraTime = arrivalTimes[0];
		if (planIterator.hasNext()) {
			currentPlan = planIterator.next();
			currentSequence = currentPlan.getSequence();
		} else {
			currentSequence = EMPTY_SEQUENCE;
		}
		currentPlanIndex = 0;
		currentTimeIndex = 0;
	}

	public final Object nextObject() {
		currentTime += extraTime;

		if (planIterator.hasNext() && ((currentPlanIndex >= (currentSequence.length - 1) && currentPlan.isIgnoreEnd()) || currentPlanIndex >= currentSequence.length)) {
			// advance by one voyageplan.
			currentPlan = planIterator.next();
			currentSequence = currentPlan.getSequence();
			currentPlanIndex = 0; // should I skip the extra port visit on the end
									// of
									// each plan?
		}

		// set extra time
		final Object obj = currentSequence[currentPlanIndex];
		extraTime = 0;
		if (obj instanceof PortDetails) {
			final PortDetails details = (PortDetails) obj;
			if (!currentPlan.isIgnoreEnd() || currentPlanIndex != (currentSequence.length - 1)) {
				currentTime = arrivalTimes[currentTimeIndex++];
				extraTime = details.getOptions().getVisitDuration();
			}
		} else if (obj instanceof VoyageDetails) {
			final VoyageDetails details = (VoyageDetails) obj;
			extraTime = details.getOptions().getAvailableTime();
		}

		currentPlanIndex++;
		return obj;
	}

	public final boolean hasNextObject() {
		return ((currentSequence != null) && (currentPlanIndex < currentSequence.length)) || planIterator.hasNext();
	}

	public final boolean nextObjectIsStartOfPlan() {
		return ((currentSequence != null) && (currentPlanIndex == 0)) || planIterator.hasNext();
	}

	public final int getCurrentTime() {
		return currentTime;
	}

	public final VoyagePlan getCurrentPlan() {
		return currentPlan;
	}
}
