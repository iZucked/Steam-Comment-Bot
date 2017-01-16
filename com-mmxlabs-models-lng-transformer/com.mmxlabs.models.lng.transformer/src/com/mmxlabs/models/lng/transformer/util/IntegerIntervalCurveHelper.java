/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.util;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.curves.IntegerIntervalCurve;

/**
 * Util class used to create {@link IIntegerIntervalCurve} instances
 * @author achurchill
 *
 */
public class IntegerIntervalCurveHelper {

	@Inject
	DateAndCurveHelper dateAndCurveHelper;
	
	@Inject
	ModelEntityMap modelEntityMap;
	
	@NonNull
	public List<Integer> getMonthAlignedWithOffsetDatesForRange(int start, int end, int offsetInHours) {
		List<Integer> intervals = new LinkedList<>();
		int proposed = start;
		while (proposed < end) {
			intervals.add(proposed);
			proposed = getNextMonth(intervals.get(intervals.size()-1)) + offsetInHours ;
		}
		intervals.add(end);
		return intervals;
	}

	@NonNull
	public IIntegerIntervalCurve getMonthAlignedIntegerIntervalCurve(int start, int end, int offsetInHours) {
		IIntegerIntervalCurve intervals = new IntegerIntervalCurve();
		int proposed = start;
		while (proposed < end) {
			proposed = getNextMonth(proposed);
			intervals.add(proposed + offsetInHours);
		}
		intervals.add(end);
		return intervals;
	}

	@NonNull
	public IIntegerIntervalCurve getSplitMonthAlignedWithOffsetDatesForRange(int start, int end, int offsetInHours, int splitInDays) {
		IIntegerIntervalCurve intervals = new IntegerIntervalCurve();
		int proposed = start + offsetInHours;
		while (proposed < end) {
			intervals.add(proposed);
			proposed = getNextMonthOrSplit(proposed + offsetInHours, splitInDays) ;
		}
		intervals.add(end);
		return intervals;
	}

	/**
	 * Get the date in hours of the first day of the next month
	 * @param hours
	 * @return
	 */
	@SuppressWarnings("null")
	public int getNextMonth(int hours) {
		ZonedDateTime asDate = modelEntityMap.getDateFromHours(hours, "UTC");
		asDate = asDate.plusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
		return dateAndCurveHelper.convertTime(asDate);
	}

	/**
	 * Get the date in hours of the first day of the previous month
	 * @param hours
	 * @return
	 */
	@SuppressWarnings("null")
	public int getPreviousMonth(int hours) {
		ZonedDateTime asDate = modelEntityMap.getDateFromHours(hours, "UTC");
		asDate = asDate.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
		return dateAndCurveHelper.convertTime(asDate);
	}

	/**
	 * Get the date in hours of the first day of the next month
	 * @param hours
	 * @return
	 */
	@SuppressWarnings("null")
	public int getNextMonthOrSplit(int hours, int splitInDays) {
		ZonedDateTime asDate = modelEntityMap.getDateFromHours(hours, "UTC");
		if (asDate.getDayOfMonth() < splitInDays) {			
			asDate = asDate.withDayOfMonth(splitInDays).withHour(0).withMinute(0).withSecond(0).withNano(0);
		} else {
			asDate = asDate.plusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
		}
		return dateAndCurveHelper.convertTime(asDate);
	}

}
