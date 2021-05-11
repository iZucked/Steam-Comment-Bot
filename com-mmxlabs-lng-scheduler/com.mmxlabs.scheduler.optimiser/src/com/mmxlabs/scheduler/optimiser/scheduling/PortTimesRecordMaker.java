/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.cache.IWriteLockable;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IEndEventScheduler;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.schedule.ICustomNonShippedScheduler;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.AvailableRouteChoices;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;

/**
 * The {@link PortTimesRecordMaker} creates the initial {@link IPortTimesRecord}s for a {@link ISequence} and a provided arrival time array from an {@link ISequenceScheduler}
 */
public class PortTimesRecordMaker {

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject(optional = true)
	private ICustomNonShippedScheduler customNonShippedScheduler;

	@Inject
	private IShippingHoursRestrictionProvider shippingHoursRestrictionProvider;

	@Inject
	private IActualsDataProvider actualsDataProvider;

	@Inject
	private IEndEventScheduler endEventScheduler;

	@Inject
	private IElementDurationProvider durationProvider;

	/**
	 * This method replaces the normal shipped cargo calculation path with one specific to DES purchase or FOB sale cargoes. However this currently merges in behaviour from other classes - such as
	 * scheduling and volume allocation - which should really stay in those other classes.
	 * 
	 * @param resource
	 * @param sequence
	 * @return
	 */
	public final @Nullable IPortTimesRecord makeDESOrFOBPortTimesRecord(final @NonNull IResource resource, final @NonNull ISequence sequence) {

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		// Check resource type
		assert vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE//
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE;

		final PortTimesRecord portTimesRecord = new PortTimesRecord();

		// Grab localised time window
		IPort localisedPort = null;
		for (final ISequenceElement element : sequence) {

			final IPortSlot thisPortSlot = portSlotProvider.getPortSlot(element);
			if (thisPortSlot instanceof ILoadSlot) {
				localisedPort = thisPortSlot.getPort();
			}

			if (thisPortSlot instanceof IDischargeSlot) {
				localisedPort = thisPortSlot.getPort();
			}
		}

		boolean startSet = false;
		int startTime = 0;
		for (final ISequenceElement element : sequence) {

			final IPortSlot thisPortSlot = portSlotProvider.getPortSlot(element);
			if (thisPortSlot.getPortType() == PortType.Start) {
				continue;
			}
			if (thisPortSlot.getPortType() == PortType.End) {
				continue;
			}

			// Set duration to get correct slot order in port times
			final int loadDuration = durationProvider.getElementDuration(portSlotProvider.getElement(thisPortSlot));
			portTimesRecord.setSlotDuration(thisPortSlot, loadDuration);
			portTimesRecord.setSlotExtraIdleTime(thisPortSlot, 0);

			// Determine transfer time
			if (!startSet && !(thisPortSlot instanceof StartPortSlot)) {

				// Find latest window start for all slots in FOB/DES combo. However if DES
				// divertible, ignore.

				ITimeWindow timeWindow = thisPortSlot.getTimeWindow();
				assert timeWindow != null;

				if (thisPortSlot instanceof ILoadOption) {

					final ILoadOption loadOption = (ILoadOption) thisPortSlot;
					// Divertible DES has real time window.
					if (!shippingHoursRestrictionProvider.isDivertible(element)) {
						if (actualsDataProvider.hasActuals(thisPortSlot)) {
							startTime = actualsDataProvider.getArrivalTime(thisPortSlot);
						} else {
							if (localisedPort != null && !(loadOption instanceof ILoadSlot)) {
								final ITimeWindow tw = loadOption.getLocalisedTimeWindowForPort(localisedPort);
								if (tw != null) {
									timeWindow = tw;
								}
							}

							final int windowStart = timeWindow.getInclusiveStart();
							startTime = Math.max(windowStart, startTime);
						}
					}
				}
				if (thisPortSlot instanceof IDischargeOption) {
					final IDischargeOption dischargeOption = (IDischargeOption) thisPortSlot;
					if (actualsDataProvider.hasActuals(thisPortSlot)) {
						startTime = actualsDataProvider.getArrivalTime(thisPortSlot);
					} else {
						// Divertible FOB has sales time window
						// TODO: Consider ship days restriction...
						if (!shippingHoursRestrictionProvider.isDivertible(element)) {

							if (localisedPort != null && !(dischargeOption instanceof IDischargeSlot)) {
								final ITimeWindow tw = dischargeOption.getLocalisedTimeWindowForPort(localisedPort);
								if (tw != null) {
									timeWindow = tw;
								}
							}

							final int windowStart = timeWindow.getInclusiveStart();
							startTime = Math.max(windowStart, startTime);
						}
					}
				}
			}

			// Only expect L-D style FOB/DES cargoes, so stop processing
			if (thisPortSlot instanceof IDischargeSlot) {
				// Break here to avoid processing further
				startSet = true;
			}
		}

		// Is this an L-D cargo?
		if (portTimesRecord.getSlots().size() != 2) {
			return null;
		}

		// Populate correct times into record
		for (final IPortSlot slot : portTimesRecord.getSlots()) {
			portTimesRecord.setSlotTime(slot, startTime);
		}

		// Permit custom code to modify the arrival times (e.g. for divertible DES)
		if (customNonShippedScheduler != null) {
			customNonShippedScheduler.modifyArrivalTimes(resource, portTimesRecord);
		}

		return portTimesRecord;
	}

