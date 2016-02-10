/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.curves;

import java.util.Collection;
import java.util.List;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;


/**
 * An interface for an interval curve.
 * 
 * @author achurchill
 * 
 */
public interface IIntegerIntervalCurve {

	int[] getIntervalRange(final int start, final int end);
	
	int[] getIntervalRangePricingTime(int start, int end, String tz, int additionalOffset, ITimeZoneToUtcOffsetProvider offsetProvider);
	
	int[] getIntervalRangeSchedulingTime(int start, int end, String tz, int additionalOffset, ITimeZoneToUtcOffsetProvider offsetProvider);
	
	int getNextInterval(final int point);
	
	int getPreviousInterval(final int point);

	void add(int point);

	void addAll(Collection<Integer> points);

	int[] getIntervalsAs1dArray(int start, int end);

	int[][] getIntervalsAs2dArray(int start, int end);

	List<Integer> getIntervalsAs1dList(int start, int end);

	List<int[]> getIntervalsAs2dList(int start, int end);
	
}
