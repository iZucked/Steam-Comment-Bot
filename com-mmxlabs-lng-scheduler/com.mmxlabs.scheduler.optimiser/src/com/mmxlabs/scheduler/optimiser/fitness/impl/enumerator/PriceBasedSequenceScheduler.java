/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.TimeWindowsTrimming;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

/**
 * {@link EnumeratingSequenceScheduler} which checks for the best price on a per cargo basis in order to determine arrival times
 * 
 * @author achurchill
 * @param
 */
public class PriceBasedSequenceScheduler extends EnumeratingSequenceScheduler {
	@Inject
	private TimeWindowsTrimming timeWindowsTrimming;

	@Inject
	private IVesselProvider vesselProvider;

	private boolean[] endsSet;

	@Override
	public int @Nullable [][] schedule(@NonNull final ISequences sequences) {
		setSequences(sequences);

		prepare();
		endsSet = new boolean[arrivalTimes.length];

		sequentialEarliestTimePriceBasedWindowTrimming(sequences, portTimeWindowsRecords);
		for (int index = 0; index < sequences.size(); ++index) {
			setTimeWindowsToEarliest(index);
		}
		synchroniseShipToShipBindings();
		return arrivalTimes;
	}

	public List<List<IPortTimeWindowsRecord>> getPortTimeWindowsRecords() {
		return portTimeWindowsRecords;
	}
	
	private void sequentialEarliestTimePriceBasedWindowTrimming(ISequences sequences, List<List<IPortTimeWindowsRecord>> portTimeWindowsRecords) {
		for (int seqIndex = 0; seqIndex < sequences.size(); seqIndex++) {
			List<IPortTimeWindowsRecord> list = portTimeWindowsRecords.get(seqIndex);
			for (int idx = 0; idx < list.size(); idx++) {
				IPortTimeWindowsRecord portTimeWindowsRecord = list.get(idx);
				if (isSequentialVessel(portTimeWindowsRecord.getResource())) {
					setFeasibleTimeWindowsUsingPrevious(portTimeWindowsRecord, seqIndex);
				} else {
					setFeasibleTimeWindowsRoundTrip(portTimeWindowsRecord, seqIndex);
				}
				timeWindowsTrimming.processCargo(portTimeWindowsRecord, arrivalTimes[seqIndex][0]);
				updateTimeWindows(portTimeWindowsRecord, seqIndex);
				IPortSlot lastSlot = portTimeWindowsRecord.getSlots().get(portTimeWindowsRecord.getSlots().size() - 1);
				setTimeWindowsToEarliest(seqIndex, portTimeWindowsRecord.getIndex(lastSlot), portTimeWindowsRecord.getSlotFeasibleTimeWindow(lastSlot));
				if (portTimeWindowsRecord.getReturnSlot() instanceof EndPortSlot && portTimeWindowsRecord.getSlotFeasibleTimeWindow(portTimeWindowsRecord.getReturnSlot()) != null) {
					if (portTimeWindowsRecord.getResource().getName().contains("Mela")) {
						int z = 0;
					}
					int endIndex = portTimeWindowsRecord.getIndex(portTimeWindowsRecord.getReturnSlot()) + 1;
					int size = arrivalTimes[seqIndex].length;
					setTimeWindowsToEarliest(seqIndex, sequences.getSequence(seqIndex).size() - 1, portTimeWindowsRecord.getSlotFeasibleTimeWindow(portTimeWindowsRecord.getReturnSlot()));
					arrivalTimes[seqIndex][arrivalTimes[seqIndex].length - 1] = portTimeWindowsRecord.getSlotFeasibleTimeWindow(portTimeWindowsRecord.getReturnSlot()).getInclusiveStart();
					endsSet[seqIndex] = true;
					if (portTimeWindowsRecord.getSlotFeasibleTimeWindow(lastSlot).getExclusiveEnd() > portTimeWindowsRecord.getSlotFeasibleTimeWindow(portTimeWindowsRecord.getReturnSlot()).getInclusiveStart()) {
						int z = 0;
					}
					if (arrivalTimes[seqIndex][arrivalTimes[seqIndex].length - 1] < windowStartTime[seqIndex][arrivalTimes[seqIndex].length - 1]) {
						int z = 0;
					}
				}
			}
		}
	}

