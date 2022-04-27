/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.curves.ILazyCurve;
import com.mmxlabs.common.parser.series.ILazyExpressionContainer;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.NamedSeriesExpression;
import com.mmxlabs.scheduler.optimiser.curves.LazyIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.providers.ILazyExpressionManager;
import com.mmxlabs.scheduler.optimiser.providers.ILazyExpressionManagerContainer;
import com.mmxlabs.scheduler.optimiser.providers.ILazyExpressionManagerEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPriceExpressionProvider;

public class DefaultLazyExpressionManager implements ILazyExpressionManager, ILazyExpressionManagerEditor, ILazyExpressionManagerContainer {

	@NonNull
	private final Map<@NonNull String, ILazyExpressionContainer> priceCurves = new HashMap<>();
	@NonNull
	private final List<@NonNull ILazyCurve> lazyCurves = new ArrayList<>();
	@NonNull
	private final List<@NonNull LazyIntegerIntervalCurve> lazyIntervalCurves = new ArrayList<>();

	@Override
	public void close() throws Exception {
		clearData();
	}

	@Override
	public void addPriceCurve(@NonNull String name, @NonNull ILazyExpressionContainer lazyExpressionContainer) {
		priceCurves.put(name.toLowerCase(), lazyExpressionContainer);
	}

	@Override
	public void addLazyCurve(@NonNull ILazyCurve lazyCurve) {
		lazyCurves.add(lazyCurve);
	}

	@Override
	public void addLazyIntervalCurve(@NonNull LazyIntegerIntervalCurve lazyIntervalCurve) {
		lazyIntervalCurves.add(lazyIntervalCurve);
	}

	@Override
	public void setPriceCurve(@NonNull String name, @NonNull ISeries series) {
		final String lowercaseName = name.toLowerCase();
		final ILazyExpressionContainer lazyContainer = priceCurves.get(lowercaseName);
		if (lazyContainer == null) {
			throw new IllegalStateException("Unexpected curve.");
		}
		lazyContainer.setExpression(new NamedSeriesExpression(series));
	}

	@Override
	public void initialiseAllPricingData() {
		if (!priceCurves.values().stream().allMatch(ILazyExpressionContainer::canGet)) {
			throw new IllegalStateException("All lazy curves must be set");
		}
		lazyCurves.forEach(ILazyCurve::initialise);
		// lazyIntervalCurves are assumed to be initialised by lazy curves
	}

	@Override
	public void clearData() {
		priceCurves.values().stream().forEach(ILazyExpressionContainer::clear);
		lazyCurves.stream().forEach(ILazyCurve::clear);
		lazyIntervalCurves.stream().forEach(LazyIntegerIntervalCurve::clear);
	}

	@Override
	public @NonNull ILazyExpressionManager getExpressionManager() {
		return this;
	}

}