	/**
	 * Returns a list of voyage plans based on breaking up a sequence of vessel real or virtual destinations into single conceptual cargo voyages. (used by the price scheduler) Works out the earliest
	 * arrival time within the trimmed window based on distances/travel time and previous arrivals.
	 * 
	 * @param resource
	 * @param sequence
	 * @param list
	 * @return
	 */
	public final @NonNull List<@NonNull IPortTimesRecord> calculateShippedPortTimesRecords(final int seqIndex, final @NonNull IResource resource, final @NonNull ISequence sequence,
			final List<IPortTimeWindowsRecord> trimmedWindows, final MinTravelTimeData travelTimeData) {

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		// Check resource type
		assert vesselAvailability.getVesselInstanceType() == VesselInstanceType.FLEET //
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER //
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER //
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP;

		final boolean isRoundTripSequence = vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP;

		final List<@NonNull IPortTimesRecord> portTimesRecords = new LinkedList<>();

		// The expected arrival based on min travel time as set by the previously seen
		// element.
		int lastNextExpectedArrivalTime = 0;

		// If non-null, set the return slot time to the next calculated slot and time.
		PortTimesRecord recordToUpdateReturnTime = null;

		Pair<IPortTimeWindowsRecord, PortTimesRecord> recordPairToUpdateWithPanama = null;

		boolean updateFirstRecordStartTime = false;
		for (final IPortTimeWindowsRecord record : trimmedWindows) {
			// Create and add record to the list.
			final PortTimesRecord portTimesRecord = new PortTimesRecord();
			portTimesRecords.add(portTimesRecord);

			for (final IPortSlot slot : record.getSlots()) {
				portTimesRecord.setRouteOptionBooking(slot, record.getRouteOptionBooking(slot));
				portTimesRecord.setSlotNextVoyageOptions(slot, record.getSlotNextVoyageOptions(slot));

				final int visitDuration = record.getSlotDuration(slot);
				final int extraIdleTime = record.getSlotExtraIdleTime(slot);

				// Pick based on earliest time
				final int arrivalTime = Math.max(record.getSlotFeasibleTimeWindow(slot).getInclusiveStart(), lastNextExpectedArrivalTime);

				assert !actualsDataProvider.hasActuals(slot) || actualsDataProvider.getArrivalTime(slot) == arrivalTime;

				portTimesRecord.setSlotTime(slot, arrivalTime);
				portTimesRecord.setSlotDuration(slot, visitDuration);
				portTimesRecord.setSlotExtraIdleTime(slot, extraIdleTime);
				// What is the next travel time?
				lastNextExpectedArrivalTime = arrivalTime + /* visitDuration already included in min travel time + */travelTimeData.getMinTravelTime(record.getIndex(slot));

				// These parts update records from the previous iteration on the first slot only.
				if (recordToUpdateReturnTime != null) {
					recordToUpdateReturnTime.setReturnSlotTime(slot, arrivalTime);
					recordToUpdateReturnTime = null;
				}
				// Make sure we do after return time updated.
				if (recordPairToUpdateWithPanama != null) {
					updatePortTimesRecordWithPanamaRestrictions(distanceProvider, vesselAvailability.getVessel(), recordPairToUpdateWithPanama.getFirst(), recordPairToUpdateWithPanama.getSecond());
				}
				recordPairToUpdateWithPanama = null;
			}

			if (updateFirstRecordStartTime) {
				updateRecord(isRoundTripSequence, vesselAvailability, portTimesRecords, trimmedWindows, travelTimeData);
				updateFirstRecordStartTime = false;
			} else if (portTimesRecords.size() == 1 && !isRoundTripSequence) {
				updateFirstRecordStartTime = true;
			}

			final IPortSlot returnSlot = record.getReturnSlot();
			if (returnSlot != null) {
				if (returnSlot.getPortType() == PortType.Round_Trip_Cargo_End) {
					// TODO: Delegate to endEventScheduler so it can be customised
					final IPortSlot startPortSlot = portTimesRecord.getSlots().get(0);
					final IPortSlot prevPortSlot = portTimesRecord.getSlots().get(portTimesRecord.getSlots().size() - 1);
					final int prevArrivalTime = portTimesRecord.getSlotTime(prevPortSlot);
					final int prevVisitDuration = portTimesRecord.getSlotDuration(prevPortSlot);
					final int availableTime = distanceProvider.getQuickestTravelTime(vesselAvailability.getVessel(), //
							prevPortSlot.getPort(), startPortSlot.getPort(), //
							vesselAvailability.getVessel().getMaxSpeed(), //
							record.getSlotNextVoyageOptions(prevPortSlot) //
					).getSecond();
					final int roundTripReturnArrivalTime = prevArrivalTime + prevVisitDuration + availableTime + record.getSlotExtraIdleTime(prevPortSlot);

					portTimesRecord.setReturnSlotTime(returnSlot, roundTripReturnArrivalTime);

					// Reset arrival time state
					lastNextExpectedArrivalTime = 0;

				} else if (returnSlot.getPortType() == PortType.End) {
					final ITimeWindow window = record.getSlotFeasibleTimeWindow(returnSlot);

					// Pick based on earliest time
					// TODO: Extract into injectable component
					int arrivalTime = window == null ? lastNextExpectedArrivalTime : Math.max(window.getInclusiveStart(), lastNextExpectedArrivalTime);

					/**
					 * Make sure we satisfy the max duration
					 **/
					final IEndRequirement endReq = vesselAvailability.getEndRequirement();
					if (endReq.isMaxDurationSet()) {
						// Get the actual time of the start event
						final IPortTimesRecord firstRecord = portTimesRecords.get(0);
						final IPortSlot startSlot = firstRecord.getFirstSlot();

						final int startTime = firstRecord.getSlotTime(startSlot);
						final int maxTime = startTime + endReq.getMaxDurationInHours();

						if (maxTime < lastNextExpectedArrivalTime) {
							// Trigger an error if the end is before the arrival at max speed
							// Will pick up as a lateness violation
							arrivalTime = lastNextExpectedArrivalTime;
						} else {

							arrivalTime = Math.min(maxTime, arrivalTime);
						}
					}

					// Only one voyage plan, we need to set the start time before adjust ending end
					// event.
					if (updateFirstRecordStartTime) {
						portTimesRecord.setReturnSlotTime(returnSlot, arrivalTime);
						updateRecord(isRoundTripSequence, vesselAvailability, portTimesRecords, trimmedWindows, travelTimeData);
						updateFirstRecordStartTime = false;
					}

					// FIXME: There is a conflict with this code and the min/max duration code
					// (which assumes end event duration is 0).
					// Delegate to the end event schedule to determine correct end time.
					portTimesRecords.addAll(endEventScheduler.scheduleEndEvent(resource, vesselAvailability, portTimesRecord, arrivalTime, returnSlot));

					// We need to update the start time after the end event is scheduled, to make
					// the min/max duration adjustments
					updateRecord(isRoundTripSequence, vesselAvailability, portTimesRecords, trimmedWindows, travelTimeData);

					// We need to update panama information this record
					recordPairToUpdateWithPanama = Pair.of(record, portTimesRecord);
				} else {
					// We need to update the return time of this record
					recordToUpdateReturnTime = portTimesRecord;
					// We need to update panama information this record
					recordPairToUpdateWithPanama = Pair.of(record, portTimesRecord);
				}
			}
		}

		// Update the final record with panama information
		if (recordPairToUpdateWithPanama != null) {
			updatePortTimesRecordWithPanamaRestrictions(distanceProvider, vesselAvailability.getVessel(), recordPairToUpdateWithPanama.getFirst(), recordPairToUpdateWithPanama.getSecond());
		}

		IWriteLockable.writeLock(portTimesRecords);

		return portTimesRecords;
	}

