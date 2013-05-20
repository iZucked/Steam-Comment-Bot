/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.MatrixEntry;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.impl.AbstractSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * A sequence scheduler which enumerates possible combinations of arrival times explicitly, rather than using the GA byte array decoding method. This should be subclassable into a random sequence
 * scheduler as well, with reduced decoding overhead
 * 
 * (C) Minimax labs inc. 2010
 * 
 * @author hinton
 * 
 */
public class EnumeratingSequenceScheduler extends AbstractSequenceScheduler {
	/**
	 * How long to let empty time windows be. Since these mostly happen at the end of sequences we make this zero.
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

	/**
	 * The output of the scheduler; these are the arrival times for each element in each sequence.
	 * 
	 */
	protected int[][] arrivalTimes;
	/**
	 * The start times of each window, appropriately `clipped' to deal with infeasible choices or null time windows.
	 */
	private int[][] windowStartTime;
	/**
	 * The end times of each window, similar to start times.
	 */
	private int[][] windowEndTime;
	/**
	 * The minimum time this vessel can take to get from the indexed element to its successor. i.e. min travel time + visit time at indexed element.
	 */
	private int[][] minTimeToNextElement;
	/**
	 * The maximum time to get from the indexed element to its successor. This is the maximum travel time + visit time at this element
	 */
	private int[][] maxTimeToNextElement;

	/**
	 * The number of elements in each array.
	 */
	protected int[] sizes;

	/**
	 * Whether or not the {@link PortType} of any {@link PortSlot} associated with each sequence element has {@link PortType} Virtual. If a sequence element is virtual, the travel times should be held
	 * at zero and the virtual element and its neighbors should have the same time window.
	 * 
	 * Thus if we see A -> virtual -> B, the arrival time at B should be clamped to the arrival time at A. This is done by checks in the {@link #getMinArrivalTime(int, int)} and
	 * {@link #getMaxArrivalTime(int, int)} methods.
	 */
	private boolean[][] isVirtual;

	/**
	 * A flag to indicate that we should just use the timewindow rather than include the previous journey time. Intended for use with the cargo shorts where each cargo is indepenedent of the others on
	 * the route.
	 */
	private boolean[][] useTimeWindow;

	/**
	 * Holds a list of points at which the cost function can be separated. This occurs when a given journey leg <em>always</em> involves some idle time, so there can be no knock-on effects on the
	 * segment following the point from the times chosen up to the point.
	 * 
	 * These are the indexes of the sequence elements at the <em>start</em> of such legs. TODO this is disabled
	 */
	// protected final ArrayList<Integer> separationPoints = new
	// ArrayList<Integer>();

	// private ITimeWindowDataComponentProvider timeWindowProvider;
	// private IElementDurationProvider durationProvider;
	// private IVesselProvider vesselProvider;
	// private IMultiMatrixProvider<T, Integer> distanceProvider;
	//

	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	/**
	 * Resize one of the integer buffers above
	 */
	private final void resize(final int[][] arrays, final int arrayIndex, final int size) {
		if ((arrays[arrayIndex] == null) || (arrays[arrayIndex].length < (size))) {
			arrays[arrayIndex] = new int[(size)];
		}
	}

	/**
	 * Resize one of the boolean buffers above.
	 */
	private void resize(final boolean[][] arrays, final int arrayIndex, final int size) {
		if ((arrays[arrayIndex] == null) || (arrays[arrayIndex].length < (size))) {
			arrays[arrayIndex] = new boolean[(size)];
		}
	}

	/**
	 * Resize all the integer buffers for a given route
	 * 
	 * @param arrayIndex
	 * @param size
	 */
	private final void resizeAll(final int sequenceIndex, final int size) {
		resize(arrivalTimes, sequenceIndex, size);
		resize(windowStartTime, sequenceIndex, size);
		resize(windowEndTime, sequenceIndex, size);
		resize(minTimeToNextElement, sequenceIndex, size);
		resize(maxTimeToNextElement, sequenceIndex, size);

		resize(isVirtual, sequenceIndex, size);
		resize(useTimeWindow, sequenceIndex, size);

		sizes[sequenceIndex] = size;
	}

