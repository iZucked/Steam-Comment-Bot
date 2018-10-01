/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.common.curves.StepwiseLongCurve;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PortsSplitExpressionMap;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.builder.IBuilderExtension;
import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * Small helper class which is intended to be injected into external {@link ITransformerExtension}s and {@link IBuilderExtension}s to help with date and time conversion. This also has some routines
 * for creating {@link ICurve}s
 * 
 */
public class DateAndCurveHelper {

	@NonNull
	private final ZonedDateTime earliestTime;

	@NonNull
	private final ZonedDateTime latestTime;

	@SuppressWarnings("null")
	@Inject
	public DateAndCurveHelper(@Named(LNGTransformerModule.EARLIEST_AND_LATEST_TIMES) @NonNull final Pair<ZonedDateTime, ZonedDateTime> earliestAndLatestTime) {
		this(earliestAndLatestTime.getFirst(), earliestAndLatestTime.getSecond());
	}

	@SuppressWarnings("null")
	public DateAndCurveHelper(@NonNull final ZonedDateTime earliest, @NonNull final ZonedDateTime latest) {

		this.earliestTime = earliest.withZoneSameInstant(ZoneId.of("UTC")).truncatedTo(ChronoUnit.HOURS);
		assert !earliestTime.isAfter(earliest);
		this.latestTime = latest;
	}

	/**
	 * Convert a date into relative hours; returns the number of hours between windowStart and earliest.
	 * 
	 * @param earliest
	 * @param windowStart
	 * @return number of hours between earliest and windowStart
	 */
	public int convertTime(@NonNull final ZonedDateTime earliest, @NonNull final ZonedDateTime windowStart) {
		return Hours.between(earliest, windowStart);
	}

	public int convertTime(@NonNull final ZonedDateTime earliest, @NonNull final YearMonth windowStart) {
		return convertTime(earliest, yearMonthToDateTime(windowStart));
	}

	@NonNull
	protected ZonedDateTime yearMonthToDateTime(@NonNull final YearMonth windowStart) {
		return ZonedDateTime.of(windowStart.getYear(), windowStart.getMonthValue(), 1, 0, 0, 0, 0, ZoneId.of("UTC"));
	}

