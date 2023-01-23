/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.time.ZonedDateTime;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * Useful binding for debugging. Shouldn't be used in the real code.
 *
 */
public interface IExternalDateProvider {
    @NonNull
    ZonedDateTime getDateFromHours(final int hours, final String tz);
    /**
     * Convert from hours, relative to earliest time, to a Date object
     * 
     * @param hours
     * @param port
     * @return
     */
    @NonNull
    ZonedDateTime getDateFromHours(final int hours, @Nullable final IPort port);
    
    @NonNull
	ZonedDateTime getEarliestTime();
    
    @NonNull
	ZonedDateTime getLatestTime();
}