	/**
	 * The sequences being evaluated at the moment
	 */
	private ISequences sequences;

	private ScheduleFitnessEvaluator evaluator;

	/**
	 * the fitness of the best result in the cycle
	 */
	private long bestValue;
	/**
	 * the best result in this cycle, or null if we have just started a cycle
	 */
	private ScheduledSequences bestResult;

	/**
	 * True if the last time we called evaluate() we evaluated the best result in this cycle.
	 */
	private boolean lastEvaluationWasBestResult = false;

	/**
	 * The last result which was accepted externally, for the purposes of doing partial evaluations.
	 */
	private ScheduledSequences lastAcceptedResult = null;

	/**
	 * Contains the last valid value calculated by evaluate(). TODO this is ugly.
	 */
	private long lastValue;

	private final TimeWindow defaultStartWindow = new TimeWindow(0, Integer.MAX_VALUE);

	public EnumeratingSequenceScheduler() {
		super();

		createLog();
	}

	@Override
	public ScheduledSequences schedule(final ISequences sequences, final IAnnotatedSolution solution) {
		return schedule(sequences, sequences.getResources(), solution);
	}

	@Override
	public ScheduledSequences schedule(final ISequences sequences, final Collection<IResource> affectedResources, final IAnnotatedSolution solution) {
		setSequences(sequences);
		resetBest();

		startLogEntry(1);
		prepare(getResourceIndices(sequences, affectedResources));
		enumerate(0, 0);
		endLogEntry();

		return reEvaluateAndGetBestResult(solution);
	}

	/**
	 * @param sequences
	 * @param affectedResources
	 * @return
	 */
	protected int[] getResourceIndices(final ISequences sequences, final Collection<IResource> affectedResources) {
		final List<IResource> resources = sequences.getResources();

		final int[] affectedIndices = new int[affectedResources.size()];
		int k = 0;
		for (int i = 0; i < resources.size(); i++) {
			if (affectedResources.contains(resources.get(i))) {
				affectedIndices[k++] = i;
			}
		}

		return affectedIndices;
	}

	protected final ScheduledSequences reEvaluateAndGetBestResult(final IAnnotatedSolution solution) {
		if (bestResult == null) {
			return null;
		}
		if (evaluator != null) {
			evaluator.evaluateSchedule(sequences, bestResult, solution);
		}
		return bestResult;
	}

	protected final void resetBest() {
		this.lastEvaluationWasBestResult = false;
		this.bestResult = null;
		this.bestValue = Long.MAX_VALUE;
		this.lastValue = Long.MAX_VALUE;
		count = 0;
	}

	protected final void setSequences(final ISequences sequences) {
		this.sequences = sequences;
	}

	protected final void prepare(final int[] indices) {
		final int size = sequences.size();

		if ((arrivalTimes == null) || (arrivalTimes.length != size)) {
			prepare();
			return;
		}

		for (final int i : indices) {
			// for (int i = 0; i < size; i++) {
			prepare(i);
		}
	}

	protected ScheduledSequences getBestResult() {
		return bestResult;
	}

	protected final void prepare() {
		final int size = sequences.size();

		if ((arrivalTimes == null) || (arrivalTimes.length != size)) {
			arrivalTimes = new int[size][];
			windowStartTime = new int[size][];
			windowEndTime = new int[size][];
			minTimeToNextElement = new int[size][];
			maxTimeToNextElement = new int[size][];
			isVirtual = new boolean[size][];
			useTimeWindow = new boolean[size][];
			sizes = new int[size];
		}

		for (int i = 0; i < size; i++) {
			prepare(i);
		}
	}

