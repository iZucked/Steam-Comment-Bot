/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.RoundTripCargoEnd;
import com.mmxlabs.scheduler.optimiser.fitness.impl.AbstractLoggingSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.util.SequenceEvaluationUtils;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShipToShipBindingProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimeWindowsRecord;

/**
 * A sequence scheduler which enumerates possible combinations of arrival times explicitly, rather than using the GA byte array decoding method. This should be subclassable into a random sequence
 * scheduler as well, with reduced decoding overhead
 * 
 * @author hinton
 * 
 */
public abstract class EnumeratingSequenceScheduler extends AbstractLoggingSequenceScheduler {

	private static final int DISCHARGE_SEQUENCE_INDEX_OFFSET = 0;
	private static final int DISCHARGE_WITHIN_SEQUENCE_INDEX_OFFSET = 1;
	private static final int LOAD_SEQUENCE_INDEX_OFFSET = 2;
	private static final int LOAD_WITHIN_SEQUENCE_INDEX_OFFSET = 3;

	/**
	 * How long to let empty time windows be. Since these mostly happen at the end of sequences we make this zero.
	 */
	private static final int EMPTY_WINDOW_SIZE = 0;

	/**
	 * The output of the scheduler; these are the arrival times for each element in each sequence.
	 * 
	 */
	protected int[][] arrivalTimes;
	/**
	 * The start times of each window, appropriately `clipped' to deal with infeasible choices or null time windows.
	 */
	protected int[][] windowStartTime;
	/**
	 * The end times of each window, similar to start times.
	 */
	protected int[][] windowEndTime;
	/**
	 * The minimum time this vessel can take to get from the indexed element to its successor. i.e. min travel time + visit time at indexed element.
	 */
	protected int[][] minTimeToNextElement;
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
	 * the route. Also used for actuals where forcast travel time is irrelevant.
	 */
	private boolean[][] useTimeWindow;
	/**
	 * Boolean indicating time is actualised and cannot be changed.
	 */
	private boolean[][] actualisedTimeWindow;

	/**
	 * A list of ship-to-ship binding information in <D_i, D_j, L_i, L_j> quadruples where D_i, D_j are the indices of the sequence and the position within the sequence of the discharge element, and
	 * L_i & L_j similarly for the load. This representation is for efficiency purposes.
	 * 
	 * N.B. The implementation assumes relatively few ship-to-ship bindings, since the time complexity of the algorithm increases linearly with the number of such bindings.
	 * 
	 */
	protected ArrayList<Integer> bindings = new ArrayList<Integer>();

	/**
	 * Holds a list of points at which the cost function can be separated. This occurs when a given journey leg <em>always</em> involves some idle time, so there can be no knock-on effects on the
	 * segment following the point from the times chosen up to the point.
	 * 
	 * These are the indexes of the sequence elements at the <em>start</em> of such legs. TODO this is disabled
	 */
	// protected final ArrayList<Integer> separationPoints = new
	// ArrayList<Integer>();

	protected List<List<IPortTimeWindowsRecord>> portTimeWindowsRecords = new ArrayList<>();

	@Inject
	private IShipToShipBindingProvider shipToShipProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private IPortProvider portProvider;

	@Inject
	private IRouteCostProvider routeCostProvider;

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	private IElementDurationProvider durationProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IActualsDataProvider actualsDataProvider;

	/**
	 * The sequences being evaluated at the moment
	 */
	protected ISequences sequences;

	private final TimeWindow defaultStartWindow = new TimeWindow(0, Integer.MAX_VALUE);

	public EnumeratingSequenceScheduler() {
		super();
	}

	protected final void setSequences(final ISequences sequences) {
		this.sequences = sequences;
	}