	private boolean isSequentialVessel(IResource resource) {
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
			return false;
		}
		return true;
	}

	private void updateFollowingPortTimeRecordStartTime(IPortTimeWindowsRecord first, IPortTimeWindowsRecord second) {
		IPortSlot lastSlotFirst = first.getSlots().get(first.getSlots().size() - 1);
		IPortSlot firstSlotSecond = second.getFirstSlot();
		ITimeWindow timeWindow = new TimeWindow(first.getSlotFeasibleTimeWindow(lastSlotFirst).getInclusiveStart(), second.getFirstSlotFeasibleTimeWindow().getInclusiveStart(),
				second.getFirstSlotFeasibleTimeWindow().getExclusiveEndFlex());
		second.setSlotFeasibleTimeWindow(firstSlotSecond, timeWindow);
	}

	private void priceBasedWindowTrimming(ISequences sequences, List<List<IPortTimeWindowsRecord>> portTimeWindowsRecords) {
		for (int seqIndex = 0; seqIndex < sequences.size(); seqIndex++) {
			for (IPortTimeWindowsRecord portTimeWindowsRecord : portTimeWindowsRecords.get(seqIndex)) {
				setFeasibleTimeWindows(portTimeWindowsRecord, seqIndex);
				timeWindowsTrimming.processCargo(portTimeWindowsRecord, arrivalTimes[seqIndex][0]);
				updateTimeWindows(portTimeWindowsRecord, seqIndex);
			}
		}
	}

	private void updateTimeWindows(IPortTimeWindowsRecord portTimeWindowsRecord, int seqIndex) {
		for (IPortSlot portSlot : portTimeWindowsRecord.getSlots()) {
			ITimeWindow timeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(portSlot);
			windowStartTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)] = timeWindow.getInclusiveStart();
			windowEndTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)] = timeWindow.getExclusiveEnd();
		}
	}

	/**
	 * The previous cargo will have changed the constraints on the time windows, so we must find the new
	 * feasible time windows before choosing an arrival time
	 * @param portTimeWindowsRecord
	 * @param seqIndex
	 */
	private void setFeasibleTimeWindowsUsingPrevious(IPortTimeWindowsRecord portTimeWindowsRecord, int seqIndex) {
		int prevFeasibleWindowStart = IPortSlot.NO_PRICING_DATE;
		for (IPortSlot portSlot : portTimeWindowsRecord.getSlots()) {
			final ITimeWindow timeWindow;
			int feasibleWindowStart, feasibleWindowEnd;
			if (portTimeWindowsRecord.getFirstSlot().equals(portSlot) && portTimeWindowsRecord.getIndex(portTimeWindowsRecord.getFirstSlot()) > 0) {
				// first load
				feasibleWindowStart = getFeasibleWindowStart(portTimeWindowsRecord, seqIndex);
				feasibleWindowEnd = getFeasibleWindowEnd(portTimeWindowsRecord, seqIndex, portSlot, feasibleWindowStart);
				timeWindow = new TimeWindow(feasibleWindowStart, feasibleWindowEnd);
			} else {
				if (prevFeasibleWindowStart == IPortSlot.NO_PRICING_DATE) { 
					feasibleWindowStart = windowStartTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)];
					feasibleWindowEnd = windowEndTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)];
				} else {
					feasibleWindowStart = Math.max(windowStartTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)], prevFeasibleWindowStart + minTimeToNextElement[seqIndex][portTimeWindowsRecord.getIndex(portSlot) - 1]);
					feasibleWindowEnd = Math.max(windowEndTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)], feasibleWindowStart + 1);
				}
				timeWindow = new TimeWindow(feasibleWindowStart, feasibleWindowEnd);
			}
			portTimeWindowsRecord.setSlotFeasibleTimeWindow(portSlot, timeWindow);
			prevFeasibleWindowStart = feasibleWindowStart;
		}
	}
	
	/**
	 * For round trip (nominal) cargoes, we don't care what else has happened on this vessel
	 * @param portTimeWindowsRecord
	 * @param seqIndex
	 */
	private void setFeasibleTimeWindowsRoundTrip(IPortTimeWindowsRecord portTimeWindowsRecord, int seqIndex) {
		int prevFeasibleWindowStart = IPortSlot.NO_PRICING_DATE;
		for (IPortSlot portSlot : portTimeWindowsRecord.getSlots()) {
			final ITimeWindow timeWindow;
			int feasibleWindowStart, feasibleWindowEnd;
			if (portTimeWindowsRecord.getFirstSlot().equals(portSlot)) {
				// first load
				feasibleWindowStart = windowStartTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)];
				feasibleWindowEnd = windowEndTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)];
				timeWindow = new TimeWindow(feasibleWindowStart, feasibleWindowEnd);
			} else {
				feasibleWindowStart = Math.max(windowStartTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)],
						prevFeasibleWindowStart
								+ minTimeToNextElement[seqIndex][portTimeWindowsRecord.getIndex(portSlot) - 1]);
				feasibleWindowEnd = Math.max(windowEndTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)],
						feasibleWindowStart + 1);
				timeWindow = new TimeWindow(feasibleWindowStart, feasibleWindowEnd);
			}
			portTimeWindowsRecord.setSlotFeasibleTimeWindow(portSlot, timeWindow);
			prevFeasibleWindowStart = feasibleWindowStart;
		}
	}

	public int getFeasibleWindowEnd(IPortTimeWindowsRecord portTimeWindowsRecord, int seqIndex, IPortSlot portSlot, int feasibleWindowStart) {
		return Math.max(feasibleWindowStart + 1, windowEndTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)]);
	}

	public int getFeasibleWindowStart(IPortTimeWindowsRecord portTimeWindowsRecord, int seqIndex) {
		return Math.max(
				windowStartTime[seqIndex][portTimeWindowsRecord.getIndex(portTimeWindowsRecord.getFirstSlot()) - 1]
						+ minTimeToNextElement[seqIndex][portTimeWindowsRecord.getIndex(portTimeWindowsRecord.getFirstSlot()) - 1],
				windowStartTime[seqIndex][portTimeWindowsRecord.getIndex(portTimeWindowsRecord.getFirstSlot())]);
	}

	private void setFeasibleTimeWindows(IPortTimeWindowsRecord portTimeWindowsRecord, int seqIndex) {
		for (IPortSlot portSlot : portTimeWindowsRecord.getSlots()) {
			int feasibleWindowStartTime = windowStartTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)];
			ITimeWindow timeWindow = new TimeWindow(feasibleWindowStartTime, getFeasibleWindowEnd(portTimeWindowsRecord, seqIndex, portSlot, feasibleWindowStartTime));
			portTimeWindowsRecord.setSlotFeasibleTimeWindow(portSlot, timeWindow);
		}
	}

	private void synchroniseShipToShipBindings() {
		// TODO: why do we not fix up later voyages? Why do we not loop multiple times here?
		// TODO: If the above is implemented, STS slots should probably arrive as early as possible.
		for (int i = 0; i < bindings.size(); i += 4) {
			final int discharge_seq = bindings.get(i);
			final int discharge_index = bindings.get(i + 1);
			final int load_seq = bindings.get(i + 2);
			final int load_index = bindings.get(i + 3);

			// sequence elements bound by ship-to-ship transfers are effectively the same slot, so the arrival times must be synchronised
			arrivalTimes[load_seq][load_index] = arrivalTimes[discharge_seq][discharge_index];
		}

	}

	private void setTimeWindowsToEarliest(final int seq) {
		if (arrivalTimes[seq] == null) {
			return;
		}

		if (sizes[seq] > 0) {
			final int lastIndex = sizes[seq] - 1;
			for (int pos = 0; pos < lastIndex; pos++) {
				final int min = getMinArrivalTime(seq, pos);
				final int max = getMaxArrivalTime(seq, pos);
				arrivalTimes[seq][pos] = min;
				// TODO force sync this with any ship-to-ship bindings
			}

			// Set the arrival time at the last bit to be as early as possible; VPO will relax it if necessary.
			if (!endsSet[seq]) {
				arrivalTimes[seq][lastIndex] = getMinArrivalTime(seq, lastIndex);
			} else {
				if (arrivalTimes[seq][lastIndex - 1] > arrivalTimes[seq][lastIndex]) {
					int z = 0;
				}
			}

			arrivalTimes[seq][0] = getMaxArrivalTimeForNextArrival(seq, 0);
		}
	}

	private void setTimeWindowsToEarliest(final int seq, final int index, ITimeWindow timeWindow) {
		if (arrivalTimes[seq] == null) {
			return;
		}
		if (sizes[seq] >= index) {
			windowStartTime[seq][index] = timeWindow.getInclusiveStart();
		}
	}

}
