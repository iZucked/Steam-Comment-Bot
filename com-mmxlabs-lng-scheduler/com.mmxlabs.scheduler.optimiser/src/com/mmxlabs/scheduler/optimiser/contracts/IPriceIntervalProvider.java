/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

/**
 * Provides price intervals for a specific date range. Uses include trimming windows to use the most favourable price.
 * 
 * @author achurchill
 *
 */
public interface IPriceIntervalProvider {

	/**
	 * Provides an ordered list of dates and price intervals
	 * 
	 * @param slot
	 *            TODO
	 * @return
	 */
	List<int[]> getPriceIntervals(@NonNull IPortSlot slot, int startOfRange, int endOfRange, @NonNull IPortTimeWindowsRecord portTimeWindowRecord);

	// TODO: extend to load/discharge interface
	List<Integer> getPriceHourIntervals(@NonNull IPortSlot slot, int start, int end, @NonNull IPortTimeWindowsRecord portTimeWindowsRecord);
}
