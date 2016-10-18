/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.pricing.CurrencyIndex;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.UnitConversion;

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
	public static @NonNull SeriesParser getParserFor(final @NonNull PricingModel pricingModel, final @NonNull EReference reference) {

		final SeriesParser indices = new SeriesParser();
		indices.setShiftMapper((date, shift) -> {
			// Get input as local date time and shift.
			@NonNull
			final LocalDateTime plusMonths = dateTimeZero.plusHours(date).minusMonths(shift);
			// Convert back to internal time units.
			return Hours.between(dateTimeZero, plusMonths);
		});
		{
			final List<NamedIndexContainer<? extends Number>> namedIndexContainerList = (List<NamedIndexContainer<? extends Number>>) pricingModel.eGet(reference);
			for (final NamedIndexContainer<? extends Number> namedIndexContainer : namedIndexContainerList) {
				if (namedIndexContainer.getName() == null) {
					continue;
				}
				final Index<? extends Number> index = namedIndexContainer.getData();
				if (index instanceof DataIndex) {
					addSeriesDataFromDataIndex(indices, namedIndexContainer.getName(), dateZero, (DataIndex<? extends Number>) index);
				} else if (index instanceof DerivedIndex) {
					indices.addSeriesExpression(namedIndexContainer.getName(), ((DerivedIndex) index).getExpression());
				}
			}
		}
		// Add in currency curves
		if (reference != PricingPackage.Literals.PRICING_MODEL__CURRENCY_INDICES) {
			final List<CurrencyIndex> namedIndexContainerList = pricingModel.getCurrencyIndices();
			for (final NamedIndexContainer<? extends Number> namedIndexContainer : namedIndexContainerList) {
				final Index<? extends Number> index = namedIndexContainer.getData();
				if (index instanceof DataIndex) {
					addSeriesDataFromDataIndex(indices, namedIndexContainer.getName(), dateZero, (DataIndex<? extends Number>) index);
				} else if (index instanceof DerivedIndex) {
					indices.addSeriesExpression(namedIndexContainer.getName(), ((DerivedIndex) index).getExpression());
				}
			}
		}

		for (final UnitConversion factor : pricingModel.getConversionFactors()) {
			final String name = createConversionFactorName(factor);
			if (name != null) {
				indices.addSeriesExpression(name, Double.toString(factor.getFactor()));
			}
		}

		return indices;
	}

	/**
	 * Provides a {@link SeriesParser} object based on the default activator (the one returned by {@link Activator.getDefault()}).
	 * 
	 * @return A {@link SeriesParser} object for use in validating price expressions.
	 */

	public static @NonNull SeriesParser getParserFor(final @NonNull PricingModel pricingModel, final @NonNull PriceIndexType priceIndexType) {
		switch (priceIndexType) {
		case BUNKERS:
			return getParserFor(pricingModel, PricingPackage.Literals.PRICING_MODEL__BASE_FUEL_PRICES);
		case CHARTER:
			return getParserFor(pricingModel, PricingPackage.Literals.PRICING_MODEL__CHARTER_INDICES);
		case COMMODITY:
			return getParserFor(pricingModel, PricingPackage.Literals.PRICING_MODEL__COMMODITY_INDICES);
		case CURRENCY:
			return getParserFor(pricingModel, PricingPackage.Literals.PRICING_MODEL__CURRENCY_INDICES);
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
	public static void addSeriesDataFromDataIndex(final SeriesParser parser, final String name, final YearMonth dateZero, final DataIndex<? extends Number> index) {
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
			final SortedSet<Pair<YearMonth, Number>> vals = new TreeSet<Pair<YearMonth, Number>>(new Comparator<Pair<YearMonth, ?>>() {
				@Override
				public int compare(final Pair<YearMonth, ?> o1, final Pair<YearMonth, ?> o2) {
					return o1.getFirst().compareTo(o2.getFirst());
				}
			});
			for (final IndexPoint<? extends Number> pt : index.getPoints()) {
				vals.add(new Pair<YearMonth, Number>(pt.getDate(), pt.getValue()));
			}
			times = new int[vals.size()];
			values = new Number[vals.size()];
			int k = 0;
			for (final Pair<YearMonth, Number> e : vals) {
				times[k] = convertTime(dateZero, e.getFirst());
				values[k++] = e.getSecond();
			}
		}

		parser.addSeriesData(name, times, values);
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

	public static @Nullable String createConversionFactorName(@NonNull final UnitConversion factor) {
		final String from = getString(factor.getFrom());
		final String to = getString(factor.getTo());
		if (from.isEmpty() || to.isEmpty()) {
			return null;
		}
		return String.format("%ss_per_%s", from, to);
	}

	private static @NonNull String getString(@Nullable final String str) {
		if (str == null) {
			return "";
		}
		return str.trim();
	}
}
