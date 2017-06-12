package com.mmxlabs.scheduler.optimiser.scheduling;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.inject.ImplementedBy;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;

@NonNullByDefault
public interface ISlotTimeScheduler {

	void startSequence(IResource resource);

	int scheduleSlot(IResource resource, IPortSlot portSlot, IPortTimeWindowsRecord portTimeWindowsRecord, PortTimesRecord partialPortTimesRecord, int expectedArrivalTime);

}