	private void updateRecord(final boolean isRoundTripSequence, final IVesselAvailability vesselAvailability, final List<@NonNull IPortTimesRecord> portTimesRecords,
			final List<IPortTimeWindowsRecord> trimmedWindows, final MinTravelTimeData travelTimeData) {
		final IPortTimesRecord firstRecord = portTimesRecords.get(0);
		final IPortTimeWindowsRecord portTimeWindowsRecord = trimmedWindows.get(0);
		final IPortTimeWindowsRecord portTimeWindowsRecordLast = trimmedWindows.get(trimmedWindows.size() - 1);
		final IPortSlot from = firstRecord.getFirstSlot();
		final IPortSlot to = firstRecord.getSlots().size() > 1 ? firstRecord.getSlots().get(1) : firstRecord.getReturnSlot();
		if (to != null) {
			final int time = firstRecord.getSlotTime(to);
			final int minTravelTime = travelTimeData.getMinTravelTime(0);
			final int ideal = time - minTravelTime;

			final ITimeWindow window = portTimeWindowsRecord.getSlotFeasibleTimeWindow(from);
			if (ideal < time) {
				// Earlier than current feasible time, so ignore
			}
			if (ideal >= window.getExclusiveEnd()) {
				// Later than window end, so use window end
				firstRecord.setSlotTime(from, window.getExclusiveEnd() - 1);
			} else {
				// Still within window, so set to this time.
				firstRecord.setSlotTime(from, ideal);
			}
			final int newStartTime = firstRecord.getSlotTime(from);
			/**
			 * Min requirement padding In case we don't meet the minimal duration but still have some time left after the end or before the start
			 **/
			final IEndRequirement endReq = vesselAvailability.getEndRequirement();
			if (endReq.isMinDurationSet()) {
				final IPortTimesRecord lastRecord = portTimesRecords.get(portTimesRecords.size() - 1);
				final IPortSlot lastPort = lastRecord.getFirstSlot();
				if (lastPort != null && lastPort.getPortType() == PortType.End) {

					// Get delta and remaining hours to fill
					int endTime = lastRecord.getSlotTime(lastPort);
					final int minDeltaInHours = endTime - newStartTime;
					final int minDurationInHours = endReq.getMinDurationInHours();
					int remainingHours = minDurationInHours - minDeltaInHours;

					// Updated end time used to set previous record return time
					int newEndTime = endTime;

					if (minDeltaInHours < minDurationInHours) {
						// Add padding to end event if possible
						final ITimeWindow windowEnd = portTimeWindowsRecordLast.getSlotFeasibleTimeWindow(lastPort);
						final int endLeftOver = windowEnd.getExclusiveEnd() - endTime - 1;
						if (endLeftOver > 0) {
							// Can we take care of the remaining time in one go ?
							if (endLeftOver > remainingHours) {
								newEndTime = endTime + remainingHours;
								lastRecord.setSlotTime(lastPort, newEndTime);
								remainingHours = 0;
							} else {
								remainingHours -= endLeftOver;
								newEndTime = endTime + endLeftOver;
								lastRecord.setSlotTime(lastPort, newEndTime);
							}
						}

						// Add padding to start event if possible
						if (remainingHours > 0) {
							final ITimeWindow windowStart = portTimeWindowsRecord.getSlotFeasibleTimeWindow(from);
							final int startLeftOver = newStartTime - windowStart.getInclusiveStart();
							if (startLeftOver > 0) {
								// Can we take care of the remaining time in one go ?
								if (startLeftOver > remainingHours) {
									firstRecord.setSlotTime(from, newStartTime - remainingHours);
									remainingHours = 0;
								} else {
									// Set new end time
									firstRecord.setSlotTime(from, newStartTime - startLeftOver);
									remainingHours -= startLeftOver;
								}
							}
						}

						// Do we still have some time to pad at the end
						// (Will trigger window/time requirement violation)
						if (remainingHours > 0) {
							// Get the updated endTime
							endTime = lastRecord.getSlotTime(lastPort);
							newEndTime = endTime + minDeltaInHours;
							lastRecord.setSlotTime(lastPort, newEndTime);
						}

						if (portTimesRecords.size() > 1 && !isRoundTripSequence) {
							final IPortTimesRecord lastRecord2 = portTimesRecords.get(portTimesRecords.size() - 2);
							lastRecord2.setSlotTime(lastPort, newEndTime);
						}
					}
				}
			}
		}
	}

