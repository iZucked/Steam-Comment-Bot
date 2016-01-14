package com.mmxlabs.models.lng.transformer.util;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.joda.time.DateTime;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.curves.IntegerIntervalCurve;

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
		for (int i : intervals.getIntervalsAs1dArray(start, end)) {
			System.out.println(i); // DO NOT COMMIT
			System.out.println(modelEntityMap.getDateFromHours(i, "UTC"));
		}
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
		for (int i : intervals.getIntervalsAs1dArray(start, end)) {
			System.out.println(i); // DO NOT COMMIT
			System.out.println(modelEntityMap.getDateFromHours(i, "UTC"));
		}
		return intervals;
	}

	/**
	 * Get the date in hours of the first day of the next month
	 * @param hours
	 * @return
	 */
	@SuppressWarnings("null")
	public int getNextMonth(int hours) {
		DateTime asDate = modelEntityMap.getDateFromHours(hours, "UTC");
		asDate = asDate.plusMonths(1).withDayOfMonth(1).withTimeAtStartOfDay();
		return dateAndCurveHelper.convertTime(asDate);
	}

	/**
	 * Get the date in hours of the first day of the next month
	 * @param hours
	 * @return
	 */
	@SuppressWarnings("null")
	public int getPreviousMonth(int hours) {
		DateTime asDate = modelEntityMap.getDateFromHours(hours, "UTC");
		asDate = asDate.minusMonths(1).withDayOfMonth(1);
		return dateAndCurveHelper.convertTime(asDate);
	}

	/**
	 * Get the date in hours of the first day of the next month
	 * @param hours
	 * @return
	 */
	@SuppressWarnings("null")
	public int getNextMonthOrSplit(int hours, int splitInDays) {
		DateTime asDate = modelEntityMap.getDateFromHours(hours, "UTC");
		if (asDate.getDayOfMonth() < splitInDays) {			
			asDate = asDate.withDayOfMonth(splitInDays);
		} else {
			asDate = asDate.plusMonths(1).withDayOfMonth(1);
		}
		return dateAndCurveHelper.convertTime(asDate);
	}

}
