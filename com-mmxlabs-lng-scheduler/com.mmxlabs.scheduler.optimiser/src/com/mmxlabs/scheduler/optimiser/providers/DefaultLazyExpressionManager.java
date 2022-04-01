package com.mmxlabs.scheduler.optimiser.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.curves.ILazyCurve;
import com.mmxlabs.common.parser.series.ILazyNamedSeriesContainer;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.NamedSeriesExpression;
import com.mmxlabs.scheduler.optimiser.curves.LazyIntegerIntervalCurve;

public class DefaultLazyExpressionManager implements ILazyExpressionManager, ILazyExpressionManagerEditor, ILazyExpressionManagerContainer {

	@Inject
	private IPriceExpressionProvider priceExpressionProvider;

	@NonNull
	private final Map<@NonNull String, ILazyNamedSeriesContainer> priceCurves = new HashMap<>();
	@NonNull
	private final List<@NonNull ILazyCurve> lazyCurves = new ArrayList<>();
	@NonNull
	private final List<@NonNull LazyIntegerIntervalCurve> lazyIntervalCurves = new ArrayList<>();

	@Override
	public void close() throws Exception {
		clearData();
	}

	@Override
	public void addPriceCurve(@NonNull String name, @NonNull ILazyNamedSeriesContainer lazyNamedSeriesContainer) {
		priceCurves.put(name.toLowerCase(), lazyNamedSeriesContainer);
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
		final ILazyNamedSeriesContainer lazyContainer = priceCurves.get(lowercaseName);
		if (lazyContainer == null) {
			throw new IllegalStateException("Unexpected curve.");
		}
		lazyContainer.setCurve(new NamedSeriesExpression(series));
	}

	@Override
	public void initialiseAllPricingData() {
		priceCurves.entrySet().stream() //
				.filter(entry -> !entry.getValue().isInitialised()) //
				.forEach(entry -> entry.getValue().setCurve(new NamedSeriesExpression(priceExpressionProvider.getExpression(entry.getKey().toLowerCase(), null))));
		lazyCurves.forEach(ILazyCurve::initialise);
		// lazyIntervalCurves are assumed to be initialised by lazy curves
	}

	@Override
	public void clearData() {
		priceCurves.values().stream().forEach(ILazyNamedSeriesContainer::clear);
		lazyCurves.stream().forEach(ILazyCurve::clear);
		lazyIntervalCurves.stream().forEach(LazyIntegerIntervalCurve::clear);
	}

	@Override
	public @NonNull ILazyExpressionManager getExpressionManager() {
		return this;
	}

}
