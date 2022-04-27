/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.curves;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;

public class LazyIntegerIntervalCurve implements IIntegerIntervalCurve {

	@Nullable
	private IIntegerIntervalCurve defaultIntervalCurve;
	private ThreadLocal<IIntegerIntervalCurve> wrappedCurve = new ThreadLocal<>();
	final Function<ISeries, IIntegerIntervalCurve> splitMonthInitialiser;
	
	public LazyIntegerIntervalCurve(@NonNull final IIntegerIntervalCurve defaultCurve, final Function<ISeries, IIntegerIntervalCurve> splitMonthInitialiser) {
		defaultIntervalCurve = defaultCurve;
		this.splitMonthInitialiser = splitMonthInitialiser;
	}

	public void initialise() {
		wrappedCurve.set(defaultIntervalCurve);
	}

	public void initialise(final ISeries series) {
		final IIntegerIntervalCurve wrapped = splitMonthInitialiser.apply(series);
		wrappedCurve.set(wrapped);
	}

	@Override
	public int[] getIntervalRange(int start, int end) {
		return wrappedCurve.get().getIntervalRange(start, end);
	}

	@Override
	public int[] getIntervalRangePricingTime(int start, int end, String tz, int additionalOffset, ITimeZoneToUtcOffsetProvider offsetProvider) {
		return wrappedCurve.get().getIntervalRangePricingTime(start, end, tz, additionalOffset, offsetProvider);
	}

	@Override
	public int[] getIntervalRangeSchedulingTime(int start, int end, String tz, int additionalOffset, ITimeZoneToUtcOffsetProvider offsetProvider) {
		return wrappedCurve.get().getIntervalRangeSchedulingTime(start, end, tz, additionalOffset, offsetProvider);
	}

	@Override
	public int getNextInterval(int point) {
		return wrappedCurve.get().getNextInterval(point);
	}

	@Override
	public int getPreviousInterval(int point) {
		return wrappedCurve.get().getPreviousInterval(point);
	}

	@Override
	public void add(int point) {
		wrappedCurve.get().add(point);
	}

	@Override
	public void addAll(Collection<Integer> points) {
		wrappedCurve.get().addAll(points);
	}

	@Override
	public int @NonNull [] getIntervalsAs1dArray(int start, int end) {
		return wrappedCurve.get().getIntervalsAs1dArray(start, end);
	}

	@Override
	public int @NonNull [] @NonNull [] getIntervalsAs2dArray(int start, int end) {
		return wrappedCurve.get().getIntervalsAs2dArray(start, end);
	}

	@Override
	public List<Integer> getIntervalsAs1dList(int start, int end) {
		return wrappedCurve.get().getIntervalsAs1dList(start, end);
	}

	@Override
	public List<int[]> getIntervalsAs2dList(int start, int end) {
		return wrappedCurve.get().getIntervalsAs2dList(start, end);
	}

	public void clear() {
		wrappedCurve.remove();
	}

}
