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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;

public class MarketIndexCache extends EContentAdapter {

	private SoftReference<Map<@NonNull PriceIndexType, @NonNull SeriesParser>> cache = new SoftReference<>(null);

	private SoftReference<Map<@NonNull NamedIndexContainer<?>, @Nullable YearMonth>> earlyDateCache = new SoftReference<>(null);

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
}
