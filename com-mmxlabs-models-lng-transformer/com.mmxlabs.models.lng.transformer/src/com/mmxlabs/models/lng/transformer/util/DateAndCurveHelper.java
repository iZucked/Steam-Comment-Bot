/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.management.timer.Timer;

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
 * @since 2.0
 */
public class DateAndCurveHelper {

	private TimeZone timezone;
	private Date earliestTime;

	public DateAndCurveHelper() {
		this.timezone = TimeZone.getTimeZone("UTC");

	}

	/**
	 * Convert a date into relative hours; returns the number of hours between windowStart and earliest.
	 * 
	 * @param earliest
	 * @param windowStart
	 * @return number of hours between earliest and windowStart
	 */
	public int convertTime(final Date earliest, final Date windowStart) {
		// I am using two calendars, because the java date objects are all
		// deprecated; however, timezones should not be a problem because
		// every Date in the EMF representation is in UTC.
		final Calendar a = Calendar.getInstance(timezone);
		a.setTime(earliest);
		final Calendar b = Calendar.getInstance(timezone);
		b.setTime(windowStart);
		final long difference = b.getTimeInMillis() - a.getTimeInMillis();
		return (int) (difference / Timer.ONE_HOUR);
	}

	public StepwiseIntegerCurve createCurveForDoubleIndex(final Index<Double> index, final double scale) {
		final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();

		curve.setDefaultValue(0);

		boolean gotOneEarlyDate = false;
		for (final Date date : index.getDates()) {
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

	public StepwiseIntegerCurve createCurveForIntegerIndex(final Index<Integer> index, final double scale, boolean smallNumber) {
		final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();

		curve.setDefaultValue(0);

		boolean gotOneEarlyDate = false;
		for (final Date date : index.getDates()) {
			final int value = index.getValueForMonth(date);
			final int hours = convertTime(date);
			if (hours < 0) {
				if (gotOneEarlyDate) {
					// TODO: While we should skip all the early stuff, we need to keep the latest, this currently however takes the earliest value!
					// continue;
				}
				gotOneEarlyDate = true;
			}
			double scaledValue = (double) value * scale;
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

	public StepwiseIntegerCurve generateExpressionCurve(final String priceExpression, SeriesParser indices) {

		if (priceExpression == null || priceExpression.isEmpty()) {
			return new StepwiseIntegerCurve();
		}

		final IExpression<ISeries> expression = indices.parse(priceExpression);
		final ISeries parsed = expression.evaluate();

		final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();
		if (parsed.getChangePoints().length == 0) {
			curve.setDefaultValue(OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(0).doubleValue()));
		} else {

			curve.setDefaultValue(0);
			for (final int i : parsed.getChangePoints()) {
				curve.setValueAfter(i - 1, OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(i).doubleValue()));
			}
		}
		return curve;
	}
	
	public int convertTime(final Date startTime) {
		assert earliestTime != null;
		return convertTime(earliestTime, startTime);
	}

	public Date getEarliestTime() {
		return earliestTime;
	}

	public void setEarliestTime(Date earliestTime) {
		this.earliestTime = earliestTime;
	}
}
