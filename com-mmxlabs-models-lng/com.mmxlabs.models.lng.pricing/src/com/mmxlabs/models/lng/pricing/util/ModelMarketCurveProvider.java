/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.RawTreeParser;
import com.mmxlabs.common.parser.nodes.Node;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.BunkerFuelCurve;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.DatePoint;
import com.mmxlabs.models.lng.pricing.DatePointContainer;
import com.mmxlabs.models.lng.pricing.HolidayCalendar;
import com.mmxlabs.models.lng.pricing.HolidayCalendarEntry;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
import com.mmxlabs.models.lng.pricing.parseutils.LookupData;
import com.mmxlabs.models.lng.pricing.util.IndexToDate.IIndexToDateNode;
import com.mmxlabs.models.lng.pricing.util.IndexToDate.IndexDateRecord;
import com.mmxlabs.models.lng.pricing.util.IndexToDate.IndexToDateRecords;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;

public class ModelMarketCurveProvider extends EContentAdapter {

	private final @NonNull PricingModel pricingModel;

	private static Map<String, String> marketIndexNames;

	private SoftReference<Map<@NonNull PriceIndexType, @NonNull SeriesParser>> cache = new SoftReference<>(null);

	private SoftReference<Map<@NonNull String, Map<@NonNull LocalDate, Double>>> settledPriceCache = new SoftReference<>(null);

	private SoftReference<Map<@NonNull String, Map<@NonNull LocalDate, String>>> holidayCalendarCache = new SoftReference<>(null);

	private SoftReference<Map<@NonNull AbstractYearMonthCurve, @Nullable YearMonth>> earlyDateCache = new SoftReference<>(null);

	private SoftReference<Map<@NonNull String, @Nullable LocalDate>> earlySettledDateCache = new SoftReference<>(null);

	private SoftReference<LookupData> expressionToIndexUseCache = new SoftReference<>(null);

	public static @NonNull ModelMarketCurveProvider getOrCreate(final @NonNull PricingModel pricingModel) {
		synchronized (pricingModel) {
			for (Object o : pricingModel.eAdapters()) {
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
				final Optional<?> min = curve.getPoints().stream() //
						.min((p1, p2) -> {
							return p1.getDate().compareTo(p2.getDate());
						});
				// No data check
				if (min.isPresent()) {
					return ((YearMonthPoint) min.get()).getDate();
				}
			}
			return null;
		};

		pricingModel.getCommodityCurves().forEach(c -> cacheObj.put(c, finder.apply(c)));
		pricingModel.getCharterCurves().forEach(c -> cacheObj.put(c, finder.apply(c)));
		pricingModel.getBunkerFuelCurves().forEach(c -> cacheObj.put(c, finder.apply(c)));
		pricingModel.getCurrencyCurves().forEach(c -> cacheObj.put(c, finder.apply(c)));

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

		final LookupData pLookupData = lookupData;
		final @Nullable Collection<@NonNull AbstractYearMonthCurve> r;
		if (lookupData.expressionCache2.containsKey(priceExpression)) {
			r = lookupData.expressionCache2.get(priceExpression);
		} else {
			try {
				final IExpression<Node> parse = new RawTreeParser().parse(priceExpression);
				final Node p = parse.evaluate();
				final Node node = expandNode(p, pLookupData);
				r = collectIndicies(node, pLookupData);
				lookupData.expressionCache2.put(priceExpression, r);
			} catch (Exception e) {
				return Collections.emptySet();
			}
		}
		if (r != null) {
			return r;
		}
		return Collections.emptySet();

	}

	public @NonNull List<Pair<AbstractYearMonthCurve, LocalDate>> getLinkedCurvesAndDate(final String priceExpression, LocalDate date) {
		if (priceExpression == null || priceExpression.trim().isEmpty()) {
			return Collections.emptyList();
		}

		LookupData lookupData = expressionToIndexUseCache.get();
		if (lookupData == null) {
			lookupData = LookupData.createLookupData(pricingModel);
			expressionToIndexUseCache = new SoftReference<>(lookupData);
		}

		final LookupData pLookupData = lookupData;
		try {
			IIndexToDateNode n = IndexToDate.calculateIndicesToDate(priceExpression, date, pLookupData);
			if (n instanceof IndexToDateRecords) {
				IndexToDateRecords indexToDateRecords = (IndexToDateRecords) n;
				List<Pair<AbstractYearMonthCurve, LocalDate>> result = new LinkedList<>();
				for (IndexDateRecord rec : indexToDateRecords.records) {
					result.add(new Pair<>(rec.index, rec.date));
				}
				return result;

			}
		} catch (Exception e) {
			// ignore
		}
		return Collections.emptyList();
	}

