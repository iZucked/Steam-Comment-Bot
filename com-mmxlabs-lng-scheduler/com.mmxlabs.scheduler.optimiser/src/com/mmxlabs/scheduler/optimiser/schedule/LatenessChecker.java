/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.impl.IEndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
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

	public static final @NonNull String GA_TOTAL_LATENESS_IN_HOURS = "total-lateness-hours";
	public static final @NonNull String GA_TOTAL_PROMPT_LATENESS_LOW_IN_HOURS = "total-prompt-low-lateness-hours";
	public static final @NonNull String GA_TOTAL_PROMPT_LATENESS_HIGH_IN_HOURS = "total-prompt-high-lateness-hours";
	public static final @NonNull String GA_TOTAL_MIDTERM_LATENESS_LOW_IN_HOURS = "total-midterm-low-lateness-hours";
	public static final @NonNull String GA_TOTAL_MIDTERM_LATENESS_HIGH_IN_HOURS = "total-midterm-high-lateness-hours";
	public static final @NonNull String GA_TOTAL_BEYOND_LATENESS_LOW_IN_HOURS = "total-beyond-low-lateness-hours";
	public static final @NonNull String GA_TOTAL_BEYOND_LATENESS_HIGH_IN_HOURS = "total-beyond-high-lateness-hours";

	/**
	 * TODO: Break out into separate class Calculate the various capacity violation check - min/max load & discharge volumes, max heel, vessel capacity and cooldown. Note the {@link IVolumeAllocator}
	 * and {@link LNGVoyageCalculator} generally feed into these checks.
	 * 
	 * @param sequences
	 * @param profitAndLossSequences
	 * @param allocations
	 * @param annotatedSolution
	 */
	public void calculateLateness(final @NonNull VolumeAllocatedSequence volumeAllocatedSequence, @Nullable final IAnnotatedSolution annotatedSolution) {
		final IResource resource = volumeAllocatedSequence.getResource();
		assert resource != null;
		for (final IPortSlot portSlot : volumeAllocatedSequence.getSequenceSlots()) {
			final ITimeWindow tw = getTW(portSlot, resource);
			if (tw == null) {
				continue;
			}

			// Where in the scenario does the lateness occur?
			final Interval interval = getInterval(tw);

			// Used by fitness constraint
			final int latenessWithFlexInHours = getLatenessWithFlex(portSlot, tw, volumeAllocatedSequence.getArrivalTime(portSlot));

			// Used only for export
			final int latenessWithoutFlexInHours = getLatenessWithoutFlex(portSlot, tw, volumeAllocatedSequence.getArrivalTime(portSlot));

			// For fitness component
			final long weightedLateness = getWeightedLateness(interval, latenessWithFlexInHours);

			
			if (latenessWithFlexInHours != 0 || weightedLateness != 0 || latenessWithoutFlexInHours != 0) {
				volumeAllocatedSequence.addLateness(portSlot, weightedLateness, interval, latenessWithFlexInHours, latenessWithoutFlexInHours);
			}

			if (annotatedSolution != null) {
				final ILatenessAnnotation annotation = new LatenessAnnotation(latenessWithFlexInHours, weightedLateness, interval, latenessWithoutFlexInHours, interval);
				annotatedSolution.getElementAnnotations().setAnnotation(portSlotProvider.getElement(portSlot), SchedulerConstants.AI_latenessInfo, annotation);
				setLatenessAnnotationsOnAnnotatedSolution(annotatedSolution, annotation);
			}
		}
	}

	private @Nullable ITimeWindow getTW(@NonNull final IPortSlot portSlot, @NonNull final IResource resource) {
		ITimeWindow tw = null;

		if (portSlot instanceof StartPortSlot) {
			final IStartEndRequirement req = startEndRequirementProvider.getStartRequirement(resource);
			if (req.hasTimeRequirement()) {
				tw = req.getTimeWindow();
			}
		} else if (portSlot instanceof IEndPortSlot) {
			final IStartEndRequirement req = startEndRequirementProvider.getEndRequirement(resource);
			if (req.hasTimeRequirement()) {
				tw = req.getTimeWindow();
			}
		} else {
			tw = portSlot.getTimeWindow();
		}
		return tw;
	}

	private int getLatenessWithFlex(@NonNull final IPortSlot portSlot, @Nullable final ITimeWindow tw, final int time) {
		if ((tw != null) && (time >= tw.getExclusiveEnd())) {
			final int latenessInHours = time - tw.getExclusiveEnd() + 1;
			return latenessInHours;
		}
		return 0;
	}

	private int getLatenessWithoutFlex(@NonNull final IPortSlot portSlot, @Nullable final ITimeWindow tw, final int time) {
		if ((tw != null) && (time >= tw.getExclusiveEndWithoutFlex())) {
			final int latenessInHours = time - tw.getExclusiveEndWithoutFlex() + 1;
			return latenessInHours;
		}
		return 0;
	}

	private @NonNull Interval getInterval(@NonNull final ITimeWindow tw) {
		ILatenessComponentParameters.@NonNull Interval interval = Interval.BEYOND;
		if (tw.getInclusiveStart() < promptPeriodProvider.getEndOfPromptPeriod()) {
			interval = Interval.PROMPT;
		} else if (tw.getInclusiveStart() < (promptPeriodProvider.getEndOfPromptPeriod() + 90 * 24)) {
			interval = Interval.MID_TERM;
		}
		return interval;
	}

	private long getWeightedLateness(@NonNull final Interval interval, final int latenessInHours) {
		long weightedLateness;
		if (latenessInHours < latenessParameters.getThreshold(interval)) {
			// Hit low penalty value
			weightedLateness = (long) latenessParameters.getLowWeight(interval) * (long) latenessInHours;
		} else {
			weightedLateness = ((long) latenessParameters.getLowWeight(interval) * (long) latenessParameters.getThreshold(interval))
					+ (long) latenessParameters.getHighWeight(interval) * ((long) latenessInHours - (long) latenessParameters.getThreshold(interval));
		}

		return weightedLateness;

	}

	// private void addEntryToLatenessAnnotation(final @NonNull IPortSlot portSlot, final @NonNull ITimeWindow tw, final int latenessInHours,
	// final @NonNull VolumeAllocatedSequence volumeAllocatedSequence, @Nullable final IAnnotatedSolution annotatedSolution) {
	// // Set port details entry
	//
	// final Pair<ILatenessComponentParameters.Interval, Long> weightedLatenessPair = getWeightedLateness(tw, latenessInHours);
	// final Interval interval = weightedLatenessPair.getFirst();
	// final long weightedLateness = weightedLatenessPair.getSecond();
	// final int latenessInHoursWithoutFlex = getLatenessWithoutFlex(portSlot, tw, volumeAllocatedSequence.getArrivalTime(portSlot));
	// final Interval intervalWithoutFlex = getWeightedLateness(tw, latenessInHoursWithoutFlex).getFirst();
	//
	// volumeAllocatedSequence.addLateness(portSlot, weightedLateness, interval, latenessInHours, latenessInHoursWithoutFlex, intervalWithoutFlex);
	//
	// if (annotatedSolution != null) {
	// final ILatenessAnnotation annotation = new LatenessAnnotation(latenessInHours, weightedLateness, interval, latenessInHoursWithoutFlex, intervalWithoutFlex);
	// annotatedSolution.getElementAnnotations().setAnnotation(portSlotProvider.getElement(portSlot), SchedulerConstants.AI_latenessInfo, annotation);
	// setLatenessAnnotationsOnAnnotatedSolution(annotatedSolution, annotation);
	// }
	// }

	private void setLatenessAnnotationsOnAnnotatedSolution(@NonNull final IAnnotatedSolution annotatedSolution, @NonNull final ILatenessAnnotation latenessAnnotation) {
		final int lateness = latenessAnnotation.getLateness();
		final String key = getKey(latenessAnnotation, lateness);
		if (key == null) {
			return;
		}
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

	@Nullable
	private String getKey(final @NonNull ILatenessAnnotation latenessAnnotation, final int lateness) {
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
