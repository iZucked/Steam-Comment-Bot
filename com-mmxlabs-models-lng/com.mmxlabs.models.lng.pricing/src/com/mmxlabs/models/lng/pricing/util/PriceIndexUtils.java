/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.UnaryOperator;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.series.CalendarMonthMapper;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.parser.series.SeriesParserData;
import com.mmxlabs.common.parser.series.SeriesType;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.common.time.Months;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.BunkerFuelCurve;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.CurrencyCurve;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;

/**
 * 
 * @author Simon McGregor
 * 
 *         Utility class to provide methods for evaluating index curves
 * 
 */
public class PriceIndexUtils {
	public static final @NonNull YearMonth dateZero = YearMonth.of(2000, 1);
	public static final @NonNull LocalDateTime dateTimeZero = LocalDateTime.of(dateZero.getYear(), dateZero.getMonthValue(), 1, 0, 0);

	public enum PriceIndexType {
		COMMODITY, CHARTER, BUNKERS, CURRENCY;
	}

	/**
	 * Provides a {@link SeriesParser} object based on the default activator (the one returned by {@link Activator.getDefault()}).
	 * 
	 * @return A {@link SeriesParser} object for use in validating price expressions.
	 */
	private static @NonNull SeriesParser getParserFor(final @NonNull PricingModel pricingModel, final @NonNull EReference reference) {
		SeriesParserData seriesParserData = new SeriesParserData();

		seriesParserData.setShiftMapper((date, shift) -> {
			// Get input as local date time and shift.
			@NonNull
			final LocalDateTime plusMonths = dateTimeZero.plusHours(date).minusMonths(shift);
			// Convert back to internal time units.
			return Hours.between(dateTimeZero, plusMonths);
		});

		seriesParserData.setCalendarMonthMapper(new CalendarMonthMapper() {

			@Override
			public int mapMonthToChangePoint(int months) {

				final LocalDateTime startOfYear = dateTimeZero.withDayOfYear(1);
				@NonNull
				final LocalDateTime plusMonths = startOfYear.plusMonths(months);

				return Hours.between(dateTimeZero, plusMonths);
			}

			@Override
			public int mapChangePointToMonth(int date) {

				final LocalDateTime startOfYear = dateTimeZero.withDayOfYear(1);
				@NonNull
				final LocalDateTime plusMonths = dateTimeZero.plusHours(date).withDayOfMonth(1);

				int m = Months.between(startOfYear, plusMonths);

				int a = startOfYear.getMonthValue();
				int b = plusMonths.getMonthValue();

				return m;
			}

			@Override
			public int mapTimePoint(int point, UnaryOperator<LocalDateTime> mapFunction) {

				final LocalDateTime ldt = dateTimeZero.plusHours(point);
				final LocalDateTime mappedTime = mapFunction.apply(ldt);

				return Hours.between(dateTimeZero, mappedTime);
			}
		});
		final SeriesParser indices = new SeriesParser(seriesParserData);

		{
			final List<AbstractYearMonthCurve> curves = (List<AbstractYearMonthCurve>) pricingModel.eGet(reference);
			for (final AbstractYearMonthCurve curve : curves) {
				SeriesType seriesType = null;
				if (curve instanceof CommodityCurve) {
					seriesType = SeriesType.COMMODITY;
				} else if (curve instanceof CharterCurve) {
					seriesType = SeriesType.CHARTER;
				} else if (curve instanceof BunkerFuelCurve) {
					seriesType = SeriesType.BUNKERS;
				} else if (curve instanceof CurrencyCurve) {
					seriesType = SeriesType.CURRENCY;
				} else {
					throw new IllegalStateException();
				}
				addCurveData(curve, seriesType, indices);
			}
		}
		if (reference == PricingPackage.Literals.PRICING_MODEL__COMMODITY_CURVES) {
			for (final AbstractYearMonthCurve curve : pricingModel.getFormulaeCurves()) {
				addCurveData(curve, SeriesType.COMMODITY, indices);
			} 
		}
		// Add in currency curves
		if (reference != PricingPackage.Literals.PRICING_MODEL__CURRENCY_CURVES) {
			final List<CurrencyCurve> curves = pricingModel.getCurrencyCurves();
			for (final AbstractYearMonthCurve curve : curves) {
				addCurveData(curve, SeriesType.CURRENCY, indices);
			}
		}

		for (final UnitConversion factor : pricingModel.getConversionFactors()) {
			final String name = createConversionFactorName(factor);
			if (name != null) {
				indices.addSeriesExpression(name, SeriesType.CONVERSION, Double.toString(factor.getFactor()));
			}
			final String reverseName = createReverseConversionFactorName(factor);
			if (reverseName != null) {
				indices.addSeriesExpression(reverseName, SeriesType.CONVERSION, Double.toString(1.0 / factor.getFactor()));
			}
		}

		return indices;
	}

