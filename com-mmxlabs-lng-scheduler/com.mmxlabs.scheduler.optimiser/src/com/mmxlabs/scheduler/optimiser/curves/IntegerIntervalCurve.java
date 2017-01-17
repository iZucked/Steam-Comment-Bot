/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.curves;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;

/**
 * Concrete implementation of {@link IIntegerIntervalCurve}
 */
public class IntegerIntervalCurve implements IIntegerIntervalCurve {

	private final TreeSet<Integer> intervals = new TreeSet<Integer>();

	public IntegerIntervalCurve() {

	}

	@Override
	public void add(int point) {
		intervals.add(point);
	}

	@Override
	public void addAll(Collection<Integer> points) {
		intervals.addAll(points);
	}

	@Override
	public int @NonNull [] getIntervalRange(int start, int end) {
		NavigableSet<Integer> subSet = intervals.subSet(start, false, end, false);
		int[] integerList = new int[subSet.size() + 2];
		int idx = 0;
		integerList[0] = start;
		for (int i : subSet) {
			integerList[++idx] = i;
		}
		integerList[++idx] = end;
		return integerList;
	}

	@Override
	public int[] getIntervalRangePricingTime(int start, int end, String tz, int additionalOffset, ITimeZoneToUtcOffsetProvider offsetProvider) {
		NavigableSet<Integer> subSet = intervals.subSet(start, false, end, false);
		int[] integerList = new int[subSet.size() + 2];
		int idx = 0;
		integerList[0] = convertTimeToLocal(start, tz, additionalOffset, offsetProvider);
		for (int i : subSet) {
			integerList[++idx] = convertTimeToLocal(i, tz, additionalOffset, offsetProvider);
		}
		integerList[++idx] = convertTimeToLocal(end, tz, additionalOffset, offsetProvider);
		return integerList;
	}

	@Override
	public int[] getIntervalRangeSchedulingTime(int start, int end, String tz, int additionalOffset, ITimeZoneToUtcOffsetProvider offsetProvider) {
		NavigableSet<Integer> subSet = intervals.subSet(start, false, end, false);
		int[] integerList = new int[subSet.size() + 2];
		int idx = 0;
		integerList[0] = convertTimeToUTC(start, tz, additionalOffset, offsetProvider);
		for (int i : subSet) {
			integerList[++idx] = convertTimeToUTC(i, tz, additionalOffset, offsetProvider);
		}
		integerList[++idx] = convertTimeToUTC(end, tz, additionalOffset, offsetProvider);
		return integerList;
	}

	private int convertTimeToLocal(int utcTime, String tz, int additionalOffset, ITimeZoneToUtcOffsetProvider offsetProvider) {
		return offsetProvider.localTime(utcTime, tz) + additionalOffset; // note: adding additionalOffset
	}

	private int convertTimeToUTC(int localTime, String tz, int additionalOffset, ITimeZoneToUtcOffsetProvider offsetProvider) {
		return offsetProvider.UTC(localTime, tz) - additionalOffset; // note: subtracting additionalOffset
	}

	@Override
	public int getNextInterval(int point) {
		return intervals.higher(point);
	}

	@Override
	public int getPreviousInterval(int point) {
		return intervals.lower(point);
	}

	@Override
	public List<int[]> getIntervalsAs2dList(int start, int end) {
		int[] range = getIntervalRange(start, end);
		List<int[]> intervals = new LinkedList<>();
		int idx = 0;
		int prev = Integer.MIN_VALUE;
		int curr;
		for (int i : range) {
			curr = i;
			if (prev != Integer.MIN_VALUE) {
				intervals.add(new int[] { prev, curr });
			}
			prev = curr;
		}
		return intervals;
	}

	@Override
	public int @NonNull [] @NonNull [] getIntervalsAs2dArray(int start, int end) {
		int @NonNull [] range = getIntervalRange(start, end);
		int @NonNull [] @NonNull [] intervals = new int @NonNull [range.length - 1] @NonNull [2];
		int idx = 0;
		int prev = Integer.MIN_VALUE;
		int curr;
		for (int i : range) {
			curr = i;
			if (prev != Integer.MIN_VALUE) {
				intervals[idx][0] = prev;
				intervals[idx][1] = curr;
				idx++;
			}
			prev = curr;
		}
		return intervals;
	}

	@Override
	public int[] getIntervalsAs1dArray(int start, int end) {
		return getIntervalRange(start, end);
	}

	@Override
	public List<Integer> getIntervalsAs1dList(int start, int end) {
		NavigableSet<Integer> subSet = intervals.subSet(start, false, end, false);
		List<Integer> intList = new LinkedList<>();
		intList.add(start);
		for (int i : subSet) {
			intList.add(i);
		}
		intList.add(end);
		return intList;
	}

}
