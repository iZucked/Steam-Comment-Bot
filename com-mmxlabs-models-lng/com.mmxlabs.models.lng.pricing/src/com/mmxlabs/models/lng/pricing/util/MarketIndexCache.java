/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;

public class MarketIndexCache extends MMXAdapterImpl {

	private Map<@NonNull PriceIndexType, @NonNull SeriesParser> cache = null;

	private final @NonNull PricingModel pricingModel;

	public MarketIndexCache(final @NonNull PricingModel pricingModel) {
		this.pricingModel = pricingModel;
	}

	@Override
	public void reallyNotifyChanged(final Notification notification) {
		if (notification.isTouch()) {
			return;
		}
		if (notification.getEventType() == Notification.REMOVING_ADAPTER) {
			return;
		}
		clearCache();
	}

	@Override
	protected void missedNotifications(final List<Notification> missed) {
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

	public synchronized void clearCache() {
		cache = null;
	}

	public @NonNull SeriesParser getSeriesParser(final @NonNull PriceIndexType marketIndexType) {

		if (cache == null) {
			buildCache();
		}
		return cache.get(marketIndexType);

	}
}
