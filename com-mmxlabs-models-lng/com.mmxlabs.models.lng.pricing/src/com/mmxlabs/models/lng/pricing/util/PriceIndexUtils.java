/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.management.timer.Timer;

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
 * @since 5.1
 * 
 */
public class PriceIndexUtils {

	/**
	 * Provides a {@link SeriesParser} object based on the default activator (the one returned by {@link Activator.getDefault()}).
	 * 
	 * @return A {@link SeriesParser} object for use in validating price expressions.
	 * @since 5.0
	 */
	@SuppressWarnings("rawtypes")
	public static SeriesParser getParserForCommodityIndices(final PricingModel pricingModel, final Date dateZero) {

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
	 * @since 5.0
	 */
	public static void addSeriesDataFromDataIndex(final SeriesParser parser, final String name, final Date dateZero, final DataIndex<? extends Number> index) {
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
			final SortedSet<Pair<Date, Number>> vals = new TreeSet<Pair<Date, Number>>(new Comparator<Pair<Date, ?>>() {
				@Override
				public int compare(final Pair<Date, ?> o1, final Pair<Date, ?> o2) {
					return o1.getFirst().compareTo(o2.getFirst());
				}
			});
			for (final IndexPoint<? extends Number> pt : index.getPoints()) {
				vals.add(new Pair<Date, Number>(pt.getDate(), pt.getValue()));
			}
			times = new int[vals.size()];
			values = new Number[vals.size()];
			int k = 0;
			for (final Pair<Date, Number> e : vals) {
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
	 * @since 5.0
	 */
	public static int convertTime(final Date earliest, final Date windowStart) {
		final TimeZone timezone = TimeZone.getTimeZone("UTC");
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

}