	protected final void prepare() {
		final int size = sequences.size();
		bindings.clear();

		// if ((arrivalTimes == null) || (arrivalTimes.length != size)) {
		arrivalTimes = new int[size][];
		windowStartTime = new int[size][];
		windowEndTime = new int[size][];
		minTimeToNextElement = new int[size][];
		maxTimeToNextElement = new int[size][];
		isVirtual = new boolean[size][];
		useTimeWindow = new boolean[size][];
		actualisedTimeWindow = new boolean[size][];
		sizes = new int[size];
		// }

		portTimeWindowsRecords.clear();
		for (int i = 0; i < size; i++) {
			portTimeWindowsRecords.add(new LinkedList<IPortTimeWindowsRecord>());
			prepare(i);
			List<IPortTimeWindowsRecord> records = portTimeWindowsRecords.get(i);
			if (records.size() > 0) {
				IPortTimeWindowsRecord lastRecord = records.get(records.size() - 1);
			}
		}

		imposeShipToShipConstraints();
	}

	/**
	 * Store the sequence and index within the sequence of the slot/element if it part of a ship to ship binding. This data is stored in the {@link #bindings} list as a quadruple mapping both slots
	 * together such that we have, [ discharge sequence index, discharge within sequence index, load sequence index, load within sequence index]
	 * 
	 * @param seqIndex
	 * @param withinSeqIndex
	 * @param element
	 */
	private final void recordShipToShipBindings(final int seqIndex, final int withinSeqIndex, final @NonNull ISequenceElement element) {

		final IPortSlot slot = portSlotProvider.getPortSlot(element);
		final IPortSlot converseSlot = shipToShipProvider.getConverseTransferElement(slot);
		if (converseSlot == null) {
			return;
		}
		final ISequenceElement transferConverseElement = portSlotProvider.getElement(converseSlot);

		if (transferConverseElement != null) {
			final boolean element_is_discharge = slot.getPortType() == PortType.Discharge;

			/*
			 * offsets within the <bindings> quadruples to represent this element's sequence, its sequence within the index, and similarly for the element which is bound to it
			 */
			int thisSequenceOffset, thisIndexOffset, converseSequenceOffset, converseIndexOffset;
			if (element_is_discharge) {
				thisSequenceOffset = DISCHARGE_SEQUENCE_INDEX_OFFSET;
				thisIndexOffset = DISCHARGE_WITHIN_SEQUENCE_INDEX_OFFSET;
				converseSequenceOffset = LOAD_SEQUENCE_INDEX_OFFSET;
				converseIndexOffset = LOAD_WITHIN_SEQUENCE_INDEX_OFFSET;
			} else {
				thisSequenceOffset = LOAD_SEQUENCE_INDEX_OFFSET;
				thisIndexOffset = LOAD_WITHIN_SEQUENCE_INDEX_OFFSET;
				converseSequenceOffset = DISCHARGE_SEQUENCE_INDEX_OFFSET;
				converseIndexOffset = DISCHARGE_WITHIN_SEQUENCE_INDEX_OFFSET;
			}

			boolean attached = false;

			/*
			 * Attach this element's indices to the bindings array if its converse element is already there
			 */
			for (int k = 0; k < bindings.size(); k += 4) {
				final int converseSequence = bindings.get(k + converseSequenceOffset);
				final int converseIndex = bindings.get(k + converseIndexOffset);

				if (converseSequence != -1 && converseIndex != -1 && sequences.getSequence(converseSequence).get(converseIndex) == transferConverseElement) {
					bindings.set(k + thisSequenceOffset, seqIndex);
					bindings.set(k + thisIndexOffset, withinSeqIndex);
					attached = true;
					break;
				}
			}

			if (!attached) {
				final int addIndex = bindings.size();
				for (int k = 0; k < 4; k++) {
					bindings.add(-1);
				}
				bindings.set(addIndex + thisSequenceOffset, seqIndex);
				bindings.set(addIndex + thisIndexOffset, withinSeqIndex);
			}
		}
	}

