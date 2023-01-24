/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesAttributesProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.TimeWindowsTrimming;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

/**
 * @author achurchill
 * @param
 */
public class PriceBasedWindowTrimmer {

	@Inject
	private TimeWindowsTrimming timeWindowsTrimming;

	@Inject
	private IVesselProvider vesselProvider;

	public void updateWindows(final Map<IResource, List<IPortTimeWindowsRecord>> trimmedWindows, final ISequences fullSequences, final Map<IResource, MinTravelTimeData> travelTimeDataMap) {

		for (int seqIndex = 0; seqIndex < fullSequences.size(); seqIndex++) {
			final IResource resource = fullSequences.getResources().get(seqIndex);
			trimWindows(resource, trimmedWindows.get(resource), travelTimeDataMap.get(resource), fullSequences.getProviders());
		}
	}

	public void trimWindows(IResource resource, List<IPortTimeWindowsRecord> trimmedWindows, final MinTravelTimeData travelTimeData, ISequencesAttributesProvider sequencesAttributesProvider) {

		final int vesselStartTime = 0;// arrivalTimes[seqIndex][0];
		ITimeWindow lastFeasibleWindow = new TimeWindow(0, 1);
		for (int idx = 0; idx < trimmedWindows.size(); idx++) {
			final IPortTimeWindowsRecord portTimeWindowsRecord = trimmedWindows.get(idx);
			final IPortTimeWindowsRecord portTimeWindowsRecordStart = trimmedWindows.get(0);

			// Retrim current window based on last window trim.
			if (isSequentialVessel(resource)) {
				setFeasibleTimeWindowsUsingPrevious(portTimeWindowsRecord, travelTimeData, lastFeasibleWindow);
			}

			timeWindowsTrimming.processCargo(resource, portTimeWindowsRecord, vesselStartTime, portTimeWindowsRecordStart, travelTimeData, sequencesAttributesProvider);

			@NonNull
			final IPortSlot lastSlot = portTimeWindowsRecord.getSlots().get(portTimeWindowsRecord.getSlots().size() - 1);
			lastFeasibleWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(lastSlot);
		}
	}

	private boolean isSequentialVessel(final IResource resource) {
		final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);
		if (vesselCharter.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
			return false;
		}
		return true;
	}

	private int getFeasibleWindowEnd(final IPortTimeWindowsRecord portTimeWindowsRecord, final IPortSlot portSlot, final int feasibleWindowStart) {
		final ITimeWindow window = portTimeWindowsRecord.getSlotFeasibleTimeWindow(portSlot);
		return Math.max(feasibleWindowStart + 1, window.getExclusiveEnd());
	}

	private int getFeasibleWindowStartForFirstSlot(final IPortTimeWindowsRecord portTimeWindowsRecord, final MinTravelTimeData travelTimeData, @NonNull final ITimeWindow previousTimeWindow) {
		final ITimeWindow timeWindow = portTimeWindowsRecord.getFirstSlotFeasibleTimeWindow();
		return Math.max(previousTimeWindow.getInclusiveStart() + travelTimeData.getMinTravelTime(portTimeWindowsRecord.getIndex(portTimeWindowsRecord.getFirstSlot()) - 1),
				timeWindow.getInclusiveStart());
	}

	/**
	 * The previous cargo will have changed the constraints on the time windows, so
	 * we must find the new feasible time windows before choosing an arrival time
	 * 
	 * @param portTimeWindowsRecord
	 * @param seqIndex
	 */
	private void setFeasibleTimeWindowsUsingPrevious(final IPortTimeWindowsRecord portTimeWindowsRecord, final MinTravelTimeData travelTimeData, final ITimeWindow previousRecordWindow) {
		int prevFeasibleWindowStart = IPortSlot.NO_PRICING_DATE;
		for (final IPortSlot portSlot : portTimeWindowsRecord.getSlots()) {

			ITimeWindow timeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(portSlot);
			int feasibleWindowStart, feasibleWindowEnd;
			if (portTimeWindowsRecord.getFirstSlot().equals(portSlot) && portTimeWindowsRecord.getIndex(portTimeWindowsRecord.getFirstSlot()) > 0) {
				// first load
				feasibleWindowStart = getFeasibleWindowStartForFirstSlot(portTimeWindowsRecord, travelTimeData, previousRecordWindow);
				feasibleWindowEnd = getFeasibleWindowEnd(portTimeWindowsRecord, portSlot, feasibleWindowStart);
				portTimeWindowsRecord.setSlotFeasibleTimeWindow(portSlot, new TimeWindow(feasibleWindowStart, feasibleWindowEnd));
			} else {
				if (prevFeasibleWindowStart == IPortSlot.NO_PRICING_DATE) {
					feasibleWindowStart = timeWindow.getInclusiveStart();
					feasibleWindowEnd = timeWindow.getExclusiveEnd();
				} else {
					feasibleWindowStart = Math.max(timeWindow.getInclusiveStart(), prevFeasibleWindowStart + travelTimeData.getMinTravelTime(portTimeWindowsRecord.getIndex(portSlot) - 1));
					feasibleWindowEnd = Math.max(timeWindow.getExclusiveEnd(), feasibleWindowStart + 1);
				}
				portTimeWindowsRecord.setSlotFeasibleTimeWindow(portSlot, new TimeWindow(feasibleWindowStart, feasibleWindowEnd));
			}
			prevFeasibleWindowStart = feasibleWindowStart;
		}
	}

}
