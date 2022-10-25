/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.ILazyCurve;
import com.mmxlabs.common.curves.LazyStepwiseIntegerCurve;
import com.mmxlabs.common.curves.PreGeneratedIntegerCurve;
import com.mmxlabs.common.curves.PreGeneratedLongCurve;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.astnodes.ASTNode;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.builder.IBuilderExtension;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.curves.IntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.curves.LazyIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.providers.IInternalDateProvider;
import com.mmxlabs.scheduler.optimiser.providers.ILazyExpressionManagerEditor;

/**
 * Small helper class which is intended to be injected into external
 * {@link ITransformerExtension}s and {@link IBuilderExtension}s to help with
 * date and time conversion. This also has some routines for creating
 * {@link ICurve}s
 * 
 */
public class DateAndCurveHelper implements IInternalDateProvider {

	private final @NonNull ZonedDateTime earliestTime;

	private final @NonNull ZonedDateTime latestTime;

	private Pair<ZonedDateTime, ZonedDateTime> earliestAndLatestTimes;

	@Inject
	@Named(LNGTransformerModule.MONTH_ALIGNED_INTEGER_INTERVAL_CURVE)
	private IIntegerIntervalCurve monthIntervalsInHoursCurve;

	@Inject
	private ILazyExpressionManagerEditor lazyExpressionMangerEditor;

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

