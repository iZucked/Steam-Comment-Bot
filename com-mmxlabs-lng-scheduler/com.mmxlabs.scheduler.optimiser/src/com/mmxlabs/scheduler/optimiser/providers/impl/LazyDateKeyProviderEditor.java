/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.management.timer.Timer;

import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.providers.IDateKeyProviderEditor;

/**
 * A lazy implementation of {@link IDateKeyProviderEditor}. This implementation will calculate date keys on demand. This does mean that calls to {@link #getDateKeyFromHours(int)} may have an
 * unpredicatable access time during the first sets of calls, but will generally be {@link TreeSet} access performance characteristics. This implementation requires a call to
 * {@link #setTimeZero(long)} to indicate when time zero in hours will be. It is expected that a {@link ISchedulerBuilder} implementation will invoke this method when this value is known. This
 * implementation will return date keys for the start of the month (in UTC timezone) for which the hours is contained.
 * 
 */
public class LazyDateKeyProviderEditor implements IDateKeyProviderEditor {

	/**
	 * How far forward to calculate date keys. This is used to avoid repetitive calculations when successive calls to {@link #getDateKeyFromHours(int)} are only one month apart. Currently calculate
	 * the current date and to about 6 months ahead.
	 */
	private static final int forwardExtend = 24 * 180;

	/**
	 * 
	 */
	private int lastExtent = Integer.MAX_VALUE;
	private long timeZeroInMillis;

	/**
	 * A sorted set of date keys - the number of hours from time zero that a month begins in UTC time zone
	 */
	private final TreeSet<Integer> dateKeySet = new TreeSet<Integer>();

	@Override
	public int getDateKeyFromHours(final int hours) {

		// Check to see whether we have enough information and calculate it if not.
		if (lastExtent == Integer.MAX_VALUE || hours > lastExtent) {
			buildTimeToDateKeyMap(hours + forwardExtend);
		}
		// Return the start of the month boundary.
		return dateKeySet.floor(hours);
	}

	@Override
	public void setTimeZero(final long timeZeroInMillis) {
		// Reset

		// Clear date
		dateKeySet.clear();
		lastExtent = Integer.MAX_VALUE;
		this.timeZeroInMillis = timeZeroInMillis;
	}

	private void buildTimeToDateKeyMap(final int targetExtent) {

		final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

		long currentTimeInMillis;
		if (lastExtent == Integer.MAX_VALUE) {
			assert (dateKeySet.isEmpty());
			// No previous data, start from scratch

			// Set time 0.
			cal.setTimeInMillis(timeZeroInMillis);

			// Roll back to start of month.
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.DAY_OF_MONTH, 1);

			// Initial milliseconds
			currentTimeInMillis = cal.getTimeInMillis();
		} else {
			// Take last entry
			currentTimeInMillis = lastExtent * Timer.ONE_HOUR;
		}
		// Get number of hours from start time - this will be zero or negative.
		int currentTime = (int) ((currentTimeInMillis - timeZeroInMillis) / Timer.ONE_HOUR);
		cal.setTimeInMillis(currentTimeInMillis);
		// Store entry
		dateKeySet.add(currentTime);
		while (currentTime < targetExtent) {
			// Roll forward one month
			cal.add(Calendar.MONTH, 1);
			// Update time information
			currentTimeInMillis = cal.getTimeInMillis();
			currentTime = (int) ((currentTimeInMillis - timeZeroInMillis) / Timer.ONE_HOUR);
			// Store entry
			dateKeySet.add(currentTime);
		}

		lastExtent = currentTime;

	}

}
