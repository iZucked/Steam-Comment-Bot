package com.mmxlabs.scheduler.optimiser.scheduling;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;

public class EarliestSlotTimeScheduler implements ISlotTimeScheduler {

	@Override
	public void startSequence(@NonNull IResource resource) {

	}

	@Override
	public int scheduleSlot(@NonNull IResource resource, @NonNull IPortSlot portSlot, @NonNull IPortTimeWindowsRecord portTimeWindowsRecord, @NonNull PortTimesRecord partialPortTimesRecord,
			int expectedArrivalTime) {
		return Math.max(expectedArrivalTime, portTimeWindowsRecord.getSlotFeasibleTimeWindow(portSlot).getInclusiveStart());
	}
}