	/**
	 * Returns a list of voyage plans based on breaking up a sequence of vessel real or virtual destinations into single conceptual cargo voyages. This variant assumes there is a
	 * IPortTimeWindowsRecord for the end event and the window start is the correct time to use. Used by the PNL time windows scheduler and assumes window start is the arrival time.
	 * 
	 * @param resource
	 * @param sequence
	 * @param list
	 * @return
	 */
	public final @NonNull List<@NonNull IPortTimesRecord> makeSimpleShippedPortTimesRecords(final int seqIndex, final @NonNull IResource resource, final @NonNull ISequence sequence,
			final List<IPortTimeWindowsRecord> trimmedWindows, final MinTravelTimeData travelTimeData) {

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		// Check resource type
		assert vesselAvailability.getVesselInstanceType() == VesselInstanceType.FLEET //
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER //
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER //
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP;

		final List<@NonNull IPortTimesRecord> portTimesRecords = new LinkedList<>();

		// If non-null, set the return slot time to the next calculated slot and time.
		PortTimesRecord recordToUpdateReturnTime = null;

		Pair<IPortTimeWindowsRecord, PortTimesRecord> recordPairToUpdateWithPanama = null;

		for (final IPortTimeWindowsRecord record : trimmedWindows) {
			// Create and add record to the list.
			final PortTimesRecord portTimesRecord = new PortTimesRecord();
			portTimesRecords.add(portTimesRecord);

			for (final IPortSlot slot : record.getSlots()) {
				portTimesRecord.setRouteOptionBooking(slot, record.getRouteOptionBooking(slot));
				portTimesRecord.setSlotNextVoyageOptions(slot, record.getSlotNextVoyageOptions(slot));

				final int visitDuration = record.getSlotDuration(slot);
				final int extraIdleTime = record.getSlotExtraIdleTime(slot); // contingency matrix.

				// Pick based on earliest time
				final int arrivalTime = record.getSlotFeasibleTimeWindow(slot).getInclusiveStart();

				assert !actualsDataProvider.hasActuals(slot) || actualsDataProvider.getArrivalTime(slot) == arrivalTime;

				portTimesRecord.setSlotTime(slot, arrivalTime);
				portTimesRecord.setSlotDuration(slot, visitDuration);
				portTimesRecord.setSlotExtraIdleTime(slot, extraIdleTime);
				// What is the next travel time?
				
				
				if (recordToUpdateReturnTime != null) {
					recordToUpdateReturnTime.setReturnSlotTime(slot, arrivalTime);
					recordToUpdateReturnTime = null;
				}

				if (recordPairToUpdateWithPanama != null) {
					updatePortTimesRecordWithPanamaRestrictions(distanceProvider, vesselAvailability.getVessel(), recordPairToUpdateWithPanama.getFirst(), recordPairToUpdateWithPanama.getSecond());
				}
				recordPairToUpdateWithPanama = null;
			}

			final IPortSlot returnSlot = record.getReturnSlot();
			if (returnSlot != null) {
				if (returnSlot.getPortType() == PortType.Round_Trip_Cargo_End) {

					final IPortSlot startPortSlot = portTimesRecord.getSlots().get(0);
					final IPortSlot prevPortSlot = portTimesRecord.getSlots().get(portTimesRecord.getSlots().size() - 1);
					final int prevArrivalTime = portTimesRecord.getSlotTime(prevPortSlot);
					final int prevVisitDuration = portTimesRecord.getSlotDuration(prevPortSlot);
					final int availableTime = distanceProvider.getQuickestTravelTime(vesselAvailability.getVessel(), //
							prevPortSlot.getPort(), startPortSlot.getPort(), //
							vesselAvailability.getVessel().getMaxSpeed(), //
							record.getSlotNextVoyageOptions(prevPortSlot) //
					).getSecond();
					final int roundTripReturnArrivalTime = prevArrivalTime + prevVisitDuration + availableTime + record.getSlotExtraIdleTime(prevPortSlot);

					portTimesRecord.setReturnSlotTime(returnSlot, roundTripReturnArrivalTime);
				} else {
					// We need to update the return time of this record
					recordToUpdateReturnTime = portTimesRecord;
					// We need to update panama information this record
					recordPairToUpdateWithPanama = Pair.of(record, portTimesRecord);
				}
			}
		}

		// In case something changes and the end event has a journey on it in the future.
		if (recordPairToUpdateWithPanama != null) {
			updatePortTimesRecordWithPanamaRestrictions(distanceProvider, vesselAvailability.getVessel(), recordPairToUpdateWithPanama.getFirst(), recordPairToUpdateWithPanama.getSecond());
		}

		IWriteLockable.writeLock(portTimesRecords);

		return portTimesRecords;
	}

