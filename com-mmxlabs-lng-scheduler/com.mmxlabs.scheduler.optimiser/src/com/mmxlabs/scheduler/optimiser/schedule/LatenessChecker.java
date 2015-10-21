/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.fitness.components.LatenessAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPromptPeriodProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;

/**
 * 
 */
public class LatenessChecker {

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	@NonNull
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Inject
	@NonNull
	private IPromptPeriodProvider promptPeriodProvider;

	@Inject
	@NonNull
	private ILatenessComponentParameters latenessParameters;

	public static String GA_TOTAL_LATENESS_IN_HOURS = "total-lateness-hours";
	public static String GA_TOTAL_PROMPT_LATENESS_LOW_IN_HOURS = "total-prompt-low-lateness-hours";
	public static String GA_TOTAL_PROMPT_LATENESS_HIGH_IN_HOURS = "total-prompt-high-lateness-hours";
	public static String GA_TOTAL_MIDTERM_LATENESS_LOW_IN_HOURS = "total-midterm-low-lateness-hours";
	public static String GA_TOTAL_MIDTERM_LATENESS_HIGH_IN_HOURS = "total-midterm-high-lateness-hours";
	public static String GA_TOTAL_BEYOND_LATENESS_LOW_IN_HOURS = "total-beyond-low-lateness-hours";
	public static String GA_TOTAL_BEYOND_LATENESS_HIGH_IN_HOURS = "total-beyond-high-lateness-hours";

	/**
	 * TODO: Break out into separate class Calculate the various capacity violation check - min/max load & discharge volumes, max heel, vessel capacity and cooldown. Note the {@link IVolumeAllocator}
	 * and {@link LNGVoyageCalculator} generally feed into these checks.
	 * 
	 * @param sequences
	 * @param scheduledSequences
	 * @param allocations
	 * @param annotatedSolution
	 */
	public void calculateLateness(final ScheduledSequences scheduledSequences, @Nullable final IAnnotatedSolution annotatedSolution) {
		// clear late slots
		scheduledSequences.resetLateSlots();
		// Loop over all sequences
		for (final ScheduledSequence scheduledSequence : scheduledSequences) {
			final IResource resource = scheduledSequence.getResource();
			assert resource != null;
			for (IPortSlot portSlot : scheduledSequence.getSequenceSlots()) {
				final ITimeWindow tw = getTW(portSlot, resource);
				int latenessInHours = getLateness(portSlot, resource, tw, scheduledSequence.getArrivalTime(portSlot));
				addLateSlot(portSlot, latenessInHours, scheduledSequences);
				if (latenessInHours > 0 || (annotatedSolution != null && tw != null)) {
					Pair<ILatenessComponentParameters.Interval, Long> weightedLatenessPair = getWeightedLateness(tw, latenessInHours);
					addEntryToLatenessAnnotation(annotatedSolution, portSlot, tw, weightedLatenessPair.getFirst(), latenessInHours,
							getLatenessWithoutFlex(portSlot, resource, tw, scheduledSequence.getArrivalTime(portSlot)), weightedLatenessPair.getSecond(), scheduledSequences);
				}
			}
		}
	}

	private void addLateSlot(IPortSlot portSlot, int latenessInHours, ScheduledSequences scheduledSequences) {
		if (latenessInHours > 0) {
			scheduledSequences.addLateSlot(portSlot);
		}
	}

	private ITimeWindow getTW(IPortSlot portSlot, IResource resource) {
		ITimeWindow tw = null;

		if (portSlot instanceof StartPortSlot) {
			final IStartEndRequirement req = startEndRequirementProvider.getStartRequirement(resource);
			tw = req.getTimeWindow();
		} else if (portSlot instanceof EndPortSlot) {
			final IStartEndRequirement req = startEndRequirementProvider.getEndRequirement(resource);
			tw = req.getTimeWindow();
		} else {
			tw = portSlot.getTimeWindow();
		}
		return tw;
	}

	private int getLateness(IPortSlot portSlot, IResource resource, ITimeWindow tw, int time) {
		if ((tw != null) && (time > tw.getEnd())) {
			int latenessInHours = time - tw.getEnd();
			return latenessInHours;
		}
		return 0;
	}

	private int getLatenessWithoutFlex(IPortSlot portSlot, IResource resource, ITimeWindow tw, int time) {
		if ((tw != null) && (time > tw.getEndWithoutFlex())) {
			int latenessInHours = time - tw.getEndWithoutFlex();
			return latenessInHours;
		}
		return 0;
	}

