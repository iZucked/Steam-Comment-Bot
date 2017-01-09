/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;

/**
 * An {@link IEndEventScheduler} defines how a vessel with an open-ended end date should end. Normally we define a notional optimisation end date and idle from the last event to thi date. However we
 * often need to adjust the final leg based on client specific rules. For example, return as fast a possible to the last load and then just count charter cost until optimisation end.
 * 
 * @author Simon Goodall
 *
 */
public interface IEndEventScheduler {

	/**
	 * Given the partially complete {@link PortTimesRecord} (where the return slot is not yet defined) determine the return slot details. Always return an additional {@link IPortTimesRecord} for the
	 * end event for correctly allocating P&L. We do this here so we can modify the end event dates correctly - for example an additional {@link IPortTimesRecord} could be returned with an extended
	 * end event. It is not intended to return further voyages.
	 * 
	 * @param resource
	 * @param vesselAvailability
	 * @param partialPortTimesRecord
	 * @param scheduledTime
	 * @param endEventSlot
	 * @return
	 */
	@NonNull
	List<@NonNull IPortTimesRecord> scheduleEndEvent(@NonNull IResource resource, @NonNull IVesselAvailability vesselAvailability, @NonNull PortTimesRecord partialPortTimesRecord, int scheduledTime,
			@NonNull IPortSlot endEventSlot);
}