	/**
	 * Excludes Panama in the case where it is not fast enough to reach destination, now that we have assigned a start time for the journey.
	 * 
	 * @param distanceProvider
	 * @param vessel
	 * @param portTimeWindowsRecord
	 * @param recordCopy
	 */
	public static void updatePortTimesRecordWithPanamaRestrictions(final IDistanceProvider distanceProvider, final IVessel vessel, final IPortTimeWindowsRecord portTimeWindowsRecord,
			final PortTimesRecord recordCopy) {
		final List<IPortSlot> allSlots = new LinkedList<>(recordCopy.getSlots());
		// returnSlot was null in recordCopy so get from PortTimeWindowsRecord instead, as seems more reliable.
		if (recordCopy.getReturnSlot() != null) {
			allSlots.add(recordCopy.getReturnSlot());
		}
		if (allSlots.size() > 1) {
			for (int i = 1; i < allSlots.size(); ++i) {
				final IPortSlot from = allSlots.get(i - 1);
				final IPortSlot to = allSlots.get(i);

				// Panama available without a booking? Need to check waiting days
				if (AvailableRouteChoices.panamaPermitted(recordCopy.getSlotNextVoyageOptions(from)) && recordCopy.getRouteOptionBooking(from) == null) {

					final int startTime = recordCopy.getSlotTime(from);
					final int departureTime = startTime + recordCopy.getSlotDuration(from);
					final int arrivalTime = recordCopy.getSlotTime(to);

					// Total time for travel and idling
					final int availableTravelTime = arrivalTime - departureTime;

					// Calculate if time to go via Panama or not including any additional idle time required for waiting for a slot if no booking.
					int panamaTime = distanceProvider.getTravelTime(ERouteOption.PANAMA, vessel, from.getPort(), to.getPort(), vessel.getMaxSpeed());

					if (panamaTime == Integer.MAX_VALUE) {
						// Normally expect to have had Panama restricted earlier, but just in case....
						recordCopy.setSlotNextVoyageOptions(from, AvailableRouteChoices.EXCLUDE_PANAMA);
					} else {
						// Add on waiting time
						final int additionalPanamaTime = portTimeWindowsRecord.getSlotAdditionalPanamaIdleHours(from);
						panamaTime += additionalPanamaTime;

						// Is there still time to go via Panama? Exclude if not
						if (panamaTime > availableTravelTime) {
							// It has all gone wrong if we hit this assert
							assert recordCopy.getSlotNextVoyageOptions(from) != AvailableRouteChoices.PANAMA_ONLY;
							recordCopy.setSlotNextVoyageOptions(from, AvailableRouteChoices.EXCLUDE_PANAMA);
						} else {
							// System.out.println("For "+from.getId()+" additionalPanamaTime = "+additionalPanamaTime+" max available = "+(availableTravelTime - (panamaTime - additionalPanamaTime)));
							recordCopy.setSlotAdditionalPanamaIdleHours(from, additionalPanamaTime);
							recordCopy.setSlotMaxAvailablePanamaIdleHours(from, availableTravelTime - (panamaTime - additionalPanamaTime));
						}
					}
				}
			}
		}
	}

}
