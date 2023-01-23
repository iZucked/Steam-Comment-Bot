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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.parser.astnodes.ASTNode;
import com.mmxlabs.common.parser.astnodes.BunkersSeriesASTNode;
import com.mmxlabs.common.parser.astnodes.CharterSeriesASTNode;
import com.mmxlabs.common.parser.astnodes.CommoditySeriesASTNode;
import com.mmxlabs.common.parser.astnodes.CurrencySeriesASTNode;
import com.mmxlabs.common.parser.astnodes.NamedSeriesASTNode;
import com.mmxlabs.common.parser.astnodes.PricingBasisSeriesASTNode;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.DatePoint;
import com.mmxlabs.models.lng.pricing.DatePointContainer;
import com.mmxlabs.models.lng.pricing.HolidayCalendar;
import com.mmxlabs.models.lng.pricing.HolidayCalendarEntry;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
import com.mmxlabs.models.lng.pricing.parseutils.LookupData;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;

public class ModelMarketCurveProvider extends EContentAdapter {

	private final @NonNull PricingModel pricingModel;

	private SoftReference<Map<@NonNull PriceIndexType, @NonNull SeriesParser>> cache = new SoftReference<>(null);

	private SoftReference<Map<@NonNull String, Map<@NonNull LocalDate, Double>>> settledPriceCache = new SoftReference<>(null);

	private SoftReference<Map<@NonNull String, Map<@NonNull LocalDate, String>>> holidayCalendarCache = new SoftReference<>(null);

	private SoftReference<Map<@NonNull AbstractYearMonthCurve, @Nullable YearMonth>> earlyDateCache = new SoftReference<>(null);

	private SoftReference<Map<@NonNull String, @Nullable LocalDate>> earlySettledDateCache = new SoftReference<>(null);

	private SoftReference<LookupData> expressionToIndexUseCache = new SoftReference<>(null);

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

