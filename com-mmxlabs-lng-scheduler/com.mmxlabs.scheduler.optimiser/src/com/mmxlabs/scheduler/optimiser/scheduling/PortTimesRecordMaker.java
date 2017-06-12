/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IEndEventScheduler;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.schedule.ICustomNonShippedScheduler;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
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

		boolean startSet = false;
		int startTime = 0;

		final PortTimesRecord portTimesRecord = new PortTimesRecord();
		for (final ISequenceElement element : sequence) {

			final IPortSlot thisPortSlot = portSlotProvider.getPortSlot(element);
			if (thisPortSlot.getPortType() == PortType.Start) {
				continue;
			}
			if (thisPortSlot.getPortType() == PortType.End) {
				continue;
			}

			// Set duration to get correct slot order in port times
			portTimesRecord.setSlotDuration(thisPortSlot, 0);

			// Determine transfer time
			if (!startSet && !(thisPortSlot instanceof StartPortSlot)) {

				// Find latest window start for all slots in FOB/DES combo. However if DES divertable, ignore.
				@Nullable
				ITimeWindow timeWindow = thisPortSlot.getTimeWindow();
				assert timeWindow != null;
				if (thisPortSlot instanceof ILoadOption) {
					// Divertible DES has real time window.
					if (!shippingHoursRestrictionProvider.isDivertable(element)) {
						if (actualsDataProvider.hasActuals(thisPortSlot)) {
							startTime = actualsDataProvider.getArrivalTime(thisPortSlot);
						} else {
							final int windowStart = timeWindow.getInclusiveStart();
							startTime = Math.max(windowStart, startTime);
						}
					}
				}
				if (thisPortSlot instanceof IDischargeOption) {
					if (actualsDataProvider.hasActuals(thisPortSlot)) {
						startTime = actualsDataProvider.getArrivalTime(thisPortSlot);
					} else {
						// Divertible FOB has sales time window
						// if (!shippingHoursRestrictionProvider.isDivertable(element)) {
						final int windowStart = timeWindow.getInclusiveStart();
						startTime = Math.max(windowStart, startTime);
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
	 * Returns a list of voyage plans based on breaking up a sequence of vessel real or virtual destinations into single conceptual cargo voyages.
	 * 
	 * @param resource
	 * @param sequence
	 * @param list
	 * @return
	 */
	public final @NonNull List<@NonNull IPortTimesRecord> makeShippedPortTimesRecords(int seqIndex, final @NonNull IResource resource, final @NonNull ISequence sequence,
			final List<IPortTimeWindowsRecord> trimmedWindows, MinTravelTimeData travelTimeData, ISlotTimeScheduler timeScheduler) {

		timeScheduler.startSequence(resource);

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		// Check resource type
		assert vesselAvailability.getVesselInstanceType() == VesselInstanceType.FLEET //
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER //
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER //
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP;

		final boolean isRoundTripSequence = vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP;

		final List<@NonNull IPortTimesRecord> portTimesRecords = new LinkedList<>();

		// The expected arrival based on min travel time as set by the previously seen element.
		int lastNextExpectedArrivalTime = 0;

		// If non-null, set the return slot time to the next calculated slot and time.
		PortTimesRecord recordToUpdateReturnTime = null;

		Runnable recordUpdate = () -> {
			IPortTimesRecord firstRecord = portTimesRecords.get(0);
			IPortTimeWindowsRecord portTimeWindowsRecord = trimmedWindows.get(0);
			IPortSlot from = firstRecord.getFirstSlot();
			IPortSlot to = firstRecord.getSlots().size() > 1 ? firstRecord.getSlots().get(1) : firstRecord.getReturnSlot();
			if (to != null) {
				int time = firstRecord.getSlotTime(to);
				int minTravelTime = travelTimeData.getMinTravelTime(seqIndex, 0);
				int ideal = time - minTravelTime;

				ITimeWindow window = portTimeWindowsRecord.getSlotFeasibleTimeWindow(from);
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
			}
		};

		boolean updateFirstRecordStartTime = false;
		for (IPortTimeWindowsRecord record : trimmedWindows) {
			// Create and add record to the list.
			PortTimesRecord portTimesRecord = new PortTimesRecord();
			portTimesRecords.add(portTimesRecord);

			for (IPortSlot slot : record.getSlots()) {
				final ITimeWindow window = record.getSlotFeasibleTimeWindow(slot);
				int visitDuration = record.getSlotDuration(slot);
				// Pick based on earliest time
				// TODO: Extract into injectable component
				int arrivalTime = timeScheduler.scheduleSlot(resource, slot, record, portTimesRecord, lastNextExpectedArrivalTime);

				assert actualsDataProvider.hasActuals(slot) == false || actualsDataProvider.getArrivalTime(slot) == arrivalTime;

				portTimesRecord.setSlotTime(slot, arrivalTime);

				portTimesRecord.setSlotDuration(slot, visitDuration);
				// What is the next travel time?
				lastNextExpectedArrivalTime = arrivalTime + /* visitDuration already included in min travel time + */travelTimeData.getMinTravelTime(seqIndex, record.getIndex(slot));

				if (recordToUpdateReturnTime != null) {
					recordToUpdateReturnTime.setReturnSlotTime(slot, arrivalTime);
					recordToUpdateReturnTime = null;
				}
			}

			if (updateFirstRecordStartTime) {
				recordUpdate.run();
				updateFirstRecordStartTime = false;
			} else if (portTimesRecords.size() == 1 && !isRoundTripSequence) {
				updateFirstRecordStartTime = true;
			}

			IPortSlot returnSlot = record.getReturnSlot();
			if (returnSlot != null) {
				if (returnSlot.getPortType() == PortType.Round_Trip_Cargo_End) {
					final IPortSlot startPortSlot = portTimesRecord.getSlots().get(0);
					final IPortSlot prevPortSlot = portTimesRecord.getSlots().get(portTimesRecord.getSlots().size() - 1);
					int prevArrivalTime = portTimesRecord.getSlotTime(prevPortSlot);
					int prevVisitDuration = portTimesRecord.getSlotDuration(prevPortSlot);
					final int availableTime = distanceProvider
							.getQuickestTravelTime(vesselAvailability.getVessel(), prevPortSlot.getPort(), startPortSlot.getPort(), vesselAvailability.getVessel().getVesselClass().getMaxSpeed())
							.getSecond();
					final int roundTripReturnArrivalTime = prevArrivalTime + prevVisitDuration + availableTime;

					portTimesRecord.setReturnSlotTime(returnSlot, roundTripReturnArrivalTime);

					// Reset arrival time state
					lastNextExpectedArrivalTime = 0;

				} else if (returnSlot.getPortType() == PortType.End) {
					ITimeWindow window = record.getSlotFeasibleTimeWindow(returnSlot);

					// Pick based on earliest time
					// TODO: Extract into injectable component
					int arrivalTime = window == null ? lastNextExpectedArrivalTime : Math.max(window.getInclusiveStart(), lastNextExpectedArrivalTime);

					// Only one voyage plan, we need to set the start time before adjustending end event.
					if (updateFirstRecordStartTime) {
						portTimesRecord.setReturnSlotTime(returnSlot, arrivalTime);
						recordUpdate.run();
						updateFirstRecordStartTime = false;
					}

					// Delegate to the end event schedule to determine correct end time.
					portTimesRecords.addAll(endEventScheduler.scheduleEndEvent(resource, vesselAvailability, portTimesRecord, arrivalTime, returnSlot));
				} else {
					// portTimesRecord.setReturnSlotTime(thisPortSlot, list[idx]);
					recordToUpdateReturnTime = portTimesRecord;
				}
			}
		}

		return portTimesRecords;
	}

}