	/**
	 * Unpack some distance/time/speed information, set up arrays etc
	 * 
	 * @param maxValue
	 * 
	 * @param sequence
	 * @return
	 */
	protected final void prepare(final int sequenceIndex) {
		final ISequence sequence = sequences.getSequence(sequenceIndex);
		final IResource resource = sequences.getResources().get(sequenceIndex);

		final IVesselProvider vesselProvider = super.getVesselProvider();
		final IVessel vessel = vesselProvider.getVessel(resource);
		if (vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
			return;
		}

		final int size = sequence.size();

		resizeAll(sequenceIndex, size);

		final int[] windowStartTime = this.windowStartTime[sequenceIndex];
		final int[] windowEndTime = this.windowEndTime[sequenceIndex];
		final int[] minTimeToNextElement = this.minTimeToNextElement[sequenceIndex];
		final int[] maxTimeToNextElement = this.maxTimeToNextElement[sequenceIndex];
		final boolean[] isVirtual = this.isVirtual[sequenceIndex];
		final boolean[] useTimeWindow = this.useTimeWindow[sequenceIndex];

		final IPortTypeProvider portTypeProvider = super.getPortTypeProvider();

		final ITimeWindowDataComponentProvider timeWindowProvider = super.getTimeWindowProvider();
		final IPortProvider portProvider = super.getPortProvider();
		final IMultiMatrixProvider<IPort, Integer> distanceProvider = super.getDistanceProvider();
		final IElementDurationProvider durationProvider = super.getDurationsProvider();

		final int maxSpeed = vessel.getVesselClass().getMaxSpeed();

		final int minSpeed = vessel.getVesselClass().getMinSpeed();

		int index = 0;
		ISequenceElement lastElement = null;

		// first pass, collecting start time windows
		for (final ISequenceElement element : sequence) {
			final List<ITimeWindow> windows;
			// Take element start window into account
			if (portTypeProvider.getPortType(element) == PortType.Start) {
				final IStartEndRequirement startRequirement = startEndRequirementProvider.getStartRequirement(resource);
				if (startRequirement != null) {
					final ITimeWindow timeWindow = startRequirement.getTimeWindow();
					if (timeWindow != null) {
						windows = Collections.singletonList(timeWindow);
					} else {
						windows = Collections.<ITimeWindow> singletonList(defaultStartWindow);
					}
				} else {
					windows = Collections.<ITimeWindow> singletonList(defaultStartWindow);
				}
			} else if (portTypeProvider.getPortType(element) == PortType.End) {
				final IStartEndRequirement endRequirement = startEndRequirementProvider.getEndRequirement(resource);
				if (endRequirement != null) {
					final ITimeWindow timeWindow = endRequirement.getTimeWindow();
					if (timeWindow != null) {
						windows = Collections.singletonList(timeWindow);
					} else {
						windows = Collections.<ITimeWindow> emptyList();
					}
				} else {
					windows = Collections.<ITimeWindow> emptyList();
				}
			} else {
				windows = timeWindowProvider.getTimeWindows(element);
			}
			isVirtual[index] = portTypeProvider.getPortType(element) == PortType.Virtual;
			useTimeWindow[index] = lastElement == null ? false : portTypeProvider.getPortType(lastElement) == PortType.Short_Cargo_End;

			// Calculate minimum inter-element durations
			maxTimeToNextElement[index] = minTimeToNextElement[index] = durationProvider.getElementDuration(element, resource);

			if (lastElement != null) {
				final IPort lastPort = portProvider.getPortForElement(lastElement);
				final IPort port = portProvider.getPortForElement(element);

				int minTravelTime = Integer.MAX_VALUE;
				int maxTravelTime = 0;
				for (final MatrixEntry<IPort, Integer> entry : distanceProvider.getValues(lastPort, port)) {
					final int distance = entry.getValue();
					if (distance != Integer.MAX_VALUE) {
						final int extraTime = routeCostProvider.getRouteTransitTime(entry.getKey(), vessel.getVesselClass());
						final int minByRoute = Calculator.getTimeFromSpeedDistance(maxSpeed, distance) + extraTime;
						final int maxByRoute = Calculator.getTimeFromSpeedDistance(minSpeed, distance) + extraTime;
						minTravelTime = Math.min(minTravelTime, minByRoute);
						maxTravelTime = Math.max(maxTravelTime, maxByRoute);
					}
				}

				minTimeToNextElement[index - 1] += minTravelTime;
				maxTimeToNextElement[index - 1] += maxTravelTime;
			}

			// Handle time windows
			if (windows.isEmpty()) { // empty time windows are made to be the
										// biggest reasonable gap
				if (index > 0) {
					// clip start of time window
					windowStartTime[index] = windowStartTime[index - 1] + minTimeToNextElement[index - 1];
					// backwards pass will fix this.
					windowEndTime[index] = windowStartTime[index] + EMPTY_WINDOW_SIZE;
				} else {
					windowStartTime[index] = 0;
					windowEndTime[index] = sequence.size() == 1 ? 0 : EMPTY_WINDOW_SIZE;
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
					if (useTimeWindow[index]) {
						// Cargo shorts - pretend this is a start element
						windowStartTime[index] = window.getStart();
					} else {
						windowStartTime[index] = Math.min(windowEndTime[index], Math.max(window.getStart(), windowStartTime[index - 1] + minTimeToNextElement[index - 1]));
					}
				}
			}

			index++;
			lastElement = element;
		}

		// now perform reverse-pass to trim any overly late end times
		// (that is end times which would make us late at the next element)
		for (index = size - 2; index >= 0; index--) {
			// trim the end of this time window so that the next element is
			// reachable without lateness
			// (but never so that the end time is before the start time)
			if (!useTimeWindow[index + 1]) {
				windowEndTime[index] = Math.max(windowStartTime[index], Math.min(windowEndTime[index], windowEndTime[index + 1] - minTimeToNextElement[index]));
			}
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
	}

	/**
	 * Recursively enumerate all the possibilities for arrival times from the given index (inclusive), evaluating each one and keeping the best.
	 * 
	 * A randomised subclass could override this and take a random decision at each step, until it has evaluated a certain number of possibilities.
	 * 
	 * @param index
	 */
	protected void enumerate(final int seq, final int index) {
		if ((seq == arrivalTimes.length) && (index < sizes[seq])) {
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
	 * Use {@link #evaluator} to evaluate the current arrival times array, and if it is the best solution so far take a copy of the result.
	 * 
	 * @return true if the result is the new best case, false otherwise
	 */
	protected boolean evaluate() {
		return evaluate(getResourceIndices(sequences, sequences.getResources()));
		// count++;
		//
		// final ScheduledSequences scheduledSequences = super.schedule(sequences, arrivalTimes);
		//
		// if (scheduledSequences == null)
		// return false;
		//
		// lastValue = evaluator.evaluateSchedule(scheduledSequences);
		//
		// logValue(lastValue);
		//
		// if (lastValue < bestValue) {
		// lastEvaluationWasBestResult = true;
		// bestIndex = count;
		// // if (bestValue == Long.MAX_VALUE) {
		// // initialValue = value;
		// // }
		// bestValue = lastValue;
		// bestResult = scheduledSequences;
		// // System.err.println(String.format("%.2f%% gain at %d (%s)", (100.0
		// // * (initialValue - bestValue))/initialValue, count,
		// // Long.toString(bestValue)));
		// return true;
		// } else {
		// lastEvaluationWasBestResult = false;
		// }
		// return false;
	}

	@Override
	public void acceptLastSchedule() {
		lastAcceptedResult = bestResult;
	}

	protected boolean evaluate(int[] changedSequences) {
		if (lastAcceptedResult == null) {
			changedSequences = getResourceIndices(sequences, sequences.getResources());
		}

		count++;

		final List<IResource> resources = sequences.getResources();

		final ScheduledSequences scheduledSequences = new ScheduledSequences();

		if (lastAcceptedResult != null) {
			scheduledSequences.addAll(lastAcceptedResult);
		} else {
			for (int i = 0; i < sequences.size(); i++) {
				scheduledSequences.add(null);
			}
		}

		for (final int index : changedSequences) {
			final ScheduledSequence sequence = super.schedule(resources.get(index), sequences.getSequence(index), arrivalTimes[index]);
			if (sequence == null) {
				return false; // break out now, something bad has happened
			}
			scheduledSequences.set(index, sequence);
		}

		if (evaluator != null) {
			lastValue = evaluator.evaluateSchedule(sequences, scheduledSequences, null);
		} else {
			lastValue = 0;
		}

		logValue(lastValue);

		lastEvaluationWasBestResult = lastValue < bestValue;

		if (lastEvaluationWasBestResult) {
			bestIndex = count;
			bestValue = lastValue;
			bestResult = scheduledSequences;
			return true;
		}

		return false;
	}

	/**
	 * Gets the earliest time at which the current vessel can arrive at the given element, given the arrival times set for the previous elements.
	 * 
	 * @param index
	 * @return
	 */
	protected final int getMinArrivalTime(final int seq, final int index) {
		if (index == 0) {
			return windowStartTime[seq][index];
		} else if (isVirtual[seq][index] || isVirtual[seq][index - 1]) {
			// if this is a virtual port, or we've just been to a virtual port
			// we enforce zero travel time, because virtual ports aren't
			// supposed to have any effect. A similar clause applies in
			// getMaxArrivalTime().

			return arrivalTimes[seq][index - 1];
		} else if (useTimeWindow[seq][index]) {
			return windowStartTime[seq][index];
		} else {
			// whichever is later: previous arrival time + travel, or
			// window start.
			return Math.max(arrivalTimes[seq][index - 1] + minTimeToNextElement[seq][index - 1], windowStartTime[seq][index]);
		}
	}

	/**
	 * Gets the latest time at which the current vessel can arrive at the given element, given the arrival times set for the previous elements.
	 * 
	 * Essentially this is max(getMinArrivalTime(index), endTimeWindow[index]).
	 * 
	 * @param index
	 * @return
	 */
	protected final int getMaxArrivalTime(final int seq, final int index) {
		if ((index > 0) && (isVirtual[seq][index] || isVirtual[seq][index - 1])) {
			// if this is a virtual element, or the previous element was
			// virtual, enforce zero travel time
			return arrivalTimes[seq][index - 1];
		} else if (useTimeWindow[seq][index]) {
			return windowStartTime[seq][index];
		} else {
			return Math.max(getMinArrivalTime(seq, index), // the latest we can
															// arrive
															// here is either
															// window
															// end
															// time, or if we're
															// late
															// clamp to the
															// earliest.
					windowEndTime[seq][index]);
		}
	}

	protected final int getMaxArrivalTimeForNextArrival(final int seq, final int pos) {
		if (sizes[seq] < 2)
			return windowStartTime[seq][pos];
		final int ideal = arrivalTimes[seq][pos + 1] - minTimeToNextElement[seq][pos];
		if (ideal < windowStartTime[seq][pos]) {
			return windowStartTime[seq][pos];
		} else if (ideal > windowEndTime[seq][pos]) {
			return windowEndTime[seq][pos];
		} else {
			return ideal;
		}

	}

	public ScheduleFitnessEvaluator getScheduleEvaluator() {
		return evaluator;
	}

	public void setScheduleEvaluator(final ScheduleFitnessEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	/**
	 * Get the approximate number of combinations of arrival times for elements from firstIndex to lastIndex inclusive, up to maxValue
	 * 
	 * @param firstIndex
	 *            first index to look at
	 * @param lastIndex
	 *            last index to look at
	 * @param maxValue
	 *            the maximum return value
	 * @return
	 */
	protected final long getApproximateCombinations(final int seq, final int firstIndex, final int lastIndex, final long maxValue) {
		long accumulator = 1;
		for (int i = firstIndex; i <= lastIndex; i++) {
			accumulator *= ((windowEndTime[seq][i] - windowStartTime[seq][i]) + 1);
			if (accumulator > maxValue) {
				return maxValue;
			}
		}
		return accumulator;
	}

	public long getLastValue() {
		return lastValue;
	}

	/**
	 * @since 2.0
	 */
	public int[][] getArrivalTimes() {
		return arrivalTimes;
	}
}
