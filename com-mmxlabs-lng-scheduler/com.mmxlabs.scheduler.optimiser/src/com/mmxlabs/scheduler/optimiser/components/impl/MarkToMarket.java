package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;

/**
 * 
 * Default implementation of {@link IMarkToMarket}
 * 
 * @author Simon Goodall
 * @since 6.0
 */
public class MarkToMarket implements IMarkToMarket {

	private final ILoadPriceCalculator loadPriceCalculator;

	private final ISalesPriceCalculator salesPriceCalculator;

	private final int cv;

	public MarkToMarket(@NonNull final ISalesPriceCalculator salesPriceCalculator) {
		this(null, 0, salesPriceCalculator);
	}

	public MarkToMarket(final ILoadPriceCalculator loadPriceCalculator, final int cv, final ISalesPriceCalculator salesPriceCalculator) {
		this.loadPriceCalculator = loadPriceCalculator;
		this.salesPriceCalculator = salesPriceCalculator;
		this.cv = cv;
	}

	public MarkToMarket(@NonNull final ILoadPriceCalculator loadPriceCalculator, final int cv) {
		this(loadPriceCalculator, cv, null);
	}

	@Override
	@Nullable
	public ILoadPriceCalculator getLoadPriceCalculator() {
		return loadPriceCalculator;
	}

	@Override
	@Nullable
	public ISalesPriceCalculator getSalesPriceCalculator() {
		return salesPriceCalculator;
	}

	@Override
	public int getCVValue() {
		return cv;
	}

}
