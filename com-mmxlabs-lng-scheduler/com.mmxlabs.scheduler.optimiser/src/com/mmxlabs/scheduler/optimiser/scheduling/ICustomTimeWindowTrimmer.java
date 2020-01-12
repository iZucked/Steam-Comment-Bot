/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduling;

import java.util.List;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

public interface ICustomTimeWindowTrimmer {
	void trimWindows(IResource resource, List<IPortTimeWindowsRecord> trimmedWindows, final MinTravelTimeData travelTimeData);
}
