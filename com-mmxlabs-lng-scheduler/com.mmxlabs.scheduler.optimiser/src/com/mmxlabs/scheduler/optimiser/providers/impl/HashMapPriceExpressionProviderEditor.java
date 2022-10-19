/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.scheduler.optimiser.providers.IPriceExpressionProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PriceCurveKey;

public class HashMapPriceExpressionProviderEditor implements IPriceExpressionProviderEditor {

	private final Map<@NonNull PriceCurveKey, ISeries> seriesMap = new HashMap<>();

	@Override
	public @NonNull ISeries getExpression(@NonNull PriceCurveKey key) {
		final ISeries series = seriesMap.get(key);
		if (series == null) {
			throw new IllegalStateException("Unknown price curve reference");
		}
		return series;
	}

	@Override
	public void setPriceCurve(@NonNull PriceCurveKey key, @NonNull ISeries curve) {
		seriesMap.put(key, curve);
	}

}
