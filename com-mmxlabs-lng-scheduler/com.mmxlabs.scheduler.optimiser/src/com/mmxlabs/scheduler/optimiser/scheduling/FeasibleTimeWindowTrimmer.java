/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.RoundTripCargoEnd;
import com.mmxlabs.scheduler.optimiser.fitness.util.SequenceEvaluationUtils;
import com.mmxlabs.scheduler.optimiser.providers.ECanalEntry;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IElementPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.schedule.PanamaBookingHelper;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PanamaPeriod;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimeWindowsRecord;

/**
 * A sequence scheduler which enumerates possible combinations of arrival times explicitly, rather than using the GA byte array decoding method. This should be subclassable into a random sequence
 * scheduler as well, with reduced decoding overhead
 * 
 * @author hinton
 * 
 */
public class FeasibleTimeWindowTrimmer {

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
	private boolean[] isRoundTripEnd;
	/**
	 * The start times of each window, appropriately `clipped' to deal with infeasible choices or null time windows.
	 */
	private int[] windowStartTime;
	/**
	 * The end times of each window, similar to start times.
	 */
	private int[] windowEndTime;

	private int[] visitDuration;
	private PortTimeWindowsRecord[] recordsByIndex;

	/**
	 * Whether or not the {@link PortType} of any {@link PortSlot} associated with each sequence element has {@link PortType} Virtual. If a sequence element is virtual, the travel times should be held
	 * at zero and the virtual element and its neighbors should have the same time window.
	 * 
	 * Thus if we see A -> virtual -> B, the arrival time at B should be clamped to the arrival time at A. This is done by checks in the {@link #getMinArrivalTime(int, int)} and
	 * {@link #getMaxArrivalTime(int, int)} methods.
	 */
	private boolean[] isVirtual;

	/**
	 * A flag to indicate that we should just use the timewindow rather than include the previous journey time. Intended for use with the cargo shorts where each cargo is indepenedent of the others on
	 * the route. Also used for actuals where forcast travel time is irrelevant.
	 */
	private boolean[] useTimeWindow;
	/**
	 * Boolean indicating time is actualised and cannot be changed.
	 */
	private boolean[] actualisedTimeWindow;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private IElementPortProvider portProvider;

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

	@Inject
	private PanamaBookingHelper panamaBookingsHelper;

	private final TimeWindow defaultStartWindow = new TimeWindow(0, Integer.MAX_VALUE);

	public FeasibleTimeWindowTrimmer() {
		super();
	}

