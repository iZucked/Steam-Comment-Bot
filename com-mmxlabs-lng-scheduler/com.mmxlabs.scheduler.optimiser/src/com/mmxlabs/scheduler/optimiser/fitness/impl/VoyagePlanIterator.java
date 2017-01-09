/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mmxlabs.common.Triple;
import com.mmxlabs.scheduler.optimiser.annotations.IHeelLevelAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
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
	private List<Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>> plans;
	private Iterator<Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>> planIterator;
	private Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord> currentPlan;
	private Object[] currentSequence;
	private int currentPlanIndex;

	private int currentTime;
	private int extraTime;

	/**
	 */
	public VoyagePlanIterator(final VolumeAllocatedSequence scheduledSequence) {
		this.plans = scheduledSequence.getVoyagePlans();
		reset();
	}

	private final void reset() {
		planIterator = plans.iterator();
		currentTime = 0;
		// extraTime = arrivalTimes[0];
		if (planIterator.hasNext()) {
			currentPlan = planIterator.next();
			currentSequence = currentPlan.getFirst().getSequence();
		} else {
			currentSequence = EMPTY_SEQUENCE;
		}
		currentPlanIndex = 0;
	}

	public final Object nextObject() {
		currentTime += extraTime;

		if (planIterator.hasNext() && ((currentPlanIndex >= (currentSequence.length - 1) && currentPlan.getFirst().isIgnoreEnd()) || currentPlanIndex >= currentSequence.length)) {
			// advance by one voyageplan.
			currentPlan = planIterator.next();
			currentSequence = currentPlan.getFirst().getSequence();
			currentPlanIndex = 0; // should I skip the extra port visit on the end
									// of
									// each plan?
		}

		// set extra time
		final Object obj = currentSequence[currentPlanIndex];
		extraTime = 0;
		if (obj instanceof PortDetails) {
			final PortDetails details = (PortDetails) obj;
			if (!currentPlan.getFirst().isIgnoreEnd() || currentPlanIndex != (currentSequence.length - 1)) {
				currentTime = currentPlan.getThird().getSlotTime(details.getOptions().getPortSlot());
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
		return ((currentPlanIndex >= (currentSequence.length - 1) && currentPlan.getFirst().isIgnoreEnd()) || currentPlanIndex >= currentSequence.length);
	}

	public final int getCurrentTime() {
		return currentTime;
	}

	public final VoyagePlan getCurrentPlan() {
		return currentPlan.getFirst();
	}

	public IPortTimesRecord getCurrentPortTimeRecord() {
		return currentPlan.getThird();
	}

	public Map<IPortSlot, IHeelLevelAnnotation> getCurrentHeelLevelAnnotations() {
		return currentPlan.getSecond();
	}
}
