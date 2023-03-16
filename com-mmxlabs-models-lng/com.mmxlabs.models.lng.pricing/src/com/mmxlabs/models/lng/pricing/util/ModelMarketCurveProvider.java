/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import java.lang.ref.SoftReference;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.DatePoint;
import com.mmxlabs.models.lng.pricing.DatePointContainer;
import com.mmxlabs.models.lng.pricing.HolidayCalendar;
import com.mmxlabs.models.lng.pricing.HolidayCalendarEntry;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.parseutils.PricingDataCache;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;

public class ModelMarketCurveProvider extends EContentAdapter {

	private final @NonNull PricingModel pricingModel;

	private SoftReference<Map<@NonNull String, Map<@NonNull LocalDate, Double>>> settledPriceCache = new SoftReference<>(null);

	private SoftReference<Map<@NonNull String, Map<@NonNull LocalDate, String>>> holidayCalendarCache = new SoftReference<>(null);

	private SoftReference<Map<@NonNull String, @Nullable LocalDate>> earlySettledDateCache = new SoftReference<>(null);

	private SoftReference<PricingDataCache> loookupDataCache = new SoftReference<>(null);

	public static @NonNull ModelMarketCurveProvider getOrCreate(final @NonNull PricingModel pricingModel) {
		synchronized (pricingModel) {
			for (final Object o : pricingModel.eAdapters()) {
				if (o instanceof ModelMarketCurveProvider) {
					return (ModelMarketCurveProvider) o;
				}
			}
			return new ModelMarketCurveProvider(pricingModel);
		}
	}

	private ModelMarketCurveProvider(final @NonNull PricingModel pricingModel) {
		this.pricingModel = pricingModel;
		pricingModel.eAdapters().add(this);
	}

	public String getVersion() {
		return pricingModel.getMarketCurvesVersionRecord().getVersion();
	}

	@Override
	public void notifyChanged(final @Nullable Notification notification) {

		super.notifyChanged(notification);

		if (notification.isTouch()) {
			return;
		}
		clearCache();
	}

	@Override
	public @Nullable Notifier getTarget() {
		return null;
	}

	@Override
	public void setTarget(final @Nullable Notifier newTarget) {

	}

	public synchronized Map<String, Map<LocalDate, Double>> buildSettledPriceCache() {
		final Map<String, Map<LocalDate, Double>> settledPrices = new HashMap<>();
		for (final DatePointContainer c : pricingModel.getSettledPrices()) {
			if (c.getName() == null || c.getName().isEmpty()) {
				continue;
			}
			final Map<LocalDate, Double> m = new HashMap<>();
			for (final DatePoint p : c.getPoints()) {
				m.put(p.getDate(), p.getValue());
			}
			settledPrices.put(c.getName().toLowerCase(), m);
		}
		settledPriceCache = new SoftReference<>(settledPrices);

		return settledPrices;
	}

	private Map<String, Map<LocalDate, String>> buildindexHolidaysCache() {
		final Map<String, Map<LocalDate, String>> indexHolidays = new HashMap<>();
		for (final HolidayCalendar hc : pricingModel.getHolidayCalendars()) {
			for (final HolidayCalendarEntry entry : hc.getEntries()) {
				final Map<LocalDate, String> m = new HashMap<>();
				m.put(entry.getDate(), entry.getComment());
				indexHolidays.put(hc.getName().toLowerCase(), m);
			}
		}
		holidayCalendarCache = new SoftReference<>(indexHolidays);

		return indexHolidays;
	}

	public synchronized Map<@NonNull String, @Nullable LocalDate> buildSettledDateCache() {
		final Map<@NonNull String, @Nullable LocalDate> cacheObj = new HashMap<>();
		earlySettledDateCache = new SoftReference<>(cacheObj);
		final Function<DatePointContainer, @Nullable LocalDate> finder = (curve) -> {
			final List<DatePoint> points = curve.getPoints();
			final Optional<?> min = points.stream() //
					.min((p1, p2) -> {
						return p1.getDate().compareTo(p2.getDate());
					});
			// No data check
			if (min.isPresent()) {
				return ((DatePoint) min.get()).getDate();
			}
			return null;
		};

		pricingModel.getSettledPrices().forEach(c -> cacheObj.put(c.getName().toLowerCase(), finder.apply(c)));

		return cacheObj;
	}