	public final List<IPortTimeWindowsRecord> generateTrimmedWindows(final IResource resource, final ISequence sequence, final MinTravelTimeData travelTimeData,
			final CurrentBookingData currentBookingData) {

		final LinkedList<IPortTimeWindowsRecord> portTimeWindowRecords = new LinkedList<IPortTimeWindowsRecord>();
		trimBasedOnMaxSpeed(resource, sequence, portTimeWindowRecords, travelTimeData, currentBookingData);

		if (checkPanamaCanalBookings) {
			trimBasedOnPanamaCanal(resource, sequence, portTimeWindowRecords, travelTimeData, currentBookingData);
		}

		for (int idx = 0; idx < portTimeWindowRecords.size(); idx++) {
			final IPortTimeWindowsRecord portTimeWindowsRecord = portTimeWindowRecords.get(idx);
			if (isSequentialVessel(resource)) {
				final boolean isLastRecord = idx == portTimeWindowRecords.size() - 1;
				setFeasibleTimeWindowsUsingPrevious(resource, portTimeWindowsRecord, travelTimeData, isLastRecord);
			} else {
				setFeasibleTimeWindowsRoundTrip(resource, portTimeWindowsRecord, travelTimeData);
			}

		}
		return portTimeWindowRecords;

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

	protected final void trimBasedOnMaxSpeed(final @NonNull IResource resource, final @NonNull ISequence sequence, final List<IPortTimeWindowsRecord> portTimeWindowRecords,
			final MinTravelTimeData travelTimeData, final CurrentBookingData currentBookings) {

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

		resizeAll(size);

		final boolean isRoundTripSequence = vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP;
		final boolean isSpotCharter = vesselAvailability.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER;

		final boolean[] breakSequence = findSequenceBreaks(sequence, isRoundTripSequence);

		final int vesselMaxSpeed = vesselAvailability.getVessel().getMaxSpeed();

		int index = 0;
		ISequenceElement prevElement = null;
		IPortSlot prevPortSlot = null;
		// Used for end of sequence checks
		PortTimeWindowsRecord portTimeWindowsRecord = new PortTimeWindowsRecord();

		// first pass, collecting start time windows
		for (final ISequenceElement element : sequence) {
			final IPortSlot portSlot = portSlotProvider.getPortSlot(element);

			final IPortSlot thisPortSlot = portSlotProvider.getPortSlot(element);
			visitDuration[index] = actualsDataProvider.hasActuals(thisPortSlot) ? actualsDataProvider.getVisitDuration(thisPortSlot) : durationProvider.getElementDuration(element, resource);

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
				final IEndRequirement endRequirement = startEndRequirementProvider.getEndRequirement(resource);
				if (endRequirement.isMaxDurationSet()) {
					window = getTrimmedWindowBasedOnMaxDuration(endRequirement, index);
				} else {
					window = endRequirement.getTimeWindow();
				}
				assert window != null;// End requirements should always have a window.
			} else if (thisPortSlot instanceof RoundTripCargoEnd) {
				isRoundTripEnd[index] = true;
				// If this is null, the we will force discharge arrival as early as possible, regardless of cost (or booking availability)
				window = null;// new TimeWindow(0, Integer.MAX_VALUE);
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
			}

			isRoundTripEnd[index] = false;
			if (breakSequence[index]) {
				// last slot in plan, set return
				portTimeWindowsRecord.setReturnSlot(thisPortSlot, null, visitDuration[index], index);
				// finalise record
				portTimeWindowRecords.add(portTimeWindowsRecord);
				// create new record
				if (!(thisPortSlot instanceof RoundTripCargoEnd)) {
					portTimeWindowsRecord = new PortTimeWindowsRecord();
					portTimeWindowsRecord.setSlot(thisPortSlot, null, visitDuration[index], index);
				} else {
					isRoundTripEnd[index] = true;
				}
			} else {
				if (!(prevPortSlot instanceof RoundTripCargoEnd)) {
					portTimeWindowsRecord.setSlot(thisPortSlot, null, visitDuration[index], index);
				} else {
					portTimeWindowsRecord = new PortTimeWindowsRecord();
					portTimeWindowsRecord.setSlot(thisPortSlot, null, visitDuration[index], index);
					isRoundTripEnd[index] = true;
				}
			}

			recordsByIndex[index] = portTimeWindowsRecord;

			isVirtual[index] = portTypeProvider.getPortType(element) == PortType.Virtual;
			useTimeWindow[index] = prevElement == null ? false : portTypeProvider.getPortType(prevElement) == PortType.Round_Trip_Cargo_End;

			// Calculate minimum inter-element durations
			travelTimeData.setMinTravelTime(index, visitDuration[index]);
			int directTravelTime = Integer.MAX_VALUE;
			int suezTravelTime = Integer.MAX_VALUE;
			int panamaTravelTime = Integer.MAX_VALUE;
			if (prevElement != null) {

				final IPort prevPort = portProvider.getPortForElement(prevElement);
				final IPort port = portProvider.getPortForElement(element);

				final int prevVisitDuration = visitDuration[index - 1];

				directTravelTime = distanceProvider.getTravelTime(ERouteOption.DIRECT, vesselAvailability.getVessel(), prevPort, port, vesselMaxSpeed);
				suezTravelTime = distanceProvider.getTravelTime(ERouteOption.SUEZ, vesselAvailability.getVessel(), prevPort, port, vesselMaxSpeed);
				panamaTravelTime = distanceProvider.getTravelTime(ERouteOption.PANAMA, vesselAvailability.getVessel(), prevPort, port, vesselMaxSpeed);

				if (directTravelTime != Integer.MAX_VALUE) {
					directTravelTime += prevVisitDuration;
				}
				if (suezTravelTime != Integer.MAX_VALUE) {
					suezTravelTime += prevVisitDuration;
				}
				if (panamaTravelTime != Integer.MAX_VALUE) {
					panamaTravelTime += prevVisitDuration;
				}

				final int minTravelTime = Math.min(directTravelTime, Math.min(panamaTravelTime, suezTravelTime));

				// final int currentTime = travelTimeData.getMinTravelTime(sequenceIndex, index - 1);
				travelTimeData.setMinTravelTime(index - 1, minTravelTime);
				travelTimeData.setTravelTime(ERouteOption.DIRECT, index - 1, directTravelTime);
				travelTimeData.setTravelTime(ERouteOption.SUEZ, index - 1, suezTravelTime);
				travelTimeData.setTravelTime(ERouteOption.PANAMA, index - 1, panamaTravelTime);
			}

			// Handle time windows
			if (window == null) { // empty time windows are made to be the
									// biggest reasonable gap
				if (index > 0) {
					// clip start of time window
					windowStartTime[index] = windowStartTime[index - 1] + travelTimeData.getMinTravelTime(index - 1);
					// backwards pass will fix this.
					if (isRoundTripEnd[index]) {
						// FIXME: This forces the previous discharge to be as early as possible. Pick a suitable window size! (windowEndTime[index-1] + minTravelTime should work as PTRMaker
						// selects
						// quickest travel time)
						windowEndTime[index] = windowStartTime[index] + 1 + EMPTY_WINDOW_SIZE;
						// The fix! Note price schedule needs updating otherwise we slow down too much,
						// windowEndTime[index] = windowEndTime[index - 1] + 2 * travelTimeData.getMinTravelTime(index - 1);
					} else {
						// We may get here for e.g. relocatable charter out events.
						windowEndTime[index] = windowStartTime[index] + 1 + EMPTY_WINDOW_SIZE;
					}
				} else {
					windowStartTime[index] = 0;
					windowEndTime[index] = 1 + sequence.size() == 1 ? 0 : EMPTY_WINDOW_SIZE;
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
						assert prevElement != null;
						windowStartTime[index] = Math.max(window.getInclusiveStart(), windowStartTime[index - 1] + travelTimeData.getMinTravelTime(index - 1));
						windowEndTime[index] = Math.max(windowEndTime[index], windowStartTime[index] + 1);

					}

				}
			}

			index++;
			prevElement = element;
			prevPortSlot = thisPortSlot;
		}

		// Do this bit twice, first time to correctly align the start/end boundaries of the time windows, then to run again after max duration trimming
		for (int pass = 0; pass < 2; ++pass) {

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
					windowEndTime[index] = windowEndTime[index + 1] - travelTimeData.getMinTravelTime(index);
				} else if (!useTimeWindow[index + 1]) {
					windowEndTime[index] = Math.max(windowStartTime[index] + 1, Math.min(windowEndTime[index], windowEndTime[index + 1] - travelTimeData.getMinTravelTime(index)));
				}