	/**
	 * Refines the start and end times of a sequence, making sure that there is enough travel time between each window.
	 * 
	 * @param startTimes
	 * @param endTimes
	 */
	private final void refineWindows(final int seqIndex) {
		final int[] startTimes = windowStartTime[seqIndex];
		final int[] endTimes = windowEndTime[seqIndex];
		final int[] minTravelTimes = minTimeToNextElement[seqIndex];
		final boolean[] useRawTimeWindow = useTimeWindow[seqIndex];
		final boolean[] actualiseTimeWindows = this.actualisedTimeWindow[seqIndex];
		// time windows after the first one have their start time clipped, so
		// they don't start any earlier
		// than you could get to them without being late.
		for (int i = 1; i < startTimes.length; i++) {
			if (actualiseTimeWindows[i] && !useRawTimeWindow[i]) {
				startTimes[i] = Math.max(startTimes[i], startTimes[i - 1] + minTravelTimes[i - 1]);
				endTimes[i] = Math.max(endTimes[i], startTimes[i] + 1);
			}
		}

		// now perform reverse-pass to trim any overly late end times
		// (that is end times which would make us late at the next element)
		for (int i = startTimes.length - 2; i >= 0; i--) {
			// trim the end of this time window so that the next element is
			// reachable without lateness
			// (but never so that the end time is before the start time)
			if (actualiseTimeWindows[i] && !useRawTimeWindow[i + 1]) {
				endTimes[i] = Math.max(startTimes[i] + 1, Math.min(endTimes[i], endTimes[i + 1] - minTravelTimes[i]));
			}
		}
	}

	/**
	 * Refines start and end times to make sure that ship to ship transfers are respected.
	 * 
	 * @param bindings
	 *            A linked list of integers indicating which sequence elements are bound to which other sequence elements. This should have the form of <i1, j1, i2, j2> quadruplets giving the indices
	 *            of the sequence, and the position within the sequence, for the discharge slots and bound load slots respectively. This list is consumed by the method.
	 * 
	 *            Note: the horrible semantics of this method are for efficiency reasons and are constrained by the data representations used by other methods in this class.
	 */
	private final void imposeShipToShipConstraints() {
		boolean recalculateDischarge = false;
		boolean recalculateLoad = false;

		int max_iterations = 100;

		/*
		 * We need to keep recalculating the windows until nothing gets modified.
		 */
		do {
			for (int i = 0; i < bindings.size(); i += 4) {

				final int discharge_seq = bindings.get(i + DISCHARGE_SEQUENCE_INDEX_OFFSET);
				final int discharge_index = bindings.get(i + DISCHARGE_WITHIN_SEQUENCE_INDEX_OFFSET);
				final int load_seq = bindings.get(i + LOAD_SEQUENCE_INDEX_OFFSET);
				final int load_index = bindings.get(i + LOAD_WITHIN_SEQUENCE_INDEX_OFFSET);

				// Must be on different routes
				assert discharge_seq != load_seq;

				recalculateDischarge = false;
				recalculateLoad = false;

				// sequence elements bound by ship-to-ship transfers are effectively the same slot, so window start and end times have to be constrained conservatively
				final int wst = Math.max(windowStartTime[discharge_seq][discharge_index], windowStartTime[load_seq][load_index]);
				int wet = Math.min(windowEndTime[discharge_seq][discharge_index], windowEndTime[load_seq][load_index]);

				// If there is no overlap in the time windows, we need to use the later time window
				wet = Math.max(wet, wst);

				if (windowStartTime[discharge_seq][discharge_index] != wst) {
					windowStartTime[discharge_seq][discharge_index] = wst;
					recalculateDischarge = true;
				}

				if (windowEndTime[discharge_seq][discharge_index] != wet) {
					windowEndTime[discharge_seq][discharge_index] = wet;
					recalculateDischarge = true;
				}

				if (windowStartTime[load_seq][load_index] != wst) {
					windowStartTime[load_seq][load_index] = wst;
					recalculateLoad = true;
				}

				if (windowEndTime[load_seq][load_index] != wet) {
					windowEndTime[load_seq][load_index] = wet;
					recalculateLoad = true;
				}

				// any sequence which we changed the time on, we have to recalculate
				if (recalculateDischarge) {
					refineWindows(discharge_seq);
				}
				if (recalculateLoad) {
					refineWindows(load_seq);
				}
			}
			max_iterations--;
		} while ((recalculateDischarge || recalculateLoad) && max_iterations > 0);

		if (max_iterations <= 0) {
			System.err.println("Something went wrong in the re-windowing of ship to ship transfers");
		}
	}