	public synchronized void clearCache() {
		loookupDataCache.clear();

		settledPriceCache.clear();
		holidayCalendarCache.clear();
		earlySettledDateCache.clear();
	}

	public @NonNull SeriesParser getSeriesParser(final @NonNull PriceIndexType type) {

		return getLookupData().getSeriesParser(type);
	}

	public @Nullable Map<LocalDate, Double> getSettledPrices(final String indexName) {
		Map<String, Map<LocalDate, Double>> map = settledPriceCache.get();
		if (map == null) {
			map = buildSettledPriceCache();
		}
		return map.get(indexName.toLowerCase());

	}

	public @Nullable Map<LocalDate, String> getIndexHolidays(final String indexName) {
		Map<String, Map<LocalDate, String>> map = holidayCalendarCache.get();
		if (map == null) {
			map = buildindexHolidaysCache();
		}
		return map.get(indexName.toLowerCase());

	}

	public @Nullable YearMonth getEarliestDate(final @NonNull AbstractYearMonthCurve curve) {
		return getLookupData().getEarliestDate(curve);
	}

	public @Nullable LocalDate getEarliestSettledDate(final @NonNull String indexName) {

		Map<@NonNull String, @Nullable LocalDate> map = earlySettledDateCache.get();
		if (map == null) {
			map = buildSettledDateCache();
		}
		return map.getOrDefault(indexName.toLowerCase(), null);
	}

	public @NonNull Collection<@NonNull AbstractYearMonthCurve> getLinkedCurves(final String priceExpression, PriceIndexType type) {
		if (priceExpression == null || priceExpression.trim().isEmpty()) {
			return Collections.emptySet();
		}

		return getLookupData().getLinkedCurves(priceExpression, type);
	}

	/**
	 * Given an expression and pricing date, work out which data curves have been used and the earliest date needed for that curve. E.g. a DATEDAVG expression will need dates before the pricing date.
	 * 
	 * @param priceExpression
	 * @param date
	 * @return
	 */
	public @NonNull Map<AbstractYearMonthCurve, LocalDate> getLinkedCurvesAndEarliestNeededDate(final String priceExpression, PriceIndexType type, final LocalDate date) {
		if (priceExpression == null || priceExpression.trim().isEmpty()) {
			return Collections.emptyMap();
		}

		try {
			return getLookupData().getLinkedCurvesAndFirstDateNeeded(priceExpression, type, date);
		} catch (final Exception e) {
			// ignore
		}
		return Collections.emptyMap();
	}

	public @Nullable AbstractYearMonthCurve getCurve(final PriceIndexType priceIndexType, final String name) {
		return getLookupData().getCurve(priceIndexType, name);
	}

	private synchronized PricingDataCache getLookupData() {
		PricingDataCache lookupData = loookupDataCache.get();
		if (lookupData == null) {
			lookupData = PricingDataCache.createLookupData(pricingModel);
			loookupDataCache = new SoftReference<>(lookupData);
		}
		return lookupData;
	}

	public static Map<String, String> getCurveToIndexMap(final PricingModel pm) {
		final Map<String, String> result = new HashMap<>();
		for (final CommodityCurve cc : pm.getCommodityCurves()) {
			if (cc.getMarketIndex() != null) {
				result.put(cc.getName().toLowerCase(), cc.getMarketIndex().getName().toLowerCase());
			}
		}
		return result;
	}

	public PricingModel getPricingModel() {
		return pricingModel;
	}
	
	public @NonNull PricingDataCache getPricingDataCache() {
		return getLookupData();
	}

}
