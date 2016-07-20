/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

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

	private Map<@NonNull PriceIndexType, @NonNull SeriesParser> cache = null;

	private Map<@NonNull NamedIndexContainer<?>, @Nullable YearMonth> earlyDateCache = null;

	private final @NonNull PricingModel pricingModel;

	public MarketIndexCache(final @NonNull PricingModel pricingModel) {
		this.pricingModel = pricingModel;
	}

	@Override
	public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);

		if (notification.isTouch()) {
			return;
		}
		if (notification.getEventType() == Notification.REMOVING_ADAPTER) {
			return;
		}
		clearCache();
	}

	public synchronized void buildCache() {
		if (cache == null) {
			cache = new HashMap<>();

			cache.put(PriceIndexType.COMMODITY, PriceIndexUtils.getParserFor(pricingModel, PriceIndexType.COMMODITY));
			cache.put(PriceIndexType.CHARTER, PriceIndexUtils.getParserFor(pricingModel, PriceIndexType.CHARTER));
			cache.put(PriceIndexType.BUNKERS, PriceIndexUtils.getParserFor(pricingModel, PriceIndexType.BUNKERS));
		}
	}

	public synchronized void buildDateCache() {
		if (earlyDateCache == null) {
			earlyDateCache = new HashMap<>();

			final Function<NamedIndexContainer<?>, @Nullable YearMonth> finder = (curve) -> {
				final Index<?> data = curve.getData();
				if (data instanceof DataIndex<?>) {
					final DataIndex<?> indexData = (DataIndex<?>) data;
					Optional<?> min = indexData.getPoints().stream() //
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

			pricingModel.getCommodityIndices().forEach(c -> earlyDateCache.put(c, finder.apply(c)));
			pricingModel.getCharterIndices().forEach(c -> earlyDateCache.put(c, finder.apply(c)));
			pricingModel.getBaseFuelPrices().forEach(c -> earlyDateCache.put(c, finder.apply(c)));
		}
	}

	public synchronized void clearCache() {
		cache = null;
		earlyDateCache = null;
	}

	public @NonNull SeriesParser getSeriesParser(final @NonNull PriceIndexType marketIndexType) {

		if (cache == null) {
			buildCache();
		}
		return cache.get(marketIndexType);

	}

	public @Nullable YearMonth getEarliestDate(final @NonNull NamedIndexContainer<?> index) {
		if (earlyDateCache == null) {
			buildDateCache();
		}
		return earlyDateCache.getOrDefault(index, null);
	}
}
