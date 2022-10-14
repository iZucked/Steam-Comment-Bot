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
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.ILazyCurve;
import com.mmxlabs.common.curves.IParameterisedCurve;
import com.mmxlabs.common.curves.LazyStepwiseIntegerCurve;
import com.mmxlabs.common.curves.ParameterisedIntegerCurve;
import com.mmxlabs.common.curves.PreGeneratedIntegerCurve;
import com.mmxlabs.common.curves.PreGeneratedLongCurve;
import com.mmxlabs.common.curves.WrappedParameterisedCurve;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.parser.series.SeriesUtil;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
import com.mmxlabs.models.lng.pricing.YearMonthPointContainer;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
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

	private final Pair<ZonedDateTime, ZonedDateTime> earliestAndLatestTimes;

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

	public ParameterisedIntegerCurve generateParameterisedExpressionCurve(final ISeries series) {

		return new ParameterisedIntegerCurve(params -> {
			if (series.getChangePoints().length == 0) {
				final int v = OptimiserUnitConvertor.convertToInternalPrice(series.evaluate(0, params).doubleValue());
				return Pair.of(v, new TreeMap<>());
			} else {
				final TreeMap<Integer, Integer> intervals = new TreeMap<>();
				for (final int i : series.getChangePoints()) {
					intervals.put(i, OptimiserUnitConvertor.convertToInternalPrice(series.evaluate(i, params).doubleValue()));
				}
				return Pair.of(0, intervals);
			}
		}, series.getParameters());
	}

	public PreGeneratedIntegerCurve generateExpressionCurve(final ISeries series) {

		if (series.isParameterised()) {
			throw new IllegalArgumentException();
		}

		final PreGeneratedIntegerCurve curve = new PreGeneratedIntegerCurve();
		if (series.getChangePoints().length == 0) {
			curve.setDefaultValue(OptimiserUnitConvertor.convertToInternalPrice(series.evaluate(0, Collections.emptyMap()).doubleValue()));
		} else {
			curve.setDefaultValue(0);
			for (final int i : series.getChangePoints()) {
				curve.setValueAfter(i, OptimiserUnitConvertor.convertToInternalPrice(series.evaluate(i, Collections.emptyMap()).doubleValue()));
			}
		}
		return curve;
	}

	public @Nullable PreGeneratedIntegerCurve generateExpressionCurve(final @Nullable String priceExpression, final SeriesParser seriesParser) {

		if (priceExpression == null || priceExpression.isEmpty()) {
			return null;
		}

		final ISeries parsed = seriesParser.asSeries(priceExpression);
		return generateExpressionCurve(parsed);
	}

	public @Nullable ParameterisedIntegerCurve generateParameterisedExpressionCurve(final @Nullable String priceExpression, final SeriesParser seriesParser) {

		if (priceExpression == null || priceExpression.isEmpty()) {
			return null;
		}

		final ISeries parsed = seriesParser.asSeries(priceExpression);
		return generateParameterisedExpressionCurve(parsed);
	}

	public @Nullable PreGeneratedLongCurve generateLongExpressionCurve(final @Nullable String priceExpression, final @NonNull SeriesParser seriesParser) {

		if (priceExpression == null || priceExpression.isEmpty()) {
			return null;
		}

		final ISeries parsed = seriesParser.asSeries(priceExpression);

		return generateLongExpressionCurve(parsed);
	}

	public PreGeneratedLongCurve generateLongExpressionCurve(final ISeries seried) {

		if (seried.isParameterised()) {
			throw new IllegalArgumentException();
		}

		final PreGeneratedLongCurve curve;
		if (seried.getChangePoints().length == 0) {
			curve = new PreGeneratedLongCurve();
			curve.setDefaultValue(OptimiserUnitConvertor.convertToInternalDailyCost(seried.evaluate(0, Collections.emptyMap()).doubleValue()));
		} else {
			curve = new PreGeneratedLongCurve();
			for (final int i : seried.getChangePoints()) {
				curve.setValueAfter(i, OptimiserUnitConvertor.convertToInternalDailyCost(seried.evaluate(i, Collections.emptyMap()).doubleValue()));
			}
			curve.setDefaultValue(0L);
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

	public @Nullable Pair<IParameterisedCurve, IIntegerIntervalCurve> createCurveAndIntervals(final SeriesParser seriesParser, final String priceExpression) {

		return createCurveAndIntervals(seriesParser, priceExpression, this::generateParameterisedExpressionCurve);
	}

	public @NonNull Pair<ICurve, IIntegerIntervalCurve> createConstantCurveAndIntervals(final int constant) {

		return Pair.of(new ConstantValueCurve(constant), monthIntervalsInHoursCurve);
	}

	public @NonNull Pair<IParameterisedCurve, IIntegerIntervalCurve> createConstantParameterisedCurveAndIntervals(final int constant) {

		return Pair.of(new WrappedParameterisedCurve(new ConstantValueCurve(constant)), monthIntervalsInHoursCurve);
	}

	public @Nullable Pair<IParameterisedCurve, IIntegerIntervalCurve> createCurveAndIntervals(final SeriesParser seriesParser, final String priceExpression,
			final Function<ISeries, IParameterisedCurve> curveFactory) {

		if (priceExpression == null || priceExpression.isEmpty()) {
			return null;
		}

		final IIntegerIntervalCurve priceIntervals;
		final IParameterisedCurve curve;

		final IExpression<ISeries> expression = seriesParser.asIExpression(priceExpression);

		if (expression.canEvaluate()) {
			final ISeries parsed = expression.evaluate();

			if (parsed.getChangePoints().length == 0) {
				priceIntervals = monthIntervalsInHoursCurve;
			} else {
				priceIntervals = getIntegerIntervalCurveForChangePoints(parsed.getChangePoints());
			}

			curve = curveFactory.apply(parsed);
		} else {
			final LazyIntegerIntervalCurve lazyIntervalCurve = new LazyIntegerIntervalCurve(monthIntervalsInHoursCurve, parsed -> getIntegerIntervalCurveForChangePoints(parsed.getChangePoints()));
			priceIntervals = lazyIntervalCurve;

			final ILazyCurve lazyCurve = new LazyStepwiseIntegerCurve(expression, curveFactory, lazyIntervalCurve::initialise);
			curve = lazyCurve;
			lazyExpressionMangerEditor.addLazyCurve(lazyCurve);
			lazyExpressionMangerEditor.addLazyIntervalCurve(lazyIntervalCurve);
		}
		return Pair.of(curve, priceIntervals);
	}

	public IParameterisedCurve generateShiftedCurve(final ISeries series, final UnaryOperator<ZonedDateTime> dateShifter) {

		return new ParameterisedIntegerCurve(params -> {
			if (series.getChangePoints().length == 0) {
				final int v = OptimiserUnitConvertor.convertToInternalPrice(series.evaluate(0, params).doubleValue());
				return Pair.of(v, new TreeMap<>());
			} else {
				final TreeMap<Integer, Integer> intervals = new TreeMap<>();
				for (final int i : series.getChangePoints()) {
					ZonedDateTime date = getDateFromHours(i, "UTC");
					date = dateShifter.apply(date);
					final int time = convertTime(date);
					intervals.put(time, OptimiserUnitConvertor.convertToInternalPrice(series.evaluate(i, params).doubleValue()));
				}
				return Pair.of(0, intervals);
			}
		}, series.getParameters());
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
	public static IIntegerIntervalCurve getIntegerIntervalCurveForChangePoints(final int[] changePoints) {
		final IIntegerIntervalCurve intervals = new IntegerIntervalCurve();
		intervals.addAll(Arrays.stream(changePoints).boxed().toList());
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

	public PreGeneratedIntegerCurve constructConcreteCurve(final YearMonthPointContainer ymPointContainer) {

		final PreGeneratedIntegerCurve curve = new PreGeneratedIntegerCurve();
		curve.setDefaultValue(0);

		for (final YearMonthPoint pt : ymPointContainer.getPoints()) {

			final int time = convertTime(pt.getDate());
			final int value = OptimiserUnitConvertor.convertToInternalPrice(pt.getValue());
			curve.setValueAfter(time, value);
		}
		return curve;
	}

	public PreGeneratedLongCurve constructConcreteLongCurve(final YearMonthPointContainer ymPointContainer) {

		final PreGeneratedLongCurve curve = new PreGeneratedLongCurve();
		curve.setDefaultValue(0L);

		for (final YearMonthPoint pt : ymPointContainer.getPoints()) {

			final int time = convertTime(pt.getDate());
			final long value = OptimiserUnitConvertor.convertToInternalDailyCost(pt.getValue());
			curve.setValueAfter(time, value);
		}
		return curve;
	}

	public ISeries constructConcreteSeries(final YearMonthPointContainer ymPointContainer) {
		final SortedSet<Pair<YearMonth, Number>> vals = new TreeSet<>((o1, o2) -> o1.getFirst().compareTo(o2.getFirst()));
		for (final YearMonthPoint pt : ymPointContainer.getPoints()) {
			vals.add(new Pair<>(pt.getDate(), pt.getValue()));
		}
		final int[] times = new int[vals.size()];
		final Number[] nums = new Number[vals.size()];
		int k = 0;
		for (final Pair<YearMonth, Number> e : vals) {
			times[k] = convertTime(e.getFirst());
			nums[k++] = e.getSecond();
		}
		return new ISeries() {

			public boolean isParameterised() {
				return false;
			}

			public Set<String> getParameters() {
				return Collections.emptySet();
			}

			@Override
			public int[] getChangePoints() {
				return times;
			}

			@Override
			public Number evaluate(final int point, final Map<String, String> params) {
				final int pos = SeriesUtil.floor(times, point);
				if (pos == -1) {
					return 0;
				}
				return nums.length == 0 ? 0 : nums[pos];
			}
		};
	}
}
