/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Sets;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.MutableTimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.RoundTripCargoEnd;
import com.mmxlabs.scheduler.optimiser.fitness.util.SequenceEvaluationUtils;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IElementPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShipToShipBindingProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimeWindowsRecord;

/**
 * A sequence scheduler which enumerates possible combinations of arrival times explicitly, rather than using the GA byte array decoding method. This should be subclassable into a random sequence
 * scheduler as well, with reduced decoding overhead
 * 
 * @author hinton
 * 
 */
public class FeasibleTimeWindowTrimmer {

	private static final int DISCHARGE_SEQUENCE_INDEX_OFFSET = 0;
	private static final int DISCHARGE_WITHIN_SEQUENCE_INDEX_OFFSET = 1;
	private static final int LOAD_SEQUENCE_INDEX_OFFSET = 2;
	private static final int LOAD_WITHIN_SEQUENCE_INDEX_OFFSET = 3;

	@Inject
	@Named(SchedulerConstants.Key_UseCanalSlotBasedWindowTrimming)
	private boolean checkPanamaCanalBookings = false;

	/**
	 * How long to let empty time windows be. Since these mostly happen at the end of sequences we make this zero.
	 */
	private static final int EMPTY_WINDOW_SIZE = 0;

	/**
	 * Flag indicating a round trip end element. The next element should be considered to be the start of a sequence. (E.g. for the DirectRandomSequenceScheduler it should reset the seed).
	 */
	private boolean[][] isRoundTripEnd;
	/**
	 * The start times of each window, appropriately `clipped' to deal with infeasible choices or null time windows.
	 */
	private int[][] windowStartTime;
	/**
	 * The end times of each window, similar to start times.
	 */
	private int[][] windowEndTime;

	/**
	 * The number of elements in each array.
	 */
	private int[] sizes;

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
	private final ArrayList<Integer> bindings = new ArrayList<Integer>();

	@Inject
	private IShipToShipBindingProvider shipToShipProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private IElementPortProvider portProvider;

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

	@Inject
	private IPanamaBookingsProvider panamaBookingsProvider;

	/**
	 * The sequences being evaluated at the moment
	 */
	private ISequences sequences;

	private final TimeWindow defaultStartWindow = new TimeWindow(0, Integer.MAX_VALUE);
	private Map<IPort, Set<IRouteOptionBooking>> assignedBookings;
	private Map<IPort, Set<IRouteOptionBooking>> unassignedBookings;

	public FeasibleTimeWindowTrimmer() {
		super();
	}

	public final Map<IResource, List<IPortTimeWindowsRecord>> generateTrimmedWindows(final ISequences sequences, final MinTravelTimeData travelTimeData) {
		final int size = sequences.size();

		if (windowStartTime == null) {
			windowStartTime = new int[size][];
			isRoundTripEnd = new boolean[size][];
			windowEndTime = new int[size][];
			isVirtual = new boolean[size][];
			useTimeWindow = new boolean[size][];
			actualisedTimeWindow = new boolean[size][];
			sizes = new int[size];
		}

		assignedBookings = new HashMap<IPort, Set<IRouteOptionBooking>>();
		unassignedBookings = new HashMap<IPort, Set<IRouteOptionBooking>>();
		panamaBookingsProvider.getBookings().entrySet().forEach(e -> {
			final Set<IRouteOptionBooking> assigned = e.getValue().stream().filter(j -> j.getPortSlot().isPresent()).collect(Collectors.toSet());
			assignedBookings.put(e.getKey(), new TreeSet<>(assigned));
			unassignedBookings.put(e.getKey(), new TreeSet<>(Sets.difference(e.getValue(), assigned)));
		});

		final Map<IResource, List<IPortTimeWindowsRecord>> portTimeWindowsRecordsMap = new HashMap<>();
		for (int seqIndex = 0; seqIndex < size; seqIndex++) {
			final IResource resource = sequences.getResources().get(seqIndex);
			final LinkedList<IPortTimeWindowsRecord> portTimeWindowRecords = new LinkedList<IPortTimeWindowsRecord>();
			prepare(sequences, seqIndex, portTimeWindowRecords, travelTimeData);

			for (int idx = 0; idx < portTimeWindowRecords.size(); idx++) {
				final IPortTimeWindowsRecord portTimeWindowsRecord = portTimeWindowRecords.get(idx);
				if (isSequentialVessel(portTimeWindowsRecord.getResource())) {
					final boolean isLastRecord = idx == portTimeWindowRecords.size() - 1;
					setFeasibleTimeWindowsUsingPrevious(resource, portTimeWindowsRecord, seqIndex, travelTimeData, isLastRecord);
				} else {
					setFeasibleTimeWindowsRoundTrip(resource, portTimeWindowsRecord, seqIndex, travelTimeData);
				}

			}
			portTimeWindowsRecordsMap.put(resource, portTimeWindowRecords);

		}

		// FIXME: Update time window records!
		// imposeShipToShipConstraints();

		return portTimeWindowsRecordsMap;
	}

