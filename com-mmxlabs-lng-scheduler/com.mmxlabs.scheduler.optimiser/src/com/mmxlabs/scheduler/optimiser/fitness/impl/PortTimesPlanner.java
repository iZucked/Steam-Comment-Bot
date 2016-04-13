/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.schedule.ICustomNonShippedScheduler;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;

/**
 */
public class PortTimesPlanner {

	@Inject
	private IElementDurationProvider durationsProvider;

	@Inject
	private IPortProvider portProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IPortTypeProvider portTypeProvider;

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
			if (thisPortSlot instanceof StartPortSlot) {
				continue;
			}
			if (thisPortSlot instanceof EndPortSlot) {
				continue;
			}

			// Set duration to get correct slot order in port times
			portTimesRecord.setSlotDuration(thisPortSlot, 0);

			// Determine transfer time
			if (!startSet && !(thisPortSlot instanceof StartPortSlot)) {

				// Find latest window start for all slots in FOB/DES combo. However if DES divertable, ignore.
				if (thisPortSlot instanceof ILoadOption) {
					// Divertible DES has real time window.
					if (!shippingHoursRestrictionProvider.isDivertable(element)) {
						if (actualsDataProvider.hasActuals(thisPortSlot)) {
							startTime = actualsDataProvider.getArrivalTime(thisPortSlot);
						} else {
							final int windowStart = thisPortSlot.getTimeWindow().getStart();
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
						final int windowStart = thisPortSlot.getTimeWindow().getStart();
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
	 * @param arrivalTimes
	 * @return
	 */
	public final @NonNull List<@NonNull IPortTimesRecord> makeShippedPortTimesRecords(final @NonNull IResource resource, final @NonNull ISequence sequence, final int @NonNull [] arrivalTimes) {

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		// Check resource type
		assert vesselAvailability.getVesselInstanceType() == VesselInstanceType.FLEET //
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER //
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER //
				|| vesselAvailability.getVesselInstanceType() == VesselInstanceType.CARGO_SHORTS;

		final boolean isShortsSequence = vesselAvailability.getVesselInstanceType() == VesselInstanceType.CARGO_SHORTS;

		final List<@NonNull IPortTimesRecord> portTimesRecords = new LinkedList<>();

		// Find the break points for this sequence
		final boolean[] breakSequence = findSequenceBreaks(sequence);

		// Create and add first record to the list.
		PortTimesRecord portTimesRecord = new PortTimesRecord();
		portTimesRecords.add(portTimesRecord);

		// Used for cargo short end of sequence checks
		IPortSlot prevPortSlot = null;

		final Iterator<@NonNull ISequenceElement> itr = sequence.iterator();

		for (int idx = 0; itr.hasNext(); ++idx) {
			final ISequenceElement element = itr.next();

			final IPortSlot thisPortSlot = portSlotProvider.getPortSlot(element);
			final PortType portType = portTypeProvider.getPortType(element);

			final int visitDuration = actualsDataProvider.hasActuals(thisPortSlot) ? actualsDataProvider.getVisitDuration(thisPortSlot) : durationsProvider.getElementDuration(element, resource);
			// Sequence scheduler should be using the actuals time
			assert actualsDataProvider.hasActuals(thisPortSlot) == false || actualsDataProvider.getArrivalTime(thisPortSlot) == arrivalTimes[idx];

			// Set current slot arrival time and duration (if not end)
			if (breakSequence[idx]) {
				if (isShortsSequence && portType == PortType.Short_Cargo_End) {
					assert prevPortSlot != null;
					final IPortSlot startPortSlot = portTimesRecord.getSlots().get(0);
					int prevArrivalTime = portTimesRecord.getSlotTime(prevPortSlot);
					int prevVisitDuration = portTimesRecord.getSlotDuration(prevPortSlot);
					final int availableTime = distanceProvider.getQuickestTravelTime(vesselAvailability.getVessel(), prevPortSlot.getPort(), startPortSlot.getPort(),
							prevArrivalTime + prevVisitDuration, vesselAvailability.getVessel().getVesselClass().getMaxSpeed()).getSecond();
					final int shortCargoReturnArrivalTime = prevArrivalTime + prevVisitDuration + availableTime;

					portTimesRecord.setReturnSlotTime(thisPortSlot, shortCargoReturnArrivalTime);
				} else {
					portTimesRecord.setReturnSlotTime(thisPortSlot, arrivalTimes[idx]);
				}
				// Return elements always have a duration of zero
				portTimesRecord.setSlotDuration(thisPortSlot, 0);
			} else {
				portTimesRecord.setSlotTime(thisPortSlot, arrivalTimes[idx]);
				portTimesRecord.setSlotDuration(thisPortSlot, visitDuration);
			}

			// Is this the end of the sequence? If so, start new port times record
			if (breakSequence[idx]) {

				// Is this the last element? Do not start a new PortTimesRecord and break out instead
				if (sequence.size() == idx + 1) {
					break;
				}

				// Reset object ref
				portTimesRecord = new PortTimesRecord();
				portTimesRecord.setSlotTime(thisPortSlot, arrivalTimes[idx]);
				portTimesRecord.setSlotDuration(thisPortSlot, visitDuration);

				portTimesRecords.add(portTimesRecord);
			}

			// Setup for next iteration
			prevPortSlot = thisPortSlot;
		}

		return portTimesRecords;
	}

	/**
	 * Returns an array of boolean values indicating whether, for each index of the vessel location sequence, a sequence break occurs at that location (separating one cargo from the next one).
	 * 
	 * @param sequence
	 * @return
	 */
	public boolean @NonNull [] findSequenceBreaks(final @NonNull ISequence sequence) {
		final boolean @NonNull [] result = new boolean[sequence.size()];

		int idx = 0;
		for (final ISequenceElement element : sequence) {
			final PortType portType = portTypeProvider.getPortType(element);
			switch (portType) {
			case Load:
				result[idx] = (idx > 0); // don't break on first load port
				break;
			case CharterOut:
			case DryDock:
			case Other:
			case Maintenance:
			case Short_Cargo_End:
			case End:
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
}
