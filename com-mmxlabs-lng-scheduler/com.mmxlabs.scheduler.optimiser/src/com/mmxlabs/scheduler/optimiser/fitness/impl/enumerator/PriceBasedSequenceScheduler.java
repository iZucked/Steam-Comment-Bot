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
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
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

	@Override
	public int @Nullable [][] schedule(@NonNull final ISequences sequences) {
		setSequences(sequences);

		prepare();
		sequentialEarliestTimePriceBasedWindowTrimming(sequences, portTimeWindowsRecords);
		for (int index = 0; index < sequences.size(); ++index) {
			setTimeWindowsToEarliest(index);
		}
		synchroniseShipToShipBindings();
		return arrivalTimes;
	}

	private void sequentialEarliestTimePriceBasedWindowTrimming(ISequences sequences, List<List<IPortTimeWindowsRecord>> portTimeWindowsRecords) {
		for (int seqIndex = 0; seqIndex < sequences.size(); seqIndex++) {
			List<IPortTimeWindowsRecord> list = portTimeWindowsRecords.get(seqIndex);
			for (int idx = 0; idx < list.size(); idx++) {
				IPortTimeWindowsRecord portTimeWindowsRecord = list.get(idx);
				setFeasibleTimeWindowsUsingPrevious(portTimeWindowsRecord, seqIndex);
				timeWindowsTrimming.processCargo(portTimeWindowsRecord);
				updateTimeWindows(portTimeWindowsRecord, seqIndex);
				IPortSlot lastSlot = portTimeWindowsRecord.getSlots().get(portTimeWindowsRecord.getSlots().size() - 1);
				setTimeWindowsToEarliest(seqIndex, portTimeWindowsRecord.getIndex(lastSlot), portTimeWindowsRecord.getSlotFeasibleTimeWindow(lastSlot));
			}
		}
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
				timeWindowsTrimming.processCargo(portTimeWindowsRecord);
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

	private void setFeasibleTimeWindowsUsingPrevious(IPortTimeWindowsRecord portTimeWindowsRecord, int seqIndex) {
		for (IPortSlot portSlot : portTimeWindowsRecord.getSlots()) {
			final ITimeWindow timeWindow;
			if (portTimeWindowsRecord.getFirstSlot().equals(portSlot) && portTimeWindowsRecord.getIndex(portTimeWindowsRecord.getFirstSlot()) > 0) {
				// first load
				timeWindow = new TimeWindow(Math.max(
						windowStartTime[seqIndex][portTimeWindowsRecord.getIndex(portTimeWindowsRecord.getFirstSlot()) - 1]
								+ minTimeToNextElement[seqIndex][portTimeWindowsRecord.getIndex(portTimeWindowsRecord.getFirstSlot()) - 1],
						windowStartTime[seqIndex][portTimeWindowsRecord.getIndex(portTimeWindowsRecord.getFirstSlot())]), windowEndTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)]);
			} else {
				timeWindow = new TimeWindow(windowStartTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)], windowEndTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)]);
			}
			portTimeWindowsRecord.setSlotFeasibleTimeWindow(portSlot, timeWindow);
		}
	}

	private void setFeasibleTimeWindows(IPortTimeWindowsRecord portTimeWindowsRecord, int seqIndex) {
		for (IPortSlot portSlot : portTimeWindowsRecord.getSlots()) {
			ITimeWindow timeWindow = new TimeWindow(windowStartTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)], windowEndTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)]);
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
			arrivalTimes[seq][lastIndex] = getMinArrivalTime(seq, lastIndex);

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
