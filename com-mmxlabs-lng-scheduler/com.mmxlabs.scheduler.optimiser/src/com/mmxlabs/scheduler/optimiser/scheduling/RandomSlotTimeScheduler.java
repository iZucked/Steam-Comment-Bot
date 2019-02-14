/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
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
	public void startSequence(final IResource resource) {
		random = new Random(seed);
	}

	@Override
	public int scheduleSlot(final IResource resource, final IPortSlot portSlot, final IPortTimeWindowsRecord portTimeWindowsRecord, final PortTimesRecord partialPortTimesRecord, final int expectedArrivalTime) {
		final ITimeWindow window = portTimeWindowsRecord.getSlotFeasibleTimeWindow(portSlot);
		final int start = Math.max(window.getInclusiveStart(), expectedArrivalTime);
		final int arrivalTime = RandomHelper.nextIntBetween(random, start, window.getExclusiveEnd() - 1);
		if (portSlot instanceof RoundTripCargoEnd) {
			// Reset
			random = new Random(seed);
		}

		return arrivalTime;
	}
}
