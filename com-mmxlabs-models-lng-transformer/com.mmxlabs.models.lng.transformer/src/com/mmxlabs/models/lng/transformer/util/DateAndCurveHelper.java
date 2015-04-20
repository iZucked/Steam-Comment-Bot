/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.util;

import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.YearMonth;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.builder.IBuilderExtension;

/**
 * Small helper class which is intended to be injected into external {@link ITransformerExtension}s and {@link IBuilderExtension}s to help with date and time conversion. This also has some routines
 * for creating {@link ICurve}s
 * 
 */
public class DateAndCurveHelper {

	private DateTime earliestTime;

	/**
	 * Convert a date into relative hours; returns the number of hours between windowStart and earliest.
	 * 
	 * @param earliest
	 * @param windowStart
	 * @return number of hours between earliest and windowStart
	 */
	public int convertTime(final DateTime earliest, final DateTime windowStart) {
		return Hours.hoursBetween(earliest, windowStart).getHours();
	}

	public int convertTime(final DateTime earliest, final YearMonth windowStart) {
		return convertTime(earliest, windowStart.toLocalDate(1).toDateTimeAtStartOfDay(DateTimeZone.UTC));
	}

	public StepwiseIntegerCurve createCurveForDoubleIndex(final Index<Double> index, final double scale) {
		final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();

		curve.setDefaultValue(0);

		boolean gotOneEarlyDate = false;
		for (final YearMonth date : index.getDates()) {
			final double value = index.getValueForMonth(date);
			final int hours = convertTime(date);
			if (hours < 0) {
				if (gotOneEarlyDate)
					continue;
				gotOneEarlyDate = true;
			}
			curve.setValueAfter(hours, OptimiserUnitConvertor.convertToInternalPrice(scale * value));
		}

		return curve;
	}

	public StepwiseIntegerCurve createCurveForIntegerIndex(final Index<Integer> index, final double scale, final boolean smallNumber) {
		final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();

		curve.setDefaultValue(0);

		boolean gotOneEarlyDate = false;
		for (final YearMonth date : index.getDates()) {
			final int value = index.getValueForMonth(date);
			final int hours = convertTime(date);
			if (hours < 0) {
				if (gotOneEarlyDate) {
					// TODO: While we should skip all the early stuff, we need to keep the latest, this currently however takes the earliest value!
					// continue;
				}
				gotOneEarlyDate = true;
			}
			final double scaledValue = (double) value * scale;
			int internalValue;//
			if (smallNumber) {
				internalValue = OptimiserUnitConvertor.convertToInternalPrice(scaledValue);
			} else {
				internalValue = OptimiserUnitConvertor.convertToInternalDailyRate(scaledValue);
			}
			curve.setValueAfter(hours, internalValue);
		}

		return curve;
	}

	public StepwiseIntegerCurve generateExpressionCurve(final String priceExpression, final SeriesParser indices) {

		if (priceExpression == null || priceExpression.isEmpty()) {
			return null;
		}

		final IExpression<ISeries> expression = indices.parse(priceExpression);
		final ISeries parsed = expression.evaluate();

		final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();
		if (parsed.getChangePoints().length == 0) {
			curve.setDefaultValue(OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(0).doubleValue()));
		} else {

			curve.setDefaultValue(0);
			for (final int i : parsed.getChangePoints()) {
				curve.setValueAfter(i, OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(i).doubleValue()));
			}
		}
		return curve;
	}

	public StepwiseIntegerCurve generateFixedCostExpressionCurve(final String priceExpression, final SeriesParser indices) {

		if (priceExpression == null || priceExpression.isEmpty()) {
			return null;
		}

		final IExpression<ISeries> expression = indices.parse(priceExpression);
		final ISeries parsed = expression.evaluate();

		final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();
		if (parsed.getChangePoints().length == 0) {
			curve.setDefaultValue((int) OptimiserUnitConvertor.convertToInternalFixedCost(parsed.evaluate(0).intValue()));
		} else {

			curve.setDefaultValue(0);
			for (final int i : parsed.getChangePoints()) {
				curve.setValueAfter(i, (int) OptimiserUnitConvertor.convertToInternalFixedCost(parsed.evaluate(i).intValue()));
			}
		}
		return curve;
	}

	public int convertTime(final YearMonth time) {
		return convertTime(time.toLocalDate(1).toDateTimeAtStartOfDay(DateTimeZone.UTC));
	}

	public int convertTime(final LocalDate time) {
		return convertTime(time.toDateTimeAtStartOfDay(DateTimeZone.UTC));
	}

	public int convertTime(final DateTime startTime) {
		assert earliestTime != null;
		return convertTime(earliestTime, startTime);
	}

	public DateTime getEarliestTime() {
		return earliestTime;
	}

	public void setEarliestTime(final DateTime earliestTime) {
		this.earliestTime = roundTimeDown(earliestTime);
	}

	/**
	 * Returns the minutes that need to be added to a date that has been rounded down elsewhere in the application (e.g. in convertTime())
	 * 
	 * @param timeZone
	 * @return
	 */
	public static int getOffsetInMinutesFromTimeZone(final String timeZone) {
		return getOffsetMinutes(getOffsetInMsFromTimeZone(timeZone));
	}

	public static int getOffsetInMsFromTimeZone(final String timeZone) {
		final int offset = TimeZone.getTimeZone(timeZone).getRawOffset();
		return offset;
	}

	public static int getOffsetMinutes(final int offsetMs) {
		int correctedOffset = 0;
		final int offsetInMinutes = (Math.abs(offsetMs) / 1000 / 60) % 60;
		if (offsetInMinutes != 0) {
			if (offsetMs > 0) {
				correctedOffset = 60 - offsetInMinutes;
			} else {
				correctedOffset = offsetInMinutes;
			}
		}
		return correctedOffset;
	}

	/**
	 * Rounds time down by losing the hour information
	 * 
	 * @return
	 */
	public static DateTime roundTimeDown(final DateTime date) {
		return date.minusMinutes(getHourRoundingRemainder(date));
	}

	/**
	 * Returns the number of milliseconds lost by rounding down to the nearest hour
	 * 
	 * @param date
	 * @return
	 */
	public static int getHourRoundingRemainder(final DateTime date) {
		return date.withZone(DateTimeZone.UTC).getMinuteOfHour();
	}

	public static DateTime createDate(final int year, final int month, final int dayOfMonth, final int hourOfDay, final String newTimeZone) {
		return new DateTime(year, month, dayOfMonth, hourOfDay, 0, DateTimeZone.forID(newTimeZone));
	}

	public static YearMonth createYearMonth(final int year, final int month) {
		return new YearMonth(year, month);
	}
}
