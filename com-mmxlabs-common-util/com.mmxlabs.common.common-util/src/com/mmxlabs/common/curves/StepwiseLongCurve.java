/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

import java.util.Collections;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeMap;

/**
 * An implementation of ICurve which consists of a bunch of intervals on the x-axis with corresponding fixed values on the y-axis. Intervals are specified using
 * {@link StepwiseLongCurve#setValueAfter(int, int)}, proceeding from least x-value to greatest x-value.
 * 
 * @author hinton
 * 
 */
public class StepwiseLongCurve implements ILongCurve {
	private long defaultValue;
	private final TreeMap<Integer, Long> intervals = new TreeMap<>();
	
	@Override
	public NavigableSet<Integer> getChangePoints() { 
		return Collections.unmodifiableNavigableSet(intervals.navigableKeySet());
	}
	
	/**
	 * Set the default value - this is what will be returned for any queries to points in the curve's specified range
	 * 
	 * @param defaultValue
	 */
	public void setDefaultValue(final long defaultValue) {
		this.defaultValue = defaultValue;
	}

	public long getDefaultValue() {
		return defaultValue;
	}

	/**
	 * This sets everything to the right of lowerBound (inclusive) to the given value. You should use this method to set up intervals from least x-value to greatest x-value.
	 * 
	 * @param lowerBound
	 * @param value
	 */
	public void setValueAfter(final int lowerBound, final long value) {
		intervals.put(lowerBound, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.common.curves.ICurve#getValueAtPoint(double)
	 */
	@Override
	public long getValueAtPoint(final int pointInt) {
		final Map.Entry<Integer, Long> value = intervals.lowerEntry(pointInt + 1);
		return value == null ? defaultValue : value.getValue().longValue();
	}
}
