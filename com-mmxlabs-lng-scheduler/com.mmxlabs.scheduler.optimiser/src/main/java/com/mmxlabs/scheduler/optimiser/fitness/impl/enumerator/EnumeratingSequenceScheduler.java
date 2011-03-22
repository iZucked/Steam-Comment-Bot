/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;

import java.util.List;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider.MatrixEntry;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.impl.AbstractSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

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
	/**
	 * How long to let empty time windows be. Since these mostly happen at the
	 * end of sequences we make this zero.
	 */
	private static final int EMPTY_WINDOW_SIZE = 0;

	/**
	 * Tracks the number of evaluations done so far
	 */
	protected int count = 0;

	/**
	 * Indicates how many evaluations the best value came after
	 */
	protected int bestIndex = 0;

	protected int[][] arrivalTimes;
	/**
	 * The start times of each window, appropriately `clipped' to deal with
	 * infeasible choices or null time windows
	 */
	private int[][] windowStartTime;
	/**
	 * The end times of each window, similar to start times.
	 */
	private int[][] windowEndTime;
	/**
	 * The minimum time this vessel can take to get from the indexed element to
	 * its successor. i.e. min travel time + visit time at indexed element.
	 */
	private int[][] minTimeToNextElement;
	/**
	 * The maximum time to get from the indexed element to its successor. This
	 * is the maximum travel time + visit time at this element
	 */
	private int[][] maxTimeToNextElement;

	/**
	 * Holds a list of points at which the cost function can be separated. This
	 * occurs when a given journey leg <em>always</em> involves some idle time,
	 * so there can be no knock-on effects on the segment following the point
	 * from the times chosen up to the point.
	 * 
	 * These are the indexes of the sequence elements at the <em>start</em> of
	 * such legs. TODO this is disabled
	 */
	// protected final ArrayList<Integer> separationPoints = new
	// ArrayList<Integer>();

	// private ITimeWindowDataComponentProvider timeWindowProvider;
	// private IElementDurationProvider<T> durationProvider;
	// private IVesselProvider vesselProvider;
	// private IMultiMatrixProvider<T, Integer> distanceProvider;
	//

	/**
	 * The current sequences
	 */
	private ISequences<T> sequences;

	private ScheduleEvaluator<T> evaluator;
	private long bestValue;
	private ScheduledSequences bestResult;

	/**
	 * Contains the last valid value calculated by evaluate(). TODO this is
	 * ugly.
	 */
	private long lastValue;

	public EnumeratingSequenceScheduler() {
		super();

		createLog();

	}

	@Override
	public ScheduledSequences schedule(final ISequences<T> sequences) {

		resetBest();

		startLogEntry(1);

		prepare(Long.MAX_VALUE);
		enumerate(0, 0);

		endLogEntry();

		return bestResult;
	}

	protected final ScheduledSequences getBestResult() {
		return bestResult;
	}

	protected final void resetBest() {
		this.bestResult = null;
		this.bestValue = Long.MAX_VALUE;
		this.lastValue = Long.MAX_VALUE;
		count = 0;
	}

	protected final void setSequences(final ISequences<T> sequences) {
		this.sequences = sequences;
	}

	protected final long prepare(final long limit) {
		long v = 1;

		final int size = sequences.size();

		arrivalTimes = new int[size][];
		windowStartTime = new int[size][];
		windowEndTime = new int[size][];
		minTimeToNextElement = new int[size][];
		maxTimeToNextElement = new int[size][];

		for (int i = 0; i < size; i++) {
			v *= prepare(i, limit);
		}
		return limit;
	}

	/**
	 * Unpack some distance/time/speed information, set up arrays etc
	 * 
	 * @param maxValue
	 * 
	 * @param sequence
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected final long prepare(final int sequenceIndex, final long maxValue) {
		final ISequence<T> sequence = sequences.getSequence(sequenceIndex);
		final IResource resource = sequences.getResources().get(sequenceIndex);

		final int size = sequence.size();
		// arrivalTimes[sequenceIndex] = new int[size];
		// windowStartTime[sequenceIndex] = new int[size];
		// windowEndTime[sequenceIndex] = new int[size];
		// minTimeToNextElement[sequenceIndex] = new int[size];
		// maxTimeToNextElement[sequenceIndex] = new int[size];
		// // separationPoints[index].clear();

		final int[] arrivalTimes = this.arrivalTimes[sequenceIndex] = new int[size];
		final int[] windowStartTime = this.windowStartTime[sequenceIndex] = new int[size];
		final int[] windowEndTime = this.windowEndTime[sequenceIndex] = new int[size];
		final int[] minTimeToNextElement = this.minTimeToNextElement[sequenceIndex] = new int[size];
		final int[] maxTimeToNextElement = this.maxTimeToNextElement[sequenceIndex] = new int[size];

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

				final MatrixEntry<IPort, Integer> minDistance = distanceProvider
						.getMinimum(lastPort, port);

				final MatrixEntry<IPort, Integer> maxDistance = distanceProvider
						.getMaximum(lastPort, port);

				final int minTravelTime = Calculator.getTimeFromSpeedDistance(
						maxSpeed, minDistance.getValue())
						+ routeCostProvider.getRouteTransitTime(minDistance
								.getKey(), vesselProvider.getVessel(resource)
								.getVesselClass());

				final int maxTravelTime = Calculator.getTimeFromRateQuantity(
						minSpeed, maxDistance.getValue())
						+ routeCostProvider.getRouteTransitTime(minDistance
								.getKey(), vesselProvider.getVessel(resource)
								.getVesselClass());

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

		// Compute separation points
		// TODO fix this code for multi-route scheduler
		// for (index = 1; index < arrivalTimes.length; index++) {
		// // if there's an edge where window start > prev. window end + max
		// // travel time,
		// // that edge is separable, and it should be possible to solve from
		// // here independently
		// // of what has gone before
		//
		// final int latestArrivalTime = windowEndTime[index - 1]
		// + maxTimeToNextElement[index - 1];
		// if (latestArrivalTime < windowStartTime[index]) {
		// separationPoints.add(index - 1);
		// }
		// }
		//
		// separationPoints.add(arrivalTimes.length - 1);

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
	protected void enumerate(final int seq, final int index) {
		if (seq == arrivalTimes.length && index == arrivalTimes[0].length) {
			evaluate();
			return;
		} else if (seq == arrivalTimes.length) {
			enumerate(seq + 1, 0);
			return;
		}

		final int min = getMinArrivalTime(seq, index);
		final int max = getMaxArrivalTime(seq, index);

		for (int time = min; time <= max; time++) {
			arrivalTimes[seq][index] = time;
			enumerate(seq, index + 1);
		}
	}

	/**
	 * Use {@link #evaluator} to evaluate the current arrival times array, and
	 * if it is the best solution so far take a copy of the result.
	 * 
	 * @return true if the result is the new best case, false otherwise
	 */
	protected boolean evaluate() {
		count++;

		final ScheduledSequences scheduledSequences = super.schedule(sequences,
				arrivalTimes);

		if (scheduledSequences == null)
			return false;

		lastValue = evaluator.evaluateSchedule(scheduledSequences);

		logValue(lastValue);

		if (lastValue < bestValue) {
			bestIndex = count;
			// if (bestValue == Long.MAX_VALUE) {
			// initialValue = value;
			// }
			bestValue = lastValue;
			bestResult = scheduledSequences;
			// System.err.println(String.format("%.2f%% gain at %d (%s)", (100.0
			// * (initialValue - bestValue))/initialValue, count,
			// Long.toString(bestValue)));
			return true;
		}
		return false;
	}

	/**
	 * Gets the earliest time at which the current vessel can arrive at the
	 * given element, given the arrival times set for the previous elements.
	 * 
	 * @param index
	 * @return
	 */
	protected final int getMinArrivalTime(final int seq, final int index) {
		if (index == 0) {
			return windowStartTime[seq][index];
		} else {
			// whichever is later: previous arrival time + travel, or
			// window start.
			return Math.max(arrivalTimes[seq][index - 1]
					+ minTimeToNextElement[seq][index - 1],
					windowStartTime[seq][index]);
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
	protected final int getMaxArrivalTime(final int seq, final int index) {
		return Math.max(getMinArrivalTime(seq, index), // the latest we can
														// arrive
														// here is either window
														// end
														// time, or if we're
														// late
														// clamp to the
														// earliest.
				windowEndTime[seq][index]);
	}

	public ScheduleEvaluator<T> getScheduleEvaluator() {
		return evaluator;
	}

	public void setScheduleEvaluator(ScheduleEvaluator<T> evaluator) {
		this.evaluator = evaluator;
	}

	/**
	 * Get the approximate number of combinations of arrival times for elements
	 * from firstIndex to lastIndex inclusive, up to maxValue
	 * 
	 * @param firstIndex
	 *            first index to look at
	 * @param lastIndex
	 *            last index to look at
	 * @param maxValue
	 *            the maximum return value
	 * @return
	 */
	protected final long getApproximateCombinations(final int seq,
			final int firstIndex, final int lastIndex, final long maxValue) {
		long accumulator = 1;
		for (int i = firstIndex; i <= lastIndex; i++) {
			accumulator *= (windowEndTime[seq][i] - windowStartTime[seq][i] + 1);
			if (accumulator > maxValue)
				return maxValue;
		}
		return accumulator;
	}

	public long getLastValue() {
		return lastValue;
	}
}