	/**
	 * Returns an array of boolean values indicating whether, for each index of the vessel location sequence, a sequence break occurs at that location (separating one cargo from the next one).
	 * 
	 * @param sequence
	 * @return
	 */
	protected boolean[] findSequenceBreaks(final ISequence sequence, boolean isRoundTripSequence) {
		final boolean[] result = new boolean[sequence.size()];

		int idx = 0;
		for (final ISequenceElement element : sequence) {
			final PortType portType = portTypeProvider.getPortType(element);
			switch (portType) {
			case Load:
				result[idx] = !isRoundTripSequence && (idx > 0); // don't break on first load port
				break;
			case CharterOut:
			case DryDock:
			case Other:
			case Maintenance:
			case End:
			case Round_Trip_Cargo_End:
				result[idx] = true;
				break;
			default:
				result[idx] = false;
				break;
			}
			idx++;
		}

		return result;
	}

	/**
	 * Unpack some distance/time/speed information, set up arrays etc
	 * 
	 * @param maxValue
	 *            s *
	 * @param sequence
	 * @return
	 */
	protected final void prepare(final int sequenceIndex) {
		final ISequence sequence = sequences.getSequence(sequenceIndex);
		final IResource resource = sequences.getResources().get(sequenceIndex);

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
			// TODO: Implement something here rather than rely on VoyagePlanner
			return;
		}

		final int size = sequence.size();
		// filters out solutions with less than 2 elements (i.e. spot charters, etc.)
		if (SequenceEvaluationUtils.shouldIgnoreSequence(sequence)) {
			return;
		}

		resizeAll(sequenceIndex, size);

		final int[] windowStartTime = this.windowStartTime[sequenceIndex];
		final int[] windowEndTime = this.windowEndTime[sequenceIndex];
		final int[] minTimeToNextElement = this.minTimeToNextElement[sequenceIndex];
		final int[] maxTimeToNextElement = this.maxTimeToNextElement[sequenceIndex];
		final boolean[] isVirtual = this.isVirtual[sequenceIndex];
		final boolean[] useTimeWindow = this.useTimeWindow[sequenceIndex];
		final boolean[] actualisedTimeWindow = this.actualisedTimeWindow[sequenceIndex];

		final int maxSpeed = vesselAvailability.getVessel().getVesselClass().getMaxSpeed();

		final int minSpeed = vesselAvailability.getVessel().getVesselClass().getMinSpeed();

		final boolean isRoundTripSequence = vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP;

		int index = 0;
		ISequenceElement prevElement = null;
		final boolean[] breakSequence = findSequenceBreaks(sequence, isRoundTripSequence);