	private Pair<ILatenessComponentParameters.Interval, Long> getWeightedLateness(ITimeWindow tw, int latenessInHours) {
		ILatenessComponentParameters.Interval interval = Interval.BEYOND;
		long weightedLateness;
		if (tw.getStart() < promptPeriodProvider.getEndOfPromptPeriod()) {
			interval = Interval.PROMPT;
		} else if (tw.getStart() < (promptPeriodProvider.getEndOfPromptPeriod() + 90 * 24)) {
			interval = Interval.MID_TERM;
		}

		if (latenessInHours < latenessParameters.getThreshold(interval)) {
			// Hit low penalty value
			weightedLateness = (long) latenessParameters.getLowWeight(interval) * (long) latenessInHours;
		} else {
			weightedLateness = ((long) latenessParameters.getLowWeight(interval) * (long) latenessParameters.getThreshold(interval)) + (long) latenessParameters.getHighWeight(interval) * ((long) latenessInHours - (long) latenessParameters.getThreshold(interval));
		}
		
		return new Pair<>(interval, weightedLateness);

	}

	private void addEntryToLatenessAnnotation(@Nullable final IAnnotatedSolution annotatedSolution, final IPortSlot portSlot, final ITimeWindow tw, final Interval interval, final int latenessInHours,
			final int latenessInHoursWithoutFlex, final long weightedLateness, final ScheduledSequences scheduledSequences) {
		// Set port details entry
		scheduledSequences.addWeightedLatenessCost(portSlot, weightedLateness);
		scheduledSequences.addLatenessCost(portSlot, new Pair<>(interval, (long) latenessInHours));

		if (annotatedSolution != null && latenessInHoursWithoutFlex > 0) {
			// set interval without flex
			Interval intervalWithoutFlex = getWeightedLateness(tw, latenessInHoursWithoutFlex).getFirst();
			// Set annotation
			final ISequenceElement element = portSlotProvider.getElement(portSlot);
			final ILatenessAnnotation annotation = new LatenessAnnotation(latenessInHours, weightedLateness, interval, latenessInHoursWithoutFlex, intervalWithoutFlex);
			annotatedSolution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_latenessInfo, annotation);
			setLatenessAnnotationsOnAnnotatedSolution(annotatedSolution, annotation);
		}
	}

	private void setLatenessAnnotationsOnAnnotatedSolution(@NonNull final IAnnotatedSolution annotatedSolution, @NonNull ILatenessAnnotation latenessAnnotation) {
		int lateness = latenessAnnotation.getLateness();
		String key = getKey(latenessAnnotation, lateness);
		// Store specific lateness
		Integer individualLateness = annotatedSolution.getGeneralAnnotation(key, Integer.class);
		if (individualLateness == null) {
			individualLateness = 0;
		}
		individualLateness += lateness;
		annotatedSolution.setGeneralAnnotation(key, individualLateness);
		
		// Store total lateness
		Integer totalLateness = annotatedSolution.getGeneralAnnotation(GA_TOTAL_LATENESS_IN_HOURS, Integer.class);
		if (totalLateness == null) {
			totalLateness = 0;
		}
		totalLateness += lateness;
		annotatedSolution.setGeneralAnnotation(GA_TOTAL_LATENESS_IN_HOURS, totalLateness);
	}

	private String getKey(ILatenessAnnotation latenessAnnotation, int lateness) {
		if (latenessAnnotation.getInterval() == Interval.PROMPT) {
			if (lateness < latenessParameters.getThreshold(Interval.PROMPT)) {
				return GA_TOTAL_PROMPT_LATENESS_LOW_IN_HOURS;
			} else {
				return GA_TOTAL_PROMPT_LATENESS_HIGH_IN_HOURS;
			}
		} else if (latenessAnnotation.getInterval() == Interval.MID_TERM) {
			if (lateness < latenessParameters.getThreshold(Interval.MID_TERM)) {
				return GA_TOTAL_MIDTERM_LATENESS_LOW_IN_HOURS;
			} else {
				return GA_TOTAL_MIDTERM_LATENESS_HIGH_IN_HOURS;
			}
		} else if (latenessAnnotation.getInterval() == Interval.BEYOND) {
			if (lateness < latenessParameters.getThreshold(Interval.BEYOND)) {
				return GA_TOTAL_BEYOND_LATENESS_LOW_IN_HOURS;
			} else {
				return GA_TOTAL_BEYOND_LATENESS_HIGH_IN_HOURS;
			}
		}
		return null;
	}

}