				// Make sure end if >= start - this may shift the end forward again violating min travel time.
				windowEndTime[index] = Math.max(windowStartTime[index] + 1, windowEndTime[index]);
			}
			if (pass == 0) {
				if (sequence.size() > 0) {
					// Apply max duration cutoff to the last element of the sequence
					trimBasedOnMaxDuration(resource, size - 1);
					// Try to trim down the start event upper bound given the minimal duration if it is set
					trimBasedOnMinDuration(resource, size - 1);
				}
			}

		}

		for (index = 1; index < size; ++index) {
			assert windowStartTime[index] < windowEndTime[index];
			if (!isRoundTripEnd[index - 1]) {
				assert windowStartTime[index - 1] <= windowStartTime[index];
				assert windowEndTime[index - 1] <= windowEndTime[index];
			}
		}

		// For charter outs where event, virtual, other, copy the event window forward
		for (index = 1; index < size; ++index) {
			if (isVirtual[index] || isVirtual[index - 1]) {
				windowStartTime[index] = windowStartTime[index - 1];
				windowEndTime[index] = windowEndTime[index - 1];
			}
		}
	}

	protected final void trimBasedOnPanamaCanal(final @NonNull IResource resource, final @NonNull ISequence sequence, final List<IPortTimeWindowsRecord> portTimeWindowRecords,
			final MinTravelTimeData travelTimeData, final CurrentBookingData currentBookings) {

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
			// No shipping
			return;
		}

		final int size = sequence.size();
		// filters out solutions with less than 2 elements (i.e. spot charters, etc.)
		if (SequenceEvaluationUtils.shouldIgnoreSequence(sequence)) {
			return;
		}

		final boolean isRoundTripSequence = vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP;
		if (isRoundTripSequence) {
			// No panama bookings.
			return;
		}

		// Make sure we can schedule stuff on Panama
		IVessel vessel = vesselAvailability.getVessel();
		if (!distanceProvider.isRouteAvailable(ERouteOption.PANAMA, vessel)) {
			return;
		}

		final int vesselMinSpeed = vessel.getMinSpeed();
		final int vesselMaxSpeed = vessel.getMaxSpeed();
		boolean changed = false;
		// Two passes - pass 0 allocate required panama voyages.
		// - pass 1 force direct/suez where possible
		for (int pass = 0; pass < 2; ++pass) {
			if (changed) {
				// Keep in the current pass until we stop changing stuff.
				--pass;
				changed = false;
			}
			int index = 0;
			ISequenceElement prevElement = null;
			IPortSlot prevPortSlot = null;

			// first pass, collecting start time windows
			for (final ISequenceElement element : sequence) {

				final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
				final IPortSlot thisPortSlot = portSlotProvider.getPortSlot(element);

				if (index > 0 && !isRoundTripEnd[index] && !useTimeWindow[index]) {
					assert prevElement != null;
					windowStartTime[index] = Math.max(windowStartTime[index], windowStartTime[index - 1] + travelTimeData.getMinTravelTime(index - 1));

					if (!isRoundTripSequence && checkPanamaCanalBookings) {

						final PortTimeWindowsRecord currentPortTimeWindowsRecord = recordsByIndex[index - 1];

						// Already processed?
						if (currentPortTimeWindowsRecord.getSlotNextVoyageOptions(prevPortSlot) != AvailableRouteChoices.OPTIMAL
								|| currentPortTimeWindowsRecord.getSlotIsNextVoyageConstrainedPanama(prevPortSlot)) {
							
							windowStartTime[index] = Math.max(windowStartTime[index], windowStartTime[index - 1] + travelTimeData.getMinTravelTime(index - 1));
							windowEndTime[index] = Math.max(windowEndTime[index], windowStartTime[index] + 1);
							
							// Choice already allocation, skip
							index++;
							prevElement = element;
							prevPortSlot = thisPortSlot;
							
							
							continue;
						}

						final IPortSlot p_prevPortSlot = prevPortSlot;
						final int directTravelTime = travelTimeData.getTravelTime(ERouteOption.DIRECT, index - 1);
						final int suezTravelTime = travelTimeData.getTravelTime(ERouteOption.SUEZ, index - 1);
						final int panamaTravelTime = travelTimeData.getTravelTime(ERouteOption.PANAMA, index - 1);

						if (panamaTravelTime == Integer.MAX_VALUE) {
							// No Panama route, so an easy decision
							travelTimeData.setMinTravelTime(index - 1, Math.min(suezTravelTime, directTravelTime));
							currentPortTimeWindowsRecord.setSlotNextVoyageOptions(prevPortSlot, AvailableRouteChoices.EXCLUDE_PANAMA, PanamaPeriod.Beyond);
							changed = true;
						} else {
							@Nullable
							final ECanalEntry routeOptionEntry = distanceProvider.getRouteOptionCanalEntrance(prevPortSlot.getPort(), ERouteOption.PANAMA);
							if (routeOptionEntry != null) {

								final int toCanal = visitDuration[index - 1] + panamaBookingsHelper.getTravelTimeToCanal(vessel, prevPortSlot.getPort(), true);
								final int fromEntryPoint = panamaBookingsHelper.getTravelTimeFromCanalEntry(vessel, routeOptionEntry, portSlot.getPort());

								boolean northBound = distanceProvider.getRouteOptionDirection(prevPortSlot.getPort(), ERouteOption.PANAMA) == IDistanceProvider.RouteOptionDirection.NORTHBOUND;

								int endTime = windowEndTime[index];
								if (endTime == Integer.MAX_VALUE) {
									// No window end, so estimate one. At worst we will travel at min speed.
									int slowPanamaTime = distanceProvider.getTravelTime(ERouteOption.PANAMA, vessel, prevPortSlot.getPort(), portSlot.getPort(), vesselMinSpeed);

									if (northBound) {
										slowPanamaTime += panamaBookingsProvider.getNorthboundMaxIdleDays() * 24;
									} else {
										slowPanamaTime += panamaBookingsProvider.getMarginInHours();
									}
									endTime = windowEndTime[index - 1] + visitDuration[index - 1] + slowPanamaTime;
								}
								int latestPanamaTime = endTime - fromEntryPoint - (northBound ? panamaBookingsProvider.getNorthboundMaxIdleDays() * 24 : panamaBookingsProvider.getMarginInHours()); // Include
								// 3am?

								PanamaPeriod panamaPeriod;
								if (latestPanamaTime > panamaBookingsProvider.getRelaxedBoundary()) {
									panamaPeriod = PanamaPeriod.Beyond;
									// For windows crossing boundaries we need to ensure the northbound idle days are taken into account but are not enforced yet.
									// The price based trimmer (which we expect to be used with panama) will refine this further later on
									if (windowStartTime[index - 1] + toCanal <= panamaBookingsProvider.getRelaxedBoundary()) {
										// Reclassify southbound voyages as relaxed. Northbound we leave for the price based trimmer
										if (northBound) {
											int panamaIdleTime = panamaBookingsProvider.getNorthboundMaxIdleDays() * 24;
											currentPortTimeWindowsRecord.setSlotNextVoyageOptions(prevPortSlot, AvailableRouteChoices.OPTIMAL, PanamaPeriod.Beyond);
											// Notify price based trimmer of variable choice
											currentPortTimeWindowsRecord.setSlotAdditionalPanamaDetails(p_prevPortSlot, true, panamaIdleTime);
										} else {
											// Voyage could start in the relaxed period, so assume relaxed.
											panamaPeriod = PanamaPeriod.Relaxed;
										}

									} else {
									}
								} else if (latestPanamaTime > panamaBookingsProvider.getStrictBoundary()) {
									panamaPeriod = PanamaPeriod.Relaxed;
								} else {
									panamaPeriod = PanamaPeriod.Strict;
								}
								final ECanalEntry panamaEntry = routeOptionEntry;
								boolean bookingAllocated = false;
								if (pass == 0) {
									// Only check for actual bookings once

									final Optional<IRouteOptionBooking> potentialBooking = currentBookings.assignedBookings.computeIfAbsent(panamaEntry, k -> new ArrayList<>()).stream().filter(e -> {
										return e.getPortSlot().isPresent() && e.getPortSlot().get().equals(p_prevPortSlot);
									}).findFirst();

									if (potentialBooking.isPresent()
											&& isBetterThroughPanama(windowStartTime[index - 1], windowStartTime[index], panamaTravelTime, Math.min(directTravelTime, suezTravelTime))) {
										// window has a booking
										// currentPortTimeRecord.setRouteOptionBooking(prevPortSlot, potentialBooking.get());

										// check if it can be reached in time
										if (windowStartTime[index - 1] + toCanal <= potentialBooking.get().getBookingDate() &&potentialBooking.get().getBookingDate() + fromEntryPoint < windowEndTime[index]) {
											currentPortTimeWindowsRecord.setSlotNextVoyageOptions(prevPortSlot, AvailableRouteChoices.PANAMA_ONLY, panamaPeriod);
											currentPortTimeWindowsRecord.setRouteOptionBooking(prevPortSlot, potentialBooking.get());

											// Visit duration should implicitly be included in this calculation.

											final int travelTime = toCanal + fromEntryPoint;
											travelTimeData.setMinTravelTime(index - 1, travelTime);

											// Adjust origin window end
											windowEndTime[index - 1] = Math.min(potentialBooking.get().getBookingDate() + 1 - toCanal, windowEndTime[index - 1]);
											assert windowStartTime[index - 1] < windowEndTime[index - 1];

											// Adjust destination window start
											windowStartTime[index] = Math.max(windowStartTime[index], potentialBooking.get().getBookingDate() + fromEntryPoint);
											assert windowStartTime[index] < windowEndTime[index];

											bookingAllocated = true;
											changed = true;
										} else {
											// // Booking can't be reached in time. Set to optimal time through panama and don't include slot.
											// No allocation is set, try and find an alternative.
										}
									}
								}
								if (bookingAllocated) {
									// No further checks.
								} else if (panamaPeriod == PanamaPeriod.Beyond) {
									// assume a Panama booking because it's far enough in the future
									currentPortTimeWindowsRecord.setSlotNextVoyageOptions(prevPortSlot, AvailableRouteChoices.OPTIMAL, panamaPeriod);
									// changed = true;
								} else if (windowStartTime[index - 1] + directTravelTime < windowEndTime[index] //
										|| (windowStartTime[index - 1] + suezTravelTime < windowEndTime[index] && suezTravelTime != Integer.MAX_VALUE) //
										|| directTravelTime <= panamaTravelTime) {

									if (pass != 0) {
										// In the first pass, do not set the voyage choice as this may cause problems further down the line (however, northbound Panama may be allowed)
										if (northBound) {
											// if northbound, allow Panama
											int panamaIdleTime = panamaBookingsProvider.getNorthboundMaxIdleDays() * 24;
											travelTimeData.setMinTravelTime(index - 1, Math.min(Math.min(suezTravelTime, directTravelTime), panamaTravelTime + panamaIdleTime));
											currentPortTimeWindowsRecord.setSlotNextVoyageOptions(p_prevPortSlot, AvailableRouteChoices.OPTIMAL, panamaPeriod);
											currentPortTimeWindowsRecord.setSlotAdditionalPanamaDetails(p_prevPortSlot, true, panamaIdleTime);
										} else {
											// journey can be made direct (or it does not go across Panama)
											travelTimeData.setMinTravelTime(index - 1, Math.min(suezTravelTime, directTravelTime));
											currentPortTimeWindowsRecord.setSlotNextVoyageOptions(p_prevPortSlot, AvailableRouteChoices.EXCLUDE_PANAMA, panamaPeriod);
										}
										changed = true;
									}

									// TODO: We could perform an additional pass and allocate a spare booking slot and set the rouce choice to optimal and let the schedule determine best route.
									// TODO: Note this would require the MinTravelTimeData object to be passed around for time calculations rather than use the distance provider to get non-booking
									// time.
									// TODO: Or we could re-calculate the travel time using the booking. (Maybe add API to distance provider to get travel time via booking?).
								} else {
									// go through panama, figure out if there is an unassigned booking
									currentPortTimeWindowsRecord.setSlotNextVoyageOptions(prevPortSlot, AvailableRouteChoices.PANAMA_ONLY, panamaPeriod);
									// TODO: this is a bit optimistic in case there is no booking ;-)
									travelTimeData.setMinTravelTime(index - 1, panamaTravelTime);
									changed = true;

									boolean foundBooking = false;
									assert prevElement != null;
									final List<IRouteOptionBooking> set = currentBookings.unassignedBookings.get(panamaEntry);
									if (set != null) {
										for (final IRouteOptionBooking booking : set) {
											final int canalTime = windowStartTime[index - 1] + toCanal;
											if (canalTime > booking.getBookingDate()) {
												// booking can't be reached. All following bookings are later and can't be reached either
												continue;
											}
											final int travelTime = toCanal + fromEntryPoint;

											if (booking.getBookingDate() + fromEntryPoint < windowEndTime[index]) {
												// destination can be reached from slot
												assert windowStartTime[index - 1] < windowEndTime[index - 1];
												windowEndTime[index - 1] = Math.min(booking.getBookingDate() + 1 - toCanal, windowEndTime[index - 1]);
												assert windowStartTime[index - 1] < windowEndTime[index - 1];
												windowStartTime[index] = Math.max(windowStartTime[index], booking.getBookingDate() + fromEntryPoint);
												assert windowStartTime[index] < windowEndTime[index]; 
												travelTimeData.setMinTravelTime(index - 1, travelTime);
												currentPortTimeWindowsRecord.setRouteOptionBooking(prevPortSlot, booking);
												currentBookings.assignedBookings.get(panamaEntry).add(booking);
												set.remove(booking);
												foundBooking = true;
												changed = true;
												break;
											}
										}
									}

									if (!foundBooking) {

										int delayedPanamaTravelTime = panamaTravelTime + panamaBookingsProvider.getNorthboundMaxIdleDays() * 24;

										// if no booking was assigned and we are within the strict boundary, set time to direct
										if (!northBound && panamaPeriod == PanamaPeriod.Strict) {
											travelTimeData.setMinTravelTime(index - 1, Math.min(suezTravelTime, directTravelTime));
											currentPortTimeWindowsRecord.setSlotNextVoyageOptions(prevPortSlot, AvailableRouteChoices.EXCLUDE_PANAMA, panamaPeriod);
											changed = true;
										}

										if (northBound && delayedPanamaTravelTime < Math.min(directTravelTime, suezTravelTime)) {
											travelTimeData.setMinTravelTime(index - 1, delayedPanamaTravelTime);
											currentPortTimeWindowsRecord.setSlotNextVoyageOptions(prevPortSlot, AvailableRouteChoices.PANAMA_ONLY, panamaPeriod);
											changed = true;
										} else if (northBound) {
											travelTimeData.setMinTravelTime(index - 1, Math.min(directTravelTime, suezTravelTime));
											currentPortTimeWindowsRecord.setSlotNextVoyageOptions(prevPortSlot, AvailableRouteChoices.EXCLUDE_PANAMA, panamaPeriod);
											changed = true;
										}
									}
								}
							}
						}
					}
					windowStartTime[index] = Math.max(windowStartTime[index], windowStartTime[index - 1] + travelTimeData.getMinTravelTime(index - 1));
					windowEndTime[index] = Math.max(windowEndTime[index], windowStartTime[index] + 1);
				}
				index++;
				prevElement = element;
				prevPortSlot = thisPortSlot;
			}

			// now perform reverse-pass to trim any overly late end times
			// (that is end times which would make us late at the next element)
			for (index = size - 2; index >= 0; index--) {

				// Trim window ends taking into account potential delays for using the panama canal. E.g. Northbound may introduce 5 day delay.
				// Consider voyage A then voyage B. Neither voyage needs panama from the windows alone. If A goes via panama, B could go direct or via panama with booking. However going direct on
				// voyage A can force B to go via panama miss the booking and pick up 5 day delay.
				// We can't easily manipulate the min travel time array as travel time is dependent on the actual departure time. Instead we can adjust the upper bound of departure time to take delays
				// into account.

				if (!isRoundTripSequence) { // Not for nominals
					// first pass, collecting start time windows
					prevElement = sequence.get(index);
					prevPortSlot = portSlotProvider.getPortSlot(prevElement);

					final ISequenceElement element = sequence.get(index + 1);
					final IPortSlot thisPortSlot = portSlotProvider.getPortSlot(element);
					final PortTimeWindowsRecord currentPortTimeWindowsRecord = recordsByIndex[index];

					// If we have already picked the route choice, travel time should have been updated.
					AvailableRouteChoices slotNextVoyageOptions = currentPortTimeWindowsRecord.getSlotNextVoyageOptions(prevPortSlot);
					if (slotNextVoyageOptions == AvailableRouteChoices.OPTIMAL && !useTimeWindow[index + 1]) {
						assert prevElement != null;

						final int directTravelTime = travelTimeData.getTravelTime(ERouteOption.DIRECT, index);
						final int suezTravelTime = travelTimeData.getTravelTime(ERouteOption.SUEZ, index);
						final int panamaTravelTime = travelTimeData.getTravelTime(ERouteOption.PANAMA, index);

						if (panamaTravelTime != Integer.MAX_VALUE) {

							@Nullable
							final ECanalEntry panamaEntry = distanceProvider.getRouteOptionCanalEntrance(prevPortSlot.getPort(), ERouteOption.PANAMA);
							if (panamaEntry != null) {
								boolean northBound = distanceProvider.getRouteOptionDirection(prevPortSlot.getPort(), ERouteOption.PANAMA) == IDistanceProvider.RouteOptionDirection.NORTHBOUND;
								//
								final int toCanal = visitDuration[index] + panamaBookingsHelper.getTravelTimeToCanal(vessel, prevPortSlot.getPort(), true);

								final int fromEntryPoint = panamaBookingsHelper.getTravelTimeFromCanalEntry(vessel, panamaEntry, thisPortSlot.getPort());
								int endTime = windowEndTime[index + 1];

								if (endTime != Integer.MAX_VALUE) {
									// Calculate latest panama arrival time. This includes excess delays. Will be adjusted based on available bookings later
									int latestPanamaTime = endTime - fromEntryPoint;
									if (northBound) {
										latestPanamaTime -= panamaBookingsProvider.getNorthboundMaxIdleDays() * 24;
									} else {
										latestPanamaTime -= panamaBookingsProvider.getMarginInHours();
									}

									// Compute panama period (for reverse pass)
									final PanamaPeriod panamaPeriod = panamaBookingsHelper.getPanamaPeriod(latestPanamaTime);
									if (panamaPeriod != PanamaPeriod.Beyond) {
										final List<IRouteOptionBooking> set = currentBookings.unassignedBookings.get(panamaEntry);
										if (set != null) {
											for (final IRouteOptionBooking booking : set) {
												final int canalTime = windowStartTime[index] + toCanal;
												if (canalTime > booking.getBookingDate()) {
													// booking can't be reached.
													continue;
												}

												// Find the latest possible booking date.
												if (booking.getBookingDate() + fromEntryPoint < endTime) {
													int time = booking.getBookingDate() - panamaBookingsProvider.getMarginInHours();
													if (time > latestPanamaTime) {
														latestPanamaTime = time;
													}
												}
											}
										}
										// If the new panama time is the fastest, update the window.
										int newPanamaTime = endTime - (latestPanamaTime - toCanal);
										if (newPanamaTime < Math.min(directTravelTime, suezTravelTime)) {
											if (endTime - newPanamaTime < windowEndTime[index]) {
												windowEndTime[index] = endTime - newPanamaTime;
												// Mark current pass as changed. If pass 0 then we may have caused more forced voyages.
												changed = true;
											}
										}
									}
								}
							}
						}
					}
				}

				// trim the end of this time window so that the next element is
				// reachable without lateness
				// (but never so that the end time is before the start time)
				if (actualisedTimeWindow[index] && actualisedTimeWindow[index + 1]) {
					// Skip, windows should already match.
				} else if (actualisedTimeWindow[index + 1] && !actualisedTimeWindow[index]) {
					// Current window if flexible, next window is fixed, bring end window back
					windowEndTime[index] = windowEndTime[index + 1] - travelTimeData.getMinTravelTime(index);
				} else if (!useTimeWindow[index + 1]) {
					windowEndTime[index] = Math.max(windowStartTime[index] + 1, Math.min(windowEndTime[index], windowEndTime[index + 1] - travelTimeData.getMinTravelTime(index)));
				}

				// Make sure end if >= start - this may shift the end forward again violating min travel time.
				windowEndTime[index] = Math.max(windowStartTime[index] + 1, windowEndTime[index]);
			}

			if (sequence.size() > 0) {
				// Apply max duration cutoff to the last element of the sequence
				trimBasedOnMaxDuration(resource, size - 1);
				// Try to trim down the start event upper bound given the minimal duration if it is set
				trimBasedOnMinDuration(resource, size - 1);
			}

			// For charter outs where event, virtual, other, copy the event window forward
			for (index = 1; index < size; ++index) {
				if (isVirtual[index] || isVirtual[index - 1]) {
					windowStartTime[index] = windowStartTime[index - 1];
					windowEndTime[index] = windowEndTime[index - 1];
				}
			}
		}

	}

	/**
	 * Check whether we should go through Panama if we've made a booking. For example, we shouldn't go through if we will be late through Panama and can arrive on time direct or suez.
	 * 
	 * @param start
	 * @param end
	 * @param panamaTravelTime
	 * @param minOtherTravel
	 * @return
	 */
	private boolean isBetterThroughPanama(int start, int end, int panamaTravelTime, int minOtherTravel) {
		if (minOtherTravel == Integer.MAX_VALUE) {
			return true;
		} else if (panamaTravelTime == Integer.MAX_VALUE) {
			return false;
		} else if ((start + panamaTravelTime) < end) {
			return true;
		} else if (panamaTravelTime <= minOtherTravel) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Resize all the integer buffers for a given route
	 * 
	 * @param arrayIndex
	 * @param size
	 */
	private final void resizeAll(final int size) {
		if (windowStartTime == null || windowStartTime.length < size) {
			windowStartTime = new int[size];
		}
		if (windowEndTime == null || windowEndTime.length < size) {
			windowEndTime = new int[size];
		}
		if (visitDuration == null || visitDuration.length < size) {
			visitDuration = new int[size];
		}
		if (isVirtual == null || isVirtual.length < size) {
			isVirtual = new boolean[size];
		}
		if (useTimeWindow == null || useTimeWindow.length < size) {
			useTimeWindow = new boolean[size];
		}
		if (actualisedTimeWindow == null || actualisedTimeWindow.length < size) {
			actualisedTimeWindow = new boolean[size];
		}
		if (isRoundTripEnd == null || isRoundTripEnd.length < size) {
			isRoundTripEnd = new boolean[size];
		}
		if (recordsByIndex == null || recordsByIndex.length < size) {
			recordsByIndex = new PortTimeWindowsRecord[size];
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
	private void setFeasibleTimeWindowsRoundTrip(final IResource resource, final IPortTimeWindowsRecord portTimeWindowsRecord, final MinTravelTimeData travelTimeData) {
		int prevFeasibleWindowStart = IPortSlot.NO_PRICING_DATE;
		for (final IPortSlot portSlot : portTimeWindowsRecord.getSlots()) {
			final TimeWindow timeWindow;
			int feasibleWindowStart, feasibleWindowEnd;
			if (portTimeWindowsRecord.getFirstSlot().equals(portSlot)) {
				// first load
				feasibleWindowStart = windowStartTime[portTimeWindowsRecord.getIndex(portSlot)];
				feasibleWindowEnd = windowEndTime[portTimeWindowsRecord.getIndex(portSlot)];
				timeWindow = new TimeWindow(feasibleWindowStart, feasibleWindowEnd);
			} else {
				feasibleWindowStart = Math.max(windowStartTime[portTimeWindowsRecord.getIndex(portSlot)],
						prevFeasibleWindowStart + travelTimeData.getMinTravelTime(portTimeWindowsRecord.getIndex(portSlot) - 1));
				feasibleWindowEnd = Math.max(windowEndTime[portTimeWindowsRecord.getIndex(portSlot)], feasibleWindowStart + 1);
				timeWindow = new TimeWindow(feasibleWindowStart, feasibleWindowEnd);
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
	private void setFeasibleTimeWindowsUsingPrevious(final IResource resource, final IPortTimeWindowsRecord portTimeWindowsRecord, final MinTravelTimeData travelTimeData, final boolean isLast) {
		int prevFeasibleWindowStart = Integer.MIN_VALUE;
		boolean lastSlotWasVirtual = false;
		TimeWindow rollingWindow = null;
		for (final IPortSlot portSlot : portTimeWindowsRecord.getSlots()) {

			final TimeWindow timeWindow;
			int feasibleWindowStart, feasibleWindowEnd;
			if (portTimeWindowsRecord.getFirstSlot().equals(portSlot) && portTimeWindowsRecord.getIndex(portTimeWindowsRecord.getFirstSlot()) > 0) {
				// first load
				feasibleWindowStart = getFeasibleWindowStart(portTimeWindowsRecord, travelTimeData);
				feasibleWindowEnd = getFeasibleWindowEnd(portTimeWindowsRecord, portSlot, feasibleWindowStart);
				timeWindow = new TimeWindow(feasibleWindowStart, feasibleWindowEnd);
			} else {
				if (portSlot.getPortType() == PortType.Virtual || lastSlotWasVirtual) {
					timeWindow = rollingWindow;
					assert timeWindow != null;
					feasibleWindowStart = timeWindow.getInclusiveStart();
					feasibleWindowEnd = timeWindow.getExclusiveEnd();
				} else {

					if (prevFeasibleWindowStart == Integer.MIN_VALUE) {
						feasibleWindowStart = windowStartTime[portTimeWindowsRecord.getIndex(portSlot)];
						feasibleWindowEnd = windowEndTime[portTimeWindowsRecord.getIndex(portSlot)];
					} else {
						feasibleWindowStart = Math.max(windowStartTime[portTimeWindowsRecord.getIndex(portSlot)],
								prevFeasibleWindowStart + travelTimeData.getMinTravelTime(portTimeWindowsRecord.getIndex(portSlot) - 1));
						feasibleWindowEnd = Math.max(windowEndTime[portTimeWindowsRecord.getIndex(portSlot)], feasibleWindowStart + 1);
					}
					timeWindow = new TimeWindow(feasibleWindowStart, feasibleWindowEnd);
				}
			}
			portTimeWindowsRecord.setSlotFeasibleTimeWindow(portSlot, timeWindow);
			assert windowStartTime[portTimeWindowsRecord.getIndex(portSlot)] == feasibleWindowStart;
			assert windowEndTime[portTimeWindowsRecord.getIndex(portSlot)] == feasibleWindowEnd;
			prevFeasibleWindowStart = feasibleWindowStart;

			rollingWindow = timeWindow;
			lastSlotWasVirtual = portSlot.getPortType() == PortType.Virtual;

		}
		if (isLast && portTimeWindowsRecord.getReturnSlot() != null) {
			final IPortSlot portSlot = portTimeWindowsRecord.getReturnSlot();
			int feasibleWindowStart, feasibleWindowEnd;
			if (prevFeasibleWindowStart == Integer.MIN_VALUE) {
				feasibleWindowStart = windowStartTime[portTimeWindowsRecord.getIndex(portSlot)];
				feasibleWindowEnd = windowEndTime[portTimeWindowsRecord.getIndex(portSlot)];
			} else {
				feasibleWindowStart = Math.max(windowStartTime[portTimeWindowsRecord.getIndex(portSlot)],
						prevFeasibleWindowStart + travelTimeData.getMinTravelTime(portTimeWindowsRecord.getIndex(portSlot) - 1));
				feasibleWindowEnd = Math.max(windowEndTime[portTimeWindowsRecord.getIndex(portSlot)], feasibleWindowStart + 1);
			}

			// ; look at the original code, what is the end timw window
			// is maria energy optional?
			// This vessel is empty - we skipped an earlier "skip resource" check - is this important here?
			// Finally - port rotations is wrong order (sort by next./prev then date)
			//
			final TimeWindow timeWindow = new TimeWindow(feasibleWindowStart, feasibleWindowEnd);
			portTimeWindowsRecord.setSlotFeasibleTimeWindow(portSlot, timeWindow);
		}
	}

	private int getFeasibleWindowEnd(final @NonNull IPortTimeWindowsRecord portTimeWindowsRecord, final @NonNull IPortSlot portSlot, final int feasibleWindowStart) {
		return Math.max(feasibleWindowStart + 1, windowEndTime[portTimeWindowsRecord.getIndex(portSlot)]);
	}

	private int getFeasibleWindowStart(final @NonNull IPortTimeWindowsRecord portTimeWindowsRecord, final @NonNull MinTravelTimeData travelTimeData) {
		final int prevIndex = portTimeWindowsRecord.getIndex(portTimeWindowsRecord.getFirstSlot()) - 1;
		return Math.max(windowStartTime[prevIndex] + travelTimeData.getMinTravelTime(prevIndex), windowStartTime[portTimeWindowsRecord.getIndex(portTimeWindowsRecord.getFirstSlot())]);
	}

	private boolean isSequentialVessel(final IResource resource) {
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
			return false;
		}
		return true;
	}

	public void setTrimByPanamaCanalBookings(final boolean value) {
		checkPanamaCanalBookings = value;
	}

	protected final void trimBasedOnMinDuration(final @NonNull IResource resource, final int index) {
		final IEndRequirement requirement = startEndRequirementProvider.getEndRequirement(resource);
		if (requirement != null) {
			if (requirement.isMinDurationSet()) {
				ITimeWindow window = getTrimmedWindowBasedOnMinDuration(requirement);
				windowStartTime[index] = Math.max(windowStartTime[index], window.getInclusiveStart());
				windowEndTime[index] = Math.max(windowEndTime[index], windowStartTime[index] + 1);
			}
		}
	}

	protected final void trimBasedOnMaxDuration(final @NonNull IResource resource, final int index) {
		final IEndRequirement requirement = startEndRequirementProvider.getEndRequirement(resource);

		if (requirement != null) {
			if (requirement.isMaxDurationSet()) {
				ITimeWindow window = getTrimmedWindowBasedOnMaxDuration(requirement, index);
				windowEndTime[index] = Math.max(windowStartTime[index] + 1, Math.min(windowEndTime[index], window.getExclusiveEnd()));
			}
		}
	}

	protected final ITimeWindow getTrimmedWindowBasedOnMaxDuration(final IEndRequirement endRequirement, final int index) {

		int upperBound = Integer.MAX_VALUE;
		int lowerBound = Integer.MIN_VALUE;

		if (endRequirement.hasTimeRequirement()) {
			upperBound = endRequirement.getTimeWindow().getExclusiveEnd();
			lowerBound = endRequirement.getTimeWindow().getInclusiveStart();
		} else {
			upperBound = windowEndTime[index];

			// Charter In Vessel have MAX_VALUE upperBound
			if (upperBound == Integer.MAX_VALUE) {
				upperBound -= 1;
			}

			lowerBound = windowStartTime[index];
		}

		// TODO: upper bound instead
		final int maxUpperBound = windowEndTime[0] + endRequirement.getMaxDurationInHours();

		// Take the min as the end window upper bound, since we try to trim it down
		assert upperBound != Integer.MAX_VALUE : "Missing upper bound when trimming with max duration";
		assert lowerBound != Integer.MIN_VALUE : "Missing lower bound when trimming with max duration";

		upperBound = Math.min(upperBound, maxUpperBound);
		lowerBound = Math.min(lowerBound, upperBound - 1);
		return new TimeWindow(lowerBound, upperBound);
	}

	protected final ITimeWindow getTrimmedWindowBasedOnMinDuration(final IEndRequirement requirement) {
		int upperBound = Integer.MAX_VALUE;
		int lowerBound = Integer.MIN_VALUE;

		upperBound = requirement.getTimeWindow().getExclusiveEnd();
		lowerBound = requirement.getTimeWindow().getInclusiveStart();

		final int minLowerBound = windowStartTime[0] + requirement.getMinDurationInHours();

		// Take the min as the end window upper bound, since we try to trim it down
		assert upperBound != Integer.MAX_VALUE : "Missing upper bound when trimming with min duration";
		assert lowerBound != Integer.MIN_VALUE : "Missing lower bound when trimming with min duration";

		lowerBound = Math.max(lowerBound, minLowerBound);

		// Be sure that the end window will never get past the EndBy date if set
		if (requirement.hasTimeRequirement()) {
			lowerBound = Math.min(lowerBound, requirement.getTimeWindow().getExclusiveEnd());
		}

		if (lowerBound > upperBound) {
			upperBound = lowerBound;
			lowerBound -= 1;
		}

		return new TimeWindow(lowerBound, upperBound);
	}

}
