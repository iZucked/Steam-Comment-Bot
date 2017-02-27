/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import java.lang.ref.SoftReference;
import java.time.YearMonth;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.CurrencyIndex;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.parser.Node;
import com.mmxlabs.models.lng.pricing.parser.RawTreeParser;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;

public class MarketIndexCache extends EContentAdapter {

	private SoftReference<Map<@NonNull PriceIndexType, @NonNull SeriesParser>> cache = new SoftReference<>(null);

	private SoftReference<Map<@NonNull NamedIndexContainer<?>, @Nullable YearMonth>> earlyDateCache = new SoftReference<>(null);

	private SoftReference<LookupData> expressionToIndexUseCache = new SoftReference<>(null);

	private final @NonNull PricingModel pricingModel;

	public MarketIndexCache(final @NonNull PricingModel pricingModel) {
		this.pricingModel = pricingModel;
	}

	@Override
	public void notifyChanged(final Notification notification) {
		super.notifyChanged(notification);

		if (notification.isTouch()) {
			return;
		}
		if (notification.getEventType() == Notification.REMOVING_ADAPTER) {
			return;
		}
		clearCache();
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

	public synchronized Map<@NonNull NamedIndexContainer<?>, @Nullable YearMonth> buildDateCache() {
		final Map<@NonNull NamedIndexContainer<?>, @Nullable YearMonth> cacheObj = new HashMap<>();
		earlyDateCache = new SoftReference<>(cacheObj);
		final Function<NamedIndexContainer<?>, @Nullable YearMonth> finder = (curve) -> {
			final Index<?> data = curve.getData();
			if (data instanceof DataIndex<?>) {
				final DataIndex<?> indexData = (DataIndex<?>) data;
				final Optional<?> min = indexData.getPoints().stream() //
						.min((p1, p2) -> {
							return p1.getDate().compareTo(p2.getDate());
						});
				// No data check
				if (min.isPresent()) {
					return ((IndexPoint) min.get()).getDate();
				}
			}
			return null;
		};

		pricingModel.getCommodityIndices().forEach(c -> cacheObj.put(c, finder.apply(c)));
		pricingModel.getCharterIndices().forEach(c -> cacheObj.put(c, finder.apply(c)));
		pricingModel.getBaseFuelPrices().forEach(c -> cacheObj.put(c, finder.apply(c)));
		pricingModel.getCurrencyIndices().forEach(c -> cacheObj.put(c, finder.apply(c)));

		return cacheObj;
	}

	public synchronized void clearCache() {
		cache.clear();
		earlyDateCache.clear();
	}

	public @NonNull SeriesParser getSeriesParser(final @NonNull PriceIndexType marketIndexType) {
		Map<@NonNull PriceIndexType, @NonNull SeriesParser> map = cache.get();
		if (map == null) {
			map = buildCache();
		}
		return map.get(marketIndexType);

	}

	public @Nullable YearMonth getEarliestDate(final @NonNull NamedIndexContainer<?> index) {

		Map<@NonNull NamedIndexContainer<?>, @Nullable YearMonth> map = earlyDateCache.get();
		if (map == null) {
			map = buildDateCache();
		}
		return map.getOrDefault(index, null);
	}

	private static class LookupData {
		public Map<String, CommodityIndex> commodityMap = new HashMap<>();
		public Map<String, CurrencyIndex> currencyMap = new HashMap<>();
		public Map<String, CharterIndex> charterMap = new HashMap<>();
		public Map<String, BaseFuelIndex> baseFuelMap = new HashMap<>();

		public Map<String, Node> expressionCache = new HashMap<>();
		public Map<String, Collection<NamedIndexContainer<?>>> expressionCache2 = new HashMap<>();
	}

	private static @NonNull LookupData createLookupData(final PricingModel pricingModel) {
		final LookupData lookupData = new LookupData();

		pricingModel.getCommodityIndices().forEach(idx -> lookupData.commodityMap.put(idx.getName().toLowerCase(), idx));
		pricingModel.getCharterIndices().forEach(idx -> lookupData.charterMap.put(idx.getName().toLowerCase(), idx));
		pricingModel.getBaseFuelPrices().forEach(idx -> lookupData.baseFuelMap.put(idx.getName().toLowerCase(), idx));
		pricingModel.getCurrencyIndices().forEach(idx -> lookupData.currencyMap.put(idx.getName().toLowerCase(), idx));

		return lookupData;

	}

	public @NonNull Collection<NamedIndexContainer<?>> getLinkedCurves(final String priceExpression) {
		if (priceExpression == null || priceExpression.trim().isEmpty()) {
			return Collections.emptySet();
		}

		LookupData lookupData = expressionToIndexUseCache.get();
		if (lookupData == null) {
			lookupData = createLookupData(pricingModel);
			expressionToIndexUseCache = new SoftReference<>(lookupData);
		}

		final LookupData pLookupData = lookupData;
		final @Nullable Collection<NamedIndexContainer<?>> r = lookupData.expressionCache2.computeIfAbsent(priceExpression, key -> {
			try {
				// Parse the expression
				final IExpression<Node> parse = new RawTreeParser().parse(key);
				final Node p = parse.evaluate();
				final Node node = expandNode(p, pLookupData);
				final Collection<NamedIndexContainer<?>> result = collectIndicies(node, pLookupData);
				if (result != null) {
					return result;
				}

				return Collections.emptySet();
			} catch (final Exception e) {
				return null;
			}
		});
		if (r != null) {
			return r;
		}
		return Collections.emptySet();

	}

	private static @NonNull Node expandNode(@NonNull final Node parentNode, final LookupData lookupData) {

		if (lookupData.expressionCache.containsKey(parentNode.token)) {
			return lookupData.expressionCache.get(parentNode.token);
		}

		if (parentNode.children.length == 0) {
			// Leaf node, this should be an index or a value
			if (lookupData.commodityMap.containsKey(parentNode.token.toLowerCase())) {
				final CommodityIndex idx = lookupData.commodityMap.get(parentNode.token.toLowerCase());

				// Matched derived index...
				if (idx.getData() instanceof DerivedIndex<?>) {
					final DerivedIndex<?> derivedIndex = (DerivedIndex<?>) idx.getData();
					// Parse the expression
					final IExpression<Node> parse = new RawTreeParser().parse(derivedIndex.getExpression());
					final Node p = parse.evaluate();
					// Expand the parsed tree again if needed,
					@Nullable
					final Node expandNode = expandNode(p, lookupData);
					// return the new sub-parse tree for the expression
					if (expandNode != null) {
						lookupData.expressionCache.put(derivedIndex.getExpression(), expandNode);
						return expandNode;
					}
					return p;
				} else {
					return parentNode;
				}
			}
			if (lookupData.charterMap.containsKey(parentNode.token.toLowerCase())) {
				final CharterIndex idx = lookupData.charterMap.get(parentNode.token.toLowerCase());

				// Matched derived index...
				if (idx.getData() instanceof DerivedIndex<?>) {
					final DerivedIndex<?> derivedIndex = (DerivedIndex<?>) idx.getData();
					// Parse the expression
					final IExpression<Node> parse = new RawTreeParser().parse(derivedIndex.getExpression());
					final Node p = parse.evaluate();
					// Expand the parsed tree again if needed,
					@Nullable
					final Node expandNode = expandNode(p, lookupData);
					// return the new sub-parse tree for the expression
					if (expandNode != null) {
						lookupData.expressionCache.put(derivedIndex.getExpression(), expandNode);
						return expandNode;
					}
					return p;
				} else {
					return parentNode;
				}
			}
			if (lookupData.baseFuelMap.containsKey(parentNode.token.toLowerCase())) {
				final BaseFuelIndex idx = lookupData.baseFuelMap.get(parentNode.token.toLowerCase());

				// Matched derived index...
				if (idx.getData() instanceof DerivedIndex<?>) {
					final DerivedIndex<?> derivedIndex = (DerivedIndex<?>) idx.getData();
					// Parse the expression
					final IExpression<Node> parse = new RawTreeParser().parse(derivedIndex.getExpression());
					final Node p = parse.evaluate();
					// Expand the parsed tree again if needed,
					@Nullable
					final Node expandNode = expandNode(p, lookupData);
					// return the new sub-parse tree for the expression
					if (expandNode != null) {
						lookupData.expressionCache.put(derivedIndex.getExpression(), expandNode);
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

	public static Set<NamedIndexContainer<?>> collectIndicies(@NonNull final Node parentNode, final LookupData lookupData) {
		final Set<NamedIndexContainer<?>> s = new HashSet<NamedIndexContainer<?>>();
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

}