	public StepwiseIntegerCurve createCurveForDoubleIndex(final Index<Double> index, final double scale) {
		final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();

		curve.setDefaultValue(0);

		boolean gotOneEarlyDate = false;
		for (final YearMonth date : index.getDates()) {
			assert date != null;
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
			assert date != null;
			final int value = index.getValueForMonth(date);
			final int hours = convertTime(date);

			if (hours < 0) {
				if (gotOneEarlyDate) {
					// TODO: While we should skip all the early stuff, we need to keep the latest,
					// this currently however takes the earliest value!
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

	public ICurve generateExpressionCurveWithShift(final String priceExpression, final @NonNull SeriesParser indices, int days) {
		ICurve nonShiftedCurve = generateExpressionCurve(priceExpression, indices);
		final ICurve shiftedCurve = new ICurve() {
			@Override
			public int getValueAtPoint(final int point) {
				return nonShiftedCurve.getValueAtPoint(point + days * 24);
			}
		};
		return shiftedCurve;
	}

	public Map<IPort, ICurve> generateSplitExpressionCurve(final Collection<PortsSplitExpressionMap> entries, int dayOfMonth, @NonNull SeriesParser parser,
			final @NonNull ModelEntityMap modelEntityMap) {

		Map<IPort, ICurve> splitExpressionMap = new HashMap<>();

		if (entries != null) {
			for (final PortsSplitExpressionMap entry : entries) {
				final ICurve curve = createSplitMonthCurve(modelEntityMap, parser, entry.getExpression1(), entry.getExpression2(), dayOfMonth);
				for (final Port p : SetUtils.getObjects(entry.getPorts())) {
					assert p != null;
					splitExpressionMap.put(modelEntityMap.getOptimiserObjectNullChecked(p, IPort.class), curve);
				}
			}
		}
		return splitExpressionMap;
	}

	public @Nullable StepwiseIntegerCurve generateExpressionCurve(final String priceExpression, final SeriesParser indices) {

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

	@Nullable
	public StepwiseLongCurve generateLongExpressionCurve(final @Nullable String priceExpression, final @NonNull SeriesParser seriesParser) {

		if (priceExpression == null || priceExpression.isEmpty()) {
			return null;
		}

		final IExpression<ISeries> expression = seriesParser.parse(priceExpression);
		final ISeries parsed = expression.evaluate();

		final StepwiseLongCurve curve = new StepwiseLongCurve();
		if (parsed.getChangePoints().length == 0) {
			curve.setDefaultValue(Math.round(parsed.evaluate(0).doubleValue() * (double) Calculator.ScaleFactor));
		} else {

			curve.setDefaultValue(0L);
			for (final int i : parsed.getChangePoints()) {
				curve.setValueAfter(i, Math.round(parsed.evaluate(i).doubleValue() * (double) Calculator.ScaleFactor));
			}
		}
		return curve;
	}

	public int convertTime(@NonNull final YearMonth time) {
		return convertTime(yearMonthToDateTime(time));
	}

	public int convertTime(@NonNull final LocalDate time) {
		return convertTime(time.atStartOfDay(ZoneId.of("UTC")));
	}

	public int convertTime(@NonNull final ZonedDateTime startTime) {
		assert earliestTime != null;
		return convertTime(earliestTime, startTime);
	}

	@NonNull
	public ZonedDateTime getEarliestTime() {
		return earliestTime;
	}

	@NonNull
	public ZonedDateTime getLatestTime() {
		return latestTime;
	}

	/**
	 * Returns the minutes that need to be added to a date that has been rounded down elsewhere in the application (e.g. in convertTime())
	 * 
	 * @param timeZone
	 * @return
	 */
	public static int getOffsetInMinutesFromDate(final ZonedDateTime date) {
		return getOffsetMinutes(getOffsetInMsFromDate(date));
	}

	private static int getOffsetInMsFromDate(final ZonedDateTime date) {
		return date.getOffset().getTotalSeconds() * 1000;
	}

	private static int getOffsetMinutes(final int offsetMs) {
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
	 * Returns the number of milliseconds lost by rounding down to the nearest hour
	 * 
	 * @param date
	 * @return
	 */
	public static int getHourRoundingRemainder(@NonNull final ZonedDateTime date) {
		return date.withZoneSameInstant(ZoneId.of("UTC")).getMinute();
	}

	public static ZonedDateTime createDate(final int year, final int month, final int dayOfMonth, final int hourOfDay, final String newTimeZone) {
		return ZonedDateTime.of(year, month, dayOfMonth, hourOfDay, 0, 0, 0, ZoneId.of(newTimeZone));
	}

	public static YearMonth createYearMonth(final int year, final int month) {
		return YearMonth.of(year, month);
	}

	@NonNull
	public StepwiseIntegerCurve createDateShiftCurve(int dayOfMonth) {
		// Create a curve, shifting the current date forwards or backwards depending on
		// whether or not it is on or after the dayOfMonth.
		final StepwiseIntegerCurve dateShiftCurve = new StepwiseIntegerCurve();
		{
			ZonedDateTime currentDate = getEarliestTime();

			// Find the previous dayOfMonth
			if (currentDate.getDayOfMonth() < dayOfMonth) {
				currentDate = currentDate.minusMonths(1);
			}
			currentDate = currentDate.withDayOfMonth(dayOfMonth);
			// Ensure midnight
			currentDate = currentDate.withHour(0);

			// Hmm, not sure how to get to the true latest date - perhaps better as a lazy
			// treemap?
			final ZonedDateTime end = getLatestTime();
			while (currentDate.isBefore(end)) {

				// We want to move anything after dayOfMonth to start of the next month
				final int time = convertTime(currentDate);
				// Set to start of current month *then* shift to the next month
				final ZonedDateTime c = currentDate.withDayOfMonth(1).plusMonths(1);

				final int startOfMonth = convertTime(c);
				dateShiftCurve.setValueAfter(time, startOfMonth);

				currentDate = currentDate.plusMonths(1);
			}
		}
		return dateShiftCurve;
	}

	public ICurve createSplitMonthCurve(final @NonNull ModelEntityMap modelEntityMap, @NonNull SeriesParser parser, final String expression1, final String expression2, final int dayOfMonth) {

		final ICurve o_curve1 = generateExpressionCurve(expression1, parser);
		final ICurve o_curve2 = generateExpressionCurve(expression2, parser);

		final ZonedDateTime earliestTime = getEarliestTime();
		final ZonedDateTime earliestTimeUTC = earliestTime.withZoneSameInstant(ZoneId.of("UTC"));

		final ZonedDateTime latestTime = getLatestTime();
		final ZonedDateTime latestTimeUTC = latestTime.withZoneSameInstant(ZoneId.of("UTC"));

		LocalDate start = earliestTimeUTC.toLocalDate().withDayOfMonth(1);
		final LocalDate end = latestTimeUTC.toLocalDate();

		final List<Pair<LocalDate, Integer>> vals = new LinkedList<>();
		final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();
		curve.setDefaultValue(0);
		while (start.isBefore(end)) {
			int month_1st = convertTime(YearMonth.from(start));
			curve.setValueAfter(month_1st, o_curve1.getValueAtPoint(month_1st));
			curve.setValueAfter(convertTime(start.withDayOfMonth(dayOfMonth)), o_curve2.getValueAtPoint(month_1st));

			start = start.plusMonths(1);
		}
		return curve;
	}

}