	private static boolean addCurveData(final AbstractYearMonthCurve curve, final @NonNull SeriesType seriesType, final @NonNull SeriesParser indices) {
		boolean added = false;
		if (curve != null) {
			final String curveName = curve.getName();
			if (curveName != null) {
				if (curve.isSetExpression()) {
					final String expression = curve.getExpression();
					if (expression != null) {
						indices.addSeriesExpression(curveName, seriesType, expression);
						added = true;
					}
				} else {
					addSeriesDataFromDataIndex(indices, curveName, seriesType, dateZero, curve);
					added = true;
				}
			}
		}
		return added;
	}

	/**
	 * Provides a {@link SeriesParser} object based on the default activator (the one returned by {@link Activator.getDefault()}).
	 * 
	 * @return A {@link SeriesParser} object for use in validating price expressions.
	 */

	public static @NonNull SeriesParser getParserFor(final @NonNull PricingModel pricingModel, final @NonNull PriceIndexType priceIndexType) {
		switch (priceIndexType) {
		case BUNKERS:
			return getParserFor(pricingModel, PricingPackage.Literals.PRICING_MODEL__BUNKER_FUEL_CURVES);
		case CHARTER:
			return getParserFor(pricingModel, PricingPackage.Literals.PRICING_MODEL__CHARTER_CURVES);
		case COMMODITY:
			return getParserFor(pricingModel, PricingPackage.Literals.PRICING_MODEL__COMMODITY_CURVES);
		case CURRENCY:
			return getParserFor(pricingModel, PricingPackage.Literals.PRICING_MODEL__CURRENCY_CURVES);
		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Add data from a DataIndex object to a SeriesParser object (which allows the evaluation of price expressions).
	 * 
	 * @param parser
	 *            The parser to add the index data information to.
	 * @param name
	 *            The index name to use.
	 * @param dateZero
	 *            Internally, dates are represented for the SeriesParser in offsets from a "date zero" value.
	 * @param index
	 *            The index data to use.
	 */
	public static void addSeriesDataFromDataIndex(final SeriesParser parser, final @NonNull String name, SeriesType seriesType, final YearMonth dateZero, final AbstractYearMonthCurve curve) {
		final int[] times;
		final Number[] values;

		// if no date zero is specified, we don't bother with real times or values
		if (dateZero == null) {
			// For this validation, we do not need real times or values
			times = new int[1];
			values = new Number[1];
		}
		// otherwise, we use the data from the DataIndex
		else {
			final SortedSet<Pair<YearMonth, Double>> vals = new TreeSet<>(new Comparator<Pair<YearMonth, Double>>() {
				@Override
				public int compare(final Pair<YearMonth, Double> o1, final Pair<YearMonth, Double> o2) {
					// Add some null checks
					if (o1 == null || o1.getFirst() == null) {
						return -1;
					}
					if (o2 == null || o2.getFirst() == null) {
						return 1;
					}
					return o1.getFirst().compareTo(o2.getFirst());
				}
			});
			for (final YearMonthPoint pt : curve.getPoints()) {
				vals.add(Pair.of(pt.getDate(), pt.getValue()));
			}
			times = new int[vals.size()];
			values = new Number[vals.size()];
			int k = 0;
			for (final Pair<YearMonth, Double> e : vals) {
				if (e.getFirst() == null) {
					// Handle nulls
					continue;
				}
				times[k] = convertTime(dateZero, e.getFirst());
				values[k++] = e.getSecond();
			}
		}

		parser.addSeriesData(name, seriesType, times, values);
	}

	/**
	 * Code duplication from DateAndCurveHelper.java to avoid circular project dependencies. Keep this method in sync!
	 * 
	 * @param earliest
	 * @param windowStart
	 * @return
	 */
	public static int convertTime(final YearMonth earliest, final YearMonth windowStart) {
		return Hours.between(earliest, windowStart);
	}

	public static int convertTime(final YearMonth earliest, final LocalDate windowStart) {
		return Hours.between(earliest.atDay(1), windowStart);
	}

	public static @Nullable String createConversionFactorName(@NonNull final UnitConversion factor) {
		final String from = getString(factor.getFrom());
		final String to = getString(factor.getTo());
		if (from.isEmpty() || to.isEmpty()) {
			return null;
		}
		return String.format("%s_to_%s", from, to);
	}

	public static @Nullable String createReverseConversionFactorName(@NonNull final UnitConversion factor) {
		final String from = getString(factor.getFrom());
		final String to = getString(factor.getTo());
		if (from.isEmpty() || to.isEmpty()) {
			return null;
		}
		return String.format("%s_to_%s", to, from);
	}

	private static @NonNull String getString(@Nullable final String str) {
		if (str == null) {
			return "";
		}
		return str.trim();
	}
}
