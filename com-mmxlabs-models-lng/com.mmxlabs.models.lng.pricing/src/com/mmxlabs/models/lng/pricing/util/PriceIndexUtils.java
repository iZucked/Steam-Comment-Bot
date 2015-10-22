/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import java.time.Duration;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingModel;

/**
 * 
 * @author Simon McGregor
 * 
 *         Utility class to provide methods for evaluating index curves
 * 
 */
public class PriceIndexUtils {

	/**
	 * Provides a {@link SeriesParser} object based on the default activator (the one returned by {@link Activator.getDefault()}).
	 * 
	 * @return A {@link SeriesParser} object for use in validating price expressions.
	 */
	@SuppressWarnings("rawtypes")
	public static SeriesParser getParserForCommodityIndices(final PricingModel pricingModel, final YearMonth dateZero) {

		final SeriesParser indices = new SeriesParser();

		for (final CommodityIndex commodityIndex : pricingModel.getCommodityIndices()) {
			final Index<Double> index = commodityIndex.getData();
			if (index instanceof DataIndex) {
				addSeriesDataFromDataIndex(indices, commodityIndex.getName(), dateZero, (DataIndex<? extends Number>) index);
			} else if (index instanceof DerivedIndex) {
				indices.addSeriesExpression(commodityIndex.getName(), ((DerivedIndex) index).getExpression());
			}
		}
		return indices;
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
		return (int) Duration.between(Instant.from(earliest.atDay(1).atStartOfDay(ZoneId.of("UTC"))), Instant.from(windowStart.atDay(1).atStartOfDay(ZoneId.of("UTC")))).toHours();
	}
}