		earliestAndLatestTimes = Pair.of(earliestTime, latestTime);
	}

	/**
	 * Convert a date into relative hours; returns the number of hours between
	 * windowStart and earliest.
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

	/**
	 * Please keep in sync with DefaultTransferModelDataProviderEditor#generateExpressionCurve (FM)
	 * @param priceExpression
	 * @param seriesParser
	 * @return
	 */
	public @Nullable PreGeneratedIntegerCurve generateExpressionCurve(final @Nullable String priceExpression, final SeriesParser parser) {

		if (priceExpression == null || priceExpression.isBlank()) {
			return null;
		}

		final ISeries parsed = parser.asSeries(priceExpression);

		final PreGeneratedIntegerCurve curve = new PreGeneratedIntegerCurve();
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

	public @Nullable PreGeneratedLongCurve generateLongExpressionCurve(final @Nullable String priceExpression, final @NonNull SeriesParser seriesParser) {

		if (priceExpression == null || priceExpression.isBlank()) {
			return null;
		}

		final ISeries parsed = seriesParser.asSeries(priceExpression);

		final PreGeneratedLongCurve curve = new PreGeneratedLongCurve();
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
	 * Returns the minutes that need to be added to a date that has been rounded
	 * down elsewhere in the application (e.g. in convertTime())
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

	public Pair<ZonedDateTime, ZonedDateTime> getEarliestAndLatestTimes() {
		return earliestAndLatestTimes;
	}

	public @Nullable Pair<ICurve, IIntegerIntervalCurve> createCurveAndIntervals(SeriesParser seriesParser, final String priceExpression) {

		return createCurveAndIntervals(seriesParser, priceExpression, this::generateExpressionCurve);
	}

	public @NonNull Pair<ICurve, IIntegerIntervalCurve> createConstantCurveAndIntervals(int constant) {

		return Pair.of(new ConstantValueCurve(constant), monthIntervalsInHoursCurve);
	}

	public @Nullable Pair<ICurve, IIntegerIntervalCurve> createCurveAndIntervals(SeriesParser seriesParser, final String priceExpression, final Function<ISeries, ICurve> curveFactory) {

		if (priceExpression == null || priceExpression.isBlank()) {
			return null;
		}

		final IIntegerIntervalCurve priceIntervals;
		final ICurve curve;

		final IExpression<ISeries> expression = seriesParser.asIExpression(priceExpression);

		if (expression.canEvaluate()) {
			final ISeries parsed = expression.evaluate();

			if (parsed.getChangePoints().length == 0) {
				priceIntervals = monthIntervalsInHoursCurve;
			} else {
				priceIntervals = getSplitMonthDatesForChangePoint(parsed.getChangePoints());
			}

			curve = curveFactory.apply(parsed);
		} else {
			final LazyIntegerIntervalCurve lazyIntervalCurve = new LazyIntegerIntervalCurve(monthIntervalsInHoursCurve, parsed -> getSplitMonthDatesForChangePoint(parsed.getChangePoints()));
			priceIntervals = lazyIntervalCurve;

			final ILazyCurve lazyCurve = new LazyStepwiseIntegerCurve(expression, curveFactory, lazyIntervalCurve::initialise);
			curve = lazyCurve;
			lazyExpressionMangerEditor.addLazyCurve(lazyCurve);
			lazyExpressionMangerEditor.addLazyIntervalCurve(lazyIntervalCurve);
		}
		return Pair.of(curve, priceIntervals);
	}

	public ICurve generateShiftedCurve(final ISeries series, final UnaryOperator<ZonedDateTime> dateShifter) {
		final PreGeneratedIntegerCurve curve = new PreGeneratedIntegerCurve();
		if (series.getChangePoints().length == 0) {
			curve.setDefaultValue(OptimiserUnitConvertor.convertToInternalPrice(series.evaluate(0).doubleValue()));
		} else {
			curve.setDefaultValue(0);
			for (final int i : series.getChangePoints()) {
				ZonedDateTime date = getDateFromHours(i, "UTC");
				date = dateShifter.apply(date);
				final int time = convertTime(date);
				curve.setValueAfter(time, OptimiserUnitConvertor.convertToInternalPrice(series.evaluate(i).doubleValue()));
			}
		}
		return curve;
	}

	public ICurve generateExpressionCurve(final ISeries parsed) {

		final PreGeneratedIntegerCurve curve = new PreGeneratedIntegerCurve();
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

	@SuppressWarnings("null")
	@NonNull
	public ZonedDateTime getDateFromHours(final int hours, final String tz) {
		final String timeZone = (tz == null) ? "UTC" : tz;
		final ZonedDateTime rawDate = getEarliestTime().withZoneSameInstant(ZoneId.of(timeZone)).plusHours(hours);
		final int offsetMinutes = DateAndCurveHelper.getOffsetInMinutesFromDate(rawDate);
		return rawDate.plusMinutes(offsetMinutes);
	}

	@NonNull
	public IIntegerIntervalCurve getMonthAlignedIntegerIntervalCurve(final int start, final int end, final int offsetInHours) {
		final IIntegerIntervalCurve intervals = new IntegerIntervalCurve();
		int proposed = start;
		while (proposed < end) {
			proposed = getNextMonth(proposed);
			intervals.add(proposed + offsetInHours);
		}
		intervals.add(end);
		return intervals;
	}

	@NonNull
	public static IIntegerIntervalCurve getSplitMonthDatesForChangePoint(final int[] changePoints) {
		final IIntegerIntervalCurve intervals = new IntegerIntervalCurve();
		intervals.addAll(Arrays.stream(changePoints).boxed().collect(Collectors.toList()));
		return intervals;
	}

	/**
	 * Get the date in hours of the first day of the next month
	 * 
	 * @param hours
	 * @return
	 */
	@SuppressWarnings("null")
	public int getNextMonth(final int hours) {
		ZonedDateTime asDate = getDateFromHours(hours, "UTC");
		asDate = asDate.plusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
		return convertTime(asDate);
	}

	/**
	 * Get the date in hours of the first day of the previous month
	 * 
	 * @param hours
	 * @return
	 */
	@SuppressWarnings("null")
	public int getPreviousMonth(final int hours) {
		ZonedDateTime asDate = getDateFromHours(hours, "UTC");
		asDate = asDate.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
		return convertTime(asDate);
	}

	public int getNextOrEqualDay(final int hours) {
		@NonNull
		ZonedDateTime date = getDateFromHours(hours, "UTC");
		if (!isAtStartOfDay(date)) {
			date = date.plusDays(1L).withHour(0).truncatedTo(ChronoUnit.DAYS);
		}
		return convertTime(date);
	}

	/*
	 * Note: this method doesn't inspect the timezone
	 */
	private boolean isAtStartOfDay(final ZonedDateTime date) {
		return date.getHour() == 0 && date.getMinute() == 0 && date.getSecond() == 0 && date.getNano() == 0;
	}
}
