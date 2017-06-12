package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.Random;

import com.mmxlabs.common.RandomHelper;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.RoundTripCargoEnd;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;

public class RandomSlotTimeScheduler implements ISlotTimeScheduler {

	private final int seed = 0;

	private Random random;

	@Override
	public void startSequence(IResource resource) {
		random = new Random(seed);
	}

	@Override
	public int scheduleSlot(IResource resource, IPortSlot portSlot, IPortTimeWindowsRecord portTimeWindowsRecord, PortTimesRecord partialPortTimesRecord, int expectedArrivalTime) {
		ITimeWindow window = portTimeWindowsRecord.getSlotFeasibleTimeWindow(portSlot);
		int start = Math.max(window.getInclusiveStart(), expectedArrivalTime);
		if (portSlot.getId().contains("BG_Charter")) {
			int ii = 0;
		}
		int arrivalTime = RandomHelper.nextIntBetween(random, start, window.getExclusiveEnd() - 1);
		if (portSlot instanceof RoundTripCargoEnd) {
			// Reset
			random = new Random(seed);
		}

		return arrivalTime;
	}
}
