/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

/**
 * Provides price intervals for a specific date range. Uses include trimming
 * windows to use the most favourable price.
 * 
 * @author achurchill
 *
 */
@NonNullByDefault
public interface IPriceIntervalProvider {

	/**
	 * Provides an ordered list of dates and price intervals
	 * 
	 * @param slot TODO
	 * @return
	 */

	List<int @NonNull []> getPriceIntervals(IPortSlot slot, int startOfRange, int endOfRange, IPortTimeWindowsRecord portTimeWindowRecord);

	// TODO: extend to load/discharge interface
	@Nullable
	List<@NonNull Integer> getPriceHourIntervals(IPortSlot slot, int start, int end, IPortTimeWindowsRecord portTimeWindowsRecord);
}
