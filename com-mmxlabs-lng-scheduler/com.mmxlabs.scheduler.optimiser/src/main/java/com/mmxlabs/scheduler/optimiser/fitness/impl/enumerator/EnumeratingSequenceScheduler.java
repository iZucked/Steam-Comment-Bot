/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.fitness.impl.AbstractSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * A sequence scheduler which enumerates possible combinations of arrival times
 * explicitly, rather than using the GA byte array decoding method. This should
 * be subclassable into a random sequence scheduler as well, with reduced
 * decoding overhead
 * 
 * (C) Minimax labs inc. 2010
 * 
 * @author hinton
 * 
 */
public class EnumeratingSequenceScheduler<T> extends
		AbstractSequenceScheduler<T> {
	private static final int EMPTY_WINDOW_SIZE = 24 * 2;

	/**
	 * Tracks the number of evaluations done so far
	 */
	protected int count = 0;

	protected int[] arrivalTimes;
	/**
	 * The start times of each window, appropriately `clipped' to deal with
	 * infeasible choices or null time windows
	 */
	private int[] windowStartTime;
	/**
	 * The end times of each window, similar to start times.
	 */
	private int[] windowEndTime;
	/**
	 * The minimum time this vessel can take to get from the indexed element to
	 * its successor. i.e. min travel time + visit time at indexed element.
	 */
	private int[] minTimeToNextElement;

	// private ITimeWindowDataComponentProvider timeWindowProvider;
	// private IElementDurationProvider<T> durationProvider;
	// private IVesselProvider vesselProvider;
	// private IMultiMatrixProvider<T, Integer> distanceProvider;
	//
	private IResource resource;
	protected ISequence<T> sequence;

	private ScheduleEvaluator<T> evaluator;
	private long bestValue;
	private Pair<Integer, List<VoyagePlan>> bestResult;

	private int[] maxTimeToNextElement;

	public EnumeratingSequenceScheduler() {
		super();

		createLog();

	}

	@Override
	public Pair<Integer, List<VoyagePlan>> schedule(final IResource resource,
			final ISequence<T> sequence) {

		resetBest();
		setResourceAndSequence(resource, sequence);

		startLogEntry(sequence.size());

		prepare(Long.MAX_VALUE);
		enumerate(0);

		endLogEntry();

		return bestResult;
	}

	protected final Pair<Integer, List<VoyagePlan>> getBestResult() {
		return bestResult;
	}

	protected final void resetBest() {
		this.bestResult = null;
		this.bestValue = Long.MAX_VALUE;
		count = 0;
	}

	protected final void setResourceAndSequence(IResource resource,
			ISequence<T> sequence) {
		this.resource = resource;
		this.sequence = sequence;
		evaluator.setResourceAndSequence(resource, sequence);
	}

	/**
	 * Unpack some distance/time/speed information, set up arrays etc
	 * 
	 * @param maxValue
	 * 
	 * @param sequence
	 * @return
	 */
	protected final long prepare(final long maxValue) {
		{
			final int size = sequence.size();
			arrivalTimes = new int[size];
			windowStartTime = new int[size];
			windowEndTime = new int[size];
			minTimeToNextElement = new int[size];
			maxTimeToNextElement = new int[size];
		}
		final IVesselProvider vesselProvider = super.getVesselProvider();
		final ITimeWindowDataComponentProvider timeWindowProvider = super
				.getTimeWindowProvider();
		final IPortProvider portProvider = super.getPortProvider();
		final IMultiMatrixProvider<IPort, Integer> distanceProvider = super
				.getDistanceProvider();
		final IElementDurationProvider<T> durationProvider = super
				.getDurationsProvider();

		final int maxSpeed = vesselProvider.getVessel(resource)
				.getVesselClass().getMaxSpeed();

		final int minSpeed = vesselProvider.getVessel(resource)
				.getVesselClass().getMinSpeed();

		int index = 0;
		T lastElement = null;

		// first pass, collecting start time windows
		for (final T element : sequence) {
			List<ITimeWindow> windows = timeWindowProvider
					.getTimeWindows(element);

			// Calculate minimum inter-element durations
			maxTimeToNextElement[index] = minTimeToNextElement[index] = durationProvider
					.getElementDuration(element, resource);

			if (lastElement != null) {
				final IPort lastPort = portProvider
						.getPortForElement(lastElement);
				final IPort port = portProvider.getPortForElement(element);

				final int minDistance = distanceProvider.getMinimumValue(
						lastPort, port);

				final int maxDistance = minDistance; // TODO this is incorrect

				final int minTravelTime = Calculator.getTimeFromSpeedDistance(
						maxSpeed, minDistance);

				final int maxTravelTime = Calculator.getTimeFromRateQuantity(
						minSpeed, maxDistance);

				minTimeToNextElement[index - 1] += minTravelTime;
				maxTimeToNextElement[index - 1] += maxTravelTime;
			}

			// Handle time windows
			if (windows.isEmpty()) { // empty time windows are made to be the
										// biggest reasonable gap
				if (index > 0) {
					// clip start of time window
					windowStartTime[index] = windowStartTime[index - 1]
							+ minTimeToNextElement[index - 1];
					// backwards pass will fix this.
					windowEndTime[index] = windowStartTime[index]
							+ EMPTY_WINDOW_SIZE;
				} else {
					windowStartTime[index] = 0;
					windowEndTime[index] = sequence.size() == 1 ? 0
							: EMPTY_WINDOW_SIZE;
				}
			} else {
				assert windows.size() == 1 : "Multiple time windows are not yet supported!";
				final ITimeWindow window = windows.get(0);
				if (index == 0) {// first time window is special
					windowStartTime[index] = window.getStart();
					windowEndTime[index] = window.getEnd();
				} else {
					// subsequent time windows have their start time clipped, so
					// they don't start any earlier
					// than you could get to them without being late.
					windowEndTime[index] = window.getEnd();
					windowStartTime[index] = Math.min(windowEndTime[index],
							Math.max(window.getStart(),
									windowStartTime[index - 1]
											+ minTimeToNextElement[index - 1]));
				}
			}

			index++;
			lastElement = element;
		}

		// now perform reverse-pass to trim any overly late end times
		// (that is end times which would make us late at the next element)
		for (index = arrivalTimes.length - 2; index >= 0; index--) {
			// trim the end of this time window so that the next element is
			// reachable without lateness
			// (but never so that the end time is before the start time)
			windowEndTime[index] = Math.max(
					windowStartTime[index],
					Math.min(windowEndTime[index], windowEndTime[index + 1]
							- minTimeToNextElement[index]));
		}

		// find possible separation points
		int separableElementCount = 0;
		for (index = 1; index < arrivalTimes.length; index++) {
			// if there's an edge where window start > prev. window end + max
			// travel time,
			// that edge is separable, and it should be possible to solve from
			// here independently
			// of what has gone before

			final int latestArrivalTime = windowEndTime[index - 1]
					+ maxTimeToNextElement[index - 1];
			if (latestArrivalTime < windowStartTime[index]) {
				separableElementCount++;
			}
		}

		System.err.println(separableElementCount + " separable elements");
		
		long approximateCombinations = 1;
		for (index = 0; index < arrivalTimes.length; index++) {
			approximateCombinations *= windowEndTime[index]
					- windowStartTime[index] + 1;
			if (approximateCombinations >= maxValue)
				break;
		}
		return approximateCombinations;
	}

	/**
	 * Recursively enumerate all the possibilities for arrival times from the
	 * given index (inclusive), evaluating each one and keeping the best.
	 * 
	 * A randomised subclass could override this and take a random decision at
	 * each step, until it has evaluated a certain number of possibilities.
	 * 
	 * @param index
	 */
	protected void enumerate(final int index) {
		if (index == arrivalTimes.length) {
			evaluate();
			return;
		}

		final int min = getMinArrivalTime(index);
		final int max = getMaxArrivalTime(index);

		for (int time = min; time <= max; time++) {
			arrivalTimes[index] = time;
			enumerate(index + 1);
		}
	}

	/**
	 * Evaluate the current arrival times array, and if it is the best solution
	 * so far take a copy of the result. This demands some fitness components.
	 */
	protected void evaluate() {
		count++;
		final Pair<Integer, List<VoyagePlan>> startTimeAndPlans = super
				.schedule(resource, sequence, arrivalTimes);

		final long value = evaluator.evaluateVoyagePlans(startTimeAndPlans);

		logValue(value);

		if (value < bestValue) {
			// if (bestValue == Long.MAX_VALUE) {
			// initialValue = value;
			// }
			bestValue = value;
			bestResult = startTimeAndPlans;
			// System.err.println(String.format("%.2f%% gain at %d (%s)", (100.0
			// * (initialValue - bestValue))/initialValue, count,
			// Long.toString(bestValue)));
		}
	}

	/**
	 * Gets the earliest time at which the current vessel can arrive at the
	 * given element, given the arrival times set for the previous elements.
	 * 
	 * @param index
	 * @return
	 */
	protected final int getMinArrivalTime(final int index) {
		if (index == 0) {
			return windowStartTime[index];
		} else {
			// whichever is later: previous arrival time + travel, or
			// window start.
			return Math.max(arrivalTimes[index - 1]
					+ minTimeToNextElement[index - 1], windowStartTime[index]);
		}
	}

	/**
	 * Gets the latest time at which the current vessel can arrive at the given
	 * element, given the arrival times set for the previous elements.
	 * 
	 * Essentially this is max(getMinArrivalTime(index), endTimeWindow[index]).
	 * 
	 * @param index
	 * @return
	 */
	protected final int getMaxArrivalTime(final int index) {
		return Math.max(getMinArrivalTime(index), // the latest we can arrive
													// here is either window end
													// time, or if we're late
													// clamp to the earliest.
				windowEndTime[index]);
	}

	public ScheduleEvaluator<T> getScheduleEvaluator() {
		return evaluator;
	}

	public void setScheduleEvaluator(ScheduleEvaluator<T> evaluator) {
		this.evaluator = evaluator;
	}
}
