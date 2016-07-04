/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * @author berkan
 *
 */
public interface ITimeZoneToUtcOffsetProvider extends IDataComponentProvider {

	int UTC(int localTime, IPortSlot portSlot);

	int UTC(int localTime, IPort port);

	int UTC(int localTime, String timezoneId);

	int localTime(int utcTime, String timezoneId);

	int localTime(int utcTime, IPort port);

	int localTime(int utcTime, IPortSlot portSlot);
	
}


