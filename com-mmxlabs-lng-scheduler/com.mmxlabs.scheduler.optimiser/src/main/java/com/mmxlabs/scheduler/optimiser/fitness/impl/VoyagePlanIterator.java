/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * A class to help you iterate through a list of voyage plans, adding up the
 * time as you go
 * 
 * @author hinton
 * 
 */
public final class VoyagePlanIterator<T> {
	private static final Object[] EMPTY_SEQUENCE = new Object[]{};
	private int startTime;
	private List<VoyagePlan> plans;
	private Iterator<VoyagePlan> planIterator;
	private VoyagePlan currentPlan;
	private Object[] currentSequence;
	private int currentIndex;

	private int currentTime;
	private int extraTime;

	public VoyagePlanIterator() {

	}

	public final void setVoyagePlans(final List<VoyagePlan> plans,
			final int startTime) {
		this.plans = plans;
		this.startTime = startTime;
		reset();
	}

	public final void reset() {
		planIterator = plans.iterator();
		currentTime = 0;
		extraTime = startTime;
		if (planIterator.hasNext()) {
			currentPlan = planIterator.next();
			currentSequence = currentPlan.getSequence();
		} else {
			currentSequence = EMPTY_SEQUENCE;
		}
		currentIndex = 0;
	}

	public final Object nextObject() {
		currentTime += extraTime;
		if (currentIndex >= currentSequence.length) {
			// advance by one voyageplan.
			currentPlan = planIterator.next();
			currentSequence = currentPlan.getSequence();
			currentIndex = 0; // should I skip the extra port visit on the end
								// of
								// each plan?
		}

		// set extra time
		final Object obj = currentSequence[currentIndex];
		extraTime = 0;
		if (obj instanceof PortDetails) {
			final PortDetails details = (PortDetails) obj;
			if (currentIndex != currentSequence.length - 1)
				extraTime = details.getVisitDuration();
		} else if (obj instanceof VoyageDetails) {
			@SuppressWarnings("unchecked")
			final VoyageDetails<T> details = (VoyageDetails<T>) obj;
			extraTime = details.getOptions().getAvailableTime();
		}

		currentIndex++;
		return obj;
	}

	public final boolean hasNextObject() {
		return (currentSequence != null && currentIndex < currentSequence.length)
				|| planIterator.hasNext();
	}

	public final int getCurrentTime() {
		return currentTime;
	}

	public final VoyagePlan getCurrentPlan() {
		return currentPlan;
	}

	public final void iterateComponents(final List<VoyagePlan> plans,
			final int startTime, final IResource resource,
			final ICargoSchedulerFitnessComponent<T>[] iteratingComponents) {
		setVoyagePlans(plans, startTime);

		for (final ICargoSchedulerFitnessComponent<T> component : iteratingComponents) {
			component.beginIterating(resource);
		}

		while (hasNextObject()) {
			final Object o = nextObject();
			final int time = getCurrentTime();
			for (final ICargoSchedulerFitnessComponent<T> component : iteratingComponents) {
				component.evaluateNextObject(o, time);
			}
		}

		for (final ICargoSchedulerFitnessComponent<T> component : iteratingComponents) {
			component.endIterating();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static final <T> ICargoSchedulerFitnessComponent<T>[] filterIteratingComponents(
			final Collection<ICargoSchedulerFitnessComponent<T>> fitnessComponents) {
		final ArrayList<ICargoSchedulerFitnessComponent<T>> result = new ArrayList<ICargoSchedulerFitnessComponent<T>>();
		for (final ICargoSchedulerFitnessComponent<T> component : fitnessComponents) {
			if (component.shouldIterate()) {
				result.add(component);
			}
		}
		return result.toArray(new ICargoSchedulerFitnessComponent[]{});
	}
}
