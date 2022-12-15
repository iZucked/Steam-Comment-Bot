/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * @author berkan
 *
 */
public interface ITimeZoneToUtcOffsetProvider extends IDataComponentProvider {

	int UTC(int localTime, @Nullable IPortSlot portSlot);

	int UTC(int localTime, @Nullable IPort port);

	int UTC(int localTime, @Nullable String timezoneId);

	int localTime(int utcTime, @Nullable String timezoneId);

	int localTime(int utcTime, @Nullable IPort port);

	int localTime(int utcTime, @Nullable IPortSlot portSlot);
	
}


