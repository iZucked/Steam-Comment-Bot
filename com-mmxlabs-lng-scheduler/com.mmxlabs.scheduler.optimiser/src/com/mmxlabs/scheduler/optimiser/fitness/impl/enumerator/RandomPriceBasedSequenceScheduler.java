/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator;

import java.util.List;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.RandomHelper;
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
public class RandomPriceBasedSequenceScheduler extends EnumeratingSequenceScheduler {
	private final int seed = 0;
	private Random random;

	@Inject
	TimeWindowsTrimming timeWindowsTrimming;

	@Override
	public int @Nullable [][] schedule(@NonNull final ISequences sequences) {
		random = new Random(seed);

		setSequences(sequences);

		prepare();
		trim();
		priceBasedWindowTrimming(sequences, portTimeWindowsRecords);
		for (int index = 0; index < sequences.size(); ++index) {
			random.setSeed(seed);
			randomise(index);
		}
		synchroniseShipToShipBindings();

		return arrivalTimes;
	}

	private void priceBasedWindowTrimming(final ISequences sequences, final List<List<IPortTimeWindowsRecord>> portTimeWindowsRecords) {
		for (int seqIndex = 0; seqIndex < sequences.size(); seqIndex++) {
			for (final IPortTimeWindowsRecord portTimeWindowsRecord : portTimeWindowsRecords.get(seqIndex)) {
				setFeasibleTimeWindows(portTimeWindowsRecord, seqIndex);
				timeWindowsTrimming.processCargo(portTimeWindowsRecord, arrivalTimes[seqIndex][0]);
				updateTimeWindows(portTimeWindowsRecord, seqIndex);
			}
		}
	}

	private void updateTimeWindows(final IPortTimeWindowsRecord portTimeWindowsRecord, final int seqIndex) {
		for (final IPortSlot portSlot : portTimeWindowsRecord.getSlots()) {
			final ITimeWindow timeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(portSlot);
			windowStartTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)] = timeWindow.getInclusiveStart();
			windowEndTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)] = timeWindow.getExclusiveEnd();
		}
	}

	private void setFeasibleTimeWindows(final IPortTimeWindowsRecord portTimeWindowsRecord, final int seqIndex) {
		for (final IPortSlot portSlot : portTimeWindowsRecord.getSlots()) {
			final ITimeWindow timeWindow = new TimeWindow(windowStartTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)], windowEndTime[seqIndex][portTimeWindowsRecord.getIndex(portSlot)]);
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

	private void randomise(final int seq) {
		if (arrivalTimes[seq] == null) {
			return;
		}

		if (sizes[seq] > 0) {
			final int lastIndex = sizes[seq] - 1;
			for (int pos = 0; pos < lastIndex; pos++) {
				final int min = getMinArrivalTime(seq, pos);
				final int max = getMaxArrivalTime(seq, pos);
				arrivalTimes[seq][pos] = RandomHelper.nextIntBetween(random, min, max);
				// TODO force sync this with any ship-to-ship bindings
			}

			// Set the arrival time at the last bit to be as early as possible; VPO will relax it if necessary.
			arrivalTimes[seq][lastIndex] = getMinArrivalTime(seq, lastIndex);

			arrivalTimes[seq][0] = getMaxArrivalTimeForNextArrival(seq, 0);
		}
	}

}