	private static @NonNull Node expandNode(@NonNull final Node parentNode, final LookupData lookupData) {

		if (lookupData.expressionCache.containsKey(parentNode.token)) {
			return lookupData.expressionCache.get(parentNode.token);
		}

		if (parentNode.children.length == 0) {
			// Leaf node, this should be an index or a value
			if (lookupData.commodityMap.containsKey(parentNode.token.toLowerCase())) {
				final CommodityCurve idx = lookupData.commodityMap.get(parentNode.token.toLowerCase());

				// Matched derived index...
				if (idx.isSetExpression()) {
					// Parse the expression
					final IExpression<Node> parse = new RawTreeParser().parse(idx.getExpression());
					final Node p = parse.evaluate();
					// Expand the parsed tree again if needed,
					@Nullable
					final Node expandNode = expandNode(p, lookupData);
					// return the new sub-parse tree for the expression
					if (expandNode != null) {
						lookupData.expressionCache.put(idx.getExpression(), expandNode);
						return expandNode;
					}
					return p;
				} else {
					return parentNode;
				}
			}
			if (lookupData.charterMap.containsKey(parentNode.token.toLowerCase())) {
				final CharterCurve idx = lookupData.charterMap.get(parentNode.token.toLowerCase());

				// Matched derived index...
				if (idx.isSetExpression()) {
					// Parse the expression
					final IExpression<Node> parse = new RawTreeParser().parse(idx.getExpression());
					final Node p = parse.evaluate();
					// Expand the parsed tree again if needed,
					@Nullable
					final Node expandNode = expandNode(p, lookupData);
					// return the new sub-parse tree for the expression
					if (expandNode != null) {
						lookupData.expressionCache.put(idx.getExpression(), expandNode);
						return expandNode;
					}
					return p;
				} else {
					return parentNode;
				}
			}
			if (lookupData.baseFuelMap.containsKey(parentNode.token.toLowerCase())) {
				final BunkerFuelCurve idx = lookupData.baseFuelMap.get(parentNode.token.toLowerCase());

				// Matched derived index...
				if (idx.isSetExpression()) {
					// Parse the expression
					final IExpression<Node> parse = new RawTreeParser().parse(idx.getExpression());
					final Node p = parse.evaluate();
					// Expand the parsed tree again if needed,
					@Nullable
					final Node expandNode = expandNode(p, lookupData);
					// return the new sub-parse tree for the expression
					if (expandNode != null) {
						lookupData.expressionCache.put(idx.getExpression(), expandNode);
						return expandNode;
					}
					return p;
				} else {
					return parentNode;
				}
			}
			return parentNode;

			// return null;
		} else {
			// We have children, token *should* be an operator, expand out the child nodes
			for (int i = 0; i < parentNode.children.length; ++i) {
				final Node replacement = expandNode(parentNode.children[i], lookupData);
				if (replacement != null) {
					parentNode.children[i] = replacement;
				}
			}
			return parentNode;
		}
	}

	public static Set<@NonNull AbstractYearMonthCurve> collectIndicies(@NonNull final Node parentNode, final LookupData lookupData) {
		final Set<@NonNull AbstractYearMonthCurve> s = new HashSet<>();
		// FIXME: Date shift!
		if (parentNode.token.equalsIgnoreCase("SHIFT")) {
			s.addAll(collectIndicies(parentNode.children[0], lookupData));
		} else if (lookupData.commodityMap.containsKey(parentNode.token.toLowerCase())) {
			s.addAll(Collections.singleton(lookupData.commodityMap.get(parentNode.token.toLowerCase())));
		} else if (lookupData.charterMap.containsKey(parentNode.token.toLowerCase())) {
			s.addAll(Collections.singleton(lookupData.charterMap.get(parentNode.token.toLowerCase())));
		} else if (lookupData.baseFuelMap.containsKey(parentNode.token.toLowerCase())) {
			s.addAll(Collections.singleton(lookupData.baseFuelMap.get(parentNode.token.toLowerCase())));
		} else if (lookupData.currencyMap.containsKey(parentNode.token.toLowerCase())) {
			s.addAll(Collections.singleton(lookupData.currencyMap.get(parentNode.token.toLowerCase())));
		}

		for (final Node child : parentNode.children) {
			s.addAll(collectIndicies(child, lookupData));
		}
		return s;
	}

	public AbstractYearMonthCurve getCurve(PriceIndexType priceIndexType, String name) {
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
		default:
			break;

		}
		return null;
	}

	public static String getMarketIndexName(final PricingModel pm, final String curveName) {
		if (marketIndexNames == null) {
			marketIndexNames = new HashMap<>();
		}
		String result = marketIndexNames.get(curveName);
		if (result != null) {
			return result;
		}
		final EList<CommodityCurve> indices = pm.getCommodityCurves();

		List<CommodityCurve> inc = indices.stream()//
				.filter(e -> e.getName().equalsIgnoreCase(curveName)).collect(Collectors.toList());

		if (inc.isEmpty() || inc.size() > 1) {
			return null;
		}
		if (inc.get(0).getMarketIndex() == null) {
			return null;
		}
		result = inc.get(0).getMarketIndex().getName();
		marketIndexNames.put(curveName, result);
		return result;
	}

	public PricingModel getPricingModel() {
		return pricingModel;
	}

}
