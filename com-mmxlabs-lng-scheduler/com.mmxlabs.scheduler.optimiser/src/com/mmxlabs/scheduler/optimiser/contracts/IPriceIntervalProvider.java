package com.mmxlabs.scheduler.optimiser.contracts;

import java.util.List;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

/**
 * Provides price intervals for a specific date range. Uses include trimming windows to use the most favourable price.
 * @author achurchill
 *
 */
public interface IPriceIntervalProvider {

	/**
	 * Provides an ordered list of dates and price intervals 
	 * @param slot TODO
	 * @return
	 */
	public List<int[]> getPriceIntervals(IPortSlot slot, int startOfRange, int endOfRange, IPortTimeWindowsRecord portTimeWindowRecord);

	/**
	 * Provides the date range for the highest price period in a given range 
	 * @param loadOption
	 * @param dischargeOption
	 * @return
	 */
	public Pair<Integer, Integer> getHighestPriceInterval(int startOfRange, int endOfRange, IPortSlot portSlot, IPortTimeWindowsRecord portTimeWindowRecord); 
	
	/**
	 * Provides the date range for the lowest price period in a given range 
	 * @param loadOption
	 * @param dischargeOption
	 * @return
	 */
	public Pair<Integer, Integer> getLowestPriceInterval(int startOfRange, int endOfRange, IPortSlot portSlot, IPortTimeWindowsRecord portTimeWindowRecord);

	// TODO: extend to load/discharge interface
	public List<Integer> getPriceHourIntervals(IPortSlot slot, int start, int end, IPortTimeWindowsRecord portTimeWindowsRecord);	
}
