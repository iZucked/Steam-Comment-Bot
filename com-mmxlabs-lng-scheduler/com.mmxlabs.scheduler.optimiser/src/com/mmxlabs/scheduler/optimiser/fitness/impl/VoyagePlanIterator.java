/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.Iterator;
import java.util.List;

import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * A class to help you iterate through a list of voyage plans, adding up the
 * time as you go
 * 
 * @author hinton
 * @noextend This class is not intended to be subclassed by clients.
 */
public class VoyagePlanIterator {
	private static final Object[] EMPTY_SEQUENCE = new Object[] {};
	private List<VoyagePlanRecord> plans;
	private Iterator<VoyagePlanRecord> planIterator;
	private VoyagePlanRecord currentPlan;
	private Object[] currentSequence;
	private int currentPlanIndex;

	private int currentTime;
	private int extraTime;

	/**
	 */
	public VoyagePlanIterator(final VolumeAllocatedSequence scheduledSequence) {
		this.plans = scheduledSequence.getVoyagePlanRecords();
		reset();
	}

	private final void reset() {
		planIterator = plans.iterator();
		currentTime = 0;
		// extraTime = arrivalTimes[0];
		if (planIterator.hasNext()) {
			currentPlan = planIterator.next();
			currentSequence = currentPlan.getVoyagePlan().getSequence();
		} else {
			currentSequence = EMPTY_SEQUENCE;
		}
		currentPlanIndex = 0;
	}

	public final Object nextObject() {
		currentTime += extraTime;

		if (planIterator.hasNext() && ((currentPlanIndex >= (currentSequence.length - 1) && currentPlan.getVoyagePlan().isIgnoreEnd()) || currentPlanIndex >= currentSequence.length)) {
			// advance by one voyageplan.
			currentPlan = planIterator.next();
			currentSequence = currentPlan.getVoyagePlan().getSequence();
			currentPlanIndex = 0; // should I skip the extra port visit on the end
									// of
									// each plan?
		}

		// set extra time
		final Object obj = currentSequence[currentPlanIndex];
		extraTime = 0;
		if (obj instanceof PortDetails) {
			final PortDetails details = (PortDetails) obj;
			if (!currentPlan.getVoyagePlan().isIgnoreEnd() || currentPlanIndex != (currentSequence.length - 1)) {
				currentTime = currentPlan.getPortTimesRecord().getSlotTime(details.getOptions().getPortSlot());
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
		return ((currentPlanIndex >= (currentSequence.length - 1) && currentPlan.getVoyagePlan().isIgnoreEnd()) || currentPlanIndex >= currentSequence.length);
	}

	public final int getCurrentTime() {
		return currentTime;
	}

	public final VoyagePlan getCurrentPlan() {
		return currentPlan.getVoyagePlan();
	}

	public final VoyagePlanRecord getCurrentPlanRecord() {
		return currentPlan;
	}

	public IPortTimesRecord getCurrentPortTimeRecord() {
		return currentPlan.getPortTimesRecord();
	}
}
