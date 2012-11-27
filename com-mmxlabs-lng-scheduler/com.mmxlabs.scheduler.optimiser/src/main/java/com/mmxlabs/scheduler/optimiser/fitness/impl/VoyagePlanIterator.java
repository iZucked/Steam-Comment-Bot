/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.CargoShortEnd;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
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
	private int startTime;
	private IVessel vessel;
	private List<VoyagePlan> plans;
	private Iterator<VoyagePlan> planIterator;
	private VoyagePlan currentPlan;
	private Object[] currentSequence;
	private int currentIndex;

	private int currentTime;
	private int extraTime;

	@Inject
	private IVesselProvider vesselProvider;

	public final void setVoyagePlans(final IResource resource, final List<VoyagePlan> plans, final int startTime) {
		this.vessel = vesselProvider.getVessel(resource);
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
		boolean replaceCurrentTime = false;
		// Special case cargo shorts rotues. The Load is always independent of the previous cargo.
		if (currentIndex > 0 && vessel.getVesselInstanceType() == VesselInstanceType.CARGO_SHORTS) {
			// Get previous object
			final Object obj = currentSequence[currentIndex - 1];
			if (obj instanceof PortDetails) {
				final PortDetails portDetails = (PortDetails) obj;
				if (portDetails.getOptions().getPortSlot() instanceof CargoShortEnd) {
					// Replace the current time, but wait until after the sequence, index update
					replaceCurrentTime = true;
				}
			}
		}

		if ((planIterator.hasNext() && currentIndex >= (currentSequence.length - 1)) || currentIndex >= currentSequence.length) {
			// advance by one voyageplan.
			currentPlan = planIterator.next();
			currentSequence = currentPlan.getSequence();
			currentIndex = 0; // should I skip the extra port visit on the end
								// of
								// each plan?
		}
		if (replaceCurrentTime) {
			// A CargoShortEnd is either the last element or followed by a load
			final PortDetails currentLoad = (PortDetails) currentSequence[currentIndex];
			final IPortSlot currentSlot = currentLoad.getOptions().getPortSlot();
			// FIXME: Not strictly correct - the sequence scheduler could have picked any time within the window!
			currentTime = currentSlot.getTimeWindow().getStart();
		}

		// set extra time
		final Object obj = currentSequence[currentIndex];
		extraTime = 0;
		if (obj instanceof PortDetails) {
			final PortDetails details = (PortDetails) obj;
			if (currentIndex != (currentSequence.length - 1)) {
				extraTime = details.getOptions().getVisitDuration();
			}
		} else if (obj instanceof VoyageDetails) {
			final VoyageDetails details = (VoyageDetails) obj;
			extraTime = details.getOptions().getAvailableTime();
		}

		currentIndex++;
		return obj;
	}

	public final boolean hasNextObject() {
		return ((currentSequence != null) && (currentIndex < currentSequence.length)) || planIterator.hasNext();
	}

	public final boolean nextObjectIsStartOfPlan() {
		return ((currentSequence != null) && (currentIndex == 0)) || planIterator.hasNext();
	}

	public final int getCurrentTime() {
		return currentTime;
	}

	public final VoyagePlan getCurrentPlan() {
		return currentPlan;
	}

	/**
	 * Iterate a bunch of scheduler components
	 * 
	 * @param components
	 * @param sequences
	 */
	public boolean iterateSchedulerComponents(final Iterable<ICargoSchedulerFitnessComponent> components, final ScheduledSequences sequences, final Collection<IResource> affectedResources) {

		if (sequences == null) {
			return false;
		}

		for (final ICargoSchedulerFitnessComponent component : components) {
			component.startEvaluation();
		}

		for (final ScheduledSequence sequence : sequences) {
			// TODO work out why this makes for problems.
			if (!iterateSchedulerComponents(components, sequence, affectedResources.contains(sequence.getResource()))) {
				return false;
			}
		}

		for (final ICargoSchedulerFitnessComponent component : components) {
			component.endEvaluationAndGetCost();
		}

		return true;
	}

	/**
	 * Iterate the given scheduler components, and copy the resulting fitness values into the fitnesses array provided (the last parameter)
	 * 
	 * @param components
	 * @param sequences
	 * @param fitnesses
	 *            output parameter containing fitnesses, in the order the iterator provides the components
	 * @return
	 */
	public boolean iterateSchedulerComponents(final Iterable<ICargoSchedulerFitnessComponent> components, final ScheduledSequences sequences, final Collection<IResource> affectedResources,
			final long[] fitnesses) {
		for (final ICargoSchedulerFitnessComponent component : components) {
			component.startEvaluation();
		}

		for (final ScheduledSequence sequence : sequences) {
			if (!iterateSchedulerComponents(components, sequence, affectedResources.contains(sequence.getResource()))) {
				return false;
			}
		}

		int i = 0;
		for (final ICargoSchedulerFitnessComponent component : components) {
			fitnesses[i++] = component.endEvaluationAndGetCost();
		}

		return true;
	}

	/**
	 * Iterate through the given sequence, with the given components. Assumes start/end evaluation process has been done.
	 * 
	 * @param components
	 * @param sequence
	 * @return
	 */
	public boolean iterateSchedulerComponents(final Iterable<ICargoSchedulerFitnessComponent> components, final ScheduledSequence sequence, final boolean sequenceHasChanged) {

		for (final ICargoSchedulerFitnessComponent component : components) {
			/*
			 * Although the "true" here looks bad (always evaluate every sequence), we need to keep track somehow of which sequences have changed in the layer above. The random sequence scheduler may
			 * well change every sequence's schedule even if only one or two sequences' elements have been shifted by a move.
			 */
			component.startSequence(sequence.getResource(), sequenceHasChanged);
		}

		setVoyagePlans(sequence.getResource(), sequence.getVoyagePlans(), sequence.getStartTime());

		while (hasNextObject()) {
			if (nextObjectIsStartOfPlan()) {
				final Object obj = nextObject();
				final int time = getCurrentTime();
				final VoyagePlan plan = getCurrentPlan();

				for (final ICargoSchedulerFitnessComponent component : components) {
					if (!component.nextVoyagePlan(plan, time)) {
						return false;
					}
					if (!component.nextObject(obj, time)) {
						return false;
					}
				}
			} else {
				final Object obj = nextObject();
				final int time = getCurrentTime();
				for (final ICargoSchedulerFitnessComponent component : components) {
					if (!component.nextObject(obj, time)) {
						return false;
					}
				}
			}

		}

		for (final ICargoSchedulerFitnessComponent component : components) {
			if (!component.endSequence()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Ask the components to annotate the given sequences.
	 * 
	 * @param components
	 * @param sequence
	 * @param annotatedSequence
	 */
	public void annotateSchedulerComponents(final Iterable<ICargoSchedulerFitnessComponent> components, final ScheduledSequences sequences, final IAnnotatedSolution annotatedSolution) {

		for (final ICargoSchedulerFitnessComponent component : components) {
			component.startEvaluation();
		}

		for (final ScheduledSequence sequence : sequences) {
			annotateSequence(sequence, components, annotatedSolution);
		}

		for (final ICargoSchedulerFitnessComponent component : components) {
			component.endEvaluationAndAnnotate(annotatedSolution);
		}
	}

	/**
	 * @param sequence
	 * @param components
	 * @param annotatedSolution
	 */
	private void annotateSequence(final ScheduledSequence sequence, final Iterable<ICargoSchedulerFitnessComponent> components, final IAnnotatedSolution annotatedSolution) {
		for (final ICargoSchedulerFitnessComponent component : components) {
			component.startSequence(sequence.getResource(), true);
		}

		setVoyagePlans(sequence.getResource(), sequence.getVoyagePlans(), sequence.getStartTime());

		while (hasNextObject()) {
			if (nextObjectIsStartOfPlan()) {
				final Object obj = nextObject();
				final int time = getCurrentTime();
				final VoyagePlan plan = getCurrentPlan();

				for (final ICargoSchedulerFitnessComponent component : components) {
					if (!component.nextVoyagePlan(plan, time)) {
						return;
					}
					if (!component.annotateNextObject(obj, time, annotatedSolution)) {
						return;
					}
				}
			} else {
				final Object obj = nextObject();
				final int time = getCurrentTime();
				for (final ICargoSchedulerFitnessComponent component : components) {
					if (!component.annotateNextObject(obj, time, annotatedSolution)) {
						return;
					}
				}
			}

		}

		for (final ICargoSchedulerFitnessComponent component : components) {
			if (!component.endSequence()) {
				return;
			}
		}
	}
}