	/**
	 * Returns an array of boolean values indicating whether, for each index of the vessel location sequence, a sequence break occurs at that location (separating one cargo from the next one).
	 * 
	 * @param sequence
	 * @return
	 */
	private boolean[] findSequenceBreaks(final ISequence sequence, final boolean isRoundTripSequence) {
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
	 * @param portTimeWindowRecords
	 * @param sequences
	 * 
	 * @param maxValue
	 *            s *
	 * @param sequence
	 * @return
	 */
	protected final void prepare(final ISequences sequences, final int sequenceIndex, final List<IPortTimeWindowsRecord> portTimeWindowRecords, final MinTravelTimeData travelTimeData) {
		final IResource resource = sequences.getResources().get(sequenceIndex);
		final ISequence sequence = sequences.getSequence(resource);

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
		final boolean[] isVirtual = this.isVirtual[sequenceIndex];
		final boolean[] useTimeWindow = this.useTimeWindow[sequenceIndex];
		final boolean[] actualisedTimeWindow = this.actualisedTimeWindow[sequenceIndex];
		final boolean[] isRoundTripEnd = this.isRoundTripEnd[sequenceIndex];

		final boolean isRoundTripSequence = vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP;
		final boolean isSpotCharter = vesselAvailability.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER;

		int index = 0;
		ISequenceElement prevElement = null;
		final boolean[] breakSequence = findSequenceBreaks(sequence, isRoundTripSequence);

		IPortSlot prevPortSlot = null;
		// Used for end of sequence checks
		PortTimeWindowsRecord portTimeWindowsRecord = new PortTimeWindowsRecord();
		portTimeWindowsRecord.setResource(resource);

		// first pass, collecting start time windows
		for (final ISequenceElement element : sequence) {

			PortTimeWindowsRecord currentPortTimeWindowsRecord = portTimeWindowsRecord;

			final IPortSlot portSlot = portSlotProvider.getPortSlot(element);

			final IPortSlot thisPortSlot = portSlotProvider.getPortSlot(element);
			final int visitDuration = actualsDataProvider.hasActuals(thisPortSlot) ? actualsDataProvider.getVisitDuration(thisPortSlot) : durationProvider.getElementDuration(element, resource);

			final ITimeWindow window;

			// Take element start window into account
			if (portTypeProvider.getPortType(element) == PortType.Start) {
				final IStartEndRequirement startRequirement = startEndRequirementProvider.getStartRequirement(resource);

				// "windows" defaults to the default start window
				ITimeWindow timeWindow = defaultStartWindow;

				// but can be overridden by a specified start requirement
				if (startRequirement != null && startRequirement.hasTimeRequirement()) {
					timeWindow = startRequirement.getTimeWindow();
				}

				window = timeWindow;
			} else if (portTypeProvider.getPortType(element) == PortType.End) {
				// Previously this was null for spot & round trip vessels, so make null again here.
				// Consider setting upper bound to "Integer.MAX_VALUE"
				// Consider extra TW flag?
				// Could make it null again...
				if (isSpotCharter || isRoundTripSequence) {
					// Special scheduling rules - finish as early as possible then the VPO or scheduling end date calculator kicks in
					window = null;
				} else {
					final IStartEndRequirement endRequirement = startEndRequirementProvider.getEndRequirement(resource);
					// "windows" defaults to an empty list
					if (endRequirement != null) {
						// but can be overridden by the specified end requirement
						if (endRequirement.hasTimeRequirement()) {
							final ITimeWindow timeWindow = endRequirement.getTimeWindow();
							// Make sure we always use the time window [This never worked - value was overwritten]
							useTimeWindow[index] = true;
							window = timeWindow;
						} else {

							window = portSlot.getTimeWindow();
						}
					} else {
						window = portSlot.getTimeWindow();
					}
				}
			} else {

				final IPortSlot prevSlot = prevElement == null ? null : portSlotProvider.getPortSlot(prevElement);
				if (prevSlot != null && actualsDataProvider.hasReturnActuals(prevSlot)) {
					window = actualsDataProvider.getReturnTimeAsTimeWindow(prevSlot);
					if (actualsDataProvider.hasActuals(portSlot)) {
						final int a = actualsDataProvider.getArrivalTime(portSlot);
						final int b = actualsDataProvider.getReturnTime(prevSlot);
						assert a == b;

					}
					actualisedTimeWindow[index] = true;
				} else if (actualsDataProvider.hasActuals(portSlot)) {
					window = actualsDataProvider.getArrivalTimeWindow(portSlot);
					actualisedTimeWindow[index] = true;
				} else {
					window = portSlot.getTimeWindow();
				}

				recordShipToShipBindings(sequenceIndex, index, element);
			}

			isRoundTripEnd[index] = false;
			if (breakSequence[index]) {
				// last slot in plan, set return
				portTimeWindowsRecord.setReturnSlot(thisPortSlot, null, visitDuration, index);
				// finalise record
				portTimeWindowRecords.add(portTimeWindowsRecord);
				// create new record
				if (!(thisPortSlot instanceof RoundTripCargoEnd)) {
					portTimeWindowsRecord = new PortTimeWindowsRecord();
					portTimeWindowsRecord.setResource(resource);
					portTimeWindowsRecord.setSlot(thisPortSlot, null, visitDuration, index);
				} else {
					isRoundTripEnd[index] = true;
				}
			} else {
				if (!(prevPortSlot instanceof RoundTripCargoEnd)) {
					portTimeWindowsRecord.setSlot(thisPortSlot, null, visitDuration, index);
				} else {
					portTimeWindowsRecord = new PortTimeWindowsRecord();
					portTimeWindowsRecord.setResource(resource);
					portTimeWindowsRecord.setSlot(thisPortSlot, null, visitDuration, index);
					isRoundTripEnd[index] = true;
				}
			}

			isVirtual[index] = portTypeProvider.getPortType(element) == PortType.Virtual;
			useTimeWindow[index] = prevElement == null ? false : portTypeProvider.getPortType(prevElement) == PortType.Round_Trip_Cargo_End;
			// Calculate minimum inter-element durations
			travelTimeData.setMinTravelTime(sequenceIndex, index, durationProvider.getElementDuration(element, resource));
			int directTravelTime = Integer.MAX_VALUE;
			int suezTravelTime = Integer.MAX_VALUE;
			int panamaTravelTime = Integer.MAX_VALUE;
			if (prevElement != null) {

				final IPort prevPort = portProvider.getPortForElement(prevElement);
				final IPort port = portProvider.getPortForElement(element);

				int prevVisitDuration = durationProvider.getElementDuration(prevElement, resource);

				directTravelTime = distanceProvider.getTravelTime(ERouteOption.DIRECT, vesselAvailability.getVessel(), prevPort, port, vesselAvailability.getVessel().getVesselClass().getMaxSpeed());
				suezTravelTime = distanceProvider.getTravelTime(ERouteOption.SUEZ, vesselAvailability.getVessel(), prevPort, port, vesselAvailability.getVessel().getVesselClass().getMaxSpeed());
				panamaTravelTime = distanceProvider.getTravelTime(ERouteOption.PANAMA, vesselAvailability.getVessel(), prevPort, port, vesselAvailability.getVessel().getVesselClass().getMaxSpeed());

				if (directTravelTime != Integer.MAX_VALUE) {
					directTravelTime += prevVisitDuration;
				}
				if (suezTravelTime != Integer.MAX_VALUE) {
					suezTravelTime += prevVisitDuration;
				}
				if (panamaTravelTime != Integer.MAX_VALUE) {
					panamaTravelTime += prevVisitDuration;
				}

				int minTravelTime = Math.min(directTravelTime, Math.min(panamaTravelTime, suezTravelTime));

				// final int currentTime = travelTimeData.getMinTravelTime(sequenceIndex, index - 1);
				travelTimeData.setMinTravelTime(sequenceIndex, index - 1, minTravelTime);
			}

			// Handle time windows
			if (window == null) { // empty time windows are made to be the
									// biggest reasonable gap
				if (index > 0) {
					// clip start of time window
					windowStartTime[index] = windowStartTime[index - 1] + travelTimeData.getMinTravelTime(sequenceIndex, index - 1);
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

						if (checkPanamaCanalBookings) {
							final IPortSlot p_prevPortSlot = prevPortSlot;

							@Nullable
							final IPort routeOptionEntry = distanceProvider.getRouteOptionEntry(prevPortSlot.getPort(), ERouteOption.PANAMA);
							assert routeOptionEntry != null;

							final IPort panamaEntry = routeOptionEntry;
							final Optional<IRouteOptionBooking> potentialBooking = assignedBookings.computeIfAbsent(panamaEntry, k -> new TreeSet()).stream().filter(e -> {
								return e.getPortSlot().isPresent() && e.getPortSlot().get().equals(p_prevPortSlot);
							}).findFirst();

							final int toCanal = distanceProvider.getTravelTime(ERouteOption.DIRECT, vesselAvailability.getVessel(), prevPortSlot.getPort(), routeOptionEntry,
									Math.max(panamaBookingsProvider.getSpeedToCanal(), vesselAvailability.getVessel().getVesselClass().getMaxSpeed()));
							// if (isRoundTripSequence) {
							// // Normal behaviour
							// } else

							if (potentialBooking.isPresent()) {
								// window has a booking
								currentPortTimeWindowsRecord.setSlotNextVoyageOptions(prevPortSlot, AvailableRouteChoices.PANAMA_ONLY);
								// currentPortTimeRecord.setRouteOptionBooking(prevPortSlot, potentialBooking.get());

								// check if it can be reached in time
								if (windowStartTime[index - 1] + toCanal + panamaBookingsProvider.getMargin() < potentialBooking.get().getBookingDate()) {
									currentPortTimeWindowsRecord.setRouteOptionBooking(prevPortSlot, potentialBooking.get());

									final int fromEntryPoint = distanceProvider.getTravelTime(potentialBooking.get().getRouteOption(), vesselAvailability.getVessel(),
											potentialBooking.get().getEntryPoint(), portSlot.getPort(), vesselAvailability.getVessel().getVesselClass().getMaxSpeed());

									// Visit duration should implicitly be included in this calculation.
									final int travelTime = (potentialBooking.get().getBookingDate() + fromEntryPoint) - windowStartTime[index - 1];
									travelTimeData.setMinTravelTime(sequenceIndex, index - 1, travelTime);

								} else {
									// Booking can't be reached in time. Set to optimal time through panama and don't include slot.
									// TODO: what to do here, should we report this to the user somehow?
									travelTimeData.setMinTravelTime(sequenceIndex, index - 1, panamaTravelTime);
								}
							} else if (windowStartTime[index] > panamaBookingsProvider.getRelaxedBoundary()) {
								// assume a Panama booking because it's far enough in the future
								currentPortTimeWindowsRecord.setSlotNextVoyageOptions(prevPortSlot, AvailableRouteChoices.OPTIMAL);
							} else if (windowStartTime[index - 1] + directTravelTime < windowEndTime[index] || windowStartTime[index - 1] + suezTravelTime < windowEndTime[index]
									|| directTravelTime == panamaTravelTime) {
								// journey can be made direct (or it does not go across Panama)
								travelTimeData.setMinTravelTime(sequenceIndex, index - 1, Math.min(suezTravelTime, directTravelTime));
								currentPortTimeWindowsRecord.setSlotNextVoyageOptions(p_prevPortSlot, AvailableRouteChoices.EXCLUDE_PANAMA);
								// minTimeToNextElement[index - 1];

							} else {
								// go through panama, figure out if there is an unassigned booking
								currentPortTimeWindowsRecord.setSlotNextVoyageOptions(prevPortSlot, AvailableRouteChoices.PANAMA_ONLY);

								// TODO: this is a bit optimistic in case there is no booking ;-)
								travelTimeData.setMinTravelTime(sequenceIndex, index - 1, panamaTravelTime);

								boolean foundBooking = false;
								assert prevElement != null;
								Set<IRouteOptionBooking> set = unassignedBookings.get(panamaEntry);
								for (final IRouteOptionBooking booking : set) {
									int canalTime = windowStartTime[index - 1] + durationProvider.getElementDuration(prevElement, resource) + toCanal + panamaBookingsProvider.getMargin();
									if (canalTime > booking.getBookingDate()) {
										// booking can't be reached. All following bookings are later and can't be reached either
										continue;
									}
									final int fromEntryPoint = distanceProvider.getTravelTime(booking.getRouteOption(), vesselAvailability.getVessel(), booking.getEntryPoint(), portSlot.getPort(),
											vesselAvailability.getVessel().getVesselClass().getMaxSpeed());
									final int travelTime = durationProvider.getElementDuration(prevElement, resource) + toCanal + panamaBookingsProvider.getMargin() + fromEntryPoint;

									if (booking.getBookingDate() + fromEntryPoint < windowEndTime[index]) {

										travelTimeData.setMinTravelTime(sequenceIndex, index - 1, travelTime);
										currentPortTimeWindowsRecord.setRouteOptionBooking(prevPortSlot, booking);
										assignedBookings.get(panamaEntry).add(booking);
										set.remove(booking);
										foundBooking = true;
										break;
									}
								}

								// if no booking was assigned and we are within the strict boundary, set time to direct
								if (!foundBooking && windowStartTime[index - 1] < panamaBookingsProvider.getStrictBoundary()) {
									travelTimeData.setMinTravelTime(sequenceIndex, index - 1, Math.min(suezTravelTime, directTravelTime));
									currentPortTimeWindowsRecord.setSlotNextVoyageOptions(prevPortSlot, AvailableRouteChoices.EXCLUDE_PANAMA);
								}

							}
						}

						windowStartTime[index] = Math.max(window.getInclusiveStart(), windowStartTime[index - 1] + travelTimeData.getMinTravelTime(sequenceIndex, index - 1));
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
				windowEndTime[index] = windowEndTime[index + 1] - travelTimeData.getMinTravelTime(sequenceIndex, index);
			} else if (!useTimeWindow[index + 1]) {
				windowEndTime[index] = Math.max(windowStartTime[index] + 1, Math.min(windowEndTime[index], windowEndTime[index + 1] - travelTimeData.getMinTravelTime(sequenceIndex, index)));
			}

			// Make sure end if >= start - this may shift the end forward again violating min travel time.
			windowEndTime[index] = Math.max(windowStartTime[index] + 1, windowEndTime[index]);
		}

		// For charter outs where event, virtual, other, copy the event window forward
		for (index = 1; index < size; ++index) {
			if (isVirtual[index] || isVirtual[index - 1]) {
				windowStartTime[index] = windowStartTime[index - 1];
				windowEndTime[index] = windowEndTime[index - 1];
			}
		}

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
		resize(windowStartTime, sequenceIndex, size);
		resize(windowEndTime, sequenceIndex, size);

		resize(isVirtual, sequenceIndex, size);
		resize(useTimeWindow, sequenceIndex, size);
		resize(actualisedTimeWindow, sequenceIndex, size);
		resize(isRoundTripEnd, sequenceIndex, size);

		sizes[sequenceIndex] = size;
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
	private final void refineWindows(final int seqIndex, final MinTravelTimeData travelTimeData) {
		final int[] startTimes = windowStartTime[seqIndex];
		final int[] endTimes = windowEndTime[seqIndex];
		final boolean[] useRawTimeWindow = useTimeWindow[seqIndex];
		final boolean[] actualiseTimeWindows = this.actualisedTimeWindow[seqIndex];
		// time windows after the first one have their start time clipped, so
		// they don't start any earlier
		// than you could get to them without being late.
		for (int i = 1; i < startTimes.length; i++) {
			if (actualiseTimeWindows[i] && !useRawTimeWindow[i]) {
				startTimes[i] = Math.max(startTimes[i], startTimes[i - 1] + travelTimeData.getMinTravelTime(seqIndex, i - 1));
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
				endTimes[i] = Math.max(startTimes[i] + 1, Math.min(endTimes[i], endTimes[i + 1] - travelTimeData.getMinTravelTime(seqIndex, i)));
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
	private final void imposeShipToShipConstraints(final MinTravelTimeData travelTimeData) {
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
					refineWindows(discharge_seq, travelTimeData);
				}
				if (recalculateLoad) {
					refineWindows(load_seq, travelTimeData);
				}
			}
			max_iterations--;
		} while ((recalculateDischarge || recalculateLoad) && max_iterations > 0);

		if (max_iterations <= 0) {
			System.err.println("Something went wrong in the re-windowing of ship to ship transfers");
		}
	}

	/**
	 * For round trip (nominal) cargoes, we don't care what else has happened on this vessel
	 * 
	 * @param resource
	 * 
	 * @param portTimeWindowsRecord
	 * @param seqIndex
	 */
	private void setFeasibleTimeWindowsRoundTrip(IResource resource, final IPortTimeWindowsRecord portTimeWindowsRecord, final int seqIndex, final MinTravelTimeData travelTimeData) {
		int prevFeasibleWindowStart = IPortSlot.NO_PRICING_DATE;
		for (final IPortSlot portSlot : portTimeWindowsRecord.getSlots()) {
			final MutableTimeWindow timeWindow;
			int feasibleWindowStart, feasibleWindowEnd;
			if (portTimeWindowsRecord.getFirstSlot().equals(portSlot)) {
				// first load
				feasibleWindowStart = windowStartTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)];
				feasibleWindowEnd = windowEndTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)];
				timeWindow = new MutableTimeWindow(feasibleWindowStart, feasibleWindowEnd);
			} else {
				feasibleWindowStart = Math.max(windowStartTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)],
						prevFeasibleWindowStart + travelTimeData.getMinTravelTime(seqIndex, portTimeWindowsRecord.getIndex(portSlot) - 1));
				feasibleWindowEnd = Math.max(windowEndTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)], feasibleWindowStart + 1);
				timeWindow = new MutableTimeWindow(feasibleWindowStart, feasibleWindowEnd);
			}
			portTimeWindowsRecord.setSlotFeasibleTimeWindow(portSlot, timeWindow);
			prevFeasibleWindowStart = feasibleWindowStart;
		}
	}

	/**
	 * The previous cargo will have changed the constraints on the time windows, so we must find the new feasible time windows before choosing an arrival time
	 * 
	 * @param portTimeWindowsRecord
	 * @param seqIndex
	 */
	private void setFeasibleTimeWindowsUsingPrevious(final IResource resource, final IPortTimeWindowsRecord portTimeWindowsRecord, final int seqIndex, final MinTravelTimeData travelTimeData,
			final boolean isLast) {
		int prevFeasibleWindowStart = Integer.MIN_VALUE;
		boolean lastSlotWasVirtual = false;
		MutableTimeWindow rollingWindow = null;
		for (final IPortSlot portSlot : portTimeWindowsRecord.getSlots()) {

			final MutableTimeWindow timeWindow;
			int feasibleWindowStart, feasibleWindowEnd;
			if (portTimeWindowsRecord.getFirstSlot().equals(portSlot) && portTimeWindowsRecord.getIndex(portTimeWindowsRecord.getFirstSlot()) > 0) {
				// first load
				feasibleWindowStart = getFeasibleWindowStart(portTimeWindowsRecord, seqIndex, travelTimeData);
				feasibleWindowEnd = getFeasibleWindowEnd(portTimeWindowsRecord, seqIndex, portSlot, feasibleWindowStart);
				timeWindow = new MutableTimeWindow(feasibleWindowStart, feasibleWindowEnd);
			} else {
				if (portSlot.getPortType() == PortType.Virtual || lastSlotWasVirtual) {
					timeWindow = rollingWindow;
					assert timeWindow != null;
					feasibleWindowStart = timeWindow.getInclusiveStart();
					feasibleWindowEnd = timeWindow.getExclusiveEnd();
				} else {

					if (prevFeasibleWindowStart == Integer.MIN_VALUE) {
						feasibleWindowStart = windowStartTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)];
						feasibleWindowEnd = windowEndTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)];
					} else {
						feasibleWindowStart = Math.max(windowStartTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)],
								prevFeasibleWindowStart + travelTimeData.getMinTravelTime(seqIndex, portTimeWindowsRecord.getIndex(portSlot) - 1));
						feasibleWindowEnd = Math.max(windowEndTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)], feasibleWindowStart + 1);
					}
					timeWindow = new MutableTimeWindow(feasibleWindowStart, feasibleWindowEnd);
				}
			}
			portTimeWindowsRecord.setSlotFeasibleTimeWindow(portSlot, timeWindow);
			assert windowStartTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)] == feasibleWindowStart;
			assert windowEndTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)] == feasibleWindowEnd;
			prevFeasibleWindowStart = feasibleWindowStart;

			rollingWindow = timeWindow;
			lastSlotWasVirtual = portSlot.getPortType() == PortType.Virtual;

		}
		if (isLast && portTimeWindowsRecord.getReturnSlot() != null) {
			final IPortSlot portSlot = portTimeWindowsRecord.getReturnSlot();
			int feasibleWindowStart, feasibleWindowEnd;
			if (prevFeasibleWindowStart == Integer.MIN_VALUE) {
				feasibleWindowStart = windowStartTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)];
				feasibleWindowEnd = windowEndTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)];
			} else {
				feasibleWindowStart = Math.max(windowStartTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)],
						prevFeasibleWindowStart + travelTimeData.getMinTravelTime(seqIndex, portTimeWindowsRecord.getIndex(portSlot) - 1));
				feasibleWindowEnd = Math.max(windowEndTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)], feasibleWindowStart + 1);
			}

			// ; look at the original code, what is the end timw window
			// is maria energy optional?
			// This vessel is empty - we skipped an earlier "skip resource" check - is this important here?
			// Finally - port rotations is wrong order (sort by next./prev then date)
			//
			final MutableTimeWindow timeWindow = new MutableTimeWindow(feasibleWindowStart, feasibleWindowEnd);
			portTimeWindowsRecord.setSlotFeasibleTimeWindow(portSlot, timeWindow);
		}
	}

	private int getFeasibleWindowEnd(final IPortTimeWindowsRecord portTimeWindowsRecord, final int seqIndex, final IPortSlot portSlot, final int feasibleWindowStart) {
		return Math.max(feasibleWindowStart + 1, windowEndTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)]);
	}

	private int getFeasibleWindowStart(final IPortTimeWindowsRecord portTimeWindowsRecord, final int seqIndex, final MinTravelTimeData travelTimeData) {
		int prevIndex = portTimeWindowsRecord.getIndex(portTimeWindowsRecord.getFirstSlot()) - 1;
		return Math.max(windowStartTime[seqIndex][prevIndex] + travelTimeData.getMinTravelTime(seqIndex, prevIndex),
				windowStartTime[seqIndex][portTimeWindowsRecord.getIndex(portTimeWindowsRecord.getFirstSlot())]);
	}

	private boolean isSequentialVessel(final IResource resource) {
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
			return false;
		}
		return true;
	}

	public void setTrimByPanamaCanalBookings(boolean value) {
		checkPanamaCanalBookings = value;
	}
}