		// from voyageplanner --->
		IPortSlot prevPortSlot = null;
		// Used for end of sequence checks
		PortTimeWindowsRecord portTimeWindowsRecord = new PortTimeWindowsRecord();
		portTimeWindowsRecord.setResource(resource);
		// --->
		// first pass, collecting start time windows
		for (final ISequenceElement element : sequence) {
			final IPortSlot portSlot = portSlotProvider.getPortSlot(element);

			// from voyageplanner --->
			final IPortSlot thisPortSlot = portSlotProvider.getPortSlot(element);
			final int visitDuration = actualsDataProvider.hasActuals(thisPortSlot) ? actualsDataProvider.getVisitDuration(thisPortSlot) : durationProvider.getElementDuration(element, resource);
			// --->

			final ITimeWindow window;

			// Take element start window into account
			if (portTypeProvider.getPortType(element) == PortType.Start) {
				final IStartEndRequirement startRequirement = startEndRequirementProvider.getStartRequirement(resource);

				// "windows" defaults to the default start window
				ITimeWindow timeWindow = defaultStartWindow;

				// but can be overridden by a specified start requirement
				if (startRequirement != null && startRequirement.getTimeWindow() != null) {
					timeWindow = startRequirement.getTimeWindow();
				}

				window = timeWindow;
			} else if (portTypeProvider.getPortType(element) == PortType.End) {
				final IStartEndRequirement endRequirement = startEndRequirementProvider.getEndRequirement(resource);
				// "windows" defaults to an empty list
				if (endRequirement != null) {
					// but can be overridden by the specified end requirement
					final ITimeWindow timeWindow = endRequirement.getTimeWindow();
					if (timeWindow != null) {
						// Make sure we always use the time window [This never worked - value was overwritten]
						useTimeWindow[index] = true;
						window = timeWindow;
					} else {
						window = portSlot.getTimeWindow();
					}
				} else {
					window = portSlot.getTimeWindow();
				}
			} else {

				final IPortSlot prevSlot = prevElement == null ? null : portSlotProvider.getPortSlot(prevElement);
				if (prevSlot != null && actualsDataProvider.hasReturnActuals(prevSlot)) {
					window = actualsDataProvider.getReturnTimeAsTimeWindow(prevSlot);
					if (actualsDataProvider.hasActuals(portSlot)) {
						int a = actualsDataProvider.getArrivalTime(portSlot);
						int b = actualsDataProvider.getReturnTime(prevSlot);
						assert a == b;

					}
					actualisedTimeWindow[index] = true;
				} else if (actualsDataProvider.hasActuals(portSlot)) {
					window = actualsDataProvider.getArrivalTimeWindow(portSlot);
					actualisedTimeWindow[index] = true;

				} else {
					// "windows" defaults to whatever windows are specified by the time window provider
					window = portSlot.getTimeWindow();
				}

				recordShipToShipBindings(sequenceIndex, index, element);
			}

			if (breakSequence[index]) {
				// last slot in plan, set return
				portTimeWindowsRecord.setReturnSlot(thisPortSlot, null, visitDuration, index);
				// finalise record
				portTimeWindowsRecords.get(sequenceIndex).add(portTimeWindowsRecord);
				// create new record
				if (!(thisPortSlot instanceof RoundTripCargoEnd)) {
					portTimeWindowsRecord = new PortTimeWindowsRecord();
					portTimeWindowsRecord.setResource(resource);
					portTimeWindowsRecord.setSlot(thisPortSlot, null, visitDuration, index);
				}
			} else {
				if (!(prevPortSlot instanceof RoundTripCargoEnd)) {
					portTimeWindowsRecord.setSlot(thisPortSlot, null, visitDuration, index);
				} else {
					portTimeWindowsRecord = new PortTimeWindowsRecord();
					portTimeWindowsRecord.setResource(resource);
					portTimeWindowsRecord.setSlot(thisPortSlot, null, visitDuration, index);
				}
			}

			isVirtual[index] = portTypeProvider.getPortType(element) == PortType.Virtual;
			useTimeWindow[index] = prevElement == null ? false : portTypeProvider.getPortType(prevElement) == PortType.Round_Trip_Cargo_End;
			// Calculate minimum inter-element durations
			maxTimeToNextElement[index] = minTimeToNextElement[index] = durationProvider.getElementDuration(element, resource);

			if (prevElement != null) {
				final IPort prevPort = portProvider.getPortForElement(prevElement);
				final IPort port = portProvider.getPortForElement(element);

				int minTravelTime = Integer.MAX_VALUE;
				int maxTravelTime = 0;
				for (final Pair<@NonNull ERouteOption, @NonNull Integer> entry : distanceProvider.getDistanceValues(prevPort, port,
						windowStartTime[index - 1] + durationProvider.getElementDuration(element, resource), vesselAvailability.getVessel())) {
					final int distance = entry.getSecond();
					if (distance != Integer.MAX_VALUE) {
						final int extraTime = routeCostProvider.getRouteTransitTime(entry.getFirst(), vesselAvailability.getVessel());
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
			if (window == null) { // empty time windows are made to be the
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
				if (index == 0) {// first time window is special
					windowStartTime[index] = window.getInclusiveStart();
					windowEndTime[index] = window.getExclusiveEnd();
				} else {
					// subsequent time windows have their start time clipped, so
					// they don't start any earlier
					// than you could get to them without being late.
					windowEndTime[index] = window.getExclusiveEnd();
					if (useTimeWindow[index] || actualisedTimeWindow[index]) {
						// Cargo shorts - pretend this is a start element
						// Actuals - use window directly
						windowStartTime[index] = window.getInclusiveStart();
					} else {
						windowStartTime[index] = Math.max(window.getInclusiveStart(), windowStartTime[index - 1] + minTimeToNextElement[index - 1]);
						windowEndTime[index] = Math.max(windowEndTime[index], windowStartTime[index] + 1);
					}
				}
			}

			index++;
			prevElement = element;
			prevPortSlot = thisPortSlot;
		}
		// add the last time window
		// portTimeWindowsRecords.get(sequenceIndex).add(portTimeWindowsRecord);
		// now perform reverse-pass to trim any overly late end times
		// (that is end times which would make us late at the next element)
		for (index = size - 2; index >= 0; index--) {
			// trim the end of this time window so that the next element is
			// reachable without lateness
			// (but never so that the end time is before the start time)

			if (actualisedTimeWindow[index] && actualisedTimeWindow[index + 1]) {
				// Skip, windows should already match.
			} else if (actualisedTimeWindow[index + 1] && !actualisedTimeWindow[index]) {
				// Current window if flexible, next window is fixed, bring end window back
				windowEndTime[index] = windowEndTime[index + 1] - minTimeToNextElement[index];
			} else if (!useTimeWindow[index + 1]) {
				windowEndTime[index] = Math.max(windowStartTime[index] + 1, Math.min(windowEndTime[index], windowEndTime[index + 1] - minTimeToNextElement[index]));
			}

			// Make sure end if >= start - this may shift the end forward again violating min travel time.
			windowEndTime[index] = Math.max(windowStartTime[index] + 1, windowEndTime[index]);
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
		} else if (useTimeWindow[seq][index] || actualisedTimeWindow[seq][index]) {
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
		} else if (useTimeWindow[seq][index] || actualisedTimeWindow[seq][index]) {
			return windowStartTime[seq][index];
		} else {
			// the latest we can arrive here is either window end time, or if we're late clamp to the earliest.
			return Math.max(getMinArrivalTime(seq, index), windowEndTime[seq][index] - 1);
		}
	}

	protected final int getMaxArrivalTimeForNextArrival(final int seq, final int pos) {
		if (sizes[seq] < 2)
			return windowStartTime[seq][pos];
		final int ideal = arrivalTimes[seq][pos + 1] - minTimeToNextElement[seq][pos];
		if (ideal < windowStartTime[seq][pos]) {
			return windowStartTime[seq][pos];
		} else if (ideal >= windowEndTime[seq][pos]) {
			return windowEndTime[seq][pos] - 1;
		} else {
			return ideal;
		}
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
			accumulator *= ((windowEndTime[seq][i] - windowStartTime[seq][i]));
			if (accumulator > maxValue) {
				return maxValue;
			}
		}
		return accumulator;
	}

	/**
	 */
	public int[][] getArrivalTimes() {
		return arrivalTimes;
	}

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
		resize(actualisedTimeWindow, sequenceIndex, size);

		sizes[sequenceIndex] = size;
	}
}