	public synchronized Map<@NonNull PriceIndexType, @NonNull SeriesParser> buildCache() {

		final Map<@NonNull PriceIndexType, @NonNull SeriesParser> cacheObj = new HashMap<>();
		cache = new SoftReference<>(cacheObj);

		cacheObj.put(PriceIndexType.COMMODITY, PriceIndexUtils.getParserFor(pricingModel, PriceIndexType.COMMODITY));
		cacheObj.put(PriceIndexType.CHARTER, PriceIndexUtils.getParserFor(pricingModel, PriceIndexType.CHARTER));
		cacheObj.put(PriceIndexType.BUNKERS, PriceIndexUtils.getParserFor(pricingModel, PriceIndexType.BUNKERS));
		cacheObj.put(PriceIndexType.CURRENCY, PriceIndexUtils.getParserFor(pricingModel, PriceIndexType.CURRENCY));
		cacheObj.put(PriceIndexType.PRICING_BASIS, PriceIndexUtils.getParserFor(pricingModel, PriceIndexType.PRICING_BASIS));

		return cacheObj;
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

	public synchronized Map<@NonNull AbstractYearMonthCurve, @Nullable YearMonth> buildDateCache() {
		final Map<@NonNull AbstractYearMonthCurve, @Nullable YearMonth> cacheObj = new HashMap<>();
		earlyDateCache = new SoftReference<>(cacheObj);
		final Function<AbstractYearMonthCurve, @Nullable YearMonth> finder = (curve) -> {
			if (!curve.isSetExpression()) {
				final Optional<YearMonthPoint> min = curve.getPoints().stream() //
						.min((p1, p2) -> p1.getDate().compareTo(p2.getDate()));
				// No data check
				if (min.isPresent()) {
					return min.get().getDate();
				}
			}
			return null;
		};

		pricingModel.getCommodityCurves().forEach(c -> cacheObj.put(c, finder.apply(c)));
		pricingModel.getCharterCurves().forEach(c -> cacheObj.put(c, finder.apply(c)));
		pricingModel.getBunkerFuelCurves().forEach(c -> cacheObj.put(c, finder.apply(c)));
		pricingModel.getCurrencyCurves().forEach(c -> cacheObj.put(c, finder.apply(c)));
		pricingModel.getPricingBases().forEach(c -> cacheObj.put(c, finder.apply(c)));

		return cacheObj;
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
		cache.clear();
		earlyDateCache.clear();
		expressionToIndexUseCache.clear();

		settledPriceCache.clear();
		holidayCalendarCache.clear();
		earlySettledDateCache.clear();
	}

	public @NonNull SeriesParser getSeriesParser(final @NonNull PriceIndexType marketIndexType) {
		Map<@NonNull PriceIndexType, @NonNull SeriesParser> map = cache.get();
		if (map == null) {
			map = buildCache();
		}
		return map.get(marketIndexType);

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

	public @Nullable YearMonth getEarliestDate(final @NonNull AbstractYearMonthCurve index) {

		Map<@NonNull AbstractYearMonthCurve, @Nullable YearMonth> map = earlyDateCache.get();
		if (map == null) {
			map = buildDateCache();
		}
		return map.getOrDefault(index, null);
	}

	public @Nullable LocalDate getEarliestSettledDate(final @NonNull String indexName) {

		Map<@NonNull String, @Nullable LocalDate> map = earlySettledDateCache.get();
		if (map == null) {
			map = buildSettledDateCache();
		}
		return map.getOrDefault(indexName.toLowerCase(), null);
	}

	public @NonNull Collection<@NonNull AbstractYearMonthCurve> getLinkedCurves(final String priceExpression) {
		if (priceExpression == null || priceExpression.trim().isEmpty()) {
			return Collections.emptySet();
		}

		LookupData lookupData = expressionToIndexUseCache.get();
		if (lookupData == null) {
			lookupData = LookupData.createLookupData(pricingModel);
			expressionToIndexUseCache = new SoftReference<>(lookupData);
		}

		final @Nullable Collection<@NonNull AbstractYearMonthCurve> r;
		if (lookupData.expressionCache2.containsKey(priceExpression)) {
			r = lookupData.expressionCache2.get(priceExpression);
		} else {
			try {
				final SeriesParser commodityIndices = PriceIndexUtils.getParserFor(lookupData.pricingModel, PriceIndexType.COMMODITY);
				final ASTNode parsedExpression = commodityIndices.parse(priceExpression);
				r = collectIndicies(parsedExpression, lookupData, new HashSet<>());
				lookupData.expressionCache2.put(priceExpression, r);
			} catch (final Exception e) {
				return Collections.emptySet();
			}
		}
		if (r != null) {
			return r;
		}
		return Collections.emptySet();

	}
	
	public @NonNull String convertPricingBasisToPriceExpression(final String pricingBasis) {
		if (pricingBasis == null || pricingBasis.isBlank()) {
			return "";
		}
		String priceExpression = "";
		LookupData lookupData = expressionToIndexUseCache.get();
		if (lookupData == null) {
			lookupData = LookupData.createLookupData(pricingModel);
			expressionToIndexUseCache = new SoftReference<>(lookupData);
		}
		try {
			final SeriesParser pricingBasesParser = PriceIndexUtils.getParserFor(lookupData.pricingModel, PriceIndexType.PRICING_BASIS);
			final ASTNode parsedExpression = pricingBasesParser.parse(pricingBasis);
			priceExpression = parsedExpression.asString();
		} catch (Exception e) {
			return priceExpression;
		}
		return priceExpression;
	}

	/**
	 * Given an expression and pricing date, work out which data curves have been
	 * used and the earliest date needed for that curve. E.g. a DATEDAVG expression
	 * will need dates before the pricing date.
	 * 
	 * @param priceExpression
	 * @param date
	 * @return
	 */
	public @NonNull Map<AbstractYearMonthCurve, LocalDate> getLinkedCurvesAndEarliestNeededDate(final String priceExpression, final LocalDate date) {
		if (priceExpression == null || priceExpression.trim().isEmpty()) {
			return Collections.emptyMap();
		}

		LookupData lookupData = expressionToIndexUseCache.get();
		if (lookupData == null) {
			lookupData = LookupData.createLookupData(pricingModel);
			expressionToIndexUseCache = new SoftReference<>(lookupData);
		}

		final LookupData pLookupData = lookupData;
		try {
			return IndexToDate.calculateIndicesToFirstDate(priceExpression, date, pLookupData);
		} catch (final Exception e) {
			// ignore
		}
		return Collections.emptyMap();
	}

	public static Set<@NonNull AbstractYearMonthCurve> collectIndicies(@NonNull final ASTNode parentNode, final LookupData lookupData) {
		final Set<@NonNull AbstractYearMonthCurve> s = new HashSet<>();

		if (parentNode instanceof final CommoditySeriesASTNode n) {
			s.addAll(Collections.singleton(lookupData.commodityMap.get(n.getName())));
		} else if (parentNode instanceof final CharterSeriesASTNode n) {
			s.addAll(Collections.singleton(lookupData.charterMap.get(n.getName())));
		} else if (parentNode instanceof final BunkersSeriesASTNode n) {
			s.addAll(Collections.singleton(lookupData.baseFuelMap.get(n.getName())));
		} else if (parentNode instanceof final CurrencySeriesASTNode n) {
			s.addAll(Collections.singleton(lookupData.currencyMap.get(n.getName())));
		} else if (parentNode instanceof final PricingBasisSeriesASTNode n) {
			s.addAll(Collections.singleton(lookupData.pricingBases.get(n.getName())));
		}

		for (final ASTNode child : parentNode.getChildren()) {
			s.addAll(collectIndicies(child, lookupData));
		}
		return s;
	}

	private static Set<@NonNull AbstractYearMonthCurve> collectIndicies(final ASTNode astNode, final LookupData lookupData, final Set<AbstractYearMonthCurve> result) {

		if (astNode instanceof final NamedSeriesASTNode namedSeriesASTNode) {

			final String indexName = namedSeriesASTNode.getName().toLowerCase();
			if (lookupData.commodityMap.containsKey(indexName)) {
				final CommodityCurve commodityCurve = lookupData.commodityMap.get(indexName);
				if (commodityCurve.isSetExpression()) {
					final SeriesParser commodityIndices = PriceIndexUtils.getParserFor(lookupData.pricingModel, PriceIndexType.COMMODITY);
					final ASTNode parsedPriceExpression = commodityIndices.parse(commodityCurve.getExpression());
					collectIndicies(parsedPriceExpression, lookupData, result);
				} else {
					result.add(commodityCurve);
				}

			} else if (lookupData.charterMap.containsKey(indexName)) {
				result.add(lookupData.charterMap.get(indexName));
			} else if (lookupData.baseFuelMap.containsKey(indexName)) {
				result.add(lookupData.baseFuelMap.get(indexName));
			} else if (lookupData.currencyMap.containsKey(indexName)) {
				result.add(lookupData.currencyMap.get(indexName));
			}
		} else {
			astNode.getChildren().forEach(c -> collectIndicies(c, lookupData, result));
		}

		return result;
	}

	public AbstractYearMonthCurve getCurve(final PriceIndexType priceIndexType, final String name) {
		LookupData lookupData = expressionToIndexUseCache.get();
		if (lookupData == null) {
			lookupData = LookupData.createLookupData(pricingModel);
			expressionToIndexUseCache = new SoftReference<>(lookupData);
		}

		switch (priceIndexType) {
		case BUNKERS:
			return lookupData.baseFuelMap.get(name.toLowerCase());
		case CHARTER:
			return lookupData.charterMap.get(name.toLowerCase());
		case COMMODITY:
			return lookupData.commodityMap.get(name.toLowerCase());
		case CURRENCY:
			return lookupData.currencyMap.get(name.toLowerCase());
		case PRICING_BASIS:
			return lookupData.pricingBases.get(name.toLowerCase());
		default:
			break;

		}
		return null;
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

}
